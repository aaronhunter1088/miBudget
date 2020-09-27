<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<title>Forgot Password</title>
<style>
    <!-- NEEDED -->
    body {
        display: block;
        margin: 8px;
    }
</style>
<body bgcolor="#32CD32">

<h1>Forgot Password</h1>
<h3>Choose which method you'd prefer to reset your password with.</h3>
<input type="radio" id="cellphone" onclick="modifyPlaceHolderText(this)" value="cellphone" checked> Cellphone </input>
<input type="radio" id="email" onclick="modifyPlaceHolderText(this)" value="email"> Email </input>
<hr/>
<script>
function modifyPlaceHolderText(element) {
  if (document.getElementById("cellphone").selected == false) {
	  document.getElementById("cellphone").checked = true;
		document.getElementById("cellphone").selected = true;
	  	document.getElementById("email").selected = false;
	  	document.getElementById("email").checked = false;
  	var radioInput = document.getElementById("cellphone").value;
    document.getElementById("input").placeholder = "Enter " + radioInput;
  } else {
	document.getElementById("cellphone").checked = false;
	document.getElementById("cellphone").selected = false;
  	document.getElementById("email").selected = true;
  	document.getElementById("email").checked = true;
  	var radioInput = document.getElementById("email").value;
    document.getElementById("input").placeholder = "Enter " + radioInput;
  }
}
</script>
<form action="ForgotPassword" method="post">
  <div class="container">
    <input type="text" id="input" placeholder="Enter cellphone" name="InputSelection" required>
	<br/>
	<hr/>
    <button type="submit">Get Password</button>
    <input class="button" type="button" onclick="window.location.replace('index.html')" value="Cancel" />
   	<br/>
   	<hr/>
  </div>
</form>
</body>
</html>