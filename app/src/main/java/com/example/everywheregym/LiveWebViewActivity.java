package com.example.everywheregym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Arrays;

public class LiveWebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("웹뷰", "onCreate: ");
        setContentView(R.layout.activity_live_web_view);

        webView = findViewById(R.id.webview);

//        webView.setOnTouchListener(new OnClickWithOnTouchListener(this, new OnClickWithOnTouchListener.OnClickListener() {
//            @Override
//            public void onClick() {
//                Log.d("웹뷰", "onClick: sdasdasdasdasdasdd");
//            }
//        }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        initWebView();

    }

    public void initWebView(){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                Log.d("웹뷰", "onPageStarted: 시작");
                //프로그래스바용
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("웹뷰", "onPageStarted: 끝");
                //프로그래스바용
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("웹뷰", "onPageStarted: 여긴써지나");
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
//
//                runOnUiThread(new Runnable() {
//                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                    @Override
//                    public void run() {
//                        Log.d("뷰뷰뷰뷰", "onPermissionRequest: 퍼미션하는곳 들어옴" + request.getOrigin().toString());
//                        if (request.getOrigin().toString().equals("https://8103-180-69-18-217.ngrok.io/")) {
//                            Log.d("뷰뷰뷰뷰", "onPermissionRequest: 퍼미션ㄱㄱ");
//                            request.grant(request.getResources());
//                            Log.d("뷰뷰뷰뷰", "onPermissionRequest: 퍼미션 끝");
//                        } else {
//                            request.deny();
//                        }
//                    }
//                });
                Log.d("뷰뷰뷰뷰", "onPermissionRequest: 퍼미션하는곳 들어옴" + Arrays.toString(request.getResources()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String[] PERMISSIONS = {
                            PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                            PermissionRequest.RESOURCE_VIDEO_CAPTURE
                    };
                    request.grant(PERMISSIONS);
                }
            }
        });

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowContentAccess(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setDomStorageEnabled(true);
        ws.setBlockNetworkImage(false);
        ws.setBlockNetworkLoads(false);
        //이거 써서 그냥 바로 상대방 영상 받아옴
        ws.setMediaPlaybackRequiresUserGesture(false);

        ws.setAppCacheEnabled(true);
        ws.setDomStorageEnabled(true);

        Log.d("웹뷰", "웹뷰 세팅");


        //webView.setWebViewClient(new WebViewClient());

        //webView.clearCache(true);
        String enterUrl = "https://8598-180-69-18-217.ngrok.io/";
        webView.loadUrl(enterUrl);
        //webView.loadUrl("https://www.google.com/");
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            System.out.println("1번");
        } else {
            super.onBackPressed();
            System.out.println("2번");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}