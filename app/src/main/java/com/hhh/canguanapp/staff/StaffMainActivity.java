package com.hhh.canguanapp.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hhh.canguanapp.R;
import com.hhh.canguanapp.admin.AdminFindCanActivity;
import com.hhh.canguanapp.admin.FindCustomerActivity;
import com.hhh.canguanapp.customer.AdminCommunicationActivity;

public class StaffMainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll01,ll02,ll03,ll04,ll_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        ll01 = findViewById(R.id.ll01);
        ll02 = findViewById(R.id.ll02);
        ll03 = findViewById(R.id.ll03);
        ll04 = findViewById(R.id.ll04);
        ll_exit = findViewById(R.id.ll_exit);

        ll01.setOnClickListener(this);
        ll02.setOnClickListener(this);
        ll03.setOnClickListener(this);
        ll04.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll01:
                Intent intent = new Intent(StaffMainActivity.this, FindCustomerActivity.class);
                startActivity(intent);
                break;
            case R.id.ll03:
                Intent intent2 = new Intent(StaffMainActivity.this, AdminFindCanActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll02:
                Intent intent3 = new Intent(StaffMainActivity.this, FindRoomActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll04:
                Intent intent4 = new Intent(StaffMainActivity.this, FindCaiGouActivity.class);
                startActivity(intent4);
                break;
            case R.id.ll_exit:
                finish();
                break;
        }
    }
}
