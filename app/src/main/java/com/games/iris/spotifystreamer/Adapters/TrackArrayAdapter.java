package com.games.iris.spotifystreamer.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import kaaes.spotify.webapi.android.models.Track;

/**
 * @author Irving
 * @since 22/06/2015
 */
public class TrackArrayAdapter extends ArrayAdapter<Track> {

    public TrackArrayAdapter(Context context, int resource,
                             Track[] objects) {
        super(context, resource, objects);
    }
}
