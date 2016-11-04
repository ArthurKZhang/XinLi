package com.xinli.xinli.util;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.test.Artical;
import com.xinli.xinli.bean.test.Recommend;
import com.xinli.xinli.bean.test.TestI;
import com.xinli.xinli.bean.test.TestLI;
import com.xinli.xinli.bean.test.VF;
import com.xinli.xinli.testdao.ArticalDao;
import com.xinli.xinli.testdao.LoginUtil;
import com.xinli.xinli.testdao.RecommendDao;
import com.xinli.xinli.testdao.SharedFileHelper;
import com.xinli.xinli.testdao.TestIDao;
import com.xinli.xinli.testdao.TestLIDao;
import com.xinli.xinli.testdao.VFDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyService extends Service implements Runnable {

    public static ArrayList<Task> taskList = new ArrayList<Task>();
    public static boolean isMyServiceRun = false;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        isMyServiceRun = true;
        Log.d("test", "MyService --> onStart() --> new Thread Start");
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        Log.d("test", "MyService-->run()");
        while (isMyServiceRun) {
            if (taskList.size() > 0) {
                doTask(taskList.get(0));
            } else {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
            }
        }
    }

    private void doTask(Task ts) {
        Message message = hand.obtainMessage();
        message.what = ts.getTaskType();
        Log.d("test", "MyService-->doTask()");
        Map<String, Object> map;
        switch (ts.getTaskType()) {
            case Task.VF_GET_DATA:
                VFDao vfdb = (VFDao) ts.getTaskParam().get("vfdb");
                List<VF> vfs = vfdb.query();//......
                RecommendDao rdb = (RecommendDao) ts.getTaskParam().get("rdb");
                List<Recommend> rs = rdb.query();//.....
                map = new HashMap<String, Object>();
                map.put("vfs", vfs);
                map.put("rs", rs);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->VF_GET_DATA");
                break;
            case Task.TEST_GET_DATA:
                TestIDao testIDao = (TestIDao) ts.getTaskParam().get("tidb");
                List<TestI> testIs = testIDao.queryByURI((String) ts.getTaskParam().get("uri"));
                map = new HashMap<String, Object>();
                map.put("testIs", testIs);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->TEST_GET_DATA");
                break;
            case Task.ARTICAL_GET_DATA:
                ArticalDao articalDao = (ArticalDao) ts.getTaskParam().get("articaldb");
                Artical artical = articalDao.queryOneArticalByURI((String)ts.getTaskParam().get("uri"));
                map = new HashMap<String, Object>();
                map.put("artical", artical);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->ARTICAL_GET_DATA");
                break;
            case Task.TESTLIST_GET_DATA:
                TestLIDao testLIDao = (TestLIDao) ts.getTaskParam().get("testLIDao");
                List<TestLI> testLIs = testLIDao.queryByCategory((String) ts.getTaskParam().get("category"));
                map = new HashMap<String, Object>();
                map.put("testLIs", testLIs);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->TESTLIST_GET_DATA");
                break;
            case Task.USER_GET_DATA:
                map = new HashMap<String, Object>();
                String name = (String) ts.getTaskParam().get("name");
                String passwd = (String) ts.getTaskParam().get("passwd");
                String userType = (String) ts.getTaskParam().get("userType");

                Boolean isLoginSuccess = LoginUtil.isLoginSuccess(name,passwd,userType);//为了测试方便
                map.put("isLoginSuccess", isLoginSuccess);
                map.put("photo", R.drawable.profile_photo);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->USER_GET_DATA");
                break;
            case Task.TEST_HISTORY_GET_DATA:
                //sharedPreferences
                map = new HashMap<String, Object>();
                SharedPreferences sharedPreferences = (SharedPreferences) ts.getTaskParam().get("sharedPreferences");
                List<Map<String, String>> list = SharedFileHelper.getInstance().loadDidTest(sharedPreferences);
                map.put("list",list);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->TEST_HISTORY_GET_DATA");
                break;
            case Task.UPLOADED_HISTORY_GET_DATA:
                //sharedPreferences
                map = new HashMap<String, Object>();
                SharedPreferences sharedPreferences2 = (SharedPreferences) ts.getTaskParam().get("sharedPreferences");
                List<Map<String, String>> list2 = SharedFileHelper.getInstance().loadUploadedTest(sharedPreferences2);
                map.put("list",list2);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->UPLOADED_HISTORY_GET_DATA");
                break;
        }

        taskList.remove(ts);
        hand.sendMessage(message);
    }

    public static void newTask(Task ts) {
        Log.d("test", "MyService-->newTask");
        taskList.add(ts);
    }

    private final Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Task.VF_GET_DATA:
//                    Log.d("test", "****"+AppManager.getAppManager().getAllActivity().toString());
                    AppManager.getAppManager().getActivityByName("MainActivity").refresh(msg.obj);
                    break;
                case Task.TEST_GET_DATA:
                    AppManager.getAppManager().getActivityByName("DoTest").refresh(msg.obj);
                    break;
                case Task.ARTICAL_GET_DATA:
                    AppManager.getAppManager().getActivityByName("ArticalActivity").refresh(msg.obj);
                    break;
                case Task.TESTLIST_GET_DATA:
                    AppManager.getAppManager().getActivityByName("TestListActivity").refresh(msg.obj);
                    break;
                case Task.USER_GET_DATA:
                    AppManager.getAppManager().getActivityByName("LoginActivity").refresh(msg.obj);
                    break;
                case Task.TEST_HISTORY_GET_DATA:
                    AppManager.getAppManager().getActivityByName("HistoryTestActivity").refresh(msg.obj);
                    break;
                case Task.UPLOADED_HISTORY_GET_DATA:
                    AppManager.getAppManager().getActivityByName("HistoryTestActivity").refresh(msg.obj);
                    break;
            }
        }
    };

}
