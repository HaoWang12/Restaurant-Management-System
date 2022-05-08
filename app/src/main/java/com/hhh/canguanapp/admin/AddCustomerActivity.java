package com.hhh.canguanapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhh.canguanapp.OkHttpUtil;
import com.hhh.canguanapp.R;
import com.hhh.canguanapp.RegisterActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_register;
    private EditText et_ID, et_password, et_again,et_name;
    private String username, password, again,name;
    private TextView tv_title,tv_right;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initBar();
        et_ID = (EditText) findViewById(R.id.et_ID);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        et_again = (EditText) findViewById(R.id.et_again);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Add Customer");
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_register:
                username = et_ID.getText().toString().trim();
                password = et_password.getText().toString().trim();
                again = et_again.getText().toString().trim();
                name = et_name.getText().toString().trim();

                if (username.equals("") || password.equals("") || again.equals("") || name.equals("")) {
                    Toast.makeText(this, "Please improve the information！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!password.equals(again)) {
                        Toast.makeText(this, "Two passwords are inconsistent！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        addBank(username,"0");
                        registerWithOkHttp(username, password,name,"customer");
                    }
                }
                break;

        }
    }

    //实现注册
    private void registerWithOkHttp(String account, String password,String name,String statue) {
        OkHttpUtil.registerWithOkHttp(account, password,name,statue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddCustomerActivity.this, "The network is abnormal. Please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddCustomerActivity.this, "Add succeeded", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddCustomerActivity.this, "User already exists, add failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void addBank(String username,String money) {
        OkHttpUtil.addBank(username, money, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddCustomerActivity.this, "The network is abnormal. Please try again!", Toast.LENGTH_SHORT).show();
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
                        } else {
                        }
                    }
                });
            }
        });
    }

}
