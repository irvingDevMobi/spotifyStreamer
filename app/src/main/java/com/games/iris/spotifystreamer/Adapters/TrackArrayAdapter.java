package com.games.iris.spotifystreamer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.iris.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * @author Irving
 * @since 22/06/2015
 */
public class TrackArrayAdapter extends ArrayAdapter<Track> {

    private LayoutInflater inflater;

    public TrackArrayAdapter(Context context, List<Track> values) {
        super(context, R.layout.list_item_track, values);

        inflater = LayoutInflater.from(getContext());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item_track, parent, false);

            // Configure ViewHolder
            TrackViewHolder viewHolder = new TrackViewHolder();
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.album = (TextView) rowView.findViewById(R.id.subtitle);
            rowView.setTag(viewHolder);
        }

        Track track = getItem(position);
        TrackViewHolder holder = (TrackViewHolder) rowView.getTag();
        holder.title.setText(track.name);
        if (track.album != null) {
            holder.album.setText(track.album.name);
            if (!track.album.images.isEmpty()) {
                String urlImage = track.album.images.get(0).url;
                Picasso.with(getContext()).load(urlImage).into(holder.icon);
            }
        }
        return rowView;
    }

    class TrackViewHolder {
        ImageView icon;
        TextView title;
        TextView album;
    }
}
