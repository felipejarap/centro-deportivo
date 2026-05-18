CREATE TABLE subscription (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT,
plans_id BIGINT,
end_date DATETIME,
state BOOLEAN NOT NULL
);

INSERT INTO subscription (user_id, plans_id, end_date, state) VALUES
(1, 1, '2026-06-15 23:59:59', true),
(1, 2, '2026-12-31 12:00:00', true),
(1, 3, '2025-05-01 00:00:00', false);