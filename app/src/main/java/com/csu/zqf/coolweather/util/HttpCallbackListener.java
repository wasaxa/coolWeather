package com.csu.zqf.coolweather.util;

/**
 * Created by zqf on 2017/9/4.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);

}
