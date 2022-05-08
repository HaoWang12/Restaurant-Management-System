package com.hhh.canguanapp.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhh.canguanapp.OkHttpUtil;
import com.hhh.canguanapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddCaiGouActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText et_time,et_note,et_money,et_content;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    private String time,note,money,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cai_gou);
        initBar();
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        et_time = findViewById(R.id.et_time);
        et_money = findViewById(R.id.et_money);
        et_content = findViewById(R.id.et_content);
        et_note = findViewById(R.id.et_note);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("Add Purchase");
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
                time = et_time.getText().toString().trim();
                content = et_content.getText().toString().trim();
                note = et_note.getText().toString().trim();
                money = et_money.getText().toString().trim();
                if(content.equals("") || note.equals("")||money.equals("")||time.equals("")){
                    Toast.makeText(this, "Incomplete information！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (note.equals("")){
                    note = "empty";
                }
                addCaiGou(content,money,time,note);
                break;
        }
    }

    private void addCaiGou(String content, String money, String time, String note) {
        OkHttpUtil.addCaiGou(content,money,time,note, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddCaiGouActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddCaiGouActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddCaiGouActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
