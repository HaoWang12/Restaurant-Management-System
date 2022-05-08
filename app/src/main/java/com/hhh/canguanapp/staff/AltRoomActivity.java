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

public class AltRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText et_time,et_note,et_number,et_phone;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    private String time,note,number,phone,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt_room);
        initBar();
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        time = sp.getString("time","");
        note = sp.getString("note","");
        number = sp.getString("number","");
        phone = sp.getString("phone","");
        id = sp.getString("id","");

        et_time = findViewById(R.id.et_time);
        et_number = findViewById(R.id.et_number);
        et_phone = findViewById(R.id.et_phone);
        et_note = findViewById(R.id.et_note);

        et_note.setText(note);
        et_time.setText(time);
        et_phone.setText(phone);
        et_number.setText(number);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("Alt Reservation");
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
                number = et_number.getText().toString().trim();
                note = et_note.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                if(phone.equals("") || note.equals("")||number.equals("")||time.equals("")){
                    Toast.makeText(this, "Incomplete information！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (note.equals("")){
                    note = "empty";
                }
                altRoom(id,phone,number,time,note);
                break;
        }
    }

    private void altRoom(String id,String phone, String number, String time, String note) {
        OkHttpUtil.altRoom(id,phone,number,time,note, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AltRoomActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AltRoomActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AltRoomActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
