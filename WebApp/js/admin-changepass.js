$(document).ready(function () {
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            submit();
        }
    });
    $("#submit").on('click', function () {
        submit();
    });

    function submit() {
        var newPass = $("#newPassword").val();
        var confirmPass = $("#confirmPassword").val();
        var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])");
        var token = 'Bearer ' + localStorage.getItem("token");
        if (newPass == "") {
            $("#changePassMessage").addClass("errorMessage");
            $("#changePassMessage").text("Please type new password!");
        } else
        if (newPass.length > 5) {
            if (regex.test(newPass)) {
                $("#changePassMessage").text("");
                if (newPass == confirmPass) {
                    $("#changePassMessage").text("");
                    if (newPass != localStorage.getItem("password")) {
                        $.ajax({
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader('Authorization', token);
                            },
                            url: 'http://localhost:8085/api/admins/' + localStorage.getItem("id") + '/profile/password',
                            type: 'PUT',
                            contentType: 'application/json',
                            data: JSON.stringify({
                                "password": newPass
                            }),
                            success: function (data) {
                                $("#changePassMessage").toggleClass("successMessage");
                                $("#changePassMessage").html("Password was changed.");
                                window.location.href = "home.html";
                            },
                            error: function () {
                                $("#changePassMessage").addClass("errorMessage");
                                $("#changePassMessage").html("Error! Password was not changed!");
                            }
                        });
                    } else {
                        $("#changePassMessage").addClass("errorMessage");
                        $("#changePassMessage").text("Password cannot be same as your old password!")
                    }
                } else {
                    $("#changePassMessage").addClass("errorMessage");
                    $("#changePassMessage").text("Passwords are not same!");
                }
            } else {
                $("#changePassMessage").addClass("errorMessage");
                $("#changePassMessage").text("Password must contain at least one upper case letter,one lower case letter, one special character and a number.");
            }
        } else {
            $("#changePassMessage").addClass("errorMessage");
            $("#changePassMessage").text("Password must be of minimum 6 characters length.");
        }
    }
});
