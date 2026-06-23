CREATE TABLE exercise_routine (
    id_routine       BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user       BIGINT       NOT NULL,
    id_coach    BIGINT       NOT NULL,
    name           VARCHAR(100) NOT NULL,
    description      VARCHAR(255),
    objective         VARCHAR(50)  NOT NULL,
    recorded_weight  DOUBLE,
    personal_brand   DOUBLE,
    assignment_date DATETIME NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO exercise_routine (id_user, id_coach, name, description, objective, recorded_weight, personal_brand, assignment_date, active)
VALUES
    (1, 1, 'Rutina Hipertrofia A', 'Rutina de volumen para tren superior', 'hipertrofia', 80.5, 100.0, '2026-03-01 09:00:00', true),
    (1, 1, 'Rutina Cardio', 'Cardio HIIT de 30 minutos', 'cardio', NULL, NULL, '2026-03-05 10:30:00', true),
    (2, 1, 'Rutina Fuerza', 'Enfoque en Powerlifting', 'fuerza', 90.0, 140.0, '2026-03-10 16:00:00', true);