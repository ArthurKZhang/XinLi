package com.xinli.xinli.util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.MyBaseActivity;
import com.xinli.xinli.bean.bean.NotifyRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhangyu on 10/29/16.
 */
public class AppManager {

    private static Stack<MyBaseActivity> activityStack;
    private static AppManager instance;
    /**
     * 判断app有没有被用户登录
     */
    public boolean isLoggedIn;
    /**
     * teacher,student
     */
    public String userType;

    public String userName;
    public String userInstitude;
    public static String _id;

    /**
     * 服务器的IP地址，例如http://192.168.45.2:8080/Test/getjson.action中的192.168.45.2
     */
    public static String serverIP = "192.168.3.6";
    /**
     * 服务器的应用名称，例如http://192.168.45.2:8080/Test/getjson.action中的Test
     */
    public static String serverName = "Test";

    public static List<NotifyRecord> notifys = new ArrayList<>();

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(MyBaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<MyBaseActivity>();
        }
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
        Log.d("test", "AppManager-->addActivity\n" + activityStack.toString());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public MyBaseActivity currentActivity() {
        MyBaseActivity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        MyBaseActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(MyBaseActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
        Log.d("test", "AppManager-->finishActivity\n" + activityStack.toString());
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (MyBaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 获得所有IdeaCodeActivity
     *
     * @return
     */
    public List<MyBaseActivity> getAllActivity() {
        ArrayList<MyBaseActivity> listActivity = new ArrayList<MyBaseActivity>();
        for (Activity activity : activityStack) {
            listActivity.add((MyBaseActivity) activity);
        }
        return listActivity;
    }

    /**
     * 根据Activity名称返回指定的Activity
     *
     * @param name
     * @return
     */
    public MyBaseActivity getActivityByName(String name) {
        for (MyBaseActivity ia : activityStack) {
            if (ia.getClass().getName().indexOf(name) >= 0) {
                Log.d("test", "AppManager-->getActivityByName()-->" + ia.getClass().getName() + "||" + name);
                return (MyBaseActivity) ia;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());
            context.stopService(new Intent(context, NotifyService.class));
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}


