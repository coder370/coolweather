package com.example.coolweather.db;

import org.litepal.crud.DataSupport;
// 县名
public class County extends DataSupport {
    private int id ;   // 县id
    private String countryName ;    //县名
    private String weatherId ;  //县对应天气
    private int cityId ;    //当前县所属的城市id
}
