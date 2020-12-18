insert into users (id, active, first_name, last_name , birthday ,gender ,username , password ,
activation_code ,email )
values (1 ,true, 'Artem' , 'Manko' ,'1992-11-11' , true , 'a' , 'a', null , 'mankoartem@gmail.com' );

insert into user_role (user_id, roles)
values (1,'ADMIN');

insert into user_level (user_id, levels)
values (1,'PROFESSIONAL');
--
-- insert into schedule_workout (client_level, user_id, id)
--  values ('Beginner', 1,1);
