
create sequence hibernate_sequence start 1 increment 1;

create table users (
id int8 not null,
first_name varchar(20),
last_name varchar(20),
username varchar(255) not null,
birthday date,
user_role varchar(255),
user_level varchar(255),
email varchar(255),
phone_number varchar(20),
gender boolean,
password varchar(255) not null,
activation_code varchar(255),
active boolean,
country varchar(30) not null,
city varchar(30) not null,
street varchar(30) not null,
membership_id int8,
primary key (id));


