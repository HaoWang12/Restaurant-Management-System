package com.hhh.restaurantapp.customer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.restaurantapp.OkHttpUtil;
import com.hhh.restaurantapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminCommunicationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_right, tv_title;
    private ListView lv_communication;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private List<Map<String, String>> list, listComment;
    private MyAdapter adapter;
    private String username,sss;
    private PopupWindow mPopWindow;
    private CommentAdapter commentAdapter;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_communication);
        list = new ArrayList<>();
        listComment = new ArrayList<>();
        initView();
        initBar();
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username", "");
        initData();
        adapter.setIvCommentClickListener(new IvCommentOnclickListener() {
            @Override
            public void itemClick(View view, int position) {
                showPopupWindow(list.get(position).get("id"), username);
            }
        });
    }

    public void initData() {
        findCommunication();
        findComment();
    }

    public void initAdapter() {
        adapter = new MyAdapter(
                AdminCommunicationActivity.this,
                list,
                R.layout.item_communication,
                new String[]{"username", "content", "time"},
                new int[]{R.id.tv_username, R.id.tv_content, R.id.tv_time}
        );
        //加载数据
        lv_communication.setAdapter(adapter);
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText("Add");
        lv_communication = findViewById(R.id.lv_communication);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    private void initBar() {
        tv_title.setText("Comment Information");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_right:
                Intent intent = new Intent(AdminCommunicationActivity.this,PublishCommunicationActivity.class);
                startActivity(intent);
                break;
        }
    }


    public interface IvCommentOnclickListener {
        void itemClick(View view, int position);
    }

    public class MyAdapter extends SimpleAdapter {
        private IvCommentOnclickListener ivCommentOnclickListener;

        public void setIvCommentClickListener(IvCommentOnclickListener mOnItemDeleteListener) {
            this.ivCommentOnclickListener = mOnItemDeleteListener;
        }

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                         String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(AdminCommunicationActivity.this).inflate(R.layout.item_communication, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > 0) {
                if (!list.get(position).get("username").equals("")) {
                    holder.tv_username.setText(list.get(position).get("username"));
                }
                if (!list.get(position).get("content").equals("")) {
                    holder.tv_content.setText(list.get(position).get("content"));
                }
                if (!list.get(position).get("time").equals("")) {
                    holder.tv_time.setText("Release Time："+list.get(position).get("time"));
                }
                holder.iv_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ivCommentOnclickListener.itemClick(view, position);
                    }
                });
                final ArrayList<Map<String, String>> arrayList = new ArrayList<>();
                arrayList.clear();
                for (int i = 0; i < listComment.size(); i++) {
                    if (listComment.get(i).get("commentId").equals(list.get(position).get("id"))) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", listComment.get(i).get("id"));
                        map.put("commentId", listComment.get(i).get("commentId"));
                        map.put("comment", listComment.get(i).get("comment"));
                        map.put("username", listComment.get(i).get("username"));
                        arrayList.add(map);
                    }
                }
                commentAdapter = new CommentAdapter(AdminCommunicationActivity.this, arrayList);
                holder.lv_comment.setAdapter(commentAdapter);
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_username,tv_content,tv_time;
            ImageView iv_comment;
            ListView lv_comment;

            public ViewHolder(View view) {
                tv_username = view.findViewById(R.id.tv_username);
                tv_content = view.findViewById(R.id.tv_content);
                tv_time = view.findViewById(R.id.tv_time);
                lv_comment = view.findViewById(R.id.lv_comment);
                iv_comment = view.findViewById(R.id.iv_comment);
            }
        }
    }

    private void findCommunication() {
        OkHttpUtil.findCommunication(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminCommunicationActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                System.out.println("res:"+responseData);
                list.clear();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            final JSONObject json = new JSONObject(ls.get(i).toString());
                            final Map<String, String> map = new HashMap<String, String>();
                            map.put("id", (String) json.get("id"));
                            map.put("username", (String) json.get("username"));
                            map.put("content", (String) json.get("content"));
                            map.put("time", (String) json.get("time"));
                            list.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findComment() {
        OkHttpUtil.findComment(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminCommunicationActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();

                listComment.clear();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            Map<String, String> map = new HashMap<String, String>();
                            ;
                            map.put("id", (String) json.get("id"));
                            map.put("commentId", (String) json.get("commentId"));
                            map.put("username", (String) json.get("username"));
                            map.put("comment", (String) json.get("comment"));
                            listComment.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void addComment(final String commentId, final String username, final String comment) {
        OkHttpUtil.addComment(commentId,username,comment, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminCommunicationActivity.this, "网络异常，请重试!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                //在主线程更新ui和响应用户操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            Toast.makeText(AdminCommunicationActivity.this,"Comment Success", Toast.LENGTH_SHORT).show();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("commentId", commentId);
                            map.put("username", username);
                            map.put("comment", comment);
                            listComment.add(map);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AdminCommunicationActivity.this,"Comment Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showPopupWindow(final String commentId, final String username) {
        //设置contentView
        View contentView = LayoutInflater.from(AdminCommunicationActivity.this).inflate(R.layout.popupwindow_comment, null);
        //设置各个控件的点击响应
        final EditText editText = contentView.findViewById(R.id.popup_comment_edt);
        final TextView tv = contentView.findViewById(R.id.popup_comment_send_tv);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置软键盘弹出
        final InputMethodManager inputMethodManager = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,0);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);//这里给它设置了弹出的时间

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString();
                //Toast.makeText(getActivity(), commentId+"-----"+username+inputString, Toast.LENGTH_SHORT).show();
                addComment(commentId,username,inputString);
                mPopWindow.dismiss();//让PopupWindow消失
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        //是否具有获取焦点的能力
        mPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_admin_communication, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
}
