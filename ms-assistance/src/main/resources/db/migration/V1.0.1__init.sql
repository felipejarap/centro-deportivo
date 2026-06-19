CREATE TABLE assistance (
    id_assistance BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user    BIGINT      NOT NULL,
    id_classe     BIGINT      NOT NULL,
    arrival_time  VARCHAR(20) NOT NULL,
    assist       BOOLEAN     NOT NULL DEFAULT FALSE
);

INSERT INTO assistance (id_user, id_classe, arrival_time, assist)
VALUES (1, 1, '08:30', true),
       (1, 2, '09:00', true),
       (2, 1, '08:45', false),
       (2, 2, '10:00', true);