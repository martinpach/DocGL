CREATE DATABASE DocGL;

USE DocGL;

CREATE TABLE Admins(idadmin INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARCHAR(30),
					passwordChanged CHAR(1) DEFAULT 'F',  
					PRIMARY KEY (idadmin));

CREATE TABLE Users(iduser INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARCHAR(30),
					PRIMARY KEY (iduser));
                    
CREATE TABLE Doctors(iddoctor INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARCHAR(30),
					PRIMARY KEY (iddoctor));

insert into Users(firstname,lastname,email,username,password) 
values("User", "Main", "user@se.sk", "user1", "secret");

insert into Doctors(firstname,lastname,email,username,password) 
values("Doctor", "Who", "doctor@who.sk", "who", "doc123");	

insert into Admins(firstname,lastname,email,username,password) 
values("Rasto", "Button", "rasto@button.sk", "rastobutton", "rastobutton123");