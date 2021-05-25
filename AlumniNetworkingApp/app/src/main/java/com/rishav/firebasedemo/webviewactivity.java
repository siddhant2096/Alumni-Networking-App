package com.rishav.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class webviewactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webviewactivity);
        WebView mywebview = (WebView) findViewById(R.id.webView);
        // mywebview.loadUrl("https://www.javatpoint.com/");

        /*String data = "<html><body><h1>Hello, Javatpoint!</h1></body></html>";
        mywebview.loadData(data, "text/html", "UTF-8"); */

        mywebview.loadUrl("http://dtu.ac.in/");
    }
}