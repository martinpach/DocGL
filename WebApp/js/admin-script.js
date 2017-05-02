/*insert js here*/
$(document).ready(function () {
    var data = {
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
    var countUsers;
    var countDocs;
    getCountOfDoctors();
    getCountOfUsers();


    //loads name to the page
    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, data);
    $("#userName").html(html);

    //load profile
    $("#myProfile").on("click", function () {
        $("#container").load('templates/admin_profile.html', function () {
            var template = "<p>{{userName}}'s</p>";
            var templateUsername = "<p>{{userName}}</p>";
            var templateMail = "<p>{{email}}</p>";
            var templatePassword = "<p>************</p>";
            var html = Mustache.to_html(template, data);
            $("#heading").html(html);
            html = Mustache.to_html(templateUsername, data);
            $("#username").html(html);
            html = Mustache.to_html(templateMail, data);
            $("#email").html(html);
            html = Mustache.to_html(templatePassword, data);
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
    });

    //logout
    $("#logout").on("click", function () {
        var dfd = $.Deferred();
        ajaxRequest('/auth/logout', 'POST').done(function () {
            localStorage.removeItem("token");
            window.location.href = 'index.html';
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

    $(document).on("click", "#sortIdUsers", function () { //not done
        event.preventDefault();
        sortByUsers = "id";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortNameUsers", function () {
        event.preventDefault();
        sortByUsers = "lastName";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortJoinedUsers", function () {
        event.preventDefault();
        sortByUsers = "registration_date";
        wayUsers = wayUsers == "desc" ? "asc" : "desc";
        getUsers(start, limit, sortByUsers, wayUsers);
        $(this).find("i").toggleClass("fa-sort-asc");
    });

    $(document).on("click", "#sortIdDocs", function () { //not done
        event.preventDefault();
        sortByDocs = "id";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        $(this).find("i").toggleClass("fa-sort-asc");

    });

    $(document).on("click", "#sortNameDocs", function () {
        event.preventDefault();
        sortByDocs = "lastName";
        wayDocs = wayDocs == "desc" ? "asc" : "desc";
        getDoctors(start, limit, sortByDocs, wayDocs);
        $(this).find("i").toggleClass("fa-sort-asc");
    });

    $(document).on("click", "#sortRatingDocs", function () {
        event.preventDefault();
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
            var likeCount = ajaxData.likes;
            $("#countLikes").html(likeCount);
            dfd.resolve();
        });
        return dfd.promise();
    }

    //edit profile

    //toggle edit fields
    $(document).on("click", "#editUsername", function () {
        event.preventDefault();
        $("#username").toggle();
        $("#changeUsernameDiv").toggle();
    });

    $(document).on("click", "#editEmail", function () {
        event.preventDefault();
        $("#email").toggle();
        $("#changeEmailDiv").toggle();

    });

    $(document).on("click", "#editPassword", function () {
        event.preventDefault();
        $("#password").toggle();
        $("#changePasswordDiv").toggle();
    });

    //send changed info to the server,to be finished
    $(document).on("click", "#submitUsername", function () {
        var newUsername = $("#editUsernameInput").val();

    });

    $(document).on("click", "#submitEmail", function () {
        var newEmail = $("#editEmailInput").val();

    });

    $(document).on("click", "#submitPassword", function () {
        event.preventDefault();
        var newPwd = $("#editPwd1").val();
        var newPwd2 = $("#editPwd2").val();
        var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])");
        var ok;
        if (newPwd == "")
            $("#pwdErrorMsg").html("Please enter something.");
        else {
            if (newPwd.length > 5 && regex.test(newPwd) && newPwd == newPwd2) {
                $("#pwdErrorMsg").html("");
                ok = true;
                newPwd = JSON.stringify({
                    "password": newPwd
                });
            }

            ajaxRequest("admins/" + data.id + "/profile/password", "PUT", newPwd).done(function () {
                $("#pwdErrorMsg").html("Password changed successfully.");
            });
            //not working yet
        }
    });

    //ajax request function
    function ajaxRequest(url, requestType, dataToSend) {
        var dfd = $.Deferred();
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + data.token);
            },
            url: 'http://localhost:8085/api/' + url,
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
