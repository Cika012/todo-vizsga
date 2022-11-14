create table todos (
                       id bigint auto_increment,
                       description varchar(255),
                       deadline date,
                       importance varchar(20),
                       status varchar(20),
                       user_id bigint not null,
                       primary key (id)
);
alter table todos add constraint FK_users foreign key (user_id) references users (id);