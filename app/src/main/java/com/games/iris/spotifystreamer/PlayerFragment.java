package com.games.iris.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;
import com.games.iris.spotifystreamer.player.PlayerService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the {@link
 * PlayerFragment.OnFragmentInteractionListener} interface to handle interaction events. Use the {@link PlayerFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class PlayerFragment extends Fragment{

    private OnFragmentInteractionListener mListener;

    private TextView artistTV;
    private TextView albumTV;
    private ImageView albumIV;
    private TextView songTV;

    private WifiManager.WifiLock wifiLock;
    private List<TrackP> tracksList;
    private Intent intentService;

    private int currentIndex;
    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param tracksList
     *     List with all Top Tracks
     * @return A new instance of fragment PlayerFragment.
     */
    public static PlayerFragment newInstance(int index, ArrayList<TrackP> tracksList) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_TRACK_INDEX, index);
        args.putParcelableArrayList(Constants.EXTRA_TRACKS_LIST_PARCELABLE, tracksList);
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
            tracksList = getArguments().getParcelableArrayList(Constants.EXTRA_TRACKS_LIST_PARCELABLE);
            currentIndex = getArguments().getInt(Constants.EXTRA_TRACK_INDEX, 0);
        }
        intentService = new Intent(getActivity(), PlayerService.class);
        wifiLock = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE))
            .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        artistTV = (TextView) root.findViewById(R.id.player_artist_tv);
        albumTV = (TextView) root.findViewById(R.id.player_album_tv);
        albumIV = (ImageView) root.findViewById(R.id.player_album_iv);
        songTV = (TextView) root.findViewById(R.id.player_song_tv);

        playTrack(currentIndex);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        String url = track.getPreviewUrl();
//        try {
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepare(); // might take long! (for buffering, etc)
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void playTrack(int index)
    {
        TrackP track = tracksList.get(index);
        if (track != null) {
            setTrackValues(track);
            intentService.putExtra(Constants.EXTRA_TRACK_PARCELABLE, track);
            intentService.setAction(Constants.ACTION_PLAY);
            getActivity().startService(intentService);
        }


    }

    private void setTrackValues(@NonNull TrackP track)
    {
        artistTV.setText(track.getArtist());
        albumTV.setText(track.getAlbum());
        Picasso.with(getActivity()).load(track.getUrlImage()).into(albumIV);
        songTV.setText(track.getTitle());
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
        wifiLock.release();
//        getActivity().stopService(intentService);
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
