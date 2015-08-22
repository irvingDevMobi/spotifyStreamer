package com.games.iris.spotifystreamer.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.games.iris.spotifystreamer.Util.Constants;
import com.games.iris.spotifystreamer.models.TrackP;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener{

    private MediaPlayer mediaPlayer;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return processIntent(intent);
    }

    private void initPlayer()
        {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
    }

    private int processIntent(Intent intent)
    {
        if (Constants.ACTION_PLAY.equals(intent.getAction()))
        {
            TrackP trackP = intent.getParcelableExtra(Constants.EXTRA_TRACK_PARCELABLE);
            if (trackP != null)
            {
                playMP(trackP);
            }
        }
        else if (Constants.ACTION_PAUSE.equals(intent.getAction()))
        {
            pauseMP();
        }
        else if (Constants.ACTION_SEEK_TO.equals(intent.getAction()))
        {
            int seekValue = intent.getIntExtra(Constants.EXTRA_SEEK_TO, 0);
            seekToMP(seekValue);
        }
        else {
            // CASE ACTION_STOP and another
            stopMP();
            stopSelf();
            return START_NOT_STICKY;
        }
        return START_STICKY;

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

    public void pauseMP()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void stopMP()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void seekToMP(int value)
    {
        mediaPlayer.seekTo(value);
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }
}
