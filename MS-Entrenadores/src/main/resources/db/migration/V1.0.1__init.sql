CREATE TABLE entrenador (
    id_entrenador BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(20)  NOT NULL,
    appaterno     VARCHAR(100) NOT NULL,
    apmaterno     VARCHAR(100),
    especialidad  VARCHAR(100) NOT NULL,
    certificacion VARCHAR(100) NOT NULL
);

INSERT INTO entrenador (nombre, appaterno, apmaterno, especialidad, certificacion)
VALUES ('Eduardo', 'Urquieta', 'Cruz', 'Deporte de contacto', 'certifcado coch');