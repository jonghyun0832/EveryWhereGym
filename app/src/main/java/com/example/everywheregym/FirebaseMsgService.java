package com.example.everywheregym;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private static final int NOTIFICATION_ID = 0;


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

        String title = remoteMessage.getNotification().getTitle();
        Log.d("노티", "onMessageReceived: " + title);
        String message = remoteMessage.getNotification().getBody();
        Log.d("노티", "onMessageReceived: " + message);
        String[] split = message.split("/");

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("view",2);

        Intent selectIntent;
        if (title.equals("라이브 방송 시작 알림")){
            Log.d("노티", "onMessageReceived: intent1");
            Intent intent1 = new Intent(this,LiveWebViewActivity.class);
            intent1.putExtra("room_id",split[1]);
            selectIntent = intent1;
        } else {
            Log.d("노티", "onMessageReceived: intent");
            selectIntent = intent;
        }
        //Intent intent1 = Intent(getBaseContext(),)
//        intent.putExtra("live_id",split[1]);
//        intent.putExtra("uploader_id",split[2]);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,selectIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,selectIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel("defaultd") == null) {
                NotificationChannel channel = new NotificationChannel("defaultd", "기본채널", NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    channel.canBubble();
//                }
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(this, "defaultd");
        }else {
            builder = new NotificationCompat.Builder(this);
        }

        //String title = remoteMessage.getNotification().getTitle();
        String body = split[0];
        //String body = remoteMessage.getNotification().getBody();

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                //.setTicker("알림 간단한 설명")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        //builder.addAction(R.drawable.ic_launcher_background,"해당 라이브 알림해제",pendingIntent1);

        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);





    }

}