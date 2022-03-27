package com.example.everywheregym;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AndroidBridge {

    private String TAG = "AndroidBridge";
    // final public Handler handler = new Handler();

    private Context mcontext;

    public AndroidBridge(Context context){
        mcontext = context;

    }

    @JavascriptInterface
    public void call_log(){
        Log.d(TAG, "call_log: ");
    }


}
