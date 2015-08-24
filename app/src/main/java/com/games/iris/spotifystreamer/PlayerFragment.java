package com.games.iris.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;
import com.games.iris.spotifystreamer.player.PlayerService;
import com.games.iris.spotifystreamer.player.PlayerStates;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener{

    public static final int IS_READY_RESULT_CODE = 1;
    public static final int IS_PLAYING_RESULT_CODE = 2;
    public static final int SEEK_TO_RESULT_CODE = 3;
    public static final int ON_COMPLETE_TRACK_RESULT_CODE = 4;

    private TextView artistTV;
    private TextView albumTV;
    private ImageView albumIV;
    private TextView songTV;
    private SeekBar seekBar;
    private TextView leftTV;
    private TextView rightTV;
    private ImageButton playButton;
    private ImageButton backButton;
    private ImageButton nextButton;

    private PlayerResultReceiver resultReceiver;

    private WifiManager.WifiLock wifiLock;
    private List<TrackP> tracksList;
    private Intent intentService;

    private int currentIndex;
    private int currentDuration;
    private int playerState;

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
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.EXTRA_TRACK_INDEX))
        {
            currentIndex = savedInstanceState.getInt(Constants.EXTRA_TRACK_INDEX);
        }
        intentService = new Intent(getActivity(), PlayerService.class);
        resultReceiver = new PlayerResultReceiver(null);
        playerState = PlayerStates.STOP;
        intentService.putExtra(Constants.EXTRA_RESULT_RECEIVER, resultReceiver);
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
        seekBar = (SeekBar) root.findViewById(R.id.player_seekBar);
        leftTV = (TextView) root.findViewById(R.id.player_left_time_tv);
        rightTV = (TextView) root.findViewById(R.id.player_right_time_tv);
        playButton = (ImageButton) root.findViewById(R.id.player_play_button);
        backButton = (ImageButton) root.findViewById(R.id.player_back_button);
        nextButton = (ImageButton) root.findViewById(R.id.player_next_button);

        playButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                intentService.setAction(Constants.ACTION_SEEK_TO);
                intentService.putExtra(Constants.EXTRA_SEEK_TO, seekBar.getProgress());
                getActivity().startService(intentService);
            }
        });

        String titleSaved =  tracksList.get(currentIndex).getTitle();
        if (savedInstanceState != null
            && savedInstanceState.containsKey(Constants.EXTRA_INSTANCE_PLAYER_STATE))
        {
            playerState = savedInstanceState.getInt(Constants.EXTRA_INSTANCE_PLAYER_STATE);
            titleSaved = savedInstanceState.getString(Constants.EXTRA_INSTANCE_TRACK_NAME);
            currentDuration = savedInstanceState.getInt(Constants.EXTRA_INSTANCE_TRACK_DURATION);
        }
        if ((playerState == PlayerStates.PAUSE || playerState == PlayerStates.PLAYING)
            && titleSaved != null && titleSaved.equals(tracksList.get(currentIndex).getTitle()))
        {
            setTrackValues(tracksList.get(currentIndex));
            initSeekBarValues(currentDuration);
            if (playerState == PlayerStates.PLAYING)
            {
                intentService.setAction(Constants.ACTION_UPDATE_SEEK_TO);
                getActivity().startService(intentService);
                playButton.setImageResource(android.R.drawable.ic_media_pause);
            }
        } else
        {
            playTrack(currentIndex);
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.EXTRA_INSTANCE_PLAYER_STATE, playerState);
        outState.putInt(Constants.EXTRA_TRACK_INDEX, currentIndex);
        outState.putString(Constants.EXTRA_INSTANCE_TRACK_NAME, tracksList.get(currentIndex).getTitle());
        outState.putInt(Constants.EXTRA_INSTANCE_TRACK_DURATION, currentDuration);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        wifiLock.release();
        if (playerState == PlayerStates.STOP)
        {
            getActivity().stopService(intentService);
        }
    }

    @Override
    public void onClick(View v)
    {
        int idView = v.getId();
        switch (idView)
        {
        case R.id.player_play_button:
            onClickPlayButton();
            break;
        case R.id.player_back_button:
            onClickBackButton();
            break;
        case R.id.player_next_button:
            onClickNextButton();
        }
    }

    private void playTrack(int index)
    {
        TrackP track = tracksList.get(index);
        if (track != null) {
            disableControl();
            currentDuration = 0;
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

    private void enableControl()
    {
        seekBar.setEnabled(true);
        playButton.setEnabled(true);
        playButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void disableControl()
    {
        seekBar.setEnabled(false);
        playButton.setEnabled(false);
    }

    private void initSeekBarValues(int duration)
    {
        seekBar.setMax(duration);
        putTimeText(leftTV, 0);
        putTimeText(rightTV, duration);
    }

    private void onClickNextButton() {
        if (currentIndex < tracksList.size() - 1)
        {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        playTrack(currentIndex);
    }

    private void onClickBackButton() {
        if (currentIndex == 0)
        {
            currentIndex = tracksList.size() - 1;
        } else {
            currentIndex--;
        }
        playTrack(currentIndex);
    }

    private void onClickPlayButton() {
        intentService.setAction(Constants.ACTION_PAUSE);
        getActivity().startService(intentService);
    }


    private void putTimeText(TextView textView, int time)
    {
        int totalSeconds = time / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        textView.setText(minutes + ":");
        if (seconds == 0)
        {
            textView.append("00");
        }
        else if (seconds < 10)
        {
            textView.append("0" + seconds);
        }
        else{
            textView.append("" + seconds);
        }
    }


    class PlayerResultReceiver extends ResultReceiver
    {

        /**
         * Create a new ResultReceive to receive results.  Your {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler null
         */
        public PlayerResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == IS_READY_RESULT_CODE)
            {
                int duration = resultData.getInt(Constants.EXTRA_TRACK_LONG);
                enableControl();
                currentDuration = duration;
                initSeekBarValues(duration);
                playerState = PlayerStates.PLAYING;
                Log.v(Constants.TAG_LOG, "" + duration);
            }
            else if (resultCode == IS_PLAYING_RESULT_CODE)
            {
                boolean isPlaying = resultData.getBoolean(Constants.EXTRA_TRACK_IS_PLAYING);
                if (isPlaying)
                {
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    playerState = PlayerStates.PLAYING;
                } else {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    playerState = PlayerStates.PAUSE;
                }
            }
            else if (resultCode == SEEK_TO_RESULT_CODE)
            {
                final int seekValue = resultData.getInt(Constants.EXTRA_SEEK_TO);
                if (isVisible())
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(seekValue);
                            putTimeText(leftTV, seekValue);
                        }
                    });
                }
            }
            else if (resultCode == ON_COMPLETE_TRACK_RESULT_CODE)
            {
                if (isVisible())
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onClickNextButton();
                        }
                    });
                } else {
                    // TODO:
                    playerState = PlayerStates.STOP;
                }
            }
        }
    }


}
