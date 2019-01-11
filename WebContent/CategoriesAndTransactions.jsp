<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.List" %>
	<%@ page import="java.util.Arrays" %>
	<%@ page import="java.util.ArrayList" %>
	<%@ page import="java.util.Iterator" %>
	<%@ page import="java.util.Set" %>
	<%@ page import="java.lang.String" %>
	<%@ page import="com.miBudget.v1.daoimplementations.MiBudgetDAOImpl" %>
	<%@ page import="com.miBudget.v1.daoimplementations.AccountDAOImpl" %>
	<%@ page import="com.miBudget.v1.daoimplementations.ItemDAOImpl" %>
	<%@ page import="com.miBudget.v1.entities.Account" %>
	<%@ page import="com.miBudget.v1.entities.User" %>
	<%@ page import="com.miBudget.v1.entities.UserAccountObject" %>
	<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="icon" type="image/x-icon" href="wallet.ico">
		<title>Categories and Transactions</title>
		<link rel="icon" type="image/x-icon" href="wallet.ico">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<script src="https://cdn.plaid.com/link/v2/stable/link-initialize.js"></script>
	    <style>
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
			}
			.images {
				display: all; /* all */
			}
			.updateButton {
			}
			.mainTable, th, td {
				border: 1px solid black;
			}
			p.changingText {
				font-weight: bold;
			}
			.outertable {}
			.innertable {
				visibility: hidden; /* visible */
			}
			.acct {}
			.border {
				border-style : solid;
			}
		</style>
	</head>
	<body>
		<h1>Categories and Transactions for ${Firstname} ${Lastname}</h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl(); %>
		<% AccountDAOImpl accountDAOImpl = new AccountDAOImpl(); %>
		<% ItemDAOImpl itemsDAOImpl = new ItemDAOImpl(); %>
		<h1>Categories and Transactions for <%= user.getFirstName() %> <%= user.getLastName() %></h1>
	
		<form action="Profile" method="get"> <!-- think about chaning this call to get -->
			<button type="submit">Profile Page</button>
		</form>
		<hr/>
		
		<div class="border" style="width:800px;">
			
			<div class="border" style="width:300px; float:left;"></div>
			
			<div class="border" style="width:300px; float:right:"></div>
			
		</div>

</body>
</html>