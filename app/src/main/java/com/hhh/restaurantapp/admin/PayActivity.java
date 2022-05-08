package com.hhh.restaurantapp.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title, tv_right,tv_total;
    private ListView lv;
    private List<Map<String, String>> list, listCart;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String did,money,username;
    private ImageView iv_back;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay);
        initBar();
        initView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Ordering Details");
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("Pay");
        tv_right.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        total = 0;
        tv_total = findViewById(R.id.tv_total);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        did = sp.getString("id", "");
        username = sp.getString("username", "");
        money = sp.getString("money", "");
        list = new ArrayList<>();
        listCart = new ArrayList<>();
        lv = findViewById(R.id.lv);
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if( total > Integer.parseInt(money)){
                    Toast.makeText(this, "Insufficient Balance！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String format = simpleDateFormat.format(date);
                for (int i = 0; i < list.size(); i++) {
                    addRecord(list.get(i).get("name"),list.get(i).get("price"),list.get(i).get("number"),format);
                }
                deleteAllCart(did);
                deleteAllDesk(did);
                altBank(username,String.valueOf(Integer.parseInt(money)-total));
                finish();
                Toast.makeText(this, "Successfully ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        did = sp.getString("id", "");
        username = sp.getString("username", "");
        money = sp.getString("money", "");
        findBank(username);
        findCart(did);
        findDish();
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
                convertView = LayoutInflater.from(PayActivity.this).inflate(R.layout.item_record, null);
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
                    holder.tv_price.setText(list.get(position).get("price") + "dollars");
                }
                if (!number.equals("")) {
                    holder.tv_number.setText("x" + list.get(position).get("number"));
                }
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_name, tv_price, tv_number;

            public ViewHolder(View view) {
                tv_name = view.findViewById(R.id.tv_name);
                tv_price = view.findViewById(R.id.tv_price);
                tv_number = view.findViewById(R.id.tv_number);
            }
        }
    }

    private void deleteAllDesk(String number) {
        OkHttpUtil.deleteAllDesk(number, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                        } else {
                        }
                    }
                });
            }
        });
    }
    private void deleteAllCart(String did) {
        OkHttpUtil.deleteAllCart(did, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                        } else {
                        }
                    }
                });
            }
        });
    }
    public void initAdapter() {
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item_record,
                new String[]{"name", "price", "number"},
                new int[]{R.id.tv_name, R.id.tv_price, R.id.tv_number}
        );
        //加载数据
        lv.setAdapter(adapter);
    }

    private void addRecord(String name, String price,String number,String time) {
        OkHttpUtil.addRecord(name,price,number,time, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                        } else {
                        }
                    }
                });
            }
        });
    }

    private void findDish() {
        OkHttpUtil.findDish(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                list.clear();
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
                            int c = getNumber((String) json.get("id"));
                            map.put("number", String.valueOf(c));
                            if(c>0){
                                list.add(map);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findCart(final String did) {
        OkHttpUtil.findCart(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                listCart.clear();
                total = 0;
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
                            map.put("did", (String) json.get("did"));
                            map.put("cid", (String) json.get("cid"));
                            String did1 = (String) json.get("did");
                            String price = (String) json.get("price");
                            if (did1.equals(did)) {
                                listCart.add(map);
                                total = total+ Integer.parseInt(price);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_total.setText("Total："+total+"dollars");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getNumber(String cid) {
        int count = 0;
        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).get("cid").equals(cid)) {
                count++;
            }
        }
        return count;
    }

    private void findBank(String username) {
        OkHttpUtil.findBank(username,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PayActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            finish();
                        } else {
                        }
                    }
                });
            }
        });
    }
}
