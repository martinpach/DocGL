/*insert js here*/
$(document).ready(function () {
    var data = {
        id: localStorage.getItem("id")
        , firstName: localStorage.getItem("firstName")
        , lastName: localStorage.getItem("lastName")
        , email: localStorage.getItem("email")
        , userName: localStorage.getItem("userName")
        , passwordChanged: localStorage.getItem("passwordChanged")
        , token: localStorage.getItem("token")
    };
    var ajaxData;

    //loads name to the page
    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, data);
    $("#userName").html(html);

    //load profile
    $("#myProfile").on("click",function(){
        $("#container").load('templates/admin_profile.html',function(){
            var template = "<p>{{userName}}'s</p>";
            var templateUsername="<p>{{userName}}</p>";
            var templateMail="<p>{{email}}</p>";
            var templatePassword="<p>************</p>";
            var html = Mustache.to_html(template, data);
            $("#heading").html(html);
            html=Mustache.to_html(templateUsername,data);
            $("#username").html(html);
            html=Mustache.to_html(templateMail,data);
            $("#email").html(html);
            html=Mustache.to_html(templatePassword,data);
            $("#password").html(html);
        });       
    });

    $("#home").on("click",function(){
        $(this).addClass("selected");
        $("#users, #doctors").removeClass("selected");
        $("#container").load('templates/admin_home.html');        
    });

    $("#home").trigger("click");

    $("#doctors").on("click",function(){
        $(this).addClass("selected");
        $("#users, #home").removeClass("selected");
        $("#container").load('templates/admin_doctors.html');  
        getDoctors();      
    });

    $("#users").on("click",function(){
        $(this).addClass("selected");
        $("#home, #doctors").removeClass("selected");
        $("#container").load('templates/admin_users.html');
        getUsers();
    });

    //logout
    $("#logout").on("click",function(){
        ajaxRequest('/auth/logout','POST').done(function(){
            localStorage.removeItem("token");
            window.location.href = 'index.html';
        });
    });


    function getDoctors (){
        ajaxRequest("/doctors","GET").done(function(){
            console.log(ajaxData);
        });

    }

    function getUsers(){
        ajaxRequest("/patients","GET").done(function(){
            console.log(ajaxData);
        });

    }

    
    //ajax request function
    function ajaxRequest(url, requestType, dataToSend) {
        var dfd = $.Deferred();
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + data.token);
            },
            url: 'http://localhost:8085' + url,
            type: requestType,
            data: dataToSend,
            contentType: 'application/json',
            success: function (data) {
                /*if (data != null) */ajaxData = data;
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