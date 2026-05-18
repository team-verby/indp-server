INSERT IGNORE INTO price_policy (policy_key, amount)
VALUES ('recommendation_fee', 500);

INSERT IGNORE INTO admin (login_id, password)
VALUES ('admin', 'admin');

-- Plan A
INSERT IGNORE INTO plan (type, monthly_price)
VALUES ('PLAN_A', 22000);

-- Plan B
INSERT IGNORE INTO plan (type, monthly_price)
VALUES ('PLAN_B', 33000);

-- discount
INSERT IGNORE INTO plan_discount (plan_id, discount_rate, is_active)
VALUES (1, 60, true);

INSERT IGNORE INTO plan_discount (plan_id, discount_rate, is_active)
VALUES (2, 60, true);
