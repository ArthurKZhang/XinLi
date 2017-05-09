package com.xinli.xinli.bean.protocol;

/**
 * Created by zhangyu on 04/05/2017.
 */
public class CTeaDownTestResult {
    private String testId;

    @Override
    public String toString() {
        return "CTeaDownTestResult{" +
                "testId='" + testId + '\'' +
                '}';
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public CTeaDownTestResult(String testId) {

        this.testId = testId;
    }
}
