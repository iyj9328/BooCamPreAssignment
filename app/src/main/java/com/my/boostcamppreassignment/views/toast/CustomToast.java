package com.my.boostcamppreassignment.views.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.my.boostcamppreassignment.R;

public class CustomToast extends Toast {

    Context context;

    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    public void makeToast(String textToShow, int duration){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.toast_layout, null);

        TextView textViewToast = v.findViewById(R.id.textview_toast);
        textViewToast.setText(textToShow);

        show(this, v, duration);
    }

    private void show(Toast toast, View v, int duration){
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(duration); toast.setView(v);
        toast.show();
    }
}
