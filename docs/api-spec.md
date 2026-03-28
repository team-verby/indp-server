# INDP Server API 명세서

> Base URL: `/api`
> Content-Type: `application/json`
> 인증: `Authorization: Bearer {accessToken}` 헤더

---

## 인증 (Auth)

### 어드민 로그인
- **POST** `/api/admin/login`
- Request Body:
  ```json
  { "loginId": "string", "password": "string" }
  ```
- Response `200`:
  ```json
  { "accessToken": "string" }
  ```

### 오너 로그인
- **POST** `/api/owner/login`
- Request Body:
  ```json
  { "loginId": "string", "password": "string" }
  ```
- Response `200`:
  ```json
  { "accessToken": "string" }
  ```

> 로그아웃은 클라이언트에서 토큰 삭제로 처리

---

## 이미지 (Image)

### 이미지 업로드
- **POST** `/api/images`
- Content-Type: `multipart/form-data`
- Request: `image` (file)
- Response `200`:
  ```json
  { "imageUrl": "string" }
  ```

---

## 플랜 (Plan)

### 플랜 목록 조회
- **GET** `/api/plans`
- Response `200`:
  ```json
  {
    "plans": [
      {
        "planId": 1,
        "code": "PLAN_A",
        "subtitle": "string",
        "description": "string",
        "monthlyPrice": 30000,
        "isRecommended": true,
        "discount": {             // 할인 없으면 null
          "discountRate": 20,
          "discountedPrice": 24000
        },
        "features": ["string"]
      }
    ]
  }
  ```

---

## 매장 — 공개

### 매장 목록 조회
- **GET** `/api/stores?page=0&size=20`
- Response `200`:
  ```json
  {
    "stores": [
      {
        "storeId": 1,
        "name": "string",
        "industry": "string",
        "address": "string",
        "mainPhotoUrl": "string",   // null 가능
        "businessHours": [
          {
            "dayOfWeek": 1,         // 1=월 ~ 7=일
            "openTime": "09:00:00",
            "closeTime": "22:00:00",
            "isClosed": false
          }
        ]
      }
    ]
  }
  ```

### 매장 상세 조회
- **GET** `/api/stores/{storeId}`
- Response `200`:
  ```json
  {
    "storeId": 1,
    "name": "string",
    "industry": "string",
    "address": "string",
    "businessHours": [
      {
        "dayOfWeek": 1,
        "openTime": "09:00:00",
        "closeTime": "22:00:00",
        "isClosed": false
      }
    ],
    "photos": [
      { "imageUrl": "string", "isMain": true }
    ]
  }
  ```

### 매장 입점 신청
- **POST** `/api/stores/apply`
- Request Body:
  ```json
  {
    "applicantName": "string",
    "applicantPhone": "string",
    "inquiryContent": "string",
    "planId": 1,
    "usagePeriod": 1,             // 1 | 3 | 6 | 12 (개월)
    "name": "string",
    "industry": "string",
    "address": "string",
    "businessHours": [
      {
        "dayOfWeek": 1,
        "openTime": "09:00:00",
        "closeTime": "22:00:00",
        "isClosed": false
      }
    ],
    "photoUrls": ["string"],
    "platform": "string",
    "playedMusic": "string",
    "customerAgeGroup": "string",
    "playMethods": ["BLUETOOTH"],  // BLUETOOTH | WIRED | OTHER
    "vibes": ["CALM"],             // CALM | MODERN | ELEGANT | DARK | NATURAL | OTHER
    "lighting": 3,                 // 1~5
    "playlistType": "MUSIC_RECOMMENDED",  // MUSIC_RECOMMENDED | TIME_BASED | CONSISTENT_VIBE
    "timePreferences": [           // playlistType=TIME_BASED 일 때
      { "startTime": "09:00:00", "endTime": "12:00:00", "mood": "string" }
    ],
    "mood": "string",              // playlistType=CONSISTENT_VIBE 일 때
    "musicTempo": "NORMAL",        // SLOW | CALM | NORMAL | LIVELY | UPBEAT
    "preferenceGenres": [
      { "genre": "string", "preferenceType": "PREFER" }  // PREFER | REJECT
    ],
    "rejectedSongNote": "string"
  }
  ```
- Response `200`:
  ```json
  {
    "orderId": "string",
    "amount": 30000,
    "orderName": "string"
  }
  ```
  > 응답값으로 토스페이먼츠 결제창 호출

### 매장 입점 결제 확인
- **GET** `/api/payments/store-apply/confirm?paymentKey=&orderId=&amount=`
- 토스페이먼츠 결제 완료 후 redirect URL
- Response: 완료 페이지로 redirect
  ```
  /apply/complete?loginId={loginId}&password={password}
  ```

---

## 매장 — 오너 전용

> `Authorization: Bearer {ownerAccessToken}` 필요

### 내 매장 목록 조회
- **GET** `/api/owner/stores`
- Response `200`: 공개 목록과 동일 구조

### 내 매장 상세 조회
- **GET** `/api/owner/stores/{storeId}`
- Response `200`:
  ```json
  {
    "applyInfo": {                // null 가능
      "applicantName": "string",
      "applicantPhone": "string",
      "inquiryContent": "string"
    },
    "storeInfo": {
      "name": "string",
      "industry": "string",
      "address": "string",
      "businessHours": [
        {
          "dayOfWeek": 1,
          "openTime": "09:00:00",
          "closeTime": "22:00:00",
          "isClosed": false
        }
      ],
      "photoUrls": ["string"]
    },
    "musicInfo": {                // null 가능
      "customerAgeGroup": "string",
      "playMethods": ["BLUETOOTH"],
      "vibes": ["CALM"],
      "lighting": 3,
      "platform": "string",
      "playedMusic": "string",
      "playlistType": "MUSIC_RECOMMENDED",
      "timePreferences": [
        { "startTime": "09:00:00", "endTime": "12:00:00", "mood": "string" }
      ],
      "mood": "string",
      "musicTempo": "NORMAL",
      "preferenceGenres": [
        { "genre": "string", "preferenceType": "PREFER" }
      ],
      "rejectedSongNote": "string"
    }
  }
  ```

### 내 매장 정보 수정
- **PUT** `/api/owner/stores/{storeId}`
- Request Body: 상세 조회의 `storeInfo` + `musicInfo` 동일 구조
- Response `204`

### 내 매장 구독 내역 조회
- **GET** `/api/owner/stores/{storeId}/subscriptions`
- Response `200`:
  ```json
  {
    "subscriptions": [
      {
        "startDate": "2025-01-01",
        "endDate": "2025-01-31",
        "paidAt": "2025-01-01T10:00:00",  // null 가능
        "planType": "PLAN_A",
        "planSubtitle": "string"
      }
    ]
  }
  ```

### 플레이리스트 재생성 요청
- **POST** `/api/owner/stores/{storeId}/playlist/regenerate`
- Response `200`
- 슬랙으로 재생성 요청 알림 전송

---

## 플레이리스트

### 플레이리스트 조회
- **GET** `/api/stores/{storeId}/playlist`
- `Authorization` 헤더 선택적 (있으면 isOwner 판별에 사용)
- Response `200`:
  ```json
  {
    "isOwner": false,
    "currentSong": {              // 재생 중인 곡 없으면 null
      "playlistSongId": 1,
      "playOrder": 1,             // 플레이리스트 내 순서 (1부터)
      "title": "string",
      "artist": "string",
      "vid": "string",            // YouTube 영상 ID
      "playTime": 210,            // 초 단위
      "elapsedSeconds": 45,       // 현재 재생 경과 초
      "isRecommended": false,
      "refereeName": null         // null 가능
    },
    "playlist": {                 // 플레이리스트 없으면 null
      "totalCount": 10,
      "recommendedCount": 2,
      "totalPlayTime": 2100,      // 초 단위
      "songs": [
        {
          "playlistSongId": 1,
          "playOrder": 1,
          "title": "string",
          "artist": "string",
          "playTime": 210,        // 초 단위
          "isRecommended": false,
          "refereeName": null     // null 가능
        }
      ]
    }
  }
  ```

---

## 노래 추천

### 노래 추천 등록
- **POST** `/api/stores/{storeId}/songs/recommendations`
- Request Body:
  ```json
  {
    "title": "string",
    "artist": "string",
    "vid": "string",
    "playTime": 210,              // 초 단위
    "refereeName": "string"
  }
  ```
- Response `200`:
  ```json
  {
    "songRecommendationId": 1,
    "orderId": "string",
    "amount": 500,
    "orderName": "string"
  }
  ```
  > 응답값으로 토스페이먼츠 결제창 호출

### 노래 추천 결제 확인
- **POST** `/api/payments/recommendation/confirm`
- Request Body:
  ```json
  {
    "paymentKey": "string",
    "orderId": "string",
    "amount": 500
  }
  ```
- Response `200`
- 결제 완료 시 플레이리스트에 추가되고 소켓으로 `SONG_RECOMMENDED` 메시지 발송

### 노래 추천 결제 실패
- **POST** `/api/payments/recommendation/fail`
- Request Body:
  ```json
  { "orderId": "string" }
  ```
- Response `200`

---

## 어드민 — Admin 전용

> `Authorization: Bearer {adminAccessToken}` 필요

### 플레이리스트 스케줄 등록
- **POST** `/api/admin/playlists/schedule`
- Request Body:
  ```json
  [
    {
      "storeId": 1,
      "scheduledAt": "2026-03-30T10:00:00",
      "songs": [
        {
          "title": "string",
          "artist": "string",
          "vid": "string",
          "playTime": 210         // 초 단위
        }
      ]
    }
  ]
  ```
- Response `204`
- `scheduledAt` 시각이 되면 기존 플레이리스트 전체 교체 (매 1분 스케줄러 체크)

---

## WebSocket — STOMP

> 연결 엔드포인트: `/ws` (SockJS 지원)
>
> Message Broker prefix: `/topic`
> App Destination prefix: `/app`

---

### 연결

```
ws://host/ws?token={ownerAccessToken}
```
- 토큰 있으면 오너로 인식, 없으면 게스트
- 연결 후 `/topic/stores/{storeId}` 구독 시 서버에서 `INIT` 메시지 자동 발송:
  ```json
  { "type": "INIT", "isOwner": true, "storeId": 1 }
  ```

---

### 구독 채널

| 채널 | 대상 | 설명 |
|---|---|---|
| `/topic/stores/{storeId}` | 오너 기기, 손님 | 매장 관련 모든 push 수신 |
| `/topic/admin/stores/{storeId}/current-song` | 어드민 | 현재 재생곡 실시간 수신 |

---

### 서버 → 클라이언트 Push 메시지

#### INIT
구독 즉시 발송
```json
{ "type": "INIT", "isOwner": true, "storeId": 1 }
```

#### SONG_RECOMMENDED
손님이 노래 추천 결제 완료 시 발송
```json
{
  "type": "SONG_RECOMMENDED",
  "title": "string",
  "artist": "string",
  "vid": "string",
  "playOrder": 4.5,         // PlaylistSong.playOrder 값 (fractional index)
  "refereeName": "string"
}
```

#### REQUEST_CURRENT_SONG
어드민이 현재 재생곡 조회 요청 시 발송 — 오너 기기가 수신 후 응답 전송
```json
{ "type": "REQUEST_CURRENT_SONG" }
```

---

### 클라이언트 → 서버 메시지

#### 현재 재생곡 전송 (오너 기기)
- **SEND** `/app/stores/{storeId}/current-song`
- `REQUEST_CURRENT_SONG` 수신 후 응답, 또는 곡이 바뀔 때마다 전송
- 서버에서 DB 저장 + `/topic/admin/stores/{storeId}/current-song` 으로 relay
  ```json
  {
    "playlistSongId": 1,
    "elapsedSeconds": 45
  }
  ```

---

### 연결 끊김 처리
오너 기기 소켓 연결 끊김 → 서버에서 `playlist.isPlaying = false` 자동 처리

---

## 열거형 (Enum)

| 필드 | 값 |
|---|---|
| `playMethods` | `BLUETOOTH` \| `WIRED` \| `OTHER` |
| `vibes` | `CALM` \| `MODERN` \| `ELEGANT` \| `DARK` \| `NATURAL` \| `OTHER` |
| `playlistType` | `MUSIC_RECOMMENDED` \| `TIME_BASED` \| `CONSISTENT_VIBE` |
| `musicTempo` | `SLOW` \| `CALM` \| `NORMAL` \| `LIVELY` \| `UPBEAT` |
| `preferenceType` | `PREFER` \| `REJECT` |
| `recommendationStatus` | `PENDING_PAYMENT` \| `RECOMMENDED` \| `PAYMENT_FAILED` |

---

## 인증 범위 요약

| 경로 패턴 | 인증 |
|---|---|
| `POST /api/admin/login` | 불필요 |
| `POST /api/owner/login` | 불필요 |
| `POST /api/stores/apply` | 불필요 |
| `GET /api/payments/store-apply/confirm` | 불필요 |
| `GET /api/plans` | 불필요 |
| `POST /api/images` | 불필요 |
| `GET /api/stores/**` | 불필요 |
| `POST /api/stores/{storeId}/songs/recommendations` | 불필요 |
| `POST /api/payments/recommendation/**` | 불필요 |
| `/api/admin/**` | 어드민 JWT |
| `/api/owner/**` | 오너 JWT |
| WebSocket `?token=` | 오너 JWT (선택적) |
