package com.xinli.xinli.testdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.test.Recommend;
import com.xinli.xinli.bean.test.VF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class RecommendDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public RecommendDao(Context context) {
        Log.d("Database", "RecommendDao --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }


    /**
     * get four datas defaultly
     * @return
     */
    public List<Recommend> query() {
        Log.d("Database", "RecommendDao --> query");
        ArrayList<Recommend> vfs = new ArrayList<Recommend>();

        Recommend r1,r2,r3,r4;
        r1= new Recommend("test/love/testListItem1",R.mipmap.ic_launcher,"shortDescription");
        r2= new Recommend("test/work/testListItem1",R.mipmap.ic_launcher,"shortDescription");
        r3= new Recommend("test/family/testListItem1",R.mipmap.ic_launcher,"shortDescription");
        r4= new Recommend("test/love/testListItem2",R.mipmap.ic_launcher,"shortDescription");
        vfs.add(r1); vfs.add(r2); vfs.add(r3); vfs.add(r4);
        return vfs;
    }

}
