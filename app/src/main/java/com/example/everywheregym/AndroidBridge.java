package com.example.everywheregym;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CpuUsageInfo;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AndroidBridge {

    private String TAG = "AndroidBridge";
    // final public Handler handler = new Handler();

    private Context mcontext;
    private WebView mwebview;
    private TextView mtv;
    private Window mwindow;
    private EditText meditText;
    private TextView marea;

    private Handler mHandler;

    public AndroidBridge(WebView webview, Context context, TextView tv, Window window, EditText et, TextView tv_area){
        this.mcontext = context;
        this.mwebview = webview;
        this.mtv = tv;
        this.mwindow = window;
        this.meditText = et;
        this.marea = tv_area;

        mHandler = new Handler();

    }

    @JavascriptInterface
    public void call_log(boolean chatting){
        Log.d(TAG, "call_log: " + chatting);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    if(chatting){
                        mtv.setVisibility(View.INVISIBLE);
                        marea.setVisibility(View.VISIBLE);
                    } else {
                        mtv.setVisibility(View.VISIBLE);
                        marea.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void chat_focus(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    meditText.setVisibility(View.VISIBLE);
                    meditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @JavascriptInterface
    public void send_alarm(String live_id){
        Log.d(TAG, "send_alarm: ");
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.sendOpenAlarm(live_id);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        AlertDialog.Builder ad3 = new AlertDialog.Builder(mcontext);
                        ad3.setTitle("전송 완료");
                        ad3.setMessage("알림 신청한 회원들에게 라이브 시작 알림을 보냈습니다.");
                        ad3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog3 = ad3.create();
                        alertDialog3.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(mcontext, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @JavascriptInterface
//    public void chat_btn(){
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    meditText.setVisibility(View.VISIBLE);
//                    meditText.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


}
