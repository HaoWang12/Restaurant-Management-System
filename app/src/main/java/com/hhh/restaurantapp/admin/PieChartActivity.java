package com.hhh.restaurantapp.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hhh.restaurantapp.OkHttpUtil;
import com.hhh.restaurantapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class PieChartActivity extends AppCompatActivity implements View.OnClickListener {
    private PieChartView pieChart;
    private LinearLayout llType, llType2;
    private double total_into,total_out;
    private ImageView iv_back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        initBar();
        llType = findViewById(R.id.ll_fenlei);
        llType2 = findViewById(R.id.ll_fenlei2);
        pieChart = findViewById(R.id.pieChart);
        findPurchase();
        findRecord();
    }

    private void initBar() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Pie Chart");
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    private void findRecord() {
        OkHttpUtil.findRecord(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PieChartActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //??????????????????????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //??????????????????
                final String responseData = response.body().string();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            String money = (String) json.get("price");
                            String number = (String) json.get("number");
                            total_into = total_into + Double.parseDouble(money)*Integer.parseInt(number);
                            BigDecimal b = new BigDecimal(total_into);
                            double v = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                            total_into = v;
                        }
                    }
                    System.out.println("into:"+total_into);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPieChartType();
                    }
                });
            }
        });
    }

    private void findPurchase() {
        OkHttpUtil.findPurchase(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PieChartActivity.this, "The network is abnormal, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //??????????????????????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //??????????????????
                final String responseData = response.body().string();
                JSONArray ls = null;
                try {
                    ls = new JSONArray(responseData);
                    if (ls.length() > 0) {
                        for (int i = 0; i < ls.length(); i++) {
                            JSONObject json = new JSONObject(ls.get(i).toString());
                            String money = (String) json.get("money");
                            total_out = total_out + Double.parseDouble(money);
                            BigDecimal b = new BigDecimal(total_out);
                            double v = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                            total_out = v;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPieChartType() {
        List<String> fenleiList = new ArrayList<>();
        fenleiList.add("Into");
        fenleiList.add("Out");
        //??????
        List<SliceValue> values = new ArrayList<>();
        //??????list
        final List<Integer> colorData = new ArrayList<>();
        //????????????
        final List<String> titleData = new ArrayList<>();
        //10?????????
        colorData.add(Color.parseColor("#85B74F"));
        colorData.add(Color.parseColor("#009BDB"));
        colorData.add(Color.parseColor("#FF0000"));
        colorData.add(Color.parseColor("#9569F8"));
        colorData.add(Color.parseColor("#F87C67"));
        colorData.add(Color.parseColor("#F1DA3D"));
        colorData.add(Color.parseColor("#87EA39"));
        colorData.add(Color.parseColor("#48AEFA"));
        colorData.add(Color.parseColor("#4E5052"));
        colorData.add(Color.parseColor("#D36458"));

        titleData.add(fenleiList.get(0));
        SliceValue sliceValue = new SliceValue();
        sliceValue.setValue((float) total_into);
        sliceValue.setColor(colorData.get(titleData.size() - 1));
        values.add(sliceValue);

        titleData.add(fenleiList.get(1));
        SliceValue sliceValue1 = new SliceValue();
        sliceValue1.setValue((float) total_out);
        sliceValue1.setColor(colorData.get(titleData.size() - 1));
        values.add(sliceValue1);

        System.out.println("total_into:"+total_into);
        System.out.println("total_out:"+total_out);
        llType.removeAllViews();
        llType2.removeAllViews();
        for (int i = 0; i < titleData.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tv_item, null, false);
            TextView viewById = (TextView) view.findViewById(R.id.tv_pe_cfenlei);
            TextView viewById2 = (TextView) view.findViewById(R.id.tv_pe_fenlei);
            viewById.setBackgroundColor(colorData.get(i));
            viewById2.setText(titleData.get(i));
            if (i <= 3) {
                llType.addView(view);
            } else {
                llType2.addView(view);
            }
        }
        final PieChartData pieChardata = new PieChartData();
        //??????????????????
        pieChardata.setHasLabels(true);
        pieChardata.setValueLabelBackgroundEnabled(false);
        pieChardata.setValueLabelsTextColor(Color.parseColor("#000000"));
        //true???????????????????????????????????????????????????  false?????????????????????
        pieChardata.setHasLabelsOnlyForSelected(false);
        //true?????????????????????????????????????????? false???????????????????????????
        pieChardata.setHasLabelsOutside(false);
        //true??????????????? false???????????????
        pieChardata.setHasCenterCircle(true);
        //?????????????????????????????????
        pieChardata.setSlicesSpacing(2);
        //??????????????????????????????????????????????????? ??????assets???????????????fonts????????????ttf????????????????????????
        //Typeface tf = Typeface.createFromAsset(getAssets(), "??????????????????????????????");
        //pieChardata.setCenterText1Typeface(tf);
        //???????????? ???????????????????????? ?????????????????????
        pieChardata.setValues(values);

        //??????????????????????????? ??????setHasCenterCircle(true)???????????????????????? ????????????????????????????????????
        pieChardata.setCenterCircleColor(Color.WHITE);
        //??????????????????????????? ????????????setHasCenterCircle(true)??????
        pieChardata.setCenterCircleScale(0.4f);

        //???????????????????????????
        pieChart.setPieChartData(pieChardata);
        //true???????????????????????????  false??????????????????????????????????????????????????????????????????
        pieChart.setValueSelectionEnabled(true);
        pieChart.setAlpha(0.9f);//???????????????
        //?????????????????? ?????????????????? 1???????????????????????????????????????????????????????????????????????????0.9f ??????????????????????????????
        pieChart.setCircleFillRatio(0.9f);

//        ???????????? (???????????????????????????????????????)
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
