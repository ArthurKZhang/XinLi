package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.mine.CLogin;
import com.xinli.xinli.bean.mine.CRegister;
import com.xinli.xinli.bean.mine.SLogin;
import com.xinli.xinli.bean.mine.SRegister;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class LoginWork {

    public static SLogin login(CLogin infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_LOGIN);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CLogin>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SLogin>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SLogin result = gson.fromJson(json, stype);
        return result;
    }
}
