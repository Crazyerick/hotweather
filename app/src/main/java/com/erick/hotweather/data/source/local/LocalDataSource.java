package com.erick.hotweather.data.source.local;

import com.erick.hotweather.app.AppExecutors;
import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.IDataSource;
import com.erick.hotweather.util.SPUtil;
import com.erick.hotweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LocalDataSource implements IDataSource {

    private static volatile LocalDataSource INSTANCE;
    private AppExecutors mAppExecutors;

    private LocalDataSource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static LocalDataSource getInstance(AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void queryProvinces(final LoadDatasCallback<Province> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Province> provinces = DataSupport.findAll(Province.class);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (provinces.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(provinces);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void queryCities(final Province province, final LoadDatasCallback<City> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<City> cities = DataSupport.where("provinceid = ?", String.valueOf(province.getId())).find(City.class);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (cities.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(cities);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void queryCounties(Province province, final City city, final LoadDatasCallback<County> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<County> counties = DataSupport.where("cityid = ?", String.valueOf(city.getId())).find(County.class);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (counties.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(counties);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshWeather() {

    }

    @Override
    public void queryWeather(String weatherId, final LoadDataCallback<Weather> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String weatherString = SPUtil.getString("weather", null);
                final Weather weather = weatherString != null ? Utility.handleWeatherResponse(weatherString) : null;
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (weather == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDataLoaded(weather);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void queryBingPic(final LoadDataCallback<String> callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String bingPic = SPUtil.getString("bing_pic", null);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (bingPic == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDataLoaded(bingPic);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

}
