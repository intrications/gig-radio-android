package com.getgigradio.gigradio.api;

import com.getgigradio.gigradio.SoundCloudModule;

import javax.inject.Singleton;

import retrofit.RequestInterceptor;

@Singleton
public final class SoundCloudApiHeaders implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addEncodedQueryParam("client_id", SoundCloudModule.SOUNDCLOUD_IOS_API_KEY_CHANGE_FOR_ANDROID_MAYBE);

    }
}