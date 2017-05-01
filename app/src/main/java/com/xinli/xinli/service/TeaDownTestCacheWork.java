package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CDownTeaTestCache;
import com.xinli.xinli.bean.protocol.CDownTeacherTestList;
import com.xinli.xinli.bean.protocol.SDownTeaTestCache;
import com.xinli.xinli.bean.protocol.SDownTeacherTestList;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 26/04/2017.
 */
public class TeaDownTestCacheWork {
    public static SDownTeaTestCache down(CDownTeaTestCache infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_TEA_TEST_CACHE);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CDownTeaTestCache>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SDownTeaTestCache>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SDownTeaTestCache result = gson.fromJson(json, stype);
        return result;
    }
}
