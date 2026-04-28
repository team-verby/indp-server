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

-- ===========================
-- Owner (19)
-- ===========================
INSERT INTO owner (login_id, password, name, phone) VALUES
('store1',  'password', '김밀키',  '010-1234-5678'),
('store2',  'password', '김선유',  '010-1234-5678'),
('store3',  'password', '김목동',  '010-1234-5678'),
('store4',  'password', '김문어',  '010-1234-5678'),
('store5',  'password', '김스모',  '010-1234-5678'),
('store6',  'password', '김일상',  '010-1234-5678'),
('store7',  'password', '김샤로',  '010-1234-5678'),
('store8',  'password', '김서대',  '010-1234-5678'),
('store9',  'password', '김용산',  '010-1234-5678'),
('store10', 'password', '김발산',  '010-1234-5678'),
('store11', 'password', '김강남',  '010-1234-5678'),
('store12', 'password', '김동래',  '010-1234-5678'),
('store13', 'password', '김전포',  '010-1234-5678'),
('store14', 'password', '김마곡',  '010-1234-5678'),
('store15', 'password', '김망원',  '010-1234-5678'),
('store16', 'password', '김신사',  '010-1234-5678'),
('store17', 'password', '김연남',  '010-1234-5678'),
('store18', 'password', '김홍대',  '010-1234-5678'),
('store19', 'password', '김페어',  '010-1234-5678');

-- ===========================
-- Store Apply (19)
-- ===========================
INSERT INTO store_apply (applicant_name, applicant_phone, created_at) VALUES
('김밀키', '010-1234-5678', NOW()),
('김선유', '010-1234-5678', NOW()),
('김목동', '010-1234-5678', NOW()),
('김문어', '010-1234-5678', NOW()),
('김스모', '010-1234-5678', NOW()),
('김일상', '010-1234-5678', NOW()),
('김샤로', '010-1234-5678', NOW()),
('김서대', '010-1234-5678', NOW()),
('김용산', '010-1234-5678', NOW()),
('김발산', '010-1234-5678', NOW()),
('김강남', '010-1234-5678', NOW()),
('김동래', '010-1234-5678', NOW()),
('김전포', '010-1234-5678', NOW()),
('김마곡', '010-1234-5678', NOW()),
('김망원', '010-1234-5678', NOW()),
('김신사', '010-1234-5678', NOW()),
('김연남', '010-1234-5678', NOW()),
('김홍대', '010-1234-5678', NOW()),
('김페어', '010-1234-5678', NOW());

-- ===========================
-- Store Music (19) - 공통: 유튜브뮤직, 인디/어쿠스틱, MUSIC_RECOMMENDED, NORMAL, 거부=댄스곡
-- ===========================
INSERT INTO store_music (platform, played_music, playlist_type, tempo, rejected_song_note) VALUES
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡'),
('유튜브 뮤직', '인디, 어쿠스틱', 'MUSIC_RECOMMENDED', 'NORMAL', '댄스곡');

-- ===========================
-- Play Method - BLUETOOTH (store_music_id 1~19)
-- ===========================
INSERT INTO play_method (store_music_id, method) VALUES
(1, 'BLUETOOTH'), (2, 'BLUETOOTH'), (3, 'BLUETOOTH'), (4, 'BLUETOOTH'),
(5, 'BLUETOOTH'), (6, 'BLUETOOTH'), (7, 'BLUETOOTH'), (8, 'BLUETOOTH'),
(9, 'BLUETOOTH'), (10, 'BLUETOOTH'), (11, 'BLUETOOTH'), (12, 'BLUETOOTH'),
(13, 'BLUETOOTH'), (14, 'BLUETOOTH'), (15, 'BLUETOOTH'), (16, 'BLUETOOTH'),
(17, 'BLUETOOTH'), (18, 'BLUETOOTH'), (19, 'BLUETOOTH');

-- ===========================
-- Music Genre - DISLIKE: CLASSIC, CHILDREN (store_music_id 1~19)
-- ===========================
INSERT INTO music_genre (store_music_id, genre, preference_type) VALUES
(1,  'CLASSIC',  'DISLIKE'), (1,  'CHILDREN', 'DISLIKE'),
(2,  'CLASSIC',  'DISLIKE'), (2,  'CHILDREN', 'DISLIKE'),
(3,  'CLASSIC',  'DISLIKE'), (3,  'CHILDREN', 'DISLIKE'),
(4,  'CLASSIC',  'DISLIKE'), (4,  'CHILDREN', 'DISLIKE'),
(5,  'CLASSIC',  'DISLIKE'), (5,  'CHILDREN', 'DISLIKE'),
(6,  'CLASSIC',  'DISLIKE'), (6,  'CHILDREN', 'DISLIKE'),
(7,  'CLASSIC',  'DISLIKE'), (7,  'CHILDREN', 'DISLIKE'),
(8,  'CLASSIC',  'DISLIKE'), (8,  'CHILDREN', 'DISLIKE'),
(9,  'CLASSIC',  'DISLIKE'), (9,  'CHILDREN', 'DISLIKE'),
(10, 'CLASSIC',  'DISLIKE'), (10, 'CHILDREN', 'DISLIKE'),
(11, 'CLASSIC',  'DISLIKE'), (11, 'CHILDREN', 'DISLIKE'),
(12, 'CLASSIC',  'DISLIKE'), (12, 'CHILDREN', 'DISLIKE'),
(13, 'CLASSIC',  'DISLIKE'), (13, 'CHILDREN', 'DISLIKE'),
(14, 'CLASSIC',  'DISLIKE'), (14, 'CHILDREN', 'DISLIKE'),
(15, 'CLASSIC',  'DISLIKE'), (15, 'CHILDREN', 'DISLIKE'),
(16, 'CLASSIC',  'DISLIKE'), (16, 'CHILDREN', 'DISLIKE'),
(17, 'CLASSIC',  'DISLIKE'), (17, 'CHILDREN', 'DISLIKE'),
(18, 'CLASSIC',  'DISLIKE'), (18, 'CHILDREN', 'DISLIKE'),
(19, 'CLASSIC',  'DISLIKE'), (19, 'CHILDREN', 'DISLIKE');

-- ===========================
-- Store Music Time Preference (mood=NULL, 1시간 단위)
-- openHour: open_time의 minute>0 이면 hour-1, 아니면 hour
-- closeHour: close_time의 minute>0 이면 hour+1, 아니면 hour
-- 자정 넘는 매장(9~13)은 23시까지로 처리
-- 1: 07:30~18:00 → 6~18  | 2: 10:00~23:00 → 10~23 | 3: 10:00~23:00 → 10~23
-- 4: 11:00~23:00 → 11~23 | 5: 08:00~21:30 → 8~22   | 6: 10:00~21:30 → 10~22
-- 7: 11:30~22:00 → 10~22 | 8: 11:30~22:00 → 10~22  | 9~13: 11:30~익일 → 10~23
-- 14: 07:00~22:00 → 7~22 | 15: 10:00~22:00 → 10~22 | 16: 11:00~23:00 → 11~23
-- 17: 10:00~22:00 → 10~22| 18: 10:00~22:00 → 10~22 | 19: 11:00~19:00 → 11~19
-- ===========================
INSERT INTO store_music_time_preference (store_music_id, start_time_hour, end_time_hour, mood) VALUES
-- 1: 6~17
(1,6,7,NULL),(1,7,8,NULL),(1,8,9,NULL),(1,9,10,NULL),(1,10,11,NULL),(1,11,12,NULL),
(1,12,13,NULL),(1,13,14,NULL),(1,14,15,NULL),(1,15,16,NULL),(1,16,17,NULL),(1,17,18,NULL),
-- 2: 10~22
(2,10,11,NULL),(2,11,12,NULL),(2,12,13,NULL),(2,13,14,NULL),(2,14,15,NULL),(2,15,16,NULL),
(2,16,17,NULL),(2,17,18,NULL),(2,18,19,NULL),(2,19,20,NULL),(2,20,21,NULL),(2,21,22,NULL),(2,22,23,NULL),
-- 3: 10~22
(3,10,11,NULL),(3,11,12,NULL),(3,12,13,NULL),(3,13,14,NULL),(3,14,15,NULL),(3,15,16,NULL),
(3,16,17,NULL),(3,17,18,NULL),(3,18,19,NULL),(3,19,20,NULL),(3,20,21,NULL),(3,21,22,NULL),(3,22,23,NULL),
-- 4: 11~22
(4,11,12,NULL),(4,12,13,NULL),(4,13,14,NULL),(4,14,15,NULL),(4,15,16,NULL),(4,16,17,NULL),
(4,17,18,NULL),(4,18,19,NULL),(4,19,20,NULL),(4,20,21,NULL),(4,21,22,NULL),(4,22,23,NULL),
-- 5: 8~21
(5,8,9,NULL),(5,9,10,NULL),(5,10,11,NULL),(5,11,12,NULL),(5,12,13,NULL),(5,13,14,NULL),
(5,14,15,NULL),(5,15,16,NULL),(5,16,17,NULL),(5,17,18,NULL),(5,18,19,NULL),(5,19,20,NULL),
(5,20,21,NULL),(5,21,22,NULL),
-- 6: 10~21
(6,10,11,NULL),(6,11,12,NULL),(6,12,13,NULL),(6,13,14,NULL),(6,14,15,NULL),(6,15,16,NULL),
(6,16,17,NULL),(6,17,18,NULL),(6,18,19,NULL),(6,19,20,NULL),(6,20,21,NULL),(6,21,22,NULL),
-- 7: 10~21
(7,10,11,NULL),(7,11,12,NULL),(7,12,13,NULL),(7,13,14,NULL),(7,14,15,NULL),(7,15,16,NULL),
(7,16,17,NULL),(7,17,18,NULL),(7,18,19,NULL),(7,19,20,NULL),(7,20,21,NULL),(7,21,22,NULL),
-- 8: 10~21
(8,10,11,NULL),(8,11,12,NULL),(8,12,13,NULL),(8,13,14,NULL),(8,14,15,NULL),(8,15,16,NULL),
(8,16,17,NULL),(8,17,18,NULL),(8,18,19,NULL),(8,19,20,NULL),(8,20,21,NULL),(8,21,22,NULL),
-- 9: 10~03 (자정 넘음, max close 03:00)
(9,10,11,NULL),(9,11,12,NULL),(9,12,13,NULL),(9,13,14,NULL),(9,14,15,NULL),(9,15,16,NULL),
(9,16,17,NULL),(9,17,18,NULL),(9,18,19,NULL),(9,19,20,NULL),(9,20,21,NULL),(9,21,22,NULL),(9,22,23,NULL),
(9,23,0,NULL),(9,0,1,NULL),(9,1,2,NULL),(9,2,3,NULL),
-- 10: 10~03 (자정 넘음, max close 03:00)
(10,10,11,NULL),(10,11,12,NULL),(10,12,13,NULL),(10,13,14,NULL),(10,14,15,NULL),(10,15,16,NULL),
(10,16,17,NULL),(10,17,18,NULL),(10,18,19,NULL),(10,19,20,NULL),(10,20,21,NULL),(10,21,22,NULL),(10,22,23,NULL),
(10,23,0,NULL),(10,0,1,NULL),(10,1,2,NULL),(10,2,3,NULL),
-- 11: 10~03 (자정 넘음, max close 03:00)
(11,10,11,NULL),(11,11,12,NULL),(11,12,13,NULL),(11,13,14,NULL),(11,14,15,NULL),(11,15,16,NULL),
(11,16,17,NULL),(11,17,18,NULL),(11,18,19,NULL),(11,19,20,NULL),(11,20,21,NULL),(11,21,22,NULL),(11,22,23,NULL),
(11,23,0,NULL),(11,0,1,NULL),(11,1,2,NULL),(11,2,3,NULL),
-- 12: 10~04 (자정 넘음, close 04:00)
(12,10,11,NULL),(12,11,12,NULL),(12,12,13,NULL),(12,13,14,NULL),(12,14,15,NULL),(12,15,16,NULL),
(12,16,17,NULL),(12,17,18,NULL),(12,18,19,NULL),(12,19,20,NULL),(12,20,21,NULL),(12,21,22,NULL),(12,22,23,NULL),
(12,23,0,NULL),(12,0,1,NULL),(12,1,2,NULL),(12,2,3,NULL),(12,3,4,NULL),
-- 13: 10~06 (자정 넘음, close 06:00)
(13,10,11,NULL),(13,11,12,NULL),(13,12,13,NULL),(13,13,14,NULL),(13,14,15,NULL),(13,15,16,NULL),
(13,16,17,NULL),(13,17,18,NULL),(13,18,19,NULL),(13,19,20,NULL),(13,20,21,NULL),(13,21,22,NULL),(13,22,23,NULL),
(13,23,0,NULL),(13,0,1,NULL),(13,1,2,NULL),(13,2,3,NULL),(13,3,4,NULL),(13,4,5,NULL),(13,5,6,NULL),
-- 14: 7~21
(14,7,8,NULL),(14,8,9,NULL),(14,9,10,NULL),(14,10,11,NULL),(14,11,12,NULL),(14,12,13,NULL),
(14,13,14,NULL),(14,14,15,NULL),(14,15,16,NULL),(14,16,17,NULL),(14,17,18,NULL),(14,18,19,NULL),
(14,19,20,NULL),(14,20,21,NULL),(14,21,22,NULL),
-- 15: 10~21
(15,10,11,NULL),(15,11,12,NULL),(15,12,13,NULL),(15,13,14,NULL),(15,14,15,NULL),(15,15,16,NULL),
(15,16,17,NULL),(15,17,18,NULL),(15,18,19,NULL),(15,19,20,NULL),(15,20,21,NULL),(15,21,22,NULL),
-- 16: 11~22
(16,11,12,NULL),(16,12,13,NULL),(16,13,14,NULL),(16,14,15,NULL),(16,15,16,NULL),(16,16,17,NULL),
(16,17,18,NULL),(16,18,19,NULL),(16,19,20,NULL),(16,20,21,NULL),(16,21,22,NULL),(16,22,23,NULL),
-- 17: 10~21
(17,10,11,NULL),(17,11,12,NULL),(17,12,13,NULL),(17,13,14,NULL),(17,14,15,NULL),(17,15,16,NULL),
(17,16,17,NULL),(17,17,18,NULL),(17,18,19,NULL),(17,19,20,NULL),(17,20,21,NULL),(17,21,22,NULL),
-- 18: 10~21
(18,10,11,NULL),(18,11,12,NULL),(18,12,13,NULL),(18,13,14,NULL),(18,14,15,NULL),(18,15,16,NULL),
(18,16,17,NULL),(18,17,18,NULL),(18,18,19,NULL),(18,19,20,NULL),(18,20,21,NULL),(18,21,22,NULL),
-- 19: 11~18
(19,11,12,NULL),(19,12,13,NULL),(19,13,14,NULL),(19,14,15,NULL),(19,15,16,NULL),(19,16,17,NULL),
(19,17,18,NULL),(19,18,19,NULL);

-- ===========================
-- Playlist (19)
-- ===========================
INSERT INTO playlist (created_at) VALUES
(NOW()), (NOW()), (NOW()), (NOW()), (NOW()),
(NOW()), (NOW()), (NOW()), (NOW()), (NOW()),
(NOW()), (NOW()), (NOW()), (NOW()), (NOW()),
(NOW()), (NOW()), (NOW()), (NOW());

-- ===========================
-- Store (19)
-- lighting: 3000 (3000K)
-- ===========================
INSERT INTO store (store_apply_id, owner_id, playlist_id, store_music_id, name, industry, address, customer_age_group, lighting, status, created_at) VALUES
(1,  1,  1,  1,  '밀키 현대 GRC점',           '카페 / 커피숍',            '경기도 성남시 분당구 분당수서로 477 4층',                              '전연령대', 3000, 'ACTIVE', NOW()),
(2,  2,  2,  2,  '서울볼더스 선유',             '클라이밍 / 스포츠센터',    '서울특별시 영등포구 양평로28마길 7 3층',                              '전연령대', 3000, 'ACTIVE', NOW()),
(3,  3,  3,  3,  '서울볼더스 클라이밍 목동점',  '클라이밍 / 스포츠센터',    '서울특별시 양천구 신목로 53 2층',                                    '전연령대', 3000, 'ACTIVE', NOW()),
(4,  4,  4,  4,  '석문어 서대문점',             '레스토랑 / 일반음식점',    '서울특별시 서대문구 통일로9안길 36-5',                               '전연령대', 3000, 'ACTIVE', NOW()),
(5,  5,  5,  5,  '스모어사이트',                '카페 / 커피숍',            '대전 유성구 학하로 98 1층 101~104호',                                '전연령대', 3000, 'ACTIVE', NOW()),
(6,  6,  6,  6,  '일상의아도',                  '카페 / 커피숍',            '충남 천안시 서북구 늘푸른3길 7-1 103호',                             '전연령대', 3000, 'ACTIVE', NOW()),
(7,  7,  7,  7,  '정숙성 샤로수길 본점',        '레스토랑 / 일반음식점',    '서울특별시 관악구 남부순환로226길 31 1층',                           '전연령대', 3000, 'ACTIVE', NOW()),
(8,  8,  8,  8,  '정숙성 서대문점',             '레스토랑 / 일반음식점',    '서울특별시 서대문구 경기대로 84 101호',                              '전연령대', 3000, 'ACTIVE', NOW()),
(9,  9,  9,  9,  '주도락 신용산본점',           '레스토랑 / 일반음식점',    '서울특별시 용산구 한강대로46길 19 1층',                              '전연령대', 3000, 'ACTIVE', NOW()),
(10, 10, 10, 10, '주도락 마곡발산점',           '레스토랑 / 일반음식점',    '서울특별시 강서구 마곡중앙6로 85 2층 202호',                         '전연령대', 3000, 'ACTIVE', NOW()),
(11, 11, 11, 11, '주도락 강남점',               '레스토랑 / 일반음식점',    '서울특별시 강남구 강남대로96길 18 1층 101호',                        '전연령대', 3000, 'ACTIVE', NOW()),
(12, 12, 12, 12, '주도락 부산동래점',           '레스토랑 / 일반음식점',    '부산 동래구 명륜로139번길 45 1층',                                   '전연령대', 3000, 'ACTIVE', NOW()),
(13, 13, 13, 13, '주도락 전포점',               '레스토랑 / 일반음식점',    '부산 부산진구 전포대로210번길 23 1층',                               '전연령대', 3000, 'ACTIVE', NOW()),
(14, 14, 14, 14, '카페 공명 마곡책빵',          '카페 / 커피숍',            '서울특별시 강서구 마곡중앙로 105-7 K스퀘어 타워 3 1층 101, 102, 103호', '전연령대', 3000, 'ACTIVE', NOW()),
(15, 15, 15, 15, '카페 공명 망원책빵',          '카페 / 커피숍',            '서울특별시 마포구 월드컵로13길 22-3',                                '전연령대', 3000, 'ACTIVE', NOW()),
(16, 16, 16, 16, '카페 공명 신사 가로수길점',   '카페 / 커피숍',            '서울특별시 강남구 도산대로15길 32-4',                                '전연령대', 3000, 'ACTIVE', NOW()),
(17, 17, 17, 17, '카페 공명 연남점',            '카페 / 커피숍',            '서울특별시 마포구 연희로 11 1층',                                    '전연령대', 3000, 'ACTIVE', NOW()),
(18, 18, 18, 18, '카페 공명 홍대점',            '카페 / 커피숍',            '서울특별시 마포구 와우산로17길 11-8',                                '전연령대', 3000, 'ACTIVE', NOW()),
(19, 19, 19, 19, '페어링페어드',                '카페 / 커피숍',            '경기도 성남시 분당구 백현로144번길 22 1층',                           '전연령대', 3000, 'ACTIVE', NOW());

-- ===========================
-- Store Vibe (19 - 모두 CALM / 차분한)
-- ===========================
INSERT INTO store_vibe (store_id, vibe) VALUES
(1,  'CALM'), (2,  'CALM'), (3,  'CALM'), (4,  'CALM'), (5,  'CALM'),
(6,  'CALM'), (7,  'CALM'), (8,  'CALM'), (9,  'CALM'), (10, 'CALM'),
(11, 'CALM'), (12, 'CALM'), (13, 'CALM'), (14, 'CALM'), (15, 'CALM'),
(16, 'CALM'), (17, 'CALM'), (18, 'CALM'), (19, 'CALM');

-- ===========================
-- Store Business Hour
-- day_of_week: 1=월 2=화 3=수 4=목 5=금 6=토 7=일
-- 자정 넘는 영업시간(25시→01:00, 27시→03:00 등)은 익일 시각으로 저장
-- ===========================

-- 1. 밀키 현대 GRC점 | 월~금 07:30~18:00 | 토,일 휴무
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(1, 1, '07:30', '18:00', false), (1, 2, '07:30', '18:00', false),
(1, 3, '07:30', '18:00', false), (1, 4, '07:30', '18:00', false),
(1, 5, '07:30', '18:00', false), (1, 6, NULL, NULL, true), (1, 7, NULL, NULL, true);

-- 2. 서울볼더스 선유 | 월~금 12:00~23:00 | 토~일 10:00~20:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(2, 1, '12:00', '23:00', false), (2, 2, '12:00', '23:00', false),
(2, 3, '12:00', '23:00', false), (2, 4, '12:00', '23:00', false),
(2, 5, '12:00', '23:00', false), (2, 6, '10:00', '20:00', false), (2, 7, '10:00', '20:00', false);

-- 3. 서울볼더스 클라이밍 목동점 | 월~금 10:00~23:00 | 토~일 10:00~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(3, 1, '10:00', '23:00', false), (3, 2, '10:00', '23:00', false),
(3, 3, '10:00', '23:00', false), (3, 4, '10:00', '23:00', false),
(3, 5, '10:00', '23:00', false), (3, 6, '10:00', '22:00', false), (3, 7, '10:00', '22:00', false);

-- 4. 석문어 서대문점 | 월~금 11:00~23:00 | 토,일 휴무
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(4, 1, '11:00', '23:00', false), (4, 2, '11:00', '23:00', false),
(4, 3, '11:00', '23:00', false), (4, 4, '11:00', '23:00', false),
(4, 5, '11:00', '23:00', false), (4, 6, NULL, NULL, true), (4, 7, NULL, NULL, true);

-- 5. 스모어사이트 | 월~금 08:00~21:30 | 토~일 10:00~21:30
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(5, 1, '08:00', '21:30', false), (5, 2, '08:00', '21:30', false),
(5, 3, '08:00', '21:30', false), (5, 4, '08:00', '21:30', false),
(5, 5, '08:00', '21:30', false), (5, 6, '10:00', '21:30', false), (5, 7, '10:00', '21:30', false);

-- 6. 일상의아도 | 월~금 10:00~21:30 | 토 10:00~21:00 | 일 휴무
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(6, 1, '10:00', '21:30', false), (6, 2, '10:00', '21:30', false),
(6, 3, '10:00', '21:30', false), (6, 4, '10:00', '21:30', false),
(6, 5, '10:00', '21:30', false), (6, 6, '10:00', '21:00', false), (6, 7, NULL, NULL, true);

-- 7. 정숙성 샤로수길 본점 | 월~일 11:30~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(7, 1, '11:30', '22:00', false), (7, 2, '11:30', '22:00', false),
(7, 3, '11:30', '22:00', false), (7, 4, '11:30', '22:00', false),
(7, 5, '11:30', '22:00', false), (7, 6, '11:30', '22:00', false), (7, 7, '11:30', '22:00', false);

-- 8. 정숙성 서대문점 | 월~토 11:30~22:00 | 일 휴무
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(8, 1, '11:30', '22:00', false), (8, 2, '11:30', '22:00', false),
(8, 3, '11:30', '22:00', false), (8, 4, '11:30', '22:00', false),
(8, 5, '11:30', '22:00', false), (8, 6, '11:30', '22:00', false), (8, 7, NULL, NULL, true);

-- 9. 주도락 신용산본점 | 월~목 11:30~01:00(익일) | 금~일 11:30~03:00(익일)
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(9, 1, '11:30', '01:00', false), (9, 2, '11:30', '01:00', false),
(9, 3, '11:30', '01:00', false), (9, 4, '11:30', '01:00', false),
(9, 5, '11:30', '03:00', false), (9, 6, '11:30', '03:00', false), (9, 7, '11:30', '03:00', false);

-- 10. 주도락 마곡발산점 | 월~화 ~01:00 | 수~목 ~02:00 | 금~토 ~03:00 | 일 12:00~00:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(10, 1, '11:30', '01:00', false), (10, 2, '11:30', '01:00', false),
(10, 3, '11:30', '02:00', false), (10, 4, '11:30', '02:00', false),
(10, 5, '11:30', '03:00', false), (10, 6, '11:30', '03:00', false), (10, 7, '12:00', '00:00', false);

-- 11. 주도락 강남점 | 월~목 ~01:00 | 금~토 ~03:00 | 일 ~02:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(11, 1, '11:30', '01:00', false), (11, 2, '11:30', '01:00', false),
(11, 3, '11:30', '01:00', false), (11, 4, '11:30', '01:00', false),
(11, 5, '11:30', '03:00', false), (11, 6, '11:30', '03:00', false), (11, 7, '11:30', '02:00', false);

-- 12. 주도락 부산동래점 | 월~일 11:30~04:00(익일, 28시)
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(12, 1, '11:30', '04:00', false), (12, 2, '11:30', '04:00', false),
(12, 3, '11:30', '04:00', false), (12, 4, '11:30', '04:00', false),
(12, 5, '11:30', '04:00', false), (12, 6, '11:30', '04:00', false), (12, 7, '11:30', '04:00', false);

-- 13. 주도락 전포점 | 월~일 11:30~06:00(익일, 30시)
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(13, 1, '11:30', '06:00', false), (13, 2, '11:30', '06:00', false),
(13, 3, '11:30', '06:00', false), (13, 4, '11:30', '06:00', false),
(13, 5, '11:30', '06:00', false), (13, 6, '11:30', '06:00', false), (13, 7, '11:30', '06:00', false);

-- 14. 카페 공명 마곡책빵 | 월~금 07:00~22:00 | 토~일 10:00~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(14, 1, '07:00', '22:00', false), (14, 2, '07:00', '22:00', false),
(14, 3, '07:00', '22:00', false), (14, 4, '07:00', '22:00', false),
(14, 5, '07:00', '22:00', false), (14, 6, '10:00', '22:00', false), (14, 7, '10:00', '22:00', false);

-- 15. 카페 공명 망원책빵 | 월~일 10:00~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(15, 1, '10:00', '22:00', false), (15, 2, '10:00', '22:00', false),
(15, 3, '10:00', '22:00', false), (15, 4, '10:00', '22:00', false),
(15, 5, '10:00', '22:00', false), (15, 6, '10:00', '22:00', false), (15, 7, '10:00', '22:00', false);

-- 16. 카페 공명 신사 가로수길점 | 월~일 11:00~23:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(16, 1, '11:00', '23:00', false), (16, 2, '11:00', '23:00', false),
(16, 3, '11:00', '23:00', false), (16, 4, '11:00', '23:00', false),
(16, 5, '11:00', '23:00', false), (16, 6, '11:00', '23:00', false), (16, 7, '11:00', '23:00', false);

-- 17. 카페 공명 연남점 | 월~일 10:00~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(17, 1, '10:00', '22:00', false), (17, 2, '10:00', '22:00', false),
(17, 3, '10:00', '22:00', false), (17, 4, '10:00', '22:00', false),
(17, 5, '10:00', '22:00', false), (17, 6, '10:00', '22:00', false), (17, 7, '10:00', '22:00', false);

-- 18. 카페 공명 홍대점 | 월~일 10:00~22:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(18, 1, '10:00', '22:00', false), (18, 2, '10:00', '22:00', false),
(18, 3, '10:00', '22:00', false), (18, 4, '10:00', '22:00', false),
(18, 5, '10:00', '22:00', false), (18, 6, '10:00', '22:00', false), (18, 7, '10:00', '22:00', false);

-- 19. 페어페어드 | 월~수 휴무 | 목~금 11:00~18:00 | 토 11:00~19:00 | 일 11:00~18:00
INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time, is_closed) VALUES
(19, 1, NULL, NULL, true),  (19, 2, NULL, NULL, true), (19, 3, NULL, NULL, true),
(19, 4, '11:00', '18:00', false), (19, 5, '11:00', '18:00', false),
(19, 6, '11:00', '19:00', false), (19, 7, '11:00', '18:00', false);

-- ===========================
-- Payment (19)
-- PLAN_B 33,000원 x 60% 할인 = 13,200원
-- status=DONE, paid_at=신청일(2026-04-27)
-- ===========================
INSERT INTO payment (order_id, order_name, total_amount, balance_amount, payment_key, status, paid_at, created_at) VALUES
('order-store-01',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-01',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-02',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-02',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-03',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-03',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-04',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-04',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-05',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-05',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-06',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-06',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-07',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-07',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-08',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-08',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-09',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-09',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-10',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-10',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-11',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-11',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-12',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-12',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-13',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-13',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-14',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-14',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-15',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-15',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-16',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-16',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-17',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-17',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-18',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-18',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00'),
('order-store-19',  '인디피뮤직 프리미엄 1개월', 13200, 13200, 'paymentKey-store-19',  'DONE', '2026-04-27 00:00:00', '2026-04-27 00:00:00');

-- ===========================
-- Store Subscription (19)
-- plan_id=2 (PLAN_B/프리미엄), payment_id=1~19
-- status=ACTIVE
-- ===========================
INSERT INTO store_subscription (store_id, plan_id, payment_id, start_date, end_date, usage_period, status, created_at) VALUES
(1,  2, 1,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(2,  2, 2,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(3,  2, 3,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(4,  2, 4,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(5,  2, 5,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(6,  2, 6,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(7,  2, 7,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(8,  2, 8,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(9,  2, 9,  '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(10, 2, 10, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(11, 2, 11, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(12, 2, 12, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(13, 2, 13, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(14, 2, 14, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(15, 2, 15, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(16, 2, 16, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(17, 2, 17, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(18, 2, 18, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW()),
(19, 2, 19, '2026-04-21', '2026-05-18', 1, 'ACTIVE', NOW());

-- ===========================
-- Store Photo (19 - 각 매장 대표 이미지 1장)
-- URL: /images/stores/{store_id}.png (Spring Boot static resource)
-- ===========================
INSERT INTO store_photo (store_id, image_url, sort_order, is_main) VALUES
(1,  '/images/stores/1.png',  1, true),
(2,  '/images/stores/2.png',  1, true),
(3,  '/images/stores/3.png',  1, true),
(4,  '/images/stores/4.png',  1, true),
(5,  '/images/stores/5.png',  1, true),
(6,  '/images/stores/6.png',  1, true),
(7,  '/images/stores/7.png',  1, true),
(8,  '/images/stores/8.png',  1, true),
(9,  '/images/stores/9.png',  1, true),
(10, '/images/stores/10.png', 1, true),
(11, '/images/stores/11.png', 1, true),
(12, '/images/stores/12.png', 1, true),
(13, '/images/stores/13.png', 1, true),
(14, '/images/stores/14.png', 1, true),
(15, '/images/stores/15.png', 1, true),
(16, '/images/stores/16.png', 1, true),
(17, '/images/stores/17.png', 1, true),
(18, '/images/stores/18.png', 1, true),
(19, '/images/stores/19.png', 1, true);

