package com.xinli.xinli.bean.protocol;

import com.xinli.xinli.bean.bean.NotifyRecord;

import java.util.List;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class SStuGetNotify {
    private List<NotifyRecord> notifys;

    @Override
    public String toString() {
        return "SStuGetNotify{" +
                "notifys=" + notifys +
                '}';
    }

    public List<NotifyRecord> getNotifys() {
        return notifys;
    }

    public void setNotifys(List<NotifyRecord> notifys) {
        this.notifys = notifys;
    }

    public SStuGetNotify(List<NotifyRecord> notifys) {

        this.notifys = notifys;
    }
}
