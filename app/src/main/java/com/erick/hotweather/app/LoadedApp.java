package com.erick.hotweather.app;

import android.app.Application;
import android.content.Context;

public class LoadedApp {

    private static Context sContext;
    private static Application sApplication;

    public static void init(Application application) {
        sContext = application;
        sApplication = application;
    }

    public static Context getContext() {
        return sContext;
    }

    public static Application getApplication() {
        return sApplication;
    }

}
