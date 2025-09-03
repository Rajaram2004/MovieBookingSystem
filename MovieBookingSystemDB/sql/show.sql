use MovieBookingSystem;
CREATE TABLE shows(
    showId BIGINT PRIMARY KEY AUTO_INCREMENT,
    movieId BIGINT NOT NULL,
    screenId BIGINT NOT NULL,
    theatreId BIGINT NOT NULL,
    showDateTime BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (movieId) REFERENCES movies(movieId),
    FOREIGN KEY (screenId) REFERENCES screen(screenId),
    FOREIGN KEY (theatreId) REFERENCES theatre(theatreId)
);
select * from shows;
DELETE FROM shows
WHERE showId > 3;

show tables;

INSERT INTO shows (movieId, screenId, theatreId, showDateTime, status) VALUES
(1, 1, 1, 1767235200000, 'Active'),  -- 01 Jan 2026 10:00:00 UTC
(2, 2, 1, 1767249600000, 'Active'),  -- 01 Jan 2026 14:00:00 UTC
(3, 3, 2, 1767264000000, 'Active'); -- 01 Jan 2026 18:00:00 UTC

INSERT INTO shows (movieId, screenId, theatreId, showDateTime, status) VALUES
(1, 1, 1, 1756469855, 'Active');

update shows set status='active' where showId=4;
update shows set showDateTime=1756469855000 where showId=4;
