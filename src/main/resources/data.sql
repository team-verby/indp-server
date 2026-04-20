INSERT INTO price_policy (policy_key, amount)
VALUES ('recommendation_fee', 500);
INSERT INTO admin (login_id, password)
VALUES ('admin', 'admin');

-- Plan A
INSERT INTO plan (type, monthly_price)
VALUES ('PLAN_A', 22000);

-- Plan B
INSERT INTO plan (type, monthly_price)
VALUES ('PLAN_B', 33000);

-- discount
INSERT INTO plan_discount (plan_id, discount_rate, is_active)
VALUES (1, 60, true);

INSERT INTO plan_discount (plan_id, discount_rate, is_active)
VALUES (2, 60, true);

-- =====================
-- 목 데이터: 매장 3개
-- =====================

-- store_apply (신청자 정보)
INSERT INTO store_apply (applicant_name, applicant_phone, created_at)
VALUES ('김지현', '010-1234-5678', NOW());
INSERT INTO store_apply (applicant_name, applicant_phone, created_at)
VALUES ('박민준', '010-5555-7777', NOW());
INSERT INTO store_apply (applicant_name, applicant_phone, created_at)
VALUES ('한나라', '010-6677-8899', NOW());

-- owner (점주 계정)
INSERT INTO owner (login_id, password, name, phone)
VALUES ('store0001', 'pass1234', '김지현', '010-1234-5678');
INSERT INTO owner (login_id, password, name, phone)
VALUES ('store0002', 'pass1234', '박민준', '010-5555-7777');
INSERT INTO owner (login_id, password, name, phone)
VALUES ('store0003', 'pass1234', '한나라', '010-6677-8899');

-- playlist (플레이리스트)
INSERT INTO playlist (created_at)
VALUES (NOW());
INSERT INTO playlist (created_at)
VALUES (NOW());
INSERT INTO playlist (created_at)
VALUES (NOW());

-- store_music (음악 설정)
INSERT INTO store_music (platform, played_music, rejected_song_note, playlist_type, tempo,
                         music_mood)
VALUES ('유튜브 뮤직', '인디, 어쿠스틱', null, 'CONSISTENT_MOOD', 'CALM', '아늑하고 조용한');
INSERT INTO store_music (platform, played_music, rejected_song_note, playlist_type, tempo,
                         music_mood)
VALUES ('스포티파이', 'R&B, 팝', null, 'TIME_BASED', 'NORMAL', '세련되고 활기찬');
INSERT INTO store_music (platform, played_music, rejected_song_note, playlist_type, tempo,
                         music_mood)
VALUES ('유튜브 뮤직', '팝, 댄스', null, 'MUSIC_RECOMMENDED', 'LIVELY', '밝고 트렌디한');

-- store (매장)
INSERT INTO store (store_apply_id, owner_id, playlist_id, store_music_id, name, industry, address,
                   customer_age_group, lighting, created_at)
VALUES (1, 1, 1, 1, '카페 공명 홍대점', '카페', '서울 마포구 와우산로17길 11-8', '20대 중반 ~ 30대 초반', 3, NOW());
INSERT INTO store (store_apply_id, owner_id, playlist_id, store_music_id, name, industry, address,
                   customer_age_group, lighting, created_at)
VALUES (2, 2, 2, 2, '더블유오엔 1층', '바/라운지', '서울 마포구 서교동 335-11', '20대 후반 ~ 40대', 2, NOW());
INSERT INTO store (store_apply_id, owner_id, playlist_id, store_music_id, name, industry, address,
                   customer_age_group, lighting, created_at)
VALUES (3, 3, 3, 3, '스모어사이트', '카페', '서울 용산구 한남동 55-9', '20대 초반 ~ 30대', 4, NOW());

-- play_method (재생 방식)
INSERT INTO play_method (store_music_id, method)
VALUES (1, 'BLUETOOTH');
INSERT INTO play_method (store_music_id, method)
VALUES (2, 'BLUETOOTH');
INSERT INTO play_method (store_music_id, method)
VALUES (3, 'WIRED');

-- music_genre (장르 선호/차단)
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (1, 'INDIE', 'LIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (1, 'BALLAD', 'LIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (1, 'HIPHOP', 'DISLIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (2, 'DANCE', 'LIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (2, 'ROCK', 'DISLIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (3, 'DANCE', 'LIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (3, 'HIPHOP', 'LIKE');
INSERT INTO music_genre (store_music_id, genre, preference_type)
VALUES (3, 'CLASSIC', 'DISLIKE');

-- music_time_preference (시간대별 무드 - 카페 공명: 10~22시)
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (1, 10, 12, '잔잔한');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (1, 12, 15, '편안한');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (1, 15, 18, '잔잔한');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (1, 18, 22, '로맨틱');
-- 더블유오엔: 18~02시
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (2, 18, 20, '편안한');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (2, 20, 23, '활기찬');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (2, 23, 24, '신나는');
-- 스모어사이트: 11~21시
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (3, 11, 14, '밝은');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (3, 14, 18, '활기찬');
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood)
VALUES (3, 18, 21, '신나는');

-- store_vibe (분위기)
INSERT INTO store_vibe (store_id, vibe)
VALUES (1, 'CALM');
INSERT INTO store_vibe (store_id, vibe)
VALUES (1, 'NATURAL');
INSERT INTO store_vibe (store_id, vibe)
VALUES (2, 'MODERN');
INSERT INTO store_vibe (store_id, vibe)
VALUES (2, 'DARK');
INSERT INTO store_vibe (store_id, vibe)
VALUES (3, 'MODERN');
INSERT INTO store_vibe (store_id, vibe)
VALUES (3, 'ELEGANT');

-- store_business_hour (영업시간: dayOfWeek 1=월 ~ 7=일)
-- 카페 공명: 매일 10:00-22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 1, '10:00:00', '22:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 2, '10:00:00', '22:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 3, '10:00:00', '22:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 4, '10:00:00', '22:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 5, '10:00:00', '22:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 6, '10:00:00', '23:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (1, 7, '10:00:00', '23:00:00', false);
-- 더블유오엔: 매일 18:00-02:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 1, '18:00:00', '02:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 2, '18:00:00', '02:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 3, '18:00:00', '02:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 4, '18:00:00', '02:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 5, '18:00:00', '03:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 6, '18:00:00', '03:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (2, 7, '18:00:00', '02:00:00', false);
-- 스모어사이트: 월-금 11:00-21:00, 주말 휴무
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 1, '11:00:00', '21:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 2, '11:00:00', '21:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 3, '11:00:00', '21:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 4, '11:00:00', '21:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 5, '11:00:00', '21:00:00', false);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 6, null, null, true);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 7, null, null, true);

-- payment (결제 완료 상태)
INSERT INTO payment (order_id, order_name, amount, status, paid_at, created_at)
VALUES ('mock-order-0001', '인디피_구독_카페 공명 홍대점', 180000, 'DONE', NOW(), NOW());
INSERT INTO payment (order_id, order_name, amount, status, paid_at, created_at)
VALUES ('mock-order-0002', '인디피_구독_더블유오엔 1층', 234000, 'DONE', NOW(), NOW());
INSERT INTO payment (order_id, order_name, amount, status, paid_at, created_at)
VALUES ('mock-order-0003', '인디피_구독_스모어사이트', 180000, 'DONE', NOW(), NOW());

-- store_subscription (구독 정보 - ACTIVE)
INSERT INTO store_subscription (store_id, plan_id, payment_id, start_date, end_date, usage_period,
                                status, created_at)
VALUES (1, 1, 1, '2026-01-12', '2027-01-12', 12, 'ACTIVE', NOW());
INSERT INTO store_subscription (store_id, plan_id, payment_id, start_date, end_date, usage_period,
                                status, created_at)
VALUES (2, 2, 2, '2026-02-03', '2026-08-03', 6, 'ACTIVE', NOW());
INSERT INTO store_subscription (store_id, plan_id, payment_id, start_date, end_date, usage_period,
                                status, created_at)
VALUES (3, 1, 3, '2026-03-18', '2026-09-18', 6, 'ACTIVE', NOW());

-- =====================
-- 플레이리스트 곡 목록
-- 성시경 - 안녕 나의 사랑 (200곡 × 3 플레이리스트)
-- =====================
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1000.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1010.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1020.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1030.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1040.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1050.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1060.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1070.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1080.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1090.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (1, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 2000.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1000.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1010.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1020.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1030.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1040.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1050.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1060.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1070.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1080.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1090.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (2, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 2000.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1000.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1010.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1020.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1030.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1040.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1050.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1060.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1070.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1080.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1090.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1100.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1110.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1120.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1130.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1140.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1150.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1160.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1170.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1180.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1190.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1200.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1210.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1220.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1230.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1240.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1250.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1260.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1270.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1280.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1290.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1300.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1310.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1320.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1330.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1340.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1350.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1360.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1370.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1380.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1390.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1400.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1410.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1420.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1430.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1440.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1450.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1460.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1470.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1480.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1490.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1500.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1510.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1520.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1530.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1540.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1550.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1560.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1570.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1580.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1590.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1600.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1610.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1620.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1630.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1640.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1650.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1660.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1670.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1680.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1690.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1700.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1710.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1720.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1730.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1740.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1750.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1760.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1770.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1780.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1790.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1800.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1810.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1820.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1830.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1840.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1850.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1860.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1870.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1880.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1890.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1900.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1910.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1920.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1930.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1940.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1950.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1960.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1970.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1980.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 1990.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time, title, artist, play_order) VALUES (3, null, false, '5zAEiu3SaO4', 259, '안녕 나의 사랑', '성시경', 2000.0);

