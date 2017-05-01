package com.xinli.xinli.dao;

/**
 * Created by zhangyu on 26/04/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinli.xinli.bean.bean.UploadRecord;
import com.xinli.xinli.testdao.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 10/27/16.
 */
public class UploadListDao {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public UploadListDao(Context context) {
        Log.d("Database", "UploadListDao --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * ("CREATE TABLE [" + TABLE_UPLOAD_TEST_LIST + "] (");
     * ("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
     * ("[userName] TEXT,");
     * ("[testName] TEXT,");
     * ("[testId] TEXT,");
     * ("[cachePath] TEXT)");
     *
     * @param record
     * @return
     */
    public boolean insert(UploadRecord record) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_UPLOAD_TEST_LIST
                            + " VALUES(null, ?, ?, ?, ?)",
                    new Object[]{record.getUserName(), record.getTestName(), record.getTestId(), record.getCachePath()});
            return true;
        } catch (SQLException e) {
            Log.e("Database", "UploadListDao-->insert exception");
            return false;
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * get three datas defaultly
     *
     * @return
     */
    public List<UploadRecord> query(String uname) {
        Log.d("Database", "UploadListDao --> query by user name:" + uname);
        ArrayList<UploadRecord> uploadRecords = new ArrayList<UploadRecord>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_UPLOAD_TEST_LIST + " WHERE userName = ?",
                new String[]{uname});
        while (c.moveToNext()) {
            String userName = c.getString(c.getColumnIndex("userName"));
            String testName = c.getString(c.getColumnIndex("testName"));
            String testId = c.getString(c.getColumnIndex("testId"));
            String cachePath = c.getString(c.getColumnIndex("cachePath"));

            UploadRecord uploadRecord = new UploadRecord(userName, testName, testId, cachePath);
            uploadRecords.add(uploadRecord);
        }
        c.close();
        return uploadRecords;
    }

    public boolean update(String testNameAskey, String colum, String value) {
//        update  test_t  set value = 'xiaoli' where id = 3
        db.beginTransaction();
        try {
            db.execSQL("update " + DatabaseHelper.TABLE_UPLOAD_TEST_LIST
                    + " set " + colum + " = " + "\'" + value + "\'" + " where testName = " + "\'" + testNameAskey + "\'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }

    }
}
