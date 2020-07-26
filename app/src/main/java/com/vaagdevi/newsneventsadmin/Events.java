package com.vaagdevi.newsneventsadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Events extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        refreshLayout = findViewById(R.id.refresh_events);

        refreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1500);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Events.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }, 1500);
            }
        });

        Toast.makeText(Events.this,"Loading Events",Toast.LENGTH_SHORT).show();
        WebView eventsview =(WebView) findViewById(R.id.webview_events);
        eventsview.getSettings().setJavaScriptEnabled(true);
        eventsview.setWebViewClient(new WebViewClient());
        eventsview.loadUrl("https://www.blogger.com/blogger.g?blogID=6758589450801578091&useLegacyBlogger=true#editor/target=post;postID=840840040484975560;onPublishedMenu=posts;onClosedMenu=posts;postNum=0;src=link");

    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            Toast.makeText(Events.this, "No Internet Connection!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
