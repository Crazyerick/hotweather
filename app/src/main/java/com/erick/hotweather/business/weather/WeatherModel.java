package com.erick.hotweather.business.weather;

import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.DataRepository;
import com.erick.hotweather.data.source.IDataSource;

public class WeatherModel implements WeatherContract.IModel {

    @Override
    public void onDestroy() {
    }

    @Override
    public void refreshWeather() {
        DataRepository.getInstance().refreshWeather();
    }

    @Override
    public void queryWeather(String weatherId, IDataSource.LoadDataCallback<Weather> callback) {
        DataRepository.getInstance().queryWeather(weatherId, callback);
    }

    @Override
    public void queryBingPic(IDataSource.LoadDataCallback<String> callback) {
        DataRepository.getInstance().queryBingPic(callback);
    }

}
