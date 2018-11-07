<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>

<h1>Welcome, ${Firstname} ${Lastname} : ${Accounts}</h1>
<hr />
<a href="">Home</a>
<a href="">Categories</a>
<a href="">Transactions</a>
<a href="">Goals</a>
<br/>
<!-- <input class="button" type="button" onclick="window.location.replace('Profile.jsp')" value="Profile" /> -->
<br/>
<form action="Profile" method="post">
<button type="submit">Profile</button> <!-- 20 and 21 -->
<!-- <a href="Profile.html">Profile</a> -->
</form>
<br/>
<!-- Add this button to the profile page -->
<form action="Logout" method="post">
<button type="submit">Logout</button>
</form>
<br/>
<hr/>
<p> Session object user firstname='${Firstname}' lastname='${Lastname}'</p>



</script>
</body>
</html>