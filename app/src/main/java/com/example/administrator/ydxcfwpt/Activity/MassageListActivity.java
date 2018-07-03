package com.example.administrator.ydxcfwpt.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Adapter.DingdanAdapter;
import com.example.administrator.ydxcfwpt.Adapter.MSGAdapter;
import com.example.administrator.ydxcfwpt.Adapter.TiXianAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/11.
 */

public class MassageListActivity extends BaseActivity {
    @BindView(R.id.iv_msglist_back)
    ImageView ivMsglistBack;
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.btn_msglist_more)
    Button btnMsglistMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_tixianmingxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tixianmingxi:
                finish();
                break;
        }
    }
}
