<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.v1.miBudget.entities.User" %>
<%@ page import="javax.servlet.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" type="image/x-icon" href="wallet.ico">
<title>Welcome</title>
<link rel="icon" type="image/x-icon" href="wallet.ico">
</head>
<body>

<h1>Welcome, <i>${Firstname}</i> : ${accountsSize}</h1>
<br/>
<%
User user = (User)session.getAttribute("user");
if (!user.getFirstName().equals(null)) {
%>
<h1>Welcome, <i><%= user.getFirstName() %></i> : <%= user.getAccountIds().size() %></h1>
<% } %>
<!-- <input class="button" type="button" onclick="window.location.replace('Profile.jsp')" value="Profile" /> -->
<br/>
<form action="Profile" method="get">
<input class="button" type="submit" value="Profile" />
</form>
<hr/>
<a href="">Categories</a>
<hr/>
<a href="">Transactions</a>
<hr/>
<a href="">Goals</a>
<hr/>
<!-- Add this button to the profile page -->
<form action="Logout" method="post">
<button class="button" type="submit">Logout</button>
</form>
<hr/>
<p> Session object user firstname='${Firstname}' lastname='${Lastname}'</p>



<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
<script>
$(function() {
	$('.button').disabled = false;
});
</script>
</body>
</html>