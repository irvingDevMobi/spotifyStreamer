package com.games.iris.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;
import com.squareup.picasso.Picasso;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the {@link
 * PlayerFragment.OnFragmentInteractionListener} interface to handle interaction events. Use the {@link PlayerFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    private TrackP track;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param track
     *     Object when track information
     * @return A new instance of fragment PlayerFragment.
     */
    public static PlayerFragment newInstance(TrackP track) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_TRACK_PARCELABLE, track);
        fragment.setArguments(args);
        return fragment;
    }
    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            track = getArguments().getParcelable(Constants.EXTRA_TRACK_PARCELABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        TextView artistTV = (TextView) root.findViewById(R.id.player_artist_tv);
        TextView albumTV = (TextView) root.findViewById(R.id.player_album_tv);
        ImageView albumIV = (ImageView) root.findViewById(R.id.player_album_iv);
        TextView songTV = (TextView) root.findViewById(R.id.player_song_tv);

        if (track != null)
        {
            artistTV.setText(track.getArtist());
            albumTV.setText(track.getAlbum());
            Picasso.with(getActivity()).load(track.getUrlImage()).into(albumIV);
            songTV.setText(track.getTitle());
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url = track.getPreviewUrl();

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //    @Override
    //    public void onAttach(Activity activity) {
    //        super.onAttach(activity);
    //        try {
    //            mListener = (OnFragmentInteractionListener) activity;
    //        } catch (ClassCastException e) {
    //            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
    //        }
    //    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an interaction in this fragment to be
     * communicated to the activity and potentially other fragments contained in that activity.
     * <p/>
     * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating
     * with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
