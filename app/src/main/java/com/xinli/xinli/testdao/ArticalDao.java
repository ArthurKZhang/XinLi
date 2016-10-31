package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.bean.test.Artical;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class ArticalDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public ArticalDao(Context context) {
        Log.d("Database", "ArticalDao --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void initData() {
        Log.d("Database", "ArticalDao --> initDao");

        //initialize data
        List<Artical> articals = new ArrayList<Artical>();
        Artical a1, a2, a3;
        a1 = new Artical("test/img1", "test artical 1");
        a2 = new Artical("test/img2", "test artical 2");
        a3 = new Artical("test/img3", "test artical 3");
        articals.add(a1);
        articals.add(a2);
        articals.add(a3);
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {
            for (Artical artical : articals) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_Artical
                        + " VALUES(null, ?, ?)", new Object[]{artical.uri,
                        artical.text});
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
    public List<Artical> query() {
        Log.d("Database", "ArticalDao --> query");
        ArrayList<Artical> articals = new ArrayList<Artical>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_Artical,
                null);
        while (c.moveToNext()) {
            Artical a = new Artical();
            a.id = c.getInt(c.getColumnIndex("_id"));
            a.uri = c.getString(c.getColumnIndex("uri"));
            a.text = c.getString(c.getColumnIndex("text"));
            articals.add(a);
        }
        c.close();
        return articals;
    }

    public Artical queryOneArticalByURI(String uri) {
        Log.d("Database", "TestIDao --> queryOneArticalByURI");
        Artical artical = new Artical();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_Artical + " WHERE uri=?",
                new String[]{uri});

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                artical.id = c.getInt(c.getColumnIndex("_id"));
                artical.uri = c.getString(c.getColumnIndex("uri"));
                artical.text = c.getString(c.getColumnIndex("text"));
            }
            c.close();
            return artical;
        } else if (c.getCount() > 1) {
            Log.e("error", "uri is NOT unique");
            return null;
        } else {
            return null;
        }
    }
}
