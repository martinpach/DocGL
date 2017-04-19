CREATE DATABASE DocGL;

USE DocGL;

CREATE TABLE Admins(idadmin INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARBINARY(200),
					passwordChanged INTEGER DEFAULT 0,  
					PRIMARY KEY (idadmin))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;

CREATE TABLE Users(iduser INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARBINARY(200),
					PRIMARY KEY (iduser))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;
                    
CREATE TABLE Doctors(iddoctor INTEGER AUTO_INCREMENT,
					firstname VARCHAR(20),
					lastname VARCHAR(30),
					email VARCHAR(50),
					username VARCHAR(20),
					password VARBINARY(200),
					PRIMARY KEY (iddoctor))
                    ENGINE = InnoDB
                    DEFAULT CHARACTER SET = utf8
                    COLLATE = utf8_general_ci;

insert into Users(firstname,lastname,email,username,password) 
values("User", "Main", "user@se.sk", "user1", AES_ENCRYPT('secret', 'sovy2017'));

insert into Doctors(firstname,lastname,email,username,password) 
values("Doctor", "Who", "doctor@who.sk", "who", AES_ENCRYPT('doc123', 'sovy2017'));	

insert into Admins(firstname,lastname,email,username,password) 
values("Rasto", "Button", "rasto@button.sk", "rastobutton", AES_ENCRYPT('rastobutton123', 'sovy2017'));