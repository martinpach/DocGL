
insert into Admins(email, first_name, last_name, password, user_name) values('rasto@button.sk', 'rasto', 'button', '5b5c51be5daa8548667254d666379b0a', 'rastobutton');
insert into Doctors(email, first_name, last_name, password, user_name, registration_date, likes, specialization) values('doctor@who.sk', 'doctor', 'who', '00a1cec95d41d45f77f0f280eebbfa3c', 'doctorwho', CURDATE(), 10, 0);
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values('darth@tyranus.sk', 'Darth', 'Tyranus', '71dfd7af6d2ae6abf10b62b719e6600f', 'tyranus', CURDATE());
insert into Appointments(datetime, note, doctor_id, patient_id) values(CURRENT_TIMESTAMP, 'test', 1, 2);