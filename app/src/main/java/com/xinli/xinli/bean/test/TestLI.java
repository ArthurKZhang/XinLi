package com.xinli.xinli.bean.test;

/**
 * Created by zhangyu on 10/27/16.
 * sBuffer.append("CREATE TABLE [" + TABLE_NAME_TestListI + "] (");
 sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
 sBuffer.append("[uri] VARCHAR(20),");
 sBuffer.append("[category] VARCHAR(10),");
 sBuffer.append("[img] INTEGER,");
 sBuffer.append("[readn] INTEGER,");
 sBuffer.append("[testn] INTEGER,");
 sBuffer.append("[shortdes] VARCHAR(20))");
 */
public class TestLI {
    public int id;
    public String uri;
    public String category;
    public int img;
    public int readn;
    public int testn;
    public String shortdes;

    public TestLI(){}

    public TestLI(String uri, String category, int img, int readn, int testn, String shortdes) {
        this.uri = uri;
        this.category = category;
        this.img = img;
        this.readn = readn;
        this.testn = testn;
        this.shortdes = shortdes;
    }
}
