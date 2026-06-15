INSERT IGNORE INTO price_policy (policy_key, amount)
VALUES ('recommendation_fee', 500);

INSERT IGNORE INTO admin (login_id, password)
VALUES ('admin', 'admin');

-- Plan A
INSERT IGNORE INTO plan (type, monthly_price)
VALUES ('PLAN_A', 4400);

-- Plan B
INSERT IGNORE INTO plan (type, monthly_price)
VALUES ('PLAN_B', 22000);

-- Plan C
INSERT IGNORE INTO plan (type, monthly_price)
VALUES ('PLAN_C', 33000);

-- discount
INSERT IGNORE INTO plan_discount (plan_id, discount_rate, is_active)
SELECT plan_id, 60, true FROM plan WHERE type = 'PLAN_B';

INSERT IGNORE INTO plan_discount (plan_id, discount_rate, is_active)
SELECT plan_id, 60, true FROM plan WHERE type = 'PLAN_C';
