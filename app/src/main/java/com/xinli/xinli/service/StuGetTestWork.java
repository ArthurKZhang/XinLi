package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CStuGetTest;
import com.xinli.xinli.bean.protocol.SStuGetTest;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class StuGetTestWork {

    public static SStuGetTest getTest(CStuGetTest infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_STU_GET_TEST);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CStuGetTest>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SStuGetTest>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SStuGetTest result = gson.fromJson(json, stype);
        return result;
    }

}
