$(document).ready(function() {

    var docData = {
        id: localStorage.getItem("id"),
        firstName: localStorage.getItem("firstName"),
        lastName: localStorage.getItem("lastName"),
        email: localStorage.getItem("email"),
        registrationDate: localStorage.getItem("registrationDate"),
        likes: localStorage.getItem("likes"),
        specialization: localStorage.getItem("specialization"),
        phone: localStorage.getItem("phone"),
        city: localStorage.getItem("city"),
        workplace: localStorage.getItem("workplace"),
        userName: localStorage.getItem("userName"),
        appointmentsDuration: localStorage.getItem("appointmentsDuration"),
        dateOfValidity: localStorage.getItem("dateOfValidity"),
        appointmentCount: 0,
        token: localStorage.getItem("token")
    };

    console.log(docData);

    var ajaxData;
    var appointments;
    var appointmentsToday;
    var freeHours;

    var today = new Date();
    var dateToday = today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
    var time= today.getHours()+":"+today.getMinutes()+":"+today.getSeconds();

    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, docData);
    $("#userName").html(html);


    $("#myProfile").on("click", function() {
        $("#container").load('templates/doctor_profile.html', function() {
            var template = "<p>{{userName}}'s</p>";
            var html = Mustache.to_html(template, docData);
            $("#heading").html(html);
            $("#username").html(docData.userName);
            $("#firstname").html(docData.firstName);
            $("#lastname").html(docData.lastName);
            $("#spec").html(docData.specialization);
            $("#phone").html(docData.phone);
            $("#email").html(docData.email);
            $("#city").html(docData.city);
            $("#workplace").html(docData.workplace);
            $("#password").html("***********");
        });
    });

    $("#changePassword").on("click",function(){
        $("#container").load('templates/doctor_password.html');
    });

    $("#home").on("click", function() {
        $(this).addClass("selected");
        $("#appointments, #settings").removeClass("selected");
        $("#container").load('templates/doctor_home.html',function(){
            var template = "{{id}}";
            var html = Mustache.to_html(template, docData);
            $("#likesCount").html(docData.likes);
        });
        getTodaysAppointmentsInfo();
        getAllAppointmentsInfo();  
    });


    $("#home").trigger("click");

    $("#appointments").on("click", function() {
        $(this).addClass("selected");
        $("#settings, #home").removeClass("selected");
        $("#container").load('templates/doctor_appointments.html',function(){
            var radioBtn = $('input[type=radio][name=time_period]');

            $(radioBtn).change(function() {
                if ($('#day-input').val() != "") {
                    if (this.value == "day") {
                        $('#contentTab').empty();
                        showDayAppointments($('#day-input').val());
                    }
                    if (this.value == "week") {
                        $('#contentTab').empty();
                        showWeeklyAppointments();
                    }
                }
            });
            $('#day-input').change(function() {
                if ($('input[type=radio][name=time_period]:checked').val() == "day") {
                    $('#contentTab').empty();
                    showDayAppointments($('#day-input').val());
                }
                if ($('input[type=radio][name=time_period]:checked').val() == "week") {
                    $('#contentTab').empty();
                    showWeeklyAppointments();
                }
            });

            function showDayAppointments(date) {
                ajaxRequest("/doctors/" + localStorage.getItem("id") + "/appointments?date=" + date, 'GET').done(function() {
                    appointments=ajaxData;
                    $(".appointment-day").remove();
                    for (var i = 0; i < appointments.length; i++) {
                        var appointmentBox = $('<div class="appointment-day"></div>');
                        var appointmentTimeBox = $('<div class="time"></div>');
                        var appointmentPatientBox = $('<div class="patient"></div>');
                        var appointmentNoteBox = $('<p class="note"></p>');
                        var btnCancelAppointment = $('<button type="button" class="btn btn-danger btn-sm btn-cancel-appointment" id="' + appointments[i].id + '">Cancel</button>');
                        var colMd4 = $('<div class="col-md-4"></div>');
                        appointmentTimeBox.text((appointments[i].time).substr(0, 5));
                        appointmentPatientBox.text((appointments[i].patientFirstName == "" ? appointments[i].patient.firstName : appointments[i].patientFirstName) + " " + (appointments[i].patientLastName == "" ? appointments[i].patient.lastName : appointments[i].patientLastName));
                        appointmentNoteBox.text(appointments[i].note);
                        appointmentBox.append(appointmentTimeBox, appointmentPatientBox, appointmentNoteBox, btnCancelAppointment);
                        appointmentBox.appendTo(colMd4);
                        colMd4.appendTo('#contentTab');
                    }
                });
            }

            function showWeeklyAppointments() {
                ajaxRequest("/doctors/" + localStorage.getItem("id") + "/appointments?date=" + $('#day-input').val() + "&timePeriod=week", 'GET').done(function() {
                    appointments=ajaxData;
                    $(".appointment-day").remove();
                    for (var i = 0; i < appointments.length; i++) {
                        var appointmentBox = $('<div class="appointment-week"></div>').css('cursor', 'pointer');
                        var appointmentDateBox = $('<div class="date"></div>');
                        var appointmentCountBox = $('<div class="num-of-appointments"></div>');
                        var colMd4 = $('<div class="col-md-4"></div>');
                        appointmentDateBox.text(appointments[i].date);
                        appointmentCountBox.text("Number of appointments: " + appointments[i].appointments);
                        appointmentBox.append(appointmentDateBox, appointmentCountBox);
                        appointmentBox.appendTo(colMd4);
                        colMd4.appendTo('#contentTab');
                    }
                });
            }


            $(function() {
                $(".datepicker").datepicker({
                    dateFormat: "yy-mm-dd",
                    defaultDate: getCurrentDate()
                }).val(getCurrentDate());
            });

            (function() {
                showDayAppointments(getCurrentDate());
            })();

            function getCurrentDate() {
                var d = new Date(),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

                if (month.length < 2) month = '0' + month;
                if (day.length < 2) day = '0' + day;

                return [year, month, day].join('-');
            }

            $(document).on('click', '.appointment-week', function() {
                var date = $(this).find('.date').text();
                $('#week-radio').prop("checked", false);
                $('#day-radio').prop("checked", true);
                $('#day-input').val(date);
                $('#contentTab').empty();
                showDayAppointments(date);
                $('#contentTab').empty();
            });

            $(document).on('click', '.btn-cancel-appointment', function() {
                var appointmentId = $(this).attr('id');
                ajaxRequest("/appointments/" + appointmentId, 'DELETE').done(function () {
                $('#contentTab').empty();    
                showDayAppointments($('#day-input').val());   
            });
        });

        });
    });

    $("#settings").on("click", function() {
        $(this).addClass("selected");
        $("#home, #appointments").removeClass("selected");
        $("#container").load('templates/doctor_settings.html',function(){

        $('.timepicker').timepicker({
            timeFormat: 'H:mm:ss',
            interval: 30,
            minTime: '7',
            maxTime: '18:00',
            defaultTime: '7',
            startTime: '7:00',
            dynamic: false,
            dropdown: true,
            scrollbar: true
        });

        $(function() {
            $(".datepicker").datepicker({
                dateFormat: "yy-mm-dd",
                defaultDate: dateToday
            }).val(dateToday);
        });

        getFreeHours();

        $("#workingHours").on("click",function(){
            $(this).addClass("activeItem");
            $("#freeHours").removeClass("activeItem");
            $("#workingHoursTab").show();
            $("#freeHoursTab").hide();

        });

        $("#freeHours").on("click",function(){
            $(this).addClass("activeItem");
            $("#workingHours").removeClass("activeItem");
            $("#freeHoursTab").show();
            $("#workingHoursTab").hide();
        });
        $(document).on("click","#addFreeHrs",function(event){
            event.preventDefault();
            addFreeHour();
        });

        $("#notWorkingFrom, #notWorkingTo").on('blur', function() {
            var time = $('#notWorkingFrom').val();
            var splitTime = time.split(":");
            var hours = parseInt(splitTime[0]) + 1;
            hours = (hours == 13) ? 1 : hours;
            var newTime = hours + ":" + splitTime[1];
            $("#notWorkingTo").timepicker('option', 'minTime', newTime);
            $("#notWorkingTo").val(newTime);
        });

        });
    });

    function getFreeHours(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors/"+docData.id+"/freeHours","GET").done(function(){
            freeHours=ajaxData;
            renderFreeHours();
            dfd.resolve();
        });
        return dfd.promise();
    }

    function renderFreeHours(){
        $("#freeHoursContainer").empty();
            for(var i=0;i<freeHours.length;i++){
            $("#freeHoursContainer").append("<div id='"+i+"' class='listItem'>"+freeHours[i].date+" "+freeHours[i].from+" "+freeHours[i].to+"</div>");
        }
    }

    function addFreeHour(){
        var dfd=$.Deferred();
        var freeHour={
            date:$("#day").val(),
            from:$("#notWorkingFrom").val(),
            to:$("#notWorkingTo").val()
        };
        var dataToSend=JSON.stringify(freeHour);
        ajaxRequest("/doctors/"+docData.id+"/freeHours","PUT",dataToSend).done(function(){
            getFreeHours();
            dfd.resolve();
        });
        return dfd.promise();
    }

    $("#logout").on("click", function() {
        var dfd = $.Deferred();
        ajaxRequest('/auth/logout', 'POST').done(function() {
            localStorage.removeItem("token");
            window.location.href = '../index.html';
            dfd.resolve();
        });
        return dfd.promise();
    });


    function getTodaysAppointmentsInfo(){//temporary stuff. count resource needed 
        var dfd=$.Deferred();
        ajaxRequest("/doctors/" + docData.id + "/appointments?date="+dateToday, "GET").done(function(){
            appointmentsToday=ajaxData;
            var appointmentCount={
                count:appointmentsToday.length
            }
            var template = "You have {{count}} appointments today.";
            var html = Mustache.to_html(template, appointmentCount)
            $("#appointmentNotification").html(html);

            if(appointmentCount>0){
                var time={
                next:appointmentsToday[0].time
            }
            template = "{{next}}";
            html = Mustache.to_html(template, time);
            $("#nextAppointmentTime").html(html);
            }
            else{
                $("#nextAppointmentContainer").hide();
            }
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getAllAppointmentsInfo(){//same
        var dfd=$.Deferred();
        ajaxRequest("/doctors/" + docData.id + "/appointments", "GET").done(function(){
            appointments=ajaxData;
            console.log(appointments);
            var appointmentCount={
                count:appointments.length
            }
            var template = "{{count}}";
            var html = Mustache.to_html(template, appointmentCount);
            $("#totalAppointments").html(html);

            canceledCount=0;
            if(appointments.length>0){
            for(var i=0; i<appointments.length;i++){
                if(appointments[i].canceled==true)
                    canceledCount++;
            }
            $("#canceled").html(canceledCount + " patients canceled an appointment.");
        }
    
            dfd.resolve();
        });
        return dfd.promise();

    }

    $(document).on("click", "#editProfile", function (event) {
        event.preventDefault(event);
        $("#firstname, #lastname, #email, #phone,#editProfile").toggle();
        $("#changeFirstnameDiv,#changeLastnameDiv,#changeEmailDiv,#changePhoneDiv,#submitProfile").toggle();
        $("#editFirstnameInput").val(docData.firstName);
        $("#editLastnameInput").val(docData.lastName);
        $("#editEmailInput").val(docData.email);
        $("#editPhoneInput").val(docData.phone);
        $("#username").html(docData.userName);
        $("#firstname").val(docData.firstName);
        $("#lastname").val(docData.lastName);
        $("#email").val(docData.email);
        $("#phone").val(docData.phone);
        $(".profileErrMsg,#profileSuccessMsg").html("");       
    });

    $(document).on("click", "#submitProfile", function () {
        var dfd = $.Deferred();
        var newData = {
            firstname: $("#editFirstnameInput").val(),
            lastname: $("#editLastnameInput").val(),
            email: $("#editEmailInput").val(),
            phone: $("#editPhoneInput").val(),
            password: ""
        }
        var regex = {
            name: /^[a-zA-Z\-]+$/,
            email:/^[a-zA-Z0-9.!#$%&’*+\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/,
            phone:/^[0-9]{10}|([0-9]{4}\s[0-9]{3}\s[0-9]{3})|\+[0-9]{12}|\+[0-9]{3}\s[0-9]{3}\s[0-9]{3}\s[0-9]{3}$/
        };
        var isFirstnameValid = false;
        var isLastnameValid=false;
        var isEmailValid = false;
        var isPhoneValid=false;

        if (regex.name.test(newData.firstname))
            isFirstnameValid = true;
        else
            $("#firstnameErrorMsg").html("Invalid first name.");

        if (regex.name.test(newData.lastname))
            isLastnameValid = true;
        else
             $("#lastnameErrorMsg").html("Invalid last name.");

        if (regex.email.test(newData.email)) 
            isEmailValid = true;
        else
             $("#emailErrorMsg").html("Invalid email format.");
         if(regex.phone.test(newData.phone))
            isPhoneValid=true;

        if (isFirstnameValid&&isLastnameValid && isEmailValid&&isPhoneValid) {
            var dataToSend = JSON.stringify({
                "firstName": newData.firstname,
                "lastName": newData.lastname,
                "password": newData.password,
                "email": newData.email,
                "phone":newData.phone
            });
            ajaxRequest("/doctors/" + docData.id + "/profile", "PUT", dataToSend).done(function () {
                $("#emailErrorMsg").html("Profile changed successfully.");
                localStorage.setItem("firstName", ajaxData.firstName);
                localStorage.setItem("lastName", ajaxData.lastName);
                localStorage.setItem("email", ajaxData.email);
                localStorage.setItem("phone", ajaxData.phone);
                docData.firstName = ajaxData.firstName;
                docData.lastName = ajaxData.lastName;
                docData.email = ajaxData.email;
                docData.phone=ajaxData.phone;
                $("#editFirstnameInput,#firstname").val(ajaxData.firstName);
                $("#editLastnameInput,#lastname").val(ajaxData.lastName);
                $("#editEmailInput,#email").val(ajaxData.email);
                $("#editPhoneInput,#phone").val(ajaxData.phone);
                var template = "<p>{{firstName}} {{lastName}}</p>";
                html = Mustache.to_html(template, ajaxData);
                $("#userName").html(html);
                $(".profileErrMsg").html("");
                $("#myProfile").trigger("click");
                dfd.resolve();
            });
            return dfd.promise();
        }
    });

    $(document).on("click","#editPassword",function(){
        $("#password,#changePasswordDiv,#submitPassword,#editPassword").toggle();
        $("#pwdErrorMsg").html("");
    });

    $(document).on("click","#submitPassword",function(){
        var dfd=$.Deferred();
        var newPassword={
            p1:$("#editPwd1").val(),
            p2:$("#editPwd2").val()
        }
        var pwdRegex=/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\/\^&\*])/;
        var isPasswordValid=false;

        if (pwdRegex.test(newPassword.p1) && newPassword.p1 == newPassword.p2)
            isPasswordValid=true;

        if(isPasswordValid==false)
            $("#pwdErrorMsg").html("Invalid Password!");
        else{
            var dataToSend=JSON.stringify({
                "password":newPassword.p1
            });
            ajaxRequest("/doctors/" + docData.id + "/profile/password","PUT",dataToSend).done(function(){
                $("#pwdErrorMsg").html("password changed successfully!");
                dfd.resolve();
                $("#myProfile").trigger("click");
            });
            return dfd.promise();
        }
    });
    



    function ajaxRequest(url, requestType, dataToSend) {
        var dfd = $.Deferred();
        $.ajax({
            beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + docData.token);
            },
            url: 'http://localhost:8085/api' + url,
            type: requestType,
            data: dataToSend,
            contentType: 'application/json',
            success: function(data) {
                if (data != null) ajaxData = data;
                dfd.resolve();
            },
            error: function() {
                dfd.reject();
                console.log("Error");
            }
        });
        return dfd.promise();
    }
});
