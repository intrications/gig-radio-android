package com.getgigradio.gigradio.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.getgigradio.gigradio.BuildConfig;
import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.event.NewSongEvent;
import com.getgigradio.gigradio.event.NextButtonPressedEvent;
import com.getgigradio.gigradio.event.StartNewStationEvent;
import com.getgigradio.gigradio.holdr.Holdr_ActivityMain;
import com.getgigradio.gigradio.holdr.Holdr_DialogWeekChooser;
import com.getgigradio.gigradio.model.songkick.event.Event;
import com.getgigradio.gigradio.model.soundcloud.track.Track;
import com.getgigradio.gigradio.playback.MediaPlayerService;
import com.getgigradio.gigradio.service.PlaylistCreationService;
import com.getgigradio.gigradio.util.AnimationUtils;
import com.getgigradio.gigradio.util.Prefs;
import com.getgigradio.gigradio.util.PrefsUtils;
import com.getgigradio.gigradio.util.WeeklyDateTime;
import com.getgigradio.gigradio.widget.ExpandingCircleButton;
import com.github.stephanenicolas.loglifecycle.LogLifeCycle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.joda.time.DateTime;

import java.util.List;

import de.greenrobot.event.EventBus;
import dreamers.graphics.RippleDrawable;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.observables.AndroidObservable;
import rx.schedulers.Schedulers;

@LogLifeCycle
public class MainActivity extends ActionBarActivity implements Holdr_ActivityMain.Listener {

    private static final int MENU_SHOW_BERLIN = 100;

    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

    private Holdr_ActivityMain holdr;
    private List<Event> events;
    private int currentEventPosition = -1;
    private Track currentTrack;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't run Crashlytics on DEBUG builds
        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }

        setContentView(R.layout.activity_main);

        holdr = new Holdr_ActivityMain(findViewById(android.R.id.content));

        setSupportActionBar(holdr.toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(Color.TRANSPARENT);

        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        holdr.setListener(this);

//        if (checkPlayServices()) {
//            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation
//                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient1a),
//                    getResources().getColor(R.color.gradient1b)});
//            GradientDrawable shadow2 = new GradientDrawable(GradientDrawable.Orientation
//                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient2a),
//                    getResources().getColor(R.color.gradient2b)});
//
//            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{shadow,
//                    shadow2});
//            holdr.background.setImageDrawable(transitionDrawable);
//            transitionDrawable.startTransition(500);
//        }

//        // TODO check for large changes in distance - save current distance for radio station so
//        // can compare.
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
        Observable<Location> lastKnownLocation = locationProvider.getLastKnownLocation();
        AndroidObservable.bindActivity(this, lastKnownLocation)
                .subscribeOn(Schedulers.computation())
                .subscribe(location -> {
                    Toast.makeText(MainActivity.this, "new location: " + location.toString
                            (), Toast.LENGTH_SHORT).show();
                    PrefsUtils.saveRadioStationLocation(MainActivity.this, location);
                    Log.d("new location found", location.toString());
                    searchForNewStation();
                });

//        locationProvider.addGeofences()
//
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.set
//        locationProvider.getUpdatedLocation(locationRequest)

        RippleDrawable.createRipple(holdr.weekChooserButton, getResources().getColor(R.color
                .ripple_drawable));
        RippleDrawable.createRipple(holdr.eventDetailsLayout, getResources().getColor(R.color
                .ripple_drawable));

        holdr.weekChooserButton.setOnClickListener(v -> showWeekChoserDialog());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unsubscribe from subscriptions
    }

    public void onEvent(StartNewStationEvent event) {
        searchForNewStation();
    }

    private void searchForNewStation() {

        hideTrackDetails();

        holdr.venueTextView.setText("");
        holdr.venue2TextView.setText("");

        long savedSelectedWeek = Prefs.with(MainActivity.this).getLong
                (PrefsUtils.SELECTED_DATE_TIME,
                        DateTime.now().getMillis());
        DateTime selected = new DateTime(savedSelectedWeek);

        holdr.weekChooserButton.setText(new WeeklyDateTime(this,
                selected).formatAsDateRange());

        // start service

        PlaylistCreationService.startService(this);
    }

    public void onEvent(NextButtonPressedEvent event) {
        PlaylistCreationService.startService(this);

//
//        Performance performance = currentEvent.getPerformances().get(0);
//        String chosenArtist = performance.getArtist().getDisplayName();
//        hideTrackDetails();
//
//        StringBuilder venueString = new StringBuilder(currentEvent.getVenue().getDisplayName());
//
//        holdr.venueTextView.setText(venueString.toString());
//
//        holdr.venue2TextView.setText(currentEvent.getVenue().getMetroArea().getDisplayName());
    }

    private void hideTrackDetails() {
        AnimationUtils.animateToGone(holdr.detailsLayout);
        holdr.progress.setVisibility(View.VISIBLE);
        ((CircularProgressDrawable) holdr.progress.getIndeterminateDrawable()).start();
    }

    private void showTrackDetails() {
        AnimationUtils.animateToVisible(holdr.detailsLayout);
        ((CircularProgressDrawable) holdr.progress.getIndeterminateDrawable()).progressiveStop
                (circularProgressDrawable -> holdr.progress.setVisibility(View.GONE));
    }

    public void onEvent(NewSongEvent event) {
        showTrackDetails();
        currentTrack = event.getCurrentSong();
        holdr.artistTextView.setText(currentTrack.getUser().getUsername());
        holdr.trackNameTextView.setText(currentTrack.getTitle());

//        holdr.eventDateTextView.setText(currentEvent.getDisplayTime(MainActivity
//                .this));
        Toast.makeText(this, "Genre: " + currentTrack.getGenre() + " Track type: " + currentTrack
                .getTrack_type(), Toast.LENGTH_SHORT).show();



        // TODO fade this out along with gradient
        holdr.backgroundImageView.setImageDrawable(null);

        String artwork_url = currentTrack.getArtwork_url();
        if (artwork_url != null) {
            String artworkUrl = artwork_url.replace("-large",
                    "-t300x300");
            // also try t300x300 etc

            Glide.with(MainActivity.this).load(artworkUrl).into(holdr
                    .backgroundImageView);

            showGradientOnBackground();
        }
    }

    private void showGradientOnBackground() {
        // TODO  fix first transition from black - and allow random transitions
        if (currentEventPosition % 2 == 0) {
            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation
                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient1a),
                    getResources().getColor(R.color.gradient1b)});
            GradientDrawable shadow2 = new GradientDrawable(GradientDrawable.Orientation
                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient2a),
                    getResources().getColor(R.color.gradient2b)});

            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{shadow,
                    shadow2});
            holdr.background.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(500);
        } else {
            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation
                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient2a),
                    getResources().getColor(R.color.gradient2b)});
            GradientDrawable shadow2 = new GradientDrawable(GradientDrawable.Orientation
                    .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient3a),
                    getResources().getColor(R.color.gradient3b)});

            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{shadow,
                    shadow2});
            holdr.background.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(500);
        }
    }

    private void startPlayingTracks(List<Track> tracks) {

        final Event currentEvent = events.get(currentEventPosition);
        holdr.eventDetailsLayout.setOnClickListener(v -> EventDetailsActivity.startActivty
                (MainActivity.this,
                        currentEvent));




//        holdr.starButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                new FavouritesManager().saveAsFavourite(MainActivity.this, currentEvent);
//            } else {
//                new FavouritesManager().removeFromFavourites(MainActivity.this, currentEvent);
//            }
//        });
//
//        new FavouritesManager().isFavourite(this, currentEvent, isFavourite -> holdr.starButton
//                .setChecked(isFavourite));




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (BuildConfig.DEBUG) {
            menu.add(0, MENU_SHOW_BERLIN, 0, "Show Berlin");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == MENU_SHOW_BERLIN) {
            Location location = new Location("");
            location.setLatitude(52.48949722);
            location.setLongitude(13.42834774);
            PrefsUtils.saveRadioStationLocation(this, location);
            searchForNewStation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showErrorDialog(status);
            } else {
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this,
                REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Google Play Services must be installed.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPreviousButtonClick(ExpandingCircleButton previousButton) {

    }

    @Override
    public void onNextButtonClick(ExpandingCircleButton nextButton) {
        EventBus.getDefault().post(new NextButtonPressedEvent());
    }

    @Override
    public void onStopButtonClick(ExpandingCircleButton stopButton) {
        MediaPlayerService.stopItAll(MainActivity.this);
    }

    //    @Override
//    public void onShareButtonClick(ImageButton shareButton) {
//        Intent intent = ShareCompat.IntentBuilder.from(this).setText(currentTrack
// .getPermalink_url()).setType("plain/text").getIntent();
//        startActivity(intent);
//    }

    private void showWeekChoserDialog() {

        // TODO use a dialog fragment?

        View view = getLayoutInflater().inflate(R.layout.dialog_week_chooser, null);
        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity
                .this, android.R.style.Theme_Holo_Light_Dialog_MinWidth))
                .setView(view)
                .create();

        final Holdr_DialogWeekChooser dialog_holdr = new Holdr_DialogWeekChooser(view);
        RippleDrawable.createRipple(dialog_holdr.cancelButton, Color.BLACK);
        RippleDrawable.createRipple(dialog_holdr.startButton, Color.BLACK);
        dialog_holdr.cancelButton.setOnClickListener(v -> dialog.cancel());
        dialog_holdr.startButton.setOnClickListener(v -> {
            Prefs.with(MainActivity.this).save(PrefsUtils.SELECTED_DATE_TIME,
                    dialog_holdr.weekChooserView.getSelectedWeek().getMillis());
            EventBus.getDefault().post(new StartNewStationEvent());
            dialog.dismiss();
        });
        long savedSelectedWeek = Prefs.with(MainActivity.this).getLong(PrefsUtils
                        .SELECTED_DATE_TIME,
                DateTime.now().getMillis());
        dialog_holdr.weekChooserView.setSelectedWeek(new DateTime(savedSelectedWeek));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        dialog.setCancelable(true);
        dialog.show();
    }
}
