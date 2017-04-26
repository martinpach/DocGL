/*insert js here*/

$(document).ready(function(){

	var data = {
		idAdmin: localStorage.getItem("idadmin"),
        firstName: localStorage.getItem("firstName"),
        lastName: localStorage.getItem("lastName"),
        email: localStorage.getItem("email"),
        userName: localStorage.getItem("userName"),
        passwordChanged: localStorage.getItem("passwordChanged"),
        token: localStorage.getItem("token")
    };
    var ajaxData;

    //loads name to the page
    console.log(data);
    var usernameTemplate = "<p>{{firstName}} {{lastName}}</p>";
    var html = Mustache.to_html(usernameTemplate, data);
    $("#userName").html(html);

    $("#myProfile").on("click",function(){
        $("#contentTab").load('templates/admin_profile.html',function(){

        });

    });

    //logout
    $("#logout").on("click",function(){
        ajaxRequest('/auth/logout','POST').done(function(){
            localStorage.removeItem("token");
            window.location.href = 'index.html';
        });
    });




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