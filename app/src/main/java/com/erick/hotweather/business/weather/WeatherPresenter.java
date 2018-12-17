package com.erick.hotweather.business.weather;

import com.erick.hotweather.base.AbstractPresenter;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.IDataSource;

public class WeatherPresenter extends AbstractPresenter<WeatherContract.IView, WeatherContract.IModel> implements WeatherContract.IPresenter {

    private String mWeatherId;

    @Override
    public void onStart() {
        super.onStart();
        loadWeather(false);
        loadBingPic();
    }

    @Override
    protected WeatherContract.IModel createModel() {
        return new WeatherModel();
    }

    @Override
    public void setWeatherId(String weatherId) {
        if (weatherId != null) {
            mWeatherId = weatherId;
        }
    }

    @Override
    public void loadWeather(boolean forceUpdate) {
        getView().showLoading();
        if (forceUpdate) {
            model.refreshWeather();
        }
        model.queryWeather(mWeatherId, new IDataSource.LoadDataCallback<Weather>() {
            @Override
            public void onDataLoaded(Weather data) {
                getView().hideLoading();
                mWeatherId = data.basic.weatherId;
                if (!isViewAttached()) {
                    return;
                }
                getView().showWeather(data);
            }

            @Override
            public void onDataNotAvailable() {
                getView().hideLoading();
                if (!isViewAttached()) {
                    return;
                }
                getView().showMessage("loading_weather_error!!!");
            }
        });
    }

    @Override
    public void loadBingPic() {
        model.queryBingPic(new IDataSource.LoadDataCallback<String>() {
            @Override
            public void onDataLoaded(String data) {
                if (!isViewAttached()) {
                    return;
                }
                getView().showBingPic(data);
            }

            @Override
            public void onDataNotAvailable() {
                if (!isViewAttached()) {
                    return;
                }
                getView().showMessage("loading_bingpic_error...");
            }
        });
    }

}
