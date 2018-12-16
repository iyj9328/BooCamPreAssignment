package com.my.boostcamppreassignment.activity;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.my.boostcamppreassignment.R;

import static com.my.boostcamppreassignment.activity.MainActivity.MOVIE_LINK;

public class WebViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //선택된 영화의 URL 받아옴
        String movieLink = getIntent().getStringExtra(MOVIE_LINK);
        Bitmap backButton = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_arrow_back_white_24dp);

        //크롬 서비스 탭 설정
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorTitle));
        builder.setCloseButtonIcon(backButton);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(movieLink));

    }
}
