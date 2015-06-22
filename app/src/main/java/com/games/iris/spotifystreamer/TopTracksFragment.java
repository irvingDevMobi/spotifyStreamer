package com.games.iris.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 */
public class TopTracksFragment extends ListFragment {

    public static String ARG_SPOTIFY_ID = "spotifyId";

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

        String spotifyId = getArguments().getString(ARG_SPOTIFY_ID);
        if (spotifyId != null) {
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotify = spotifyApi.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", Locale.getDefault().getCountry());
            spotify.getArtistTopTrack(spotifyId, map, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });        }
    }

}
