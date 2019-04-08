package com.coolweather.yewuds.db;

import org.litepal.crud.DataSupport;

/**
 * 省份表
 */
public class Province extends DataSupport {
    private int id;
    private String provinceName;//省名称
    private String provinceCode;//省代号

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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
