package com.xinli.xinli.bean;

import java.io.Serializable;
import java.util.HashMap;

public class Task implements Serializable {

    private static final long serialVersionUID = -10544600464481L;

    public static final int VF_GET_DATA = 0;
    public static final int TEST_GET_DATA = 1;
    public static final int ARTICAL_GET_DATA = 3;
    public static final int TESTLIST_GET_DATA = 4;
    public static final int USER_GET_DATA = 5;
    public static final int TEST_HISTORY_GET_DATA = 6;
    public static final int UPLOADED_HISTORY_GET_DATA = 7;
    public static final int USER_REGISTER = 8;
    public static final int TEACHER_POST_TEST = 9;



    private int taskType;
    private HashMap taskParam;

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public HashMap getTaskParam() {
        return taskParam;
    }

    public void setTaskParam(HashMap taskParam) {
        this.taskParam = taskParam;
    }

    public Task(int taskType, HashMap hm) {
        this.taskType = taskType;
        this.taskParam = hm;
    }
}
