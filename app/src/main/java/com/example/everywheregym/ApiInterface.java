package com.example.everywheregym;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("example_select.php")
//    Call<Person> getNameHobby();

    //메일 인증용 get
    @GET("mail.php")
    Call<UserInfo> sendMail(
            @Query("email") String email
    );

    @GET("findPwMail.php")
    Call<UserInfo> sendFindMail(
            @Query("email") String email
    );

    //중복 체크용 get
    @GET("checkDuplicate.php")
    Call<UserInfo> sendNickName(
            @Query("nickname") String nickname
    );

    @FormUrlEncoded
    @POST("saveUserInfo.php")
    Call<UserInfo> sendUserInfo(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nickname") String nickname
    );


    @FormUrlEncoded
    @POST("login.php")
    Call<UserInfo> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("resetPassword.php")
    Call<UserInfo> resetPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("destroyAccount.php")
    Call<UserInfo> destroyAccount(
            @Field("user_id") String user_id
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
