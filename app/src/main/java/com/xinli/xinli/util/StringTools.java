package com.xinli.xinli.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/29/16.
 */
public class StringTools {
    public static List<String> String2Strings(String string){
        String[] stringArr = string.split("/");
        List<String> sl = new ArrayList<String>();
        for(int i=0;i<stringArr.length;i++)
            sl.add(stringArr[i]);

        return sl;
    }
}
