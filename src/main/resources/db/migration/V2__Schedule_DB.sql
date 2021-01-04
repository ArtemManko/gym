create table schedule_workout (
id int8 not null,
schedule_level varchar(255),
start_end_time varchar(255) not null,
primary key (id));

create table users_schedule_workouts (
user_id int8 not null,
schedule_id int8 not null);

create table schedule_workout_days (
schedule_workout_id int8 not null,
schedule_day varchar(255));

alter table if exists schedule_workout_days
add constraint FKf5kenlsdpcva8f94orlal5gry
foreign key (schedule_workout_id) references schedule_workout;

alter table if exists users_schedule_workouts
add constraint FK72ydo7ipyy04au22synoygsal
foreign key (schedule_id) references schedule_workout;

alter table if exists users_schedule_workouts
add constraint FK67edehvpy5r9bwp5dfpx33f3n
foreign key (user_id) references users;


