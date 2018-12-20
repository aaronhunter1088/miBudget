<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.miBudget.v1.daoimplementations.MiBudgetDAOImpl" %>
<%@ page import="com.miBudget.v1.daoimplementations.AccountDAOImpl" %>
<%@ page import="com.miBudget.v1.daoimplementations.ItemDAOImpl" %>
<%@ page import="com.miBudget.v1.entities.Account" %>
<%@ page import="com.miBudget.v1.entities.User" %>
<%@ page import="com.miBudget.v1.entities.UserAccountObject" %>
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
		</style>
	</head>
	<body>
		<h1>Profile for ${Firstname} ${Lastname}</h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl(); %>
		<% AccountDAOImpl accountDAOImpl = new AccountDAOImpl(); %>
		<% ItemDAOImpl itemsDAOImpl = new ItemDAOImpl(); %>
		<h1>Profile for <%= user.getFirstName() %> <%= user.getLastName() %></h1>
	
		<form action="Welcome" method="post"> <!-- think about chaning this call to get -->
			<br/><!-- Add a space between the buttons -->
			<button type="submit">Welcome Page</button>
		</form>
		<hr/>
		<button id="link-button">Link Account</button>
		<hr/>
		<button id="testButton" onclick="reloadPage()">Update Table</button>
		<hr/>
		<p id="changingText" class="changingText"><b>${change}</b></p>
		<br/>
		<p>Items for '${Firstname}' '${Lastname}' </p>
		<br/>
		<p id="institutions">Banks : ${institutionIdsListSize}
		<p id="accounts">Accounts : ${accountsSize}</p>
		<!-- Create a table that lists all accounts -->
		<!-- Each account should have an update button and a delete button -->
		<!-- Update updates the Item -->
		<!-- Delete deletes the Item -->
		<% List institutionsIdsList = (ArrayList<String>)session.getAttribute("institutionIdsList"); %>
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
				      <form id="delete" method="post" onsubmit="return deleteBank();" action="Delete"> 
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
				<tr id="header" name="">
					<th colspan="5">
			    		<h4 id="bankName"></h4>
			    	</th>
			    	<th> 
						<button onclick="hideInnerTable()">Go Back</button>
					</th>
			    </tr>
				<%
				HashMap<Integer, ArrayList<Account>> acctsMap = (HashMap<Integer, ArrayList<Account>>)
					session.getAttribute("acctsAndInstitutionIdMap");
				Iterator mapsIter = acctsMap.keySet().iterator();
				
				institutionIdsIter = institutionsIdsList.iterator();
				
				while (mapsIter.hasNext()) {
					int itemTableId = Integer.parseInt(mapsIter.next().toString());
					ArrayList<Account> acctsList = acctsMap.get(itemTableId);
					String currentId = (String) institutionIdsIter.next();
					System.out.println("\tacctsList size: " + acctsList.size());
					Iterator acctsListIter = acctsList.iterator();
					while (acctsListIter.hasNext()) {
						Account acct = (Account) acctsListIter.next(); 
						System.out.println("next account: " + acct); %>
						<!-- [Name | Mask | Available Balance] | Delete --> 
						<tr id="acct" class="acct" name="<%= currentId %>"> 
							<!-- Account -->
							<!-- Name | Mask | Subtype -->
						  	<td>
						  		<%= acct.getNameOfAccount() != null ? 
						  			acct.getNameOfAccount() :
						  			acct.getOfficialName() %> <!-- Name of Account otherwise Official Name --> 
						  	</td> 
					 		<!-- Whitespace -->	
					 		<td>
					 			<%= acct.getMask() %>
					 		</td>
					 		<!-- Whitespace -->	
					 		<td> 
					 			<%= acct.getAvailableBalance() %>
					 		</td>
					 		<!-- Whitespace -->	
					 		<td>
					 			<%= acct.getType() %>
					 		</td>
					 		<!-- Whitespace -->	
					 		<td> 
					 			<%= acct.getSubType() %>
					 		</td> 
						 	<!-- Delete Account -->
						 	<td>
						 		<!-- this functionality is not yet set up -->
						 		<button id="deleteAcct" name="<%= acct.getAccountId() %>">Delete Account</button> 
						 		<!-- Delete button -->
						 		<!-- Goes to Delete.java and performs doPost --> 
						        <!-- <form id="delete" method="post" action="Delete"> 
							    <button type="submit" formmethod="post">Delete Account</button>
							  </form> -->
						 	</td> 
						</tr>
				<% }
		    } %> 
			</table>
		</div>
		
		<script>
			$("img").on("click", function() {
				console.log('inside click()');
				$(".outerTable").hide();
				$(".innerTable").show();
			});
			function hideInnerTable() {
				$("[id='acct']").each(function() {
					$(this).show();
				});
				$(".innerTable").hide();
				$(".outerTable").show();
			};
			function replaceAll(str, find, replace) {
			    return str.replace(new RegExp(find, 'g'), replace);
			};
			function deleteBank() {
				var bankName = $("[id='acct']").find('td:nth-child(1)').attr('name');
				var ans = "";
				//do {
				ans = prompt('WARNING! You are about to delete your \'' + bankName + '\' bank. Are you sure you want to continue? Enter: \'Yes\' to confirm.', '');
				console.log('answer: ' + ans);
				//} while (ans != "Yes".toLowerCase() || ans != "No".toLowerCase() || ans != null || ans != "" );
				if (ans == 'Yes'.toLowerCase()) {
					console.log('Making a post request to Delete to delete this single ' + bankName + ' account.');
					return true;
				}
				return false;
			}; 
			function deleteAccount() {
				var acctName = $(this).attr('name');
				var acctMask = $("[id='acct'] > td:first-child > td:nthh-child(2)");
				console.log('acctMask: ' + acctMask);
				var ans = prompt('WARNING! You are about to delete this single ' + acctName + ' account. Are you sure you want to continue? Enter: \'Yes\' or \'No\'','');
				while (!ans.equals('Yes'.toLowerCase()) || !ans.equals('No'.toLowerCase())) {
					ans = prompt('WARNING! You are about to delete this single ' + acctName + ' account. Are you sure you want to continue? Enter: \'Yes\' or \'No\'','');
				}
				if (ans == 'Yes'.toLowerCase()) {
					console.log('Make a post request to Delete to delete this single ' + acctName + ' account.');
				}
				return;
			};
			function resetParagraphs(metadata_accounts_length) {
				console.log("Inside resetParagraphs");
				var usersAccounts = <%= user.getAccountIds().size() %>;
				console.log("usersAccounts: " + usersAccounts);
				var strAccounts = (usersAccounts == 1) ? ' account!' : ' accounts!';
				$('#accounts').text('Accounts - ' + usersAccounts);
		        $('#changingText').text('You have successfully loaded ' + metadata_accounts_length + strAccounts);
			};
			function updateBanksTable() {
				console.log('Inside updateBanksTable()...');
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
					var col1 = $(this).find('td:nth-child(1)');
					
					//var institutionId = $(this).text().replace(/\s/g,'');
					//var name = $(this).find('td:nth-child(2)').attr('name', institutionId);
					console.log("cell value: " + institutionId);
					var nameOfButton = col2.attr('name');
					console.log("button name is: " + nameOfButton);
					if (institutionId == "") { $(this).html(''); }
					// will do for all update buttons
					if (col2.attr('name') == "true") { col2.show(); }
					else { col2.hide(); }
					
					
					if (institutionId == "ins_1") { 
						$(this).find('td:nth-child(1)').html('<img src="bankofamerica.jpg" alt="Bank_of_America"/>'); 
					}
					if (institutionId == "ins_2") { 
						$(this).find('td:nth-child(1)').html('<img src="bb&t.jpg" alt="BB&T"/>');
				    }
					if (institutionId == "ins_3") { 
						$(this).find('td:nth-child(1)').html('<img src="chase.jpg" alt="Chase"/>');
					}
					if (institutionId == "ins_4") { 
						$(this).find('td:nth-child(1)').html('<img src="wellsfargo.jpg" alt="Wells_Fargo"/>'); 
					}
					if (institutionId == "ins_5") { 
						$(this).find('td:nth-child(1)').html('<img src="citi.jpg" alt="Citi"/>'); 
					}
					if (institutionId == "ins_6") { 
						$(this).find('td:nth-child(1)').html('<img src="usbank.jpg" alt="US Bank"/>'); 
					}
					if (institutionId == "ins_7") { $(this).find('td:nth-child(1)').html('<img src="usaa.jpg" alt="USAA"/>'); }
					if (institutionId == "ins_9") { $(this).find('td:nth-child(1)').html('<img src="capitalone.jpg" alt="Capital_One"/>'); }
					if (institutionId == "ins_10") { $(this).find('td:nth-child(1)').html('<img src="amex.jpg" alt="American_Express"/>'); }
					if (institutionId == "ins_11") { $(this).find('td:nth-child(1)').html('<img src="charlesschwab.jpg" alt="Charles_Schwab"/>'); }
					if (institutionId == "ins_12") { $(this).find('td:nth-child(1)').html('<img src="fidelity.jpg" alt="Fidelity"/>'); }
					if (institutionId == "ins_13") { $(this).find('td:nth-child(1)').html('<img src="pnc.jpg" alt="PNC"/>'); }
					if (institutionId == "ins_14") { $(this).find('td:nth-child(1)').html('<img src="tdbank.jpg" alt="TD_Bank"/>'); }
					if (institutionId == "ins_15") { $(this).find('td:nth-child(1)').html('<img src="navyfederal.jpg" alt="Navy_Federal"/>'); }
					if (institutionId == "ins_16") { $(this).find('td:nth-child(1)').html('<img src="suntrust.jpg" alt="Sun_Trust"/>'); }
					if (institutionId == "ins_19") { $(this).find('td:nth-child(1)').html('<img src="regions.jpg" alt="Regions"/>'); }
					if (institutionId == "ins_20") { 
						$(this).find('td:nth-child(1)').html('<img src="citizensbank.jpg" alt="Citizens_Bank"/>'); 
					}
					if (institutionId == "ins_21") { 
						$(this).find('td:nth-child(1)').html('<img src="huntington.jpg" alt="Huntington"/>'); 
					}
					// will do for all images
					var col2 = $(this).find('td:nth-child(2)'); // Update button
					var code = col1.html().split(" ",2).pop(); 
					<td id="logo" name="<%= currentId %>">
					 	<%= currentId %> <!-- change to Logo -->  
				 	</td> // TODO: all buttons coming back as false
					var nameOfButton = code.substring(code.indexOf('"')+1, code.indexOf('.'));
					//console.log('name: ' + name);
					col1.attr('name', nameOfButton);
					console.log('image column name is now: ' + col1.attr('name'));
					col1.click(function() {
						var col1 = $(this);
						var nameOfButton = "";
						var code = col1.html().split(" ",3).pop();
						nameOfButton = code.substring(code.indexOf('"')+1, code.lastIndexOf('"'));
						nameOfButton = nameOfButton.includes('_') == true ? replaceAll(nameOfButton, '_', ' ') : nameOfButton; 
						//nameOfButton = nameOfButton.substring(0, nameOfButton.length-4);
						//console.log('code: ' + code);
						console.log('you clicked ' + nameOfButton);
						// hilde outer table. show inner table
						$('.outerTable').hide();
						<%-- <tr id="acct" name="<%= currentId %>"> --%>
						$("[id='header'] > th > h4").text(nameOfButton);
						$("[id='acct']").each(function() {
							var acctRow = $(this); //$("[id='acct']")
							acctRow.show();
							var acctRowId = acctRow.attr('id');
							var acctRowName = acctRow.attr('name');
							//console.log('name is ' + acctRowName);
							if (acctRowName == nameOfButton) acctRow.show();
							else acctRow.hide();
						});
						$('.innerTable').show();
						
					});
				}); // end for each row
			};

			 <%-- <tr id="header">
		    	<th>
		    		<h4 id="bankName"><%= currentId %></h4>
		    	</th> --%>
			
			function updateAccountsTable() {
				console.log("\nInside updateAccountsTable()");
				<%-- <tr id="acct" name="<%= currentId %>"> 
				id="bankName" --%>
				$("[id='acct']").each(function() {
					var acctRow = $(this); //$("[id='acct']")
					var acctRowId = acctRow.attr('id');
					var acctRowName = acctRow.attr('name');
					var secondCol = acctRow.find('td:nth-child(2)');
					console.log(acctRowId == 'acct' ? 'ROW ATTAINED!' : 'DO NOT HAVE ROW')  ;
					
					//console.log('acctRowName: ' + acctRowName);
					//var institutionId = secondRow.attr('name');
					if (acctRowName == "ins_1") { 
						acctRow.attr('name', 'Bank of America');
						secondCol.attr('name', 'Bank of America');
					}
					if (acctRowName == "ins_2") { 
						accrRow.attr('name', 'BB&T');
				    }
					if (acctRowName == "ins_3") { 
						acctRow.attr('name', 'Chase');
					}
					if (acctRowName == "ins_4") { 
						acctRow.attr('name', 'Wells Fargo');
					}
					if (acctRowName == "ins_5") {
						acctRow.attr('name', 'Citi');
					}
					if (acctRowName == "ins_6") {
						acctRow.attr('name', 'US Bank');
					}
					if (acctRowName == "ins_7") {
						acctRow.attr('name', 'USAA');
					}
					if (acctRowName == "ins_9") {
						acctRow.attr('name', 'Capital One');
					}
					if (acctRowName == "ins_10") {
						acctRow.attr('name', 'Amex');
					}
					if (acctRowName == "ins_11") {
						acctRow.attr('name', 'Charles Schwab');
					}
					if (acctRowName == "ins_12") {
						acctRow.attr('name', 'Fidelity');
					}
					if (acctRowName == "ins_13") {
						acctRow.attr('name', 'PNC');
					}
					if (acctRowName == "ins_14") {
						acctRow.attr('name', 'TD Bank');
					}
					if (acctRowName == "ins_15") {
						acctRow.attr('name', 'Navy Federal');
					}
					if (acctRowName == "ins_16") {
						acctRow.attr('name', 'Sun Trust');
					}
					if (acctRowName == "ins_19") {
						acctRow.attr('name', 'Regions');
					}
					if (acctRowName == "ins_20") {
						acctRow.attr('name', 'Citizens Bank');
					}
					if (acctRowName == "ins_21") {
						acctRow.attr('name', 'Huntington');
					}
					console.log("acctRow name="+acctRow.attr('name')+"\"");
				});
			};
			function getUpdateHandler(publicToken) {
				console.log('Inside getUpdateHandler');
				console.log('passed in param: ' + publicToken);
				//console.log('passed in param: ' + institutionIdName);
				var thisPT = publicToken;
				var updateHandler = Plaid.create({
			        env: 'development',
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
			            //document.getElementById("changingText").innerHTML = "You've re-authenticated <bank>. It's good to go!", metadata.instititution[zero].name;
			            // Left to show that < text > will be blank with no value....
			            //document.getElementById("changingText").innerHTML = "You've re-authenticated <your_bank>. It's good to go!";
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
			$(function() {
				console.log("starting document.onload()..");
				$('.button').removeAttr('disabled');
				var text = $("[id='changingText']").text();
				var usersAccounts = <%= user.getAccountIds().size() %>;
				//$('.accountsSize').text('Accounts - ' + usersAccounts);
				//$('.changingText').val('This text will change after using the Plaid Link Initializer.');
				//$('p').toggle();
				var goodText = "You successfully re-authorized your bank!";
				var goodLength = goodText.length;
				var goodText2 = "You have successfully loaded";
				var goodLength2 = goodText2.length;
				var errText = "We cannot add it again.";
				var errLength = errText.length;
				var deleteText = "You have successfully deleted your bank.";
				var deleteLength = deleteText.length;
				
				matchGoodText = text.substring(0, goodLength);
				matchGoodText2 = text.substring(0, goodLength2);
				matchErrText = text.substring(text.indexOf('.')+2, errLength);
				matchDeleteText = text.substring(0, deleteLength);
				console.log('matchGoodText: ' + matchGoodText);
				console.log('matchErrText: ' + matchErrText);
				if ( matchGoodText == goodText ) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
						.css({ 'font-weight' : 'bold'});
					});
				} else if ( matchGoodText2 == goodText2) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
						.css({ 'font-weight' : 'bold'});
					});
				} else if ( matchDeleteText == deleteText) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
						.css({ 'font-weight' : 'bold'});
					});
				}
			


			// IIFE Immediately Invoked Function Expression
			// Create an Item
			//(function($) {
			  $('#link-button').on('click', function(e) {
				  var handler = Plaid.create({
						env: 'development',
						apiVersion: 'v2',
					    clientName: 'Plaid Upload Mode',
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
					      console.log("metadata: " + metadata);
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
					    	  //var resp = JSON.stringify(response);
					          //usersAccounts = parseInt(metadata.accounts.length) + usersAccounts;
					          //var strAccounts = (usersAccounts == 1) ? ' account!' : ' accounts!';
					          //console.log(usersAccounts);
					          //$('#accounts').html('Accounts : ' + usersAccounts);
						      location.reload(true);
						      updateAccountsTable();
					          updateBanksTable();
					          console.log("end of success");
						  }).error(function (response) {
							  // in Profile.java, we set the response using
							  // response.getWriter().append("");
							  // This is how the response object in this function
							  // and alike are set!
							  var res = JSON.stringify(response.responseText); 
							  res = res.substring(1, res.length -1);
							  $("[id='changingText']").text(res).css({ 'font-weight': 'bold' }).fadeOut(8000, function() {
									$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
									.css({ 'font-weight' : 'bold'});
								});
					    	  //document.getElementById("changingText").innerHTML = "We didn't load the accounts because the bank already exists in your profile.";
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
					//alert('Public token received...');
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
			  hideInnerTable();
			  updateAccountsTable();
			  updateBanksTable();
		      console.log("jsp page has finished loading.")
		    });
		    //})(jQuery);
		</script>
	</body>
</html>

