# 코로나19 예방접종센터 지도 서비스

## ⚙️ 주요 기능
- 공공데이터 & NAVER MAP을 활용한 코로나19 예방접종센터 목록 조회  
- 현재 위치 기능을 통한 인접 예방접종센터 목록 조회  
- 코로나19 예방접종센터 상세 정보 조회  

<br>

## 🎥 화면 소개
### 1. Splash
- API를 통해 1페이지(page)에 10개(perPage)씩 순서대로 10개 페이지 호출(총 100개)하여 데이터 저장. (Room 사용)
- 2초에 걸쳐 100%가 되도록 로딩바 구현. (첫번째 영상)  
- API 데이터 저장이 완료되지 않았다면 80%에서 대기 후 저장이 완료되면 0.4초에 걸쳐 로딩바가 100% 되도록 구현. (두번째 영상)
- 저장이 완료되면 Map 화면으로 이동.

<p align="center">
  <img src="https://user-images.githubusercontent.com/66666533/232448241-ef737194-0efa-408b-998d-68956adee57b.gif" width="20%">
  <img src="https://user-images.githubusercontent.com/66666533/232448698-81bbdca8-a268-404a-8ff4-c1441cd461f9.gif" width="20%">
</p>

<br>

### 2. Map
- 저장된 리스트의 데이터를 통해 마커 생성
- CenterType에 따라 마커 색상 구분 (중앙/권역: 빨간색, 지역: 파란색)
- 마커 클릭 시 해당 마커로 지도 카메라 이동 후 해당 마커의 정보를 정보안내창에 표시
- 현재 위치 버튼 클릭 시 현재 위치로 이동

<p align="center">
  <img src="https://user-images.githubusercontent.com/66666533/232457150-b5c4491a-5bab-43d3-870e-18f30bc0f76e.gif" width="20%">
</p>

<br>

## 🛠️ 활용 기술
- Language: Kotlin  
- Design Pattern: MVVM
- UI Layout: XML
- Network: Retrofit2
- Jetpack: View Bidning, DataBinding, ROOM
- Naver MAP
- DI: Hilt
- Coroutine Flow

<br>

## 🔎 사용 라이브러리
- Hilt
- Retrofit2
- Gson
- Coroutine
- Room
- Naver Map SDK
- Play-Services-Location

<br>

## 🗂️ 패키지 소개
- dto: API 호출 시 전달 받는 Response 데이터 클래스를 모아 놓은 패키지
- entity: Room Entity 데이터 클래스를 모아 놓은 패키지
- di: Hilt를 활용한 의존성 주입 관련 클래스를 모아 놓은 패키지
- db: Room DB 관련 설정 클래스를 모아 놓은 패키지
- dao: Room의 CRUD Query와 관련된 클래스를 모아 놓은 패키
- interceptor: Retrofit에서 사용되는 Interceptor 클래스를 모아 놓은 패키지
- service: Retrofit에서 사용되는 Rest API 관련 클래스를 모아 놓은 패키지
- dataSource: Room 또는 Retrofit과 연결되는 DataSource 클래스를 모아 놓은 패키지
- repository: 비즈니스 로직을 담당하는 Repository 클래스와 인터페이스를 모아 놓은 패키지
- viewmodel: ViewModel 클래스를 모아 놓은 패키지
- view: Activity, Fragment 등 UI와 관련된 클래스를 모아 놓은 패키지  

<br>

## 👩‍🔧 더 좋은 서비스를 위해❕
- MAP 화면에서 상세 정보가 보일 때 화면 회전 시 UI가 사라지는 이슈 개선 필요.
- MAP 화면에서 현재 위치 또는 마커 클릭 후 화면 회전 시 줌 아웃되는 이슈 개선 필요.
- 더욱 자세한 예외 처리 필요.
