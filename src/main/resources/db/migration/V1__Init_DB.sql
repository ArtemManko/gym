
create sequence hibernate_sequence start 1 increment 1;

create table users (
id int8 not null,
active boolean,
first_name varchar(255),
last_name varchar(255),
birthday date,
gender boolean,
username varchar(255),
password varchar(255),
activation_code varchar(255),
email varchar(255),
phone_number varchar(255),
country varchar(255),
city varchar(255),
street varchar(255),
primary key (id)
);
create table user_level (
user_id int8 not null,
levels varchar(255)
);

create table user_role (
user_id int8 not null,
roles varchar(255)
);
alter table if exists user_level
add constraint FKc01x4qr4bey54ybtjj6f9qinn
foreign key (user_id) references users;

alter table if exists user_role
add constraint FKj345gk1bovqvfame88rcx7yyx
foreign key (user_id) references users;