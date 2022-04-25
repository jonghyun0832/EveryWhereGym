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
    @GET("/src/mail/mail.php")
    Call<UserInfo> sendMail(
            @Query("email") String email
    );

    @GET("/src/mail/findPwMail.php")
    Call<UserInfo> sendFindMail(
            @Query("email") String email
    );

    //중복 체크용 get
    @GET("/src/account/checkDuplicate.php")
    Call<UserInfo> sendNickName(
            @Query("nickname") String nickname
    );

    @FormUrlEncoded
    @POST("/src/account/saveUserInfo.php")
    Call<UserInfo> sendUserInfo(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nickname") String nickname
    );


    @FormUrlEncoded
    @POST("/src/account/login.php")
    Call<UserInfo> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/src/account/resetPassword.php")
    Call<UserInfo> resetPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/src/account/destroyAccount.php")
    Call<UserInfo> destroyAccount(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("/src/info/getInfo.php")
    Call<UserInfo> getInfo(
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("/src/profile/profileEdit.php")
    Call<UserInfo> profileEdit(
            @Field("user_id") String user_id,
            @Field("user_name") String user_name
            //여기 이미지도 같이 보내야함
    );

    @Multipart
    @POST("/src/profile/uploadFile.php")
    Call<UserInfo> uploadFile(
            @Part MultipartBody.Part image,
            @Query("prev_url") String prev_url,
            @Query("user_id") String user_id
    );



    @FormUrlEncoded
    @POST("/src/info/getTrainerInfo.php")
    Call<TrainerInfo> getTrainerInfo(
            @Field("user_id") String user_id
    );

    //트레이너 프로필 사진들
    @Multipart
    @POST("/src/profile/uploadFileTR.php")
    Call<UserInfo> uploadFileTR(
            @Part ArrayList<MultipartBody.Part> images,
            @Query("prev_img_url") String prev_img_url,
            @Query("prev_back_url") String prev_back_img_url,
            @Query("user_id") String user_id
    );

    //트레이너 프로필 글씨 정보들
    @FormUrlEncoded
    @POST("/src/profile/profileEditTR.php")
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
    @POST("/src/vod/uploadVideo.php")
    Call<UserInfo> uploadVideo(
            @Part MultipartBody.Part file, @PartMap HashMap<String, RequestBody> data
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name
            //@PartMap Map<String, RequestBody> map
            //@Part MultipartBody.Part video
            );

    //동영상 데이터 업로드
    @Multipart
    @POST("/src/vod/uploadVideoData.php")
    Call<UserInfo> uploadVideoData(
            @Part MultipartBody.Part file, @PartMap HashMap<String, RequestBody> data,
            @Part MultipartBody.Part image
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name

    );


    @FormUrlEncoded
    @POST("/src/info/getvideoInfo.php")
    Call<VideoInfo> getvideoInfo(
            @Field("user_id") String user_id
    );


    //vod 리스트 만들기 (리사이클러뷰 array)
    @FormUrlEncoded
    @POST("/src/vod/getVodListInfo.php")
    Call<VodDataArray> getvodList(
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("cursor") String cursor
    );

    //동영상 삭제하기
    @FormUrlEncoded
    @POST("/src/vod/deleteVideo.php")
    Call<VodData> deleteVod(
            @Field("vod_id") String vod_id,
            @Field("vod_thumbnail_path") String vod_thumbnail_path,
            @Field("vod_path") String vod_path
    );

    //동영상 수정하기
    @Multipart
    @POST("/src/vod/editVideoData.php")
    Call<VodData> editVideoData(
            @PartMap HashMap<String, RequestBody> data,
            @Part MultipartBody.Part image
            //@Part MultipartBody.Part file, @Part("filename") RequestBody name

    );


    @FormUrlEncoded
    @POST("/src/vod/getVodListInfo.php")
    Call<VodData> getvodFilterList(
            @Field("category") String category,
            @Field("time") String time,
            @Field("difficulty") String difficulty
    );

    //vod 리스트 만들기 (리사이클러뷰 array)
    //조회수증가
    @FormUrlEncoded
    @POST("/src/vod/addVideoView.php")
    Call<VodData> addVideoView(
            @Field("vod_id") String vod_id,
            @Field("prev_vod_view") int prev_vod_view
    );


    //특정 트레이너의 vod 리스트 가져오기
    @FormUrlEncoded
    @POST("/src/vod/getTrainerVodInfo.php")
    Call<VodDataArray> getTrainervodList(
            @Field("user_id") String user_id
    );

    //필터 (길게보내서 잘라서 쓰면될듯)
    @FormUrlEncoded
    @POST("/src/vod/filterVod.php")
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
    @POST("/src/bookmark/bookmarkVod.php")
    Call<VodData> bookmarkVod(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크 삭제
    @FormUrlEncoded
    @POST("/src/bookmark/bookmarkDelete.php")
    Call<VodData> bookmarkDelete(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크 체크
    @FormUrlEncoded
    @POST("/src/bookmark/checkBookMark.php")
    Call<VodData> checkBookMark(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //북마크된 영상리스트 불러오기
    @FormUrlEncoded
    @POST("/src/bookmark/getBookMarkList.php")
    Call<VodDataArray> getBookMarkList(
            @Field("user_id") String user_id
    );


    //시청기록 남기기
    @FormUrlEncoded
    @POST("/src/history/addHistory.php")
    Call<VodData> addHistory(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //시청기록 가져오기
    @FormUrlEncoded
    @POST("/src/history/getHistory.php")
    Call<VodHistoryDataArray> getHistory(
            @Field("user_id") String user_id
    );

    //시청기록 삭제
    @FormUrlEncoded
    @POST("/src/history/deleteHistory.php")
    Call<VodData> deleteHistory(
            @Field("user_id") String user_id,
            @Field("vod_id") String vod_id
    );

    //검색기록 가져오기
    @FormUrlEncoded
    @POST("/src/search/getSearchHistory.php")
    Call<SearchDataArray> getSearchHistory(
            @Field("user_id") String user_id
    );

    //검색기록 모두삭제
    @FormUrlEncoded
    @POST("/src/search/deleteSearchHistoryAll.php")
    Call<SearchDataArray> deleteSearchHistoryAll(
            @Field("user_id") String user_id
    );

    //특정 검색기록 삭제
    @FormUrlEncoded
    @POST("/src/search/deleteSearchHistory.php")
    Call<SearchDataArray> deleteSearchHistory(
            @Field("user_id") String user_id,
            @Field("sh_id") String sh_id
    );

    //검색기록 저장
    @FormUrlEncoded
    @POST("/src/search/addSearchHistory.php")
    Call<SearchDataArray> addSearchHistory(
            @Field("user_id") String user_id,
            @Field("search_text") String search_text
    );

    //검색결과 불러오기
    @FormUrlEncoded
    @POST("/src/search/getSearchList.php")
    Call<VodDataArray> getSearchList(
            @Field("search_text") String search_text
    );

    //라이브 일정 데이터 업로드
    @Multipart
    @POST("/src/live/uploadLiveData.php")
    Call<LiveData> uploadLiveData(
            @PartMap HashMap<String, RequestBody> data
    );

    //live 리스트 만들기 (리사이클러뷰 array 페이징 포함)
    @FormUrlEncoded
    @POST("/src/live/getLiveListInfoPaging.php")
    Call<LiveDataArray> getliveList(
            @Field("select_date") String select_date,
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("cursor") String cursor
    );

    //live 리스트 만들기 (리사이클러뷰 array)
    @FormUrlEncoded
    @POST("/src/live/getLiveListInfo.php")
    Call<LiveDataArray> getliveList(
            @Field("select_date") String select_date
    );

    // 내 live 리스트 가져오기 (리사이클러뷰 array)
    @FormUrlEncoded
    @POST("/src/live/getMyLiveListInfo.php")
    Call<LiveDataArray> getMyliveList(
            @Field("select_date") String select_date,
            @Field("user_id") String user_id
    );

    // 내 live 날짜 리스트 가져오기 (string [])
    @FormUrlEncoded
    @POST("/src/live/getMyLiveListDateInfo.php")
    Call<LiveDataArray> getMyliveDateList(
            @Field("user_id") String user_id
    );

    //라이브 일정 삭제하기
    @FormUrlEncoded
    @POST("/src/live/deleteLive.php")
    Call<LiveData> deleteLive(
            @Field("li_id") String vod_id
    );

    //알림 일정 확인하기
    @FormUrlEncoded
    @POST("/src/alarm/checkLiveAlarm.php")
    Call<LiveData> checkLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //알림 일정 추가하기
    @FormUrlEncoded
    @POST("/src/alarm/addLiveAlarm.php")
    Call<LiveData> addLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //알림 일정 삭제하기
    @FormUrlEncoded
    @POST("/src/alarm/deleteLiveAlarm.php")
    Call<LiveData> deleteLiveAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("uploader_id") String uploader_id
    );

    //로그인시 토큰 재저장
    @FormUrlEncoded
    @POST("/src/token/updateToken.php")
    Call<LiveData> updateToken(
            @Field("user_id") String user_id,
            @Field("token") String token
    );

    //로그아웃시 토큰 삭제
    @FormUrlEncoded
    @POST("/src/token/deleteToken.php")
    Call<LiveData> deleteToken(
            @Field("user_id") String user_id
    );


    //삭제 알림 보내고 라이브 삭제하기
    @FormUrlEncoded
    @POST("/src/alarm/sendDeleteAlarm.php")
    Call<LiveData> sendDeleteAlarm(
            @Field("user_id") String user_id,
            @Field("live_id") String live_id,
            @Field("message") String message
    );

    //수정 알림 보내고 라이브 일정 수정
    @Multipart
    @POST("/src/alarm/sendEditAlarm.php")
    Call<LiveData> sendEditAlarm(
            @PartMap HashMap<String, RequestBody> data
    );

    //라이브 시작했음
    @FormUrlEncoded
    @POST("/src/alarm/sendOpenAlarm.php")
    Call<LiveData> sendOpenAlarm(
            @Field("live_id") String live_id
    );

    //라이브 끝났음
    @FormUrlEncoded
    @POST("/src/live/finishLive.php")
    Call<LiveData> finishLive(
            @Field("live_id") String live_id
    );

    //유저 정보 가져오기
    @FormUrlEncoded
    @POST("/src/info/getUserInfo.php")
    Call<LiveData> getUserInfo(
            @Field("user_id") String user_id
    );

    //라이브 현재 인원수 체크 - 참여가능 여부
    @FormUrlEncoded
    @POST("/src/live/joinCheck.php")
    Call<LiveData> joinCheck(
            @Field("live_id") String live_id
    );

    //라이브 현재 인원수 추가하기
    @FormUrlEncoded
    @POST("/src/live/joinLive.php")
    Call<LiveData> joinLive(
            @Field("live_id") String live_id,
            @Field("live_join") String live_join,
            @Field("user_id") String user_id
    );

    //라이브 현재 인원수 빼기
    @FormUrlEncoded
    @POST("/src/live/leftLive.php")
    Call<LiveData> leftLive(
            @Field("live_id") String live_id,
            @Field("user_id") String user_id
    );

    //라이브 평가 요청
    @FormUrlEncoded
    @POST("/src/review/reviewLive.php")
    Call<LiveData> reviewLive(
            @Field("live_id") String live_id,
            @Field("user_id") String user_id
    );

    //라이브 정보 가져오기
    @FormUrlEncoded
    @POST("/src/live/getLiveInfo.php")
    Call<LiveData> getLiveInfo(
            @Field("live_id") String live_id
    );

    //라이브 리뷰 저장하기
    @FormUrlEncoded
    @POST("/src/review/addReview.php")
    Call<LiveData> addReview(
            @Field("uploader_id") String uploader_id,
            @Field("user_id") String user_id,
            @Field("review") String review,
            @Field("score") int score,
            @Field("title") String title
    );

    //라이브 리뷰 가져오기
    @FormUrlEncoded
    @POST("/src/review/getReview.php")
    Call<ReviewDataArray> getReview(
            @Field("uploader_id") String uploader_id,
            @Field("page") int page,
            @Field("cursor") String cursor
    );

    //라이브 리뷰 점수 가져오기
    @FormUrlEncoded
    @POST("/src/review/getReviewScore.php")
    Call<ReviewData> getReviewScore(
            @Field("uploader_id") String uploader_id
    );

    //라이브 리뷰 삭제
    @FormUrlEncoded
    @POST("/src/review/deleteReview.php")
    Call<ReviewData> deleteReivew(
            @Field("rv_id") String rv_id
    );

    //라이브 리뷰 수정
    @FormUrlEncoded
    @POST("/src/review/editReview.php")
    Call<ReviewData> editReivew(
            @Field("rv_id") String rv_id,
            @Field("rv_text") String rv_text,
            @Field("rv_score") int rv_score
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
