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
import com.xinli.xinli.bean.mine.CLogin;
import com.xinli.xinli.bean.mine.SLogin;
import com.xinli.xinli.bean.mine.SRegister;
import com.xinli.xinli.bean.mine.STeacherPostTest;
import com.xinli.xinli.bean.test.Artical;
import com.xinli.xinli.bean.test.Recommend;
import com.xinli.xinli.bean.test.TestI;
import com.xinli.xinli.bean.test.TestLI;
import com.xinli.xinli.bean.test.VF;
import com.xinli.xinli.net.SimpleCommunicate;
import com.xinli.xinli.service.LoginWork;
import com.xinli.xinli.service.RegisterWork;
import com.xinli.xinli.service.TeacherPostTestWork;
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

    /**
     * task处理函数，根据不同的task type来做相应的处理
     *
     * @param ts
     */
    private void doTask(Task ts) {
        Message message = hand.obtainMessage();
        message.what = ts.getTaskType();
        Log.d("test", "MyService-->doTask():" + ts.getTaskType());
        Map<String, Object> map = null;
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
                Artical artical = articalDao.queryOneArticalByURI((String) ts.getTaskParam().get("uri"));
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
            case Task.USER_GET_DATA://登录
                map = new HashMap<String, Object>();
                String name = (String) ts.getTaskParam().get("name");
                String passwd = (String) ts.getTaskParam().get("passwd");
                String userType = (String) ts.getTaskParam().get("userType");

                CLogin cLogin = new CLogin((String) ts.getTaskParam().get("name"),
                        (String) ts.getTaskParam().get("passwd"));
//                        (String) ts.getTaskParam().get("userType"));
                SLogin sLogin = LoginWork.login(cLogin);

                Boolean isLoginSuccess = false;
                if (sLogin == null) {
                    Log.d("test", "MyService-->doTask()-->USER_GET_DATA:" + "sLogin is null");
                    break;
                }
                Log.d("test", "MyService-->doTask()-->USER_GET_DATA:" + "sLogin to String:" + sLogin.toString());
                if (sLogin.getResult() == null) {
                    Log.d("test", "MyService-->doTask()-->USER_GET_DATA:" + "sLogin is incomplete");
                    break;
                }
                String loginResult = sLogin.getResult();
                //0 means success
                if (loginResult.equals("0")){
                    isLoginSuccess = true;
                }
                //1 means wrong password
                if(loginResult.equals("1")){

                }
                //-1 means username doesn't exist
                if(loginResult.equals("-1")){

                }

                map.put("isLoginSuccess", isLoginSuccess);
                map.put("result", sLogin.getResult());
                map.put("_id", sLogin.get_id());
                map.put("institution", sLogin.getInstitution());
                map.put("enrollmentDate", sLogin.getEnrollmentDate());
                map.put("type",sLogin.getType());
                map.put("photo", R.drawable.profile_photo);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->USER_GET_DATA:map:" + message.obj.toString());
                break;
            case Task.TEST_HISTORY_GET_DATA:
                //sharedPreferences
                map = new HashMap<String, Object>();
                SharedPreferences sharedPreferences = (SharedPreferences) ts.getTaskParam().get("sharedPreferences");
                List<Map<String, String>> list = SharedFileHelper.getInstance().loadDidTest(sharedPreferences);
                map.put("list", list);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->TEST_HISTORY_GET_DATA");
                break;
            case Task.UPLOADED_HISTORY_GET_DATA:
                //sharedPreferences
                map = new HashMap<String, Object>();
                SharedPreferences sharedPreferences2 = (SharedPreferences) ts.getTaskParam().get("sharedPreferences");
                List<Map<String, String>> list2 = SharedFileHelper.getInstance().loadUploadedTest(sharedPreferences2);
                map.put("list", list2);
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->UPLOADED_HISTORY_GET_DATA");
                break;
            case Task.USER_REGISTER://注册
                HashMap<String, Object> infos = ts.getTaskParam();
                //获得注册的服务器回复结果rgresult
                SRegister rgresult = RegisterWork.register(infos);
                if (rgresult == null) {
                    Log.d("test", "MyService-->doTask()-->USER_REGISTER->receive null");
                    break;
                }
                map = new HashMap<String, Object>();
                map.put("_id", rgresult.get_id());
                map.put("result", rgresult.getResult());
                message.obj = map;
                Log.d("test", "MyService-->doTask()-->USER_REGISTER");
                break;
            case Task.TEACHER_POST_TEST://老师提交试题
                STeacherPostTest sTeacherPostTest = TeacherPostTestWork.postTest(ts.getTaskParam());
//                map = new HashMap<String, Object>();
//                map.put("_id",rgresult.get_id());
//                map.put("result",rgresult.getResult());
//                message.obj = map;
                Log.d("test", "MyService-->doTask()-->TEACHER_POST_TEST");
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
                case Task.USER_REGISTER:
                    AppManager.getAppManager().getActivityByName("RegisterActivity").refresh(msg.obj);
                    break;
            }
        }
    };

}
