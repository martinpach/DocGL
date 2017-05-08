$(document).ready(function(){

	var docData = {
		id:localStorage.getItem("id"),
		firstName:localStorage.getItem("firstName"),
		lastName:localStorage.getItem("lastName"),
		email:localStorage.getItem("email"),
		registrationDate:localStorage.getItem("registrationDate"),
		likes:localStorage.getItem("likes"),
		specialization:localStorage.getItem("specialization"),
		phone:localStorage.getItem("phone"),
		city:localStorage.getItem("city"),
		workplace:localStorage.getItem("workplace"),
		userName:localStorage.getItem("user"),
		appointmentsDuration:localStorage.getItem("appointmentsDuration"),
		dateOfValidity:localStorage.getItem("dateOfValidity")
	};

	var ajaxData;

	var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, docData);
    $("#userName").html(html);

    $("#myProfile").on("click", function () {
        $("#container").load('templates/doctor_profile.html', function () {
            var template = "<p>{{userName}}'s</p>";
            var html = Mustache.to_html(template, docData);
            $("#heading").html(html);
            html = Mustache.to_html(templateUsername, docData);
        });
    });

    $("#home").on("click", function () {
        $(this).addClass("selected");
        $("#appointments, #settings").removeClass("selected");
        $("#container").load('templates/doctor_home.html');
    });

    $("#home").trigger("click");

    $("#appointments").on("click", function () {
        $(this).addClass("selected");
        $("#settings, #home").removeClass("selected");
        $("#container").load('templates/doctor_appointments.html');
    });

    $("#settings").on("click", function () {
        $(this).addClass("selected");
        $("#home, #appointments").removeClass("selected");
        $("#container").load('templates/doctor_settings.html');
    });

    $("#logout").on("click", function () {
        var dfd = $.Deferred();
        ajaxRequest('/auth/logout', 'POST').done(function () {
            localStorage.removeItem("token");
            window.location.href = '../index.html';
            dfd.resolve();
        });
        return dfd.promise();
    });


    function ajaxRequest(url, requestType, dataToSend) {
        var dfd = $.Deferred();
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + adminData.token);
            },
            url: 'http://localhost:8085/api' + url,
            type: requestType,
            data: dataToSend,
            contentType: 'application/json',
            success: function (data) {
                if (data != null) ajaxData = data;
                dfd.resolve();
            },
            error: function () {
                dfd.reject();
                console.log("Error");
            }
        });
        return dfd.promise();
    }
});