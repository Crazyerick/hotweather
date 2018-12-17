package com.erick.hotweather.business.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.erick.hotweather.app.LoadedApp;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.DataRepository;
import com.erick.hotweather.data.source.IDataSource;
import com.erick.hotweather.network.NetworkUtil;
import com.erick.hotweather.util.HttpUtil;
import com.erick.hotweather.util.SPUtil;
import com.erick.hotweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    public AutoUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 更新天气信息。
     */
    private void updateWeather() {
        String weatherString = SPUtil.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            DataRepository.getInstance().refreshWeather();
            DataRepository.getInstance().queryWeather(weatherId, new IDataSource.LoadDataCallback<Weather>() {
                @Override
                public void onDataLoaded(Weather data) {
                }

                @Override
                public void onDataNotAvailable() {
                }
            });
        }
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) return;
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.httpGet(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SPUtil.putString("bing_pic", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}
