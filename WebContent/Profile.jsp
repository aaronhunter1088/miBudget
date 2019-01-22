<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%><%@ page import="com.miBudget.v1.entities.User" %><%@ page import="javax.servlet.*" %><%@ page import="java.util.List" %><%@ page import="java.util.Arrays" %><%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
		<link href="wallet.ico" rel="icon" type="image/x-icon">
		<title>Profile</title>
		<link href="wallet.ico" rel="icon" type="image/x-icon">
	</head>
	<body>
		<h1>Welcome, <i>${Firstname}</i> : ${accountsSize}</h1><br>
		<% User user = (User)session.getAttribute("user"); %>
		<br>
		<form action="Accounts" method="get">
			<input class="button" type="submit" value="Accounts">
		</form>
		<hr>
		<!-- <a href="">Categories</a> -->
		<form action="CAT" method="get">
			<button type="submit">Categories and Transactions</button>
		</form>
		<hr>
		<a href="">Goals</a>
		<hr>
		<!-- Add this button to the profile page -->
		<form action="Logout" method="post">
			<button class="button" type="submit">Logout</button>
		</form>
		<hr>
		<p>Session object user firstname='${Firstname}' lastname='${Lastname}'</p>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js">
		</script> 
		<script>
		$(function() {
		   $('.button').disabled = false;
		});
		</script>
	</body>
</html>