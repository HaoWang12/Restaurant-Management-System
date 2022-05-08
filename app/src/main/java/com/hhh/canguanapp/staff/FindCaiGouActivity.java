package com.hhh.canguanapp.staff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.hhh.canguanapp.OkHttpUtil;
import com.hhh.canguanapp.R;

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

public class FindCaiGouActivity extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_find_cai_gou);
        initBar();
        initView();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Purchase management");
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
        list = new ArrayList<>();
        lv = findViewById(R.id.lv);
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(this, AddCaiGouActivity.class);
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
        findCaiGou();
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
                convertView = LayoutInflater.from(FindCaiGouActivity.this).inflate(R.layout.item_caigou, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > 0) {
                String content = list.get(position).get("content");
                String money = list.get(position).get("money");
                String time = list.get(position).get("time");
                String note = list.get(position).get("note");
                if (!content.equals("")) {
                    holder.tv_content.setText(list.get(position).get("content"));
                }
                if (!money.equals("")) {
                    holder.tv_money.setText(list.get(position).get("money")+" dollars");
                }
                if (!time.equals("")) {
                    holder.tv_time.setText("Data："+list.get(position).get("time"));
                }
                if (!note.equals("")) {
                    holder.tv_note.setText("Note:"+list.get(position).get("note"));
                }
            }
            return convertView;
        }


        class ViewHolder {
            TextView tv_content,tv_money,tv_note,tv_time;

            public ViewHolder(View view) {
                tv_content = view.findViewById(R.id.tv_content);
                tv_money = view.findViewById(R.id.tv_money);
                tv_note = view.findViewById(R.id.tv_note);
                tv_time = view.findViewById(R.id.tv_time);
            }
        }
    }

    public void initAdapter() {
        adapter = new MyAdapter(
                this,
                list,
                R.layout.item_caigou,
                new String[]{"content", "money","note","time"},
                new int[]{R.id.tv_content, R.id.tv_money,R.id.tv_note,R.id.tv_time}
        );
        //加载数据
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editor.putString("money", list.get(i).get("money"));
                editor.putString("content", list.get(i).get("content"));
                editor.putString("note", list.get(i).get("note"));
                editor.putString("time", list.get(i).get("time"));
                editor.putString("id", list.get(i).get("id"));
                editor.commit();
                Intent intent = new Intent(FindCaiGouActivity.this, AltCaiGouActivity.class);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindCaiGouActivity.this);
                builder.setMessage("Are you sure you want to delete it？");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCaiGou(list.get(position).get("id"));
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();

                return true;
            }
        });
    }

    private void deleteCaiGou(String id) {
        OkHttpUtil.deleteCaiGou(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FindCaiGouActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FindCaiGouActivity.this, "Delete succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindCaiGouActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void findCaiGou() {
        OkHttpUtil.findCaiGou(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FindCaiGouActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
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
                            map.put("content", (String) json.get("content"));
                            map.put("money", (String) json.get("money"));
                            map.put("time", (String) json.get("time"));
                            map.put("note", (String) json.get("note"));
                            list.add(map);
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
