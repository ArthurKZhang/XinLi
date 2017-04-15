package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.test.VF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class VFDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public VFDao(Context context) {
        Log.d("Database", "VFDao --> Constructor");
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
        Log.d("Database", "VFDao --> initDao");

        //initialize data
        List<VF> vfs = new ArrayList<VF>();
        VF v1,v2,v3;
        v1=  new VF("test/img1", R.drawable.vf_emotion);
        v2=  new VF("test/img2", R.drawable.vf_emotion_by_gene);
        v3=  new VF("test/img3", R.drawable.vf_take_your_time);
        vfs.add(v1); vfs.add(v2); vfs.add(v3);


        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (VF vf : vfs)
            {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME_VF
                        + " VALUES(null, ?, ?)", new Object[] { vf.uri,
                        vf.img});
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * get three datas defaultly
     * @return
     */
    public List<VF> query() {
        Log.d("Database", "VFDao --> query");
        ArrayList<VF> vfs = new ArrayList<VF>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_VF,
                null);
        while (c.moveToNext())
        {
            VF vf = new VF();
            vf.id = c.getInt(c.getColumnIndex("_id"));
            vf.uri = c.getString(c.getColumnIndex("uri"));
            vf.img = c.getInt(c.getColumnIndex("img"));
            vfs.add(vf);
        }
        c.close();
        return vfs;
    }
}
