package com.games.iris.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.games.iris.spotifystreamer.Adapters.TrackArrayAdapter;
import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * http://developer.android.com/guide/components/fragments.html
 */
public class TopTracksFragment extends ListFragment {

    private static final String BUNDLE_KEY_TRACK_LIST = "trackPList";
    public static String ARG_SPOTIFY_ID = "spotifyId";
    public static String ARG_ARTIST_NAME = "artistName";

    private TopTracksFragmentInteractionListener interactionListener;

    private ActionBar actionBar;

    private TrackArrayAdapter arrayAdapter;
    private ArrayList<TrackP> trackListValues;

    private boolean isTablet;

    public static TopTracksFragment newInstance(String spotifyId, String artistName) {
        TopTracksFragment fragment = new TopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SPOTIFY_ID, spotifyId);
        args.putString(ARG_ARTIST_NAME, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public TopTracksFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interactionListener = (TopTracksFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                         + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().findViewById(R.id.fragment_top_tracks) == null)
        {
            setHasOptionsMenu(true);
            isTablet = true;
        }
        /**
         TODO: I have a question
         Why when a rotate the phone, The Fragment.OnCreate() method is called after
         that Activity.OnCreate() method?
         **/
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isTablet) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setSubtitle(getArguments().getString(ARG_ARTIST_NAME));
            }
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_TRACK_LIST)) {
            trackListValues = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_TRACK_LIST);
            if (trackListValues != null && !trackListValues.isEmpty()) {
                arrayAdapter = new TrackArrayAdapter(getActivity(), trackListValues);
                setListAdapter(arrayAdapter);
            } else {
                initAndLaunch();
            }

        } else {
            initAndLaunch();
        }

    }

    private void initAndLaunch() {
        trackListValues = new ArrayList<>();
        arrayAdapter = new TrackArrayAdapter(getActivity(), trackListValues);
        setListAdapter(arrayAdapter);
        String spotifyId = getArguments().getString(ARG_SPOTIFY_ID);
        if (spotifyId != null)
        {
            new TopTracksAsyncTask().execute(spotifyId);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this.getActivity(), PlayerActivity.class);
        intent.putExtra(Constants.EXTRA_TRACK_INDEX, position);
        intent.putExtra(Constants.EXTRA_TRACKS_LIST_PARCELABLE, trackListValues);
        intent.putExtra(Constants.EXTRA_IS_TABLET, isTablet);
        startActivity(intent);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_KEY_TRACK_LIST, trackListValues);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isTablet) {
            if (item.getItemId() == android.R.id.home) {
                interactionListener.onBackPressedFragment();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class TopTracksAsyncTask extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {
            if (params.length > 0 && params[0] != null) {
                SpotifyApi spotifyApi = new SpotifyApi();
                SpotifyService spotify = spotifyApi.getService();
                Map<String, Object> map = new HashMap<>();
                map.put("country", Locale.getDefault().getCountry());
                return spotify.getArtistTopTrack(params[0], map);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            super.onPostExecute(tracks);
            if (!tracks.tracks.isEmpty()) {
                arrayAdapter.clear();
                for (Track track : tracks.tracks) {
                    arrayAdapter.add(new TrackP(track));
                }
            }
        }
    }

    public interface TopTracksFragmentInteractionListener {
        void onBackPressedFragment();
    }
}
