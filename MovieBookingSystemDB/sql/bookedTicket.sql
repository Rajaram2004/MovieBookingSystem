CREATE TABLE bookedTicket (
    ticketId BIGINT NOT NULL,
    bookingSeatId BIGINT NOT NULL,
    FOREIGN KEY (ticketId) REFERENCES ticket(ticketId),
    FOREIGN KEY (bookingSeatId) REFERENCES bookingSeat(bookingSeatId)
);
drop table bookedTicket;


select * from bookedTicket;
