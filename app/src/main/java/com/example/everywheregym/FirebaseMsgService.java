package com.example.everywheregym;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMsgService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        /* DB서버로 새토큰을 업데이트시킬수 있는 부분 */
        Log.d("fcm", "onNewToken: " + s);

        //새로운 토큰 저장
//        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<LiveData> call2 = apiInterface2.sendDeleteAlarm(trainer_id,live_id,message);
//        call2.enqueue(new Callback<LiveData>() {
//            @Override
//            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
//                if (response2.isSuccessful() && response2.body() != null){
//                    if(response2.body().isSuccess()){
//                        AlertDialog.Builder ad3 = new AlertDialog.Builder(context);
//                        ad3.setTitle("전송 완료");
//                        ad3.setMessage("알림 신청한 회원들에게 삭제 알림을 보냈습니다");
//                        ad3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//
//                        });
//                        AlertDialog alertDialog3 = ad3.create();
//                        alertDialog3.show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LiveData> call2, Throwable t) {
//                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    /* 메세지를 새롭게 받을때 */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("테스트", "From: " + remoteMessage.getFrom());

        /* 새메세지를 알림기능을 적용하는 부분 */

//        if (remoteMessage.getNotification() != null) {
//            Log.d("테스트", "From: " + remoteMessage.getFrom());
//            Log.d("테스트", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            String messageBody = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
//        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel("default") == null) {
                NotificationChannel channel = new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), "default");
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);





    }



}