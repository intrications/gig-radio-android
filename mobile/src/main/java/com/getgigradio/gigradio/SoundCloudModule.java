package com.getgigradio.gigradio;

import com.getgigradio.gigradio.api.SoundCloudApiHeaders;
import com.getgigradio.gigradio.api.SoundCloudService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.RestAdapter;

@Module(
        complete = false,
        library = true
)
public class SoundCloudModule {

    public static final String SOUNDCLOUD_BASE_API = "http://api.soundcloud.com";

    public static final String SOUNDCLOUD_IOS_API_KEY_CHANGE_FOR_ANDROID_MAYBE = "d47e0a544e6e43721dd9847efc6120c5";

    @Provides
    @Singleton
    SoundCloudEndpoint provideSoundCloudEndpoint() {
        return new SoundCloudEndpoint();
    }

    @Provides
    @Singleton
    SoundCloudApiHeaders provideApiHeaders() {
        return new SoundCloudApiHeaders();
    }

//    @Provides
//    RestAdapter provideSoundCloudRestAdapter() {
//        return new RestAdapter.Builder()
////                .setClient(client)
//                .setEndpoint(endpoint)
//                .setRequestInterceptor(headers)
//                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
//                .build();
//    }

    @Provides
    @Singleton
    SoundCloudService provideSoundCloudService(SoundCloudEndpoint endpoint, SoundCloudApiHeaders headers) {
        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setClient(client)
                .setEndpoint(endpoint)
                .setRequestInterceptor(headers)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        return restAdapter.create(SoundCloudService.class);
    }

    public class SoundCloudEndpoint implements Endpoint {
        @Override
        public String getUrl() {
            return SOUNDCLOUD_BASE_API;
        }

        @Override
        public String getName() {
            return "soundcloud";
        }
    }
}
