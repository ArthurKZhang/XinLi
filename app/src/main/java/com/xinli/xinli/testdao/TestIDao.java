package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.test.TestI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class TestIDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public TestIDao(Context context) {
        Log.d("Database", "TestIDao --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * save three datas defaultly
     */
    public void initData() {
        Log.d("Database", "TestIDao --> initDao");

        //initialize data
        List<TestI> tis = new ArrayList<TestI>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                TestI t = new TestI("test/love/testListItem" + i, 1, 3, "123/234/455", "description");
                tis.add(t);
            }

        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                TestI t = new TestI("test/work/testListItem" + i, 2, 3, "123/234/455", "description");
                tis.add(t);
            }

        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                TestI t = new TestI("test/family/testListItem" + i, 2, 3, "123/234/455", "description");
                tis.add(t);
            }

        }

        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {
            for (TestI t : tis) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_TestI
                        + " VALUES(null, ?, ?, ?, ?, ?)", new Object[]{t.uri,
                        t.answertype, t.choosen, t.items, t.des});
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
     * get three datas defaultly
     *
     * @return
     */
    public List<TestI> query() {
        Log.d("Database", "TestIDao --> query");
        ArrayList<TestI> vfs = new ArrayList<TestI>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_TestI,
                null);
        while (c.moveToNext()) {
            TestI t = new TestI();
            t.id = c.getInt(c.getColumnIndex("_id"));
            t.uri = c.getString(c.getColumnIndex("uri"));
            t.items = c.getString(c.getColumnIndex("items"));
            t.des = c.getString(c.getColumnIndex("des"));
            t.answertype = c.getInt(c.getColumnIndex("answertype"));
            t.choosen = c.getInt(c.getColumnIndex("choosen"));

            vfs.add(t);
        }
        c.close();
        return vfs;
    }

    public List<TestI> queryByURI(String uri) {
        Log.d("Database", "TestIDao --> queryByURI");
        ArrayList<TestI> vfs = new ArrayList<TestI>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_TestI + " WHERE uri=?",
                new String[]{uri});
        while (c.moveToNext()) {
            TestI t = new TestI();
            t.id = c.getInt(c.getColumnIndex("_id"));
            t.uri = c.getString(c.getColumnIndex("uri"));
            t.items = c.getString(c.getColumnIndex("items"));
            t.des = c.getString(c.getColumnIndex("des"));
            t.answertype = c.getInt(c.getColumnIndex("answertype"));
            t.choosen = c.getInt(c.getColumnIndex("choosen"));

            vfs.add(t);
        }
        c.close();
        return vfs;
    }
}
