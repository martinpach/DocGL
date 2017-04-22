CREATE DATABASE DocGL;

USE DocGL;

CREATE TABLE Admins(id INTEGER AUTO_INCREMENT,
					first_name VARCHAR(20),
					last_name VARCHAR(30),
					email VARCHAR(50),
					user_name VARCHAR(20),
					password VARBINARY(200),
					password_changed INTEGER DEFAULT 0,  
					PRIMARY KEY (id))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;

CREATE TABLE Users(id INTEGER AUTO_INCREMENT,
					first_name VARCHAR(20),
					last_name VARCHAR(30),
					email VARCHAR(50),
					user_name VARCHAR(20),
					password VARBINARY(200),
					PRIMARY KEY (id))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;
                    
CREATE TABLE Doctors(id INTEGER AUTO_INCREMENT,
					first_name VARCHAR(20),
					last_name VARCHAR(30),
					email VARCHAR(50),
					user_name VARCHAR(20),
					password VARBINARY(200),
					PRIMARY KEY (id))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;

insert into Users(first_name,last_name,email,user_name,password) 
values("User", "Main", "user@se.sk", "user1", AES_ENCRYPT('secret', 'sovy2017'));

insert into Doctors(first_name,last_name,email,user_name,password) 
values("Doctor", "Who", "doctor@who.sk", "who", AES_ENCRYPT('doc123', 'sovy2017'));	

insert into Admins(first_name,last_name,email,user_name,password) 
values("Rasto", "Button", "rasto@button.sk", "rastobutton", AES_ENCRYPT('rastobutton123', 'sovy2017'));