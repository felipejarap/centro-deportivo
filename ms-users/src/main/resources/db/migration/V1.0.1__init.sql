CREATE TABLE type_user (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO type_user (name) VALUES
('Admin'),
('Cliente'),
('Entrenador');

CREATE TABLE `user` (
    id_user      BIGINT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(20)  NOT NULL UNIQUE,
    paternal_surname    VARCHAR(100) NOT NULL,
    maternal_surname    VARCHAR(100) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone        VARCHAR(20),
    id_type_user BIGINT,
    CONSTRAINT fk_user_type FOREIGN KEY (id_type_user)
        REFERENCES type_user(id)
);

INSERT INTO `user` (username, paternal_surname, maternal_surname, email, phone, id_type_user)
VALUES ('Eduardo','Urquieta','Cruz','ed.urquieta@duocuc.cl','+56912345678', 1);



