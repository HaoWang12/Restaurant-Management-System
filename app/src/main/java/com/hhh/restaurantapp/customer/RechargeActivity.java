package com.hhh.restaurantapp.customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.restaurantapp.OkHttpUtil;
import com.hhh.restaurantapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String username,money,content;
    private EditText et_content;
    private TextView tv_title,tv_submit,tv_money;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initBar();
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username","");

        et_content = findViewById(R.id.et_content);
        tv_submit = findViewById(R.id.tv_submit);
        tv_money = findViewById(R.id.tv_money);
        tv_submit.setOnClickListener(this);
        findBank(username);
    }

    private void initBar() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Recharge");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_submit:
                content = et_content.getText().toString().trim();
                if(content.equals("")){
                    Toast.makeText(this, "Recharge amount cannot be blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                money = String.valueOf(Integer.parseInt(money)+Integer.parseInt(content));
                altBank(username,money);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void findBank(String username) {
        OkHttpUtil.findBank(username,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            money = (String) json.get("money");
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_money.setText("The current balance is:"+money+"dollars");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void altBank(String username,String money) {
        OkHttpUtil.altBank(username,money, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RechargeActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RechargeActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
