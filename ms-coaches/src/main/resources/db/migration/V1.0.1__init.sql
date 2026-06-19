CREATE TABLE coach (
    id_coach BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(20)  NOT NULL,
    paternal_surname     VARCHAR(100) NOT NULL,
    maternal_surname     VARCHAR(100),
    specialty  VARCHAR(100) NOT NULL,
    certification VARCHAR(100) NOT NULL
);

INSERT INTO coach (name, paternal_surname, maternal_surname, specialty, certification)
VALUES ('Eduardo', 'Urquieta', 'Cruz', 'Deporte de contacto', 'certifcado coch');