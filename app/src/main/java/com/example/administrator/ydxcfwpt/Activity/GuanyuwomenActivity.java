package com.example.administrator.ydxcfwpt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.ydxcfwpt.R;

//关于我们
public class GuanyuwomenActivity extends BaseActivity {

    private ImageView iv_back;
    private LinearLayout ll_gongsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanyuwomen);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_guanyuwomen_back);
        ll_gongsi = (LinearLayout) findViewById(R.id.ll_guanyuwomen_gongsijieshao);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_gongsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuanyuwomenActivity.this,Gongsijieshao.class);
                startActivity(intent);
            }
        });
    }
}
