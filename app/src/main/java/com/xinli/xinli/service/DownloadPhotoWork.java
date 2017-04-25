package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.mine.CDownloadPhoto;
import com.xinli.xinli.bean.mine.SDownloadPhoto;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 25/04/2017.
 */
public class DownloadPhotoWork {

    public static SDownloadPhoto download(CDownloadPhoto infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_REQUEST_PHOTO);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CDownloadPhoto>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<SDownloadPhoto>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        SDownloadPhoto result = gson.fromJson(json, stype);
        return result;
    }
}
