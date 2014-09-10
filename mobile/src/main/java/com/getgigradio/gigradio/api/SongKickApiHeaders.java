package com.getgigradio.gigradio.api;

import com.getgigradio.gigradio.SongKickModule;

import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public final class SongKickApiHeaders implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addEncodedQueryParam("apikey", SongKickModule.SONGKICK_IOS_API_KEY_CHANGE_FOR_ANDROID_MAYBE);
    }
}