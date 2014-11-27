package com.getgigradio.gigradio.api;

import android.content.Context;

import com.getgigradio.gigradio.R;

import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public final class SoundCloudApiHeaders implements RequestInterceptor {

    private final String apikey;

    public SoundCloudApiHeaders(Context context) {
        this.apikey = context.getString(R.string.soundcloud_api_key);
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addEncodedQueryParam("client_id", apikey);

    }
}