CREATE TABLE theatre (
    theatreId BIGINT PRIMARY KEY AUTO_INCREMENT,
    theatreName VARCHAR(50) NOT NULL,
    theatreLocation VARCHAR(60) NOT NULL,
    status VARCHAR(20) NOT NULL
);
DELETE FROM theatre
WHERE theatreId > 10;

select * from theatre;

drop table theatre;
-- Insert dummy data
INSERT INTO theatre (theatreName, theatreLocation, status) VALUES
('PVR Cinemas', 'Chennai', 'ACTIVE'),
('INOX', 'Bangalore', 'ACTIVE'),
('Cinepolis', 'Hyderabad', 'ACTIVE'),
('Sathyam Cinemas', 'Chennai', 'ACTIVE'),
('Escape Cinemas', 'Chennai', 'ACTIVE'),
('AGS Cinemas', 'Chennai', 'ACTIVE'),
('Sangam Cinemas', 'Chennai', 'ACTIVE'),
('Mayajaal', 'Chennai', 'ACTIVE'),
('Luxe Cinemas', 'Chennai', 'ACTIVE'),
('Devi Cinemas', 'Chennai', 'ACTIVE');
