CREATE TABLE reservation (
    id_reservation     BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user     BIGINT NOT NULL,
    id_classe       BIGINT NOT NULL,
    id_coach  BIGINT NOT NULL,
    reservation_date  DATETIME  NOT NULL,
    reservation_status VARCHAR(50)  NOT NULL
);

INSERT INTO reservation (id_user, id_classe, id_coach, reservation_date, reservation_status)
VALUES (1, 1, 1, '2026-05-17 14:00:00', 'Confirmada'),
       (1, 2, 1, '2026-05-18 16:30:00', 'Pendiente'),
       (2, 1, 2, '2026-05-19 09:15:00', 'Cancelada');