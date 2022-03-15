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
    @FormUrlEncoded
    @POST("getVodListInfo.php")
    Call<VodDataArray> getvodList(
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("cursor") String cursor
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

    //vod 리스트 만들기 (리사이클러뷰 array)
    //조회수증가
    @FormUrlEncoded
    @POST("addVideoView.php")
    Call<VodData> addVideoView(
            @Field("vod_id") String vod_id,
            @Field("prev_vod_view") int prev_vod_view
    );


    //특정 트레이너의 vod 리스트 가져오기
    @FormUrlEncoded
    @POST("getTrainerVodInfo.php")
    Call<VodDataArray> getTrainervodList(
            @Field("user_id") String user_id
    );

    //필터 (길게보내서 잘라서 쓰면될듯)
    @FormUrlEncoded
    @POST("filterVod.php")
    Call<VodDataArray> filterVod(
            @Field("filter_category") String filter_category,
            @Field("filter_difficulty") String filter_difficulty,
            @Field("filter_time") String filter_time,
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("cursor") String cursor
    );


    //북마크 추가
    @FormUrlEncoded
    @POST("bookmarkVod.php")
    Call<VodData> bookmarkVod(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크 삭제
    @FormUrlEncoded
    @POST("bookmarkDelete.php")
    Call<VodData> bookmarkDelete(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크 체크
    @FormUrlEncoded
    @POST("checkBookMark.php")
    Call<VodData> checkBookMark(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크된 영상리스트 불러오기
    @FormUrlEncoded
    @POST("getBookMarkList.php")
    Call<VodDataArray> getBookMarkList(
            @Field("user_id") String user_id
    );


    //시청기록 남기기
    @FormUrlEncoded
    @POST("addHistory.php")
    Call<VodData> addHistory(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //시청기록 가져오기
    @FormUrlEncoded
    @POST("getHistory.php")
    Call<VodHistoryDataArray> getHistory(
            @Field("user_id") String user_id
    );

    //시청기록 삭제
    @FormUrlEncoded
    @POST("deleteHistory.php")
    Call<VodData> deleteHistory(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //검색기록 가져오기
    @FormUrlEncoded
    @POST("getSearchHistory.php")
    Call<SearchDataArray> getSearchHistory(
            @Field("user_id") String user_id
    );

    //검색기록 모두삭제
    @FormUrlEncoded
    @POST("deleteSearchHistoryAll.php")
    Call<SearchDataArray> deleteSearchHistoryAll(
            @Field("user_id") String user_id
    );

    //특정 검색기록 삭제
    @FormUrlEncoded
    @POST("deleteSearchHistory.php")
    Call<SearchDataArray> deleteSearchHistory(
            @Field("user_id") String user_id,
            @Field("sh_id") String sh_id
    );

    //검색기록 저장
    @FormUrlEncoded
    @POST("addSearchHistory.php")
    Call<SearchDataArray> addSearchHistory(
            @Field("user_id") String user_id,
            @Field("search_text") String search_text
    );

    //검색결과 불러오기
    @FormUrlEncoded
    @POST("getSearchList.php")
    Call<VodDataArray> getSearchList(
            @Field("search_text") String search_text
    );

    //라이브 일정 데이터 업로드
    @Multipart
    @POST("uploadLiveData.php")
    Call<LiveData> uploadLiveData(
            @PartMap HashMap<String, RequestBody> data
    );

    //live 리스트 만들기 (리사이클러뷰 array 페이징 포함)
    @FormUrlEncoded
    @POST("getLiveListInfoPaging.php")
    Call<LiveDataArray> getliveList(
            @Field("select_date") String select_date,
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("cursor") String cursor
    );

    //live 리스트 만들기 (리사이클러뷰 array)
    @FormUrlEncoded
    @POST("getLiveListInfo.php")
    Call<LiveDataArray> getliveList(
            @Field("select_date") String select_date
    );

    // 내 live 리스트 가져오기 (리사이클러뷰 array)
    @FormUrlEncoded
    @POST("getMyLiveListInfo.php")
    Call<LiveDataArray> getMyliveList(
            @Field("select_date") String select_date,
            @Field("user_id") String user_id
    );

    // 내 live 날짜 리스트 가져오기 (string [])
    @FormUrlEncoded
    @POST("getMyLiveListDateInfo.php")
    Call<LiveDataArray> getMyliveDateList(
            @Field("user_id") String user_id
    );

    //라이브 일정 삭제하기
    @FormUrlEncoded
    @POST("deleteLive.php")
    Call<LiveData> deleteLive(
            @Field("li_id") String vod_id
    );

    //알림 일정 확인하기
    @FormUrlEncoded
    @POST("checkLiveAlarm.php")
    Call<LiveData> checkLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //알림 일정 추가하기
    @FormUrlEncoded
    @POST("addLiveAlarm.php")
    Call<LiveData> addLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //알림 일정 삭제하기
    @FormUrlEncoded
    @POST("deleteLiveAlarm.php")
    Call<LiveData> deleteLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //로그인시 토큰 재저장
    @FormUrlEncoded
    @POST("updateToken.php")
    Call<LiveData> updateToken(
            @Field("user_id") String user_id,
            @Field("token") String token
    );

    //로그아웃시 토큰 삭제
    @FormUrlEncoded
    @POST("deleteToken.php")
    Call<LiveData> deleteToken(
            @Field("user_id") String user_id
    );


    //삭제 알림 보내고 라이브 삭제하기
    @FormUrlEncoded
    @POST("sendDeleteAlarm.php")
    Call<LiveData> sendDeleteAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("message") String message
    );

    //수정 알림 보내고 라이브 일정 수정
    @Multipart
    @POST("sendEditAlarm.php")
    Call<LiveData> sendEditAlarm(
            @PartMap HashMap<String, RequestBody> data
    );

    //라이브 시작했음
    @FormUrlEncoded
    @POST("sendOpenAlarm.php")
    Call<LiveData> sendOpenAlarm(
            @Field("live_id") String live_id,
            @Field("live_title") String live_title
    );

    //라이브 끝났음
    @FormUrlEncoded
    @POST("finishLive.php")
    Call<LiveData> finishLive(
            @Field("live_id") String live_id
    );

    //유저 정보 가져오기
    @FormUrlEncoded
    @POST("getUserInfo.php")
    Call<LiveData> getUserInfo(
            @Field("user_id") String user_id
    );

    //라이브 현재 인원수 체크 - 참여가능 여부
    @FormUrlEncoded
    @POST("joinCheck.php")
    Call<LiveData> joinCheck(
            @Field("live_id") String live_id
    );

    //라이브 현재 인원수 추가하기
    @FormUrlEncoded
    @POST("joinLive.php")
    Call<LiveData> joinLive(
            @Field("live_id") String live_id,
            @Field("live_join") String live_join
    );

    //라이브 현재 인원수 빼기
    @FormUrlEncoded
    @POST("leftLive.php")
    Call<LiveData> leftLive(
            @Field("live_id") String live_id
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
