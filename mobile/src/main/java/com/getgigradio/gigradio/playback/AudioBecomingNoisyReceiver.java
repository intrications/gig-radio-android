package com.getgigradio.gigradio.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AudioBecomingNoisyReceiver extends BroadcastReceiver {
    public AudioBecomingNoisyReceiver() {
    }

    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            Intent pauseIntent = new Intent(ctx, MediaPlayerService.class);
            pauseIntent.setAction(MediaPlayerService.START_PAUSE);
            ctx.startService(pauseIntent);
        }
    }
}
