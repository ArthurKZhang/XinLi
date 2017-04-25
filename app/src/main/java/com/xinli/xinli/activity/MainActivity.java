package com.xinli.xinli.activity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.test.Recommend;
import com.xinli.xinli.bean.test.VF;
import com.xinli.xinli.testdao.RecommendDao;
import com.xinli.xinli.testdao.VFDao;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends MyBaseActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    ViewFlipper viewFlipper;
    LinearLayout ll1,ll2,ll3,ll4;

    int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setCustomActionBar();
        initViewComponent();

        loadVFsAndRecommends();
    }

    private void setCustomActionBar() {
        android.support.v7.app.ActionBar.LayoutParams lp = new android.support.v7.app.ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        TextView textView = (TextView) mActionBarView.findViewById(R.id.tv_actionbar);
        textView.setText("Test Yard");  textView.setTextColor(Color.WHITE); textView.setTextSize(AppManager.dip2px(this,10));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initViewComponent() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        button1 = (Button) findViewById(R.id.category1);
        button2 = (Button) findViewById(R.id.category2);
        button3 = (Button) findViewById(R.id.category3);

        button1.setTag(R.id.tag_category,"love");
        button2.setTag(R.id.tag_category,"work");
        button3.setTag(R.id.tag_category,"family");

        ll1 = (LinearLayout)findViewById(R.id.recom1);
        ll2 = (LinearLayout)findViewById(R.id.recom2);
        ll3 = (LinearLayout)findViewById(R.id.recom3);
        ll4 = (LinearLayout)findViewById(R.id.recom4);

        /*
        * 为ViewFlipper去添加动画效果
        *
        */
        viewFlipper.setInAnimation(this, R.anim.left_in);
        viewFlipper.setOutAnimation(this, R.anim.left_out);
        viewFlipper.setFlipInterval(6000);
        viewFlipper.startFlipping();


//        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float startX = 0;
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startX = event.getX();
//                        break;
//                    case MotionEvent.ACTION_MOVE://判断向左滑动还是向右滑动
//                        if (event.getX() - startX > 100) {
//                            viewFlipper.setInAnimation(MainActivity.this, R.animator.right_in);
//                            viewFlipper.setOutAnimation(MainActivity.this, R.animator.right_out);
//                            viewFlipper.showPrevious();
//                        } else if (startX - event.getX() > 100) {
//                            viewFlipper.setInAnimation(MainActivity.this, R.animator.left_in);
//                            viewFlipper.setOutAnimation(MainActivity.this, R.animator.left_out);
//                            viewFlipper.showNext();
//                        }
//                    case MotionEvent.ACTION_UP:
//                        break;
//                }
//                return true;
//            }
//        });

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = (String) viewFlipper.getCurrentView().getTag(R.id.tag_uri);
                if(uri !=null){
                    Bundle bundle = new Bundle();
                    bundle.putString("uri", uri);
                    Intent intent = new Intent(MainActivity.this, ArticalActivity.class);
                    intent.putExtras(bundle);
                    Log.d("test","iewFlipper.getCurrentView().setOnClickListener-->"+uri);
                    MainActivity.this.startActivity(intent);
                }else{
                    Log.e("test","uri is null");
                    Toast.makeText(MainActivity.this,"uri is null",Toast.LENGTH_SHORT).show();
                }
            }
        });

        MyButtonClickListener listener = new MyButtonClickListener();
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);

        ll1.setOnClickListener(new MyLLClickListener());
        ll2.setOnClickListener(new MyLLClickListener());
        ll3.setOnClickListener(new MyLLClickListener());
        ll4.setOnClickListener(new MyLLClickListener());
    }


    class MyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            Bundle bundle = new Bundle();
            //NEED CORRECT
            bundle.putString("category", (String) button.getTag(R.id.tag_category));
            Intent intent = new Intent(MainActivity.this, TestListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    class MyLLClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String uri = (String) ((LinearLayout)v).getTag(R.id.tag_uri);
            Bundle bundle = new Bundle();
            bundle.putString("testURI",uri);
            Intent intent = new Intent(MainActivity.this, DoTest.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.requestCode){
            String returnString = data.getStringExtra("DoTestReturn");
            Toast.makeText(this,returnString,Toast.LENGTH_SHORT).show();
        }
    }

    private ImageView getImageView(int i) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(i);
        return imageView;
    }

    private void loadVFsAndRecommends(){
        VFDao vfDao = new VFDao(MainActivity.this);
        RecommendDao recommendDao = new RecommendDao(MainActivity.this);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("vfdb",vfDao);
        hm.put("rdb",recommendDao);
        Task ts = new Task(Task.VF_GET_DATA, hm);
        Log.d("test", "MainActivity-->loadVFsAndRecommends");
        MyService.newTask(ts);
    }


    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];

        List<VF> vfs = (List<VF>) map.get("vfs");
        List<Recommend> rs = (List<Recommend>) map.get("rs");
        refreshVF(vfs);
        refreshRecommend(rs);

    }


    /**
     * refresh ViewFlipper Component,contains three images
     * @param vfs
     */
    private void refreshVF(List<VF> vfs){

        for (int i = 0; i < vfs.size(); i++) {
            ImageView imageView = getImageView(vfs.get(i).img);
            imageView.setTag(R.id.tag_uri,vfs.get(i).uri);
            viewFlipper.addView(imageView);
        }
    }

    /**
     * refresh Recommend component, contains four parts
     * @param rs
     */
    private void refreshRecommend(List<Recommend> rs){
//        ll1 = (LinearLayout)findViewById(R.id.recom1);
        ll1.setTag(R.id.tag_uri, rs.get(0).uri);
        ImageView im1 = (ImageView) ll1.findViewById(R.id.recom1_img);
        im1.setImageResource(rs.get(0).img);
        TextView tv1 = (TextView) ll1.findViewById(R.id.recom1_text);
        tv1.setText(rs.get(0).shortdes);

//        ll2 = (LinearLayout)findViewById(R.id.recom2);
        ll2.setTag(R.id.tag_uri, rs.get(1).uri);
        ImageView im2 = (ImageView) ll2.findViewById(R.id.recom2_img);
        im2.setImageResource(rs.get(1).img);
        TextView tv2 = (TextView) ll2.findViewById(R.id.recom2_text);
        tv2.setText(rs.get(1).shortdes);

//        ll3 = (LinearLayout)findViewById(R.id.recom3);
        ll3.setTag(R.id.tag_uri, rs.get(2).uri);
        ImageView im3 = (ImageView) ll3.findViewById(R.id.recom3_img);
        im3.setImageResource(rs.get(2).img);
        TextView tv3 = (TextView) ll3.findViewById(R.id.recom3_text);
        tv3.setText(rs.get(2).shortdes);

//        ll4 = (LinearLayout)findViewById(R.id.recom4);
        ll4.setTag(R.id.tag_uri, rs.get(3).uri);
        ImageView im4 = (ImageView) ll4.findViewById(R.id.recom4_img);
        im4.setImageResource(rs.get(3).img);
        TextView tv4 = (TextView) ll4.findViewById(R.id.recom4_text);
        tv4.setText(rs.get(3).shortdes);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MainActivity.this).setTitle("Exit App?")
                    .setMessage("Click \"Exit\" button to exit App")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppManager.getAppManager().AppExit(MainActivity.this);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
        }
        return true;

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
