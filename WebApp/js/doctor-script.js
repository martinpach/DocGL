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

    var ajaxData;
    var appointments;
    var appointmentsToday;

    var today = new Date();
    var dateToday = today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
    var time= today.getHours()+":"+today.getMinutes()+":"+today.getSeconds();

    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, docData);
    $("#userName").html(html);

    var likesTemplate = "{{likes}}";
    var html = Mustache.to_html(likesTemplate, docData);
    $("#likesCount").html(html);

    $("#myProfile").on("click", function() {
        $("#container").load('templates/doctor_profile.html', function() {
            var template = "<p>{{userName}}'s</p>";
            var html = Mustache.to_html(template, docData);
            $("#heading").html(html);
        });
    });

    $("#home").on("click", function() {
        $(this).addClass("selected");
        $("#appointments, #settings").removeClass("selected");
        $("#container").load('templates/doctor_home.html');
        getTodaysAppointmentsInfo();
        getAllAppointmentsInfo();
    });

    $("#home").trigger("click");

    $("#appointments").on("click", function() {
        $(this).addClass("selected");
        $("#settings, #home").removeClass("selected");
        $("#container").load('templates/doctor_appointments.html');
    });

    $("#settings").on("click", function() {
        $(this).addClass("selected");
        $("#home, #appointments").removeClass("selected");
        $("#container").load('templates/doctor_settings.html');
    });

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
                $("#nextAppointmentAt").html("");
                $("#nextAppointmentTime").html("No appointments left.");
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
