package com.example.everywheregym;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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

import com.bumptech.glide.Glide;

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

    private Spinner sp_difficulty;

    private String selected_category; //????????????
    private String selected_difficulty; //?????????

    private Bitmap thumbnail;

    private Button btn_upload;

    private ProgressDialog prDialog;

    private String vod_length;
    private long totaltime;

    private String user_id;

    private CheckBox cb_whole;
    private CheckBox cb_abs;
    private CheckBox cb_leg;
    private CheckBox cb_chest;
    private CheckBox cb_back;


    //??????, ????????? ??????
    private TextView tv_vod_explain;
    private TextView tv_vod_material;
    private TextView tv_vod_calorie;
    //????????????
    private String vod_explain = "";
    private String vod_material = "";
    private String vod_calorie = "";

    //???????????? ??? ????????? ??????
    private boolean from_list = false;
    private boolean isEdit = false;
    private String getted_vod_id;
    private String getted_vod_thumbnail;
    private String getted_vod_time;
    private String getted_vod_title;
    private String getted_vod_category;
    private String getted_vod_difficulty;
    private String getted_vod_explain;
    private String getted_vod_material;
    private String getted_vod_calorie;
    private String previous_thumbnail;

    private int spinner_default = 0;

    private boolean ischange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_detail2);

        iv_back = (ImageView) findViewById(R.id.imageview_video_detail_back);
        iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        iv_thumbnail_edit = (ImageView) findViewById(R.id.iv_thumbnail_edit);

        tv_vod_length = (TextView) findViewById(R.id.textview_video_length);
        tv_title_limit = (TextView) findViewById(R.id.textview_detail_title_limit);

        et_vod_title = (EditText) findViewById(R.id.edittext_vod_detail_title);

        tv_categoty = (TextView) findViewById(R.id.textview_category_select);

        sp_difficulty = (Spinner) findViewById(R.id.spinner_vod_difficulty);

        btn_upload = (Button) findViewById(R.id.btn_upload_vod);

        //??????, ?????????, ??????????????? ??????
        tv_vod_explain = (TextView) findViewById(R.id.textveiw_vod_explain);
        tv_vod_material = (TextView) findViewById(R.id.textview_vod_material);
        tv_vod_calorie = (TextView) findViewById(R.id.textveiw_vod_calorie);

        initDialog(); //??????????????? ??????????????? ??????

        ///////////////////////////////////////////////////////////////////////////////////////////
        //???????????? ????????????
        try{
            Intent intentEdit = getIntent();
            from_list = intentEdit.getBooleanExtra("fromList",false);
            isEdit = intentEdit.getBooleanExtra("isEdit",false);
            getted_vod_id = intentEdit.getStringExtra("vod_id");
            getted_vod_thumbnail = intentEdit.getStringExtra("vod_thumbnail_path");
            getted_vod_time = intentEdit.getStringExtra("vod_time");
            getted_vod_title = intentEdit.getStringExtra("vod_title");
            getted_vod_category = intentEdit.getStringExtra("vod_category");
            getted_vod_difficulty = intentEdit.getStringExtra("vod_difficulty");
            getted_vod_explain = intentEdit.getStringExtra("vod_explain");
            getted_vod_material = intentEdit.getStringExtra("vod_material");
            getted_vod_calorie = intentEdit.getStringExtra("vod_calorie");

        }catch (Exception e){
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences2= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences2.getString("user_id","");

        if(isEdit){
            //????????? ???????????? ????????????
            String getted_thumbnail_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/image/" + getted_vod_thumbnail;
            Glide.with(VodDetailActivity.this).load(getted_thumbnail_url).centerCrop().into(iv_thumbnail);
            previous_thumbnail = getted_vod_thumbnail;

            //?????? ???????????? ????????????
            tv_vod_length.setText(getted_vod_time);

            //?????? ???????????? ???????????? (????????? ?????? ??????)
            et_vod_title.setText(getted_vod_title);

            tv_title_limit.setText(et_vod_title.getText().toString().length() + " / 30");
            //?????? ?????? ???????????? ????????????
            tv_categoty.setText(getted_vod_category);
            //????????? ???????????? ???????????? (?????????)
            if (getted_vod_difficulty.equals("??????")){
                spinner_default = 0;

            }else if(getted_vod_difficulty.equals("??????")){
                spinner_default = 1;
            } else {
                spinner_default = 2;
            }

            vod_explain = getted_vod_explain;
            vod_material = getted_vod_material;
            vod_calorie = getted_vod_calorie;

            String[] split = getted_vod_explain.split("\\n");
            if(split.length > 1){
                tv_vod_explain.setText(split[0] + ".....");
            } else {
                tv_vod_explain.setText(split[0]);
            }

            tv_vod_material.setText(vod_material);

            String spend_calorie = vod_calorie + " Kcal";
            tv_vod_calorie.setText(spend_calorie);

            //sp_difficulty.setSelection(spinner_default);
            //????????? ????????? -> ????????? ??????????????? ????????? ?????????????????? ???????????? ???????????? ?????????
            //????????? ??????????????? ???????????? ????????? ???????????? ???????????? ????????? ????????? ??????????????? (???????????? ?????? ????????????)
            ischange = false;
            btn_upload.setText("????????? ????????????");

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tv_categoty.getText().toString().equals("") ||
                            et_vod_title.getText().toString().equals("") ||
                            vod_explain.equals("") ||
                            vod_calorie.equals("")){
                        AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                        ad.setTitle("??????");
                        ad.setMessage("?????? ?????? ???????????? ?????? ??????????????????\n(???????????? : ??????,????????????,???????????????,??????)");
                        ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                        //????????? ?????? ??????????????? (????????? ??????)
                        File upload_file;
                        MultipartBody.Part img_file;
                        RequestBody file_previous_thumbnail;
                        if (ischange){
                            upload_file = saveImage(thumbnail);
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file);
                            img_file = MultipartBody.Part.createFormData("thumbnail", upload_file.getName(), reqFile);

                            file_previous_thumbnail = RequestBody.create(MediaType.parse("text/plain"),previous_thumbnail);
                        } else {
                            upload_file = emptyFile();
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file);
                            img_file = MultipartBody.Part.createFormData("thumbnail", upload_file.getName(), reqFile);

                            file_previous_thumbnail = RequestBody.create(MediaType.parse("text/plain"),"");
                        }

                        //?????? ??????
                        String input_title = et_vod_title.getText().toString();

                        HashMap<String, RequestBody> map = new HashMap<>();

                        System.out.println(vod_material);
                        System.out.println(vod_calorie);

                        if(vod_material == null){
                            vod_material = "";
                        }

                        RequestBody file_user = RequestBody.create(MediaType.parse("text/plain"),user_id);
                        RequestBody file_title = RequestBody.create(MediaType.parse("text/plain"),input_title);
                        RequestBody file_category = RequestBody.create(MediaType.parse("text/plain"),selected_category);
                        RequestBody file_difficulty = RequestBody.create(MediaType.parse("text/plain"),selected_difficulty);
                        RequestBody file_vod_id = RequestBody.create(MediaType.parse("text/plain"),getted_vod_id);
                        RequestBody file_vod_explain = RequestBody.create(MediaType.parse("text/plain"),vod_explain);
                        RequestBody file_vod_material = RequestBody.create(MediaType.parse("text/plain"),vod_material);
                        RequestBody file_vod_calorie = RequestBody.create(MediaType.parse("text/plain"),vod_calorie);


                        map.put("userId",file_user);
                        map.put("title",file_title);
                        map.put("category",file_category);
                        map.put("difficulty",file_difficulty);
                        map.put("vod_id",file_vod_id);
                        map.put("previous",file_previous_thumbnail);
                        map.put("explain",file_vod_explain);
                        map.put("material",file_vod_material);
                        map.put("calorie",file_vod_calorie);


                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodData> call = apiInterface.editVideoData(map,img_file);
                        call.enqueue(new Callback<VodData>() {
                            @Override
                            public void onResponse(Call<VodData> call, Response<VodData> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(response.body().isSuccess()){
                                        hidepDialog();

                                        AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                                        ad.setTitle("??????");
                                        ad.setMessage("????????? ????????? ?????????????????????!");
                                        ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (from_list){
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
                                                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }

                                        });
                                        AlertDialog alertDialog = ad.create();
                                        alertDialog.show();

                                    } else {
                                        hidepDialog();
                                        Toast.makeText(VodDetailActivity.this, "???????????? sql??? ????????????", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    hidepDialog();
                                    Toast.makeText(VodDetailActivity.this, "?????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<VodData> call, Throwable t) {
                                hidepDialog();
                                Toast.makeText(VodDetailActivity.this, "????????? ????????? ??????", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        } else {


            SharedPreferences sharedPreferences= this.getSharedPreferences("video", Context.MODE_PRIVATE);
            String get_path = sharedPreferences.getString("v_path","");
            String get_uri = sharedPreferences.getString("v_uri", "");


            if (!get_path.equals("")){ //?????? ????????? ???????????? (??????)
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                System.out.println("get_path ???????????? : " + get_path);

                mediaMetadataRetriever.setDataSource(get_path);

                thumbnail = mediaMetadataRetriever.getFrameAtTime(1000000);
                iv_thumbnail.setImageBitmap(thumbnail);
                //?????? : ????????? ????????????

                String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInmillisec = Long.parseLong(time);
                //vod_length = timeInmillisec; //????????? ?????????
                long duration = timeInmillisec / 1000;
                long hours = duration / 3600;
                long minutes = (duration - hours * 3600) / 60;
                long seconds = duration - (hours * 3600 + minutes * 60);
                totaltime = hours * 3600 + minutes * 60 + seconds;

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

            } else { //path??? ????????? ?????? ???????????? ?????? ??? ???????????? ?????????.
                Toast.makeText(this, "????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }




            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (get_uri.equals("")){ //?????? uri??? ????????? shared??? ?????? ???????????? ?????? ???????????? ??????
                        Toast.makeText(VodDetailActivity.this, "????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VodDetailActivity.this, TrainerHomeActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else { // ????????? ????????? ?????? ????????? ??????
                        if(tv_categoty.getText().toString().equals("") ||
                                et_vod_title.getText().toString().equals("") ||
                                vod_explain.equals("") ||
                                vod_calorie.equals("")
                                ){
                            AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                            ad.setTitle("??????");
                            ad.setMessage("?????? ?????? ???????????? ?????? ??????????????????\n(???????????? : ??????,????????????,???????????????,??????)");
                            ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                            //????????? ?????? ??????????????? (????????? ??????)
                            File upload_file = saveImage(thumbnail);
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file);
                            MultipartBody.Part img_file = MultipartBody.Part.createFormData("thumbnail", upload_file.getName(), reqFile);

                            //?????? ??????
                            String input_title = et_vod_title.getText().toString();

                            File file = new File(get_path);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);

                            HashMap<String, RequestBody> map = new HashMap<>();


                            //???????????? ????????? ?????? ?????? ""??? ????????? ???????????? ??????
//                            if(vod_explain == null){
//                                vod_explain = "";
//                            }
//
//                            if(vod_material == null){
//                                vod_material = "";
//                            }
//
//                            if(vod_calorie == null){
//                                vod_calorie = "";
//                            }

                            if(vod_material == null){
                                vod_material = "";
                            }

                            String pdname = user_id + "_" + Calendar.getInstance().getTimeInMillis();
                            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"),pdname);
                            RequestBody file_length = RequestBody.create(MediaType.parse("text/plain"),vod_length);
                            RequestBody file_time = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totaltime));
                            RequestBody file_user = RequestBody.create(MediaType.parse("text/plain"),user_id);
                            RequestBody file_title = RequestBody.create(MediaType.parse("text/plain"),input_title);
                            RequestBody file_category = RequestBody.create(MediaType.parse("text/plain"),selected_category);
                            RequestBody file_difficulty = RequestBody.create(MediaType.parse("text/plain"),selected_difficulty);
                            RequestBody file_explain = RequestBody.create(MediaType.parse("text/plain"),vod_explain);
                            RequestBody file_material = RequestBody.create(MediaType.parse("text/plain"),vod_material);
                            RequestBody file_calorie = RequestBody.create(MediaType.parse("text/plain"),vod_calorie);
                            //RequestBody file_img = RequestBody.create(MediaType.parse("text/plain"),); ?????????
                            map.put("name",filename);
                            map.put("length",file_length);
                            map.put("time",file_time);
                            map.put("userId",file_user);
                            map.put("title",file_title);
                            map.put("category",file_category);
                            map.put("difficulty",file_difficulty);
                            map.put("explain",file_explain);
                            map.put("material",file_material);
                            map.put("calorie",file_calorie);
                            //RequestBody vod = RequestBody.create(MediaType.parse("text/plain"),vod_length)

                            MultipartBody.Part vFile = MultipartBody.Part.createFormData("video_file", file.getName(),requestBody);
                            System.out.println("???????????? : " + filename);
                            System.out.println("???????????? : " + vFile);

                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<UserInfo> call = apiInterface.uploadVideoData(vFile,map,img_file);
                            call.enqueue(new Callback<UserInfo>() {
                                @Override
                                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            hidepDialog();

                                            AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                                            ad.setTitle("??????");
                                            ad.setMessage("????????? ???????????? ?????????????????????!");
                                            ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                                            Toast.makeText(VodDetailActivity.this, "???????????? sql??? ????????????", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        hidepDialog();
                                        Toast.makeText(VodDetailActivity.this, "?????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserInfo> call, Throwable t) {
                                    hidepDialog();
                                    Toast.makeText(VodDetailActivity.this, "????????? ????????? ??????", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });

            tv_title_limit.setText("0 / 30");

        }



        ///////////////////////////////////////////////////////////////////////////////////////////



        //??? ????????? ????????? ???????????? ????????????
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                ad.setTitle("???????????? ??????");
                ad.setMessage("??????????????? ???????????? ????????????.");
                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }

                });
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                    tv_title_limit.setText("30??? ????????? ??????????????????");
                    tv_title_limit.setTextColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_vod_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????? ??????????????? ????????? ????????? ?????? ???????????? ????????? ???????????? text??? ???????????? ????????? ????????? ????????????.
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                LayoutInflater inflater = (LayoutInflater) VodDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_explain, null);
                ad.setView(dialogView);
                ad.setTitle("????????? ?????? ??????");

                EditText et_explain = dialogView.findViewById(R.id.et_dialog_vod_explain);

                et_explain.setText(vod_explain);

                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vod_explain = et_explain.getText().toString();
                        String[] split = vod_explain.split("\\n");
                        if (split.length > 1){
                            tv_vod_explain.setText(split[0] + ".....");
                        } else {
                            tv_vod_explain.setText(split[0]);
                        }

                    }
                });

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();

            }
        });


        tv_categoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                LayoutInflater inflater = (LayoutInflater) VodDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_category_box, null);
                ad.setView(dialogView);
                ad.setTitle("?????? ????????? ??????????????????\n(??????????????????)");

                cb_whole = dialogView.findViewById(R.id.checkBox_whole);
                cb_abs = dialogView.findViewById(R.id.checkBox_abs);
                cb_leg = dialogView.findViewById(R.id.checkBox_leg);
                cb_chest = dialogView.findViewById(R.id.checkBox_chest);
                cb_back = dialogView.findViewById(R.id.checkBox_back);

                String[] split = tv_categoty.getText().toString().split(", ");

                if(split.length != 0){ //?????????
                    for (int i = 0; i < split.length; i++){
                        if(split[i].equals("??????"))
                            cb_whole.setChecked(true);
                        if(split[i].equals("??????"))
                            cb_abs.setChecked(true);
                        if(split[i].equals("??????"))
                            cb_leg.setChecked(true);
                        if(split[i].equals("??????"))
                            cb_chest.setChecked(true);
                        if(split[i].equals("???"))
                            cb_back.setChecked(true);
                    }
                }



                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuffer select = new StringBuffer();
                        if(cb_whole.isChecked()) {
                            select.append("??????, ");
                        }
                        if(cb_abs.isChecked()){
                            select.append("??????, ");
                        }
                        if(cb_leg.isChecked())
                            select.append("??????, ");
                        if(cb_chest.isChecked())
                            select.append("??????, ");
                        if(cb_back.isChecked())
                            select.append("???, ");

                        if(select.length() != 0){
                            System.out.println(select);
                            String result = select.substring(0,select.length()-2);
                            tv_categoty.setText(result);
                        } else {
                            tv_categoty.setText("");
                        }

                    }

                });

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //??????????????? ????????????
                    }
                });


                AlertDialog alertDialog = ad.create();
                alertDialog.show();

            }
        });


        String[] vod_difficulty = getResources().getStringArray(R.array.difficulty_array);



        ArrayAdapter difficultyAdapter = ArrayAdapter.createFromResource(this,R.array.difficulty_array, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_difficulty.setAdapter(difficultyAdapter);

        sp_difficulty.setSelection(spinner_default);

        sp_difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_difficulty = vod_difficulty[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selected_difficulty = vod_difficulty[spinner_default];
            }
        });


        tv_vod_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                ad.setTitle("?????? ????????? ??????");
                final EditText et_tmp = new EditText(VodDetailActivity.this);
                et_tmp.setText(vod_calorie);
                ad.setMessage("???????????? ?????? ???????????? ?????????????????? (Kcal)");
                ad.setView(et_tmp);

                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vod_calorie = et_tmp.getText().toString();
                        tv_vod_calorie.setText(vod_calorie + " Kcal");
                    }

                });

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });


        tv_vod_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(VodDetailActivity.this);
                LayoutInflater inflater = (LayoutInflater) VodDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_material, null);
                ad.setView(dialogView);
                ad.setTitle("?????? ????????? ????????????");

                EditText et_material = dialogView.findViewById(R.id.et_dialog_vod_material);

                et_material.setText(vod_material);

                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vod_material = et_material.getText().toString();

                        tv_vod_material.setText(vod_material);

                    }
                });

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = ad.create();
                //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
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
                            //????????????????????? ????????????
//                            int width = iv_thumbnail.getWidth();
//                            int height = iv_thumbnail.getHeight();
//                            Bitmap resize_bitmap = Bitmap.createScaledBitmap(thumbnail, width, height, true);
//                            iv_thumbnail.setImageBitmap(resize_bitmap);
                            iv_thumbnail.setImageBitmap(thumbnail);
                            ischange = true;
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


    private File emptyFile(){
        String filename = "EMPTY_IMAGE";

        File tmp_file = new File(getApplicationContext().getCacheDir(),filename);
        try{
            tmp_file.createNewFile();
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

    private int getIvWidth(ImageView iv){
        return iv.getWidth();
    }

    private int getIvHeight(ImageView iv){
        return iv.getHeight();
    }


}