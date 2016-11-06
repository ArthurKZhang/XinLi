package com.xinli.xinli.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.test.Artical;
import com.xinli.xinli.testdao.ArticalDao;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.HashMap;
import java.util.Map;

public class ArticalActivity extends MyBaseActivity {

    TextView textView;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_artical);

        textView = (TextView) this.findViewById(R.id.tv_artical);

        Bundle bundle = this.getIntent().getExtras();
        uri = bundle.getString("uri");

        loadArtical();

    }

    private void loadArtical() {
        ArticalDao articalDao = new ArticalDao(this);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("articaldb", articalDao);
        hm.put("uri", uri);
        Task ts = new Task(Task.ARTICAL_GET_DATA, hm);
        Log.d("test", "ArticalActivity-->loadArtical"+uri);
        MyService.newTask(ts);
    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];
        Artical artical = (Artical) map.get("artical");
        if (artical == null) {
            Log.e("error", "can't find artical from " + uri);
            textView.setText("nothing in " + uri);
        }else {
            textView.setText(artical.text);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(ArticalActivity.this);
        }
        return false;

    }
}
