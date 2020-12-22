package com.marcellinus.projectmovie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.marcellinus.projectmovie.R;

import java.util.Objects;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(R.color.colorTransparent);
        setCanceledOnTouchOutside(false);
    }
}
