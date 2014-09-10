package com.getgigradio.gigradio.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {
    public RemoteControlReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if ((event.getAction() == KeyEvent.ACTION_DOWN)) {
                Log.d(getClass().getSimpleName(), intent.toString());
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == event.getKeyCode()) {
                    Log.d(getClass().getSimpleName(), "play button pressed");
                    Intent newIntent = new Intent(context, MediaPlayerService.class);
                    newIntent.setAction(MediaPlayerService.START_PLAY_PAUSE);
                    context.startService(newIntent);
                } else if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode()) {
                    Intent newIntent = new Intent(context, MediaPlayerService.class);
                    newIntent.setAction(MediaPlayerService.START_SKIP_NEXT);
                    context.startService(newIntent);
                } else if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == event.getKeyCode()) {
                    Intent newIntent = new Intent(context, MediaPlayerService.class);
                    newIntent.setAction(MediaPlayerService.START_SKIP_PREVIOUS);
                    context.startService(newIntent);
                }
            }
        }
    }
}
