package com.example.everywheregym;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
