package com.xinli.xinli.bean.protocol;

import com.xinli.xinli.bean.bean.UploadRecord;

import java.util.List;

/**
 * Created by zhangyu on 26/04/2017.
 */
public class SDownTeacherTestList {
    List<UploadRecord> records;

    @Override
    public String toString() {
        return "SDownTeacherTestList{" +
                "records=" + records +
                '}';
    }

    public List<UploadRecord> getRecords() {
        return records;
    }

    public void setRecords(List<UploadRecord> records) {
        this.records = records;
    }

    public SDownTeacherTestList(List<UploadRecord> records) {

        this.records = records;
    }
}
