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
<%@ page import="java.util.HashMap" %>
    
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
		.updateButton {
			display: all;
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
		<button id="testButton" onclick="reloadPage()")>Update Table</button>
		<hr/>
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
		<div class="mainTable" id="accountsTable">
			<table class="outerTable" id="outerTable">
				<% 
				Iterator institutionIdsIter = institutionsIdsList.iterator();
				Iterator accountIdsIter = user.getAccountIds().iterator();
				HashMap<String, Boolean> errMapForItems = (HashMap) session.getAttribute("ErrMapForItems");
				// Load Map of ItemGetResponses here
				while (institutionIdsIter.hasNext()) {
				String currentId = (String)institutionIdsIter.next();
				String idCopy = currentId;
				%> 
				<!-- [Bank Logo | Update | Delete] --> 
				<tr id="bank">
					<td id="logo" name="<%= currentId %>">
					 	<%= currentId %> <!-- change to Logo --> 
				 	</td> 
				 	<!-- Whitespace -->
				 	<!--  Change this button to ONLY show if there is an error.  -->
				 	<td id="updatebtn" name="<%= errMapForItems.get(currentId) %>">
				 		<!-- Update button --> 
				 		<button class="updateButton" id="link-update-button" name="<%= currentId %>"><b>Update Bank</b></button>
				 	</td> 
				 	<!-- Whitespace -->
				 	<td id="deletebtn">
				 	  <!-- Delete button -->
				 	  <!-- Goes to Delete.java and performs doPost --> 
				      <form id="delete" method="post" action="Delete"> 
				      	<input type="hidden" name="delete" value="bank"></input>
				      	<input type="hidden" name="idCopy" value="<%= idCopy %>"></input>
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
			/* $("img").on("click", function() {
				console.log('inside click()');
				$(".outerTable").hide();
				$(".innerTable").show();
			}); */
			/* function hideInnerTable() {
				$(".innerTable").hide();
				$(".outerTable.").show();
			}; */
			function resetParagraphs(metadata_accounts_length) {
				console.log("Inside resetParagraphs");
				var usersAccounts = <%= user.getAccountIds().size() %>;
				console.log("usersAccounts: " + usersAccounts);
				var strAccounts = (usersAccounts == 1) ? ' account!' : ' accounts!';
				$('#accounts').text('Accounts - ' + usersAccounts);
		        $('#changingText').text('You have successfully loaded ' + metadata_accounts_length + strAccounts);
			};
			function updateAccountsTable() {
				console.log('Inside updateAccountsTable()...');
				var firstRowText = $("[id='bank']").attr('id');
				console.log(firstRowText == 'bank' ? 'ROW ATTAINED!' : 'DO NOT HAVE ROW')  ;
				// $('tr > td:first-child').each(function() {
				// $('tr > td:nth-child(2)').each(function() {
				
				// we want every element that is a column that is a direct
				// child of a tr element that has id attr = blah
				// every element we want should be a td
				$("[id='bank']").each(function() {
					//var institutionId = $(this).find('td:nth-child(1)').text().replace(/\s/g,'');
					var institutionId = $(this).find('td:nth-child(1)').attr('name');
					var row2 = $(this).find('td:nth-child(2)');
					//var institutionId = $(this).text().replace(/\s/g,'');
					//var name = $(this).find('td:nth-child(2)').attr('name', institutionId);
					console.log("cell value: " + institutionId);
					var nameOfButton = $(this).find('td:nth-child(2)').attr('name');
					console.log("button name is: " + nameOfButton);
					if (institutionId == "") { $(this).html(''); }
					if (institutionId == "ins_1") { 
						$(this).find('td:nth-child(1)').html('<img src="bankofamerica.jpg" alt="ins_1"/>'); 
						if (row2.attr('name') == "true") { row2.show(); }
						else { row2.hide(); }
					}
					if (institutionId == "ins_2") { $(this).find('td:nth-child(1)').html('<img src="bb&t.jpg" alt="ins_2"/>'); }
					if (institutionId == "ins_3") { 
						$(this).find('td:nth-child(1)').html('<img src="chaseLogo.jpg" alt="ins_3"/>');
						if (row2.attr('name') == "true") { row2.show(); }
						else { row2.hide(); }
					}
					if (institutionId == "ins_4") { 
						$(this).find('td:nth-child(1)').html('<img src="wellsfargo.jpg" alt="ins_4"/>'); 
						if ($(this).find('td:nth-child(2)').attr('name') == "true") { $(this).find('td:nth-child(2)').show(); }
						else { $(this).find('td:nth-child(2)').hide(); }
					}
					if (institutionId == "ins_5") { 
						$(this).find('td:nth-child(1)').html('<img src="citi.jpg" alt="ins_5"/>'); 
						if (row2.attr('name') == "true") { row2.show(); }
						else { row2.hide(); }
					}
					if (institutionId == "ins_6") { $(this).find('td:nth-child(1)').html('<img src="usbank.jpg" alt="ins_6"/>'); }
					if (institutionId == "ins_7") { $(this).find('td:nth-child(1)').html('<img src="usaa.jpg" alt="ins_7"/>'); }
					if (institutionId == "ins_9") { $(this).find('td:nth-child(1)').html('<img src="capitalone.jpg" alt="ins_9"/>'); }
					if (institutionId == "ins_10") { $(this).find('td:nth-child(1)').html('<img src="amex.jpg" alt="ins_10"/>'); }
					if (institutionId == "ins_11") { $(this).find('td:nth-child(1)').html('<img src="charlesschwab.jpg" alt="ins_11"/>'); }
					if (institutionId == "ins_12") { $(this).find('td:nth-child(1)').html('<img src="fidelity.jpg" alt="ins_12"/>'); }
					if (institutionId == "ins_13") { $(this).find('td:nth-child(1)').html('<img src="pnc.jpg" alt="ins_13"/>'); }
					if (institutionId == "ins_14") { $(this).find('td:nth-child(1)').html('<img src="tdbank.jpg" alt="ins_14"/>'); }
					if (institutionId == "ins_15") { $(this).find('td:nth-child(1)').html('<img src="navyfederal.jpg" alt="ins_15"/>'); }
					if (institutionId == "ins_16") { $(this).find('td:nth-child(1)').html('<img src="suntrust.jpg" alt="ins_16"/>'); }
					if (institutionId == "ins_19") { $(this).find('td:nth-child(1)').html('<img src="regions.jpg" alt="ins_19"/>'); }
					if (institutionId == "ins_20") { $(this).find('td:nth-child(1)').html('<img src="citizensbank.jpg" alt="ins_20"/>'); }
					if (institutionId == "ins_21") { $(this).find('td:nth-child(1)').html('<img src="huntington.jpg" alt="ins_21"/>'); }
				});
				//$('tr > td:nth-child(2)')
			};
			function getUpdateHandler(publicToken) {
				console.log('Inside getUpdateHandler');
				console.log('passed in param: ' + publicToken);
				//console.log('passed in param: ' + institutionIdName);
				var thisPT = publicToken;
				var updateHandler = Plaid.create({
			        env: 'sandbox',
			        apiVersion: 'v2',
			        clientName: 'Plaid Update Mode',
			        // Replace with your public_key from the Dashboard
				    key: 'f0503c4bc8e63cc6c37f07dbe0ae2b',
				    product: ["transactions"],
			        token: thisPT,
			        // Optional, use webhooks to get transaction and error updates
		   	        webhook: 'https://requestb.in',
				    onLoad: function() {
				      // Optional, called when Link loads
				      console.log("loading Plaid updateHandler");
				      console.log("using token: " + publicToken);
				      //
			        },
			        onSuccess: function(thisPT, metadata) {
			            console.log('Account reauthentication done.');
			            console.log('metadata:');
			            console.log(JSON.stringify(metadata));
			            location.reload(true);
			            reloadErrMapForItems();
			        },
			        onExit: function(err, metadata) {
			            // The user exited the Link flow.
			            if (err != null) {
			                // The user encountered a Plaid API error prior
		                    // to exiting.
		    	            console.log(JSON.stringify(err));
		    	            console.log(JSON.stringify(metadata));
			            }
			                // metadata contains the most recent API request ID and the
			                // Link session ID. Storing this information is helpful
			                // for support.
			        }  
			    }); // end handlerUpdate
			    console.log('created updateHandler');
			    //updateHandler.open();
			    return updateHandler; 
		    	
		    }
			function reloadErrMapForItems() {
				$.get("Profile",
				function(data) {

				});
			};
			$(function() {
				$('.button').removeAttr('disabled');
				
				var usersAccounts = <%= user.getAccountIds().size() %>;
				//$('.accountsSize').text('Accounts - ' + usersAccounts);
				//$('.changingText').val('This text will change after using the Plaid Link Initializer.');
				//$('p').toggle();
				
			


			// IIFE Immediately Invoked Function Expression
			// Create an Item
			//(function($) {
			  $('#link-button').on('click', function(e) {
				  var handler = Plaid.create({
						env: 'sandbox',
						apiVersion: 'v2',
					    clientName: 'Plaid Walkthrough Demo',
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
					      console.log(metadata.institution_id);
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
					             institutionId: metadata.insitution_id,
					             accounts: jsonAccount,
					             institution_name: metadata.institution.name,
					             institution_id: metadata.institution.institution_id
					    	 }
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
					  }); // end handler
			    handler.open();
			  });

			  
			  $("[id='link-update-button']").on('click', function(e) {
				 var institutionIdName = $(this).attr('name');
				 var publicToken;
				 var updateHandler;
			     console.log('the button you clicked: ' + institutionIdName);
			     
			     $.when(ajax1()).done(function(publicToken) {
					alert('Public token received...');
					updateHandler = getUpdateHandler(publicToken);
					 console.log('updateHandler returned');
					 //updateHandler.open(institutionIdName);
					 //updateHandler.open(publicToken);
					 
					 updateHandler.open();
				 });
				 function ajax1() {
					 console.log('before ajax call: ' + publicToken);
					 return $.ajax({
					     type: "POST",
					     url: "updatebank",
					     data: {
					         institutionId: institutionIdName
						 },
					  }).success(function (response) {
					     publicToken = JSON.stringify(response);
					     console.log('after call to UpdateBank: ' + publicToken);
					     //getUpdateHandler(publicToken);
					     
						  
					  }).error(function (response) { // end .success
						 console.log('review the response...');
					 	 console.log(response);
					  }); // end .error
					  console.log('after ajax is defined: ' + publicToken);
				      console.log('performing ajax call');
				 } 
				  // upddateHandler not defined yet
				  //updateHandler.open();
				 console.log('end update button here');
			   }); // end link-update-button.on('click')
			  /* $('button').on('click', function(e) {
				alert('hi');
			  }); */
			  updateAccountsTable();
		      console.log("jsp page has finished loading.")
		    });
		    //})(jQuery);
		    function reloadPage() {
			    $.when(reload()).done(function() {
			    	location.reload(true);
				});
			    function reload() { 
			    	return $.get("Profile",
					function(data) {

					});
				};
			}
		</script>
	</body>
</html>

