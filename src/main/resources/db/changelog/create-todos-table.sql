create table todos (
                       id bigint auto_increment,
                       description varchar(255),
                       deadline date,
                       importance int default(1),
                       status int default(1),
                       user_id bigint not null,
                       primary key (id)
);
alter table todos add constraint FK_users foreign key (user_id) references users (id);