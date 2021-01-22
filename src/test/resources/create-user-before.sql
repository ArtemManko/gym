delete from users_schedule_workouts;
delete from schedule_workout_days;
delete from users;
delete from schedule_workout;
delete from membership;

insert into membership (id , purchase_date , duration , price , payment_id , active )
values (2 , null , 3 , 25 , 'PAYID-MAD7XRQ30R08908KS341031T' , null);

insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street, membership_id)
values (1 , 'Artem' , 'Manko' , 'bbb', '1992-11-11' , 'ADMIN' ,'BEGINNER', 'mankoartem2@gmail.com',
 '+375292823571', true , 'bbb' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' , 2);

insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street, membership_id)
values (4 , 'Katy' , 'Manko' , 'kkk', '1992-11-11' , 'COACH' ,'BEGINNER', 'mankoartem2@gmail.com2',
 '+375292823571', true , 'kkk' , '123', true, 'Belarus' , 'Grodno' , 'Oginscogo' , null);

insert into schedule_workout ( id , schedule_level , start_end_time)
values (3 , 'BEGINNER' , '12:00-13:30');

insert into users_schedule_workouts ( user_id , schedule_id )
values (4 , 3);

insert into schedule_workout_days ( schedule_workout_id , schedule_day )
values (3 , 'MONDAY');


