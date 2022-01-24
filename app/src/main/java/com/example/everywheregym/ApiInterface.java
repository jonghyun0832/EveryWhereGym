package com.example.everywheregym;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("example_select.php")
//    Call<Person> getNameHobby();

    //메일 인증용 get
    @GET("/mail/mail.php")
    Call<UserInfo> sendMail(
            @Query("email") String email
    );

    @GET("/mail/findPwMail.php")
    Call<UserInfo> sendFindMail(
            @Query("email") String email
    );

    //중복 체크용 get
    @GET("/account/checkDuplicate.php")
    Call<UserInfo> sendNickName(
            @Query("nickname") String nickname
    );

    @FormUrlEncoded
    @POST("/account/saveUserInfo.php")
    Call<UserInfo> sendUserInfo(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nickname") String nickname
    );


    @FormUrlEncoded
    @POST("/account/login.php")
    Call<UserInfo> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/account/resetPassword.php")
    Call<UserInfo> resetPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/account/destroyAccount.php")
    Call<UserInfo> destroyAccount(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("getInfo.php")
    Call<UserInfo> getInfo(
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("profileEdit.php")
    Call<UserInfo> profileEdit(
            @Field("user_id") String user_id,
            @Field("user_name") String user_name
            //여기 이미지도 같이 보내야함
    );

    @Multipart
    @POST("uploadFile.php")
    Call<UserInfo> uploadFile(
            @Part MultipartBody.Part image,
            @Query("prev_url") String prev_url,
            @Query("user_id") String user_id
    );



    @FormUrlEncoded
    @POST("getTrainerInfo.php")
    Call<TrainerInfo> getTrainerInfo(
            @Field("user_id") String user_id
    );

    //트레이너 프로필 사진들
    @Multipart
    @POST("uploadFileTR.php")
    Call<UserInfo> uploadFileTR(
            @Part ArrayList<MultipartBody.Part> images,
            @Query("prev_img_url") String prev_img_url,
            @Query("prev_back_url") String prev_back_img_url,
            @Query("user_id") String user_id
    );

    //트레이너 프로필 글씨 정보들
    @FormUrlEncoded
    @POST("profileEditTR.php")
    Call<TrainerInfo> profileEditTR(
            @Field("user_id") String user_id,
            @Field("user_name") String user_name,
            @Field("tr_intro") String tr_intro,
            @Field("tr_expert") String tr_expert,
            @Field("tr_career") String tr_career,
            @Field("tr_certify") String tr_certify
            //여기 이미지도 같이 보내야함
    );

    //동영상 업로드
    @Multipart
    @POST("uploadVideo.php")
    Call<UserInfo> uploadVideo(
            @Part MultipartBody.Part file, @PartMap HashMap<String, RequestBody> data
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name
            //@PartMap Map<String, RequestBody> map
            //@Part MultipartBody.Part video
            );

    //동영상 데이터 업로드
    @Multipart
    @POST("uploadVideoData.php")
    Call<UserInfo> uploadVideoData(
            @Part MultipartBody.Part file, @PartMap HashMap<String, RequestBody> data,
            @Part MultipartBody.Part image
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name

    );


    @FormUrlEncoded
    @POST("getvideoInfo.php")
    Call<VideoInfo> getvideoInfo(
            @Field("user_id") String user_id
    );


    //vod 리스트 만들기 (리사이클러뷰 array)
    @POST("getVodListInfo.php")
    Call<VodDataArray> getvodList(
    );

    //동영상 삭제하기
    @FormUrlEncoded
    @POST("deleteVideo.php")
    Call<VodData> deleteVod(
            @Field("vod_id") String vod_id,
            @Field("vod_thumbnail_path") String vod_thumbnail_path,
            @Field("vod_path") String vod_path
    );

    //동영상 수정하기
    @Multipart
    @POST("editVideoData.php")
    Call<VodData> editVideoData(
            @PartMap HashMap<String, RequestBody> data,
            @Part MultipartBody.Part image
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name

    );


    @FormUrlEncoded
    @POST("getVodListInfo.php")
    Call<VodData> getvodFilterList(
            @Field("category") String category,
            @Field("time") String time,
            @Field("difficulty") String difficulty
    );



//    @Multipart
//    @POST("getTrainerInfo.php")
//    Call<UserInfo> uploadFile(
//            @Part MultipartBody.Part image,
//            @Query("prev_url") String prev_url,
//            @Query("user_id") String user_id
//    );



//    @FormUrlEncoded
//    @POST("example_insert.php")
//    Call<Person> insertPerson( //post일떄는 Field get일떄는 Query 이노테이션 사용
//                               @Field("name") String name,
//                               @Field("hobby") String hobby
//    );
//
//    @GET("example_select.php")
//    Call<Person> getNameHobby(
//            @Query("name") String name,
//            @Query("hobby") String hobby
//    );
}
