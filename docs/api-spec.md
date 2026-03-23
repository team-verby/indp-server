# INDP Server API 명세서

> Base URL: `/api`
> Content-Type: `application/json`
> 인증: `Authorization: Bearer {accessToken}` 헤더

---

## 인증 (Auth)

### 어드민 로그인
- **POST** `/api/admin/login`
- 인증 불필요
- Request Body:
  ```json
  {
    "loginId": "string",
    "password": "string"
  }
  ```
- Response `200`:
  ```json
  {
    "accessToken": "string"
  }
  ```

### 오너 로그인
- **POST** `/api/owner/login`
- 인증 불필요
- Request Body:
  ```json
  {
    "loginId": "string",
    "password": "string"
  }
  ```
- Response `200`:
  ```json
  {
    "accessToken": "string"
  }
  ```

> **로그아웃**: 서버 API 없음. 클라이언트에서 토큰 삭제로 처리.

---

## 플랜 (Plan)

### 플랜 목록 조회
- **GET** `/api/plans`
- 인증 불필요
- Response `200`:
  ```json
  {
    "plans": [
      {
        "planId": 1,
        "code": "string",
        "subtitle": "string",
        "description": "string",
        "monthlyPrice": 30000,
        "isRecommended": true,
        "discount": {
          "discountRate": 20,
          "discountedPrice": 24000
        },
        "features": ["string"]
      }
    ]
  }
  ```
  > `discount`: 할인 없으면 `null`

---

## 이미지 (Image)

### 이미지 업로드
- **POST** `/api/images`
- Content-Type: `multipart/form-data`
- 인증 불필요
- Request: `image` (file)
- Response `200`:
  ```json
  {
    "imageUrl": "string"
  }
  ```

---

## 매장 (Store) — 공개 API

### 매장 목록 조회
- **GET** `/api/stores`
- 인증 불필요
- Query Params: `page`, `size` (default: 20)
- Response `200`:
  ```json
  {
    "stores": [
      {
        "storeId": 1,
        "name": "string",
        "industry": "string",
        "address": "string",
        "mainPhotoUrl": "string",
        "businessHours": [
          {
            "dayOfWeek": 1,
            "openTime": "09:00:00",
            "closeTime": "22:00:00",
            "isClosed": false
          }
        ]
      }
    ]
  }
  ```
  > `mainPhotoUrl`: null 가능
  > `dayOfWeek`: 1=월요일 ~ 7=일요일

### 매장 상세 조회
- **GET** `/api/stores/{storeId}`
- 인증 불필요
- Response `200`:
  ```json
  {
    "storeId": 1,
    "name": "string",
    "industry": "string",
    "address": "string",
    "playedMusic": "string",
    "businessHours": [
      {
        "dayOfWeek": 1,
        "openTime": "09:00:00",
        "closeTime": "22:00:00",
        "isClosed": false
      }
    ],
    "photos": [
      {
        "imageUrl": "string",
        "isMain": true
      }
    ]
  }
  ```
  > `playedMusic`: null 가능
  > `dayOfWeek`: 1=월요일 ~ 7=일요일

### 매장 입점 신청
- **POST** `/api/stores/apply`
- 인증 불필요
- Request Body:
  ```json
  {
    "applicantName": "string",
    "applicantPhone": "string",
    "inquiryContent": "string",
    "planId": 1,
    "usagePeriod": 1,
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
    "playMethods": ["BLUETOOTH"],
    "moods": ["CALM"],
    "lighting": 3,
    "playlistType": "MUSIC_RECOMMENDED",
    "timePreferences": [
      {
        "startTime": "09:00:00",
        "endTime": "12:00:00",
        "mood": "string"
      }
    ],
    "vibe": "string",
    "tempo": "NORMAL",
    "rejectedGenres": ["string"],
    "rejectedSongNote": "string"
  }
  ```
  > `usagePeriod`: `1` | `3` | `6` | `12` (개월)
  > `lighting`: 1~5 밝기 단계
  > `playMethods`: `BLUETOOTH` | `WIRED` | `OTHER`
  > `moods`: `CALM` | `MODERN` | `ELEGANT` | `DARK` | `NATURAL` | `OTHER`
  > `playlistType`: `MUSIC_RECOMMENDED` | `TIME_BASED` | `CONSISTENT_VIBE`
  > `timePreferences`: `playlistType`이 `TIME_BASED`일 때만 사용
  > `vibe`: `playlistType`이 `CONSISTENT_VIBE`일 때 원하는 무드 텍스트
  > `tempo`: `SLOW` | `CALM` | `NORMAL` | `LIVELY` | `UPBEAT`
- Response `201`:
  ```json
  {
    "orderId": "string",
    "amount": 234000,
    "orderName": "string"
  }
  ```
  > 토스 SDK에 전달할 결제 초기화 파라미터 반환

---

## 결제 (Payment)

### 매장 도입 결제 확인
- **POST** `/api/payments/apply/confirm`
- 인증 불필요
- Request Body:
  ```json
  {
    "paymentKey": "string",
    "orderId": "string",
    "amount": 234000
  }
  ```
- Response `200`:
  ```json
  {
    "loginId": "string",
    "password": "string"
  }
  ```
  > 결제 확인 후 오너 계정 ID와 임시 비밀번호 반환

### 매장 도입 결제 실패
- **POST** `/api/payments/apply/fail`
- 인증 불필요
- Request Body:
  ```json
  {
    "orderId": "string"
  }
  ```
- Response `200` (body 없음)

### 음악 추천 결제 확인
- **POST** `/api/payments/recommendation/confirm`
- 인증 불필요
- Request Body:
  ```json
  {
    "paymentKey": "string",
    "orderId": "string",
    "amount": 1000
  }
  ```
- Response `200` (body 없음)
  > 결제 확인 후 플레이리스트에 추천곡 반영 및 WebSocket 알림 발송

### 음악 추천 결제 실패
- **POST** `/api/payments/recommendation/fail`
- 인증 불필요
- Request Body:
  ```json
  {
    "orderId": "string"
  }
  ```
- Response `200` (body 없음)

---

## 매장 플레이리스트 — 통합 조회

### 플레이리스트 단건 조회 (공개 + 오너 통합)
- **GET** `/api/stores/{storeId}/playlist`
- 인증: 선택적 (`Authorization: Bearer {ownerAccessToken}` 헤더 포함 시 `isOwner` 판별)
- Response `200`:
  ```json
  {
    "isOwner": true,
    "currentSong": {
      "playlistSongId": 1,
      "playOrder": 3,
      "title": "string",
      "artist": "string",
      "vid": "string",
      "playTime": 210,
      "elapsedSeconds": 45,
      "isRecommended": false,
      "refereeName": null
    },
    "playlist": {
      "totalCount": 10,
      "recommendedCount": 2,
      "totalPlayTime": 2100,
      "songs": [
        {
          "playlistSongId": 1,
          "playOrder": 1,
          "title": "string",
          "artist": "string",
          "playTime": 210,
          "isRecommended": false,
          "refereeName": null
        }
      ]
    }
  }
  ```
  > `isOwner`: 오너 토큰 포함 시 본인 매장 여부 판별, 미포함 시 `false`
  > `currentSong`: 현재 재생곡 없으면 `null`
  > `playlist`: 플레이리스트 없으면 `null`
  > `playOrder`: 플레이리스트 내 재생 순서 (1부터 시작)
  > `vid`: YouTube 영상 ID
  > `playTime`: 초 단위
  > `elapsedSeconds`: 현재 재생 경과 초
  > `totalPlayTime`: 전체 재생 시간 합산 (초 단위)
  > `refereeName`: 추천한 손님 이름, null 가능

---

## 곡 추천 (Song Recommendation)

### 곡 추천 등록 (손님용)
- **POST** `/api/{storeId}/songs/recommendations`
- 인증 불필요
- Request Body:
  ```json
  {
    "title": "string",
    "artist": "string",
    "vid": "string",
    "refereeName": "string"
  }
  ```
  > `vid`: YouTube 영상 ID (UI에서 검색 후 전달)
- Response `201`:
  ```json
  {
    "songRecommendationId": 1,
    "title": "string",
    "artist": "string",
    "fee": 1000,
    "orderId": "string",
    "amount": 1000,
    "orderName": "노래 추천"
  }
  ```
  > `fee`: 추천 수수료 (원)
  > `orderId`, `amount`, `orderName`: 토스 SDK에 전달할 결제 초기화 파라미터
  > 결제 완료 후 `POST /api/payments/recommendation/confirm` 호출 필요

---

## 매장 (Owner) — 오너 전용

> 모든 `/api/owner/**` 요청에 `Authorization: Bearer {ownerAccessToken}` 필요

### 내 매장 목록 조회
- **GET** `/api/owner/stores`
- Response `200`:
  ```json
  {
    "stores": [
      {
        "storeId": 1,
        "name": "string",
        "industry": "string",
        "address": "string",
        "mainPhotoUrl": "string",
        "businessHours": [
          {
            "dayOfWeek": 1,
            "openTime": "09:00:00",
            "closeTime": "22:00:00",
            "isClosed": false
          }
        ]
      }
    ]
  }
  ```

### 내 매장 정보 조회
- **GET** `/api/owner/stores/{storeId}`
- Response `200`:
  ```json
  {
    "applyInfo": {
      "applicantName": "string",
      "applicantPhone": "string"
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
    "musicInfo": {
      "customerAgeGroup": "string",
      "playMethods": ["BLUETOOTH"],
      "moods": ["CALM"],
      "lighting": 3,
      "playlistType": "MUSIC_RECOMMENDED",
      "timePreferences": [
        {
          "startTime": "09:00:00",
          "endTime": "12:00:00",
          "mood": "string"
        }
      ],
      "vibe": "string",
      "tempo": "NORMAL",
      "rejectedGenres": ["string"],
      "rejectedSongNote": "string"
    }
  }
  ```
  > `applyInfo`: null 가능
  > `musicInfo`: null 가능
  > `timePreferences`: `playlistType`이 `TIME_BASED`일 때만 사용, 그 외 `null` 또는 빈 배열
  > `vibe`: `playlistType`이 `CONSISTENT_VIBE`일 때만 사용, 그 외 `null`

### 플랜 구독 내역 조회
- **GET** `/api/owner/stores/{storeId}/subscriptions`
- Response `200`:
  ```json
  [
    {
      "startDate": "2025-01-01",
      "endDate": "2025-01-31",
      "paidAt": "2025-01-01T10:00:00",
      "planCode": "string",
      "planSubtitle": "string"
    }
  ]
  ```
  > `paidAt`: null 가능

### 플레이리스트 재생성 요청
- **POST** `/api/owner/stores/{storeId}/playlist/regenerate`
- Response `200` (body 없음)

---

## WebSocket — STOMP 통신

### 연결

- 엔드포인트: `/ws` (SockJS 지원)
- **오너 인증**: 연결 시 쿼리 파라미터로 토큰 전달
  ```
  ws://host/ws?token={ownerAccessToken}
  ```
  > 토큰 없이 연결하면 게스트로 처리 (연결 자체는 허용)

| 설정 | 값 |
|---|---|
| Message Broker (subscribe prefix) | `/topic` |
| App Destination (send prefix) | `/app` |

---

### 구독 채널

#### 매장 채널 구독
- **SUBSCRIBE** `/topic/stores/{storeId}`
- 오너 기기, 손님 등 매장과 관련된 모든 클라이언트가 구독
- 구독 즉시 서버에서 INIT 메시지 자동 발송:
  ```json
  {
    "type": "INIT",
    "isOwner": true,
    "storeId": 1
  }
  ```
  > `isOwner`: 오너 토큰으로 연결한 경우 `true`, 게스트면 `false`

#### 어드민 현재 재생곡 채널 구독
- **SUBSCRIBE** `/topic/admin/stores/{storeId}/current-song`
- 어드민이 실시간 현재 재생곡 정보를 수신할 때 사용

---

### 메시지 전송 (Client → Server)

#### 현재 재생곡 전송 (오너 기기 → 서버)
- **SEND** `/app/stores/{storeId}/current-song`
- 서버가 `REQUEST_CURRENT_SONG`을 보낸 뒤 오너 기기가 응답할 때 사용
- Payload:
  ```json
  {
    "title": "string",
    "artist": "string",
    "vid": "string",
    "elapsedSeconds": 45
  }
  ```
- 서버는 이를 `/topic/admin/stores/{storeId}/current-song` 으로 relay

---

### 서버 Push 메시지 (Server → Client)

#### `REQUEST_CURRENT_SONG`
- 채널: `/topic/stores/{storeId}`
- 트리거: 어드민이 `GET /api/admin/stores/{storeId}/playlist/current-realtime` 호출
- 수신 후 오너 기기는 `/app/stores/{storeId}/current-song` 으로 현재 재생곡 정보를 전송
  ```json
  {
    "type": "REQUEST_CURRENT_SONG"
  }
  ```

#### `SONG_RECOMMENDED`
- 채널: `/topic/stores/{storeId}`
- 트리거: 손님이 `POST /api/{storeId}/songs/recommendations` 호출 시 자동 발송
- 오너 기기에서 추천 알림 표시 용도
  ```json
  {
    "type": "SONG_RECOMMENDED",
    "title": "string",
    "artist": "string",
    "vid": "string",
    "playOrder": 4.5,
    "refereeName": "string"
  }
  ```
  > `playOrder`: `PlaylistSong`에 저장된 정렬용 double 값 (null 가능 — 플레이리스트 없는 매장)

---

## WebSocket 메시지 흐름

```
어드민           서버                    오너 기기
  |                |                        |
  | GET /current-realtime                   |
  |--------------->|                        |
  |                | PUBLISH REQUEST_CURRENT_SONG
  |                |----------------------->|
  |                |    /topic/stores/{id}  |
  |                |                        |
  |                | SEND /app/stores/{id}/current-song
  |                |<-----------------------|
  |                |                        |
  | MESSAGE (current song info)             |
  |<---------------|                        |
  | /topic/admin/stores/{id}/current-song  |
```

---

## 열거형 (Enum) 값 정리

| 필드 | 값 |
|---|---|
| `usagePeriod` | `1`, `3`, `6`, `12` (개월) |
| `playMethods` | `BLUETOOTH`, `WIRED`, `OTHER` |
| `moods` | `CALM`, `MODERN`, `ELEGANT`, `DARK`, `NATURAL`, `OTHER` |
| `playlistType` | `MUSIC_RECOMMENDED`, `TIME_BASED`, `CONSISTENT_VIBE` |
| `tempo` | `SLOW`, `CALM`, `NORMAL`, `LIVELY`, `UPBEAT` |

---

## 인증 범위 요약

| 경로 패턴 | 인증 |
|---|---|
| `POST /api/admin/login` | 불필요 |
| `POST /api/owner/login` | 불필요 |
| `POST /api/stores/apply` | 불필요 |
| `GET /api/plans` | 불필요 |
| `POST /api/images` | 불필요 |
| `GET /api/stores/**` | 불필요 |
| `GET /api/stores/{storeId}/playlist` | 불필요 (토큰 있으면 isOwner 판별) |
| `POST /api/{storeId}/songs/recommendations` | 불필요 |
| `POST /api/payments/**` | 불필요 |
| `/api/admin/**` | 어드민 JWT |
| `/api/owner/**` | 오너 JWT |
| WebSocket `/ws?token=` | 오너 JWT (선택적) |
