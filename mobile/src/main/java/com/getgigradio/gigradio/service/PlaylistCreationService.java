package com.getgigradio.gigradio.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.getgigradio.gigradio.GigRadioApp;
import com.getgigradio.gigradio.api.SongKickService;
import com.getgigradio.gigradio.api.SoundCloudService;
import com.getgigradio.gigradio.model.songkick.event.Event;
import com.getgigradio.gigradio.model.songkick.event.EventObject;
import com.getgigradio.gigradio.playback.MediaPlayerService;
import com.getgigradio.gigradio.util.Prefs;
import com.getgigradio.gigradio.util.PrefsUtils;
import com.getgigradio.gigradio.util.SongKickDateTime;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlaylistCreationService extends WakefulIntentService {

    @Inject
    SongKickService songKickService;
    @Inject
    SoundCloudService soundCloudService;

    public static final String FETCH_NEW_PLAYLIST = "new_playlist";
    public static final String FETCH_NEXT_SONG_IN_PLAYLIST = "fetch";
    private static List<Event> savedEvents;
    private static int currentEventPosition = -1;

    public PlaylistCreationService() {
        super("PlaylistCreationService");
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, PlaylistCreationService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((GigRadioApp) getApplication()).inject(this);
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        createPlaylist();
    }

    private void createPlaylist() {

        // If playlist already created then just get next track
        if (savedEvents != null) {
            getNextTrack();
        }
        Location location = PrefsUtils.getRadioStationLocation(this);

        if (location == null) {
//            return;
            //   problem!
            //   Use berlin.
        }

//        // Test search for metro areas
//
//        Observable<LocationObject> locationObjectObservable = songKickService
//                .fetchLocationsByLocation(String.format(Locale.US, "geo:%f,%f",
//                        location.getLatitude(), location.getLongitude()));
//        locationObjectObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(locationObject -> Observable.from(locationObject.getResultsPage()
//                        .getResults().getLocationsList()))
//                .subscribe(location2 -> Log.d("cities", location2.getCity().getDisplayName()));


        long savedSelectedWeek = Prefs.with(this).getLong
                (PrefsUtils.SELECTED_DATE_TIME,
                        DateTime.now().getMillis());
        DateTime selected = new DateTime(savedSelectedWeek);
        DateTime sundayOfSelectedWeek = selected.withDayOfWeek(DateTimeConstants
                .SUNDAY);

//        Observable<EventObject> observable = songKickService.fetchEventsByLocation("geo:52
// .48949722,13.42834774");
        Observable<EventObject> observable = songKickService.fetchEventsByLocation
                (String.format(Locale.US, "geo:%f,%f",
                                location.getLatitude(), location.getLongitude()),
                        new SongKickDateTime(selected),
                        new SongKickDateTime(sundayOfSelectedWeek));
//        Observable<EventObject> observable = songKickService.fetchEventsByClientIp(formatter
// .print(now), formatter.print(sundayOfSelectedWeek));

        // TODO if no events for current location then do a search of other local
        // metro areas
        // Or fallback to ip address?
        // TODO check if location services are even switched on

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        // Probably not needed but makes clear that only one result is expected
                .first()
                .flatMap(eventObject -> Observable.from(eventObject.getResultsPage().getResults().getEvents()))
                        // Skip events with no start time
                .filter(event -> event.getStart().getDatetime() != null)
                .toList()
                .subscribe(events -> {
                            // do something with events
                            // save them?
                            //process them
                            savedEvents = events;
                        },
                        error -> {
                            Log.e("fetch events error", error.toString());
//                            holdr.progress.setVisibility(View.GONE);
                        },
                        () -> getNextTrack());

        // TODO save all events - maybe get extra pages.
        // Then process 10 events at a time
//        Observable.from(events).skip(10).take(10).
    }

    private void getNextTrack() {
        currentEventPosition++;
        Event currentEvent = savedEvents.get(currentEventPosition);
        soundCloudService.fetchArtist(currentEvent.getPerformances().get(0).getArtist().getDisplayName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .single()
                .flatMap(artists -> {
                    if (artists.size() == 0) {
                        return null;
                    }
//                    chosenArtistSoundCloudId = artists.get(0).getId();
                    Log.d("chosen artist", artists.get(0).getFull_name());
                    return soundCloudService.fetchTracks(artists.get(0).getId());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                    if (tracks.size() == 0) {
                        getNextTrack();
                    } else {
                        MediaPlayerService.startWithTrack(PlaylistCreationService.this, tracks.get(0));
                    }
                }, error -> Log.d("Error!", error.toString()));
    }
}
