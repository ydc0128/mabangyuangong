package com.example.administrator.ydxcfwpt.Utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.ydxcfwpt.R;

/**
 * Created by Administrator on 2017/12/19.
 */

public class PsdVisibleChangeListener implements View.OnClickListener  {
    private ImageView imageView;
    private EditText editText;

    public PsdVisibleChangeListener(ImageView imageButton, EditText editText) {
        this.editText = editText;
        this.imageView = imageView;
    }

    @Override
    public void onClick(View v) {
        if (imageView == null || editText == null) return;

        if (imageView.isSelected()) {
            imageView.setSelected(false);
            imageView.setBackgroundResource(R.drawable.buxianshi);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        } else {
            imageView.setSelected(true);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        }
    }
}
