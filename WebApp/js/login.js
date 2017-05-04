$(document).ready(function () {
    var username;
    var password;
    var userType = localStorage.getItem("userType")

    $(document).on('click', '#btn-admin-login', function() {
        localStorage.setItem("userType", "ADMIN");
        window.location.href = "login.html";
    });
    $(document).on('click', '#btn-doctor-login', function() {
        localStorage.setItem("userType", "DOCTOR");
        window.location.href = "login.html";
    });
    $(document).on('click', '#btn-doctor-register', function() {
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
    console.log(userType);
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
                    "userType": userType
                }),
                success: function (data) {
                    if (userType == "ADMIN") {
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
                    if (userType == "DOCTOR") {
                        var doctor = data.doctor;
                        localStorage.setItem("id", doctor.id);
                        localStorage.setItem("firstName", doctor.firstName);
                        localStorage.setItem("lastName", doctor.lastName);
                        localStorage.setItem("email", doctor.email);
                        localStorage.setItem("phone", doctor.phone);
                        localStorage.setItem("userName", doctor.userName);
                        localStorage.setItem("passwordChanged", doctor.passwordChanged);
                        localStorage.setItem("token", data.token);
                        if (doctor.approved == false) {
                            window.location.href = "doctor/approval-wating.html";
                        } else {
                            //window.location.href = "../doctor/home.html"
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
