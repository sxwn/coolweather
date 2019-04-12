package com.coolweather.yewuds;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.yewuds.gson.Forecast;
import com.coolweather.yewuds.gson.Weather;
import com.coolweather.yewuds.util.HttpUtil;
import com.coolweather.yewuds.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView bingPicImg;
    public SwipeRefreshLayout refreshLayout;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private SharedPreferences prefs;
    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //状态栏颜色设置为透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String weatherString = prefs.getString("weather", null);
        final String weatherId;
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时直接解析天气数据
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }

    public void requestWeather(final String weatherId) {
//        String weatherUrl = "http://guolin.tech/api/weather?cityId=" + weatherId + "&key=98052e2e825b4ccc9ab02730628d2d23";
        String weatherUrl = "https://free-api.heweather.net/s6/weather/forecast?location=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        Log.d("weip", "weatherUrl:" + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
//        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather + "℃";
//        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
//        titleUpdateTime.setText(updateTime);
//        degreeText.setText(degree);
//        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forcast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forcast.date);
//            infoText.setText(forcast.more.info);
            maxText.setText(forcast.tmp_max);
            minText.setText(forcast.tmp_min);
            forecastLayout.addView(view);
        }
//        if (weather.aqi != null) {
//            aqiText.setText(weather.aqi.city.aqi);
//            pm25Text.setText(weather.aqi.city.pm25);
//        }
//        String comfort = "舒适度:" + weather.suggestion.comfort.info;
//        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
//        String sport = "运动建议:" + weather.suggestion.sport.info;
//        comfortText.setText(comfort);
//        carWashText.setText(carWash);
//        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        bingPicImg = findViewById(R.id.bing_pic_mg);
        navButton = findViewById(R.id.nav_button);
        navButton.setOnClickListener(this);
        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        drawerLayout = findViewById(R.id.drawer_layout);
//        if (bing_pic != null){
//            Log.d("weip","address:"+bing_pic);
//            Glide.with(this).load(bing_pic).into(bingPicImg);
//        }else{
        loadBingPic();
//        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                Log.d("weip", "address:" + bingPic);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_button:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }
}
