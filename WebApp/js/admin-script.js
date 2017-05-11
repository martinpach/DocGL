/*insert js here*/
$(document).ready(function () {
    var adminData = {
        id: localStorage.getItem("id"),
        firstName: localStorage.getItem("firstName"),
        lastName: localStorage.getItem("lastName"),
        email: localStorage.getItem("email"),
        userName: localStorage.getItem("userName"),
        passwordChanged: localStorage.getItem("passwordChanged"),
        token: localStorage.getItem("token")
    };
    var ajaxData;

    var start = 0;
    var limit = 4;
    var sortByDocs = "id";
    var wayDocs = "asc";
    var sortByUsers = "id";
    var wayUsers = "asc";
    var doctorList;
    var userList;
    var countUsers;
    var countDocs;
    getCountOfDoctors();
    getCountOfUsers();


    //loads name to the page
    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, adminData);
    $("#userName").html(html);

    //load profile
    $("#myProfile").on("click", function () {
        $("#container").load('templates/admin_profile.html', function () {
            var template = "<p>{{userName}}'s</p>";
            var templateFirstname = "<p>{{firstName}}</p>";
            var templateLastname="<p>{{lastName}}</p>";
            var templateMail = "<p>{{email}}</p>";
            var templatePassword = "<p>************</p>";
            var html = Mustache.to_html(template, adminData);
            $("#heading").html(html);
            html = Mustache.to_html(templateFirstname, adminData);
            $("#firstname").html(html);
            html = Mustache.to_html(templateLastname, adminData);
            $("#lastname").html(html);
            html = Mustache.to_html(templateMail, adminData);
            $("#email").html(html);
            html = Mustache.to_html(templatePassword, adminData);
            $("#password").html(html);
        });
    });

    $("#home").on("click", function () {
        $(this).addClass("selected");
        $("#users, #doctors").removeClass("selected");
        $("#container").load('templates/admin_home.html');
        $("#paginationContainer").hide();
        getLikes();
        getAppointmentCount();

    });

    $("#home").trigger("click");

    $("#doctors").on("click", function () {
        $(this).addClass("selected");
        $("#users, #home").removeClass("selected");
        $("#container").load('templates/admin_doctors.html');
        start = 0;
        getCountOfDoctors();
        getDoctors(start, limit, sortByDocs, wayDocs);
        setText(countDocs);
        setButtons(countDocs);
        $("#paginationContainer").show();
        setActionToDoctorsSearchBox();
    });

    $("#users").on("click", function () {
        $(this).addClass("selected");
        $("#home, #doctors").removeClass("selected");
        $("#container").load('templates/admin_users.html');
        start = 0;
        getCountOfUsers();
        getUsers(start, limit, sortByUsers, wayUsers);
        setText(countUsers);
        setButtons(countUsers);
        $("#paginationContainer").show();
        setActionToUsersSearchBox();
    });

    //logout
    $("#logout").on("click", function () {
        var dfd = $.Deferred();
        ajaxRequest('/auth/logout', 'POST').done(function () {
            localStorage.removeItem("token");
            window.location.href = '../index.html';
            dfd.resolve();
        });
        return dfd.promise();
    });

    //get doctor list
    function getDoctors(start, limit, sortBy, way) {
        var dfd = $.Deferred();
        ajaxRequest("/doctors?limit=" + limit + "&start=" + start + "&sortBy=" + sortByDocs + "&way=" + wayDocs + "&name=", "GET").done(function () {
            var icon = '<i class="fa fa-user-md tableIcon"></i>';
            generateDoctorTable();
            doctorList=ajaxData;
            dfd.resolve();
        });
        return dfd.promise();
    }
    //count of items for pagination purpose
    function getCountOfDoctors() {
        var dfd = $.Deferred();
        ajaxRequest("/doctors/count", "GET").done(function () {
            countDocs = ajaxData.count;
            dfd.resolve();
        });
        return dfd.promise();
    }

    //get user list
    function getUsers(start, limit) {
        var dfd = $.Deferred();
        ajaxRequest("/patients?limit=" + limit + "&start=" + start + "&sortBy=" + sortByUsers + "&way=" + wayUsers, "GET").done(function () {
            userList=ajaxData;
            generateUserTable();
            dfd.resolve();
        });
        return dfd.promise();
    }
    //count of items for pagination purpose
    function getCountOfUsers() {
        var dfd = $.Deferred();
        ajaxRequest("/patients/count", "GET").done(function () {
            countUsers = ajaxData.count;
            dfd.resolve();
        });
        return dfd.promise();
    }

    //pagination
    function setButtons(count) {
        if (count - start >= 5)
            $("#arrowRight").removeClass("disabledBtn");
        else
            $("#arrowRight").addClass("disabledBtn");
        if (start >= 4)
            $("#arrowLeft").removeClass("disabledBtn");
        else
            $("#arrowLeft").addClass("disabledBtn");
    }

    function setText(count) {
        if (count <= 0) {
            $("#paginationText").hide();
        } else {
            var from = start + 1;
            var to;
            if (start + 5 > count)
                to = limit;
            else
                to = start + 5;
            var paginationText = "Showing " + from + " - " + to + " from " + count;
            $("#paginationText").html(paginationText);
        }
    }

    $("#arrowLeft").on("click", function () {
        start -= 4;
        if ($("#doctors").hasClass("selected")) {
            getDoctors(start, limit, sortByDocs, wayDocs);
            setText(countDocs);
            setButtons(countDocs);
            generateDoctorTable();
        }
        if ($("#users").hasClass("selected")) {
            getUsers(start, limit, sortByUsers, wayUsers);
            setText(countUsers);
            setButtons(countUsers);
            generateUserTable();
        }
    });

    $("#arrowRight").on("click", function () {
        console.log("click");
        start += 4;
        if ($("#doctors").hasClass("selected")) {
            getDoctors(start, limit, sortByDocs, wayDocs);
            setText(countDocs);
            setButtons(countDocs);
            generateDoctorTable();
        }
        if ($("#users").hasClass("selected")) {
            getUsers(start, limit, sortByUsers, wayUsers);
            setText(countUsers);
            setButtons(countUsers);
            generateUserTable();
        }
    });


    function generateDoctorTable() {
        var i = 0;
        var icon = '<i class="fa fa-user-md tableIcon"></i>';
        var approvedIcon='<i class="fa fa-check-circle-o tableIcon statusIcon approvedIcon" aria-hidden="true"></i>';
        var unapprovedIcon='<i class="fa fa-circle-o tableIcon statusIcon approvedIcon clickable approve" aria-hidden="true"></i>';
        var blockedIcon='<i class="fa fa-ban tableIcon statusIcon blockedIcon clickable unblock" aria-hidden="true"></i>';
        var unblockedIcon='<i class="fa fa-unlock statusIcon tableIcon unapprovedIcon clickable block" aria-hidden="true"></i>';
        var approvedStatus;
        var blockedStatus;
        var statusIcon;
        var approvalIcon;
        $(".tableRow").remove();
        for (i = 0; i < ajaxData.length; i++) {
            approvedStatus=ajaxData[i].approved;
            blockedStatus=ajaxData[i].blocked;
            approvalIcon=approvedStatus==false?unapprovedIcon:approvedIcon;
            statusIcon=blockedStatus==false?unblockedIcon:blockedIcon;

            $("#tableDoctors").append('<tr class="tableRow" data-id="'+i+'">' +
                '<td>' + icon + '</td>' +
                '<td>' + ajaxData[i].id + '</td>' +
                '<td>' + ajaxData[i].firstName + '</td>' +
                '<td>'+ ajaxData[i].lastName+'</td>'+
                '<td>' + ajaxData[i].specialization + '</td>' +
                '<td>' + ajaxData[i].email + '</td>' +
                '<td><p class="like"><i class="fa fa-heart heart"></i>' + ajaxData[i].likes + '</p></td>' +
                '<td class="text-center">' + statusIcon + '</td>' +
                '<td class="text-center">' + approvalIcon + '</td>'  +
                '</tr>');
        }
    }

    function generateUserTable() {
        var i = 0;
        var icon = '<i class="fa fa-user tableIcon"></i>';
        var blockedStatus;
        var blockedIcon='<i class="fa fa-ban tableIcon statusIcon blockedIcon clickable unblockU" aria-hidden="true"></i>';
        var unblockedIcon='<i class="fa fa-unlock tableIcon statusIcon approvedIcon clickable blockU" aria-hidden="true"></i>';
        var statusIcon;
        $(".tableRow").remove();
        for (i = 0; i < ajaxData.length; i++) {
            blockedStatus=ajaxData[i].blocked;
            statusIcon=blockedStatus==false?unblockedIcon:blockedIcon;
            $("#tableUsers").append('<tr class="tableRow" data-id="'+i+'">' +
            '<td>' + icon + '</td>' +
            '<td>' + ajaxData[i].id + '</td>' +
            '<td>' + ajaxData[i].firstName+ '</td>' +
            '<td>' + ajaxData[i].lastName +'</td>' +
            '<td>' + ajaxData[i].email + '</td>' +
            '<td>' + ajaxData[i].registrationDate + '</td>' +
            '<td class="text-center">' + statusIcon + '</td>' +
            '</tr>');
        }
    }


    
    
    $(document).on("click", "#sortIdUsers", function (event) { //not done
        event.preventDefault(event);
        sortByUsers = "id";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortNameUsers", function (event) {
        event.preventDefault(event);
        sortByUsers = "lastName";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortJoinedUsers", function (event) {
        event.preventDefault(event);
        sortByUsers = "registration_date";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");
    });

    $(document).on("click", "#sortIdDocs", function (event) { //not done
        event.preventDefault(event);
        sortByDocs = "id";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortNameDocs", function (event) {
        event.preventDefault(event);
        sortByDocs = "lastName";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        $(this).find("i").toggleClass("fa-sort-asc");
    });

    $(document).on("click", "#sortRatingDocs", function (event) {
        event.preventDefault(event);
        sortByDocs = "likes";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

//approving and blocking docs
    $(document).on("click",".approve",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var approved=JSON.stringify({
            "approved":true
        });
        ajaxRequest("/doctors/"+(id+1)+"/approved?name=","PUT",approved).done(function(){
            getDoctors(start, limit, sortByDocs, wayDocs);
            dfd.resolve();
        });
        return dfd.promise();
    });

    $(document).on("click",".unblock",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var blocked=JSON.stringify({
            "blocked":false
        });
        ajaxRequest("/doctors/"+(id+1)+"/blocked?name=","PUT",blocked).done(function(){
            getDoctors(start, limit, sortByDocs, wayDocs);
            dfd.resolve();
        });
        return dfd.promise();
    });

    $(document).on("click",".block",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var blocked=JSON.stringify({
            "blocked":true
        });
        ajaxRequest("/doctors/"+(id+1)+"/blocked?name=","PUT",blocked).done(function(){
            getDoctors(start, limit, sortByDocs, wayDocs);
            dfd.resolve();
        });
        return dfd.promise();
    });

    $(document).on("click",".blockU",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var blocked=JSON.stringify({
            "blocked":true
        });
        ajaxRequest("/patients/"+(id+1)+"/blocked?name=","PUT",blocked).done(function(){
            getUsers(start, limit, sortByUsers, wayUsers);
            dfd.resolve();
        });
        return dfd.promise();
    });

    $(document).on("click",".unblockU",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var blocked=JSON.stringify({
            "blocked":false
        });
        ajaxRequest("/patients/"+(id+1)+"/blocked?name=","PUT",blocked).done(function(){
            getUsers(start, limit, sortByUsers, wayUsers);
            dfd.resolve();
        });
        return dfd.promise();
    });


    //get statistics
    function getAppointmentCount() {
        var dfd = $.Deferred();
        ajaxRequest("/appointments/count", "GET").done(function () {
            var appointmentCount = ajaxData.count;
            $("#countAppointments").html(appointmentCount);
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getLikes() {
        var dfd = $.Deferred();
        ajaxRequest("/doctors/likes", "GET").done(function () {
            var likeCount = ajaxData.count;
            $("#countLikes").html(likeCount);
            dfd.resolve();
        });
        return dfd.promise();
    }

    //edit profile
    //toggle edit fields
    $(document).on("click", "#editProfile", function (event) {
        event.preventDefault(event);
        $("#firstname, #lastname, #email").toggle();
        $("#changeFirstnameDiv,#changeLastnameDiv,#changeEmailDiv,#submitProfile").toggle();
        $("#editFirstnameInput").val(adminData.firstName);
        $("#editLastnameInput").val(adminData.lastName);
        $("#editEmailInput").val(adminData.email);
        $(".profileErrMsg,#profileSuccessMsg").html("");       
    });

    $(document).on("click","#editPassword",function(){
        $("#password,#changePasswordDiv,#submitPassword").toggle();
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
            ajaxRequest("/admins/" + adminData.id + "/profile/password","PUT",dataToSend).done(function(){
                $("#pwdErrorMsg").html("password changed successfully!");
                dfd.resolve();
            });
            return dfd.promise();
        }
    });

    $(document).on("click", "#submitProfile", function () {
        var dfd = $.Deferred();
        var newData = {
            firstname: $("#editFirstnameInput").val(),
            lastname: $("#editLastnameInput").val(),
            email: $("#editEmailInput").val(),
            password: ""
        }
        var regex = {
            name: /^[a-zA-Z\-]+$/,
            email:/^[a-zA-Z0-9.!#$%&’*+\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/
        };
        var isFirstnameValid = false;
        var isLastnameValid=false;
        var isEmailValid = false;

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

        console.log(newData.lastname+" "+newData.firstname)

        if (isFirstnameValid&&isLastnameValid && isEmailValid) {
            var dataToSend = JSON.stringify({
                "firstName": newData.firstname,
                "lastName": newData.lastname,
                "password": newData.password,
                "email": newData.email
            });
            ajaxRequest("/admins/" + adminData.id + "/profile", "PUT", dataToSend).done(function () {
                $("#emailErrorMsg").html("Profile changed successfully.");
                console.log(ajaxData);
                localStorage.setItem("firstName", ajaxData.firstName);
                localStorage.setItem("lastName", ajaxData.lastName);
                localStorage.setItem("email", ajaxData.email);
                adminData.firstName = ajaxData.firstName;
                adminData.lastName = ajaxData.lastName;
                adminData.email = ajaxData.email
                console.log(localStorage.getItem("userName") + " " + localStorage.getItem("email"));
                $("#editUsernameInput").val(ajaxData.userName);
                $("#editEmailInput").val(ajaxData.email);
                var template = "<p>{{firstName}} {{lastName}}</p>";
                html = Mustache.to_html(template, ajaxData);
                $("#userName").html(html);
                $(".profileErrMsg").html("");
                $("#profileSuccessMsg").html("Profile changed successfully");

                dfd.resolve();
            });
            return dfd.promise();
        }
    });

    var searchString;

    function getDoctorsSearch() {
        var dfd = $.Deferred();
        ajaxRequest("/doctors?name=" + searchString, "GET").done(function () {
            var icon = '<i class="fa fa-user-md tableIcon"></i>';
            generateDoctorTable();
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getUsersSearch() {
        var dfd = $.Deferred();
        ajaxRequest("/patients?name=" + searchString, "GET").done(function () {
            var icon = '<i class="fa fa-user-md tableIcon"></i>';
            generateDoctorTable();
            dfd.resolve();
        });
        return dfd.promise();
    }

    function setActionToUsersSearchBox() {
        $(document).on("keydown", "#searchFieldUsers", function (event) {
            searchString = $("#searchFieldUsers").val();
            if (event.keyCode == 13) {
                if ($("#users").hasClass("selected")) {
                    getUsersSearch();
                }
            }
        });
    }

    function setActionToDoctorsSearchBox() {
        $(document).on("keydown", "#searchFieldDoctors", function (event) {
            searchString = $("#searchFieldDoctors").val();
            if (event.keyCode == 13) {
                if ($("#doctors").hasClass("selected")) {
                    getDoctorsSearch();
                }
            }
        });
    }

    //ajax request function
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
