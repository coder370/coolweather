package com.example.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utility {
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response) ;
                for(int i=0 ; i<jsonArray.length() ; ++i){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    Province province = new Province() ;
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    province.save() ;
                }
                return true ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false ;
    }
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response) ;
                for(int i=0 ; i<jsonArray.length() ; ++i){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    City city = new City() ;
                    city.setProvinceId(provinceId);
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.save() ;
                }
                return true ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false ;
    }
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response) ;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    County county = new County() ;
                    county.setCityId(cityId);
                    county.setCountryName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.save() ;
                }
                return true ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false ;
    }
}
