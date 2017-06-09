$(document).ready(function() {
    $(this).idleTimer(3600000);
    $(this).on( "idle.idleTimer", function(){
        localStorage.removeItem('token');
        window.location.href = '../index.html';
    });

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

    (function requestForNewToken() {
        setTimeout(requestForNewToken, 3540000);
        ajaxRequest("/auth/token", "GET").done(function(){
            localStorage.setItem("token", docData.token)
        });
    })();

    var ajaxData;
    var appointments;
    var appointmentsToday=[];
    var appointmentCount;
    var canceledAppointments;
    var canceledAmount;
    var freeHours;
    var workingHours;

    var today = new Date();
    var dateToday = today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
    var time= today.getHours()+":"+today.getMinutes()+":"+today.getSeconds();

    getCanceledAppointments();

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
        getUpcomingAppointment();
        getCanceledAppointments();
    });


    $("#home").trigger("click");

    $("#appointments").on("click", function() {
        $("#appointments").addClass("selected");
        $("#settings, #home").removeClass("selected");
        $("#container").load('templates/doctor_appointments.html');
    });

    $("#settings").on("click", function() {
        $(this).addClass("selected");
        $("#home, #appointments").removeClass("selected");
        $("#container").load('templates/doctor_settings.html',function(){

        $('.timepicker').timepicker({
            timeFormat: 'H:mm',
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
        getWorkingHours();
        $("#workingHours").on("click",function(){
            $(this).addClass("activeItem");
            $("#freeHours").removeClass("activeItem");
            $("#workingHoursTab").show();
            $("#freeHoursTab").hide();

        });

        $("#workingFrom, #workingTo , #notWorkingTo, #notWorkingFrom").on('click blur', function() {
            var time = $('#workingFrom').val();
            var splitTime = time.split(":");
            var hours = parseInt(splitTime[0]) + 1;
            hours = (hours == 13) ? 1 : hours;
            var newTime = hours + ":" + splitTime[1];
            $("#workingTo").timepicker('option', 'minTime', newTime);
            $("#workingTo").val(newTime);
        });

        function getWorkingHours(){
            var dfd =$.Deferred();
            ajaxRequest("/doctors/"+docData.id+"/workingHours","GET").done(function(){
                workingHours=ajaxData;
                console.log(workingHours);
                dfd.resolve();
            });
            return dfd.promise();
        }

        var updatedWorkingHours = {
            mon: [],
            tue: [],
            wed: [],
            thu: [],
            fri: []
        }

        var workingHours1st = {
            mondayFrom: null,
            mondayTo: null,
            tuesdayFrom: null,
            tuesdayTo: null,
            wednesdayFrom: null,
            wednesdayTo: null,
            thursdayFrom: null,
            thursdayTo: null,
            fridayFrom: null,
            fridayTo: null
        }

        var workingHours2nd = {
            mondayFrom: null,
            mondayTo: null,
            tuesdayFrom: null,
            tuesdayTo: null,
            wednesdayFrom: null,
            wednesdayTo: null,
            thursdayFrom: null,
            thursdayTo: null,
            fridayFrom: null,
            fridayTo: null
        }

        $(document).on("click","#setWorkingHours",function(event){
            event.preventDefault();
            addWorkingHour();
            renderWorkingHours();
            $("#submitHours").show();
        });

        function addWorkingHour(){
            var dayInput=$("#input-day").val();
            console.log(dayInput);
            var fromInput=$("#workingFrom").val();
            var toInput=$("#workingTo").val();

            if(fromInput != null&& toInput != null){
                var interval ={
                    from: fromInput,
                    to: toInput
                }
                if(updatedWorkingHours[dayInput].length<2){
                    updatedWorkingHours[dayInput].push(interval);
                    setIntervalValues();
                    console.log(interval);
                }
                else{
                    $("#workingHoursError").show();
                }
            }
            else{
                console.log("inputs must not be empty.");
            }

        }

        function setIntervalValues (){
            if (updatedWorkingHours.mon[0] != undefined) {
                workingHours1st.mondayFrom = updatedWorkingHours.mon[0].from;
                workingHours1st.mondayTo = updatedWorkingHours.mon[0].to;
                console.log(workingHours1st.mondayFrom+" "+workingHours1st.mondayTo);
            }
            if (updatedWorkingHours.mon[1] != undefined) {
                workingHours2nd.mondayFrom = updatedWorkingHours.mon[1].from;
                workingHours2nd.mondayTo = updatedWorkingHours.mon[1].to;
            }
            if (updatedWorkingHours.tue[0] != undefined) {
                workingHours1st.tuesdayFrom = updatedWorkingHours.tue[0].from;
                workingHours1st.tuesdayTo = updatedWorkingHours.tue[0].to;
            }
            if (updatedWorkingHours.tue[1] != undefined) {
                workingHours2nd.tuesdayFrom = updatedWorkingHours.tue[1].from;
                workingHours2nd.tuesdayTo = updatedWorkingHours.tue[1].to;
            }
            if (updatedWorkingHours.wed[0] != undefined) {
                workingHours1st.wednesdayFrom = updatedWorkingHours.wed[0].from;
                workingHours1st.wednesdayTo = updatedWorkingHours.wed[0].to;
            }
            if (updatedWorkingHours.wed[1] != undefined) {
                workingHours2nd.wednesdayFrom = updatedWorkingHours.wed[1].from;
                workingHours2nd.wednesdayTo = updatedWorkingHours.wed[1].to;
            }
            if (updatedWorkingHours.thu[0] != undefined) {
                workingHours1st.thursdayFrom = updatedWorkingHours.thu[0].from;
                workingHours1st.thursdayTo = updatedWorkingHours.thu[0].to;
            }
            if (updatedWorkingHours.thu[1] != undefined) {
                workingHours2nd.thursdayFrom = updatedWorkingHours.thu[1].from;
                workingHours2nd.thursdayTo = updatedWorkingHours.thu[1].to;
            }
            if (updatedWorkingHours.fri[0] != undefined) {
                workingHours1st.fridayFrom = updatedWorkingHours.fri[0].from;
                workingHours1st.fridayTo = updatedWorkingHours.fri[0].to;
            }
            if (updatedWorkingHours.fri[1] != undefined) {
                workingHours2nd.fridayFrom = updatedWorkingHours.fri[1].from;
                workingHours2nd.fridayTo = updatedWorkingHours.fri[1].to;
            }

        }


        $(document).on("click","#submitWorkingHours",function(event){
            event.preventDefault();
            setIntervalValues();
            var workingHoursObj=JSON.stringify([workingHours1st,workingHours2nd]);
            console.log(workingHoursObj);
            ajaxRequest("/doctors/"+docData.id+"/workingHours","PUT",workingHoursObj).done(function(){
                console.log("update successful.");
                }).fail(function(){
                    $("#warningModal").modal("show");
            });

        });

        $("#input-day").on("change", function(){
            renderWorkingHours();
            $("#workingHoursError").hide();
        });

        function renderWorkingHours(){
            $("#hoursContainer").empty();
            var day=$("#input-day").val();
                for(var i=0;i<updatedWorkingHours[day].length;i++)
                    if(updatedWorkingHours[day][i]!=undefined)
                        $("#hoursContainer").append("<div id='"+i+"' class='listItem'><b>"+ day+" </b>"+updatedWorkingHours[day][i].from+" - "+updatedWorkingHours[day][i].to+"</div>");
        }


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
            from:$("#notWorkingFrom").val()+":00",
            to:$("#notWorkingTo").val()+":00"
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

    $(document).on("click","#cancelledAppointments",function(){
        getCanceledAppointments();
        $("#canceledModal").modal("show");

    });

    function getCanceledAppointments(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors/" + docData.id + "/appointments/cancelled?timePeriod=today","GET").done(function(){
            canceledAppointments=ajaxData;
            canceledAmount=canceledAppointments.length;
            $("#canceledContainer").empty();
            for(var i=0;i<canceledAppointments.length;i++){
                $("#canceledContainer").append("<div class='col-md-12 text-center canceledAppointment'>"+
                    "<p><b>"+canceledAppointments[i].time+"</b></p>"+
                    "<p>"+canceledAppointments[i].patientFirstName+" "+canceledAppointments[i].patientLastName+"</p>"+
                    "<p>"+canceledAppointments[i].note+"</p>"+
                    "</div>");
            }
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getTodaysAppointmentsInfo(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors/" + docData.id + "/appointments?date="+dateToday, "GET").done(function(){
            var appointmentsToday=ajaxData;
            var countAppointments=0;
            for(var i=0;i<appointmentsToday.length;i++){
                if(appointmentsToday[i].canceled==false)
                    countAppointments++;
            }
            var appointmentCount={
                count:countAppointments
            }
            var template = "You have {{count}} appointments today.";
            var html = Mustache.to_html(template, appointmentCount)
            $("#appointmentNotification").html(html);          
           
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getUpcomingAppointment(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors/" + docData.id + "/appointments?date="+dateToday, "GET").done(function(){
            appointmentsToday=ajaxData;
            var done=0;
            var time={
                next:null
            }
            if(appointmentsToday.length>0){
                for(var i=0;i<appointmentsToday.length;i++){
                    if(appointmentsToday[i].done==false&&appointmentsToday[i].canceled==false){
                        time.next=appointmentsToday[i].time;
                        done++;
                        break;
                    }
                }
                console.log(time.next);
                template = "{{next}}";
                html = Mustache.to_html(template, time);
                $("#nextAppointmentTime").html(html);
                if(time.next==null)
                    $("#nextAppointmentAt").html("All appointments are done.");
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
            $("#canceled").html(canceledAmount + " canceled appointments");    
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
                if (data != null && data.token === undefined){
                    ajaxData = data;
                }
                else if(data != null){
                    docData.token = data.token;
                }
                dfd.resolve();
            },
            error: function(errorData) {
                dfd.reject();
                console.log(errorData);
            }
        });
        return dfd.promise();
    }
});
