create table plans(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE  NOT NULL,
    duration_days INT NOT NULL
);

INSERT INTO plans (name, price, duration_days) VALUES
('Basico', 15000.00, 7),
('Normal', 25000.00, 25),
('Anual', 50000.00, 365);