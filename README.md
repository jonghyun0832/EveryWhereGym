# EveryWhereGym (어디든짐)

## Brief
올바른 홈트레이닝을 위한 비대면 PT 어플리케이션
Client(안드로이드), Server, Signaling Server(WebRTC) 3가지로 구성  
Client : https://github.com/jonghyun0832/EveryWhereGym  
Server : https://github.com/jonghyun0832/EveryWhereGymServer  
Signaling Server : https://github.com/jonghyun0832/everywheregym_signal_server  


## Application
### Application Version
* minSdk 21
* targetSdk 31

### Development
* Android Studio  
* Apache
* Mysql
* PHP

### APIs
* Retrofit2
* WebRTC
* FCM


### Dependencies
* gun0912.ted:tedpermission:2.0.0  
퍼미션
* com.github.bumptech.glide:glide:4.11.0  
글라이드
* com.google.android.material:material:1.5.0  
범위 슬라이더
* com.github.skydoves:balloon:1.1.0  
말풍선
* com.prolificinteractive:material-calendarview:1.4.3  
커스텀 캘린더
* com.google.firebase:firebase-messaging:23.0.0  
FCM
* androidx.viewpager2:viewpager2:1.1.0-beta01  
뷰페이저
### User
#### Trainer / Member
<img src="https://user-images.githubusercontent.com/72550133/164966476-b4a4e0f5-9e7f-4c86-80cc-19a5ea3ec1ad.png" width="250px" height="320px" title="User" alt="User"></img>
### Functions
#### 공통
* 로그인
* 회원가입
* 비밀번호 찾기
* 로그아웃
* 프로필 관리
* 북마크
* 내 시청기록
* VOD 필터
* VOD 검색

#### 트레이너
* 동영상 업로드
* 라이브 트레이닝 일정 만들기
* 라이브 트레이닝 시작
* 라이브 트레이닝 종료

#### 회원
* 회원탈퇴
* 동영상 이용
* 라이브 트레이닝 평가
