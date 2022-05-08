package com.hhh.canguanapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.hhh.canguanapp.admin.AdminMainActivity;
import com.hhh.canguanapp.customer.CustomerMainActivity;
import com.hhh.canguanapp.staff.StaffMainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_login, bt_stu_register;
    private EditText et_ID, et_password;
    private String username, password, sta, result;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_ID = (EditText) findViewById(R.id.et_ID);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_stu_register = (Button) findViewById(R.id.bt_register);
        bt_stu_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                username = et_ID.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (et_ID.getText().toString().trim().equals("") ||
                        et_password.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Or the account number password cannot be blank！", Toast.LENGTH_SHORT).show();
                } else {
                    loginWithOkHttp(username, password);
                }
                break;

            case R.id.bt_register:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                break;
        }
    }

    //实现登陆
    private void loginWithOkHttp(final String account, final String password) {
        OkHttpUtil.loginWithOkHttp(account, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                        JSONArray ls = null;
                        sta = "";
                        result = "";
                        try {
                            ls = new JSONArray(responseData);
                            if (ls.length() > 0) {
                                for (int i = 0; i < ls.length(); i++) {
                                    JSONObject json = new JSONObject(ls.get(i).toString());
                                    result = (String) json.get("result");
                                    sta = (String) json.get("sta");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (result.equals("true")) {
                            Toast.makeText(LoginActivity.this, "Landing success", Toast.LENGTH_SHORT).show();
                            if (sta.equals("admin")) {
                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                            } else if (sta.equals("staff")) {
                                Intent intent = new Intent(LoginActivity.this, StaffMainActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                                startActivity(intent);
                            }
                            editor.putString("username", account);
                            editor.putString("password", password);
                            editor.putString("statue", sta);
                            editor.commit();
                        } else {
                            Toast.makeText(LoginActivity.this, "The account number or password is incorrect！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
