package com.xinli.xinli;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.xinli.xinli.activity.MainActivity;
import com.xinli.xinli.activity.MessageList;
import com.xinli.xinli.activity.MineActivity;
import com.xinli.xinli.util.AppManager;

public class HomeActivity extends TabActivity {

    TabHost tabHost;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        Log.d("test","--setContentView--done");

        tabHost = getTabHost();
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("test").setIndicator("Test").setContent(new Intent(this, MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("message").setIndicator("Message").setContent(new Intent(this, MessageList.class)));
        tabHost.addTab(tabHost.newTabSpec("mine").setIndicator("Mine").setContent(new Intent(this, MineActivity.class)));

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

    }

    private final RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            beforView = currentView;
            switch (checkedId) {
                case R.id.radio_test:
//                    AppContext.SHOW_LOGIN_FLAG = true;
                    tabHost.setCurrentTabByTag("test");
                    currentView = 0;
                    Log.d("testHomeView", "test: " + tabHost.getCurrentTab());
                    break;
                case R.id.radio_message:
                    tabHost.setCurrentTabByTag("message");
                    currentView = 1;
                    Log.d("testHomeView", "message: " + tabHost.getCurrentTab());
                    break;
                case R.id.radio_mine:
                    tabHost.setCurrentTabByTag("mine");
                    currentView = 2;
                    Log.d("testHomeView", "mine: " + tabHost.getCurrentTab());
                    break;
                default:
                    break;
            }
            resetTabColor();
            flipView();
        }
    };

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    int currentView = 0;
    int beforView = 0;
    private static int maxTabIndex = 2;

    // 左右滑动刚好页面也有滑动效果
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            TabHost tabHost = getTabHost();
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("test", "right");
                    if (currentView == maxTabIndex) {
//                        currentView = 0;
                    } else {
                        beforView=currentView;
                        currentView++;
                        tabHost.setCurrentTab(currentView);
                    }

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("test", "left");
                    if (currentView == 0) {
//                        currentView = maxTabIndex;
                    } else {
                        beforView = currentView;
                        currentView--;
                        tabHost.setCurrentTab(currentView);
                    }

                }
                resetTabColor();
                flipView();

            } catch (Exception e) {
            }
            return false;
        }
    }

    private void flipView() {
        currentView = tabHost.getCurrentTab();
        if (currentView > beforView) {
            tabHost.getTabContentView().getChildAt(beforView)
                    .startAnimation(AnimationUtils.loadAnimation(getApplication(), R.animator.left_exit_anim));
            tabHost.getCurrentView().
                    startAnimation(AnimationUtils.loadAnimation(getApplication(), R.animator.right_enter_anim));
        }
        if (currentView < beforView) {
            tabHost.getTabContentView().getChildAt(beforView)
                    .startAnimation(AnimationUtils.loadAnimation(getApplication(), R.animator.right_exit_anim));
            tabHost.getCurrentView()
                    .startAnimation(AnimationUtils.loadAnimation(getApplication(), R.animator.left_enter_anim));
        }
        beforView = currentView;
    }

    private void resetTabColor() {
        int[] temp = {R.id.radio_test, R.id.radio_message, R.id.radio_mine};
        int i = tabHost.getCurrentTab();
        for (int a = 0; a < 3; a++) {
            if (a == i) {
                HomeActivity.this.findViewById(temp[a]).setBackgroundResource(R.drawable.border_normal2);
            } else {
                HomeActivity.this.findViewById(temp[a]).setBackgroundResource(R.drawable.border_normal);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(HomeActivity.this).setTitle("Exit App?")
                    .setMessage("Click \"Exit\" button to exit App")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppManager.getAppManager().AppExit(HomeActivity.this);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();

//            HomeActivity.this.finish();

        }
        return false;

    }
}
