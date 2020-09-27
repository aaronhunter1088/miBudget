<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sign Up</title>
<link rel="icon" type="image/x-icon" href="wallet.ico">
<style>
::placeholder {
	color: black;
<!-- NEEDED -->
body {
	display: block;
	margin: 8px;
}
</style>
</head>
<body bgcolor="#1E90FF">
<h1>Sign Up</h1>
<form id="Register" action="Register" onsubmit="return validateFields()" method="post">
	<input type="hidden" name="Validated" value="false"></input>
	<input type="text" placeholder="Enter first name" tabindex="1" name="Firstname" value="${Firstname}" required="required"/>
	<br/>
	<hr/>
	
	<input type="text" placeholder="Enter last name" tabindex="2" name="Lastname" value="${Lastname}" required/>
	<br/>
	<hr/>
	
    <input type="text" placeholder="Enter cellphone" tabindex="3" name="Cellphone" value="${Cellphone}" required/>
	<br/>
	<hr/>
	
	<input type="text" placeholder="Enter email" tabindex="4" name="Email" value="${Email}" required/>
	<br/>
	<hr/>
	
    <input type="password" placeholder="Enter password" tabindex="5" name="Password" value="${Password}" required/>
	</br/>
	<hr/>
	
    <input type="password" placeholder="Repeat password" tabindex="6" name="Password-repeat" value="${PasswordRepeat}" required/>
	<br/>
	<hr/>
	
    <label>
      <input type="checkbox" checked="checked" name="remember" style="margin-bottom:15px"/> Remember me
    </label>

    <p>By creating an account, you agree to our <a href="#" style="color:darkblue">Terms & Privacy</a>.</p>

    <div class="clearfix">
      <input class="button" type="button" onclick="window.location.replace('index.html')" value="Cancel" />
      <button type="submit" class="signupbtn" tabindex="6">Sign Up</button>
    </div>
</form>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
<script>
function validateFields() {
	alert("This JavaScript runs before submitting the page.");
	/*
	let value = prompt("Enter true or false","");
	if (value.toLowerCase() == "true") {
		alert("Submitting the form...");
		return true;
	} else {
		alert("Something is wrong. Please check your fields.");
		return false;
	} */
	let validate = false;
	let count = 0;
	
	// Check First Name
	let firstName = $('#Register') // get the form
	.find('input[name=Firstname]') // locate the input element 
	.val(); // get its value
	if (firstName === "") {
		alert("First Name cannot be blank.");
		return validate;
	} else {
		count++; // 1
	}
	
	// Check Last Name
	let lastName = $('#Register') 
	.find('input[name=Lastname]') 
	.val();
	if (lastName === "") {
		alert("Last Name cannot be blank.");
		return validate;
	} else {
		count++; // 2
	}
	
	// Check cellphone
	let cellphone = $('#Register')
	.find('input[name=Cellphone]')
	.val();
	if (cellphone === "") {
		alert("Cellphone cannot be blank.");
		return validate;
	}
	else if (cellphone.length != 10) {
		alert("Invalid cellphone format. Please provide 10 digits.");
		return validate;
	}
	else if (isNaN(cellphone)) {
		alert("Invalid cellphone format. Cellphones cannot include letters.");
		return validate;
	} else {
		count++; // 3
	}
	
	// Check email
	let email = $('#Register') 
	.find('input[name=Email]') 
	.val();
	if (email === "") {
		alert("Email cannot be blank.");
		return validate;
	}
	else if (email.indexOf('@') <= -1) {
		alert("Invalid email format. You must include the \'@\' character.");
		return validate;
	}
	else if (email.indexOf('.') <= -1) {
		alert("Invalid email format. You must have a \'.\' after the domain.");
		return validate;
	} else {
		count++; // 4
	}
	
	// Check if password-repeat is the same as password
	let password = $('#Register')
	.find('input[name=Password]')
	.val();
	let passwordRepeat = $('#Register')
	.find('input[name=Password-repeat]')
	.val();
	if (password !== passwordRepeat) {
		alert("Repeated password does not match Password!");
		return validate;
	} else {
		count++; // 5
	}
	
	// Check if all fields have been validated
	if (count == 5) {
		validate = true;
		$('#Register')
		.find('input[name=Validated]')
		.val("true");
	}
	
	return validate;
};
</script>
</body>
</html>