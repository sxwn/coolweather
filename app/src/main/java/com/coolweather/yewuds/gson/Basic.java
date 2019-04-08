package com.coolweather.yewuds.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    //json字段和java字段之间建立映射关系
    @SerializedName("parent_city")
    public String cityName;
    @SerializedName("cid")
    public String weatherId;
//
//    public Update update;
//
//    public class Update{
//        @SerializedName("loc")
//        public String updateTime;
//
//        @SerializedName("utc")
//        public String utc;
//    }
}
