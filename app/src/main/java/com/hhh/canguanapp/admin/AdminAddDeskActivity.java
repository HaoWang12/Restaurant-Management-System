package com.hhh.canguanapp.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.canguanapp.OkHttpUtil;
import com.hhh.canguanapp.R;

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

public class AdminAddDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText et_number,et_total;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    private String number,total,username;
    private List<Map<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_desk);
        initBar();
        initView();
    }

    private void initView() {
        findDesk();
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        list = new ArrayList<>();
        username = sp.getString("username","");
        et_number = findViewById(R.id.et_number);
        et_total = findViewById(R.id.et_total);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("Add a table");
        tv_right.setText("Submit");
        tv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_right:
                number = et_number.getText().toString().trim();
                total = et_total.getText().toString().trim();
                if(total.equals("") || number.equals("")){
                    Toast.makeText(this, "Incomplete information！", Toast.LENGTH_SHORT).show();
                    return;
                }
                int flag = 0;
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).get("number").equals(number)){
                        flag = 1;
                    }
                }
                if(flag == 0){
                    addDesk(number,total,username);
                }else {
                    Toast.makeText(this, "The table is occupied！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void findDesk() {
        OkHttpUtil.findDesk(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminAddDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                list.clear();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", (String) json.get("id"));
                            map.put("number", (String) json.get("number"));
                            map.put("total", (String) json.get("total"));
                            list.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addDesk(String number, String total,String username) {
        OkHttpUtil.addDesk(number,total,username, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminAddDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                System.out.println(responseData);
                //在主线程更新ui和响应用户操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            finish();
                            Toast.makeText(AdminAddDeskActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminAddDeskActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
