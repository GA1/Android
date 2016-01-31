package com.ga1.ronquito;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

class MicSensitivityButton extends Button {

    private final int changeBy;
    private Context ctx;

    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            RonQuitoParams.SNORING_ENERGY += changeBy;
            CharSequence text = "Snoring sensitivity level is " + RonQuitoParams.SNORING_ENERGY + " dB";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };

    public MicSensitivityButton(Context ctx, int changeBy, String text) {
        super(ctx);
        setText(text);
        this.changeBy = changeBy;
        this.ctx = ctx;
        setOnClickListener(clicker);
    }
}