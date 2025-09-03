CREATE TABLE screen (
    screenId BIGINT PRIMARY KEY AUTO_INCREMENT,
    screenName VARCHAR(20) NOT NULL,
    theatreId BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (theatreId) REFERENCES theatre(theatreId)
);
drop table screen;
select * from screen;
INSERT INTO screen (screenName, theatreId, status) VALUES
('Screen 1', 1, 'Active'),
('Screen 2', 1, 'Active'),

('Screen 1', 2, 'Active'),
('Screen 2', 2, 'Active'),

('Screen 1', 3, 'Active'),
('Screen 2', 3, 'Active'),


('Screen 1', 4, 'Active'),
('Screen 2', 4, 'Active'),

('Screen 1', 5, 'Active'),
('Screen 2', 5, 'Active'),

('Screen 1', 6, 'Active'),
('Screen 2', 6, 'Active'),

('Screen 1', 7, 'Active'),
('Screen 2', 7, 'Active'),

('Screen 1', 8, 'Active'),
('Screen 2', 8, 'Active'),

('Screen 1', 9, 'Active'),
('Screen 2', 9, 'Active'),

('Screen 1', 10, 'Active'),
('Screen 2', 10, 'Active');
