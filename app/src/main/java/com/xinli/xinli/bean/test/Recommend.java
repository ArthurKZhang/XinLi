package com.xinli.xinli.bean.test;

/**
 * Created by zhangyu on 10/28/16.
 * sBuffer.append("CREATE TABLE [" + TABLE_NAME_Artical + "] (");
 sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
 sBuffer.append("[uri] VARCHAR(20),");
 sBuffer.append("[img] INTEGER,");
 sBuffer.append("[shortdes] VARCHAR(20))");
 */
public class Recommend {
    public int id;
    public String uri;
    public int img;
    public String shortdes;

    public Recommend(String uri, int img, String shortdes) {
        this.uri = uri;
        this.img = img;
        this.shortdes = shortdes;
    }
    public Recommend(){}
}
