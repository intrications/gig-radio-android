package com.getgigradio.gigradio.api;

import android.content.Context;

import com.getgigradio.gigradio.R;

import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public final class SongKickApiHeaders implements RequestInterceptor {

    private final String apikey;

    public SongKickApiHeaders(Context context) {
        this.apikey = context.getString(R.string.songkick_api_key);
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addEncodedQueryParam("apikey", apikey);
    }
}