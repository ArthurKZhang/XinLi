package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhangyu on 10/26/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "TestDB.db";

    // 数据表名，一个数据库中可以有多个表（）
    public static final String TABLE_NAME_VF = "ViewFlipper";
    public static final String TABLE_NAME_Artical = "Artical";
    public static final String TABLE_NAME_TestListI = "TestListItem";
    public static final String TABLE_NAME_TestI = "TestItem";
    public static final String TABLE_NAME_Recom = "Recommend";

//    ---------------------------------------------------------

    public static final String TABLE_UPLOAD_TEST_LIST = "uploadlist";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d("Database", "DatabaseHelper Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作

        Log.d("Database", "DatabaseHelper onCreate");

        createVFTable(db);
        createArticalTable(db);
        createTestListItemTable(db);
        createTestItemTable(db);
//        createRecommendTable(db);
        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

        createUploadTestListTable(db);

    }

    /**
     * String userName;
     private String testName;
     private String testId;
     private String cachePath;
     * @param db
     */
    private void createUploadTestListTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_UPLOAD_TEST_LIST + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[userName] TEXT,");
        sBuffer.append("[testName] TEXT,");
        sBuffer.append("[testId] TEXT,");
        sBuffer.append("[cachePath] TEXT)");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    private void createRecommendTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME_Artical + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[img] INTEGER,");
        sBuffer.append("[shortdes] VARCHAR(20))");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    private void createTestItemTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME_TestI + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[answertype] INTEGER,");
        sBuffer.append("[choosen] INTEGER,");
        sBuffer.append("[items] VARCHAR(40),");
        sBuffer.append("[des] VARCHAR(200))");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    private void createTestListItemTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME_TestListI + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[category] VARCHAR(10),");
        sBuffer.append("[img] INTEGER,");
        sBuffer.append("[readn] INTEGER,");
        sBuffer.append("[testn] INTEGER,");
        sBuffer.append("[shortdes] VARCHAR(20))");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    private void createArticalTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME_Artical + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[text] VARCHAR(200))");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }

    private void createVFTable(SQLiteDatabase db) {
        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME_VF + "] (");
        sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[uri] VARCHAR(20),");
        sBuffer.append("[img] INTEGER)");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}