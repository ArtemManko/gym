create table credentials (
id int8 not null,
create_date timestamp,
password varchar(255),
active boolean,
user_id int8,
primary key (id));

alter table if exists credentials
add constraint FKcbcgksvnqvqxrrc4dwv3qys65
foreign key (user_id) references users;