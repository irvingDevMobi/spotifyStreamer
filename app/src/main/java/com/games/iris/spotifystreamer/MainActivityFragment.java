package com.games.iris.spotifystreamer;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.games.iris.spotifystreamer.Adapters.ArtistsArrayAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArtistsArrayAdapter arrayAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final EditText editText = (EditText) rootView.findViewById(R.id.input_main_editText);
        ListView artistsListView = (ListView) rootView.findViewById(R.id.results_listView);
        arrayAdapter = new ArtistsArrayAdapter(getActivity(), new ArrayList<Artist>());
        artistsListView.setAdapter(arrayAdapter);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    launchSearch(editText.getText().toString());
                }
                return false;
            }
        });
        return rootView;
    }

    private void launchSearch(@NonNull String query)
    {
        if (query.length() > 0)
        {
            new SearchArtistsAsyncTask().execute(query);
        }
    }

    class SearchArtistsAsyncTask extends AsyncTask<String, Void, Pager<Artist>>
    {

        @Override
        protected Pager<Artist> doInBackground(String... params) {
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotify = spotifyApi.getService();
            ArtistsPager artistsPager = spotify.searchArtists(params[0]);
            if (artistsPager != null && artistsPager.artists != null)
            {
                return artistsPager.artists;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Pager<Artist> pager) {
            super.onPostExecute(pager);
            if (pager != null && !pager.items.isEmpty())
            {
                arrayAdapter.clear();
                for (Artist artist : pager.items)
                {
                    arrayAdapter.add(artist);
                }
            }
            else
            {
                Toast.makeText(getActivity(), getString(R.string.label_dont_find_artist),
                               Toast.LENGTH_SHORT).show();
            }
        }
    }
}
