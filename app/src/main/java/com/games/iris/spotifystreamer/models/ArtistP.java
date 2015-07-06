package com.games.iris.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * @author Irving
 * @since 29/06/2015
 */
public class ArtistP implements Parcelable {
    public static final String DEFAULT_IMAGE = "default";

    private String id;
    private String name;
    private String image;

    public ArtistP(@NonNull Artist spotifyArtist) {
        this.id = spotifyArtist.id;
        this.name = spotifyArtist.name;
        if (spotifyArtist.images.isEmpty()) {
            this.image = DEFAULT_IMAGE;
        } else {
            this.image = spotifyArtist.images.get(0).url;
        }
    }

    private ArtistP(Parcel input) {
        readFromParcel(input);
    }

    public static final Parcelable.Creator<ArtistP> CREATOR =
        new Parcelable.Creator<ArtistP>() {

            @Override
            public ArtistP createFromParcel(Parcel source) {
                return new ArtistP(source);
            }

            @Override
            public ArtistP[] newArray(int size) {
                return new ArtistP[size];
            }
        };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
    }

    private void readFromParcel(Parcel input) {
        id = input.readString();
        name = input.readString();
        image = input.readString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
