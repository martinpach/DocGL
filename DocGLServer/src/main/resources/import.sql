insert into Admins(email, first_name, last_name, password, user_name) values("rasto@button.sk", "rasto", "button", "5b5c51be5daa8548667254d666379b0a", "rastobutton");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("doctor@who.sk", "doctor", "who", "00a1cec95d41d45f77f0f280eebbfa3c", "doctorwho", CURDATE()+ INTERVAL -1 DAY, 10, 0, "0949473196", "Kosice", "Kukucinova 5");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("obi.wan@kenobi.sk", "Obi-Wan", "Kenobi", "00a1cec95d41d45f77f0f280eebbfa3c", "obiwan", CURDATE()+ INTERVAL -1 DAY, 5, 1, "0942223896", "Kosice", "Prazska 8");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("qui.gon@jinn.de", "Qui-Gon", "Jinn", "00a1cec95d41d45f77f0f280eebbfa3c", "jinn", CURDATE()+ INTERVAL -2 DAY, 3, 2, "0945463155", "Presov", "Komenskeho 34");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("r2.d2@doctor.com", "R2", "D2", "00a1cec95d41d45f77f0f280eebbfa3c", "r2d2", CURDATE(), 20, 1, "09568989898", "Kosice", "Zimna 16");
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("patient@who.cz", "patient", "who", "8442e9419e2ad25990864b41865fc9c0", "patientwho", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("anakin@skywalker.com", "Anakin", "Skywalker", "71dfd7af6d2ae6abf10b62b719e6600f", "anakin", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("yoda@master.com", "Yoda", "Master", "71dfd7af6d2ae6abf10b62b719e6600f", "yoda", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("darth@tyranus.sk", "Darth", "Tyranus", "71dfd7af6d2ae6abf10b62b719e6600f", "tyranus", CURDATE());

insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values(CURDATE(), "7:30", "Headache", 1, 2, "Janko", "Hrasko");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values(CURDATE(), "7:50", "Ear ache", 1, 1, "Chuck", "Norris");

-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Dad Joseph", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Brother John", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Friend Mark", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Friend Martin", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Friend Rasto", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Friend Dominika", 1, 1);
-- insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), "Friend Vladimir", 1, 1);

insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, friday_from, friday_to, doctor_id) values( "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", 1);
insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, doctor_id) values( "01:00", "03:00", "01:00", "03:00", "01:00", "05:00", "01:00", "03:00", 1);
insert into Free_Hours(date, from, to, doctor_id) values(CURDATE(), "8:00", "9:00", 1);
insert into Free_Hours(date, from, to, doctor_id) values(CURDATE(), "15:00", "18:00", 1);
insert into favourite_doctors VALUES( 1, 1);
insert into favourite_doctors VALUES( 1, 2);