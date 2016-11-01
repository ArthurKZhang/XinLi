package com.xinli.xinli.testdao;

/**
 * Created by zhangyu on 11/1/16.
 */
public class LoginUtil {

    /**
     * 只有,name:000,passwd:000,userType:student || name:111,passwd:111,userType:teacher返回true
     * @param name
     * @param passwd
     * @param userType
     * @return
     */
    public static boolean isLoginSuccess(String name, String passwd, String userType) {
        return (name.equals("000") && passwd.equals("000") && userType.equals("student"))
                || (name.equals("111") && passwd.equals("111") && userType.equals("teacher"));
    }
    public static final String STUDENT = "student";
    public static final String TEACHER = "teacher";
}
