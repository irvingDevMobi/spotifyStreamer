package com.games.iris.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import kaaes.spotify.webapi.android.models.Track;

/**
 * @author Irving
 * @since 02/07/2015
 */
public class TrackP implements Parcelable {

    private String id;
    private String title;
    private String album;
    private String urlImage;

    public TrackP(@NonNull Track spotifyTrack) {
        this.id = spotifyTrack.id;
        this.title = spotifyTrack.name;
        this.album = spotifyTrack.album.name;
        if (!spotifyTrack.album.images.isEmpty()) {
            this.urlImage = spotifyTrack.album.images.get(0).url;
        }
    }

    private TrackP(Parcel input) {
        readFromParcel(input);
    }

    public static final Parcelable.Creator<TrackP> CREATOR =
        new Parcelable.Creator<TrackP>() {

            @Override
            public TrackP createFromParcel(Parcel source) {
                return new TrackP(source);
            }

            @Override
            public TrackP[] newArray(int size) {
                return new TrackP[size];
            }
        };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(urlImage);
    }

    private void readFromParcel(Parcel input) {
        id = input.readString();
        title = input.readString();
        album = input.readString();
        urlImage = input.readString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
