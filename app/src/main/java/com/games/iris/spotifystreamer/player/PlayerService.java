package com.games.iris.spotifystreamer.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener{

    /**
     * Message type: register the activity's messenger for receiving responses
     * from Service. We assume only one activity can be registered at one time.
     */
    public static final int MESSAGE_TYPE_REGISTER = 1;
    public static final int ACTION_PLAY = 2;
    public static final int ACTION_PAUSE = 3;
    public static final int ACTION_STOP = 4;
    public static final int ACTION_BACK = 5;
    public static final int ACTION_NEXT = 6;
    public static final int ACTION_SEEK_TO = 7;

    /**
     * Messenger used for handling incoming messages.
     */
    private final Messenger messenger = new Messenger(new IncomingHandler());

    /**
     * Messenger on Activity side, used for sending messages back to Activity
     */
    private Messenger responseMessenger = null;

    private MediaPlayer mediaPlayer;


    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (mediaPlayer == null)
//        {
//            initPlayer();
//        }
//        if (Constants.ACTION_PLAY.equals(intent.getAction()))
//        {
//            TrackP trackP = intent.getParcelableExtra(Constants.EXTRA_TRACK_PARCELABLE);
//            if (trackP != null)
//            {
//                playMP(trackP);
//            }
//        }
//        else if (Constants.ACTION_PAUSE.equals(intent.getAction()))
//        {
//            pauseMP();
//        }
//        else if (Constants.ACTION_STOP.equals(intent.getAction()))
//        {
//            stopMP();
//            stopSelf();
//            return START_NOT_STICKY;
//        }
//        else if (Constants.ACTION_SEEK_TO.equals(intent.getAction()))
//        {
//            int seekValue = intent.getIntExtra(Constants.EXTRA_SEEK_TO, 0);
//            seekToMP(seekValue);
//        }
//        else {
//            stopSelf();
//            return START_NOT_STICKY;
//        }
//        return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlayer()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
    }

    private void playMP(TrackP trackP)
    {
        try {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(trackP.getPreviewUrl());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseMP()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    private void stopMP()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void seekToMP(int value)
    {
        mediaPlayer.seekTo(value);
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }


    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what)
            {
            case MESSAGE_TYPE_REGISTER:
                responseMessenger = msg.replyTo;
                break;
            case ACTION_PLAY:
                TrackP trackP = bundle.getParcelable(Constants.EXTRA_TRACK_PARCELABLE);
                if (trackP != null)
                {
                    playMP(trackP);
                }
                break;
            default:
                super.handleMessage(msg);
            }

        }
    }
}
