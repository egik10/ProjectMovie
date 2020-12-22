package com.marcellinus.projectmovie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.marcellinus.projectmovie.R;

import java.util.Objects;

import androidx.annotation.NonNull;

public class NoInternetDialog extends Dialog {

    public NoInternetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_internet);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(R.color.colorTransparent);
        setCanceledOnTouchOutside(false);

        Button btnTryAgain = findViewById(R.id.btn_nic_tryagain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
