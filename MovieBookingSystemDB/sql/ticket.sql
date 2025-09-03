CREATE TABLE ticket (
    ticketId BIGINT PRIMARY KEY AUTO_INCREMENT,
    userId BIGINT NOT NULL,
    showId BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    ticketBookedDate BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (showId) REFERENCES shows(showId)
);
drop table ticket;

select * from ticket;

INSERT INTO shows (movieId, screenId, theatreId, showDateTime, status)
VALUES ( 1, 1, 1761823200000, 'Active');

INSERT INTO ticket (userId, showId, theatreId, amount, ticketBookedDate, status)
VALUES ( 1, 1, 100, 1756361174565, 'active');

INSERT INTO ticket (userId, showId, theatreId, amount, ticketBookedDate, status)
VALUES (1, 1, 100, 1756361174565, 'active');

INSERT INTO ticket (userId, showId, theatreId, amount, ticketBookedDate, status)
VALUES ( 1, 1, 100, 1756366913809, 'active');

INSERT INTO ticket (userId, showId, theatreId, amount, ticketBookedDate, status)
VALUES ( 1, 1, 100, 1756373611786, 'active');

INSERT INTO ticket (user_id, showId, theatreId, amount, ticketBookedDate, status)
VALUES ( 1, 1, 100, 1756373672916, 'active');

INSERT INTO ticket (userId, showId, theatreId, amount, ticketBookedDate, status)
VALUES ( 1, 1, 300, 1756373731728, 'active');