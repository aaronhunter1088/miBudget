<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ page import="com.miBudget.entities.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/wallet.ico"/>
		<title>Accounts Springboot</title>
		<!-- jQuery -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
		<!-- Plaid Initializer -->
		<script src="https://cdn.plaid.com/link/v2/stable/link-initialize.js"></script>
		<style>
			html {
				background-color: #ee1c1c;
			}
			body {
				display: block;
				margin: 8px;
				background-color: #ee1c1c;
			}
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
				cursor:pointer;
			}
			button {
				cursor:pointer;
			}
			.updateButton {
			}
			th, td {
				border: 1px solid black;
			}
			p.changingText {
				font-weight: bold;
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
			.outerTable {}
			.innerTable {}
			.acct {}
			.footer {
				position: fixed;
				display: table-cell;
				vertical-align: middle;
				left: 0;
				bottom: 0;
				height: 2.5rem;
				width: 100%;
				background-color: white;
				color: black;
				text-align: center;
			}
		</style>
	</head>
	<body>
		<% User user = (User) session.getAttribute("user"); %>
		<h1 class="font1">Accounts Page for <i>${user.getFirstName()} ${user.getLastName()}</i></h1>
		<br/>
		<div style="display: inline-block; width:30%">
			<input id="homepageBtn" type="button" class="button cursor" value="Homepage"/>
			<hr/>
			<input id="linkAccountsBtn" type="button" class="button cursor" value="Link Account"/>
			<br/>
		</div>
		<div style="width: 500px; display: inline-block; overflow-wrap: break-word; word-wrap:break-word; word-break: break-all; vertical-align: top;" class="container">
			<p id="changingText" class="changingText" style="overflow-wrap:break-word; word-wrap:break-word; word-break:break-all;">${changingText}</p>
			<% Map institutionsIdsAndAccounts = (Map) session.getAttribute("institutionIdsAndAccounts"); %>
			<div class="mainTable" id="bankAccountsTable">
				<!-- Create a table that lists all banks -->
				<!-- Each account should have an update button and a delete button -->
				<table class="outerTable" id="outerTable">
					<%
						Map<String, Boolean> errMapForItems = (Map) session.getAttribute("errMapForItems");
						// Load Map of ItemGetResponses here
						List<Item> items = user.getItems();
						for(Item item : items) {
					%>
					<!--BANK: [Bank Logo | Update | Delete] --> <!-- var firstRowText = $("[id='bank']").attr('id'); -->
					<tr id="bankRow">
						<td id="logoCell">
							<%= item.getBankName() %> <!-- change to Logo -->
						</td>
						<!-- Whitespace -->
						<!--  Change this button to ONLY show if there is an error.  -->
						<td id="updateBtnCell" name="<%= errMapForItems.get(item.getInstitutionId()) %>">
							<form id="updateBankForm">
								<input type="hidden" name="action" value="update_bank"/>
								<input type="hidden" name="institutionId" value="<%= item.getInstitutionId() %>"/>
								<input type="hidden" name="bankName" value="<%= item.getBankName() %>"/>
								<input id="updateBankBtn" type="button" value="Update bank"/>
							</form>
						</td>
						<!-- Whitespace -->
						<td id="deleteBtnCell">
							<form id="deleteBankForm">
								<input type="hidden" name="action" value="delete_bank"/>
								<input type="hidden" name="institutionId" value="<%= item.getInstitutionId() %>"/>
								<input type="hidden" name="bankName" value="<%= item.getBankName() %>"/>
								<input id="deleteBankBtn" type="button" value="Delete bank"/>
							</form>
						</td>
					</tr>
					<%--<% } %>--%>
				</table>
				<!-- Whitespace -->
				<!-- Create a table that lists all accounts for each bank-->
				<table class="innerTable" id="innerTable" style="border: 1px solid black;">
					<tr id="header" name="">
						<th colspan="5">
							<h4 id="bankName"></h4>
						</th>
						<th>
							<button onclick="hideInnerTable()">Go Back</button>
						</th>
					</tr>
					<%
						List<Account> accountsForItem = (List<Account>) institutionsIdsAndAccounts.get(item.getInstitutionId());
						for (Account acct : accountsForItem) {
							String name = (acct.getAccountName() != null) ? acct.getAccountName() : acct.getOfficialName();
					%>
					<!-- [Name | Mask | Available Balance] | Delete -->
					<tr id="acct" class="acct" name="<%= item.getBankName() %>">
						<!-- Account -->
						<td>
							<%= name %>
						</td>
						<!-- Whitespace -->
						<td>
							<%= acct.getMask() %>
						</td>
						<!-- Whitespace -->
						<td>
							<%= acct.getAvailableBalance() %>
							<!-- add logic for credit cards to show owed amount as well -->
						</td>
						<!-- Whitespace -->
						<td>
							<%= acct.get_type() %>
						</td>
						<!-- Whitespace -->
						<td>
							<%= acct.getSubType() %>
						</td>
						<!-- Delete Account -->
						<td id="deleteAccount">
							<form id="deleteAccountForm">
								<input type="hidden" id="action" value="delete_account"/>
								<input type="hidden" id="accountId" value="<%= acct.getAccountId() %>"/>
								<input type="hidden" id="item_ItemId" value="<%= acct.getItemId() %>"/>
								<input type="hidden" id="bankName2" value="<%= item.getBankName() %>"/>
								<input type="hidden" id="accountName" value="<%= name %>"/>
								<input type="button" id="deleteAccountBtn" class="button cursor" value="Delete account"/>
							</form>
						</td>
					</tr>
					<% }
					} %>
				</table>
			</div>
		</div>
		<br/>
		<script type="text/javascript">
			$(function() {
				//console.log("starting document.onload()..");
				$("img").on("click", function() {
					$(".outerTable").hide();
					$(".innerTable").show();
				});
				$('.button').removeAttr('disabled');
				$('#homepageBtn').on("click", function() {
					console.log("Clicked Homepage btn");
					window.location.href = "${pageContext.request.contextPath}/homepage"
				})
				// IIFE Immediately Invoked Function Expression
				// Create an Item
				//(function($) {
				$('#linkAccountsBtn').on('click', function() {
					let handler = Plaid.create({
						env: 'development',
						apiVersion: 'v2',
						clientName: 'Plaid Link Mode',
						// Replace with your public_key from the Dashboard
						key: 'f0503c4bc8e63cc6c37f07dbe0ae2b',
						//token: (await $.post('/create_link_token')).link_token,
						product: ["transactions"],
						// Optional, use webhooks to get transaction and error updates
						webhook: 'https://requestb.in',
						onLoad: function() {
							// Optional, called when Link loads
							console.log("loading Plaid handler...")
						},
						onSuccess: function(public_token, metadata) {
							console.log("public token: " + public_token);
							console.log("metadata: " + metadata);
							let accounts = [], account = [];
							let _id, _name, _mask, _type, _subtype;
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
								headers: {
									accept: "application/json",
									contentType: "application/json"
								},
								type: "POST",
								url: "${pageContext.request.contextPath}/add-item",
								async: true,
								dataType: "application/json",
								crossDomain: true,
								data: {
									public_token: public_token,
									link_session_id: metadata.link_session_id,
									institutionId: metadata.institution_id,
									accounts: jsonAccount,
									institution_name: metadata.institution.name,
									institution_id: metadata.institution.institution_id
								},
								statusCode: {
									200: function(data) {
										//updateAccountsTable();
										updateBanksTable();
										location.reload();
										console.log(JSON.stringify(data));
									},
									400: function(data) {
										console.log(JSON.stringify(data));
									},
									404: function(data) {
										var res = JSON.stringify(data);
										res = res.substring(1, res.length -1);
										$("[id='changingText']").text(res).css({ 'font-weight': 'bold' }).fadeOut(8000, function() {
											$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
													.css({ 'font-weight' : 'bold'});
										});
									},
									500: function(data) {
										console.log(JSON.stringify(data));
									}
								}
							});
						},
						onExit: function(err, metadata) {
							// The user exited the Link flow.
							if (err != null) {
								// The user encountered a Plaid API error prior to exiting.
								console.log(err);
								console.log(metadata);
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
				$('#updateBankBtn').on('click', function() {
					let institutionIdName = $(this).attr('name');
					let publicToken;
					let updateHandler;
					console.log('the button you clicked: ' + institutionIdName);

					$.when(ajax1()).done(function(publicToken) {
						updateHandler = getUpdateHandler(publicToken);
						console.log('updateHandler returned');
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
						}).error(function (response) { // end .success
							console.log('review the response...');
							console.log(response);
						}); // end .error
						console.log('after ajax is defined: ' + publicToken);
						console.log('performing ajax call');
					}
					console.log('end update button here');
				}); // end link-update-button.on('click')
				$('#deleteBankBtn').on('click', function() {
					let action = $("[id='deleteBankForm']").find('input:nth-child(1)').text();
					let institutionId = $("[id='deleteBankForm']").find('input:nth-child(2)').text();
					let bankName = $("[id='deleteBankForm']").find('input:nth-child(3)').text();
					let buttonSelectedText = $("[id='deleteBankForm']").find('input:nth-child(4)').text();
					let ans = '';
					console.log('bankName: ' + bankName);
					ans = prompt('WARNING! You are about to delete your \'' + bankName + '\' bank. Are you sure you want to continue? Enter: \'Yes\' to confirm.', '');
					if (ans === 'Yes'.toLowerCase() || ans === 'Y'.toLowerCase())
					{
						console.log('Deleting one ' + bankName + ' account.');
						$.ajax({
							headers: {
								accept: "application/json",
								contentType: "application/json"
							},
							type: "POST",
							url: "${pageContext.request.contextPath}/delete-item",
							async: true,
							dataType: "application/json",
							crossDomain: true,
							data: {
								action: action,
								institutionId: institutionId,
								bankName: bankName,
								btnSelected: buttonSelectedText,
							},
							statusCode: {
								200: function(data) {
									console.log(JSON.stringify(data))
									location.reload();
								},
								400: function() {
									console.log(JSON.stringify(data))
								},
								404: function(data) {
									console.log(JSON.stringify(data));
								},
								500: function(data) {
									console.log(JSON.stringify(data));
								}
							}
						});
					}
				});
				$('#deleteAccountBtn').on('click', function() {
					let action = $("[id='deleteAccountForm']").find('input:nth-child(1)').text();
					let accountId = $("[id='deleteAccountForm']").find('input:nth-child(2)').text();
					let itemId = $("[id='deleteAccountForm']").find('input:nth-child(3)').text();
					let bankName = $("[id='deleteAccountForm']").find('input:nth-child(4)').text();
					let accountName = $("[id='deleteAccountForm']").find('input:nth-child(5)').text();
					let buttonSelectedText = $("[id='deleteAccountForm']").find('input:nth-child(6)').text();
					var ans = "";
					ans = prompt('WARNING! You are about to delete your ' + acctName + ' account. Are you sure you want to continue? Enter: \'Yes\' to confirm.','');
					if (ans === 'Yes'.toLowerCase() || ans === 'Y'.toLowerCase()) {
						console.log('Making a post request to Delete to delete this single ' + acctName + ' account.');
						$.ajax({
							headers: {
								accept: "application/json",
								contentType: "application/json"
							},
							type: "POST",
							url: "${pageContext.request.contextPath}/delete-account",
							async: true,
							dataType: "application/json",
							crossDomain: true,
							data: {
								action: action,
								accountId: accountId,
								itemId: itemId,
								bankName: bankName,
								accountName: accountName,
								btnSelected: buttonSelectedText
							},
							statusCode: {
								200: function(data) {
									console.log(JSON.stringify(data))
									location.reload();
								},
								400: function() {
									console.log(JSON.stringify(data))
								},
								404: function(data) {
									console.log(JSON.stringify(data));
								},
								500: function(data) {
									console.log(JSON.stringify(data));
								}
							}
						});
					}
					return false;
				});

				var text = $("[id='changingText']").text();
				var usersAccounts = <%= user.getAccounts().size() %>;
				var goodText = "You successfully re-authorized your bank!";
				var goodLength = goodText.length;
				var goodText2 = "You have successfully loaded";
				var goodLength2 = goodText2.length;
				var errText = "We cannot add it again.";
				var errLength = errText.length;
				var errText2 = ":( You can't create a Budget with no Accounts. Go add Accounts next.";
				var deleteText = "You have successfully deleted your bank.";
				var deleteLength = deleteText.length;

				matchGoodText = text.substring(0, goodLength);
				matchGoodText2 = text.substring(0, goodLength2);
				matchErrText = text.substring(text.indexOf('.')+2, errLength);
				matchDeleteText = text.substring(0, deleteLength);
				//console.log('matchGoodText: ' + matchGoodText);
				//console.log('matchErrText: ' + matchErrText);
				if ( matchGoodText == goodText ) {
					$("[id='changingText']").fadeOut(8000, function () {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({'font-weight': 'bold'});
					});
				}
				else if (text === errText2) {
					$("[id='changingText']").text("Add your Accounts by clicking the \"Link Account\" button");
					$("[id='changingText']").fadeOut(8000, function () {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({'font-weight': 'bold'});
					});
				}
				else if ( matchGoodText2 == goodText2 ) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({ 'font-weight' : 'bold'});
					});
				}
				else if ( matchDeleteText == deleteText) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({ 'font-weight' : 'bold'});
					});
				}
				else if (text != 'This text will change after using the Plaid Link Initializer.') {
					// Default
					$("[id='changingText']").fadeOut(20000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({ 'font-weight' : 'bold'});
					});
				}
				else {
					// Don't fade the text
				}
				hideInnerTable();
				updateBanksTable();
			});
			function hideInnerTable() {
				$("[id='acct']").each(function() {
					$(this).show();
				});
				$(".innerTable").hide();
				$(".outerTable").show();
			}
			function replaceAll(str, find, replace) {
			    return str.replace(new RegExp(find, 'g'), replace);
			}
			function resetParagraphs(metadata_accounts_length) {
				//console.log("Inside resetParagraphs");
				var usersAccounts = <%= user.getAccounts().size() %>;
				var strAccounts = (usersAccounts === 1) ? ' account!' : ' accounts!';
				$('#accounts').text('Accounts - ' + usersAccounts);
		        $('#changingText').text('You have successfully loaded ' + metadata_accounts_length + strAccounts);
			}
			function updateBanksTable() {
				$("[id='bankRow']").each(function() {
					var col1 = $(this).find('td:nth-child(1)'); // Will be the Logo
					var bankName = col1.text().trim();
					var col2 = $(this).find('td:nth-child(2)'); // Update button
					var col3 = $(this).find('td:nth-child(3)'); // Delete button
					console.log("bankName: '" + bankName + '\'');
					if (col2.attr('name') === "true") { col2.show(); }
					else { col2.hide(); }
					// will do for all images, sets col1 to its appropriate image
					switch (bankName) {
						case "Bank of America"  : col1.html('<img src="${pageContext.request.contextPath}/images/bankofamerica.jpg" alt="Bank_of_America"/>');
										break;
						case "BB&T"  : col1.html('<img src="${pageContext.request.contextPath}/images/bb&t.jpg" alt="BB&T"/>');
										break;
						case "Chase"  : col1.html('<img src="${pageContext.request.contextPath}/images/chase.jpg" alt="Chase"/>');
										break;
						case "Wells Fargo"  : col1.html('<img src="${pageContext.request.contextPath}/images/wellsfargo.jpg" alt="Wells_Fargo"/>');
										break;
						case "Citi"  : col1.html('<img src="${pageContext.request.contextPath}/images/citi.jpg" alt="Citi"/>');
										break;
						case "US Bank"  : col1.html('<img src="${pageContext.request.contextPath}/images/usbank.jpg" alt="US Bank"/>');
										break;
						case "USAA"  : col1.html('<img src="${pageContext.request.contextPath}/images/usaa.jpg" alt="USAA"/>');
										break;
						case "Capital One"  : col1.html('<img src="${pageContext.request.contextPath}/images/capitalone.jpg" alt="Capital_One"/>');
										break;
						case "American Express" : col1.html('<img src="${pageContext.request.contextPath}/images/amex.jpg" alt="American_Express"/>');
										break;
						case "Charles Schwab" : col1.html('<img src="${pageContext.request.contextPath}/images/charlesschwab.jpg" alt="Charles_Schwab"/>');
										break;
						case "Fidelity" : col1.html('<img src="${pageContext.request.contextPath}/images/fidelity.jpg" alt="Fidelity"/>');
										break;
						case "PNC" : col1.html('<img src="${pageContext.request.contextPath}/images/pnc.jpg" alt="PNC"/>');
										break;
						case "TDBank" : col1.html('<img src="${pageContext.request.contextPath}/images/tdbank.jpg" alt="TD_Bank"/>');
										break;
						case "Navy Federal" : col1.html('<img src="${pageContext.request.contextPath}/images/navyfederal.jpg" alt="Navy_Federal"/>');
										break;
						case "Sun Trust" : col1.html('<img src="${pageContext.request.contextPath}/images/suntrust.jpg" alt="Sun_Trust"/>');
										break;
						case "Regions" : col1.html('<img src="${pageContext.request.contextPath}/images/regions.jpg" alt="Regions"/>');
										break;
						case "Citizens Bank" : col1.html('<img src="${pageContext.request.contextPath}/images/citizensbank.jpg" alt="Citizens_Bank"/>');
										break;
						case "Huntington" : col1.html('<img src="${pageContext.request.contextPath}/images/huntington.jpg" alt="Huntington"/>');
										break;
						default  : console.log('unknown institution.');
					}
					// setup logo to switch between tables
					col1.click(function() {
						var col1 = $(this);
						var nameOfButton = "";
						var code = col1.html().split(" ",3).pop();
						nameOfButton = code.substring(code.indexOf('"')+1, code.lastIndexOf('"'));
						nameOfButton = nameOfButton.includes('_') === true ? replaceAll(nameOfButton, '_', ' ') : nameOfButton;
						console.log('you clicked ' + nameOfButton);
						$('.outerTable').hide();
						<%-- <tr id="acct" name="<%= currentId %>"> --%>
						$("[id='header'] > th > h4").text(nameOfButton);
						$("[id='acct']").each(function() {
							$(this).show();
							var acctRowName = $(this).attr('name');
							console.log('acctRowName is ' + acctRowName);
							if (acctRowName === nameOfButton) $(this).show();
							else $(this).hide();
						});
						$('.innerTable').show();
					});
				}); // end for each row
			}
			function getUpdateHandler(publicToken) {
				//console.log('Inside getUpdateHandler');
				//console.log('passed in param: ' + publicToken);
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
				      //console.log("loading Plaid updateHandler");
				      //console.log("using token: " + publicToken);
			        },
			        onSuccess: function(thisPT, metadata) {
			            //console.log('Account reauthentication done.');
			            //console.log('metadata:');
			            //console.log(JSON.stringify(metadata));
			            location.reload(true);
			        },
			        onExit: function(err, metadata) {
			            if (err != null) {
			                // The user encountered a Plaid API error prior
		                    // to exiting.
		    	            //console.log(JSON.stringify(err));
		    	            //console.log(JSON.stringify(metadata));
			            }
			                // metadata contains the most recent API request ID and the
			                // Link session ID. Storing this information is helpful
			                // for support.
			        }  
			    }); // end handlerUpdate
			    //console.log('created updateHandler');
			    return updateHandler; 
		    	
		    }
			function reloadPage() {
				//console.log('running reloadPage()')
			    $.when(reload()).done(function() {
			    	location.reload();
				});
			    function reload() { 
				    //console.log('running reload()');
			    	return $.get("Profile",
						function(data) {
	
						});
				};
			}
		</script>
	</body>
	<footer id="date" class="footer">${dateAndTime}</footer>
</html>

