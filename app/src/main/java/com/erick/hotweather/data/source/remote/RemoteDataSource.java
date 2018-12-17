package com.erick.hotweather.data.source.remote;

import com.erick.hotweather.app.AppExecutors;
import com.erick.hotweather.app.LoadedApp;
import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;
import com.erick.hotweather.data.source.IDataSource;
import com.erick.hotweather.network.NetworkUtil;
import com.erick.hotweather.util.HttpUtil;
import com.erick.hotweather.util.SPUtil;
import com.erick.hotweather.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RemoteDataSource implements IDataSource {

    private static final String ADDRESS_PROVINCE = "http://guolin.tech/api/china";

    private static volatile RemoteDataSource INSTANCE;
    private AppExecutors mAppExecutors;

    private RemoteDataSource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static RemoteDataSource getInstance(AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (RemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void queryProvinces(final LoadDatasCallback<Province> callback) {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) {
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
            return;
        }
        HttpUtil.httpGet(ADDRESS_PROVINCE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<Province> datas = Utility.handleProvinceResponse(responseText);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (datas.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(datas);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void queryCities(final Province province, final LoadDatasCallback<City> callback) {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) {
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
            return;
        }
        String address = ADDRESS_PROVINCE + "/" + province.getProvinceCode();
        HttpUtil.httpGet(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<City> datas = Utility.handleCityResponse(responseText, province.getProvinceCode());
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (datas.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(datas);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void queryCounties(Province province, final City city, final LoadDatasCallback<County> callback) {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) {
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
            return;
        }
        int provinceCode = province.getProvinceCode();
        int cityCode = city.getCityCode();
        String address = ADDRESS_PROVINCE + "/" + provinceCode + "/" + cityCode;
        HttpUtil.httpGet(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<County> datas = Utility.handleCountyResponse(responseText, city.getCityCode());
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (datas.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDatasLoaded(datas);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void refreshWeather() {

    }

    @Override
    public void queryWeather(String weatherId, final LoadDataCallback<Weather> callback) {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) {
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
            return;
        }
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=ddc86730c0e54e11bd313ab04423000e";
        HttpUtil.httpGet(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final Weather weather = responseText != null ? Utility.handleWeatherResponse(responseText) : null;
                final boolean result = weather != null && "ok".equals(weather.status);
                if (result) {
                    SPUtil.putString("weather", responseText);
                }
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            callback.onDataLoaded(weather);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void queryBingPic(final LoadDataCallback<String> callback) {
        if (!NetworkUtil.isNetworkAvailable(LoadedApp.getContext())) {
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable();
                }
            });
            return;
        }
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.httpGet(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                if (bingPic != null) {
                    SPUtil.putString("bing_pic", bingPic);
                }
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (bingPic != null) {
                            callback.onDataLoaded(bingPic);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        });
    }

}
