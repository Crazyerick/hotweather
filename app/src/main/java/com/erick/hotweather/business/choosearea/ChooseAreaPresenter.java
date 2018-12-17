package com.erick.hotweather.business.choosearea;

import com.erick.hotweather.base.AbstractPresenter;
import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.source.IDataSource;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaPresenter extends AbstractPresenter<ChooseAreaContract.IView, ChooseAreaContract.IModel> implements ChooseAreaContract.IPresenter {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    /**
     * 省列表
     */
    private List<Province> mProvinceList;

    /**
     * 市列表
     */
    private List<City> mCityList;

    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Override
    protected ChooseAreaContract.IModel createModel() {
        return new ChooseAreaModel();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadProvinces();
    }

    @Override
    public void handleBackClicked() {
        if (currentLevel == LEVEL_COUNTY) {
            loadCities();
        } else if (currentLevel == LEVEL_CITY) {
            loadProvinces();
        }
    }

    @Override
    public void loadDatas(int position) {
        if (currentLevel == LEVEL_PROVINCE) {
            selectedProvince = mProvinceList.get(position);
            loadCities();
        } else if (currentLevel == LEVEL_CITY) {
            selectedCity = mCityList.get(position);
            loadCounties();
        } else if (currentLevel == LEVEL_COUNTY) {
            String weatherId = mCountyList.get(position).getWeatherId();
            getView().updateWeather(weatherId);
        }
    }

    private void loadProvinces() {
        getView().showLoading();
        getView().setTitle("中国");
        getView().hideBackButton();
        model.queryProvinces(new IDataSource.LoadDatasCallback<Province>() {
            @Override
            public void onDatasLoaded(List<Province> datas) {
                getView().hideLoading();
                mProvinceList = datas;
                if (!isViewAttached()) {
                    return;
                }
                List<String> showDatas = new ArrayList<>();
                for (Province province : mProvinceList) {
                    showDatas.add(province.getProvinceName());
                }
                getView().showDatas(showDatas);
                getView().setSelection(0);
                currentLevel = LEVEL_PROVINCE;
            }

            @Override
            public void onDataNotAvailable() {
                getView().hideLoading();
                if (!isViewAttached()) {
                    return;
                }
                getView().showMessage("loading_datas_error...");
            }
        });
    }

    private void loadCities() {
        getView().showLoading();
        getView().setTitle(selectedProvince.getProvinceName());
        getView().showBackButton();
        model.queryCities(selectedProvince, new IDataSource.LoadDatasCallback<City>() {
            @Override
            public void onDatasLoaded(List<City> datas) {
                getView().hideLoading();
                mCityList = datas;
                if (!isViewAttached()) {
                    return;
                }
                List<String> showDatas = new ArrayList<>();
                for (City city : mCityList) {
                    showDatas.add(city.getCityName());
                }
                getView().showDatas(showDatas);
                getView().setSelection(0);
                currentLevel = LEVEL_CITY;
            }

            @Override
            public void onDataNotAvailable() {
                getView().hideLoading();
                if (!isViewAttached()) {
                    return;
                }
                getView().showMessage("loading_datas_error...");
            }
        });
    }

    private void loadCounties() {
        getView().showLoading();
        getView().setTitle(selectedCity.getCityName());
        getView().showBackButton();
        model.queryCounties(selectedProvince, selectedCity, new IDataSource.LoadDatasCallback<County>() {
            @Override
            public void onDatasLoaded(List<County> datas) {
                getView().hideLoading();
                mCountyList = datas;
                if (!isViewAttached()) {
                    return;
                }
                List<String> showDatas = new ArrayList<>();
                for (County county : mCountyList) {
                    showDatas.add(county.getCountyName());
                }
                getView().showDatas(showDatas);
                getView().setSelection(0);
                currentLevel = LEVEL_COUNTY;
            }

            @Override
            public void onDataNotAvailable() {
                getView().hideLoading();
                if (!isViewAttached()) {
                    return;
                }
                getView().showMessage("loading_datas_error...");
            }
        });
    }

}
