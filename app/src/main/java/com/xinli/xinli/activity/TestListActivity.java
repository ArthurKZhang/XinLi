package com.xinli.xinli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.adapter.ListViewAdapter;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.TestListItem;
import com.xinli.xinli.bean.test.TestLI;
import com.xinli.xinli.testdao.TestLIDao;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestListActivity extends MyBaseActivity {

    private String resourceURI;
    String category;
    private ListView listView;
    private List<TestLI> testLIs;

    int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        listView = (ListView) findViewById(R.id.testList);


        Bundle bundle = this.getIntent().getExtras();
        category = bundle.getString("category");//category

        //request data from server(new Thread)
        loadTestList();


    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];
        testLIs = (List<TestLI>) map.get("testLIs");
        if (testLIs.isEmpty() || testLIs == null) {
            Log.e("test", "TestListActivity-->refresh-testLIs is null or empty");
            Toast.makeText(TestListActivity.this, "testLIs is null or empty", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(new ListViewAdapter(this, testLIs));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TestLI testLI = testLIs.get(position);
                Bundle bundle1 = new Bundle();
                bundle1.putString("testURI", testLI.uri);

                Intent intent = new Intent(TestListActivity.this, DoTest.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent, requestCode);

            }
        });
        setContentView(listView);
    }

    private void loadTestList() {
        TestLIDao testLIDao = new TestLIDao(this);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("testLIDao", testLIDao);
        hm.put("category", category);
        Task ts = new Task(Task.TESTLIST_GET_DATA, hm);
        Log.d("test", "TestListActivity-->loadTestList-" + category);
        MyService.newTask(ts);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            String returnString = data.getStringExtra("DoTestReturn");
            Toast.makeText(this, returnString, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(TestListActivity.this);
        }
        return false;
    }


}
/*
private List<TestListItem> requestDataFromServer(String uri) {
//        String imageURI;
//        private int viewCount;
//        private int testCount;

        List<TestListItem> list = new LinkedList<TestListItem>();
        //insert 10 elements to list for test
        for (int i = 0; i < 10; i++) {
            TestListItem t = new TestListItem(i + "www.dfsdfsdf", i * 20, i * 10, i + "testURI");
            list.add(t);
        }

        return list;

    }
 */
