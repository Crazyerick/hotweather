package com.erick.hotweather.util;

import android.text.TextUtils;

import com.erick.hotweather.data.db.City;
import com.erick.hotweather.data.db.County;
import com.erick.hotweather.data.db.Province;
import com.erick.hotweather.data.entity.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static List<Province> handleProvinceResponse(String response) {
        List<Province> provinceList = null;
        if (!TextUtils.isEmpty(response)) {
            try {
                provinceList = new ArrayList<>();
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    provinceList.add(province);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return provinceList;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static List<City> handleCityResponse(String response, int provinceId) {
        List<City> cityList = null;
        if (!TextUtils.isEmpty(response)) {
            try {
                cityList = new ArrayList<>();
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    cityList.add(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cityList;
    }

    /**
     * 解释和处理服务器返回的县级数据
     */
    public static List<County> handleCountyResponse(String response, int cityId) {
        List<County> countyList = null;
        if (!TextUtils.isEmpty(response)) {
            try {
                countyList = new ArrayList<>();
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    countyList.add(county);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return countyList;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
                String weatherContent = jsonArray.getJSONObject(0).toString();
                return new Gson().fromJson(weatherContent, Weather.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
