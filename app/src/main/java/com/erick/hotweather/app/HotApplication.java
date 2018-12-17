package com.erick.hotweather.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by erickxhyang on 2018/3/13.
 */

public class HotApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LoadedApp.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
