package com.getgigradio.gigradio.module;

import android.content.Context;

import com.getgigradio.gigradio.GigRadioApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                DataModule.class
        },
        injects = {
                GigRadioApp.class
        }
)
public final class GigRadioModule {
    private final GigRadioApp app;

    public GigRadioModule(GigRadioApp app) {
        this.app = app;
    }

//    @Provides
//    @Singleton
//    Application provideApplication() {
//        return app;
//    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return app.getApplicationContext();
    }
}