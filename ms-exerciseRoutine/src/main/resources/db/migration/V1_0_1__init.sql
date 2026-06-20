CREATE TABLE exercise_routine (
    id_routine       BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user       BIGINT       NOT NULL,
    id_coach    BIGINT       NOT NULL,
    name           VARCHAR(100) NOT NULL,
    description      VARCHAR(255),
    objective         VARCHAR(50)  NOT NULL,
    recorded_weight  DOUBLE,
    personal_brand   DOUBLE,
    assignment_date VARCHAR(20)  NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO exercise_routine (id_user, id_coach, name, description, objective, recorded_weight, personal_brand, assignment_date, active)
VALUES
    (1, 1, 'Rutina Hipertrofia A', 'Rutina de volumen para tren superior', 'hipertrofia', 80.5, 100.0, '2024-03-01', true),
    (1, 1, 'Rutina Cardio', 'Cardio HIIT de 30 minutos', 'cardio', NULL, NULL, '2024-03-05', true),
    (2, 1, 'Rutina Fuerza B', 'Rutina de fuerza para tren inferior', 'fuerza', 95.0, 140.0, '2024-03-10', false);