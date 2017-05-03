package com.xinli.xinli.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.DoTestAtivity;
import com.xinli.xinli.activity.ShowTestResultActivity;
import com.xinli.xinli.bean.bean.NotifyRecord;
import com.xinli.xinli.bean.protocol.CStuGetNotify;
import com.xinli.xinli.bean.protocol.SStuGetNotify;
import com.xinli.xinli.service.StuGetNotifyWork;

import java.util.Date;
import java.util.List;

public class NotifyService extends Service implements Runnable {

    public final int NOTIFICATION_ID = 0x222;
    public static boolean isStart = false;

    Thread thread;

    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void run() {
        while (isStart) {
            doCheck();
            try {
                Thread.sleep(1000 * 60);//一分钟之后再次探测
            } catch (Exception e) {

            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStart = true;

        thread = new Thread(this);
        thread.start();
        return super.onStartCommand(intent, flags, startId);

    }

    private void doCheck() {
        String usertype = AppManager.getAppManager().userType;
        if (usertype == null) return;
        if (!usertype.equals("student")) {
            return;
        }

        //TODO 访问服务器，得到应该做的所有试题的记录
        String userid = AppManager.getAppManager()._id;
        CStuGetNotify c = new CStuGetNotify(userid);
        SStuGetNotify s = StuGetNotifyWork.getNotify(c);
        if (s == null) return;
        if (s.getNotifys() == null) return;
        if (s.getNotifys().isEmpty()) return;

        List<NotifyRecord> records = s.getNotifys();
        Log.e("Notify", "AppManager.notifys:" + AppManager.notifys);
        if (AppManager.notifys.isEmpty()) {
            AppManager.notifys.addAll(records);
            for (NotifyRecord r : records) {
                String testId = r.getTestId();
                String testName = r.getTestName();
                String teaName = r.getTeaName();
                Date endDate = r.getEndDate();
                //TODO 发送提醒
                sendNotification(testId, testName, teaName, endDate);
            }
        }

        for (NotifyRecord r : records) {
            if (!AppManager.notifys.contains(r)) {
                String testId = r.getTestId();
                String testName = r.getTestName();
                String teaName = r.getTeaName();
                Date endDate = r.getEndDate();
                //TODO 发送提醒
                sendNotification(testId, testName, teaName, endDate);
                AppManager.notifys.add(r);
            }
        }
    }

    private void sendNotification(String testID, String testName, String teaName, Date endDate) {
        Intent intent = null;
        String msg = testName;
        Bundle bundle = new Bundle();
        bundle.putString("testid", testID);
        bundle.putString("testname", testName);
        intent = new Intent(getBaseContext(), DoTestAtivity.class);
        intent.putExtras(bundle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 100, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setTicker("XinLi")
                .setSmallIcon(R.mipmap.new_icon)
                .setContentTitle("您有新的测试题")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void sendNotification(String userType, String what) {
//        Intent intent = null;
//        String msg = null;
//        if (userType == "student") {
//            msg = what + "has been uploaded";
////            testURI
//            Bundle bundle = new Bundle();
//            bundle.putString("testURI", "test/love/testListItem1");
//            intent = new Intent(getBaseContext(), DoTestAtivity.class);
//            intent.putExtras(bundle);
//        }
//        String result;
//        if (userType == "teacher") {
//            msg = what + "has been submited";
//            SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", MODE_PRIVATE);
//            result = sharedPreferences.getString(what, null);
//            if (result == null) {
//                return;
//            }
////            "resultUri"
//            Bundle bundle = new Bundle();
//            bundle.putString("resultUri", what);
//            intent = new Intent(getBaseContext(), ShowTestResultActivity.class);
//            intent.putExtras(bundle);
//        }
//        if (intent == null)
//            return;
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 100, intent, 0);
//        Notification notification = new Notification.Builder(this)
//                .setAutoCancel(true)
//                .setTicker("XinLi")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("new Message")
//                .setContentText(msg)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
//                .build();
//
//        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
        Log.e("test", "NotifyService --> closed");
        Toast.makeText(this, "in OnDestory() of NotifyService", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
/*
private void doCheck() {

        String usertype = AppManager.getAppManager().userType;
        if (usertype == "student" && shouldSend2Student) {
            SharedPreferences sharedPreferences = getSharedPreferences("UploadedTest", Context.MODE_PRIVATE);
            String uploadedFileName = sharedPreferences.getString("fileName", null);
            if (uploadedFileName != null)
                sendNotification(usertype, uploadedFileName);
        }
        if (usertype == "teacher" && shouldSend2Teacher) {
            SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", MODE_PRIVATE);
            String submittedTesturi = sharedPreferences.getString("testuri", null);
            if (submittedTesturi != null) {
                Log.d("test", "Notify##########" + submittedTesturi);
                sendNotification(usertype, submittedTesturi);
            }
        }
    }
 */