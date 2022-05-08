package com.hhh.restaurantapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.restaurantapp.R;
import com.hhh.restaurantapp.customer.AdminCommunicationActivity;
import com.hhh.restaurantapp.staff.FindRoomActivity;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll01,ll02,ll03,ll04,ll05,ll06,ll_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ll01 = findViewById(R.id.ll01);
        ll02 = findViewById(R.id.ll02);
        ll03 = findViewById(R.id.ll03);
        ll04 = findViewById(R.id.ll04);
        ll05 = findViewById(R.id.ll05);
        ll06 = findViewById(R.id.ll06);
        ll_exit = findViewById(R.id.ll_exit);

        ll01.setOnClickListener(this);
        ll02.setOnClickListener(this);
        ll03.setOnClickListener(this);
        ll04.setOnClickListener(this);
        ll05.setOnClickListener(this);
        ll06.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll01:
                Intent intent = new Intent(AdminMainActivity.this,FindCustomerActivity.class);
                startActivity(intent);
                break;
            case R.id.ll02:
                Intent intent2 = new Intent(AdminMainActivity.this,FindStaffActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll03:
                Intent intent3 = new Intent(AdminMainActivity.this, AdminFindDishActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll04:
                Intent intent5 = new Intent(AdminMainActivity.this, FindRoomActivity.class);
                startActivity(intent5);
                break;
            case R.id.ll05:
                Intent intent4 = new Intent(AdminMainActivity.this,NumberActivity.class);
                startActivity(intent4);
                break;
            case R.id.ll06:
                Intent intent6 = new Intent(AdminMainActivity.this, AdminCommunicationActivity.class);
                startActivity(intent6);
                break;
            case R.id.ll_exit:
               finish();
                break;
        }
    }
}
