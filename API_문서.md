# API 문서

## 엔드 포인트

### 사용자 인증
**1. 회원가입**
   - url : `/join`
   - method : `POST`
   - 설명 : 회원가입
   - request
     ```
      "username": "user",
      "password": "a123456789"
     ```

**2. 로그인**
   - url : `/login`
   - method : `POST`
   - 설명 : 로그인
   - request
     ```
      "username": "user",
      "password": "a123456789"
     ```

**3. 로그아웃**
   - url : `/logout`
   - method : `POST`
   - 설명 : 로그아웃

### AI 여행 추천

**4. 클로바 X 여행 생성**
   - url : `/api/completion`
   - method : `POST`
   - 설명 : 키워드를 통해서 클로바 X를 이용한 여행 생성
   - request
     ```
     {
      location: sixthfeedback,  // 지역
      time: seventhfeedback,    // 시간
      keywords: keywords[],       // 키워드 배열
     }
     ```
**5. 클로바 X 여행 삭제**
   - url : `/api/recommend/delete`
   - method : `DELETE`
   - 설명 : 클로바 X를 이용한 여행 삭제

### 데이터 조회
**6. 여행 조회**
   - url : `/api/recommend/{username}
   - method : `GET`
   - 설명 : 사용자가 생성한 여행 조회
   - response
     ```
     [
       {
           "id": 1,
           "name": "오스테리아 파로",
           "address": "서울 성동구 서울숲6길 20 1층",
           "url": "https://map.naver.com/p/search/오스테리아 파로",
           "username": "user",
           "createId": 1,
           "createdAt": "2024-08-01T05:28:33.312918"
       },
       {
           "id": 2,
           "name": "블루보틀 성수 카페",
           "address": "서울 성동구 아차산로 7",
           "url": "https://map.naver.com/p/search/블루보틀 성수 카페",
           "username": "user",
           "createId": 1,
           "createdAt": "2024-08-01T05:28:33.322754"
       },
       {
           "id": 3,
           "name": "할아버지공장",
           "address": "서울 성동구 성수이로7가길 3",
           "url": "https://map.naver.com/p/search/할아버지공장",
           "username": "user",
           "createId": 1,
           "createdAt": "2024-08-01T05:28:33.327221"
       },
       {
           "id": 4,
           "name": "가로수길",
           "address": "서울 강남구 도산대로13길 36",
           "url": "https://map.naver.com/p/search/가로수길",
           "username": "user",
           "createId": 2,
           "createdAt": "2024-08-01T05:30:07.710842"
       },
     ]
     ```

**6. 랜덤 여행 추천**
   - url : `/api/random/
   - method : `GET`
   - 설명 : 사용자가 랜덤으로 생성한 여행 조회
   - request
     ```
     [     
          {
           "id": 24,
           "name": "냠냠",
           "address": "????",
           "url": "https://map.naver.com/p/search/가로수길",
           "username": "user",
           "createId": 2,
           "createdAt": "2024-08-01T05:30:07.710842"
          }
     ]
     ```

     
