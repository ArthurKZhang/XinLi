package com.xinli.xinli.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.adapter.ListViewAdapter;
import com.xinli.xinli.bean.TestListItem;

import java.util.LinkedList;
import java.util.List;

public class TestList extends AppCompatActivity {

    private String resourceURI;

    private ListView listView;
    private List<TestListItem> list;

    int requestCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        listView = (ListView) findViewById(R.id.testList);


        Bundle bundle = this.getIntent().getExtras();
        resourceURI = bundle.getString("resourceURI");

        //request data from server(new Thread)
        list = requestDataFromServer(resourceURI);


        listView.setAdapter(new ListViewAdapter(this, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TestListItem testListItem = list.get(position);
                Bundle bundle1 = new Bundle();
                bundle1.putString("testURI", testListItem.getTestURI());

                Intent intent = new Intent(TestList.this, DoTest.class);
                intent.putExtras(bundle1);
                startActivityForResult(intent,requestCode);
//                startActivity(intent);

            }
        });
        setContentView(listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.requestCode){
            String returnString = data.getStringExtra("DoTestReturn");
            Toast.makeText(this,returnString,Toast.LENGTH_SHORT).show();
        }
    }

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
}
