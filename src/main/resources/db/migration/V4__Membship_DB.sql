create table membership (
id int8 not null,
purchase_date timestamp,
duration int4,
price int4,
primary key (id));

alter table if exists users
add constraint FKtg1lp9h3syp6o3bweo0tab6yr
foreign key (membership_id) references membership;