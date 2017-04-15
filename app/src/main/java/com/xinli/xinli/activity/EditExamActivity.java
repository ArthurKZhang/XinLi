package com.xinli.xinli.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.adapter.RecyclerAdapter;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.bean.Quz;
import com.xinli.xinli.bean.bean.Test;
import com.xinli.xinli.ui.DividerGridItemDecoration;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 21/03/2017.
 */
public class EditExamActivity extends MyBaseActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<String> mDatas;

    private Button addButton, deleteButton, getinfos, bttijiao;
    private TextView justfortest;

//    private Map<>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_exam);
        initData();

        addButton = (Button) this.findViewById(R.id.add);
        deleteButton = (Button) this.findViewById(R.id.delete);
        bttijiao = (Button) findViewById(R.id.bttijiao);
        getinfos = (Button) this.findViewById(R.id.getinfos);
        justfortest = (TextView) this.findViewById(R.id.justfortest);

        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        adapter = new RecyclerAdapter(this, mDatas);

        recyclerView.setLayoutManager(new LinearLayoutManager(EditExamActivity.this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(mDatas.size());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeData(mDatas.size() - 1);
            }
        });
        getinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                justfortest.setText(getInfos());
            }
        });

        bttijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", AppManager._id);
                hm.put("test", getTest());
                Task ts = new Task(Task.TEACHER_POST_TEST, hm);
                Log.d("test", "EditExamActivity-->bttijiao-->Onclick" + AppManager._id + "@" + getTest().toString());
                MyService.newTask(ts);
            }
        });


        initEvent();


    }

    private Test getTest() {
        List<Quz> quzs = new ArrayList<Quz>();
//        StringBuffer stringBuffer = new StringBuffer();

        int total = recyclerView.getChildCount();
        for (int i = 0; i < total; i++) {
            View item = recyclerView.getChildAt(i);
            EditText editText = (EditText) item.findViewById(R.id.comp_quz_quzcontent);

            RadioGroup rg = (RadioGroup) item.findViewById(R.id.comp_quz_radiogrp);
            int checkBoxID = rg.getCheckedRadioButtonId();
            int type = -1;
            switch (checkBoxID) {
                case R.id.comp_quz_rad1:    //danxuan
                    type = 1;
                    break;
                case R.id.comp_quz_rad2:    //duoxuan
                    type = 2;
                    break;
                case R.id.comp_quz_rad3:    //panduan
                    type = 3;
                    break;
                case R.id.comp_quz_rad4:    //tiankong
                    type = 4;  //TODO 196 Dynamic Programming--Algorithm
                    break;
                case -1:
                    Toast.makeText(this, "第" + i + 1 + "没选题目类型", Toast.LENGTH_SHORT).show();
                    break;
            }

            RecyclerView choses = (RecyclerView) item.findViewById(R.id.comp_quz_chofield);
            int choseNum = choses.getChildCount();

            List<String> choseList = new ArrayList<String>();
            for (int j = 0; j < choseNum; j++) {
                EditText choseEditText = (EditText) choses.getChildAt(j).findViewById(R.id.edit_exam_chose_edittext);
                choseList.add(choseEditText.getText().toString());
            }

            Quz quz = new Quz();
            quz.setQuzContent(editText.getText().toString());
            quz.setType(type);
            if (type == 3 && choseNum != 2) {
//                Toast.makeText(this,"第"+i+1+"题不是判断题，判断题只有两个选项",Toast.LENGTH_SHORT).show();
                return null;//"第"+i+1+"题不是判断题，判断题只有两个选项";
                //TODO 判断只有对错，如果是 "不确定" 之类的，那是选择题
            }
            if (type != 4) {
                quz.setChooseNum(choseNum);
                quz.setChooseItems(choseList);
            }
            quzs.add(quz);
        }
        return new Test(quzs);
    }

    private String getInfos() {
        Test test = getTest();
        return test.toString();
    }

    private void initData() {
        mDatas = new ArrayList<String>();
//        for (int i = 'A'; i < 'z'; i++)
//        {
//            mDatas.add("" + (char) i);
//        }
    }


    private void initEvent() {
        adapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(EditExamActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                adapter.removeData(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(EditExamActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void refresh(Object... param) {

    }

}
