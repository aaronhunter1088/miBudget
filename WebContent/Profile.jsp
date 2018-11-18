<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.v1.miBudget.daoimplementations.AccountDAOImpl" %>
<%@ page import="com.v1.miBudget.entities.Account" %>
<%@ page import="com.v1.miBudget.entities.User" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="icon" type="image/x-icon" href="wallet.ico">
		<title>Profile</title>
		<link rel="icon" type="image/x-icon" href="wallet.ico">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<script src="https://cdn.plaid.com/link/v2/stable/link-initialize.js"></script>
	</head>
	<style>
		img {
			width : 50px;
			height: 50px;
			border-style : solid;
		}
		.images {
			display: none; /* all */
		}
		.outertable {}
		.innertable {}
	</style>
	<body>
		<h1>Profile for ${Firstname} ${Lastname}</h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% List institutionsIdsList = (ArrayList<String>)session.getAttribute("institutionIdsList"); %>
		<h1>Profile for <%= user.getFirstName() %> <%= user.getLastName() %></h1>
	
		<form action="Welcome" method="post">
			<br/><!-- Add a space between the buttons -->
			<button type="submit">Welcome Page</button>
		</form>
		<hr/>
		<button id="link-button">Link Account</button>
		<hr/>
		<button id="testButton" onclick="updateAccountsTable()">Update Table</button>
		
		<script>
			function resetParagraphs(metadata_accounts_length) {
				console.log("Inside resetParagraphs");
				var usersAccounts = <%= user.getAccountIds().size() %>;
				console.log("usersAccounts: " + usersAccounts);
				var strAccounts = (usersAccounts == 1) ? ' account!' : ' accounts!';
				$('#accounts').text('Accounts - ' + usersAccounts);
		        $('#changingText').text('You have successfully loaded ' + metadata_accounts_length + strAccounts);
			};
			function updateAccountsTable() {
				console.log('Inside updateInnerTable()...');

				//$('.outerTable').find('tr td:first').each(function() {
				$('tr > td:first-child').each(function() {
					//var institutionId = $(this).text().replace(/\s/g,'');
					var institutionId = $(this).text().replace(/\s/g,'');
					console.log("cell value: " + institutionId);

					if (institutionId == "") { $(this).html(''); }

					
					if (institutionId == "ins_1") { $(this).html('<img src="bankofamerica.jpg" alt="bankofamericaLogo"/>'); }
					if (institutionId == "ins_2") { $(this).html('<img src="bb&t.jpg" alt="bb&tLogo"/>'); }
					if (institutionId == "ins_3") { $(this).html('<img src="chaseLogo.jpg" alt="chaseLogo"/>'); }
					if (institutionId == "ins_4") { $(this).html('<img src="wellsfargo.jpg" alt="wellsfargoLogo"/>'); }
					if (institutionId == "ins_5") { $(this).html('<img src="citi.jpg" alt="citiLogo"/>'); }
					if (institutionId == "ins_6") { $(this).html('<img src="usbank.jpg" alt="usbankLogo"/>'); }
					if (institutionId == "ins_7") { $(this).html('<img src="usaa.jpg" alt="usaaLogo"/>'); }
					if (institutionId == "ins_9") { $(this).html('<img src="capitalone.jpg" alt="capitaloneLogo"/>'); }
					if (institutionId == "ins_10") { $(this).html('<img src="amex.jpg" alt="amexLogo"/>'); }
					if (institutionId == "ins_11") { $(this).html('<img src="charlesschwab.jpg" alt="charlesschwabLogo"/>'); }
					if (institutionId == "ins_12") { $(this).html('<img src="fidelity.jpg" alt="fidelityLogo"/>'); }
					if (institutionId == "ins_13") { $(this).html('<img src="pnc.jpg" alt="pncLogo"/>'); }
					if (institutionId == "ins_14") { $(this).html('<img src="tdbank.jpg" alt="tdbankLogo"/>'); }
					if (institutionId == "ins_15") { $(this).html('<img src="navyfederal.jpg" alt="navyfederalLogo"/>'); }
					if (institutionId == "ins_16") { $(this).html('<img src="suntrust.jpg" alt="suntrustLogo"/>'); }
					if (institutionId == "ins_19") { $(this).html('<img src="regions.jpg" alt="regionsLogo"/>'); }
					if (institutionId == "ins_20") { $(this).html('<img src="citizensbank.jpg" alt="citizensbankLogo"/>'); }
					if (institutionId == "ins_21") { $(this).html('<img src="huntington.jpg" alt="huntingtonLogo"/>'); }
					
				});
				//console.log(outerTable);
				
			    //$('#accountsTable').toggle();
			};
			$(function() {
				$('.button').removeAttr('disabled');
				$('.accountsTable').show();
				var usersAccounts = <%= user.getAccountIds().size() %>;
				//$('.accountsSize').text('Accounts - ' + usersAccounts);
				//$('.changingText').val('This text will change after using the Plaid Link Initializer.');
				//$('p').toggle();
				updateAccountsTable();
				console.log("jsp page has finished loading.")
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
			      console.log("loading Plaid handler...")
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
		    	 
			      }).success(function (response) {
			    	  location.reload(true);
			    	  var resp = JSON.stringify(response);
			    	  <%-- var usersAccounts = <%= user.getAccountIds().size() %>; --%>
			          //usersAccounts = parseInt(metadata.accounts.length) + usersAccounts;
			          //var strAccounts = (usersAccounts == 1) ? ' account!' : ' accounts!';
			          //console.log(usersAccounts);
			          //$('#accounts').html('Accounts : ' + usersAccounts);
				      //$('#changingText').html('You have successfully loaded ' + metadata.accounts.length + strAccounts);
			          //location.reload(true);
			          updateAccountsTable();
			          console.log("end of success");
				  }).error(function (response) {
			    	  document.getElementById("changingText").innerHTML = "We didn't load the accounts because the bank already exists in your profile.";
			      }).done(function () {
				  }).fail(function () {
			      }).always(function (response) {
			    	  //console.log(parseInt(metadata.accounts.length));
			    	  console.log(response);
			    	  //alert("hello");
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

		<p id="changingText"><b>${change}</b></p>
		<br/>
		<p>Items for '${Firstname}' '${Lastname}' </p>
		<br/>
		<p id="institutions">Banks : ${institutionIdsListSize}
		<p id="accounts">Accounts : ${accountsSize}</p>
		<!-- Create a table that lists all accounts -->
		<!-- Each account should have an update button and a delete button -->
		<!-- Update updates the Item -->
		<!-- Delete deletes the Item -->
		
		<!-- List of images -->
		
		
	
		<div class="mainTable" id="accountsTable">
			<table class="outerTable" id="outerTable">
				<% 
				Iterator institutionIdsIter = institutionsIdsList.iterator();
				Iterator accountIdsIter = user.getAccountIds().iterator();
				while (institutionIdsIter.hasNext()) {
				//while (true) {
				%> 
				<!-- [Bank Logo | Update | Delete] --> 
				<tr id="bank"> 
					<td id="logo">
					 	<%= institutionIdsIter.next() %> <!-- change to Logo --> 
				 	</td> 
				 	<!-- Whitespace -->
				 	<td id="updatebtn">
				 		<!-- Update button --> 
				 		<button id="link--update-button">Update Bank</button>
				 	</td> 
				 	<!-- Whitespace -->
				 	<td id="deletebtn">
				 	  <!-- Delete button -->
				 	  <!-- Goes to Delete.java and performs doPost --> 
				      <form id="delete" method="post" action="Delete"> 
				      	<input type="hidden" value="DeleteBank"></input>
					    <button type="submit" formmethod="post">Delete Bank</button>
					  </form> 
				    </td> 
				</tr> 
				<% } %>
			</table>
			<!-- Space for readability -->
			<table class="innerTable" id="innerTable">
				<!-- <th> 
					<button type="button" onclick="hideInnerTable()">Back</button>
				</th> -->
				<% 
				//int noOfAccts = user.getAccountIds().size(); 
				List<String> acctIds = user.getAccountIds(); 
		        Iterator innerIter = acctIds.iterator();
				//while (innerIter.hasNext()) {
				%> 
				<!-- [Name | Mask | Subtype | Delete] --> 
				<tr id="acct"> 
					<td>
				 	<!-- Account -->
				 		<!-- Name | Mask | Subtype -->
					  	<td>
					  		<%-- <%= innerIter.next() %> --%> <!-- Name --> 
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
				      <!-- <form id="delete" method="post" action="Delete"> 
					    <button type="submit" formmethod="post">Delete Account</button>
					  </form> --> 
				    </td> 
				</tr> 
				<%-- <% } %> --%>
			</table>
		</div>
		
		<script>
			$("img").on("click", function() {
				$(".outerTable").hide();
				$(".innerTable").show();
			});
			function hideInnerTable() {
				$(".innerTable").hide();
				$(".outerTable.").show();
			};
		</script>
	</body>
</html>

