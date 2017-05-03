package com.xinli.xinli.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.bean.Quz;
import com.xinli.xinli.bean.bean.Test;
import com.xinli.xinli.bean.protocol.CStuHandResult;
import com.xinli.xinli.bean.protocol.SStuHandResult;
import com.xinli.xinli.service.StuHandResultWork;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;
import com.xinli.xinli.util.TestItemHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoTestAtivity extends MyBaseActivity {

    /**
     * 第一个integer代表着题号
     * 第二个integer代表着这道题的题目类型
     * String数组承载着选项信息
     */
    private Map<Integer, Map<Integer, List<String>>> resultMap1 = new HashMap<>();
    private Test test;
    private List<Quz> quzs;
    /**
     * 这套题目的id
     */
    private String testid;
    private String testname;


    /**
     * FLAG,记录目前是第几题,初始是0.
     */
    private int FLAG;

    TextView tv_description;
    LinearLayout linearLayout;
    Button bt_submit;
    Button bt_next;
    Button bt_previous;

    /**
     * resultCode, 返回给调用DoTest的activity的
     */
    int resutCode = 5;
    /**
     * testNum,这套题的题目的数量
     */
    int testNum;

    SharedPreferences.Editor editor;

    private void initData() {
        this.testid = getIntent().getStringExtra("testid");
        this.testname = getIntent().getStringExtra("testname");
        this.resultMap1 = new HashMap<>();
        FLAG = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_do_test);

        initData();

        initViewComponent();
        loadData();


    }

    private void loadData() {

        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("testid", testid);

        Task ts = new Task(Task.TEST_GET_DATA, hm);
        Log.d("test", "DoTestAtivity-->loadData");
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
                refreshUI(quzs, FLAG);

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
                refreshUI(quzs, FLAG);

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoTestAtivity.this).setTitle("(｡ì _ í｡)")
                        .setMessage("做完了就提交吧")
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyAsyHandResult(DoTestAtivity.this).execute();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        String testAsJson = (String) map.get("testAsJson");
        Gson gson = new Gson();
        Type type = new com.google.gson.reflect.TypeToken<Test>() {
        }.getType();
        test = gson.fromJson(testAsJson, type);

        quzs = test.getQuzs();
        testNum = quzs.size();
        if (testNum <= 0)
            Log.e("test", "testItems is empty");
        Toast.makeText(DoTestAtivity.this, "有" + testNum + "道题" + "来自" + testname, Toast.LENGTH_LONG).show();
        refreshUI(quzs, FLAG);

    }

    /**
     * 更新第flag题的ui
     *
     * @param quzs
     * @param flag
     */
    private void refreshUI(final List<Quz> quzs, final int flag) {

        Quz quz = quzs.get(flag);

        tv_description.setText(flag + 1 + "||" + quz.getQuzContent());

        List<String> chooItems = quz.getChooseItems();
        int chNum = chooItems.size();
        if (chNum != quz.getChooseNum()) {
            //异常处理,获得的选项的数量和记录中指明的数量不一致
        }
        linearLayout.removeAllViews();

        switch (quz.getType()) {
            case TestItemHelper.CHOOSE_SINGLE:
                //单选处理
                int checkedFlag = -1;
                if (resultMap1.get(flag) != null) {
                    String ch = resultMap1.get(flag).get(TestItemHelper.CHOOSE_SINGLE).get(0);
                    checkedFlag = ch.charAt(0) - 97;
                }


                RadioGroup radios = new RadioGroup(DoTestAtivity.this);
                for (int i = 0; i < chNum; i++) {
                    RadioButton radioB = new RadioButton(DoTestAtivity.this);
                    radioB.setText(i + 1 + ". " + chooItems.get(i));
                    if (checkedFlag == i) {
                        radioB.setChecked(true);
                    }
                    radios.addView(radioB, i);
                }
                linearLayout.addView(radios);
                radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int choose = group.indexOfChild(DoTestAtivity.this.findViewById(checkedId));
                        Map<Integer, List<String>> answer = new HashMap<Integer, List<String>>();
                        List<String> l = new ArrayList<String>();
                        l.add(String.valueOf((char) (choose + 97)));
                        answer.put(TestItemHelper.CHOOSE_SINGLE, l);
                        resultMap1.put(flag, answer);
                        //判断是不是最后一题也做完
                        if (quzs.size() == flag + 1) {
                            bt_submit.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case TestItemHelper.CHOOSE_MULTIPLE://多选处理
                Map<Integer, List<String>> answers = resultMap1.get(flag);
                List<String> choss = null;
                if (answers!=null){
                    choss = answers.get(TestItemHelper.CHOOSE_MULTIPLE);//[a,b]
                    Log.e("DoTest", "第" + FLAG + "题上次选了" + choss.toString());
                    int num = choss.size();
                }

                //显示每一个选项
                for (int i = 0; i < chNum; i++) {
                    final CheckBox checkBox = new CheckBox(DoTestAtivity.this);
                    checkBox.setTag(i);
                    String abc = String.valueOf((char) (i + 97));
                    checkBox.setText(abc + ". " + chooItems.get(i));
                    if ((choss != null) && choss.contains(abc)) {
                        //如果这一选项之前被选中
                        checkBox.setChecked(true);
                    }
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int ched = (Integer) checkBox.getTag();
                            String abc2 = String.valueOf((char) (ched + 97));
                            if (isChecked) {
                                if (resultMap1.get(flag) == null) {
                                    //IL中记录了这一道题选了什么
                                    Map<Integer, List<String>> IL = new HashMap<Integer, List<String>>();
                                    ArrayList<String> chose1 = new ArrayList<String>();
                                    chose1.add(abc2);
                                    IL.put(TestItemHelper.CHOOSE_MULTIPLE, chose1);
                                    resultMap1.put(flag, IL);
                                    Toast.makeText(DoTestAtivity.this, flag + 1 + " 题选了 " + abc2, Toast.LENGTH_SHORT).show();
                                } else {
                                    resultMap1.get(flag).get(TestItemHelper.CHOOSE_MULTIPLE).add(abc2);
                                }
                                //判断是不是最后一题也做完
                                if (quzs.size() == flag + 1) {
                                    bt_submit.setVisibility(View.VISIBLE);
                                }

                            } else {
                                //由选择变为不选，将其从记录中删除
                                resultMap1.get(flag).get(TestItemHelper.CHOOSE_MULTIPLE).remove(abc2);
                            }

                        }
                    });
                    linearLayout.addView(checkBox);
                }
                break;
            case TestItemHelper.CHOOSE_TF:
                //判断题处理---和单选的处理一模一样
                int checkedFlag1 = -1;
                if (resultMap1.get(flag) != null) {
                    String ch = resultMap1.get(flag).get(TestItemHelper.CHOOSE_TF).get(0);
                    checkedFlag1 = ch.charAt(0) - 97;
                }


                RadioGroup radios4tf = new RadioGroup(DoTestAtivity.this);
                for (int i = 0; i < chNum; i++) {
                    RadioButton radioB = new RadioButton(DoTestAtivity.this);
                    radioB.setText(i + 1 + ". " + chooItems.get(i));
                    if (checkedFlag1 == i) {
                        radioB.setChecked(true);
                    }
                    radios4tf.addView(radioB, i);
                }
                linearLayout.addView(radios4tf);
                radios4tf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int choose = group.indexOfChild(DoTestAtivity.this.findViewById(checkedId));
                        Map<Integer, List<String>> answer = new HashMap<Integer, List<String>>();
                        List<String> l = new ArrayList<String>();
                        l.add(String.valueOf((char) (choose + 97)));
                        answer.put(TestItemHelper.CHOOSE_TF, l);
                        resultMap1.put(flag, answer);
                        //判断是不是最后一题也做完
                        if (quzs.size() == flag + 1) {
                            bt_submit.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case TestItemHelper.CHOOSE_ANSWER:
                //TODO 简答题的处理

                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(DoTestAtivity.this).setTitle("Submit?")
                    .setMessage("Please make sure you don't want to change anything")
                    .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DoTestAtivity.this.setResult(resutCode, new Intent().putExtra("DoTestReturn", "Not Complete"));
                            AppManager.getAppManager().finishActivity(DoTestAtivity.this);
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

    class MyAsyHandResult extends AsyncTask<Void, Void, String> {
        Context context;

        public MyAsyHandResult(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            Type t = new TypeToken<Map<Integer, Map<Integer, List<String>>>>() {
            }.getType();
            Gson gson = new Gson();
            String result = gson.toJson(resultMap1, t);
            CStuHandResult c = new CStuHandResult(AppManager.getAppManager()._id, testid, result);
            SStuHandResult s = StuHandResultWork.handResult(c);
            if (s == null) return null;
            return s.getReply();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("error")) {
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (s.equals("success")) {
                Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                AppManager.getAppManager().finishActivity(DoTestAtivity.this);
            }
        }
    }

}
