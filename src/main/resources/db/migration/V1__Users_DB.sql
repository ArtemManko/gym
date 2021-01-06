
create sequence hibernate_sequence start 1 increment 1;

create table users (
id int8 not null,
first_name varchar(20),
last_name varchar(20),
username varchar(255) not null,
birthday date,
user_role varchar(50),
user_level varchar(50),
email varchar(50),
phone_number varchar(20),
gender boolean,
password varchar(250) not null,
activation_code varchar(50),
active boolean,
country varchar(30),
city varchar(30),
street varchar(30),
membership_id int8,
primary key (id));


