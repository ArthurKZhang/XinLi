package com.xinli.xinli.bean.bean;

import java.util.Date;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class PublishRecord {
    private Date startDate;
    private Date endDate;
    private String forEnrollYear;
    private String teacherName;
    private String testId;
    private String forInstitude;
    private String testName;

    @Override
    public String toString() {
        return "PublishRecord{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", forEnrollYear='" + forEnrollYear + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", testId='" + testId + '\'' +
                ", forInstitude='" + forInstitude + '\'' +
                ", testName='" + testName + '\'' +
                '}';
    }

    public PublishRecord(Date startDate, Date endDate, String forEnrollYear, String teacherName, String testId, String forInstitude, String testName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.forEnrollYear = forEnrollYear;
        this.teacherName = teacherName;
        this.testId = testId;
        this.forInstitude = forInstitude;
        this.testName = testName;
    }

    public String getTestName() {

        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getForEnrollYear() {
        return forEnrollYear;
    }

    public void setForEnrollYear(String forEnrollYear) {
        this.forEnrollYear = forEnrollYear;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getForInstitude() {
        return forInstitude;
    }

    public void setForInstitude(String forInstitude) {
        this.forInstitude = forInstitude;
    }

}
