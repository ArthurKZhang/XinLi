package com.xinli.xinli.bean.protocol;


/**
 * Created by zhangyu on 26/03/2017.
 */
public class CTeacherPostTest {
    private String userName;
    private String testName;
    private String testAsJson;

    public CTeacherPostTest(String userName, String testName, String testAsJson) {
        this.userName = userName;
        this.testName = testName;
        this.testAsJson = testAsJson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestAsJson() {
        return testAsJson;
    }

    public void setTestAsJson(String testAsJson) {
        this.testAsJson = testAsJson;
    }

}
