INSERT INTO price_policy (policy_key, amount) VALUES ('recommendation_fee', 500);
INSERT INTO admin (login_id, password) VALUES ('admin', 'admin');

-- Plan A
INSERT INTO plan (type, subtitle, description, monthly_price) VALUES ('PLAN_A', '기본 요금제', '플레이리스트 큐레이션 전용 플랜', 15000);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (1, '매장 맞춤형 플레이리스트 제공', false, 1);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (1, '월 4회 업데이트', false, 2);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (1, '결과 보고서 제공', false, 3);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (1, '공연권료 포함', false, 4);

-- Plan B
INSERT INTO plan (type, subtitle, description, monthly_price) VALUES ('PLAN_B', '프리미엄 요금제', '플레이리스트 큐레이션 + 추천곡 전용 플랜', 39000);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (2, '매장 맞춤형 플레이리스트 제공', false, 1);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (2, '월 4회 업데이트', false, 2);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (2, '결과 보고서 제공', false, 3);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (2, '공연권료 포함', false, 4);
INSERT INTO plan_feature (plan_id, feature_label, is_highlighted, sort_order) VALUES (2, '추천곡 기능 제공', true, 5);
