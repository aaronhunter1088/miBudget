<!-- This page is no longer used. Replaced by Profile.jsp -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.miBudget.v1.entities.User" %>
    <%@ page import="javax.servlet.*" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.Arrays" %>
    <%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
		<link href="wallet.ico" rel="icon" type="image/x-icon">
		<title>NO LONGER USED</title>
		<link href="wallet.ico" rel="icon" type="image/x-icon">
	</head>
	<style>
		<!-- NEEDED -->
		body {
			display: block;
			margin: 8px;
	</style>
	<body>
		<h1>Welcome, <i>${Firstname}</i> : ${accountsSize}</h1><br>
		<%
		User user = (User)session.getAttribute("user");
		if (!user.getFirstName().equals(null)) {
		%>
		<h1>Welcome, <i><%= user.getFirstName() %></i> : <%= user.getAccountIds().size() %></h1><%} %><!-- <input class="button" type="button" onclick="window.location.replace('Profile.jsp')" value="Profile" /> -->
		<br>
		<form action="Profile" method="get">
			<input class="button" type="submit" value="Profile">
		</form>
		<hr>
		<a href="">Categories</a>
		<hr>
		<a href="">Transactions</a>
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