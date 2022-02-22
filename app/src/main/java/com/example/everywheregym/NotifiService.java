package com.example.everywheregym;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifiService extends Service {
    public NotifiService() {
    }

    private String user_id;
    private String live_id;
    private String uploader;

    @Override
    public IBinder onBind(Intent intent) {
        live_id = intent.getStringExtra("live_id");
        uploader = intent.getStringExtra("uploader_id");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call = apiInterface.deleteLiveAlarm(user_id,live_id,uploader);
        call.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                if (response.isSuccessful() && response.body() != null){
                    AlertDialog.Builder ad = new AlertDialog.Builder(NotifiService.this);
                    ad.setTitle("알림이 해제되었습니다");
                    ad.setMessage("해당 라이브가 시작되도 알림을 받지 않습니다");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopSelf();
                        }

                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();

                }
            }

            @Override
            public void onFailure(Call<LiveData> call, Throwable t) {
                Toast.makeText(NotifiService.this, "통신 오류", Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        });

    }
}