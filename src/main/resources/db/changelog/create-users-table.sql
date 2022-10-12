create table users (
                       id bigint auto_increment,
                       user_email varchar(255) unique,
                       primary key (id)
);