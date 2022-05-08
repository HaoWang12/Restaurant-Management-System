package com.hhh.restaurantapp.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.restaurantapp.OkHttpUtil;
import com.hhh.restaurantapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddStaffActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_register;
    private EditText et_ID, et_password, et_again,et_name;
    private String username, password, again,name;
    private TextView tv_title,tv_right;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
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
        tv_title.setText("Add Staff");
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
                        registerWithOkHttp(username, password,name,"staff");
                    }
                }
                break;

        }
    }

    private void registerWithOkHttp(String account, String password,String name,String statue) {
        OkHttpUtil.registerWithOkHttp(account, password,name,statue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddStaffActivity.this, "The network is abnormal. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                System.out.println(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            Toast.makeText(AddStaffActivity.this, "Add succeeded", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddStaffActivity.this, "User already exists, add failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
