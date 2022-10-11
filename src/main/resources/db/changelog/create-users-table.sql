create table users (
                       id bigint auto_increment,
                       userEmail varchar(255) unique,
                       primary key (id)
);