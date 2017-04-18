$(document).ready(function () {


    $("#submit").on('click', function () {
        var newPass = $("#newPassword").val();
        var confirmPass = $("#confirmPassword").val();
        var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])");

        console.log(localStorage.getItem("idadmin"));
        if (newPass == "") {
            $("#changePassMessage").toggleClass("errorMessage");
            $("#changePassMessage").text("Please type new password!");
        } else
        if (regex.test(newPass)) {
            if (newPass == confirmPass) {
                $("#changePassMessage").text("");
                $.ajax({
                    url: 'http://localhost:8080/admins/' + localStorage.getItem("idadmin") + '/profile/password',
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
                        $("#changePassMessage").toggleClass("errorMessage");
                        $("#changePassMessage").html("Error! Password was not changed!");

                    }

                });

            } else {
                $("#changePassMessage").toggleClass("errorMessage");
                $("#changePassMessage").text("Passwords are not same!");
            }
        } else {
            $("#changePassMessage").toggleClass("errorMessage");
            $("#changePassMessage").text("Password must contain at: upper case letter, lower case letter and number.");
        }
    });

});
