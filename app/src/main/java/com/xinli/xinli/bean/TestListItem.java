package com.xinli.xinli.bean;

import java.util.Random;

/**
 * Created by zhangyu on 10/11/16.
 */
public class TestListItem {
    private String imageURI;
    private int viewCount;
    private int testCount;
    private String testURI;

    /**
     *
     * @param imageURI  Image resource,
     * @param viewCount number that this test has been viewed
     * @param testCount number that this test has been tested
     * @param testURI   testItem resource
     */
    public TestListItem(String imageURI, int viewCount, int testCount, String testURI) {
        this.imageURI = imageURI;
        this.viewCount = viewCount;
        this.testCount = testCount;
        this.testURI = testURI;
    }

    public String getTestURI() {
        return testURI;
    }

    public void setTestURI(String testURI) {
        this.testURI = testURI;
    }

    public TestListItem(){}

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    @Override
    public String toString() {
        return "TestListItem{" +
                "imageURI='" + imageURI + '\'' +
                ", viewCount=" + viewCount +
                ", testCount=" + testCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestListItem that = (TestListItem) o;
        return viewCount == that.viewCount &&
                testCount == that.testCount &&
                testURI.equals(that.getTestURI());
    }

    @Override
    public int hashCode() {
        return new Random().hashCode();
    }
}
