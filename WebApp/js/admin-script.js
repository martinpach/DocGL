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
    console.log(localStorage.getItem("firstName"));

    var usernameTemplate = "{{userName}}";
    var html = Mustache.to_html(usernameTemplate, data.userName);
    $("#userName").html(html);//sets null since admin object doesn't contain all data yet

	
});