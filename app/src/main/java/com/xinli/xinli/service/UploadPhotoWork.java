package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.mine.CUploadPhoto;
import com.xinli.xinli.bean.mine.SUploadPhoto;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 25/04/2017.
 */
public class UploadPhotoWork {

    public static SUploadPhoto upload(CUploadPhoto infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_UPLOAD_PHOTO);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CUploadPhoto>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SUploadPhoto>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SUploadPhoto result = gson.fromJson(json, stype);
        return result;
    }
}
