package com.getgigradio.gigradio;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.getgigradio.gigradio.module.GigRadioModule;
import com.squareup.okhttp.OkHttpClient;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.InputStream;

import dagger.ObjectGraph;

public class GigRadioApp extends Application {

    private ObjectGraph objectGraph;

    private static GigRadioApp app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(new OkHttpClient()));

        JodaTimeAndroid.init(this);

        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(new GigRadioModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static GigRadioApp get(Context context) {
        return (GigRadioApp) context.getApplicationContext();
    }

    public static GigRadioApp getApplication() {
        return app;
    }
}
