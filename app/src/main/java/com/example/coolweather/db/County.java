package com.example.coolweather.db;

import org.litepal.crud.DataSupport;
// 县名
public class County extends DataSupport {
    private int id ;   // 县id
    private String countryName ;    //县名
    private String weatherId ;  //县对应天气
    private int cityId ;    //当前县所属的城市id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
