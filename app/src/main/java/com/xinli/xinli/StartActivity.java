package com.xinli.xinli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.xinli.xinli.activity.MyBaseActivity;
import com.xinli.xinli.testdao.ArticalDao;
import com.xinli.xinli.testdao.RecommendDao;
import com.xinli.xinli.testdao.TestIDao;
import com.xinli.xinli.testdao.TestLIDao;
import com.xinli.xinli.testdao.VFDao;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;
import com.xinli.xinli.util.NotifyService;

public class StartActivity extends MyBaseActivity {
    Button initDB,skip;
    EditText etInputIP,etInputhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        initDB = (Button) this.findViewById(R.id.Bt_initDB);
        skip = (Button)this.findViewById(R.id.Bt_skip);
        etInputIP = (EditText) findViewById(R.id.etInputIP);
        etInputhost = (EditText) findViewById(R.id.etInputhost);

        launchMasterService();

        checkLogIn();

        initDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.serverIP==null){
                    String ip = etInputIP.getText().toString().trim();
                    AppManager.serverIP = ip;
                    AppManager.serverName = etInputhost.getText().toString().trim();
                }
                Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(intent);
//                AppManager.getAppManager().finishActivity(StartActivity.this);
            }
        });

//        launchMasterService();

        Log.e("test","||"+getSupportActionBar());
    }

    @Override
    public void refresh(Object... param) {

    }

    /**
     * 初始化用于原型模拟的数据库数据
     */
    public void initData(){
        VFDao vfDao = new VFDao(this);
        TestIDao testIDao = new TestIDao(this);
        ArticalDao articalDao = new ArticalDao(this);
//        RecommendDao recommendDao = new RecommendDao(this);
        TestLIDao testLIDao = new TestLIDao(this);

        vfDao.initData();
        testIDao.initData();
        articalDao.initData();
        testLIDao.initData();

//        recommendDao
    }

    /**
     * 启动总后台服务
     */
    public void launchMasterService(){
        if (!MyService.isMyServiceRun){
            Intent intent = new Intent(this, MyService.class);
            this.startService(intent);
        }
        if(!NotifyService.isStart){
            this.startService(new Intent(this,NotifyService.class));
        }
    }

    /**
     * check whether Costumer logged in or not. by check SharedPreferences file "LoginInfo", sp.getBoolean("isLogIn",false);
     * This method will change the value of <AppManager(instance).isLoggedIn>
     */
    private void checkLogIn(){
        SharedPreferences sp = StartActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
        boolean isLog = sp.getBoolean("isLogIn",false);
        if (isLog){
            //editor.putString("userName", name).putString("userType", userType).putBoolean("isLogIn", isLoginSuccess)
//            .putInt("photo", photo);
            AppManager.getAppManager().isLoggedIn = true;
            AppManager.getAppManager().userName = sp.getString("userName",null);
            AppManager.getAppManager().userType = sp.getString("userType",null);
            Log.e("test", "StartActivity-->Person Infos get ready");
        }else {
            AppManager.getAppManager().isLoggedIn = false;
            Log.e("test", "StartActivity-->not Logged in");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        item=menu.findItem(R.id.action_refresh);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

}
