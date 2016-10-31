package com.xinli.xinli.bean.test;

/**
 * Created by zhangyu on 10/27/16.
 */
public class Artical {
    /*
    sBuffer.append("CREATE TABLE [" + TABLE_NAME_Artical + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[text] VARCHAR(200))")
     */
    public int id;
    public String uri;
    public String text;

    public Artical() {
    }

    public Artical(String uri, String text) {
        this.uri = uri;
        this.text = text;
    }
}
