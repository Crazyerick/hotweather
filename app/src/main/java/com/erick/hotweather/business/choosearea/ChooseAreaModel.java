package com.erick.hotweather.business.choosearea;

import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.source.DataRepository;
import com.erick.hotweather.data.source.IDataSource;

public class ChooseAreaModel implements ChooseAreaContract.IModel {

    @Override
    public void onDestroy() {
    }

    @Override
    public void queryProvinces(IDataSource.LoadDatasCallback<Province> callback) {
        DataRepository.getInstance().queryProvinces(callback);
    }

    @Override
    public void queryCities(Province province, IDataSource.LoadDatasCallback<City> callback) {
        DataRepository.getInstance().queryCities(province, callback);
    }

    @Override
    public void queryCounties(Province province, City city, IDataSource.LoadDatasCallback<County> callback) {
        DataRepository.getInstance().queryCounties(province, city, callback);
    }

}
