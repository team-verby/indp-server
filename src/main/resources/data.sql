INSERT INTO price_policy (policy_key, amount)
VALUES ('recommendation_fee', 500);
INSERT INTO admin (login_id, password)
VALUES ('admin', 'admin');

-- Plan A
INSERT INTO plan (type, monthly_price)
VALUES ('PLAN_A', 15000);

-- Plan B
INSERT INTO plan (type, monthly_price)
VALUES ('PLAN_B', 39000);

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
VALUES (3, 6, '11:00:00', '21:00:00', true);
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed)
VALUES (3, 7, '11:00:00', '21:00:00', true);

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
-- playlist 1: 카페 공명 홍대점 (인디/팝 카페 분위기)
-- =====================
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'yKNxeF4KMsY', 2690, 'Yellow', 'Coldplay', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'H5v3kku4y6Q', 1670, 'As It Was', 'Harry Styles', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'FOrexgpy_oY', 2340, 'Glimpse of Us', 'Joji', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'mRD0-GxqHVo', 2380, 'Heat Waves', 'Glass Animals', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, '2Vv-BfVoq4g', 2780, 'Perfect', 'Ed Sheeran', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, '4NRXx6pAKkc', 2020, 'Blinding Lights', 'The Weeknd', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'JGwWNGJdvx8', 2640, 'Shape of You', 'Ed Sheeran', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'pB-5XG-DbAA', 1720, 'Stay With Me', 'Sam Smith', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'k4V3Mo61fJM', 2890, 'Fix You', 'Coldplay', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (1, null, false, 'hT_nvWreIhg', 2570, 'Counting Stars', 'OneRepublic', 100.0);

-- playlist 2: 더블유오엔 1층 (팝/R&B 바 분위기)
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'gdZLi9oWNZg', 2230, 'Dynamite', 'BTS', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'b1kbLwvqugk', 2000, 'Anti-Hero', 'Taylor Swift', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'G7KNmW9a75Y', 2000, 'Flowers', 'Miley Cyrus', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'E07s5ZYygMg', 1740, 'Watermelon Sugar', 'Harry Styles', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'TUVcZfQe-Kw', 2030, 'Levitating', 'Dua Lipa', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, '8xg3vE8Ie_E', 2350, 'Love Story', 'Taylor Swift', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'VF-r7qMGEBs', 2340, 'Bad Habit', 'Steve Lacy', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'D1PvIWGrAIs', 2140, 'LILAC', 'IU', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'WMpsOi0lLHs', 1640, 'Butter', 'BTS', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (2, null, false, 'hT_nvWreIhg', 2570, 'Counting Stars', 'OneRepublic', 100.0);

-- playlist 3: 스모어사이트 (팝/댄스 트렌디 카페)
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'OPf0YbXqDm0', 270, 'Uptown Funk', 'Mark Ronson ft. Bruno Mars', 10.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'HCjNJDNzw8Y', 217, 'Havana', 'Camila Cabello', 20.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'fJ9rUzIMcZQ', 355, 'Bohemian Rhapsody', 'Queen', 30.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, '4NRXx6pAKkc', 202, 'Blinding Lights', 'The Weeknd', 40.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'JGwWNGJdvx8', 264, 'Shape of You', 'Ed Sheeran', 50.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'mRD0-GxqHVo', 238, 'Heat Waves', 'Glass Animals', 60.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'b1kbLwvqugk', 200, 'Anti-Hero', 'Taylor Swift', 70.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'gdZLi9oWNZg', 223, 'Dynamite', 'BTS', 80.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, 'G7KNmW9a75Y', 200, 'Flowers', 'Miley Cyrus', 90.0);
INSERT INTO playlist_song (playlist_id, song_recommendation_id, is_recommended, vid, play_time,
                           title, artist, play_order)
VALUES (3, null, false, '2Vv-BfVoq4g', 278, 'Perfect', 'Ed Sheeran', 100.0);
