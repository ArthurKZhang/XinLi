package com.xinli.xinli.bean.test;

/**
 *  sBuffer.append("CREATE TABLE [" + TABLE_NAME_TestI + "] (");
 sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
 sBuffer.append("[uri] VARCHAR(20),");
 sBuffer.append("[answertype] INTEGER,");
 sBuffer.append("[choosen] INTEGER,");
 sBuffer.append("[items] VARCHAR(40),");
 sBuffer.append("[des] VARCHAR(200))");
 */
public class TestI {
    public int id;
    public String uri;
    public int answertype;
    public int choosen;
    public String items;
    public String des;

    public TestI(){}

    public TestI(String uri, int answertype, int choosen, String items, String des) {
        this.uri = uri;
        this.answertype = answertype;
        this.choosen = choosen;
        this.items = items;
        this.des = des;
    }
}
