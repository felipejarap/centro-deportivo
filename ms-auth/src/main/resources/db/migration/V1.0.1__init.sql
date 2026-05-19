CREATE TABLE credencial (
    id_credencial BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    id_usuario    BIGINT       NOT NULL,
    activo        BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO credencial (username, password, id_usuario, activo)
VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU9kSVhu',
    -- password es: 'admin123' hasheado con BCrypt
    1,
    true
);