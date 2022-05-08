package com.hhh.canguanapp.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.canguanapp.OkHttpUtil;
import com.hhh.canguanapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminAltCanActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText et_name,et_price,et_note,et_newprice;
    private Spinner sp_answer;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    private String name,price,note,types,id,newprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alt_can);
        initBar();
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        editor = sp.edit();
        newprice = sp.getString("newprice","");
        name = sp.getString("name","");
        id = sp.getString("id","");
        price = sp.getString("price","");
        note = sp.getString("note","");
        types = sp.getString("types","");
        et_name = findViewById(R.id.et_name);
        et_price = findViewById(R.id.et_price);
        et_note = findViewById(R.id.et_note);
        sp_answer = findViewById(R.id.sp_answer);
        sp_answer = findViewById(R.id.sp_answer);
        et_newprice = findViewById(R.id.et_newprice);

        et_name.setText(name);
        et_note.setText(note);
        et_price.setText(price);
        et_newprice.setText(newprice);

        final String[] stringSta = new String[]{"Cold Dish", "Hot Dish","Soup","Staple","Drinks"};
        ArrayAdapter<String> adapterSta = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stringSta);
        sp_answer.setAdapter(adapterSta);
        if(types.equals("Cold Dish")){
            sp_answer.setSelection(0,true);
        }else if(types.equals("Hot Dish")){
            sp_answer.setSelection(1,true);
        }else if(types.equals("Soup")){
            sp_answer.setSelection(2,true);
        }else if(types.equals("Staple")){
            sp_answer.setSelection(3,true);
        }else{
            sp_answer.setSelection(4,true);
        }
        sp_answer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                types = stringSta[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("Alt Dish");
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
                name = et_name.getText().toString().trim();
                price = et_price.getText().toString().trim();
                note = et_note.getText().toString().trim();
                newprice = et_newprice.getText().toString().trim();
                if(name.equals("") || price.equals("")||newprice.equals("")){
                    Toast.makeText(this, "Incomplete information！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (note.equals("")){
                    note = "empty";
                }
                altCai(id,name,price,types,note,newprice);
                break;
        }
    }

    private void altCai(String id,String name, String price, String types, String note,String newprice) {
        OkHttpUtil.altCai(id,name,price,types,note,newprice, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminAltCanActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminAltCanActivity.this,"Submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminAltCanActivity.this,"Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
