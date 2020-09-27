<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.miBudget.v1.entities.User" %>
    <%@ page import="javax.servlet.*" %>
	<%@ page import="java.time.*" %>
	<%@ page import="java.util.*" %>
<!DOCTYPE>
<html>
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
		<link href="images/wallet.ico" rel="icon" type="image/x-icon">
		<title>Profile</title>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<style>
			<!-- NEEDED -->
			body {
				display: block;
				margin: 8px;
			}
			h1.font1 {
				font-family: "Times New Roman", Times, serif;
				font-weight: bold;
				font-size: 2em;
				margin-block-start: 0.67em;
				margin-block-end: 0.67em;
				margin-inline-start: 0px;
				margin-inline-end: 0px;
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
	</head>
	<body>
		<div>
			<%
				Calendar cal = Calendar.getInstance();
				Date today = new Date();
				cal.setTime(today);
				int hours = cal.get(Calendar.HOUR_OF_DAY);
				if (hours >= 0 && hours < 12)  { %>
			<h1 id="greeting" class="font1">Good morning, <i>${Firstname} ${Lastname}</i></h1>
			<% }
			else if (hours >= 12 && hours < 16)  { %>
			<h1 id="greeting" class="font1">Good afternoon, <i>${Firstname} ${Lastname}</i></h1>
			<% }
			else  { %>
			<h1 id="greeting" class="font1">Good evening, <i>${Firstname} ${Lastname}</i></h1>
			<% }
			%>
			<br/>
			<form action="accounts" method="get">
				<%--<input style="cursor:pointer;" type="submit" value="Accounts"/>--%>
				<button type="submit">Accounts</button>
			</form>
			<hr/>
			<!-- <a href="">Categories</a> -->
			<%--<form action="cat" method="get">
                <input style="cursor:pointer;" type="submit" value="Categories and Transactions"/>
            </form>--%>
			<input type="button" value="Categories and Transactions" onclick="goToCat()" >
			<hr/>
			<input style="cursor:pointer;" type="submit" value="Goals"/>
			<hr/>
			<!-- Add this button to the profile page -->
			<input style="cursor:pointer;" type="submit" onclick="return logout();" value="Logout"/>
			<hr/>
			<p>Profile for ${Firstname} ${Lastname}</p>
		</div>
		<br/>
		<br/>
		<p id="date" class="footer" style="text-align:center">${dateAndTime}</p>
		<script>
			// Throwing Uncaught ReferenceError: Michael is not defined.... in the else section of this function
			/*function updateGreetingMessage() {
                let greetingElement = $("[id='greeting']");
                let hours = new Date().getHours();
                if (hours >= 0 && hours < 12) {
                    greetingElement.text('Good morning, ' + ${Firstname} + ' ' + ${Lastname});
			} else if (hours >=12 && hours < 16) {
				greetingElement.text('Good afternoon, ' + ${Firstname} + ' ' + ${Lastname});
			} else {
				greetingElement.text('Good evening, ' + ${Firstname} + ' ' + ${Lastname});
			}
		}*/
			$(function() {
				//updateGreetingMessage();
			});
			function goToCat() {
				$.ajax({
					type: "Get",
					url: "/miBudget/cat",
					data: {}
				}).success(function (response) {
				}).error(function (response) {
					console.log(response)
				}).done(function () {
				}).fail(function () {
				}).always(function (response) {
				});
			}
			function logout() {
				$.ajax({
					type: "Post",
					url: "/miBudget/logout",
					data: {}
				}).success(function (response) {
					return true;
				}).error(function (response) {
					//var res = JSON.stringify(response.responseText);
					//res = res.substring(1, res.length -1);
					// $("[id='changingText']").text(res).css({ 'font-weight': 'bold' }).fadeOut(20000, function() {
					// 	$("[id='changingText']").show().text('This text will change after taking an action.')
					// 			.css({ 'font-weight' : 'bold'});
					// });
					console.log("failed to logout");
					return false;
				}).done(function () {
				}).fail(function () {
				}).always(function (response) {
					// $("[id='changingText']").fadeOut(20000, function() {
					// 	$("[id='changingText']").show().text('This text will change after taking an action.')
					// 			.css({ 'font-weight' : 'bold'});
					// });
				});
			}
		</script>
	</body>

</html>