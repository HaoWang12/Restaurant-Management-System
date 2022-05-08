package com.hhh.restaurantapp.staff;

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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltPurchaseActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText et_time,et_note,et_money,et_content;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    private String time,note,content,money,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt_purchase);
        initBar();
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        time = sp.getString("time","");
        note = sp.getString("note","");
        content = sp.getString("content","");
        money = sp.getString("money","");
        id = sp.getString("id","");

        et_time = findViewById(R.id.et_time);
        et_money = findViewById(R.id.et_money);
        et_content = findViewById(R.id.et_content);
        et_note = findViewById(R.id.et_note);

        et_note.setText(note);
        et_time.setText(time);
        et_content.setText(content);
        et_money.setText(money);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("Alt Purchase");
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
                altPurchase(id,content,money,time,note);
                break;
        }
    }

    private void altPurchase(String id, String phone, String number, String time, String note) {
        OkHttpUtil.altPurchase(id,phone,number,time,note, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AltPurchaseActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AltPurchaseActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AltPurchaseActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
