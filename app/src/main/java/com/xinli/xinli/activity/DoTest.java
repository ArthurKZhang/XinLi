package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.test.TestI;
import com.xinli.xinli.testdao.TestIDao;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;
import com.xinli.xinli.util.StringTools;
import com.xinli.xinli.util.TestItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoTest extends MyBaseActivity {
    /**
     * List<TestI> tis,题目数据的承载者
     */
    List<TestI> tis;
    /**
     * FLAG,记录目前是第几题,初始是0.
     */
    int FLAG = 0;
    //    List<TestItem> testItems;

    TextView tv_description;
    LinearLayout linearLayout;
    Button bt_submit;
    Button bt_next;
    Button bt_previous;
    /**
     * first Integer--testItemNumber; second List<Integer>--sequences that choosed
     * example: <1,<,2,3>> 意味着第一题选了第2,第3 两个选项
     */
    Map<Integer, List<Integer>> resultMap;

    SharedPreferences sharedPreferences;
    /**
     * 这套题目的uri
     */
    String testURI;
    /**
     * resultCode, 返回给调用DoTest的activity的
     */
    int resutCode = 5;
    /**
     * testNum,这套题的题目的数量
     */
    int testNum;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_test);

        resultMap = new HashMap<Integer, List<Integer>>();

        Bundle bundle = this.getIntent().getExtras();
        testURI = bundle.getString("testURI");

        sharedPreferences = getSharedPreferences("submitTestHistory", MODE_APPEND);
        editor = sharedPreferences.edit();

        initViewComponent();
        loadData();


    }//end onCreate()

    private void loadData() {
        TestIDao testIDao = new TestIDao(DoTest.this);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("tidb", testIDao);
        hm.put("uri", testURI);
        Task ts = new Task(Task.TEST_GET_DATA, hm);
        Log.d("test", "DoTest-->loadData");
        MyService.newTask(ts);
    }

    private void initViewComponent() {
        linearLayout = (LinearLayout) findViewById(R.id.doTestOptionField);
        tv_description = (TextView) findViewById(R.id.tv_description);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_previous = (Button) findViewById(R.id.bt_previous);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setVisibility(View.INVISIBLE);

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG < testNum - 1)
                    FLAG++;
                refreshUI(tis, FLAG);

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
                refreshUI(tis, FLAG);

            }
        });

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
                                        editor.putString("testuri",testURI);
                                        editor.putString(testURI, resultMap.toString());
                                        editor.commit();
                                    }
                                }).start();
                                DoTest.this.setResult(resutCode, new Intent().putExtra("DoTestReturn", testURI + "Completed!!"));
                                AppManager.getAppManager().finishActivity(DoTest.class);
//                                DoTest.this.f;
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
    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];

        tis = (List<TestI>) map.get("testIs");
        testNum = tis.size();
        if (testNum <= 0)
            Log.e("test", "testItems is empty");
        Toast.makeText(DoTest.this, "有" + testNum + "道题" + "来自" + testURI, Toast.LENGTH_LONG).show();
        refreshUI(tis, FLAG);

    }

    /**
     * testItems 存放所有题目, flag第几题,第一题flag是0
     *
     * @param testItems
     * @param flag
     */
    private void refreshUI(final List<TestI> testItems, final int flag) {


        TestI ti = testItems.get(flag);
        tv_description.setText(flag + "||" + ti.des);

        List<String> chooItems = StringTools.String2Strings(ti.items);//t.getChooseItems();
        int chNum = chooItems.size();
        if (chNum != ti.choosen) {
            //异常处理,获得的选项的数量和记录中指明的数量不一致
        }
        linearLayout.removeAllViews();
        if (ti.answertype == TestItemHelper.CHOOSE_MULTIPLE) {
            //多选处理
            /**
             * checkedFlag,记录哪些选项之前被选过
             */
            List<Integer> checkedFlag = resultMap.get(flag);

            for (int i = 0; i < chNum; i++) {
                final CheckBox checkBox = new CheckBox(DoTest.this);
                checkBox.setTag(i);
                checkBox.setText(i + "||" + chooItems.get(i));
                if ((checkedFlag!=null)&&checkedFlag.contains(i))
                    checkBox.setChecked(true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            int choose = (Integer) checkBox.getTag();
                            if (resultMap.get(flag) == null) {
                                ArrayList<Integer> IL = new ArrayList<Integer>();
                                IL.add(choose);
                                resultMap.put(flag, IL);
                                Toast.makeText(DoTest.this, flag + "||" + choose, Toast.LENGTH_SHORT).show();
                            } else {
                                resultMap.get(flag).add(choose);
                            }
                            //判断是不是最后一题也做完
                            if (testItems.size() == flag + 1) {
                                bt_submit.setVisibility(View.VISIBLE);
                            }

                        } else {
                            resultMap.get(flag).remove(checkBox.getTag());
                        }

                    }
                });
                linearLayout.addView(checkBox);
            }

        } else if (ti.answertype == TestItemHelper.CHOOSE_ONE) {
            //单选处理
            int checkedFlag = -1;
            if (resultMap.get(flag) != null)
                checkedFlag = resultMap.get(flag).get(0);

            RadioGroup radios = new RadioGroup(DoTest.this);
            for (int i = 0; i < chNum; i++) {
                RadioButton radioB = new RadioButton(DoTest.this);
                radioB.setText(i + "||" + chooItems.get(i));
                if (checkedFlag == i)
                    radioB.setChecked(true);
                radios.addView(radioB, i);
//                radios.addView(radioB);
            }
            linearLayout.addView(radios);
            radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int choose = group.indexOfChild(DoTest.this.findViewById(checkedId));
                    ArrayList<Integer> IL = new ArrayList<Integer>();
                    IL.add(choose);
                    resultMap.put(flag, IL);
                    //判断是不是最后一题也做完
                    if (testItems.size() == flag + 1) {
                        bt_submit.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            //异常处理
            Log.e("error", "DoTest-->refreshUI-->题目既不是单选也不是双选");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(DoTest.this).setTitle("Submit?")
                    .setMessage("Please make sure you don't want to change anything")
                    .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DoTest.this.setResult(resutCode, new Intent().putExtra("DoTestReturn", testURI + "Not Complete"));
                            AppManager.getAppManager().finishActivity(DoTest.this);
//                            DoTest.this.finish();
                        }
                    })
                    .setNegativeButton("deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();

        }

        return false;

    }

}

/*
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
 */
