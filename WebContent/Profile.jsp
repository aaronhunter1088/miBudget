<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.miBudget.v1.entities.User" %>
    <%@ page import="javax.servlet.*" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.Arrays" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="java.time.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
		<link href="wallet.ico" rel="icon" type="image/x-icon">
		<title>Profile</title>
		<style>
			h1.font1 {
				font-family: "Times New Roman", Times, serif;
			}
			p.a {
			    font-family: "Times New Roman", Times, serif;
			}
			
			p.b {
			    font-family: Arial, Helvetica, sans-serif;
			}
			.footer {
			    position: fixed;
			    left: 0;
			    bottom: 0;
			    width: 100%;
			    background-color: white;
			    color: black;
			    text-align: center;
			    text-size: 30%;
			}
		</style>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
	</head>
	<body>
		<h1 class="font1">Welcome, <i>${Firstname}</i> : ${accountsSize}</h1>
		<br>
		<% User user = (User)session.getAttribute("user"); %>
		<br>
		<form action="Accounts" method="get">
			<input style="cursor:pointer;" type="submit" value="Accounts"/>
		</form>
		<hr>
		<!-- <a href="">Categories</a> -->
		<form action="CAT" method="get">
			<input style="cursor:pointer;" type="submit" value="Categories and Transactions"/>
		</form>
		<hr>
		<a id="checked" href="">Goals</a>
		<hr>
		<!-- Add this button to the profile page -->
		<form action="Logout" method="post">
			<input style="cursor:pointer;" type="submit" value="Logout"/>
		</form>
		<hr>
		<p>Session object user firstname='${Firstname}' lastname='${Lastname}'</p>
		<br/>
		<br/>
		<p id="date" class="footer" style="text-align:center">${instantNow}</p>
		
		
	</body>
	<script>
		$(function() {
			
		});
	</script>
</html>