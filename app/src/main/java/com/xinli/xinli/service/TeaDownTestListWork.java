package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CDownTeacherTestList;
import com.xinli.xinli.bean.protocol.CLogin;
import com.xinli.xinli.bean.protocol.SDownTeacherTestList;
import com.xinli.xinli.bean.protocol.SLogin;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 26/04/2017.
 */
public class TeaDownTestListWork {
    public static SDownTeacherTestList down(CDownTeacherTestList infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_DOWN_TEACHER_TEST_LIST);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CDownTeacherTestList>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SDownTeacherTestList>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SDownTeacherTestList result = gson.fromJson(json, stype);
        return result;
    }
}
