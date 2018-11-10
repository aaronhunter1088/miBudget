<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.v1.miBudget.daoimplementations.AccountDAOImpl" %>
<%@ page import="com.v1.miBudget.entities.Account" %>
<%@ page import="com.v1.miBudget.entities.User" %>
<%@ page import="javax.swing.ImageIcon" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Profile</title>
		<link rel="icon" type="image/x-icon" href="wallet.ico">
	</head>
	<body>
		<h1>Profile for ${Firstname} ${Lastname}</h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<h1>Profile for <%= user.getFirstName() %> <%= user.getLastName() %></h1>
	
		<form action="Welcome" method="post">
			<br/><!-- Add a space between the buttons -->
			<button type="submit">Welcome Page</button>
		</form>
		<hr/>
		<button id="link-button">Link Account</button>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<script src="https://cdn.plaid.com/link/v2/stable/link-initialize.js"></script>
		<script>
		function updateInnerTable() {
			console.log('Inside updateInnerTable()...');
			
		    $("#accountsTable").toggle();
		};
		$(function() {
			$('.button').removeAttr('disabled');
			console.log('Document.onReady()...')
			$("h4" > '#change').hide().show();
			$("#innerTable").hide();
			
		});
		(function($) {
		  var handler = Plaid.create({
		    clientName: 'Plaid Walkthrough Demo',
		    env: 'sandbox',
		    // Replace with your public_key from the Dashboard
		    key: 'f0503c4bc8e63cc6c37f07dbe0ae2b',
		    product: ["transactions"],
		    // Optional, use webhooks to get transaction and error updates
		    webhook: 'https://requestb.in',
		    onLoad: function() {
		      // Optional, called when Link loads
		      console.log("HTML page has finished loading.")
		    },
		    onSuccess: function(public_token, metadata) {
		      console.log("public_token: " + public_token);
		      var accounts = [], account = [];
		      var _id, _name, _mask, _type, _subtype;
		      for(var i = 0; i < metadata.accounts.length; i++) {
		        console.log("account id: " + metadata.accounts[i].id);
		        console.log("name: " + metadata.accounts[i].name);
		        console.log("mask: " + metadata.accounts[i].mask);
		        console.log("type: " + metadata.accounts[i].type);
		        console.log("subtype: " + metadata.accounts[i].subtype);
		
		        _id = metadata.accounts[i].id;
		        _name = metadata.accounts[i].name;
		        _mask = metadata.accounts[i].mask;
		        _type = metadata.accounts[i].type;
		        _subtype = metadata.accounts[i].subtype;
		
		        account = {id: _id, name: _name, mask: _mask, type: _type, subtype: _subtype};
		        
		        accounts[i] = account;
		      }
		
		      var jsonAccount = JSON.stringify(accounts);
		      
		      $.ajax({
		    	 type: "POST",
		    	 url: "authenticate",
		    	 data: {
		    		 public_token: public_token,
		             link_session_id: metadata.link_session_id,
		             accounts: jsonAccount,
		             institution_name: metadata.institution.name,
		             institution_id: metadata.institution.institution_id
		    	 },
		    	 
		      }).done(function (response) {
		    	  location.reload();
		          var usersAccounts = <%= user.getAccountIds().size() %>;
		          usersAccounts = parseInt(metadata.accounts.length) + usersAccounts;
		          console.log(response);
		          console.log(parseInt(metadata.accounts.length));
		          console.log(usersAccounts);
		          <% 
		          int usersAccts = user.getAccountIds().size();
		          session.setAttribute("changeText", "You have successfully loaded " + Integer.toString(usersAccts) + " accounts.");
		          session.setAttribute("noOfAccts", usersAccts);
		          %>
		          //$("NoOfAccts").html = 'Accounts - ' + usersAccounts ;
		          //console.log('Updating accountsTable...');
		      }).fail(function (response) {
		    	  document.getElementById("change").innerHTML = "We didn't load the accounts because the bank already exists in your profile.";
		    	  console.log(response);
		    	  var text = "some text" + response.responseText
		    	  console.log("responseText: " + response.responseText);
		      }).always(function (response) {
		    	  
		      });
			  
		    },
		    onExit: function(err, metadata) {
		      // The user exited the Link flow.
		      if (err != null) {
		        // The user encountered a Plaid API error prior to exiting.
		      }
		      
		      // metadata contains information about the institution
		      // that the user selected and the most recent API request IDs.
		      // Storing this information can be helpful for support.
		    },
		    onEvent: function(eventName, metadata) {
		      // Optionally capture Link flow events, streamed through
		      // this callback as your users connect an Item to Plaid.
		      // For example:
		      // eventName = "TRANSITION_VIEW"
		      // metadata  = {
		      //   link_session_id: "123-abc",
		      //   mfa_type:        "questions",
		      //   timestamp:       "2017-09-14T14:42:19.350Z",
		      //   view_name:       "MFA",
		      // }
		      console.log("Inside onEvent()");
		      var selectEventName = "SELECT_INSTITUTION"; // the user has selected an institution
		      console.log(eventName);
		      if (eventName == "SELECT_INSTITUTION") {
		    	  console.log("User has selected an institution.");
		    	  console.log(metadata);
		    	  console.log(metadata.institution_id);
		    	  console.log(metadata.institution_name);
		      }
		    }
		  });
		
		  $('#link-button').on('click', function(e) {
		    handler.open();
		  });
		})(jQuery);
		</script>
		<hr>
		<h4 id="change">${changeText}</h4>
		<br/>
		<p>Items for '${Firstname}' '${Lastname}' </p>
		<br/>
		<p id="NoOfAccts">Accounts : ${NoOfAccts}</p>
		<!-- Create a table that lists all accounts -->
		<!-- Each account should have an update button and a delete button -->
		<!-- Update updates the Item -->
		<!-- Delete deletes the Item -->
	
		<div class="mainTable" id="accountsTable">
			<table class="outerTable" id="outerTable">
				<% 
				int noOfAccts = user.getAccountIds().size(); 
				List<ImageIcon> banksImagesList = {
						// list img src of bank images
						ImageIcon chaseLogo = new ImageIcon("WebContent/chaseLogo.jpg")
				};
		        Iterator iter = banksImagesList.iterator();
				while (iter.hasNext()) {
				%> 
				<!-- [Bank Logo | Update | Delete] --> 
				<tr id="acct"> 
					<td>
					 	<div id="logo"><%= iter.next() %></div> <!-- Logo --> 
				 	</td> 
				 	<!-- Whitespace -->
				 	<td>
				 		<!-- Update button --> 
				 		<button id="link--update-button">Update Account</button>
				 	</td> 
				 	<!-- Whitespace -->
				 	<td>
				 		<!-- Delete button -->
				 		<!-- Goes to Delete.java and performs doPost --> 
				      <form id="delete" method="post" action="Delete"> 
					    <button type="submit" formmethod="post">Delete Account</button>
					  </form> 
				    </td> 
				</tr> 
				<% } %>
			</table>
			<!-- Space for readability -->
			<table class="innerTable" id="innerTable">
				<th> 
					<button type="button" onclick="hideInnerTable()">Back</button>
				</th>
				<% 
				//int noOfAccts = user.getAccountIds().size(); 
				List<String> acctIds = user.getAccountIds(); 
		        Iterator innerIter = acctIds.iterator();
				while (innerIter.hasNext()) {
				%> 
				<!-- [Name | Mask | Subtype | Delete] --> 
				<tr id="acct"> 
					<td>
				 	<!-- Account -->
				 		<!-- Name | Mask | Subtype -->
					  	<td>
					  		<%= innerIter.next() %> <!-- Name --> 
					  	</td> 
				 			<!-- Whitespace - Mask --> 
				 		<td>
				 		</td>
				 			<!-- Whitespace - Subtype -->
				 		<td> 
				 		</td>
				 	</td> 
				 	<!-- Whitespace -->
				 	<td>
				 		<!-- Delete button -->
				 		<!-- Goes to Delete.java and performs doPost --> 
				      <form id="delete" method="post" action="Delete"> 
					    <button type="submit" formmethod="post">Delete Account</button>
					  </form> 
				    </td> 
				</tr> 
				<% } %>
			</table>
		</div>
		
		<script>
			$("#logo").on("click" function() {
				$("#outerTable").hide();
				$("#innerTable").show();
			});
			function hideInnerTable() {
				$("#innerTable").hide();
				$("#outerTable.").show();
			};
		</script>
	</body>
</html>

