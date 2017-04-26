/*insert js here*/
$(document).ready(function () {
    var data = {
        idAdmin: localStorage.getItem("idadmin")
        , firstName: localStorage.getItem("firstName")
        , lastName: localStorage.getItem("lastName")
        , email: localStorage.getItem("email")
        , userName: localStorage.getItem("userName")
        , passwordChanged: localStorage.getItem("passwordChanged")
        , token: localStorage.getItem("token")
    };
    var ajaxData;
    //loads username to the page
    var usernameTemplate = "<p>{{userName}}</p>";
    var html = Mustache.to_html(usernameTemplate, data);
    $("#userName").html(html);
    //logout
    $("#logout").on("click", function () {
        $.ajax({
            url: 'http://localhost:8085/auth/logout'
            , type: 'POST'
            , contentType: 'application/json'
            , beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + data.token);
            }
            , success: function (data) {
                localStorage.removeItem("token");
                window.location.href = 'index.html';
            }
            , error: function () {
                console.log("Error!");
            }
        });
    });
});