package com.getgigradio.gigradio.module;

import com.getgigradio.gigradio.GigRadioApp;
import com.getgigradio.gigradio.activity.MainActivity;
import com.getgigradio.gigradio.service.PlaylistCreationService;

import dagger.Module;

@Module(
        includes = {SongKickModule.class, SoundCloudModule.class},
        complete = false,
        library = true,
        injects = {GigRadioApp.class, PlaylistCreationService.class
        }
)
public final class DataModule {
//    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

//    @Provides
//    @Singleton
//    SharedPreferences provideSharedPreferences(Application app) {
//        return app.getSharedPreferences("u2020", MODE_PRIVATE);
//    }

//  @Provides @Singleton Picasso providePicasso(Application app, OkHttpClient client) {
//    return new Picasso.Builder(app)
//        .downloader(new OkHttpDownloader(client))
//        .listener(new Picasso.Listener() {
//          @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
//            Timber.e(e, "Failed to load image: %s", uri);
//          }
//        })
//        .build();
//  }

//    static OkHttpClient createOkHttpClient(Application app) {
//        OkHttpClient client = new OkHttpClient();

        // Install an HTTP cache in the application cache directory.
//    try {
//      File cacheDir = new File(app.getCacheDir(), "http");
//      HttpResponseCache cache = new HttpResponseCache(cacheDir, DISK_CACHE_SIZE);
//      client.setResponseCache(cache);
//    } catch (IOException e) {
//      Timber.e(e, "Unable to install disk cache.");
//    }

//        return client;
//    }
}