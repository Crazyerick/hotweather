package com.erick.hotweather.business.choosearea;

import com.erick.hotweather.base.BaseModel;
import com.erick.hotweather.base.BasePresenter;
import com.erick.hotweather.base.BaseView;
import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.source.IDataSource;

import java.util.List;

public interface ChooseAreaContract {

    interface IModel extends BaseModel {

        void queryProvinces(IDataSource.LoadDatasCallback<Province> callback);

        void queryCities(Province province, IDataSource.LoadDatasCallback<City> callback);

        void queryCounties(Province province, City city, IDataSource.LoadDatasCallback<County> callback);

    }

    interface IView extends BaseView<IPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String msg);

        void setTitle(String text);

        void showBackButton();

        void hideBackButton();

        void showDatas(List<String> datas);

        void setSelection(int position);

        void updateWeather(String weatherId);
    }

    interface IPresenter extends BasePresenter<IView> {

        void loadDatas(int position);

        void handleBackClicked();

    }

}
