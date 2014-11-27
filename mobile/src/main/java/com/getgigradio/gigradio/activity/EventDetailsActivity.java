package com.getgigradio.gigradio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageButton;

import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.fragment.EventDetailsFragment;
import com.getgigradio.gigradio.holdr.Holdr_ActivityEventDetails;
import com.getgigradio.gigradio.model.songkick.event.Event;

public class EventDetailsActivity extends ActionBarActivity implements
        Holdr_ActivityEventDetails.Listener {

    private Holdr_ActivityEventDetails holdr;

    public static void startActivty(Context context, Event event) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(Event.EXTRA_JSON, event.toJson());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        holdr = new Holdr_ActivityEventDetails(findViewById(android.R.id.content));
        holdr.setListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, EventDetailsFragment.newInstance(getIntent()
                            .getStringExtra(Event.EXTRA_JSON)))
                    .commit();
        }
    }

    @Override
    public void onBackButtonClick(ImageButton backButton) {
        finish();
    }
}
