package com.hhh.restaurantapp.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.hhh.restaurantapp.OkHttpUtil;
import com.hhh.restaurantapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NumberActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title, tv_right,tv_total;
    private ListView lv;
    private List<Map<String, String>> listDish,listRecord,list;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String start,end;
    private ImageView iv_back;
    private int total;
    private EditText et_start,et_end;
    private Button bt_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_number);
        initBar();
        initView();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Running Water");
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("Chart");
        tv_right.setOnClickListener(this);
    }

    private void initView() {
        bt_submit = findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        et_end = findViewById(R.id.et_end);
        et_start = findViewById(R.id.et_start);
        tv_total = findViewById(R.id.tv_total);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        list = new ArrayList<>();
        listDish = new ArrayList<>();
        listRecord = new ArrayList<>();
        lv = findViewById(R.id.lv);
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(NumberActivity.this, PieChartActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_submit:
                start = et_start.getText().toString().trim();
                end = et_end.getText().toString().trim();
                if (start.equals("")||end.equals("")){
                    Toast.makeText(this, "Incomplete filtering information！", Toast.LENGTH_SHORT).show();
                    return;
                }
                findRecord(start,end);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        findDish();
        findRecord("1000-01-01 00:00","3000-01-01 00:00");
    }

    private void getData(){
        total = 0;
        list.clear();
        for (int i = 0; i < listDish.size(); i++) {
            String name = listDish.get(i).get("name");
            System.out.println("name:"+name);
            int count = 0;
            for (int j = 0; j < listRecord.size(); j++) {
                if(listRecord.get(j).get("name").equals(name)){
                    count = count + Integer.parseInt(listRecord.get(j).get("number"));
                }
            }
            if(count>0){
                Map<String, String> map = new HashMap<>();
                map.put("name",name);
                map.put("number", String.valueOf(count));
                map.put("price",listDish.get(i).get("price"));
                list.add(map);
                total = total+count*Integer.parseInt(listDish.get(i).get("price"));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = i; j < list.size(); j++) {
                if(Integer.parseInt(list.get(i).get("number"))>Integer.parseInt(list.get(j).get("number"))){
                    Map<String, String> map = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,map);
                }
            }
        }
        List<Map<String,String>> tempList = new ArrayList<>();
        tempList.addAll(list);
        list.clear();
        for (int i = tempList.size()-1; i>=0; i--) {
            list.add(tempList.get(i));
        }
        System.out.println("list:"+list);
        adapter.notifyDataSetChanged();
        tv_total.setText("Sales："+total+" dollars");
    }

    public class MyAdapter extends SimpleAdapter {


        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                         String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(NumberActivity.this).inflate(R.layout.item_message, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > 0) {
                String name = list.get(position).get("name");
                String price = list.get(position).get("price");
                String number = list.get(position).get("number");
                if (!name.equals("")) {
                    holder.tv_name.setText(list.get(position).get("name"));
                }
                if (!price.equals("")) {
                    holder.tv_price.setText(list.get(position).get("price")+" dollars");
                }
                if (!number.equals("")) {
                    holder.tv_number.setText("x"+list.get(position).get("number"));
                }
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_name,tv_price,tv_number;

            public ViewHolder(View view) {
                tv_name = view.findViewById(R.id.tv_name);
                tv_price = view.findViewById(R.id.tv_price);
                tv_number = view.findViewById(R.id.tv_number);
            }
        }
    }

    public void initAdapter() {
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item_message,
                new String[]{"name", "price","number"},
                new int[]{R.id.tv_name, R.id.tv_price,R.id.tv_number}
        );
        //加载数据
        lv.setAdapter(adapter);
    }


    private void findDish() {
        OkHttpUtil.findDish(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NumberActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                listDish.clear();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", (String) json.get("id"));
                            map.put("name", (String) json.get("name"));
                            map.put("price", (String) json.get("price"));
                            map.put("types", (String) json.get("types"));
                            map.put("note", (String) json.get("note"));
                            listDish.add(map);
                        }
                    }
                    System.out.println("listDish:"+listDish);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findRecord(final String start, final String end) {
        OkHttpUtil.findRecord(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NumberActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                listRecord.clear();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", (String) json.get("id"));
                            map.put("name", (String) json.get("name"));
                            map.put("price", (String) json.get("price"));
                            map.put("number", (String) json.get("number"));
                            map.put("time", (String) json.get("time"));
                            String time = (String) json.get("time");
                            if(time.compareTo(start)>=0&&time.compareTo(end)<=0){
                                listRecord.add(map);
                            }
                        }
                    }
                    System.out.println("listRecord:"+listRecord);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
