<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign Up</title>
</head>
<body bgcolor="#1E90FF">
<h1>Sign Up</h1>
<form action="Register" method="post">
	<input type="text" placeholder="Enter first name" name="Firstname" value="${user.firstname}" required/>
	<br/>
	<hr/>
	<input type="text" placeholder="Enter last name" name="Lastname" value="${user.lastname}" required/>
	<br/>
	<hr/>
    <input type="text" placeholder="Enter cellphone" name="Cellphone" value="${user.cellphone}" required/>
	<br/>
	<hr/>
    <input type="password" placeholder="Enter Password" name="Password" required/>
	</br/>
	<hr/>
    <input type="password" placeholder="Repeat Password" name="Password-repeat" required/>
	<br/>
	<hr/>
    <label>
      <input type="checkbox" checked="checked" name="remember" style="margin-bottom:15px"/> Remember me
    </label>

    <p>By creating an account you agree to our <a href="#" style="color:black">Terms & Privacy</a>.</p>

    <div class="clearfix">
      <input class="button" type="button" onclick="window.location.replace('index.html')" value="Cancel" />
      <button type="submit" class="signupbtn">Sign Up</button>
    </div>
</form>
</body>
</html>