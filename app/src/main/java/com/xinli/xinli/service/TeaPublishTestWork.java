package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CPublishTest;
import com.xinli.xinli.bean.protocol.SPublishTest;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 28/04/2017.
 */
public class TeaPublishTestWork {
    public static SPublishTest publish(CPublishTest infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_PUBLISH_TEST);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CPublishTest>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SPublishTest>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SPublishTest result = gson.fromJson(json, stype);
        return result;
    }
}
