CREATE TABLE asistencia (
    id_asistencia BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    BIGINT      NOT NULL,
    id_classe     BIGINT      NOT NULL,
    hora_llegada  VARCHAR(20) NOT NULL,
    asistio       BOOLEAN     NOT NULL DEFAULT FALSE
);

INSERT INTO asistencia (id_usuario, id_classe, hora_llegada, asistio)
VALUES (1, 1, '08:30', true),
       (1, 2, '09:00', true),
       (2, 1, '08:45', false),
       (2, 2, '10:00', true);