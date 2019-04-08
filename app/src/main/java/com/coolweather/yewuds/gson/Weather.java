package com.coolweather.yewuds.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public String status;
    public Basic basic;
    public Update update;
//    public AQI aqi;
//    public Now now;
//    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;


    public class Update{
        public String loc;
        public String utc;
    }
}
