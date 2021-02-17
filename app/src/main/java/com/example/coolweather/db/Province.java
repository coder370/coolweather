package com.example.coolweather.db;

import androidx.annotation.NonNull;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {
    private int id ;    //省id
    private String provinceName ;   //省名
    private int provinceCode ;  //省代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    @NonNull
    @Override
    public String toString() {
        return id+"|"+provinceName+"|"+provinceCode ;
    }
}
