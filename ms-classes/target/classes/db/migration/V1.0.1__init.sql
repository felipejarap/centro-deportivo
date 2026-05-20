create table type_classes(
    id bigint auto_increment primary key,
    name varchar(255) not null
);

insert into type_classes (name) values
("Futbol"),
("Basketball"),
("Tennis");


create table classe(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    maximum_capacity INT NOT NULL,
    spots_available INT NOT NULL,
    id_type_classe BIGINT

);

ALTER TABLE classe ADD CONSTRAINT fk_classe_type_classe FOREIGN KEY (id_type_classe) REFERENCES type_classes(id);

INSERT INTO classe (start_date, end_date, maximum_capacity, spots_available, id_type_classe) VALUES
('2026-05-10 09:00:00', '2026-05-10 10:30:00', 20, 20, 1),
('2026-05-11 15:00:00', '2026-05-11 16:30:00', 15, 15, 2),
('2026-05-12 18:00:00', '2026-05-12 19:00:00', 10, 10, 3);