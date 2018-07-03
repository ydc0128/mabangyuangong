package com.example.administrator.ydxcfwpt.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 */

public class DialogListViewAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;

    public DialogListViewAdapter(Context context,List<String> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_listview_dialog_text,null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_listview_dialog_text);
        textView.setText(list.get(position));
        return convertView;
    }
}
