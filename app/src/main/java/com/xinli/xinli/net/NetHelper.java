package com.xinli.xinli.net;

import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.Resource;

import java.net.URL;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class NetHelper {
    public static String createURL(String actionName){
        StringBuilder url = new StringBuilder();
        url.append("http://").
                append(AppManager.serverIP).
                append(":8080/").
                append(AppManager.serverName).
                append("/").
                append(actionName);
        return url.toString();
    }
}
