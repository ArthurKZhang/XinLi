package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CStuHandResult;
import com.xinli.xinli.bean.protocol.SStuHandResult;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class StuHandResultWork {

    public static SStuHandResult handResult(CStuHandResult infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_STU_HAND_RESULT);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CStuHandResult>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SStuHandResult>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SStuHandResult result = gson.fromJson(json, stype);
        return result;
    }

}
