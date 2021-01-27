insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (1 , 'Artem' , 'Manko' , 'aaa', '1992-11-11' , 'ADMIN' ,'BEGINNER', 'mankoartem2@gmail.com',
 '+375292823571', true , 'aaa' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (2 , 'Artur' , 'Coach_1' , 'bbb', '1992-11-11' , 'COACH' ,'BEGINNER', 'coach_artur_bbb@gmail.com',
 '+375292823572', true , 'bb' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

 insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (3 , 'Koly' , 'Coach_2' , 'ccc', '1992-11-11' , 'COACH' ,'BEGINNER', 'coach_koly_ccc@gmail.com',
 '+375292823571', true , 'ccc' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (4 , 'Igor' , 'Client_1' , 'ddd', '1992-11-11' , 'CLIENT' ,'BEGINNER', 'client_igor_ddd2@gmail.com',
 '+375292823571', true , 'ddd' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

 insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (5 , 'Lena' , 'Client_2' , 'eee', '1992-11-11' , 'CLIENT' ,'BEGINNER', 'client_elena_eee@gmail.com',
 '+375292823571', true , 'eee' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

ALTER SEQUENCE hibernate_sequence RESTART WITH 6;
