package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.bean.Test;
import com.xinli.xinli.bean.mine.CTeacherPostTest;
import com.xinli.xinli.bean.mine.STeacherPostTest;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by zhangyu on 26/03/2017.
 */
public class TeacherPostTestWork {
    public static STeacherPostTest postTest(HashMap<String, Object> infos) {

        CTeacherPostTest data = new CTeacherPostTest((String) infos.get("id"), (Test) infos.get("test"));

        Log.d("nettest", "CTeacherPostTest:" + data.toString());

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CTeacherPostTest>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<STeacherPostTest>() {
        }.getType();


        String json = gson.toJson(data, ctype);

        String url = NetHelper.createURL(Resource.ACTION_TEACHER_POST_TEST);
        Log.d("nettest", "url:" + url);

        //使用POST来提交数据
        String result = SimpleCommunicate.post(url, json);

        STeacherPostTest sTeacherPostTest = gson.fromJson(result, stype);
        return sTeacherPostTest;
    }
}
