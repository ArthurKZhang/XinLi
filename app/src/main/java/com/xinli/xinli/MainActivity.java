package com.xinli.xinli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xinli.xinli.activity.TestList;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.category1);
        button2 = (Button)findViewById(R.id.category2);
        button3 = (Button)findViewById(R.id.category3);

        MyButtonClickListener listener = new MyButtonClickListener();
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);

    }
    class MyButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            Bundle bundle = new Bundle();
            //NEED CORRECT
            bundle.putString("resourceURI", button.getTag().toString());
            Intent intent = new Intent(MainActivity.this, TestList.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
