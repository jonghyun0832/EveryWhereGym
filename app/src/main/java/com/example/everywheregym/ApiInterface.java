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

    @GET("mail.php")
    Call<UserInfo> sendMail(
            @Query("email") String email
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
