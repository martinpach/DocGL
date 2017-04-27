
insert into Admins(email, first_name, last_name, password, user_name) values("rasto@button.sk", "rasto", "button", "5b5c51be5daa8548667254d666379b0a", "rastobutton");
insert into Doctors(email, first_name, last_name, password, user_name, registration_date) values("doctor@who.sk", "doctor", "who", "00a1cec95d41d45f77f0f280eebbfa3c", "doctorwho", CURDATE());
insert into Patients(email, first_name, last_name, password, user_name, registration_date) values("patient@who.sk", "patient", "who", "71dfd7af6d2ae6abf10b62b719e6600f", "patientwho", CURDATE());