create database DocGL;

use DocGL;

create table Admins(idadmin INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARCHAR(30),
					PRIMARY KEY (idadmin));

insert into Admins(firstname,lastname,email,username,password) 
values("Rasto", "Button", "rasto@button.sk", "rastobutton", "rastobutton123");					