package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CTeaDownTestResult;
import com.xinli.xinli.bean.protocol.STeaDownTestResult;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 04/05/2017.
 */
public class TeaDownTestResultWork {
    public static STeaDownTestResult getResult(CTeaDownTestResult infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_LOGIN);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CTeaDownTestResult>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<STeaDownTestResult>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        STeaDownTestResult result = gson.fromJson(json, stype);
        return result;
    }
}
