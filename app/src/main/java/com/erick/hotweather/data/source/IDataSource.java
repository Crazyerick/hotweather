package com.erick.hotweather.data.source;

import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;

import java.util.List;

public interface IDataSource {

    interface LoadDatasCallback<T> {

        void onDatasLoaded(List<T> datas);

        void onDataNotAvailable();
    }

    interface LoadDataCallback<T> {

        void onDataLoaded(T data);

        void onDataNotAvailable();
    }

    void queryProvinces(LoadDatasCallback<Province> callback);

    void queryCities(Province province, LoadDatasCallback<City> callback);

    void queryCounties(Province province, City city, LoadDatasCallback<County> callback);

    void queryWeather(String weatherId, IDataSource.LoadDataCallback<Weather> callback);

    void queryBingPic(IDataSource.LoadDataCallback<String> callback);

    void refreshWeather();

}
