$(document).ready(function(){
	$("#username").on("input",function(){
		checkInput($(this));				
	});

	$("#username").on("input",function(){
		checkInput($(this));				
	});

	$("#loginBtn").on("click",function(){
		logIn();
	});

	$(document).keydown(function(event) {
        if (event.keyCode == 13) {
            login();
        }
    });

	function checkInput(inputField){
		var input = inputField.val();
		var latestC=input.charAt(input.length-1)
		if(latestC.match(/^[0-9a-zA-Z_\-.@#]$/)==null){
			inputField.val(inputField.val().slice(0, -1));
		}
	}

	function logIn(){
		$("#errorMsg").hide();

		var username=$("#username").val();
		var password=$("#password").val();

		if(username.length>2&&password.length>2){
			$.ajax({
				url: 'http://localhost:8080/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    "username": username,
                    "password": password
                }),
                success:function(data){
                	console.log(data);
                	var admin=data.admin;
                    localStorage.setItem("username", admin.photoUrl);
                    localStorage.setItem("token", data.token);
                    //window.location.href = "home.html"; not yet
                },
                error:function(){ 
                	$("#errorMsg").html("Incorrect username or password.");
                	$("#errorMsg").show();
                }
                	
			});
		}
		else{
			$("#errorMsg").html("Please enter username and password.");
			$("#errorMsg").show();
		}
	}
});