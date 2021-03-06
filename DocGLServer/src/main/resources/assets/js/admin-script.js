 /*insert js here*/
$(document).ready(function () {
    $(this).idleTimer(3600000);
    $(this).on( "idle.idleTimer", function(){
        localStorage.removeItem('token');
        window.location.href = '../index.html';
    });

    var adminData = {
        id: localStorage.getItem("id"),
        firstName: localStorage.getItem("firstName"),
        lastName: localStorage.getItem("lastName"),
        email: localStorage.getItem("email"),
        userName: localStorage.getItem("userName"),
        passwordChanged: localStorage.getItem("passwordChanged"),
        token: localStorage.getItem("token")
    };

    (function requestForNewToken() {
        setTimeout(requestForNewToken, 3540000);
        ajaxRequest("/auth/token", "GET").done(function () {
            localStorage.setItem("token", adminData.token);
        });
    })();
    var ajaxData;

    var start = 1;
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
            var templateUsername="<p>{{userName}}</p>";
            var templatePassword = "<p>************</p>";
            var html = Mustache.to_html(template, adminData);
            $("#heading").html(html);
            $("#firstname").html(html);
            html = Mustache.to_html(templateUsername, adminData);
            $("#username").html(html);
            html = Mustache.to_html(templateFirstname, adminData);
            $("#firstname").html(html);
            html = Mustache.to_html(templateLastname, adminData);
            $("#lastname").html(html);
            html = Mustache.to_html(templateMail, adminData);
            $("#email").html(html);
            html = Mustache.to_html(templatePassword, adminData);
            $("#password").html(html);
        });
        $("#users, #doctors,#home").removeClass("selected");
        $("#paginationContainer").hide();
    });

    $("#home").on("click", function () {
        $(this).addClass("selected");
        $("#users, #doctors").removeClass("selected");
        $("#container").load('templates/admin_home.html');
        $("#paginationContainer").hide();
        getAppointmentCount();
    });

    $("#home").trigger("click");

    $("#doctors").on("click", function () {
        $(this).addClass("selected");
        $("#users, #home").removeClass("selected");
        $("#container").load('templates/admin_doctors.html',function(){
            start = 1;
            getCountOfDoctors();
            getDoctors(start, limit, sortByDocs, wayDocs);
            setText(countDocs);
            setButtons(countDocs);
            $("#paginationContainer").show();
            setActionToDoctorsSearchBox();
        });
    });

    $("#users").on("click", function () {
        $(this).addClass("selected");
        $("#home, #doctors").removeClass("selected");
        $("#container").load('templates/admin_users.html',function(){
            start = 1;
            getCountOfUsers();
            getUsers(start, limit, sortByUsers, wayUsers);
            setText(countUsers);
            setButtons(countUsers);
            $("#paginationContainer").show();
            setActionToUsersSearchBox();
        });
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

    $("#changePassword").on("click",function(){
         $("#container").load('templates/admin_password.html');
         $("#paginationContainer").hide();
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
        if (count - start > 3)
            $("#arrowRight").removeClass("disabledBtn");
        else
            $("#arrowRight").addClass("disabledBtn");
        if (start >= 3)
            $("#arrowLeft").removeClass("disabledBtn");
        else
            $("#arrowLeft").addClass("disabledBtn");
    }

    function setText(count) {
        if (count <= 0) {
            $("#paginationText").hide();
        } else {
            var from = start;
            var to;
            if (start + 3 >count)
                to = count;
            else
                to = start + 3;
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
            //console.log("start"+start+" "+"limit "+limit);
        }
        if ($("#users").hasClass("selected")) {
            getUsers(start, limit, sortByUsers, wayUsers);
            setText(countUsers);
            setButtons(countUsers);
            generateUserTable();
        }
    });

    $("#arrowRight").on("click", function () {
        //console.log("click");
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
            if(approvedStatus==false)
                statusIcon="";
            $("#tableDoctors").append('<tr class="tableRow" data-id="'+ajaxData[i].id+'">' +
                '<td>' + icon + '</td>' +
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
            $("#tableUsers").append('<tr class="tableRow" data-id="'+ajaxData[i].id+'">' +
            '<td>' + icon + '</td>' +
            '<td>' + ajaxData[i].firstName+ '</td>' +
            '<td>' + ajaxData[i].lastName +'</td>' +
            '<td>' + ajaxData[i].email + '</td>' +
            '<td>' + ajaxData[i].registrationDate + '</td>' +
            '<td class="text-center">' + statusIcon + '</td>' +
            '</tr>');
        }
    }


    var sortAsc='<i class="fa fa-sort-asc sortArrow" aria-hidden="true"></i>';
    var sortDesc='<i class="fa fa-sort-desc sortArrow" aria-hidden="true"></i>';
    var sortingIcon;

    $(document).on("click", "#sortNameUsers", function () {
        sortByUsers = "lastName";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        sortingIcon=wayUsers=="asc"?sortAsc:sortDesc;
        getUsers(start, limit, sortByUsers, wayUsers);
        $(".sortArrow").remove();
        $(this).find("span").html(sortingIcon);

    });

    $(document).on("click", "#sortJoinedUsers", function () {
        sortByUsers = "registration_date";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        sortingIcon=wayUsers=="asc"?sortAsc:sortDesc;
        getUsers(start, limit, sortByUsers, wayUsers);
        $(".sortArrow").remove();
        $(this).find("span").html(sortingIcon);
    });

    $(document).on("click", "#sortLastNameDocs", function () {
        sortByDocs = "lastName";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        sortingIcon=wayDocs=="asc"?sortAsc:sortDesc;
        $(".sortArrow").remove();
        $(this).find("span").html(sortingIcon);
    });

    $(document).on("click", "#sortSpecDocs", function () {
        sortByDocs = "specialization";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        sortingIcon=wayDocs=="asc"?sortAsc:sortDesc;
        $(".sortArrow").remove();
        $(this).find("span").html(sortingIcon);
    });


    $(document).on("click", "#sortRatingDocs", function () {
        sortByDocs = "likes";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        sortingIcon=wayDocs=="asc"?sortAsc:sortDesc;
        $(".sortArrow").remove();
        $(this).find("span").html(sortingIcon);

    });

//approving and blocking docs
    $(document).on("click",".approve",function(event){
        event.preventDefault();
        var dfd=$.Deferred();
        var id=parseInt($(this).parent().parent().attr("data-id"));
        var approved=JSON.stringify({
            "approved":true
        });
        ajaxRequest("/doctors/"+(id)+"/approved?name=","PUT",approved).done(function(){
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
        ajaxRequest("/doctors/"+(id)+"/blocked?name=","PUT",blocked).done(function(){
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
        ajaxRequest("/doctors/"+(id)+"/blocked?name=","PUT",blocked).done(function(){
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
        ajaxRequest("/patients/"+(id)+"/blocked?name=","PUT",blocked).done(function(){
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
        ajaxRequest("/patients/"+(id)+"/blocked?name=","PUT",blocked).done(function(){
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

    //edit profile
    //toggle edit fields
    $(document).on("click", "#editProfile", function (event) {
        event.preventDefault(event);
        $("#firstname, #lastname, #email,#editProfile").toggle();
        $("#changeFirstnameDiv,#changeLastnameDiv,#changeEmailDiv,#submitProfile").toggle();
        $("#editFirstnameInput, #firstname").val(adminData.firstName);
        $("#editLastnameInput,#lastname").val(adminData.lastName);
        $("#editEmailInput,#email").val(adminData.email);
        $(".profileErrMsg,#profileSuccessMsg").html("");       
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
            ajaxRequest("/admins/" + adminData.id + "/profile/password","PUT",dataToSend).done(function(){
                $("#pwdErrorMsg").html("password changed successfully!");
                dfd.resolve();
                $("#myProfile").trigger("click");
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

        //console.log(newData.lastname+" "+newData.firstname)

        if (isFirstnameValid&&isLastnameValid && isEmailValid) {
            var dataToSend = JSON.stringify({
                "firstName": newData.firstname,
                "lastName": newData.lastname,
                "password": newData.password,
                "email": newData.email
            });
            ajaxRequest("/admins/" + adminData.id + "/profile", "PUT", dataToSend).done(function () {
                $("#emailErrorMsg").html("Profile changed successfully.");
                //console.log(ajaxData);
                localStorage.setItem("firstName", ajaxData.firstName);
                localStorage.setItem("lastName", ajaxData.lastName);
                localStorage.setItem("email", ajaxData.email);
                adminData.firstName = ajaxData.firstName;
                adminData.lastName = ajaxData.lastName;
                adminData.email = ajaxData.email
                $("#editFirstnameInput").val(ajaxData.firstName);
                $("#editLastnameInput").val(ajaxData.lastName);
                $("#editEmailInput").val(ajaxData.email);
                var template = "<p>{{firstName}} {{lastName}}</p>";
                html = Mustache.to_html(template, ajaxData);
                $("#userName").html(html);
                $(".profileErrMsg").html("");
                $("#profileSuccessMsg").html("Profile changed successfully");

                dfd.resolve();
                $("#myProfile").trigger("click");
            });
            return dfd.promise();
        }
    });

    var searchString;
    var numberOfResults;

    function getDoctorsSearch() {
        var dfd = $.Deferred();
        ajaxRequest("/doctors?name=" + searchString, "GET").done(function () {
            if(ajaxData!=null){
                generateDoctorTable();
                numberOfResults=ajaxData.length;
                dfd.resolve();
            }
        });
        return dfd.promise();
    }

    function searchDocsBySpec(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors?spec="+searchString,"GET").done(function(){
            generateDoctorTable();
            //console.log(ajaxData.length);
            dfd.resolve();
        })
    }


    function getUsersSearch() {
        var dfd = $.Deferred();
        ajaxRequest("/patients?name=" + searchString, "GET").done(function () {
            generateUserTable();
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
            var e = jQuery.Event("keydown");
            e.which = 13; 
            if (event.keyCode == 13) {
                if ($("#doctors").hasClass("selected")) {
                    getDoctorsSearch();
                    if(numberOfResults==0){
                        $("#searchFieldDoctors").trigger(e);
                        searchString = $("#searchFieldDoctors").val();
                        searchDocsBySpec();
                    }
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
                if (data != null && data.token === undefined){
                    ajaxData = data;
                }
                else if(data != null){
                    adminData.token = data.token;
                }

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
