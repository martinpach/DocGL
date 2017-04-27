
insert into Admins(email, first_name, last_name, password, user_name) values("rasto@button.sk", "rasto", "button", "5b5c51be5daa8548667254d666379b0a", "rastobutton");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date) values("doctor@who.sk", "doctor", "who", "00a1cec95d41d45f77f0f280eebbfa3c", "doctorwho", CURDATE());
insert into Doctors(email, first_name, last_name, password, user_name, registration_date) values("obi.wan@kenobi.sk", "Obi-Wan", "Kenobi", "00a1cec95d41d45f77f0f280eebbfa3c", "obiwan", CURDATE());
insert into Doctors(email, first_name, last_name, password, user_name, registration_date) values("qui.gon@jinn.de", "Qui-Gon", "Jinn", "00a1cec95d41d45f77f0f280eebbfa3c", "jinn", CURDATE());
insert into Doctors(email, first_name, last_name, password, user_name, registration_date) values("r2.d2@doctor.com", "R2", "D2", "00a1cec95d41d45f77f0f280eebbfa3c", "r2d2", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("patient@who.cz", "patient", "who", "71dfd7af6d2ae6abf10b62b719e6600f", "patientwho", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("anakin@skywalker.com", "Anakin", "Skywalker", "71dfd7af6d2ae6abf10b62b719e6600f", "anakin", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("yoda@master.com", "Yoda", "Master", "71dfd7af6d2ae6abf10b62b719e6600f", "yoda", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("darth@tyranus.sk", "Darth", "Tyranus", "71dfd7af6d2ae6abf10b62b719e6600f", "tyranus", CURDATE());
insert into Doctor_details(likes, specialization, doctor_id) values(12, "Dentist", 1);
insert into Doctor_details(likes, specialization, doctor_id) values(35, "Jedi Knight", 2);
insert into Doctor_details(likes, specialization, doctor_id) values(75, "Jedi Master", 3);
insert into Doctor_details(likes, specialization, doctor_id) values(55, "Engineer", 4);
