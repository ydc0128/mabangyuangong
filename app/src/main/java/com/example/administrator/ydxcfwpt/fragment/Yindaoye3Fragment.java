package com.example.administrator.ydxcfwpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.ydxcfwpt.Activity.MainActivity;
import com.example.administrator.ydxcfwpt.R;


/**
 * Created by Administrator on 2017/10/17.
 */

public class Yindaoye3Fragment extends Fragment {

    private Button start;

    public Yindaoye3Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yindaoye3, container, false);
        start = (Button) view.findViewById(R.id.btn_yindaoye3_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
