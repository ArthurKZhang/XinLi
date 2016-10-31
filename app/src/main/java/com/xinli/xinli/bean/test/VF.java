package com.xinli.xinli.bean.test;

/**
 * Created by zhangyu on 10/27/16.
 */
public class VF {
    //    sBuffer.append("CREATE TABLE [" + TABLE_NAME_VF + "] (");
//    sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
//    sBuffer.append("[uri] VARCHAR(20),");
//    sBuffer.append("[img] INTEGER)");
    public int id;
    public String uri;
    public int img;

    public VF(String uri, int img) {
        this.uri = uri;
        this.img = img;
    }

    public VF() {
    }
}
