insert into users (id, active, first_name, last_name , birthday ,gender ,username , password ,
activation_code ,email )
values (1 ,true, 'Artem' , 'Manko' ,'1992-11-11' , true , 'Art92' , 'Art92', null , 'mankoartem@gmail.com' );

insert into user_role (user_id, roles)
values (1,'ADMIN');
