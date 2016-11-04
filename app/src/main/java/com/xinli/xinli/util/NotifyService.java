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
import android.provider.Settings;
import android.util.Log;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.DoTest;
import com.xinli.xinli.activity.ShowTestResultActivity;

public class NotifyService extends Service implements Runnable {

    public final int NOTIFICATION_ID = 0x222;
    public static boolean isStart = false;

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
                Thread.sleep(5000);
            } catch (Exception e) {

            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStart = true;

        Thread thread = new Thread(this);
        thread.start();
        return super.onStartCommand(intent, flags, startId);

    }

    private void doCheck() {
        String usertype = AppManager.getAppManager().userType;
        if (usertype == "student") {
            SharedPreferences sharedPreferences = getSharedPreferences("UploadedTest", Context.MODE_PRIVATE);
            String uploadedFileName = sharedPreferences.getString("fileName", null);
            if (uploadedFileName != null)
                sendNotification(usertype, uploadedFileName);
        }
        if (usertype == "teacher") {
            SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", MODE_PRIVATE);
            String submittedTesturi = sharedPreferences.getString("testuri", null);
            if (submittedTesturi != null)
                Log.d("test","Notify##########"+submittedTesturi);
                sendNotification(usertype, submittedTesturi);
        }
    }

    private void sendNotification(String userType, String what) {
        Intent intent = null;
        String msg = null;
        if (userType == "student") {
            msg = what + "has been uploaded";
//            testURI
            Bundle bundle = new Bundle();
            bundle.putString("testURI", "test/love/testListItem1");
            intent = new Intent(getBaseContext(), DoTest.class);
            intent.putExtras(bundle);
        }
        String result;
        if (userType == "teacher") {
            msg = what + "has been submited";
            SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", MODE_PRIVATE);
            result = sharedPreferences.getString(what, null);
            if (result == null) {
                return;
            }
//            "resultUri"
            Bundle bundle = new Bundle();
            bundle.putString("resultUri", what);
            intent = new Intent(getBaseContext(), ShowTestResultActivity.class);
            intent.putExtras(bundle);
        }
        if (intent == null)
            return;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 100, intent, 0);
        Notification notification = null;

        notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setTicker("XinLi")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("new Message")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}
