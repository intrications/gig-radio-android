package com.getgigradio.gigradio.playback;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.getgigradio.gigradio.BuildConfig;
import com.getgigradio.gigradio.activity.MainActivity;
import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.event.BufferingEvent;
import com.getgigradio.gigradio.event.NewSongEvent;
import com.getgigradio.gigradio.event.NoSongPlayingEvent;
import com.getgigradio.gigradio.event.PausingEvent;
import com.getgigradio.gigradio.event.PlayingEvent;
import com.getgigradio.gigradio.event.SeekBarMoveEvent;
import com.getgigradio.gigradio.model.soundcloud.track.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {

    public static final String START_NEW_TRACK = "play";
    public static final String START_NEW_ARRAY_OF_SONG_OBJECT_IDS = "playNewArrayOfSongObjectIds";
    public static final String START_PLAY_PAUSE = "playPause";
    public static final String START_PAUSE = "pause";
    public static final String START_STOP = "stop";
    public static final String START_SKIP_NEXT = "next";
    public static final String START_SKIP_PREVIOUS = "previous";
    public static final String START_SEARCH_TO = "search_to";
    private static final String PLAYLIST_CURRENT_POSITION = "playlistCurrentPosition";
    private static final String ARRAY_OF_SONG_OBJECT_IDS = "arrayOfSongObjectIds";
    boolean isPlaying;
    private MediaPlayer mediaPlayer;

    public List<Track> currentPlaylistSongs;

    private static int notificationID = 579; // just a number
    private WifiManager.WifiLock wifiLock;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    private int currentPosition;
    private AudioManager am;
    private BroadcastReceiver myNoisyAudioStreamReceiver = new AudioBecomingNoisyReceiver();
    private BroadcastReceiver remoteControlReceiver = new RemoteControlReceiver();
    private ComponentName eventReceiver;
    private RemoteControlClient mRemoteControlClient;
    private Random random;
    //    private SavedPreferences preferences;
    private Notification notification;
    private boolean buffering;

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.setVolume(0.1f, 0.1f);
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause playback
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mediaPlayer.setVolume(1.0f, 1.0f);
            startPlayback();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)

        {
//        am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
//        am.abandonAudioFocus(afChangeListener);
            // Stop playback
            pausePlayback();
        }
    }

    public static void stopItAll(Context context) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        context.stopService(intent);
    }

    public class LocalBinder extends Binder {
        MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }

    }

    public void seekMusicTo(int progress) {
        if (mediaPlayer != null) {
//            EventBus.getDefault().post(new BufferingEvent());
            mediaPlayer.seekTo(MediaPlayerUtils.progressToTimer(progress, mediaPlayer.getDuration()));
        }
    }

    public void playPauseButtonPressed() {
        if (!buffering) {
            if (mediaPlayer.isPlaying()) {
                pausePlayback();
            } else {
                startPlayback();
            }
        }
    }

    private void startPlayback() {
        if (isPlaying) {
            mediaPlayer.start();
            EventBus.getDefault().post(new PlayingEvent());
            mRemoteControlClient.setPlaybackState(
                    RemoteControlClient.PLAYSTATE_PLAYING);
        }
    }

    private void pausePlayback() {
        mediaPlayer.pause();
        EventBus.getDefault().post(new PausingEvent());
        mRemoteControlClient.setPlaybackState(
                RemoteControlClient.PLAYSTATE_PAUSED);

    }

    public void nextButtonPressed() {
//        if (currentPlaylistSongs != null) {
//            if (preferences.repeatMode() == 1) {
//                play();
//                return;
//            }
//            if (preferences.shuffleModeEnabled()) {
//                currentPosition = random.nextInt(currentPlaylistSongs.size());
//                play();
//                return;
//            }
//            if (currentPosition < currentPlaylistSongs.size() - 1) {
//                currentPosition++;
//                play();
//                return;
//            } else if (preferences.repeatMode() == 2) {
//                currentPosition = 0;
//                play();
//                return;
//            } else {
//                // Playing last song in playlist
//                return;
//            }
//        }
//        stop();
    }

    public void previousButtonPressed() {
        if (currentPlaylistSongs != null) {
            if (mediaPlayer.getCurrentPosition() < 5000 && currentPosition > 0) {
                currentPosition--;
                play();
            } else {
                mediaPlayer.seekTo(0);
            }
        }
    }

    public Track getCurrentSong() {
        if (isPlaying && currentPlaylistSongs != null) {
            return currentPlaylistSongs.get(currentPosition);
        } else {
            return null;
        }
    }

    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static void startWithTrack(Context context, Track track) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.START_NEW_TRACK);
        intent.putExtra("track", track);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnPreparedListener(this);

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        eventReceiver = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        am.registerMediaButtonEventReceiver(eventReceiver);
        registerReceiver(myNoisyAudioStreamReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(eventReceiver);
        PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
        mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
        mRemoteControlClient.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS | RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        am.registerRemoteControlClient(mRemoteControlClient);

        random = new Random();
//        preferences = Esperandro.getPreferences(SavedPreferences.class, this);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (START_NEW_TRACK.equals(intent.getAction())) {
            mediaPlayer.pause();
            EventBus.getDefault().post(new NoSongPlayingEvent());
            currentPosition = intent.getIntExtra(PLAYLIST_CURRENT_POSITION, 0);
            MediaPlayerService.this.currentPlaylistSongs = new ArrayList<>();
            currentPlaylistSongs.add(intent.getParcelableExtra("track"));
            play();
        } else if (START_PAUSE.equals(intent.getAction())) {
            pausePlayback();
        } else if (START_PLAY_PAUSE.equals(intent.getAction())) {
            playPauseButtonPressed();
        } else if (START_SKIP_NEXT.equals(intent.getAction())) {
            nextButtonPressed();
        } else if (START_SKIP_PREVIOUS.equals(intent.getAction())) {
            previousButtonPressed();
        } else if (START_STOP.equals(intent.getAction())) {
            stop();
        }

//        // TODO should this be STICKY? Probably not.
        return Service.START_NOT_STICKY;
    }

    private void play() {

        isPlaying = true;

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        if (currentPosition < 0) {
            currentPosition = 0;
        }

        // Request audio focus for playback
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            EventBus.getDefault().post(new BufferingEvent());

            buffering = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mRemoteControlClient.setPlaybackState(
                        RemoteControlClient.PLAYSTATE_BUFFERING);
            }
            EventBus.getDefault().post(new PlayingEvent());

            Track currentSong = currentPlaylistSongs.get(currentPosition);

            EventBus.getDefault().post(new NewSongEvent(currentSong));

            Intent stopMusicIntent = new Intent(this, MediaPlayerService.class);
            stopMusicIntent.setAction(START_STOP);
            PendingIntent stopMusicPendingIntent = PendingIntent.getService(this, 0, stopMusicIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

//            Intent nextIntent = new Intent(this, MediaPlayerService.class);
//            stopMusicIntent.setAction(START_SKIP_NEXT);
//            PendingIntent nextPendingIntent = PendingIntent.getService(this, 0, nextIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

            notification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Now Playing: " + currentSong.getTitle() + " - " + currentSong.getUser().getUsername())
                    .setSmallIcon(R.drawable.ic_launcher_white)
                    .setContentIntent(pi)
                    .addAction(R.drawable.ic_action_playback_pause, getString(R.string.pause), stopMusicPendingIntent)
                    .addAction(R.drawable.ic_action_playback_next, getString(R.string.next), null)
                    .build();

//        Picasso.with(this).load(currentMetadata.getUri()).into();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(currentSong.getStream_urlWithApiKey()));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            startForeground(notificationID, notification);
//            lockScreenControls();
        }
    }

//    private Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//            // Should probably check if current song is still the song that was clicked on
//            if (mRemoteControlClient != null && getCurrentTrack() != null) {
//                mRemoteControlClient.editMetadata(true)
//                        .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, getCurrentTrack().getName())
//                        .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, getCurrentTrack().getArtistName())
//                        .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, getCurrentTrack().getAlbumName())
//                        .putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, bitmap)
//                        .apply();
//            }
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//        }
//    };

//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    private void lockScreenControls() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//
//            Picasso.with(this).load(getCurrentTrack().getCoverImageUrl()).skipMemoryCache().into(target);
//        }
//    }

    @Override
    public void onDestroy() {
//        Picasso.with(this).cancelRequest(target);
        isPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        wifiLock.release();
        stopForeground(true);
        am.abandonAudioFocus(this);
        unregisterReceiver(myNoisyAudioStreamReceiver);
        am.unregisterMediaButtonEventReceiver(eventReceiver);
        EventBus.getDefault().post(new NoSongPlayingEvent());
    }

    private void stop() {
        mediaPlayer.stop();
        isPlaying = false;
        am.abandonAudioFocus(this);
        EventBus.getDefault().post(new NoSongPlayingEvent());
        stopForeground(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPlaying = false;
        nextButtonPressed();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(getClass().getSimpleName(), "MediaPlayer error. What:" + what + " Extra:" + extra);
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mp.isPlaying()) {
            buffering = false;
            SeekBarMoveEvent event = new SeekBarMoveEvent();
            event.setBufferingProgress(percent);
            event.setProgress(MediaPlayerUtils.getProgressPercentage(mp.getCurrentPosition(), mp.getDuration()));
            event.setCurrentMilliseconds(mediaPlayer.getCurrentPosition());
            event.setDuration(mediaPlayer.getDuration());
            event.setCurrentTrack(currentPlaylistSongs.get(currentPosition));
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (BuildConfig.DEBUG) {
            Log.e(getClass().getSimpleName(), "MediaPlayer onInfo. What:" + what + " Extra:" + extra);
        }
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            EventBus.getDefault().post(new BufferingEvent());
        }
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        mRemoteControlClient.setPlaybackState(
                RemoteControlClient.PLAYSTATE_PLAYING);
    }
}
