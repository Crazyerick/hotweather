package com.erick.hotweather.business.weather;

import com.erick.hotweather.base.BaseModel;
import com.erick.hotweather.base.BasePresenter;
import com.erick.hotweather.base.BaseView;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.IDataSource;

public interface WeatherContract {

    interface IModel extends BaseModel {

        void refreshWeather();

        void queryWeather(String weatherId, IDataSource.LoadDataCallback<Weather> callback);

        void queryBingPic(IDataSource.LoadDataCallback<String> callback);
    }

    interface IView extends BaseView<WeatherContract.IPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String msg);

        void showBingPic(String imagePath);

        void showWeather(Weather weather);
    }

    interface IPresenter extends BasePresenter<WeatherContract.IView> {

        void setWeatherId(String weatherId);

        void loadWeather(boolean forceUpdate);

        void loadBingPic();
    }

}
