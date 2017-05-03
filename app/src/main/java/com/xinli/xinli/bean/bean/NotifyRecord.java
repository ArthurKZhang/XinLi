package com.xinli.xinli.bean.bean;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Date;
import java.util.Objects;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class NotifyRecord {
    private String testId;
    private Date endDate;
    private String teaName;
    private String testName;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotifyRecord that = (NotifyRecord) o;
        return Objects.equals(testId, that.testId) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(teaName, that.teaName) &&
                Objects.equals(testName, that.testName);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(testId, endDate, teaName, testName);
    }

    @Override
    public String toString() {
        return "SStuGetNotify{" +
                "testId='" + testId + '\'' +
                ", endDate=" + endDate +
                ", teaName='" + teaName + '\'' +
                ", testName='" + testName + '\'' +
                '}';
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public NotifyRecord(String testId, Date endDate, String teaName, String testName) {

        this.testId = testId;
        this.endDate = endDate;
        this.teaName = teaName;
        this.testName = testName;
    }
}
