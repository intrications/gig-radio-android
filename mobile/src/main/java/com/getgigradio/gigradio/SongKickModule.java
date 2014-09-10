package com.getgigradio.gigradio;

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

    public static final String SONGKICK_BASE_API = "http://api.songkick.com/api/3.0";

    public static final String SONGKICK_IOS_API_KEY_CHANGE_FOR_ANDROID_MAYBE = "D9wakwauz69uzYV5";

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(SONGKICK_BASE_API);
    }

//    @Provides
//    @Singleton
//    Client provideClient(OkHttpClient client) {
//        return new OkClient(client);
//    }

    @Provides
    @Singleton
    SongKickApiHeaders provideApiHeaders() {
        return new SongKickApiHeaders();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, SongKickApiHeaders headers) {
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
