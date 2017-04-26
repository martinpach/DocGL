$(document).ready(function () {
    var username;
    var password;
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
                url: 'http://localhost:8085/auth/login'
                , type: 'POST'
                , contentType: 'application/json'
                , data: JSON.stringify({
                    "userName": username
                    , "password": password
                })
                , success: function (data) {
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
                        window.location.href = "changepass.html";
                    }
                    else window.location.href = "home.html";
                }
                , error: function () {
                    $("#errorMsg").html("Incorrect username or password.");
                    $("#errorMsg").show();
                }
            });
        }
        else {
            $("#errorMsg").html("Please enter username and password.");
            $("#errorMsg").show();
        }
    }
});