package com.xinli.xinli.util;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class Resource {
    public static final String ACTION_REGISTER = "register.action";
    public static final String ACTION_LOGIN = "login.action";

    public static final String ACTION_REQUEST_PHOTO = "photo.action";
    public static final String ACTION_UPLOAD_PHOTO = "uploadphoto.action";

    public static final String ACTION_DOWN_TEACHER_TEST_LIST = "downTeacherTestList.action";
    public static final String ACTION_TEACHER_POST_TEST = "uploadtest.action";
    public static final String ACTION_PUBLISH_TEST = "publish.action";
    public static final String ACTION_TEA_TEST_CACHE = "downcache.action";

    public static final String ACTION_STU_GET_NOTIFY = "stugetnotify.action";
    public static final String ACTION_STU_GET_TEST = "stugettest.action";
    public static final String ACTION_STU_HAND_RESULT = "stuhandresult.action";

    public static final String ACTION_TEA_TEST_RESULT_ITEM = "teatestresultitem.action";



    //file resource
    public static final String SP1DOC_LOGIN_INFO = "LoginInfo";

    public static final String SP1_USERNAME = "userName";
    public static final String SP1_USERTYPE = "userType";
    public static final String SP1_ISLOGIN = "isLogIn";
    public static final String SP1_PHOTO = "photo";//String 类型，没有是"no"，有的话是photo在服务祺数据库中的id

    /**
     * TODO 改名字 不用test, 用exam
     * 学生做题提交的
     */
    public static final String SP2_SUBMIT_TEST_HISTORY = "submitTestHistory";

    /**
     * test teacher uploaded
     */
    public static final String SP3_UPLOAD_TEST = "UploadedTest";
}
