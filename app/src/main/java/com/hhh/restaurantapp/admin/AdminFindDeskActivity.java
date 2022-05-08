package com.hhh.restaurantapp.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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

public class AdminFindDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title, tv_right;
    private ListView lv;
    private List<Map<String, String>> list;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String username;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_find_desk);
        initBar();
        initView();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Table management");
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("Add");
        tv_right.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString("username","");
        list = new ArrayList<>();
        lv = findViewById(R.id.lv);
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if(list.size()>0){
                    Toast.makeText(this, "EveryBody only one table can be opened", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, AdminAddDeskActivity.class);
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
        username = sp.getString("username","");
        findDesk(username);
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
                convertView = LayoutInflater.from(AdminFindDeskActivity.this).inflate(R.layout.item_desk, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > 0) {
                String number = list.get(position).get("number");
                String total = list.get(position).get("total");
                if (!number.equals("")) {
                    holder.tv_number.setText("Number："+list.get(position).get("number"));
                }
                if (!total.equals("")) {
                    holder.tv_total.setText(list.get(position).get("total")+"person");
                }
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_number,tv_total;

            public ViewHolder(View view) {
                tv_number = view.findViewById(R.id.tv_number);
                tv_total = view.findViewById(R.id.tv_total);
            }
        }
    }

    public void initAdapter() {
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item_desk,
                new String[]{"number", "total"},
                new int[]{R.id.tv_number, R.id.tv_total}
        );
        //加载数据
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("id", list.get(i).get("id"));
                editor.commit();
                Intent intent = new Intent(AdminFindDeskActivity.this, AltDeskActivity.class);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFindDeskActivity.this);
                builder.setMessage("Are you sure you want to delete it？");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDesk(list.get(position).get("id"));
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private void deleteDesk(String id) {
        OkHttpUtil.deleteDesk(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminFindDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminFindDeskActivity.this, "Delete succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminFindDeskActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void findDesk(final String username) {
        OkHttpUtil.findDesk(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdminFindDeskActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            map.put("number", (String) json.get("number"));
                            map.put("total", (String) json.get("total"));
                            map.put("username", (String) json.get("username"));
                            String username1 = (String) json.get("username");
                            if(username.equals(username1)){
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
}
