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

public class AltDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title, tv_right, tv01, tv02, tv03, tv04, tv05;
    private ListView lv;
    private List<Map<String, String>> list, listCart;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String did,username;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alt_desk);
        initBar();
        initView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Order");
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("Pay");
        tv_right.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        tv01 = findViewById(R.id.tv01);
        tv02 = findViewById(R.id.tv02);
        tv03 = findViewById(R.id.tv03);
        tv04 = findViewById(R.id.tv04);
        tv05 = findViewById(R.id.tv05);

        tv01.setOnClickListener(this);
        tv02.setOnClickListener(this);
        tv03.setOnClickListener(this);
        tv04.setOnClickListener(this);
        tv05.setOnClickListener(this);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username", "");
        did = sp.getString("id", "");
        list = new ArrayList<>();
        listCart = new ArrayList<>();
        lv = findViewById(R.id.lv);
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv01:
                tv01.setBackgroundResource(R.color.white);
                tv02.setBackgroundResource(R.color.bottom);
                tv03.setBackgroundResource(R.color.bottom);
                tv04.setBackgroundResource(R.color.bottom);
                tv05.setBackgroundResource(R.color.bottom);
                findDish("Cold Dish");
                break;
            case R.id.tv02:
                tv02.setBackgroundResource(R.color.white);
                tv01.setBackgroundResource(R.color.bottom);
                tv03.setBackgroundResource(R.color.bottom);
                tv04.setBackgroundResource(R.color.bottom);
                tv05.setBackgroundResource(R.color.bottom);
                findDish("Hot Dish");
                break;
            case R.id.tv03:
                tv03.setBackgroundResource(R.color.white);
                tv02.setBackgroundResource(R.color.bottom);
                tv01.setBackgroundResource(R.color.bottom);
                tv04.setBackgroundResource(R.color.bottom);
                tv05.setBackgroundResource(R.color.bottom);
                findDish("Soup");
                break;
            case R.id.tv04:
                tv04.setBackgroundResource(R.color.white);
                tv02.setBackgroundResource(R.color.bottom);
                tv03.setBackgroundResource(R.color.bottom);
                tv01.setBackgroundResource(R.color.bottom);
                tv05.setBackgroundResource(R.color.bottom);
                findDish("Staple");
                break;
            case R.id.tv05:
                tv05.setBackgroundResource(R.color.white);
                tv02.setBackgroundResource(R.color.bottom);
                tv03.setBackgroundResource(R.color.bottom);
                tv04.setBackgroundResource(R.color.bottom);
                tv01.setBackgroundResource(R.color.bottom);
                findDish("Drinks");
                break;
            case R.id.tv_right:
                finish();
                Intent intent = new Intent(this, PayActivity.class);
                startActivity(intent);
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
        username = sp.getString("username", "");
        did = sp.getString("id", "");
        findCart(did);
        findDish("Cold Dish");
    }

    public interface IvJiaOnclickListener {
        void itemClick(View view, int position);
    }

    public interface IvJianOnclickListener {
        void itemClick(View view, int position);
    }

    public class MyAdapter extends SimpleAdapter {
        private IvJiaOnclickListener ivJiaOnclickListener;
        private IvJianOnclickListener ivJianOnclickListener;

        public void setIvJiaOnclickListener(IvJiaOnclickListener ivJiaOnclickListener) {
            this.ivJiaOnclickListener = ivJiaOnclickListener;
        }

        public void setIvJianOnclickListener(IvJianOnclickListener ivJianOnclickListener) {
            this.ivJianOnclickListener = ivJianOnclickListener;
        }

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                         String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(AltDeskActivity.this).inflate(R.layout.item_user_dish, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > 0) {
                String name = list.get(position).get("name");
                String price = list.get(position).get("price");
                String note = list.get(position).get("note");
                String number = list.get(position).get("number");
                String newprice = list.get(position).get("newprice");
                if (!name.equals("")) {
                    holder.tv_name.setText(list.get(position).get("name"));
                }
                if (!price.equals("")) {
                    holder.tv_price.setText(list.get(position).get("price") + "dollars");
                }
                if (!note.equals("")) {
                    holder.tv_note.setText("Note：" + list.get(position).get("note"));
                }
                if (!number.equals("")) {
                    holder.tv_number.setText(list.get(position).get("number"));
                }
                if (!newprice.equals("")) {
                    holder.tv_newprice.setText(list.get(position).get("newprice"));
                }
            }
            holder.iv_jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivJiaOnclickListener.itemClick(view, position);
                }
            });
            holder.iv_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivJianOnclickListener.itemClick(view, position);
                }
            });
            return convertView;
        }


        class ViewHolder {
            TextView tv_name, tv_price, tv_note, tv_number,tv_newprice;
            ImageView iv_jia, iv_jian;

            public ViewHolder(View view) {
                tv_name = view.findViewById(R.id.tv_name);
                tv_price = view.findViewById(R.id.tv_price);
                tv_note = view.findViewById(R.id.tv_note);
                tv_number = view.findViewById(R.id.tv_number);
                iv_jia = view.findViewById(R.id.iv_jia);
                iv_jian = view.findViewById(R.id.iv_jian);
                tv_newprice = view.findViewById(R.id.tv_newprice);
            }
        }
    }

    public void initAdapter() {
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item_user_dish,
                new String[]{"name", "price", "note", "number","newprice"},
                new int[]{R.id.tv_name, R.id.tv_price, R.id.tv_note, R.id.tv_number,R.id.tv_newprice}
        );
        //加载数据
        lv.setAdapter(adapter);
        adapter.setIvJiaOnclickListener(new IvJiaOnclickListener() {
            @Override
            public void itemClick(View view, int position) {
                String number = list.get(position).get("number");
                addCart(did, list.get(position).get("id"), list.get(position).get("name"), list.get(position).get("price"),
                        list.get(position).get("types"), list.get(position).get("note"),username,list.get(position).get("newprice"));
                list.get(position).put("number", String.valueOf(Integer.parseInt(number) + 1));
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setIvJianOnclickListener(new IvJianOnclickListener() {
            @Override
            public void itemClick(View view, int position) {
                findCart(did);
                String number = list.get(position).get("number");
                if (Integer.parseInt(number) > 0) {
                    System.out.println("id："+getId(listCart.get(position).get("cid")));
                    deleteCart(getId(listCart.get(position).get("cid")));
                    list.get(position).put("number", String.valueOf(Integer.parseInt(number) - 1));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void deleteCart(String id) {
        OkHttpUtil.deleteCart(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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


    private void addCart(String did, String cid, String name, String price, String types, String note,String username,String newprice) {
        OkHttpUtil.addCart(did, cid, name, price, types, note,username,newprice, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AltDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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


    private void findDish(final String types) {
        OkHttpUtil.findDish(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AltDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            map.put("newprice", (String) json.get("newprice"));
                            String types1 = (String) json.get("types");
                            int c = getNumber((String) json.get("id"));
                            map.put("number", String.valueOf(c));
                            if (types1.equals(types)) {
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
                        Toast.makeText(AltDeskActivity.this, "网络异常，请重试!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                listCart.clear();
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
                            map.put("newprice", (String) json.get("newprice"));
                            String did1 = (String) json.get("did");
                            if (did1.equals(did)) {
                                listCart.add(map);
                            }
                        }
                    }
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

    private String getId(String cid) {
        String s="";
        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).get("cid").equals(cid)) {
                s = listCart.get(i).get("id");
            }
        }
        return s;
    }
}
