package com.games.iris.spotifystreamer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.iris.spotifystreamer.R;
import com.games.iris.spotifystreamer.models.ArtistP;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Irving
 * @since 18/06/2015
 */
public class ArtistsArrayAdapter extends ArrayAdapter<ArtistP> {

    private LayoutInflater inflater;

    public ArtistsArrayAdapter(Context context, List<ArtistP> objects) {
        super(context, R.layout.list_item_artist, objects);

        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null)
        {
            rowView = inflater.inflate(R.layout.list_item_artist, parent, false);

            // Configure view Holder
            ArtistViewHolder viewHolder = new ArtistViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.title);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        }

        ArtistViewHolder holder = (ArtistViewHolder) rowView.getTag();
        ArtistP artist = getItem(position);
        holder.name.setText(artist.getName());
        Picasso.with(getContext()).load(artist.getImage()).into(holder.icon);
        return rowView;
    }

    class ArtistViewHolder{
        ImageView icon;
        TextView name;
    }
}
