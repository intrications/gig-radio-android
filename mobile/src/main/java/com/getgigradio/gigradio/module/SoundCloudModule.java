package com.getgigradio.gigradio.module;

import android.content.Context;

import com.getgigradio.gigradio.BuildConfig;
import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.api.SoundCloudApiHeaders;
import com.getgigradio.gigradio.api.SoundCloudService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;

@Module(
        complete = false,
        library = true
)
public class SoundCloudModule {

    @Provides
    @Singleton
    @ForSoundCloud
    Endpoint provideEndpoint(Context context) {
        return Endpoints.newFixedEndpoint(context.getString(R.string.soundcloud_base_url));
    }

    @Provides
    @Singleton
    SoundCloudApiHeaders provideApiHeaders(Context context) {
        return new SoundCloudApiHeaders(context);
    }

    @Provides
    @Singleton
    SoundCloudService provideSoundCloudService(@ForSoundCloud Endpoint endpoint, SoundCloudApiHeaders headers) {
        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setClient(client)
                .setEndpoint(endpoint)
                .setRequestInterceptor(headers)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        return restAdapter.create(SoundCloudService.class);
    }
}
