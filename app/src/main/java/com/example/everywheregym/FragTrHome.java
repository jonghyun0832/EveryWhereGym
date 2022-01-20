package com.example.everywheregym;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragTrHome extends Fragment {

    TextView tv_top; //맨위 텍스트
    Button btn_make_live; //라이브 일정 추가 버튼
    Button btn_upload_vod; //영상 업로드 버튼
    RecyclerView rv_home_live; //오늘의 라이브 리사이클러뷰

    Button btn_home_test;

    private Uri video_uri;
    private String video_path;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer_home,container,false);

        tv_top = view.findViewById(R.id.textview_tr_home_top);
        btn_make_live = view.findViewById(R.id.btn_tr_home_make_live);
        btn_upload_vod = view.findViewById(R.id.btn_tr_home_upload_vod);

        btn_home_test = view.findViewById(R.id.button_home_test);

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TrainerInfo> call = apiInterface.getTrainerInfo(user_id);
        call.enqueue(new Callback<TrainerInfo>() {
            @Override
            public void onResponse(Call<TrainerInfo> call, Response<TrainerInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        String tr_name = response.body().getUser_name();
                        tv_top.setText(tr_name + " 트레이너님 환영합니다!");
                    }
                }
            }

            @Override
            public void onFailure(Call<TrainerInfo> call, Throwable t) {
                Toast.makeText(getContext(), "레트로핏 오류", Toast.LENGTH_SHORT).show();
            }
        });

        btn_make_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MakeLiveActivity.class);
                startActivity(intent);
            }
        });



        ActivityResultLauncher<Intent> activityLauncher_getVideo = registerForActivityResult( //갤러리에서 이미지 받아오기
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Log.d("TAG", "gallery_pick ");
                            //Intent galleryIntent = result.getData();
                            if (result.getData() != null) {
                                video_uri = result.getData().getData(); //얠 보내서 동영상 재생
                                Log.d("VIDEO", "화면에 띄울 파일의 uri: " + video_uri);
                                video_path = getRealPathFromURI2(getContext(), video_uri); //파일 만들때 쓸거임
                                Log.d("VIDEO", "절대경로 : " + video_path);

                                SharedPreferences sharedPreferences= getContext().getSharedPreferences("video", MODE_PRIVATE);
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("v_uri",video_uri.toString()); //쓸때는 파싱해서 써야함
                                editor.putString("v_path",video_path);
                                editor.commit();


                                Intent intent = new Intent(getContext(), VideoCheck.class);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(getContext(), "영상 선택 에러발생", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btn_upload_vod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ted();

                Intent vod_intent = new Intent(Intent.ACTION_PICK);
                vod_intent.setType("video/*");
                activityLauncher_getVideo.launch(vod_intent);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("video/*");
//                activityLauncher_getVideo.launch(intent);
            }
        });

        btn_home_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //VodUploadActivity.class삭제해야됨
                Intent intent = new Intent(getContext(), TestActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    public static String getRealPathFromURI2(Context context, Uri uri){

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


    void ted(){
        PermissionListener permissionLitener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.d("permission", "onPermissionDenied: 요청 실패");
            }
        };

        TedPermission.with(getContext()).setPermissionListener(permissionLitener)
                .setDeniedMessage("동영상을 업로드 하기 위해 접근 권한이 필요합니다.")
                .setPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .check();


    }



}