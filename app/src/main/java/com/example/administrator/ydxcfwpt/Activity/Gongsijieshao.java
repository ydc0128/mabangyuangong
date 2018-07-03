package com.example.administrator.ydxcfwpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.R;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Gongsijieshao extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_biaoti;
    private TextView tv_neirong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_content_back);
        tv_title = (TextView) findViewById(R.id.tv_content_title);
        tv_biaoti = (TextView) findViewById(R.id.tv_content_biaoti);
        tv_neirong = (TextView) findViewById(R.id.tv_content_neirong);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText("公司介绍");
        tv_biaoti.setText(R.string.tv_content_gongsijieshao);
        tv_neirong.setText(R.string.tv_content_gongsijieshao_neirong);
    }

}
