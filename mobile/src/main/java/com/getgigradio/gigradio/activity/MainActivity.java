package com.getgigradio.gigradio.activity;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getgigradio.gigradio.GigRadioApp;
import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.api.SongKickService;
import com.getgigradio.gigradio.api.SoundCloudService;
import com.getgigradio.gigradio.event.GetTracksEvent;
import com.getgigradio.gigradio.event.NewArtistEvent;
import com.getgigradio.gigradio.holdr.Holdr_ActivityMain;
import com.getgigradio.gigradio.model.songkickevent.Event;
import com.getgigradio.gigradio.model.songkickevent.EventObject;
import com.getgigradio.gigradio.model.songkickevent.Performance;
import com.getgigradio.gigradio.model.soundhoundartist.Artist;
import com.getgigradio.gigradio.model.soundhoundtrack.Track;
import com.getgigradio.gigradio.playback.MediaPlayerService;
import com.negusoft.holoaccent.activity.AccentActivity;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AccentActivity {

    @Inject
    SongKickService songKickService;
    @Inject
    SoundCloudService soundCloudService;

    private Holdr_ActivityMain holdr;
    private Number chosenArtistSoundCloudId;
    private List<Event> events;
    private int currentEventPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GigRadioApp app = (GigRadioApp) getApplication();
        app.inject(this);

        setContentView(R.layout.activity_main);

        holdr = new Holdr_ActivityMain(findViewById(android.R.id.content));

        holdr.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NewArtistEvent());
            }
        });

        holdr.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerService.stopItAll(MainActivity.this);
            }
        });

        holdr.venueTextView.setText("Finding local venues");

        Observable<EventObject> observable = songKickService.fetchEvents("geo:52.48949722,13.42834774");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EventObject>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new NewArtistEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("fetch events error", e.toString());
                        holdr.progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(EventObject s) {
                        events = s.getResultsPage().getResults().getEvents();
                    }
                });
    }

    public void onEvent(NewArtistEvent event) {
        currentEventPosition++;
        Event currentEvent = events.get(currentEventPosition);
        if (currentEvent.getPerformances().size() == 0) {
            EventBus.getDefault().post(new NewArtistEvent());
            return;
        }

        if (currentEventPosition % 2 == 0) {
            holdr.background.setBackgroundResource(R.drawable.gradient2to1transition);
            TransitionDrawable trans = (TransitionDrawable) holdr.background.getBackground();
            trans.startTransition(500);
        } else {
            holdr.background.setBackgroundResource(R.drawable.gradient1to2transition);
            TransitionDrawable trans = (TransitionDrawable) holdr.background.getBackground();
            trans.startTransition(500);
        }

        Performance performance = currentEvent.getPerformances().get(0);
        String chosenArtist = performance.getArtist().getDisplayName();
        hideTrackDetails();

        StringBuilder venueString = new StringBuilder(currentEvent.getVenue().getDisplayName());

        String datetimeString = currentEvent.getStart().getDatetime();
        if (datetimeString != null) {
            DateTime dateTime = DateTime.parse(datetimeString, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
            venueString.append(" @ ").append(DateUtils.formatDateTime(this, dateTime, DateUtils.FORMAT_SHOW_TIME));
        }
        holdr.venueTextView.setText(venueString.toString());

        holdr.venue2TextView.setText(currentEvent.getVenue().getMetroArea().getDisplayName());
        soundCloudService.fetchArtist(chosenArtist).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new GetTracksEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        chosenArtistSoundCloudId = artists.get(0).getId();
                    }
                });
    }

    private void hideTrackDetails() {
        holdr.artistTextView.setVisibility(View.GONE);
        holdr.trackNameTextView.setVisibility(View.GONE);
        holdr.progress.setVisibility(View.VISIBLE);
    }

    private void showTrackDetails() {
        holdr.artistTextView.setVisibility(View.VISIBLE);
        holdr.trackNameTextView.setVisibility(View.VISIBLE);
        holdr.progress.setVisibility(View.GONE);
    }

    public void onEvent(GetTracksEvent event) {
        soundCloudService.fetchTracks(chosenArtistSoundCloudId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Track>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(List<Track> tracks) {
                        holdr.artistTextView.setText(tracks.get(0).getUser().getUsername());
                        holdr.trackNameTextView.setText(tracks.get(0).getTitle());
                        showTrackDetails();
                        MediaPlayerService.startWithTrack(MainActivity.this, tracks.get(0));

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
