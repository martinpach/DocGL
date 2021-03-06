$(document).ready(function () {
    var username;
    var password;


    $(document).on('click', '#btn-login', function() {
        window.location.href = "login.html";
    });
    $(document).on('click', '#btn-register', function() {
        window.location.href = "doctor/registration.html";
    });

    $("#username").on("input", function () {
        checkInput($(this));
    });
    $("#username").on("input", function () {
        checkInput($(this));
    });
    $("#loginBtn").on("click", function () {
        logIn();
    });
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            logIn();
        }
    });

    function checkInput(inputField) {
        var input = inputField.val();
        var latestC = input.charAt(input.length - 1)
        if (latestC.match(/^[0-9a-zA-Z_\-.@#]$/) == null) {
            inputField.val(inputField.val().slice(0, -1));
        }
    }

    function logIn() {
        $("#errorMsg").hide();
        username = $("#username").val();
        password = $("#password").val();
        if (username.length > 2 && password.length > 2) {
            $.ajax({
                url: 'http://localhost:8085/api/auth/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    "userName": username,
                    "password": password,
                    "userType": "DOCTOR"
                }),
                success: function (data) {
                    if (data.admin) {
                        var admin = data.admin;
                        localStorage.setItem("id", admin.id);
                        localStorage.setItem("firstName", admin.firstName);
                        localStorage.setItem("lastName", admin.lastName);
                        localStorage.setItem("email", admin.email);
                        localStorage.setItem("userName", admin.userName);
                        localStorage.setItem("passwordChanged", admin.passwordChanged);
                        localStorage.setItem("token", data.token);
                        if (admin.passwordChanged == 0) localStorage.setItem("password", password);
                        if (data.admin.passwordChanged == 0) {
                            window.location.href = "admin/changepass.html";
                        } else window.location.href = "admin/home.html";
                    }
                    else {
                        var doctor = data.doctor;
                        localStorage.setItem("id", doctor.id);
                        localStorage.setItem("firstName", doctor.firstName);
                        localStorage.setItem("lastName", doctor.lastName);
                        localStorage.setItem("email", doctor.email);
                        localStorage.setItem("likes",doctor.likes);
                        localStorage.setItem("specialization",doctor.specialization);
                        localStorage.setItem("phone", doctor.phone);
                        localStorage.setItem("city",doctor.city);
                        localStorage.setItem("workplace",doctor.workplace);
                        localStorage.setItem("userName", doctor.userName);
                        localStorage.setItem("appointmentsDuration",doctor.appointmentsDuration);
                        localStorage.setItem("dateOfValidity",doctor.dateOfValidity);                        
                        localStorage.setItem("token", data.token);
                        if (doctor.blocked == true) {
                        	$("#errorMsg").html("Your account is blocked!");
                    		$("#errorMsg").show();
                        } else if (doctor.approved == false) {
                            window.location.href = "doctor/approval-wating.html";                        
                        } else if(!doctor.workingHoursSet){
                            window.location.href = "doctor/working-hours-first-setup.html"
                        } else {
                            window.location.href = "doctor/home.html"
                        }
                    }                    
                },
                error: function () {
                    $("#errorMsg").html("Incorrect username or password.");
                    $("#errorMsg").show();
                }
            });
        } else {
            $("#errorMsg").html("Please enter username and password.");
            $("#errorMsg").show();
        }
    }
});
