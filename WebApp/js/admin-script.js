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
    var start=0;
    var limit=4;
    var countUsers;
    var countDocs;
    getCountOfDoctors();
    getCountOfUsers();

    
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
        $("#paginationContainer").hide();
        getLikes();
        getAppointmentCount();
         
    });

    $("#home").trigger("click");

    $("#doctors").on("click",function(){
        $(this).addClass("selected");
        $("#users, #home").removeClass("selected");
        $("#container").load('templates/admin_doctors.html');  
        getDoctors(start,5);
        setText(countDocs);
        setButtons(countDocs);
        $("#paginationContainer").show();      
    });

    $("#users").on("click",function(){
        $(this).addClass("selected");
        $("#home, #doctors").removeClass("selected");
        $("#container").load('templates/admin_users.html');
        getUsers(start,5);
        setText(countUsers);
        setButtons(countUsers); 
        $("#paginationContainer").show();
    });

    //logout
    $("#logout").on("click",function(){
        var dfd = $.Deferred();
        ajaxRequest('/auth/logout','POST').done(function(){
            localStorage.removeItem("token");
            window.location.href = 'index.html';
            dfd.resolve();
        });
        return dfd.promise();
    });

    //get doctor list
    function getDoctors (){
        var dfd= $.Deferred();
        ajaxRequest("/doctors?limit="+limit+"&start="+start,"GET").done(function(){
            var icon='<i class="fa fa-user-md tableIcon"></i>';
            generateDoctorTable();
            dfd.resolve();
        });
        return dfd.promise();
    }
    //count of items for pagination purpose
    function getCountOfDoctors(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors?limit="+limit+"&start="+start,"GET").done(function(){
            countDocs=ajaxData.length;
            console.log(countDocs);
            dfd.resolve();
        })
        return dfd.promise();
    }

    //get user list
    function getUsers(start,limit){
        var dfd= $.Deferred();
        ajaxRequest("/patients?limit="+limit+"&start="+start,"GET").done(function(){
            generateUserTable();
            dfd.resolve();            
        });
        return dfd.promise();
    }
    //count of items for pagination purpose
    function getCountOfUsers(){
        var dfd=$.Deferred();
        ajaxRequest("/patients?limit="+limit+"&start="+start,"GET").done(function(){
            countUsers=ajaxData.length;
            dfd.resolve();
        });
        return dfd.promise();
    }

    //pagination
    function setButtons(count){
        if(count-start>=5)
            $("#arrowRight").removeClass("disabledBtn");
        else
            $("#arrowRight").addClass("disabledBtn");
        if(start>=6)
            $("#arrowLeft").removeClass("disabledBtn");
        else
            $("#arrowLeft").addClass("disabledBtn");
    }

    function setText(count){
        if(count<0){
            $("#paginationText").hide();
        }
        else{
            var from=start+1;
            var to;
            if(start+4>count)
                to=count;
            else
                to=start+4;
        }

        var paginationText= "Showing "+from+" - "+to+" from " +count;
        $("#paginationText").html(paginationText);

    }

    $("#arrowLeft").on("click",function(){
        start -=5;
        if($("#doctors").hasClass("selected"))
            getDoctors(start,5);
            setText(countDocs);
            setButtons(countDocs);
        if($("#users").hasClass("selected"))
            getUsers(start,5);
            setText(countUsers);
            setButtons(countUsers);
    });

    $("#arrowRight").on("click", function(){
        console.log("click");
        start +=5;
        if($("#doctors").hasClass("selected"))
            getDoctors(start,5);
            setText(countDocs);
            setButtons(countDocs);
        if($("#users").hasClass("selected"))
            getUsers(start,5);
            setText(countUsers);
            setButtons(countUsers);
    });

    //generate table for users/docs
    function generateDoctorTable(){
        var i=0;
        var icon='<i class="fa fa-user-md tableIcon"></i>';
        for(i=0;i<ajaxData.length;i++){
            $("#tableDoctors").append('<tr>'+
                '<td>'+icon+'</td>'+
                '<td>'+ajaxData[i].id+'</td>'+
                '<td>'+ajaxData[i].firstName+" "+ajaxData[i].lastName+'</td>'+
                '<td>'+ajaxData[i].specialization+'</td>'+
                '<td>'+ajaxData[i].email+'</td>'+
                '<td><p class="like"><i class="fa fa-heart heart"></i>'+ajaxData[i].likes+'</p></td>'+
                '</tr>');        
        }
    }

    function generateUserTable(){
        var i=0;
        var icon='<i class="fa fa-user tableIcon"></i>';
        for(i=0;i<ajaxData.length;i++){
            $("#tableUsers").append('<tr>'+
                '<td>'+icon+'</td>'+
                '<td>'+ajaxData[i].id+'</td>'+
                '<td>'+ajaxData[i].firstName+" "+ajaxData[i].lastName+'</td>'+
                '<td>'+ajaxData[i].email+'</td>'+
                '<td>'+ajaxData[i].registrationDate+'</td>'+
                '</tr>');        
        }

    }

    //sorting, not ready yet
    $(document).on("click" "#sortNameUsers",function(){
        event.preventDefault();
    });

    $("sortJoinedUsers").on("click",function(){
        alert("whee!");
    });

    $("#sortNameDocs").on("click",function(){
        alert("whee!");
    });

    $("#sortRatingDocs").on("click",function(){
        alert("whee!");
    });

    //get statistics
    function getAppointmentCount(){
        var dfd = $.Deferred();
        ajaxRequest("/appointments/count","GET").done(function(){
           var appointmentCount=ajaxData.count;
            $("#countAppointments").html(appointmentCount);
            dfd.resolve();
        });
        return dfd.promise();
    }

    function getLikes(){
        var dfd=$.Deferred();
        ajaxRequest("/doctors/likes","GET").done(function(){
            var likeCount=ajaxData.likes;
            $("#countLikes").html(likeCount);
            dfd.resolve();
        });
        return dfd.promise();
    }



    function editUsername(){

    }
    
    function editPassword(){

    }

    function editEmail(){

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