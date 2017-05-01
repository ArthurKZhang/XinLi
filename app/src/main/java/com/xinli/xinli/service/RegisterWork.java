package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CRegister;
import com.xinli.xinli.bean.protocol.SRegister;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class RegisterWork {

    public static SRegister register(HashMap<String, Object> infos) {
        Log.d("test", "RegisterWork-->register()-->infos:" + infos.toString());

        StringBuilder url = new StringBuilder();
        url.append("http://").
                append(AppManager.serverIP).
                append(":8080/").
                append(AppManager.serverName).
                append("/").
                append(Resource.ACTION_REGISTER);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CRegister>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SRegister>() {
        }.getType();

        CRegister cRegister = new CRegister((String) infos.get("name"),
                (String) infos.get("password"),
                (String) infos.get("institution"),
                (Date) infos.get("enrollmentDate"),
                (String)infos.get("type"));
        String json = gson.toJson(cRegister, ctype);
        //使用POST来提交数据
        String result = SimpleCommunicate.post(url.toString(), json);

        SRegister sRegister = gson.fromJson(result, stype);

        return sRegister;
    }
}
