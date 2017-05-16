$(document).ready(function() {
	var ajaxData;

	fillSpecializations ();

	$(document).on('blur', "#specialization", function () {
		console.log("ahoj");
        if ($("#specialization :selected").text() != "Choose Specialization") {
            $("#specialization").addClass('select-checked');
        } else $("#specialization").removeClass('select-checked');
    });

	$.validator.setDefaults({
		submitHandler: function() {
			registerDoctor ()
		}
	});

	$("#registration-form").validate({
		rules: {
			firstname: "required",
			lastname: "required",
			username: {
				required:true,
				minlength:3
			},
			workplace: "required",
			city: "required",
			specialization: "required",
			email: {
				required: true,
				email: true
			},
			phone: {
				required: true,
				number: true
			},
			password: {
				required: true,
				minlength: 6,
				passwordRestrictions: true
			},
			confirm_password: {
				required: true,
				minlength: 6,
				equalTo: "#password"
			},
		},
		messages: {
			firstname: "Please enter your firstname",
			lastname: "Please enter your lastname",
			username: "Please enter username (3 characters at least)",
			workplace: "Please enter a workplace location",
			city: "Please enter a City",
			specialization: "Please choose your specialization",
			email: "Please enter a valid email address",
			phone: "Please enter your phone number",
			password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 6 characters long"
			},
			confirm_password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 6 characters long",
				equalTo: "Please enter the same password as above"
			},
		}
	});

	/*Method for password validation*/
	$.validator.addMethod("passwordRestrictions", function(value, element) {
		return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\/\^&\*])/.test(value);
	}, 'Password must contains lowercase, uppercase, number and special character');

	/*Function for fill select options with specializations*/
	function fillSpecializations () {
		var dfd = $.Deferred();
		ajaxRequest('/doctors/specializations', "GET").done(function () {
			for (var i = 0; i < ajaxData.length; i++) {
	            var text = ajaxData[i].toLowerCase().replace(/\b[a-z]/g, function (letter) {
                    return letter.toUpperCase();
                });
	            $('<option />', {
	                "class": 'specialization'
	            }).text(text).appendTo("#specialization").val(text.toLowerCase().slice(0, 4));
	        }
	        dfd.resolve();
		});
		return dfd.promise();
	}

	/*Function that register new doctor*/
	function registerDoctor () {
		var dfd = $.Deferred();
		var dataToSend = JSON.stringify({
            "firstName": $("#firstname").val(),
		    "lastName": $("#lastname").val(),
		    "email": $("#email").val(),
		    "userType": "DOCTOR",
		    "userName": $("#username").val(),
		    "password": $("#password").val(),
		    "specialization": $("#specialization option:selected").text().toUpperCase(),
		    "phone": $("#phone").val(),
		    "city": $("#city").val(),
		    "workplace": $("#workplace").val()
        });
		ajaxRequest('/auth/registration', "POST", dataToSend).done(function () {
			var doctor = ajaxData.doctor;
            localStorage.setItem("id", doctor.id);
            localStorage.setItem("firstName", doctor.firstName);
            localStorage.setItem("lastName", doctor.lastName);
            localStorage.setItem("email", doctor.email);
            localStorage.setItem("phone", doctor.phone);
            localStorage.setItem("city", doctor.city);
            localStorage.setItem("workplace", doctor.workplace);
            localStorage.setItem("userName", doctor.userName);
            localStorage.setItem("passwordChanged", doctor.passwordChanged);
            localStorage.setItem("token", ajaxData.token);
            window.location.href = "approval-wating.html";
	        dfd.resolve();
		});
		return dfd.promise();
	}
	

	function ajaxRequest(ajaxUrl, requestType, dataToSend) {
        var dfd = $.Deferred();
        $.ajax({
            url: 'http://localhost:8085/api' + ajaxUrl,
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