package com.example.everywheregym;
import android.view.LayoutInflater;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VodDetailActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ImageView iv_thumbnail;
    private ImageView iv_thumbnail_edit;

    private TextView tv_vod_length;
    private TextView tv_title_limit;

    private EditText et_vod_title;

    private TextView tv_categoty;
    private Spinner sp_category;
    private Spinner sp_difficulty;

    private String selected_category; //카테고리
    private String selected_difficulty; //난이도

    private Bitmap thumbnail;

    private Button btn_upload;

    private ProgressDialog prDialog;

    private String vod_length;

    private String user_id;

    private CheckBox cb_whole;
    private CheckBox cb_abs;
    private CheckBox cb_leg;
    private CheckBox cb_chest;
    private CheckBox cb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_detail);

        iv_back = (ImageView) findViewById(R.id.imageview_video_detail_back);
        iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        iv_thumbnail_edit = (ImageView) findViewById(R.id.iv_thumbnail_edit);

        tv_vod_length = (TextView) findViewById(R.id.textview_video_length);
        tv_title_limit = (TextView) findViewById(R.id.textview_detail_title_limit);

        et_vod_title = (EditText) findViewById(R.id.edittext_vod_detail_title);

        tv_categoty = (TextView) findViewById(R.id.textview_category_select);
        //sp_category = (Spinner) findViewById(R.id.spinner_vod_category);
        sp_difficulty = (Spinner) findViewById(R.id.spinner_vod_difficulty);

        btn_upload = (Button) findViewById(R.id.btn_upload_vod);

        initDialog(); //프로그래스 다이얼로그 세팅

        SharedPreferences sharedPreferences= this.getSharedPreferences("video", Context.MODE_PRIVATE);
        String get_path = sharedPreferences.getString("v_path","");
        String get_uri = sharedPreferences.getString("v_uri", "");

        SharedPreferences sharedPreferences2= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences2.getString("user_id","");

//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(get_path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
//        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap,480,360);
//        iv_thumbnail.setImageBitmap(thumbnail);

        if (!get_path.equals("")){ //영상 썸네일 설정하기 (자동)
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            System.out.println("get_path 들어온거 : " + get_path);

            mediaMetadataRetriever.setDataSource(get_path);

            thumbnail = mediaMetadataRetriever.getFrameAtTime(1000000);
            iv_thumbnail.setImageBitmap(thumbnail);

            String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInmillisec = Long.parseLong(time);
            //vod_length = timeInmillisec; //데이터 저장용
            long duration = timeInmillisec / 1000;
            long hours = duration / 3600;
            long minutes = (duration - hours * 3600) / 60;
            long seconds = duration - (hours * 3600 + minutes * 60);

            if(hours > 0){
                if(minutes < 10){
                    if(seconds < 10){
                        tv_vod_length.setText(hours + ":0" + minutes + ":0"+seconds);
                    } else {
                        tv_vod_length.setText(hours + ":0" + minutes + ":"+seconds);
                    }
                }else {
                    if(seconds < 10){
                        tv_vod_length.setText(hours + ":" + minutes + ":0"+seconds);
                    } else {
                        tv_vod_length.setText(hours + ":" + minutes + ":"+seconds);
                    }
                }
            } else {
                if(seconds < 10){
                    tv_vod_length.setText(minutes + ":0"+seconds);
                } else {
                    tv_vod_length.setText(minutes + ":"+seconds);
                }
            }

        } else { //path가 제대로 저장 안된경우 다시 홈 화면으로 보낸다.
            Toast.makeText(this, "영상을 다시 선택해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        tv_title_limit.setText("0 / 30");


        //뒷 화살표 클릭시 이전으로 돌아가기
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                ad.setTitle("변경사항 삭제");
                ad.setMessage("변경사항이 저장되지 않습니다.");
                ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }

                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });

        iv_thumbnail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                activityLauncher_edit_thumbnail.launch(galleryIntent);
            }
        });

        //tv_vod_length.setText("이거 어캐가져옴");

        et_vod_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = et_vod_title.getText().toString();
                if (input.length() < 30){
                    tv_title_limit.setText(input.length() + " / 30");
                    tv_title_limit.setTextColor(Color.BLACK);
                } else {
                    tv_title_limit.setText("30자 이하로 입력해주세요");
                    tv_title_limit.setTextColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tv_categoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                LayoutInflater inflater = (LayoutInflater) VodDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_category_box, null);
                ad.setView(dialogView);
                ad.setTitle("집중 부위를 선택해주세요\n(다중선택가능)");

                cb_whole = dialogView.findViewById(R.id.checkBox_whole);
                cb_abs = dialogView.findViewById(R.id.checkBox_abs);
                cb_leg = dialogView.findViewById(R.id.checkBox_leg);
                cb_chest = dialogView.findViewById(R.id.checkBox_chest);
                cb_back = dialogView.findViewById(R.id.checkBox_back);

                String[] split = tv_categoty.getText().toString().split(", ");

                if(split.length != 0){ //수정용
                    for (int i = 0; i < split.length; i++){
                        if(split[i].equals("전신"))
                            cb_whole.setChecked(true);
                        if(split[i].equals("복근"))
                            cb_abs.setChecked(true);
                        if(split[i].equals("하체"))
                            cb_leg.setChecked(true);
                        if(split[i].equals("가슴"))
                            cb_chest.setChecked(true);
                        if(split[i].equals("등"))
                            cb_back.setChecked(true);
                    }
                }



                ad.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuffer select = new StringBuffer();
                        if(cb_whole.isChecked()) {
                            select.append("전신, ");
                        }
                        if(cb_abs.isChecked()){
                            select.append("복근, ");
                        }
                        if(cb_leg.isChecked())
                            select.append("하체, ");
                        if(cb_chest.isChecked())
                            select.append("가슴, ");
                        if(cb_back.isChecked())
                            select.append("등, ");

                        if(select.length() != 0){
                            System.out.println(select);
                            String result = select.substring(0,select.length()-2);
                            tv_categoty.setText(result);
                        } else {
                            tv_categoty.setText(null);
                        }

                    }

                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //취소했을때 할게없음
                    }
                });


                AlertDialog alertDialog = ad.create();
                alertDialog.show();

            }
        });


//        String[] vod_category = getResources().getStringArray(R.array.category_array);
        String[] vod_difficulty = getResources().getStringArray(R.array.difficulty_array);

//        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,R.array.category_array, android.R.layout.simple_spinner_item);
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_category.setAdapter(categoryAdapter);
//
//        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_category = vod_category[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                selected_category = vod_category[0];
//            }
//        });

        ArrayAdapter difficultyAdapter = ArrayAdapter.createFromResource(this,R.array.difficulty_array, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_difficulty.setAdapter(difficultyAdapter);

        sp_difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_difficulty = vod_difficulty[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selected_difficulty = vod_difficulty[0];
            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get_uri.equals("")){ //선택 uri가 제대로 shared에 저장 안된경우 다시 메인으로 보냄
                    Toast.makeText(VodDetailActivity.this, "영상을 다시 선택해주세요", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else { // 제대로 들어간 경우 업로드 진행
                    if(tv_categoty.getText().toString().equals("") || et_vod_title.getText().toString().equals("")){
                        AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                        ad.setTitle("알림");
                        ad.setMessage("세부 정보들을 모두 입력해주세요");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }

                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();
                    } else {
                        showpDialog();
                        vod_length = tv_vod_length.getText().toString();
                        selected_category = tv_categoty.getText().toString();
                        //이미지 파일 만들어주기 (캐시에 생성)
                        File upload_file = saveImage(thumbnail);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file);
                        MultipartBody.Part img_file = MultipartBody.Part.createFormData("thumbnail", upload_file.getName(), reqFile);

                        //영상 제목
                        String input_title = et_vod_title.getText().toString();

                        File file = new File(get_path);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);

                        HashMap<String, RequestBody> map = new HashMap<>();

                        String pdname = user_id + "_" + Calendar.getInstance().getTimeInMillis();
                        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),pdname);
                        RequestBody file_length = RequestBody.create(MediaType.parse("text/plain"),vod_length);
                        RequestBody file_user = RequestBody.create(MediaType.parse("text/plain"),user_id);
                        RequestBody file_title = RequestBody.create(MediaType.parse("text/plain"),input_title);
                        RequestBody file_category = RequestBody.create(MediaType.parse("text/plain"),selected_category);
                        RequestBody file_difficulty = RequestBody.create(MediaType.parse("text/plain"),selected_difficulty);
                        //RequestBody file_img = RequestBody.create(MediaType.parse("text/plain"),); 이미지
                        map.put("name",filename);
                        map.put("length",file_length);
                        map.put("userId",file_user);
                        map.put("title",file_title);
                        map.put("category",file_category);
                        map.put("difficulty",file_difficulty);
                        //RequestBody vod = RequestBody.create(MediaType.parse("text/plain"),vod_length)

                        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video_file", file.getName(),requestBody);
                        System.out.println("파일이름 : " + filename);
                        System.out.println("만든파일 : " + vFile);

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<UserInfo> call = apiInterface.uploadVideoData(vFile,map,img_file);
                        call.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(response.body().isSuccess()){
                                        hidepDialog();

                                        AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                                        ad.setTitle("알림");
                                        ad.setMessage("동영상 업로드가 완료되었습니다!");
                                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
                                                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }

                                        });
                                        AlertDialog alertDialog = ad.create();
                                        alertDialog.show();

                                    } else {
                                        hidepDialog();
                                        Toast.makeText(VodDetailActivity.this, "서버단쪽 sql에 문제생김", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    hidepDialog();
                                    Toast.makeText(VodDetailActivity.this, "영상 업로드에 문제생김", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                hidepDialog();
                                Toast.makeText(VodDetailActivity.this, "동영상 업로드 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });




    }

    ActivityResultLauncher<Intent> activityLauncher_edit_thumbnail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("TAG", "gallery_pick ");
                    Intent galleryIntent = result.getData();
                    if(galleryIntent != null){
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),galleryIntent.getData()
                            );
                            iv_thumbnail.setImageBitmap(thumbnail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private File saveImage(Bitmap bitmap){
        String filename = "VIDEO_IMAGE";

        File tmp_file = new File(getApplicationContext().getCacheDir(),filename);
        try{
            tmp_file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,70,bos);
        byte[] bitmapdata = bos.toByteArray();


        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(tmp_file);
        }catch (Exception e){
            e.printStackTrace();
        }try{
            fos.write(bitmapdata);
            //fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tmp_file;
    }



    protected void initDialog(){
        prDialog = new ProgressDialog(this);
        prDialog.setMessage("loading");
        prDialog.setCancelable(true);
    }

    protected void showpDialog(){
        if(!prDialog.isShowing()){
            prDialog.show();
        }
    }

    protected void hidepDialog(){
        if(prDialog.isShowing()){
            prDialog.dismiss();
        }
    }


}