package com.example.everywheregym;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VodUploadActivity extends AppCompatActivity {

    private Button btn_select, btn_upload;
    private VideoView mVideoView;
    private TextView mBufferingTextView;
    private Uri video_uri;
    private String video_path;
    private  Button btn_check;

    public ProgressDialog pDialog;

    private static final String PLAYBACK_TIME = "play_time";

    //millisecond단위임
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_upload);

        btn_select = findViewById(R.id.pickVideo);
        btn_upload = findViewById(R.id.uploadVideo);
        btn_check = findViewById(R.id.button_check);

        mVideoView = findViewById(R.id.videoview);
        mBufferingTextView = findViewById(R.id.buffering_textview);


        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                video_uri = Uri.parse("content://media/external/images/media");
//                Intent intent = new Intent(Intent.ACTION_VIEW,video_uri);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                activityLauncher_video.launch(intent);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(video_uri != null){
                    uploadFile();
                } else {
                    Toast.makeText(VodUploadActivity.this, "비디오를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VodUploadActivity.this,VideoCheck.class);
                startActivity(intent);
            }
        });


        if(savedInstanceState != null){ //중단점 있으면 언젠지 가져오기
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);

        initDialog();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        relesePlayer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME,mVideoView.getCurrentPosition());
    }

    private void initializePlayer(Uri uri){
        //버퍼링 중일 떄 버퍼링 중이라는 메세지 출력
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        if(uri!=null){
            mVideoView.setVideoURI(uri);
        }
        //Onprepared() 이벤트 리스너 (미디어가 준비된 후 작동)
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        //다시 안보이게
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        //이전 위치  복원 (가능하면)
                        if(mCurrentPosition > 0){
                            mVideoView.seekTo(mCurrentPosition);
                        }else {
                            // 비디오 첫프레임 보여줘야하니 1초 스킵
                            mVideoView.seekTo(1);
                        }

                        //플레이 시작
                        mVideoView.start();
                    }
                });

        //onComplete리스너 (미디어 끝나면 발생)
        mVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(VodUploadActivity.this, "동영상이 끝났습니다", Toast.LENGTH_SHORT).show();

                        //다시 처음으로 간다
                        mVideoView.seekTo(0);
                    }
                });
    }

    private void relesePlayer(){
        mVideoView.stopPlayback();
    }



    ActivityResultLauncher<Intent> activityLauncher_video = registerForActivityResult( //갤러리에서 이미지 받아오기
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("TAG", "gallery_pick ");
                        //Intent galleryIntent = result.getData();
                        if (result.getData() != null) {
                            video_uri = result.getData().getData();
                            Log.d("VIDEO", "화면에 띄울 파일의 uri: " + video_uri);
                            video_path = getRealPathFromURI(VodUploadActivity.this, video_uri);
                            Log.d("VIDEO", "절대경로 : " + video_path);

                            initializePlayer(video_uri);

                        }
                    } else {
                        Toast.makeText(VodUploadActivity.this, "영상 선택 에러발생", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    public static String getRealPathFromURI(Context context, Uri uri){

        if(DocumentsContract.isDocumentUri(context,uri)){

            //External storage provider
            if(isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                Log.d("VIDEO", "split전 데이터: " + docId);
                final String[] split = docId.split(":");
                final String type = split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    String SDcardpath = getRemovableSDCardPath(context).split("/Android")[0];
                    return SDcardpath + "/" + split[1];
                }
            }

            //Downloads provider
            else if (isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id)
                );

                return getDataColumn(context, contentUri, null, null);
            }

            //MediaProvider
            else if (isMediaDocumnet(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            //remote address를 리턴한다
            if(isGooglePhotosUri(uri)){
                return uri.getLastPathSegment();
            }
            return getDataColumn(context,uri,null,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }


    public static String getRemovableSDCardPath(Context context){
        File[] storage = ContextCompat.getExternalFilesDirs(context,null);
        Log.d("VIDEO", "getRemovableSDCardPath: " + storage);
        if(storage.length > 1 && storage[0] != null && storage[1] != null){
            return storage[1].toString();
        } else {
            return "";
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try{
          cursor = context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
          if(cursor!=null && cursor.moveToFirst()){
              final int index = cursor.getColumnIndexOrThrow(column);
              return cursor.getString(index);
          }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocumnet(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    protected void initDialog(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("loading");
        pDialog.setCancelable(true);
    }


    private void uploadFile(){
        if(video_uri == null || video_uri.equals("")){
            Toast.makeText(this, "비디오를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else {
            showpDialog();

            //Map은 multipart에서 okhttp3.RequestBody에 사용됨
            HashMap<String, RequestBody> map = new HashMap<>();
            File file = new File(video_path);

            System.out.println("파일의 경로는 :"+file);

            //미디어 파일 파싱
            //RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);

            //map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            //map.put("file\"; filename=\""+file.getName() + "\"", requestBody);

            String pdname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),pdname);
            RequestBody filetest = RequestBody.create(MediaType.parse("text/plain"),"123123");
            map.put("test1",filename);
            map.put("test2",filetest);

            MultipartBody.Part vFile = MultipartBody.Part.createFormData("video_file", file.getName(),requestBody);

            //System.out.println("맵넘어가는것좀 보자 : "+ map);
            System.out.println("파일이름 : " + filename);
            System.out.println("만든파일 : " + vFile);
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<UserInfo> call = apiInterface.uploadVideo(vFile,map);
            //Call<UserInfo> call = apiInterface.uploadVideo(vFile);
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (response.isSuccessful() && response.body() != null){
                        if(response.body().isSuccess()){
                            hidepDialog();
                        } else {
                            hidepDialog();
                            Toast.makeText(VodUploadActivity.this, "서버단쪽 sql에 문제생김", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hidepDialog();
                        Toast.makeText(VodUploadActivity.this, "영상 업로드에 문제생김", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    hidepDialog();
                    Toast.makeText(VodUploadActivity.this, "동영상 업로드 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    protected void showpDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    protected void hidepDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

}