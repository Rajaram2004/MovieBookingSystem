CREATE TABLE requestTheatre (
    requestId BIGINT PRIMARY KEY AUTO_INCREMENT,
    theatreId BIGINT,
    theatreAdminId BIGINT NOT NULL,
    isApproved BOOLEAN DEFAULT FALSE,
    approvedAdminId BIGINT,
    FOREIGN KEY (theatreId) REFERENCES theatre(theatreId),
    FOREIGN KEY (theatreAdminId) REFERENCES users(id),
    FOREIGN KEY (approvedAdminId) REFERENCES users(id)
);

ALTER TABLE requestTheatre 
MODIFY theatreId BIGINT NULL;

desc requestTheatre;

select * from requestTheatre;