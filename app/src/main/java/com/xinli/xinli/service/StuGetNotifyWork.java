package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CStuGetNotify;
import com.xinli.xinli.bean.protocol.SStuGetNotify;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class StuGetNotifyWork {
    public static SStuGetNotify getNotify(CStuGetNotify infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_STU_GET_NOTIFY);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CStuGetNotify>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SStuGetNotify>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SStuGetNotify result = gson.fromJson(json, stype);
        return result;
    }
}
