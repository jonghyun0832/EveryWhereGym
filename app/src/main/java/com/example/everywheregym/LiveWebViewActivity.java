package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Arrays;

public class LiveWebViewActivity extends AppCompatActivity {

    private WebView webView;

    private String room_url;
    private String user_id;
    private String host_id;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("웹뷰", "onCreate: ");
        setContentView(R.layout.activity_live_web_view);

        Intent getIntent = getIntent();
        room_url = getIntent.getStringExtra("room_id");
        host_id = getIntent.getStringExtra("host_id");

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
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
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(LiveWebViewActivity.this)
                        .setTitle("라이브 종료 안내")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                        finish();
                                        //나가서 ? 아니면 여기서 바로 라이브 평가 ㄱㄱㄱ
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

                new AlertDialog.Builder(LiveWebViewActivity.this)
                        .setTitle("라이브 나가기")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                        finish();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
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
        String enterUrl = "https://70b2-1-227-215-212.ngrok.io/" + room_url;
        //String enterUrl = "https://7c77-180-69-18-217.ngrok.io/123";
        webView.loadUrl(enterUrl);
        //webView.loadUrl("https://www.google.com/");
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            System.out.println("1번");
        } else {
            if(host_id.equals(user_id)){
                //여기서 하면될듯 라이브 삭제 ㄱㄱ?
            }
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