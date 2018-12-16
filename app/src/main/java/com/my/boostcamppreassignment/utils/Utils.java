package com.my.boostcamppreassignment.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
