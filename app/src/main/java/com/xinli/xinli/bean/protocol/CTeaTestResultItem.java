package com.xinli.xinli.bean.protocol;

/**
 * Created by zhangyu on 04/05/2017.
 */
public class CTeaTestResultItem {
    private String teaId;

    @Override
    public String toString() {
        return "CTeaTestResultItem{" +
                "teaId='" + teaId + '\'' +
                '}';
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public CTeaTestResultItem(String teaId) {

        this.teaId = teaId;
    }
}
