CREATE TABLE rutina_ejercicio (
    id_rutina        BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario       BIGINT       NOT NULL,
    id_entrenador    BIGINT       NOT NULL,
    nombre           VARCHAR(100) NOT NULL,
    descripcion      VARCHAR(255),
    objetivo         VARCHAR(50)  NOT NULL,
    peso_registrado  DOUBLE,
    marca_personal   DOUBLE,
    fecha_asignacion VARCHAR(20)  NOT NULL,
    activa           BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO rutina_ejercicio (id_usuario, id_entrenador, nombre, descripcion, objetivo, peso_registrado, marca_personal, fecha_asignacion, activa)
VALUES
    (1, 1, 'Rutina Hipertrofia A', 'Rutina de volumen para tren superior', 'hipertrofia', 80.5, 100.0, '2024-03-01', true),
    (1, 1, 'Rutina Cardio', 'Cardio HIIT de 30 minutos', 'cardio', NULL, NULL, '2024-03-05', true),
    (2, 1, 'Rutina Fuerza B', 'Rutina de fuerza para tren inferior', 'fuerza', 95.0, 140.0, '2024-03-10', false);