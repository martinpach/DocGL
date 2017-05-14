insert into Admins(email, first_name, last_name, password, user_name) values("rasto@button.sk", "rasto", "button", "5b5c51be5daa8548667254d666379b0a", "rastobutton");

insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace, validity_date, appointments_duration, working_hours_set, approved) values("doctor@who.sk", "doctor", "who", "00a1cec95d41d45f77f0f280eebbfa3c", "doctorwho", CURDATE(), 10, 0, "0949473196", "Kosice", "Kukucinova 5", "2017-05-08", 20, true, true);
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace, validity_date, appointments_duration, working_hours_set, approved) values("obi.wan@kenobi.sk", "Obi-Wan", "Kenobi", "00a1cec95d41d45f77f0f280eebbfa3c", "obiwan", CURDATE()+ INTERVAL -1 DAY, 5, 1, "0942223896", "Kosice", "Prazska 8", "2017-05-15", 20, true, true);
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("qui.gon@jinn.de", "Qui-Gon", "Jinn", "00a1cec95d41d45f77f0f280eebbfa3c", "jinn", CURDATE()+ INTERVAL -2 DAY, 3, 2, "0945463155", "Presov", "Komenskeho 34");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization, phone, city, workplace) values("r2.d2@doctor.com", "R2", "D2", "00a1cec95d41d45f77f0f280eebbfa3c", "r2d2", CURDATE(), 20, 1, "09568989898", "Kosice", "Zimna 16");

insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("patient@who.cz", "patient", "who", "8442e9419e2ad25990864b41865fc9c0", "patientwho", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("anakin@skywalker.com", "Anakin", "Skywalker", "71dfd7af6d2ae6abf10b62b719e6600f", "anakin", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("yoda@master.com", "Yoda", "Master", "71dfd7af6d2ae6abf10b62b719e6600f", "yoda", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("darth@tyranus.sk", "Darth", "Tyranus", "71dfd7af6d2ae6abf10b62b719e6600f", "tyranus", CURDATE());

insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name, canceled) values("2017-05-24", "7:30:00", "Headache", 1, 1, "Janko", "Hrasko", true);
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "10:10:00", "Headache", 1, 3, "Juraj", "Janosik");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "11:30:00", "Headache", 1, 3, "Tyroone", "Bigdik");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-19", "9:30:00", "Ear ache", 1, 2, "Chuck", "Norris");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-06-21", "16:00:00", "Error 404 leg is missing.", 2, 2, "Lucia", "Norrisova");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-07-03", "13:40:00", "Leprosy", 1, 1, "Rocky", "Balboa");
insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-07-07", "10:50:00", "Cancer", 2, 1, "King", "Arthur");

-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "7:30:00", "Headache", 1, 3, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "10:30:00", "Headache", 1, 1, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "10:50:00", "Headache", 1, 2, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "11:10:00", "Headache", 1, 1, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "13:00:00", "Headache", 1, 2, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "13:20:00", "Headache", 1, 2, "Juraj", "Janosik");
-- insert into Appointments(date, time, note, doctor_id, patient_id, patient_first_name, patient_last_name) values("2017-05-24", "13:40:00", "Headache", 1, 2, "Juraj", "Janosik");

insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, friday_from, friday_to, doctor_id) values( "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", "07:30", "12:00", 1);
insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, doctor_id) values( "13:00", "15:00", "13:00", "15:00", "13:00", "17:00", "13:00", "15:00", 1);
insert into Free_Hours(date, time_from, time_to, doctor_id) values("2017-05-24", "8:00:00", "10:00:00", 1);
insert into Free_Hours(date, time_from, time_to, doctor_id) values("2017-05-24", "14:10:00", "17:00:00", 1);
insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, doctor_id) values( "07:30", "12:00", "07:30", "12:00", "07:30", "12:10", "09:00", "12:00", 2);
insert into Working_Hours(monday_from, monday_to, tuesday_from, tuesday_to, wednesday_from, wednesday_to, thursday_from, thursday_to, doctor_id) values( "13:00", "15:00", "13:00", "15:00", "13:00", "17:00", "13:00", "15:00", 2);
insert into favourite_doctors VALUES( 1, 1);
insert into favourite_doctors VALUES( 1, 2);
insert into favourite_doctors VALUES( 2, 1);

-- PUBLIC HOLIDAYS 2017-2018
insert into Public_Holidays(date) VALUES("2017-01-01");
insert into Public_Holidays(date) VALUES("2017-01-06");
insert into Public_Holidays(date) VALUES("2017-04-14");
insert into Public_Holidays(date) VALUES("2017-04-17");
insert into Public_Holidays(date) VALUES("2017-05-01");
insert into Public_Holidays(date) VALUES("2017-05-08");
insert into Public_Holidays(date) VALUES("2017-07-05");
insert into Public_Holidays(date) VALUES("2017-08-29");
insert into Public_Holidays(date) VALUES("2017-09-01");
insert into Public_Holidays(date) VALUES("2017-09-15");
insert into Public_Holidays(date) VALUES("2017-11-01");
insert into Public_Holidays(date) VALUES("2017-11-17");
insert into Public_Holidays(date) VALUES("2017-12-24");
insert into Public_Holidays(date) VALUES("2017-12-25");
insert into Public_Holidays(date) VALUES("2017-12-26");

insert into Public_Holidays(date) VALUES("2018-01-01");
insert into Public_Holidays(date) VALUES("2018-01-06");
insert into Public_Holidays(date) VALUES("2018-03-30");
insert into Public_Holidays(date) VALUES("2018-04-02");
insert into Public_Holidays(date) VALUES("2018-05-01");
insert into Public_Holidays(date) VALUES("2018-05-08");
insert into Public_Holidays(date) VALUES("2018-07-05");
insert into Public_Holidays(date) VALUES("2018-08-29");
insert into Public_Holidays(date) VALUES("2018-09-01");
insert into Public_Holidays(date) VALUES("2018-09-15");
insert into Public_Holidays(date) VALUES("2018-11-01");
insert into Public_Holidays(date) VALUES("2018-11-17");
insert into Public_Holidays(date) VALUES("2018-12-24");
insert into Public_Holidays(date) VALUES("2018-12-25");
insert into Public_Holidays(date) VALUES("2018-12-26");
