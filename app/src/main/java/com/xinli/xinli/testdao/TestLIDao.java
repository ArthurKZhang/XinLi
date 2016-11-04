package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.test.TestLI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class TestLIDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public TestLIDao(Context context) {
        Log.d("Database", "TestLIDao --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void initData() {
        Log.d("Database", "TestLIDao --> initDao");

        //initialize data
        List<TestLI> tlis = new ArrayList<TestLI>();

        for (int i = 0; i < 10; i++) {
            TestLI t = new TestLI("test/love/testListItem" + i, "love", R.mipmap.ic_launcher, 0, 0, "shortDescription");
            tlis.add(t);
        }
        for (int i = 0; i < 10; i++) {
            TestLI t = new TestLI("test/work/testListItem" + i, "work", R.mipmap.ic_launcher, 0, 0, "shortDescription");
            tlis.add(t);
        }
        for (int i = 0; i < 10; i++) {
            TestLI t = new TestLI("test/family/testListItem" + i, "family", R.mipmap.ic_launcher, 0, 0, "shortDescription");
            tlis.add(t);
        }


        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {
            for (TestLI tli : tlis) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_TestListI
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)",
                        new Object[]{tli.uri, tli.category, tli.img, tli.readn, tli.testn, tli.shortdes});
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * @return
     */
    public List<TestLI> query() {
        Log.d("Database", "TestLIDao --> query");
        ArrayList<TestLI> tlis = new ArrayList<TestLI>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_TestListI,
                null);
        while (c.moveToNext()) {
            TestLI tli = new TestLI();
            tli.id = c.getInt(c.getColumnIndex("_id"));
            tli.uri = c.getString(c.getColumnIndex("uri"));
            tli.category = c.getString(c.getColumnIndex("category"));
            tli.img = c.getInt(c.getColumnIndex("img"));
            tli.readn = c.getInt(c.getColumnIndex("readn"));
            tli.testn = c.getInt(c.getColumnIndex("testn"));
            tli.shortdes = c.getString(c.getColumnIndex("shortdes"));
            tlis.add(tli);
        }
        c.close();
        return tlis;
    }

    public List<TestLI> queryByCategory(String category){
        Log.d("Database", "TestLIDao --> query");
        ArrayList<TestLI> tlis = new ArrayList<TestLI>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_TestListI+" WHERE category = ?",
                new String[]{category});
        while (c.moveToNext()) {
            TestLI tli = new TestLI();
            tli.id = c.getInt(c.getColumnIndex("_id"));
            tli.uri = c.getString(c.getColumnIndex("uri"));
            tli.category = c.getString(c.getColumnIndex("category"));
            tli.img = c.getInt(c.getColumnIndex("img"));
            tli.readn = c.getInt(c.getColumnIndex("readn"));
            tli.testn = c.getInt(c.getColumnIndex("testn"));
            tli.shortdes = c.getString(c.getColumnIndex("shortdes"));
            tlis.add(tli);
        }
        c.close();
        return tlis;
    }
}
/*
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
