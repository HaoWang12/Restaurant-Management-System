package com.hhh.canguanapp.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hhh.canguanapp.R;
import com.hhh.canguanapp.admin.AdminFindDeskActivity;

public class CustomerMainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll01,ll02,ll03,ll_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        ll01 = findViewById(R.id.ll01);
        ll02 = findViewById(R.id.ll02);
        ll03 = findViewById(R.id.ll03);
        ll_exit = findViewById(R.id.ll_exit);

        ll01.setOnClickListener(this);
        ll02.setOnClickListener(this);
        ll03.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll01:
                Intent intent1 = new Intent(CustomerMainActivity.this, RechargeActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll02:
                Intent intent = new Intent(CustomerMainActivity.this, AdminFindDeskActivity.class);
                startActivity(intent);
                break;
            case R.id.ll03:
                Intent intent3 = new Intent(CustomerMainActivity.this, AdminCommunicationActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll_exit:
                finish();
                break;
        }
    }
}
