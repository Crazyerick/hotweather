package com.erick.hotweather.data.source;

import com.erick.hotweather.app.AppExecutors;
import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.local.LocalDataSource;
import com.erick.hotweather.data.source.remote.RemoteDataSource;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DataRepository implements IDataSource {

    private AppExecutors mAppExecutors;
    private IDataSource mRemoteDataSource;
    private IDataSource mLocalDataSource;
    private boolean mWeatherIsDirty = false;

    private DataRepository() {
        mAppExecutors = new AppExecutors();
        mRemoteDataSource = RemoteDataSource.getInstance(mAppExecutors);
        mLocalDataSource = LocalDataSource.getInstance(mAppExecutors);
    }

    private static class SingletonHolder {
        private static final DataRepository INSTANCE = new DataRepository();
    }

    public static DataRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setRemoteDataSource(IDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public void setLocalDataSource(IDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询.
     */
    @Override
    public void queryProvinces(final LoadDatasCallback<Province> callback) {
        mLocalDataSource.queryProvinces(new LoadDatasCallback<Province>() {
            @Override
            public void onDatasLoaded(List<Province> datas) {
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                queryProvincesFromRemoteDataSource(callback);
            }
        });
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    @Override
    public void queryCities(final Province province, final LoadDatasCallback<City> callback) {
        mLocalDataSource.queryCities(province, new LoadDatasCallback<City>() {
            @Override
            public void onDatasLoaded(List<City> datas) {
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                queryCitiesFromRemoteDataSource(province, callback);
            }
        });
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    @Override
    public void queryCounties(final Province province, final City city, final LoadDatasCallback<County> callback) {
        mLocalDataSource.queryCounties(province, city, new LoadDatasCallback<County>() {
            @Override
            public void onDatasLoaded(List<County> datas) {
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                queryCountiesFromRemoteDataSource(province, city, callback);
            }
        });
    }

    @Override
    public void refreshWeather() {
        mWeatherIsDirty = true;
    }

    @Override
    public void queryWeather(final String weatherId, final LoadDataCallback<Weather> callback) {
        if (mWeatherIsDirty) {
            queryWeatherFromRemoteDataSource(weatherId, callback);
        } else {
            mLocalDataSource.queryWeather(weatherId, new LoadDataCallback<Weather>() {
                @Override
                public void onDataLoaded(Weather data) {
                    callback.onDataLoaded(data);
                }

                @Override
                public void onDataNotAvailable() {
                    queryWeatherFromRemoteDataSource(weatherId, callback);
                }
            });
        }
    }

    /**
     * 加载必应每日一图
     */
    @Override
    public void queryBingPic(final LoadDataCallback<String> callback) {
        mLocalDataSource.queryBingPic(new LoadDataCallback<String>() {
            @Override
            public void onDataLoaded(String data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable() {
                queryBingPicFromRemoteDataSource(callback);
            }
        });
    }

    private void queryProvincesFromRemoteDataSource(final LoadDatasCallback<Province> callback) {
        mRemoteDataSource.queryProvinces(new LoadDatasCallback<Province>() {
            @Override
            public void onDatasLoaded(List<Province> datas) {
                saveProvincesInLocalDataSource(datas);
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void queryCitiesFromRemoteDataSource(Province province, final LoadDatasCallback<City> callback) {
        mRemoteDataSource.queryCities(province, new LoadDatasCallback<City>() {
            @Override
            public void onDatasLoaded(List<City> datas) {
                saveCitiesInLocalDataSource(datas);
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void queryCountiesFromRemoteDataSource(Province province, City city, final LoadDatasCallback<County> callback) {
        mRemoteDataSource.queryCounties(province, city, new LoadDatasCallback<County>() {
            @Override
            public void onDatasLoaded(List<County> datas) {
                saveCountiesInLocalDataSource(datas);
                callback.onDatasLoaded(datas);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void queryWeatherFromRemoteDataSource(String weatherId, final LoadDataCallback<Weather> callback) {
        mRemoteDataSource.queryWeather(weatherId, new LoadDataCallback<Weather>() {
            @Override
            public void onDataLoaded(Weather data) {
                mWeatherIsDirty = false;
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void queryBingPicFromRemoteDataSource(final LoadDataCallback<String> callback) {
        mRemoteDataSource.queryBingPic(new LoadDataCallback<String>() {
            @Override
            public void onDataLoaded(String data) {
                callback.onDataLoaded(data);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void saveProvincesInLocalDataSource(final List<Province> provinces) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataSupport.saveAll(provinces);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    private void saveCitiesInLocalDataSource(final List<City> cities) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataSupport.saveAll(cities);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    private void saveCountiesInLocalDataSource(final List<County> counties) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataSupport.saveAll(counties);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

}
