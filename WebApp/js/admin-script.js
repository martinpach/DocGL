/*insert js here*/
$(document).ready(function () {
    var adminData
     = {
        id: localStorage.getItem("id"),
        firstName: localStorage.getItem("firstName"),
        lastName: localStorage.getItem("lastName"),
        email: localStorage.getItem("email"),
        userName: localStorage.getItem("userName"),
        passwordChanged: localStorage.getItem("passwordChanged"),
        token: localStorage.getItem("token")
    };
    var ajaxData
    ;

    var start = 0;
    var limit = 4;
    var sortByDocs = "id";
    var wayDocs = "asc";
    var sortByUsers = "id";
    var wayUsers = "asc";
    var countUsers;
    var countDocs;
    getCountOfDoctors();
    getCountOfUsers();


    //loads name to the page
    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, adminData
        );
    $("#userName").html(html);

    //load profile
    $("#myProfile").on("click", function () {
        $("#container").load('templates/admin_profile.html', function () {
            var template = "<p>{{userName}}'s</p>";
            var templateUsername = "<p>{{userName}}</p>";
            var templateMail = "<p>{{email}}</p>";
            var templatePassword = "<p>************</p>";
            var html = Mustache.to_html(template, adminData
                );
            $("#heading").html(html);
            html = Mustache.to_html(templateUsername, adminData
                );
            $("#username").html(html);
            html = Mustache.to_html(templateMail, adminData
                );
            $("#email").html(html);
            html = Mustache.to_html(templatePassword, adminData
                );
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
        $(".tableRow").remove();
        for (i = 0; i < ajaxData.length; i++) {
            $("#tableDoctors").append('<tr class="tableRow">' +
                '<td>' + icon + '</td>' +
                '<td>' + ajaxData[i].id + '</td>' +
                '<td>' + ajaxData[i].firstName + " " + ajaxData[i].lastName + '</td>' +
                '<td>' + ajaxData[i].specialization + '</td>' +
                '<td>' + ajaxData[i].email + '</td>' +
                '<td><p class="like"><i class="fa fa-heart heart"></i>' + ajaxData[i].likes + '</p></td>' +
                '</tr>');
        }
    }

    function generateUserTable() {
        var i = 0;
        var icon = '<i class="fa fa-user tableIcon"></i>';
        $(".tableRow").remove();
        for (i = 0; i < ajaxData.length; i++) {
            $("#tableUsers").append('<tr class="tableRow">' +
                '<td>' + icon + '</td>' +
                '<td>' + ajaxData[i].id + '</td>' +
                '<td>' + ajaxData[i].firstName + " " + ajaxData[i].lastName + '</td>' +
                '<td>' + ajaxData[i].email + '</td>' +
                '<td>' + ajaxData[i].registrationDate + '</td>' +
                '</tr>');
        }

    }

    //sorting, not ready yet - id, joined date, name, spec,likes

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
        $("#username").toggle();
        $("#changeUsernameDiv").toggle();
        $("#editUsernameInput").val(adminData.userName);
        $("#email").toggle();
        $("#changeEmailDiv").toggle();
        $("#editEmailInput").val(adminData.email);
        $("#password").toggle();
        $("#changePasswordDiv").toggle();
    });

    //send changed info to the server,to be finished
    $(document).on("click", "#submitProfile", function (event) {
        event.preventDefault(event);
        var dfd=$.Deferred();
        var newData={
            username:$("#editUsernameInput").val(),
            email:$("#editEmailInput").val(),
            password:$("#editPwd1").val(),
            password2:$("#editPwd2").val()
        }
        var regex={
            username:/^[a-zA-Z0-9_\-]*$/,
            password:/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])/,
            email:/^[a-zA-Z0-9.!#$%&’*+\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/
        };
        var isUsernameValid=false;
        var isEmailValid=false;
        var isPasswordValid=false;
        
        if(regex.username.test(newData.username)){
            isUsernameValid=true;
            console.log(isUsernameValid);
        }
        else{
            //alert user to enter a valid username
            console.log("invalid username!");
            $("#usernameErrorMsg").val("Choose a different username.");
        }
        if(regex.email.test(newData.email)){
            isEmailValid=true;
        }
        else{
            $("#emailErrorMsg").val("Choose a valid email.");
        }
        if((regex.password.test(newData.password)&&newData.password==newData.password2)||newData.password==""){
            isPasswordValid=true;
        }
        else{
            $("#pwdErrorMsg").val("Choose a valid password.");
        }
        if(isPasswordValid&&isUsernameValid&&isEmailValid){
            var dataToSend=JSON.stringify({
                "userName":newData.username,
                "password":newData.password,
                "email":newData.email
            });
        //console.log(dataToSend);
        ajaxRequest("/admins/" + adminData.id + "/profile", "PUT", dataToSend).done(function () {
            $("#pwdErrorMsg").html("Profile changed successfully.");
            console.log(ajaxData);
            localStorage.setItem("userName",ajaxData.userName);
            localStorage.setItem("email",ajaxData.email);
            adminData.userName=ajaxData.userName;
            adminData.email=ajaxData.email
            console.log(localStorage.getItem("userName")+" "+localStorage.getItem("email"));
            $("#editUsernameInput").val(ajaxData.userName);
            $("#editEmailInput").val(ajaxData.email);
            var template = "<p>{{userName}}'s</p>";
            html = Mustache.to_html(template, ajaxData);
            $("#heading").html(html);

            dfd.resolve();
        });
            //finish error messages and test with backend!
        return dfd.promise();
        }
        else{
            console.log("couldn't change password. inputs invalid");
        }
    });

        var searchString;

    function getDoctorsSearch() {
        console.log("search doctors begins");
        var dfd = $.Deferred();
        ajaxRequest("/doctors?name=" + searchString, "GET").done(function () {
            var icon = '<i class="fa fa-user-md tableIcon"></i>';
            generateDoctorTable();
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getUsersSearch() {
        console.log("user search not working yet.")
            // var dfd = $.Deferred();
            // ajaxRequest("/doctors?name=" + searchString, //"GET").done(function () {
            //    var icon = '<i class="fa fa-user-md tableIcon"></i>';
            //   generateDoctorTable();
            //   dfd.resolve();
            // });
            // return dfd.promise();
    }

    function setActionToUsersSearchBox() {
        $(document).on("keydown","#searchFieldUsers",function (event) {
            searchString = $("#searchFieldUsers").val();
            if (event.keyCode == 13) {
                if ($("#users").hasClass("selected")) {
                    getUsersSearch();
                }
            }
        });
    }
    
     function setActionToDoctorsSearchBox() {
       $(document).on("keydown","#searchFieldDoctors",function (event) {
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
