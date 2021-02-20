package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;
// 当前天气类
public class Now {
    @SerializedName("tmp")
    public String temperature ;

    @SerializedName("cond")
    public More more ;

    public class More{
        @SerializedName("info")
        public String info ;
    }
}
