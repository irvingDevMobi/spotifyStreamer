package com.games.iris.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.games.iris.spotifystreamer.Adapters.TrackArrayAdapter;

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
 */
public class TopTracksFragment extends ListFragment {

    public static String ARG_SPOTIFY_ID = "spotifyId";
    private TrackArrayAdapter arrayAdapter;
    private ActionBar actionBar;

    public static TopTracksFragment newInstance(String spotifyId) {
        TopTracksFragment fragment = new TopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SPOTIFY_ID, spotifyId);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        arrayAdapter = new TrackArrayAdapter(getActivity(), new ArrayList<Track>());
        setListAdapter(arrayAdapter);

        String spotifyId = getArguments().getString(ARG_SPOTIFY_ID);
        new TopTracksAsyncTask().execute(spotifyId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
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
                    arrayAdapter.add(track);
                }
            }
        }
    }

}
