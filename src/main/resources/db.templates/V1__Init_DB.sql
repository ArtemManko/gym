
create sequence hibernate_sequence start 1 increment 1

create table clients_data_base (
id int8 not null,
active boolean,
first_name varchar(255) not null
last_name varchar(255) not null
birthday date not null
gender boolean,
username varchar(255) not null
password varchar(255) not null
password2 varchar(255) not null
activation_code varchar(255),
email varchar(255) vnot null
phone_number varchar(255) not null
country varchar(255) not null
city varchar(255) not null
street varchar(255) not null
primary key (id))

create table user_role (
user_id int8 not null,
roles varchar(255))

alter table if
exists user_role
-- add constraint user_role_user_fk change after use @onetomany
add constraint FKscdn90ulgcp6gmaijfi4ggvrn
foreign key (user_id)
references clients_data_base