package com.csu.zqf.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csu.zqf.coolweather.R;
import com.csu.zqf.coolweather.util.HttpCallbackListener;
import com.csu.zqf.coolweather.util.HttpUtil;
import com.csu.zqf.coolweather.util.Utility;

/**
 * Created by zqf on 2017/9/6.
 */
public class WeatherInfoActivity extends Activity {

    private LinearLayout weatherInfoLayout;
    private TextView cityNameTv,publishTextTv,currentDateTv,weatherDespTv,temp1Tv,temp2Tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout=findViewById(R.id.weather_info_layout);
        cityNameTv = findViewById(R.id.city_name);
        publishTextTv=findViewById(R.id.publish_text);
        currentDateTv=findViewById(R.id.current_date);
        weatherDespTv=findViewById(R.id.weather_desp);
        temp1Tv=findViewById(R.id.temp1);
        temp2Tv=findViewById(R.id.temp2);

        String countyCode = getIntent().getStringExtra("countyCode");
        queryWeatherCode(countyCode);

    }


    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameTv.setText(preferences.getString("city_name",""));
        temp1Tv.setText(preferences.getString("temp1",""));
        temp2Tv.setText(preferences.getString("temp2",""));
        weatherDespTv.setText(preferences.getString("weather_desp",""));
        publishTextTv.setText("今天"+preferences.getString("publish_time",""));
        currentDateTv.setText(preferences.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameTv.setVisibility(View.VISIBLE);
    }

    private void queryFromServer(final String code,final String type){

        HttpUtil.sendHttpRequest(code, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)){
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else {
                    Utility.handleWeatherResponse(WeatherInfoActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherInfoActivity.this,"同步失败...",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
