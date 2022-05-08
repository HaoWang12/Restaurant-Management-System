package com.hhh.canguanapp.customer;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;


public class PublishCommunicationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_content;
    private TextView tv_title,tv_right;
    private String username,content,time;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_communication);
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username","");
        et_content = findViewById(R.id.et_content);
        initBar();
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        tv_title.setText("Publish Comment");
        tv_right.setText("Submit");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_right:
                content = et_content.getText().toString().trim();
                if(content.equals("")){
                    Toast.makeText(this, "Evaluation content cannot be empty！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String format = simpleDateFormat.format(date);
                addCommunication(username,content,format);
                break;

        }
    }

    private void addCommunication(String username, String content,String time) {
        OkHttpUtil.addCommunication(username,content,time, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PublishCommunicationActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PublishCommunicationActivity.this,"Publish successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PublishCommunicationActivity.this,"Publishing failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
