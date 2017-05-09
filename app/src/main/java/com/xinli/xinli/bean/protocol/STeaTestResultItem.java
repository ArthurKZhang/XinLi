package com.xinli.xinli.bean.protocol;

import com.xinli.xinli.bean.bean.TestResultItemBean;

import java.util.List;

/**
 * Created by zhangyu on 04/05/2017.
 */
public class STeaTestResultItem {
    private String items;// List<TestResultItemBean> As Json

    @Override
    public String toString() {
        return "STeaTestResultItem{" +
                "items='" + items + '\'' +
                '}';
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public STeaTestResultItem(String items) {

        this.items = items;
    }
}
