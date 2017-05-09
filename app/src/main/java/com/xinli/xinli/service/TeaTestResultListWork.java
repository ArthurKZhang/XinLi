package com.xinli.xinli.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xinli.xinli.bean.protocol.CTeaTestResultItem;
import com.xinli.xinli.bean.protocol.STeaTestResultItem;
import com.xinli.xinli.net.NetHelper;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.util.Resource;

import java.lang.reflect.Type;

/**
 * Created by zhangyu on 04/05/2017.
 */
public class TeaTestResultListWork {
    public static STeaTestResultItem getTestResultItem(CTeaTestResultItem infos) {

        Log.d("test", "LoginWork-->login()-->infos:" + infos.toString());
        String url = NetHelper.createURL(Resource.ACTION_TEA_TEST_RESULT_ITEM);

        Gson gson = new Gson();
        Type ctype = new com.google.gson.reflect.TypeToken<CTeaTestResultItem>() {
        }.getType();
        Type stype = new com.google.gson.reflect.TypeToken<STeaTestResultItem>() {
        }.getType();

        String json = SimpleCommunicate.post(url, gson.toJson(infos, ctype));
        STeaTestResultItem result = gson.fromJson(json, stype);
        return result;
    }
}
