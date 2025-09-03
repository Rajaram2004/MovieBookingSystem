use MovieBookingSystem;
CREATE TABLE movies (
    movieId BIGINT PRIMARY KEY AUTO_INCREMENT,
    movieName VARCHAR(50) NOT NULL,
    duration INT NOT NULL,
    releaseDate BIGINT NOT NULL,
    language varchar(20),
    genre varchar(20)
);

ALTER TABLE movies
ADD COLUMN isActive BOOLEAN NOT NULL DEFAULT FALSE;

update movies set isActive = 0 where movieId=9;
select * from movies;
INSERT INTO movies (movieName, duration, releaseDate, language, genre) VALUES
("Leo", 165, 1697395200, "Tamil", "Action"),
("RRR", 187, 1648166400, "Telugu", "Historical"),
("Inception", 148, 1279305600, "English", "Thriller"),
("Vikram", 175, 1654214400, "Tamil", "Action"),
("KGF Chapter 2", 168, 1650067200, "Hindi", "Action"),
("Pushpa", 179, 1640131200, "Telugu", "Action"),
("Avengers: Endgame", 181, 1556150400, "English", "Action"),
("Jailer", 170, 1691625600, "Tamil", "Drama"),
("Drishyam 2", 140, 1668643200, "Malayalam", "Thriller"),
("Interstellar", 169, 1415232000, "English", "Drama");

