CREATE TABLE TheatreAdmin (
    id BIGINT NOT NULL,
    theatreId BIGINT NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id),
    FOREIGN KEY (theatreId) REFERENCES theatre(theatreId)
);
ALTER TABLE TheatreAdmin
ADD CONSTRAINT pk_theatreadmin PRIMARY KEY (id, theatreId);

desc TheatreAdmin;

drop table TheatreAdmin;

select * from TheatreAdmin;

show tables;

insert into TheatreAdmin values(4,1),(5,2),(6,3),(4,4),(5,5),(6,6),(4,7),(5,8),(6,9),(6,10);