CREATE TABLE reserva (
    id_reserva     BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario     BIGINT NOT NULL,
    id_clase       BIGINT NOT NULL,
    id_entrenador  BIGINT NOT NULL,
    fecha_reserva  VARCHAR(20)  NOT NULL,
    estado_reserva VARCHAR(50)  NOT NULL
);

INSERT INTO reserva (id_usuario, id_clase, id_entrenador, fecha_reserva, estado_reserva)
VALUES (1, 1, 1, '2026-05-17', 'Confirmada'),
       (1, 2, 1, '2026-05-18', 'Pendiente'),
       (2, 1, 2, '2026-05-19', 'Cancelada');