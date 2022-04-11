package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveWebViewActivity extends AppCompatActivity {
    private TextView tv_area;

    private ConstraintLayout conView;
    private Boolean isView = false;

    private BackPressEditText et_test;
    private TextView tv_chat_area;

    private SeekBar sb_mic;
    private SeekBar sb_volume;
    private final int max = 2;
    private final float min = 0;
    private final float step = (float)0.1;
    private float mic;
    private float vol;
    private int voll;

    private AudioManager audioManager;

    private WebView webView;

    private String room_url;
    private String user_id;
    private String host_id;
    private String live_id;
    private String live_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("웹뷰", "onCreate: ");
        setContentView(R.layout.activity_live_web_view);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.

                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        Intent getIntent = getIntent();
        room_url = getIntent.getStringExtra("room_id");
        host_id = getIntent.getStringExtra("host_id");
        live_id = getIntent.getStringExtra("live_id");
        live_title = getIntent.getStringExtra("live_title");

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

        tv_area = findViewById(R.id.tv_area);
        tv_chat_area = findViewById(R.id.tv_chat_area);

        webView = findViewById(R.id.webview);
        conView = findViewById(R.id.conView);

        et_test = (BackPressEditText)findViewById(R.id.editTexttest);
        et_test.setVisibility(View.INVISIBLE);

        et_test.setOnBackPressListener(onBackPressListener);

        tv_chat_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_test.setVisibility(View.VISIBLE);
                et_test.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        et_test.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    Log.d("되는중인가?", "onKey: ");
                    String message1 = et_test.getText().toString();
                    Log.d("되는중인가?", "onKey: " + message1);
                    webView.loadUrl("javascript:getInput('" + message1 + "')");
                    InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
//                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    et_test.setText(null);
                    et_test.setVisibility(View.INVISIBLE);
                    return true;
                }
                return false;
            }
        });

        et_test.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    Log.d("체크크크크", "onFocusChange: 123123123123");

                    webView.loadUrl("javascript:loadText()");
                    String text = et_test.getText().toString();
                    webView.loadUrl("javascript:saveText('" + text + "')");

                    et_test.setVisibility(View.INVISIBLE);
                    InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

//        et_test.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent event) {
//                if(i == KeyEvent.KEYCODE_ENTER){
//                    Log.d("되는중인가?", "onKey: ");
//                    String message = et_test.getText().toString();
//                    webView.loadUrl("javascript:getInput(" + message + ")");
//                    return true;
//                }
//                return false;
//            }
//        });

        Window window = getWindow();



        AndroidBridge ab =new AndroidBridge(webView,LiveWebViewActivity.this, tv_area, window, et_test, tv_chat_area);
        webView.addJavascriptInterface(ab,"Android");

        sb_mic = findViewById(R.id.sb_web_mic);
        sb_volume = findViewById(R.id.sb_web_volume);

        sb_mic.setProgress(10);
        sb_volume.setProgress(1);

        setSeekBarMax(sb_mic,max);
        sb_mic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("t22", "onProgressChanged: ");
                setSeekBarChange(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("t22", "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("t22", "onStopTrackingTouch: ");
                webView.loadUrl("javascript:handleMicVol(" + mic + ")");
            }
        });

        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        System.out.println("최대값");
        System.out.println(maxVol);
        sb_volume.setMax(maxVol);

        //setSeekBarMax(sb_volume,1);
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i);
                voll = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(voll != 0){
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,voll,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else {
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,AudioManager.ADJUST_MUTE,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                //webView.loadUrl("javascript:handleVol(" + vol + ")");
            }
        });

        conView.setVisibility(View.INVISIBLE);

//        AndroidBridge ab = new AndroidBridge(LiveWebViewActivity.this);
//        webView.addJavascriptInterface(ab,"Android");

        tv_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isView){
                    tv_area.setVisibility(View.GONE);
                    conView.setVisibility(View.VISIBLE);
                    isView = true;
                }
            }
        });

        conView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isView){
                    conView.setVisibility(View.INVISIBLE);
                    tv_area.setVisibility(View.VISIBLE);
                    isView = false;
                }
            }
        });

//        webView.setOnTouchListener(new OnClickWithOnTouchListener(this, new OnClickWithOnTouchListener.OnClickListener() {
//            @Override
//            public void onClick() {
//                if(isView){
//                    conView.setVisibility(View.INVISIBLE);
//                    isView = false;
//                } else {
//                    conView.setVisibility(View.VISIBLE);
//                    isView = true;
//                }
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
            Log.d("뭐가문제냐", "123123123들어왔슴");
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
                        .setTitle("안내")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                        finishLive(live_id);
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
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                        finishLive(live_id);
                                        //finish();
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
        String enterUrl = "https://8687-203-234-103-157.ngrok.io/" + room_url;

        webView.loadUrl(enterUrl);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            System.out.println("1번");
        } else {
            //뒤로가기로는 못나가게 막음
            //super.onBackPressed();
            System.out.println("2번");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();

    }

    private void setSeekBarMax(SeekBar sb, int max_value) {
        sb.setMax((int)((max_value-min) / step));
    }

    private void setSeekBarChange(int progress) {
        mic = min + (progress * step);
    }

    private void setSeekBarChangeVolume(int progress) {
        vol = min + (progress * step);
    }

    private void finishLive(String live_id){
        if(host_id.equals(user_id)){
            ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
            Call<LiveData> call2 = apiInterface2.finishLive(live_id);
            call2.enqueue(new Callback<LiveData>() {
                @Override
                public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                    if (response2.isSuccessful() && response2.body() != null){
                        if(response2.body().isSuccess()){
                            AlertDialog.Builder ad3 = new AlertDialog.Builder(LiveWebViewActivity.this);
                            ad3.setTitle("알림");
                            ad3.setMessage("라이브가 종료되었습니다.");
                            ad3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reviewLive("0");
                                    //finish();
                                }
                            });
                            AlertDialog alertDialog3 = ad3.create();
                            alertDialog3.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LiveData> call2, Throwable t) {
                    Toast.makeText(LiveWebViewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //여긴 회원들이 나가기 했을떄임
            //나갈경우에는 현재인원수에서 1개씩 뺴주면 됨
            leftLive(live_id);
        }

    }

    private void leftLive(String live_id){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.leftLive(live_id,user_id);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        reviewLive(user_id);
                        //finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(LiveWebViewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reviewLive(String user){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.reviewLive(live_id, user);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        //평가창 띄우기???
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(LiveWebViewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void didBackPressOnEditText()
    {
        et_test.setVisibility(View.INVISIBLE);
        webView.loadUrl("javascript:loadText()");
        String text = et_test.getText().toString();
        webView.loadUrl("javascript:saveText('" + text + "')");
        Log.d("체크크크크", "didBackPressOnEditText:dddddddddddddd");
    }

    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener()
    {
        @Override
        public void onBackPress()
        {
            didBackPressOnEditText();
        }
    };


}