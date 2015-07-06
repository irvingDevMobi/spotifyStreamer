package com.games.iris.spotifystreamer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.iris.spotifystreamer.R;
import com.games.iris.spotifystreamer.models.TrackP;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Irving
 * @since 22/06/2015
 */
public class TrackArrayAdapter extends ArrayAdapter<TrackP> {

    private LayoutInflater inflater;

    public TrackArrayAdapter(Context context, List<TrackP> values) {
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

        TrackP track = getItem(position);
        TrackViewHolder holder = (TrackViewHolder) rowView.getTag();
        holder.title.setText(track.getTitle());
        holder.album.setText(track.getAlbum());
        String urlImage = track.getUrlImage();
        Picasso.with(getContext()).load(urlImage).into(holder.icon);
        return rowView;
    }

    class TrackViewHolder {
        ImageView icon;
        TextView title;
        TextView album;
    }
}
