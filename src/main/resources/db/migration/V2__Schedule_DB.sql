create table schedule_workout (
id int8 not null,
-- client_level varchar(255),
day_of_week varchar(255),
start_time varchar(255),
end_time varchar(255),
-- user_id int8,
primary key (id)
);
--
-- alter table if exists schedule_workout
-- add constraint FKa3f370ysnhsuv62kop2gyum5a
-- foreign key (user_id) references users;