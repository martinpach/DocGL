
insert into Admins(email, first_name, last_name, password, user_name) values('rasto@button.sk', 'rasto', 'button', '5b5c51be5daa8548667254d666379b0a', 'rastobutton');
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization) values('doctor@who.sk', 'doctor', 'who', '00a1cec95d41d45f77f0f280eebbfa3c', 'doctorwho', CURDATE(), 10, 0);
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values('patient@who.cz', 'patient', 'who', '71dfd7af6d2ae6abf10b62b719e6600f', 'patientwho', CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values('anakin@skywalker.com', 'Anakin', 'Skywalker', '71dfd7af6d2ae6abf10b62b719e6600f', 'anakin', CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values('yoda@master.com', 'Yoda', 'Master', '71dfd7af6d2ae6abf10b62b719e6600f', 'yoda', CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values('darth@tyranus.sk', 'Darth', 'Tyranus', '71dfd7af6d2ae6abf10b62b719e6600f', 'tyranus', CURDATE());
insert into Appointments(date, time, note, doctor_id, patient_id) values(CURDATE(), CURTIME(), 'test', 1, 1);