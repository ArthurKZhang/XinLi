package com.xinli.xinli.activity.upload_test_tool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xinli.xinli.activity.EditTestActivity;
import com.xinli.xinli.activity.PublishTestActivity;
import com.xinli.xinli.bean.bean.UploadRecord;
import com.xinli.xinli.bean.protocol.CDownTeaTestCache;
import com.xinli.xinli.bean.protocol.SDownTeaTestCache;
import com.xinli.xinli.service.TeaDownTestCacheWork;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Paul Burke (ipaulpro)
 */
public class TeacherTestListFragment extends Fragment implements OnStartDragListener {
    final String TAG = "TeacherTestListFragment";
    private static final int NEWTEST = 1;
    private static final int EDITTEST = 2;

    Context context;

    /**
     * 记录点击了第几个Record
     */
    public int recordPosition;

    private ItemTouchHelper mItemTouchHelper;

    private List<UploadRecord> items;
    private RecyclerListAdapter adapter;

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TeacherTestListFragment() {
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Map<String,Object> tag = (Map<String, Object>) v.getTag();
            final int position = (int) tag.get("position");
            recordPosition = position;
            new AlertDialog.Builder(getContext()).setTitle("^ - ^")
                    .setMessage("再次编辑 还是 上传测试题？")
                    .setPositiveButton("再次编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //获取view中的tag，UploadRecord object

                            UploadRecord u = (UploadRecord) tag.get("record");
                            String filepath = u.getCachePath();
                            String testName = u.getUserName();
                            String testId = u.getTestId();
                            Log.e("TestListReedit","filePath:"+filepath+",testName:"+testName+",testId:"+testId);
                            if (filepath==null){
                                AsyncTaskgetCache asyncTaskgetCache =
                                        new AsyncTaskgetCache(getContext(), testName, testId);
                                asyncTaskgetCache.execute();
                                return;
                            }
                            try {
                                InputStream is = new FileInputStream(filepath);
                                String content = IOUtils.toString(is, "UTF-8");

                                Log.e(TAG, "test getContext():" + getContext().toString());
                                Log.e(TAG, "test my context:" + context.toString());
                                Intent intent = new Intent(getContext(), EditTestActivity.class);
                                intent.putExtra("isAddNew", false);
                                intent.putExtra("content", content);
                                intent.putExtra("testid",testId);
                                intent.putExtra("position",position);
                                startActivityForResult(intent, EDITTEST);
                            } catch (FileNotFoundException e) {
                                //本地没有缓存，向服务器要试题缓存内容
                                AsyncTaskgetCache asyncTaskgetCache =
                                        new AsyncTaskgetCache(getContext(), testName, testId);
                                asyncTaskgetCache.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("上传测试题", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UploadRecord u = (UploadRecord) tag.get("record");
                            Log.e("TEST,fragment", getContext().toString());
                            Intent intent = new Intent(getContext(), PublishTestActivity.class);
                            //TODO 可以加上试题内容，如果是没有网络环境下编辑的呢？这个情况暂时不考虑
                            intent.putExtra("testid", u.getTestId());
                            intent.putExtra("teacherName", u.getUserName());
                            startActivity(intent);
                        }
                    }).show();
        }
    };

    public void updateUIadd(UploadRecord u) {
        adapter.onItemAdd(u);
    }
    public void updateUIremove(int index) {
        adapter.onItemDismiss(index);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String content = getArguments().getString("content", null);
        if (content == null || content.isEmpty()) {
            items = new ArrayList<>();
        } else {
            Gson gson = new Gson();
            Type stype = new com.google.gson.reflect.TypeToken<List<UploadRecord>>() {
            }.getType();
            items = gson.fromJson(content, stype);
        }
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RecyclerListAdapter(items, this);
        adapter.setOnClickListener(onClickListener);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    class AsyncTaskgetCache extends AsyncTask<Void, Void, Map<String, String>> {
        private Context context;
        String testname;
        String testid;

        public AsyncTaskgetCache(Context context, String testname, String testid) {
            this.context = context;
            this.testname = testname;
            this.testid = testid;
        }

        @Override
        protected Map<String, String> doInBackground(java.lang.Void... params) {
            CDownTeaTestCache c = new CDownTeaTestCache(testname, testid);
            SDownTeaTestCache s = TeaDownTestCacheWork.down(c);
            //TODO 暂时先不存手机内存，有余力再实现
            String id = s.getTestId();
            Map<String, String> infos = new HashMap<>();
            infos.put("content",s.getCache());
            infos.put("testid",id);
            return infos;
        }

        @Override
        protected void onPostExecute(Map<String, String> infos) {
            Intent intent = new Intent(getContext(), EditTestActivity.class);
            intent.putExtra("isAddNew", false);
            intent.putExtra("content", infos.get("content"));
            intent.putExtra("testid",infos.get("testid"));
            intent.putExtra("position",recordPosition);
            startActivityForResult(intent, EDITTEST);
        }
    }

}
