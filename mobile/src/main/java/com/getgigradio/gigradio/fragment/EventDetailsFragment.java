package com.getgigradio.gigradio.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.holdr.Holdr_FragmentEventDetails;
import com.getgigradio.gigradio.model.songkick.event.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dreamers.graphics.RippleDrawable;

public class EventDetailsFragment extends Fragment implements Holdr_FragmentEventDetails.Listener {

    private Event event;
    private View view;
    private Holdr_FragmentEventDetails holdr;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;


    public static EventDetailsFragment newInstance(String eventJson) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Event.EXTRA_JSON, eventJson);
        fragment.setArguments(args);
        return fragment;
    }

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = Event.fromJson(getArguments().getString(Event.EXTRA_JSON));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        holdr = new Holdr_FragmentEventDetails(view);
        holdr.setListener(this);

        RippleDrawable.createRipple(holdr.buyTicketsButton, getResources().getColor(R.color
                .ripple_drawable));
        RippleDrawable.createRipple(holdr.venueWebsiteButton, getResources().getColor(R.color
                .ripple_drawable));
        RippleDrawable.createRipple(holdr.showDirectionsButton, getResources().getColor(R.color
                .ripple_drawable));
        RippleDrawable.createRipple(holdr.shareButton, getResources().getColor(R.color
                .ripple_drawable));

        holdr.artistTextView.setText(event.getPerformances().get(0).getDisplayName());
        holdr.venueTextView.setText(event.getVenue().getDisplayName());

        GoogleMapOptions options = new GoogleMapOptions();
//        options.compassEnabled(false);
        mMapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_layout, mMapFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
                LatLng latLng = new LatLng(event.getLocation().getLat().doubleValue(),
                        event.getLocation().getLng().doubleValue());
                CameraUpdate eventLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(eventLocation);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
            }
        }
    }

    @Override
    public void onBuyTicketsButtonClick(Button buyTicketsButton) {

    }

    @Override
    public void onVenueWebsiteButtonClick(Button venueWebsiteButton) {

    }

    @Override
    public void onShowDirectionsButtonClick(Button showDirectionsButton) {
        launchGoogleMaps(getActivity(), event.getLocation()
                        .getLat().doubleValue(), event.getLocation().getLng().doubleValue(),
                event.getVenue().getDisplayName());
    }

    public static void launchGoogleMaps(Context context, double latitude, double longitude,
                                        String label) {
        String format = "geo:0,0?q=" + Double.toString(latitude) + "," +
                "" + Double.toString(longitude) + "(" + label + ")";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onShareButtonClick(Button shareButton) {

    }
}
