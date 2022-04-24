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

## Live Training Flow (WebRTC)
<img src="https://user-images.githubusercontent.com/72550133/164974264-5a9ec269-3ffa-47e5-a68d-d158b4af7ac0.png" width="800px" height="400px" title="1" alt="1"></img><br>
<img src="https://user-images.githubusercontent.com/72550133/164974267-70fa0810-8487-4d58-9dd1-12465ce0f567.png" width="800px" height="400px" title="2" alt="2"></img><br>
<img src="https://user-images.githubusercontent.com/72550133/164974270-7e4aca3c-7daf-4500-87c8-3e5111f742bb.png" width="800px" height="400px" title="3" alt="3"></img><br>
<img src="https://user-images.githubusercontent.com/72550133/164974474-3846c170-13a0-4a61-9ec2-77198bcbad55.png" width="800px" height="400px" title="4" alt="4"></img><br>
<img src="https://user-images.githubusercontent.com/72550133/164974278-3c6454a5-d92b-4822-bcde-383419c01f66.png" width="800px" height="400px" title="5-1" alt="5-1"></img>        
<img src="https://user-images.githubusercontent.com/72550133/164974279-cc5d6568-a2c7-44c0-bdf5-ce26a482423a.png" width="800px" height="400px" title="5-2" alt="5-2"></img>  
