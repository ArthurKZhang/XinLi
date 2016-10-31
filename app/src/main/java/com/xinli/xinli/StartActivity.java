package com.xinli.xinli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xinli.xinli.testdao.ArticalDao;
import com.xinli.xinli.testdao.RecommendDao;
import com.xinli.xinli.testdao.TestIDao;
import com.xinli.xinli.testdao.TestLIDao;
import com.xinli.xinli.testdao.VFDao;
import com.xinli.xinli.util.MyService;

public class StartActivity extends AppCompatActivity {
    Button initDB,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initDB = (Button) this.findViewById(R.id.Bt_initDB);
        skip = (Button)this.findViewById(R.id.Bt_skip);

        launchMasterService();

        initDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(intent);

                StartActivity.this.finish();
            }
        });

//        launchMasterService();
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
    }

}
