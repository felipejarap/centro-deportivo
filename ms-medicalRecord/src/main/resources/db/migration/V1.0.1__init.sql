CREATE TABLE medical_record (
    id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    allergy VARCHAR(255),
    disease VARCHAR(255),
    medical_center VARCHAR(255),
    user_id BIGINT
);

INSERT INTO medical_record (allergy, disease, medical_center, user_id) VALUES
('Aspirina', 'Hipotiroidismo', 'Hospital Clínico UC', 1);