CREATE DATABASE MovieBookingSystem;
drop DATABASE MovieBookingSystem;
use MovieBookingSystem;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    emailId VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    phoneNumber BIGINT UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('customer', 'theatreAdmin', 'admin') NOT NULL,
    timeZone VARCHAR(50),
    balance DOUBLE DEFAULT 0.0,
    active BOOLEAN
);
DROP TABLE users;
DELETE FROM users;
TRUNCATE TABLE users;
SELECT * FROM users;
show tables;
show databases;
INSERT INTO users (name, emailId, phoneNumber, password, timeZone, role, balance, active)
VALUES
('User 1', 'user1@example.com', 987650001, 'pass123', 'Asia/Kolkata', 'customer', 2000, 1),
('User 2', 'user2@example.com', 987650002, 'pass456', 'Asia/Kolkata', 'customer', 20000, 1),
('User 3', 'user3@example.com', 987650003, 'pass789', 'Asia/Kolkata', 'customer', 20000, 1),

('theatreAdmin One', 'theatreadmin1@example.com', 987650004, 'pass123', 'Asia/Kolkata', 'theatreAdmin', 0, 1),
('theatreAdmin Two', 'theatreadmin2@example.com', 987650005, 'pass456', 'Asia/Kolkata', 'theatreAdmin', 0, 1),
('theatreAdmin Three', 'theatreadmin3@example.com', 987650006, 'pass789', 'Asia/Kolkata', 'theatreAdmin', 0, 1),

('Admin One', 'admin1@example.com', 987650007, 'pass123', 'Asia/Kolkata', 'admin', 0, 1),
('Admin Two', 'admin2@example.com', 987650008, 'pass456', 'Asia/Kolkata', 'admin', 0, 1),
('Admin Three', 'admin3@example.com', 987650009, 'pass789', 'Asia/Kolkata', 'admin', 0, 1);




 
 
 

