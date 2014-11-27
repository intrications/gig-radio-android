package com.getgigradio.gigradio.module;

import android.content.Context;

import com.getgigradio.gigradio.BuildConfig;
import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.api.SongKickApiHeaders;
import com.getgigradio.gigradio.api.SongKickService;

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
public class SongKickModule {

    @Provides
    @Singleton
    @ForSongKick
    Endpoint provideEndpoint(Context context) {
        return Endpoints.newFixedEndpoint(context.getString(R.string.songkick_base_url));
    }

//    @Provides
//    @Singleton
//    Client provideClient(OkHttpClient client) {
//        return new OkClient(client);
//    }

    @Provides
    @Singleton
    SongKickApiHeaders provideApiHeaders(Context context) {
        return new SongKickApiHeaders(context);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(@ForSongKick Endpoint endpoint, SongKickApiHeaders headers) {
        return new RestAdapter.Builder()
//                .setClient(client)
                .setEndpoint(endpoint)
                .setRequestInterceptor(headers)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
    }

    @Provides
    @Singleton
    SongKickService provideSongkickService(RestAdapter restAdapter) {
        return restAdapter.create(SongKickService.class);
    }
}
