package com.example.administrator.ydxcfwpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TiXianXiangQingActivity extends AppCompatActivity {
//提现成功
    @BindView(R.id.tv_txxq_daozhangshijian)
    TextView tvTxxqDaozhangshijian;
    @BindView(R.id.tv_txxq_yinhangka)
    TextView tvTxxqYinhangka;
    @BindView(R.id.tv_txxq_tixianjine)
    TextView tvTxxqTixianjine;
    @BindView(R.id.bt_txxq_tixian)
    Button btTxxqTixian;
    @BindView(R.id.iv_tixianxiangqing_back)
    ImageView ivTixianxiangqingBack;

    private String time;
    private String jine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_xian_xiang_qing);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        long shijian = intent.getLongExtra("shijian", 0);
        Long now = shijian + 3 * 24 * 60 * 60 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = sdf.format(new Date(now));
        jine = intent.getStringExtra("jine");
        setViews();

    }

    private void setViews() {
        tvTxxqTixianjine.setText(jine);
        tvTxxqDaozhangshijian.setText(time);
        String kahao = Contast.worker.getW_BankCard();
        String weihao = kahao.substring(kahao.length()-4,kahao.length());
        tvTxxqYinhangka.setText(Contast.worker.getW_BankName()+"("+weihao+")");
    }


    @OnClick({R.id.iv_tixianxiangqing_back, R.id.bt_txxq_tixian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tixianxiangqing_back:
                finish();
                break;
            case R.id.bt_txxq_tixian:
                finish();
                break;
        }
    }
}
