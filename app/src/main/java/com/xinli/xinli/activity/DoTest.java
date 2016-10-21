package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.TestItem;
import com.xinli.xinli.util.TestItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoTest extends AppCompatActivity {

    List<TestItem> testItems;
    private int i;
    TextView tv_description;
    LinearLayout linearLayout;
    int FLAG = 0;
    Button bt_submit;

    Map<Integer, Integer> resultMap; // first Integer--testItemNumber; second Integer--chooseNumber

    SharedPreferences sharedPreferences;
    String testURI;
    int resutCode = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_test);
        linearLayout = (LinearLayout) findViewById(R.id.doTestOptionField);
        resultMap = new HashMap<Integer, Integer>();
        tv_description = (TextView) findViewById(R.id.tv_description);

        Button bt_next = (Button) findViewById(R.id.bt_next);
        Button bt_previous = (Button) findViewById(R.id.bt_previous);
        bt_submit = (Button) findViewById(R.id.bt_submit);

        bt_submit.setVisibility(View.INVISIBLE);

        //dynamic load option infos by ROM? search later
//        linearLayout.addView();
        Bundle bundle = this.getIntent().getExtras();
        testURI= bundle.getString("testURI");

        testItems = getTestItems();
        final int testNum = testItems.size();
        if (testNum <= 0) {
            Log.e("doTest", "testItems is empty");
            throw new NullPointerException();
        }


        //**START**this part should be update after click button
        loadTestItem(testItems, FLAG);
        //**END**

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG < testNum - 1)
                    FLAG++;
                loadTestItem(testItems, FLAG);

                if (FLAG == testNum - 1) {
                    bt_submit.setVisibility(View.VISIBLE);

                }
            }
        });

        bt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG > 0)
                    FLAG--;
                loadTestItem(testItems, FLAG);

            }
        });

        sharedPreferences = getSharedPreferences("submitTestHistory",MODE_APPEND);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoTest.this).setTitle("Submit?")
                        .setMessage("Please make sure you don't want to change anything")
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //New Thread send result to server
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        editor.putString(testURI,resultMap.toString());
                                        editor.commit();
                                    }
                                }).start();
                                DoTest.this.setResult(resutCode, new Intent().putExtra("DoTestReturn",testURI));
                                DoTest.this.finish();
                            }
                        })
                        .setNegativeButton("deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }).show();
            }
        });

    }//end onCreate()

    private void loadTestItem(List<TestItem> testItems, final int flag) {

        TestItem t = testItems.get(flag);
        tv_description.setText(t.getDescriptions());

        List<String> chooItems = t.getChooseItems();
        int chNum = chooItems.size();
        linearLayout.removeAllViews();
        int cheched = -1;
        if (resultMap.get(flag) != null)
            cheched = resultMap.get(flag);
        for (int i = 0; i < chNum; i++) {
            View view = View.inflate(getApplicationContext(), R.layout.choose_multiple_component, null);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setTag(i);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int num = (Integer) checkBox.getTag();
                        resultMap.put(flag, num);
                        Toast.makeText(DoTest.this, flag + "||" + num, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if ((i) == cheched)
                checkBox.setChecked(true);
            TextView tv_chooseItem = (TextView) view.findViewById(R.id.tv_chooseItem);
            tv_chooseItem.setText(chooItems.get(i));
            linearLayout.addView(view);
        }
    }

    private List<TestItem> getTestItems() {
        List<String> choos2 = new ArrayList<String>();
        List<String> choos3 = new ArrayList<String>();
        choos2.add("aaaaaaaaaaaaaaaa");
        choos2.add("bbbbbbbbbbbbbbbb");

        choos3.add("111111111111");
        choos3.add("22222222222");
        choos3.add("333333333333");

        List<TestItem> testItems = new ArrayList<TestItem>();
        for (int i = 0; i < 5; i++) {
            TestItem testItem = new TestItem(i + "高德地图中如何把map放到每个Listview的item中-CSDN问答\n" +
                    "2014年12月16日 - 我要提问 问答规则说明", TestItemHelper.CHOOSE_ONE, 2, choos2);
            testItems.add(testItem);
        }
        for (int
             i = 0; i < 5; i++) {
            TestItem testItem = new TestItem(i + "安卓listview setadapter 必须把所有map 键值对 都用上吗_百度知道\n" +
                    "1个回答 - 提问时间: 2015年02月12日\n" +
                    "最佳答案: 一般自动会遍历map的,你看adapter里是不是有个getcount方法", TestItemHelper.CHOOSE_MULTIPLE, 3, choos3);
            testItems.add(testItem);
        }
        return testItems;
    }

}
