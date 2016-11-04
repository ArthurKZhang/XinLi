package com.xinli.xinli.testdao;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangyu on 11/3/16.
 */
public class SharedFileHelper {

    private SharedFileHelper() {
    }

    private static SharedFileHelper sharedFileHelper;

    public static SharedFileHelper getInstance() {
        if (sharedFileHelper == null) {
            return new SharedFileHelper();
        }
        return sharedFileHelper;
    }

//    public  SharedPreferences sharedPreferences;

    /**
     * 加载做过的题目的历史记录,针对student
     *
     * @param sharedPreferences
     * @return
     */
    public List<Map<String, String>> loadDidTest(SharedPreferences sharedPreferences) {
        List<Map<String, String>> itemMaps = new ArrayList<>();
//        Set<String> uris = sharedPreferences.getStringSet("testuri", null);/不能用sharedpreferences了
        String uri = sharedPreferences.getString("testuri", null);
        if (uri == null || uri.isEmpty()) {
            Log.e("test", "SharedFileHelper--loadDidTest-->nothing in sharedFile");
            return null;
        }
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("testUri", uri);
        tempMap.put("tag", "student");
        itemMaps.add(tempMap);
//        for (String testUri : uris) {
//            Map<String, String> tempMap = new HashMap<>();
//            tempMap.put("testUri", testUri);
//            tempMap.put("tag", "student");
//            itemMaps.add(tempMap);
//        }
        return itemMaps;
    }


    public List<Map<String, String>> loadUploadedTest(SharedPreferences sharedPreferences) {
        List<Map<String, String>> itemMaps = new ArrayList<>();
//        Set<String> fileNames = sharedPreferences.getStringSet("fileName", null);
//        if (fileNames == null || fileNames.isEmpty()) {
//            Log.e("test", "SharedFileHelper--loadUploadedTest-->nothing in sharedFile");
//            return null;
//        }
//        for (String testUri : fileNames) {
//            Map<String, String> tempMap = new HashMap<>();
//            tempMap.put("testUri", testUri);
//            tempMap.put("tag", "teacher");
//            itemMaps.add(tempMap);
//        }
        String uri = sharedPreferences.getString("fileName", null);
        if (uri == null || uri.isEmpty()) {
            Log.e("test", "SharedFileHelper--loadUploadedTest-->nothing in sharedFile");
            return null;
        }
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("testUri", uri);
        tempMap.put("tag", "teacher");
        itemMaps.add(tempMap);
        Log.d("test", itemMaps.toString());
        return itemMaps;
    }

}
