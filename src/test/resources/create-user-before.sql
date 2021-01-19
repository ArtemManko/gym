delete from users;


insert into users (id, first_name, last_name, username, birthday, user_role, user_level, email, phone_number, gender,
password, activation_code, active, country, city, street )
values (1 , 'Artem' , 'Manko' , 'b', '1992-11-11' , 'ADMIN' ,'BEGINNER', 'mankoartem2@gmail.com',
 '+375292823571', true , '$2a$08$omqo8w2yWd/bouFI/KvT3eoA5CM0O.8PKgPz2zsdRGTEWMHrynQJi' , null, true, 'Belarus' , 'Grodno' , 'Oginscogo' );

