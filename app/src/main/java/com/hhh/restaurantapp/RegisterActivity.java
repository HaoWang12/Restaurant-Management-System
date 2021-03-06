package com.hhh.restaurantapp;

import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_register;
    private EditText et_ID, et_password, et_again,et_name;
    private String username, password, again,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_ID = (EditText) findViewById(R.id.et_ID);
        et_ID.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0' };
            }
            @Override
            public int getInputType() {
                // TODO Auto-generated method stub
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        et_again = (EditText) findViewById(R.id.et_again);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                username = et_ID.getText().toString().trim();
                password = et_password.getText().toString().trim();
                again = et_again.getText().toString().trim();
                name = et_name.getText().toString().trim();

                if (username.equals("") || password.equals("") || again.equals("") || name.equals("")) {
                    Toast.makeText(this, "Please improve the information???", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!password.equals(again)) {
                        Toast.makeText(this, "Two passwords are inconsistent???", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        registerWithOkHttp(username, password,name,"customer");
                    }
                }
                break;

        }
    }

    //????????????
    private void registerWithOkHttp(String account, String password,String name,String statue) {
        OkHttpUtil.registerWithOkHttp(account, password,name,statue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "The network is abnormal. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //??????????????????????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //??????????????????
                final String responseData = response.body().string();
                System.out.println(responseData);
                //??????????????????ui?????????????????????
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            Toast.makeText(RegisterActivity.this, "Registration succeeded", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "User already exists, registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
