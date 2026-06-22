CREATE TABLE assistance (
    id_assistance BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user    BIGINT      NOT NULL,
    id_classe     BIGINT      NOT NULL,
    arrival_time  DATETIME  NOT NULL,
    assist       BOOLEAN     NOT NULL DEFAULT FALSE
);

INSERT INTO assistance (id_user, id_classe, arrival_time, assist)
VALUES (1, 1, '2026-06-22 08:30:00', true),
       (1, 2, '2026-06-22 09:00:00', true),
       (2, 1, '2026-06-22 08:45:00', false);