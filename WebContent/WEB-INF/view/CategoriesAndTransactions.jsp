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
	<%@ page import="com.miBudget.v1.entities.*" %>
	<%@ page import="com.miBudget.v1.entities.Category" %>
	<%@ page import="java.util.HashMap" %>
	<%@ page import="org.json.*" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="static com.miBudget.v1.servlets.CAT.LOGGER" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="icon" type="image/x-icon" href="images/wallet.ico">
		<title>Categories and Transactions</title>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<style>
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
				cursor:pointer;
			}
			.center {
				top: 50%;
				left: 50%;
				margin-top: -10px;
				margin-left: -10px;
			}
			.box {
				float: start;
				padding: 0px 8px;
				width: fit-content;
				height: fit-content;
				box-sizing: border-box;
			}
			button{
			    height:21px; 
			    width:100px; 
			    margin: -20px -50px; 
			    position:relative;
			    top:50%; 
			    left:50%;
			    cursor:pointer;
			    font-family: Arial, Helvetica, sans-serif;
			    font-size: 1em;
			}
			.cursor {
				cursor:pointer;
			}
			.updateButton {
			}
			.mainTable, th, td {
				border: 1px solid black;
				visibility: visible;
			}
			.transactionTable {
				visibility: hidden;
				border:none;

			}
			.transactionTicket {
				width:100%;
				height:100%;
				border: none;
				margin-left:10px;
				margin-top:10px;
				margin-right:10px;
				margin-bottom: 10px;
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
			.fonta {
			    font-family: "Times New Roman", Times, serif;
			}
			.fontb {
			    font-family: Arial, Helvetica, sans-serif;
			}
			.outertable {}
			.innertable {
				visibility: hidden; /* visible */
			}
			.acct {}
			.border {
				border : 1px solid black;
			}
			::placeholder {
				text-align: center;
				color: black;
				font-weight: bold;
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
			.wrap {
				white-space: normal;
				font-size:large ;
				width:100px;
				height: 50px;
			}
		</style>
	</head>
	<body style="margin:8px; overflow: auto;" class="fonta">
		<h1 class="font1">Categories and Transactions for <i>${Firstname} ${Lastname}</i></h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl(); %>
		<% AccountDAOImpl accountDAOImpl = new AccountDAOImpl(); %>
		<% ItemDAOImpl itemsDAOImpl = new ItemDAOImpl(); %>
		<%
			response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
			response.setHeader("Pragma","no-cache"); //HTTP 1.0
			response.setDateHeader ("Expires", 0);
			//response.setIntHeader("Refresh", 1);
            //prevents caching at the proxy server
		%>

		<div id="profileDiv" style="float:left; margin-left:50px;">
			<form action="profile" method="get"> <!-- think about changing this call to get -->
<!-- 			<input class="button" type="submit" value="Profile">
 -->			<button type="submit">Profile Page</button>
			</form>
		</div>
		<br/>
		<!-- Start of Main Div -->
		<div style="width:100%;">
			<h3 style="display:inline;width:50%;float:left;text-align:center;">Categories</h3>
			<h3 style="display:inline;width:50%;float:right;text-align:center;">Transactions</h3>

			<div id="categoriesDiv" class="border" style="height: 526px; width:49%; float:left;"> <!-- 425px -->
				<p id="checked" style="float:right; font-size:90%;" value="New Category" name="newCategory">Click for Your Categories</p>
				<br/>

				<!-- <form id="newCategoryForm" onsubmit="" method="post" action="CAT"> -->
				<div id="newCategoryDiv" style="width:99%;">
					<div class="input-group" style="width:99%;">
						<span class="input-group-addon">
							<i id="glyphiconRemove" onclick="clearInput('categoryName')" class="glyphicon glyphicon-remove"></i>
						</span>
						<input class="form-control" id="categoryName" type="text" placeholder="Name of Category" tabindex="1" name="categoryName" required>
					</div>
					<br/>
					<div style="width:99%;">
						<input list="currencies" style="width:100px; display:inline-block; margin-left:50px;" placeholder="Currency" tabindex="2" name="currency" />
						<!-- TODO: Once one is selected, list disappears basically. Figure out how to keep entire list. -->
						<datalist id="currencies">
							<option value="USD">
							<option value="CAD">
							<option value="Euro">
							<option value="Mex$">
						</datalist>
						<input type="text" style="direction:RTL; width: 144px; display:inline-block; margin-left:75px;" placeholder="Budgeted Amount" tabindex="3" name="categoryAmt" value="" required>
					</div>
					<br/>
					<p style="text-align:center;">Rules</p>
					<p>Rules takes precedence when new transactions come in and will affect this Category! You can define both types of rules but you must choose which one will take precedence.</p>
					<button type="submit" style="right; left:71%; width:130px; white-space: nowrap;" id="saveRule">Save Rule</button>

					<br/>

					<div id="group1">
						<p style="float:left;">If a transactions amount is:</p>
						<input id="transactionPrecendence" name="group1" value="precedenceAmount" style="float:right;" type="radio">

						<br/>
						<div id="currencyBlock">
							<input list="currencies" style="width:100px; display:block; float:left;" placeholder="Currency" tabindex="2" name="currency" />
							<!-- TODO: Once one is selected, list disappears basically. Figure out how to keep entire list. -->
							<datalist id="currencies">
								<option value="USD">
								<option value="CAD">
								<option value="Euro">
								<option value="Mex$">
							</datalist>
							<input type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Transaction Amount" tabindex="3" name="categoryAmt" value="" required>
						</div>
						<input id="transactionPrecendence" name="group1" value="precedenceName" style="float:right;" type="radio"/>
						<p style="float:left;">If a transactions name is like:</p>
						<br/>
						<div class="input-group" style="right:0px;">
							 <span class="input-group-addon">
							 	<i id="glyphiconRemove" onclick="clearInput('merchantsName2')" class="glyphicon glyphicon-remove"></i>
							 </span>
							<input id="merchantsName2" class="form-control" type="text" style="width: 205px; display:block; left:0px;" placeholder="ex: McDs, Target, Sonic" tabindex="7" name="merchantsName" value="" required/>
						</div>
					</div>
					<br/>
					<br/>
					<div id="currentRules" style="height: 90px; width:99%; overflow-y: scroll;">
						<p>This div should be scrollable</p>
						<p>It will dynamically list the rules the user has added to a new category.</p>
						<p>Once rule is saved, user should have the freedom to edit any value right there. When category is saved the last
							value inside each rule will be used as the ultimate decision for the rule. Also, all inputs for rules will be cleared.</p>
						<p>Once rule is saved, user should have the freedom to edit any value right there. When category is saved the last
							value inside each rule will be used as the ultimate decision for the rule. Also, all inputs for rules will be cleared.</p>

						<p>Once rule is saved, user should have the freedom to edit any value right there. When category is saved the last
							value inside each rule will be used as the ultimate decision for the rule. Also, all inputs for rules will be cleared.</p>

					</div>
					<br/>
					<br/>
					<button type="submit" style="float:left; left:19%; width:180px; white-space: nowrap;" tabindex="7" id="wipeForm">Wipe Category Form</button>
					<button type="submit" style="float:left; left:70%; width:150px; white-space: nowrap;" tabindex="8" id="saveNewCategory">Save New Category</button>
					<br/>
					<br/>
				</div>

				<!-- <form id="currentCategoryForm" onsubmit="" method="post" action="CAT"> -->
				<div id="currentCategoryDiv" style="width:99%;">
					<div class="input-control">
						<input id="currentCategory"  class="form-control" list="categories1" type="text" style="width:244px; width:99%;" placeholder="Choose a Category" tabindex="1" name="categoryName" required/>
						<datalist id="categories1">
							<% ArrayList<Category> categoriesList = miBudgetDAOImpl.getAllCategories(user);
								for (Category cat : categoriesList) { %>
							<option value="<%= cat.getName() %>">
									<% } %>
						</datalist>
					</div>
					<br/>
					<div id="currencyBlock" style="width:99%;">
						<input id="currentCurrency" list="currencies" style="width:100px; display:inline-block; margin-left:50px;" placeholder="Currency" tabindex="2" name="currency" />
						<!-- TODO: Once one is selected, list disappears basically. Figure out how to keep entire list. -->
						<datalist id="currencies">
							<option value="USD">
							<option value="CAD">
							<option value="Euro">
							<option value="Mex$">
						</datalist>
						<input id="budgetedAmt" type="text" style="direction:RTL; width: 144px; display:inline-block; margin-left:75px;" placeholder="Budgeted Amount" tabindex="3" name="categoryAmt" value="" required>
					</div>
					<br/>
					<p style="text-align:center;">Rules</p>
					<p>Rules takes precedence when new transactions come in and will affect this Category! You can define both types of rules but you must choose which one will take precedence.</p>
					<button type="submit" style="right; left:71%; width:130px; white-space: nowrap;" id="saveRule">Save Rule</button>

					<br/>

					<div id="group2">
						<p style="float:left;">If a transactions amount is:</p>
						<input id="transactionPrecendence" name="group2" value="precedenceAmount" style="float:right;" type="radio">

						<br/>
						<div id="currencyBlock">
							<input id="currentCurrency" list="currencies" style="width:100px; display:block; float:left;" placeholder="Currency" tabindex="2" name="currency" />
							<!-- TODO: Once one is selected, list disappears basically. Figure out how to keep entire list. -->
							<datalist id="currencies">
								<option value="USD">
								<option value="CAD">
								<option value="Euro">
								<option value="Mex$">
							</datalist>
							<input type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Transaction Amount" tabindex="3" name="categoryAmt" value="" required>
						</div>
						<input id="transactionPrecendence" name="group2" value="precedenceName" style="float:right;" type="radio"/>
						<p style="float:left;">If a transactions name is like:</p>
						<br/>
						<div class="input-group" style="right:10px;">
							 <span class="input-group-addon">
							 	<i id="glyphiconRemove" onclick="clearInput('merchantsName2')" class="glyphicon glyphicon-remove"></i>
							 </span>
							<input id="merchantsName2" class="form-control" type="text" style="width: 205px; display:block; left:0px;" placeholder="ex: McDs, Target, Sonic" tabindex="7" name="merchantsName" value="" required/>
						</div>
					</div>
					<br/>
					<br/>
					<div id="currentRules" style="height: 90px; width:99%; overflow: scroll;">
						<p>Rules will appear here</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
						<p>Rule 1</p>
						<p>Rule 2</p>
					</div>
					<br/>
					<br/>
					<button type="submit" style="float:left; left:19%; width:130px; white-space: nowrap;" id="deleteCategory">Delete Category</button>
					<button type="submit" style="float:left; left:70%; width:130px; white-space: nowrap;" id="updateCategory">Update Category</button>
					<br/>
					<br/>
				</div>

			</div> <!-- Ends New Categories Div -->

			<!-- Form Line Divider -->
			
			<!-- Start Transactions Div -->		
			<div id="transactionsDiv" class="border" style="height: 526px; width:49%; float:right; overflow-y:scroll;">
				<div id="getTransactionsDiv" style="width:99%; text-align:center;">
					<div style="margin: auto; text-align: center; display: flex; flex-direction: row;">
						<input style="display: block;" title="The From Date is not required" class="form-control" id="FromDate" name="FromDate" placeholder="From" required="" tabindex="#" type="text" value=""><br>
						<input style="display: block;" title="The To Date is not required" class="form-control" id="ToDate" name="ToDate" placeholder="To" required="" tabindex="#" type="text" value=""><br>
				    </div>
					<datalist id="accounts">
						<% HashMap<String, ArrayList<Account>> acctsMap = (HashMap<String, ArrayList<Account>>)
										session.getAttribute("acctsAndInstitutionIdMap");
						   for (String insId : acctsMap.keySet()) {
						       for (Account acct : acctsMap.get(insId)) { 
							       String name = acct.getNameOfAccount(); %>
								   <option label="Account" value="<%= name %>"/>
							   <% } %>
						   <% } %>

					</datalist>
					<input type="hidden" name="validated" value="false"/>
					<input type="hidden" name="formId" value="transactions"/>
					<div style="margin: auto; text-align: right; display: flex; flex-direction: row;">
						<input type="text" id="numberOfTrans" name="numberOfTrans" class="form-control" style="width:50px; display: block;" title="Enter the number of transactions <=50 you wish to receive" placeholder="#"/>
						<input id="currentAccount" name="currentAccount" value="" title="Choose an Account to pull transactions from" class="form-control" list="accounts" type="text" style="display: block;" placeholder="Choose an Account" tabindex="1" required/>
						<input type="hidden" id="currentAccountHidden" name="currentAccountHidden" value=""/> 
						<input id="getTransactions" type="submit" title="Get Transactions" onclick="validateFields('get transactions')" class="form-control" style="width:130px; display: block;" value="Get Transactions"/>
					</div>
				</div> <!-- end getTransactionsDiv -->
				<div id="displayTransactionsDiv" style="width:98%;">
					<%
					// Java code here
					ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
					for (int i = 0; i < transactions.size(); i++) {
						Transaction transaction = transactions.get(i);
					%>
					<div id="transactionsContainerDiv" class="" name="transactionTicket">
						<table id="transactionsTable" class="transactionTable transactionTicket" name="transactionsTable">
							<tr id="header" name="">
								<th colspan="2">
									<h4 id="TransactionMapping" style="text-align: center">Transaction <%= (i+1) %></h4>
								</th>
							</tr>
							<tr id="row1">
								<td>
									<!-- Merchant Name: <merchantName> (Editable) -->
									<label for="merchantName" style="margin-left:10px;">Merchant Name:</label>
									<input type="text" style="float: right; text-align:right;" id="merchantName" value="<%= transaction.getName() %>"/>
								</td>
							</tr>
							<tr id="row2">
								<td>
									<!-- Amount: <amount> (Non-editable) -->
									<label for="amount" style="margin-left:10px;">Amount:</label>
									<input type=text" style="float: right; text-align:right;" id="amount" value="<%= transaction.getAmount() %>"/>
								</td>
							</tr>
							<tr id="row3" style="margin-left:10px;">
								<td>
									<!-- TODO: change to aggregated list
									<datalist id="categories2"></datalist> -->
									<input id="categorySelected" name="categorySelected" value=""
										   title="Choose a Category" class="form-control" list="categories1" type="text"
										   style="display: block; text-align:left;" placeholder="Choose a Category" tabindex="#" required/>
								</td>
							</tr>
							<tr id="row4">
								<td>
									<table class="transactionTable" style="width:100%;">
										<tr class="wrap">
											<td><button style="height:100%; display: inline-block;" tabindex="#" type="submit" onclick="updateUsersTransactions('<%= transaction.getTransactionId() %>', 'ignore')">Ignore</button></td>
											<td><button style="height:100%; display: inline-block;" tabindex="#" type="submit" onclick="updateUsersTransactions('<%= transaction.getTransactionId() %>', 'bill')">Mark as Bill</button></td>
											<td><button style="height:100%; display: inline-block;" tabindex="#" type="submit" onclick="updateUsersTransactions('<%= transaction.getTransactionId() %>', 'income')">Mark as Income</button></td>
											<td><button style="height:100%; display: inline-block;" tabindex="#" type="submit" onclick="updateUsersTransactions('<%= transaction.getTransactionId() %>', 'save')">Save to Category</button></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr id="row5">
								<td>
									&nbsp;
								</td>
							</tr>
						</table> <!-- end transactionsTicket -->
					</div> <!-- end transactionsTable -->
					<% if (i != (transactions.size()-1)) {%>
					<hr size="20" style="border: 1px solid black;"/>
					<!-- if we just printed last transaction or are printing first transaction, don't print -->
					<% } }%>
				</div> <!-- end transactionContainerDiv -->
			</div> <!-- end transactionsDiv -->
			
		</div> <!-- end MainDiv -->
		
		<br/>
		<br/>
		<p id="date" class="footer" style="text-align:center">${dateAndTime}</p>
		<script>
			function updateUsersTransactions(transaction, methodName) {
				$.ajax({
					type: "Post",
					url: "cat",
					data: {
						transaction: transaction,
						methodName: methodName
					}
				}).success(function (response) {
					updateTransactionsTable();
					location.reload();
					console.log("call to CAT was successful.");
				}).error(function (response) {
					var res = JSON.stringify(response.responseText);
					res = res.substring(1, res.length -1);
					$("[id='changingText']").text(res).css({ 'font-weight': 'bold' }).fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after using the Plaid Link Initializer.')
								.css({ 'font-weight' : 'bold'});
					});
					console.log("failed to perform Post to CAT");
				}).done(function () {
				}).fail(function () {
				}).always(function (response) {
					console.log(response);
				});
			};
			function clearInput(input) {
				//alert("Clearing this text box.")
				var target = input;
				console.log('target: ' + target);
				
				if (target == "categoryName") {
					console.log('target before: ' + $("#categoryName").val());
					$("#categoryName").val("");
					console.log('target after: ' + $("#categoryName").val());
					
				} else if (target == "merchantsName") {
					console.log('target before: ' + $("#merchantsName").val());
					$("#merchantsName").val("");
					console.log('target after: ' + $("#merchantsName").val());
				} else if (target == "merchantsName2") {
					console.log('target before: ' + $("#merchantsName2").val());
					$("#merchantsName2").val("");
					console.log('target after: ' + $("#merchantsName2").val());
				}
				
			};
			function validateFields(name) {
				let validate = false;
				let methodName = name;
				let formName = name.split(' ')[1];
				
				if (formName == "transactions") {
					let fromDate = $("#FromDate").val();
					let toDate = $("#ToDate").val();
					console.log("fromDate: " + fromDate + "\ntoDate: " + toDate);
					// Check transactions count has value
					let count = $("#numberOfTrans").val();
					if (count === "undefined") {
						count = 0
						console.log("count: " + count);
					} else {
						console.log("count: " + count);
					}
					// Check that an account was chosen
					let accountName = $('#currentAccountHidden').val()
					if (accountName === "undefined" || accountName === "" || accountName == null) {
						console.log("accountName: " + null);
						validate = false;
					} else {
						console.log("accountName: " + accountName);
						$("#transactions")
						.find('input[name=validated]')
						.val(true);
						validate = true;
					}
					
					if (validate) {
						console.log("form is good to send");
						//$(formName).find('input[name=validated]').val("true");
						console.log("making post request to CAT");
						$.ajax({
					        'type': "Post",
					        'url': "cat",
					        'data': {
					            'fromDate': fromDate,
                                'toDate': toDate,
						        'numberOfTrans': count,
								'currentAccount': accountName,
								'validated': true,
								'formName': formName,
								'methodName': methodName
					        },				    
					        'success': function () {
						        updateTransactionsTable();
								location.reload();
								console.log("call to CAT was successful.");
					        },
					        'error': function(response) {
								console.log("failed to perform Post to CAT");
						    }
					    });
					}
					else if (!validate) {
						console.log("Unable to post to CAT");
					}
				}
			}; //validate fields

			function updateTransactionsTable() {
				let displayTransactionsDiv = $("[id='displayTransactionsDiv']")
				displayTransactionsDiv.hide();
				displayTransactionsDiv.show();
			}
			function combineCategories(array1, array2) {
				var result_array = [];
				var arr = array1.concat(array2);
				var len = arr.length;
				var assoc = {};

				while(len--) {
					var item = arr[len];

					if(!assoc[item])
					{
						result_array.unshift(item);
						assoc[item] = true;
					}
				}

				return result_array;
			}

			function reloadPage() {
				console.log('running reloadPage()')
			    $.when(reload()).done(function() {
			    	location.reload(true);
				});
			    function reload() { 
				    console.log('running reload()');
				};
			}
		
			// onReady function
			$(function() {
				$('#currentAccount').on('input',function() {
				    var opt = $('option[value="'+$(this).val()+'"]');
				    var acct = opt.attr('value');
				    $('#currentAccountHidden').val(acct);
				    //console.log('input value: ' + opt.attr('value'));
				    console.log('currentAccountHidden value: ' + $('#currentAccountHidden').val());
				    //alert(opt.length ? opt.attr('value') : 'NO OPTION');
				  });
				$( "#FromDate" ).datepicker();
				$( "#ToDate" ).datepicker();
			    var acctsAndInsIdMap = function () {
					var list = [];
					var map = new Map();
					var acctsAndInsIdMap = new Map();
				    $.ajax({
				        'type': "GET",
				        'url': "Services",
				        'data': { 'method': 'getAcctsAndInstitutionIdMap' },
				        'success': function (data) {
					        console.log("successful retrieval of acctAndInstitutionIdMap");
					        console.log(JSON.parse(data));
							map = JSON.parse(data);
					        var mapIter = Object.keys(JSON.parse(data));
					        
				            for (const key in map) {
								for (const acct in map[key]) {
									console.log(map[key][acct]);
									list.push(map[key][acct]);
								}
								acctsAndInsIdMap.set(key, list);
				            }
				        }
				    });
				    return acctsAndInsIdMap;
				}(jQuery);
				console.log('acctsAndInsIdMap');
				if (acctsAndInsIdMap.size != 0) {
					for (const key in map) {
						console.log('key: ' + key);
						for (const acct in map[key]) {
							console.log('acct: ' + acct);
						}
		            }
				} 
				
				/** $("#transactions").find('input[name=numberOfTrans]').on('change', function () {
					if ($("#transactions").find('input[name=numberOfTrans]').val() == "") {
						$("#transactions").find('input[name=validated]').val("false");
					}
				}); */
				
				/** $("#transactions").find('input[id=currentAccount]').on('change', function () {
					if ($("#transactions").find('input[id=currentAccount]').val() == "" || 
						$("#transactions").find('input[id=currentAccount]').val() == "Choose an Account" )
						$("#transactions").find('input[name=validated]').val("false");
				}); */
				
				var categoriesMap = function () {
				    var categoriesM = new Map();
				    $.ajax({
				        'type': "GET",
				        'url': "Services",
				        'data': { 'method': 'getAllCategories' },
				        'success': function (data) {
					        console.log("successful retrieval of categories");
					        //console.log(data);
					        data = JSON.parse(data);
				            for (var i = 0; i < data.length; i++) {
								var _name = data[i].name;
								var _budgetAmt = data[i].budgetedAmt;
								var _currency = data[i].currency;
								console.log(_name);
								console.log(_budgetAmt);
								console.log(_currency);
		
								category = {name: _name, amount: _budgetAmt, currency: _currency};
								categoriesM.set(_name, category); 
				            }
				        }
				    });
				    return categoriesM;
				} /**(jQuery); */
				console.log('categoriesMap');
				console.log(categoriesMap);
				
				$("#currentCategoryDiv").hide();
				//$("#transactionsTable").hide();
		
				$("#checked").on("mouseover", function() {
					 document.body.style.cursor="pointer";
				}).on("mouseout", function() {
					document.body.style.cursor="default";
				});
				
				$("#checked").on("click", function() {
					if ($("#checked").text() == "Click for Your Categories")  {
						console.log("activating current categories list...");
						$("#newCategoryDiv").hide();
						$("#currentCategoryDiv").show();
						$("#checked").text("Click for New Categories");
						
					} else {
						console.log("activating new categories view...");
						$("#currentCategoryDiv").hide();
						$("#newCategoryDiv").show();
						$("#checked").text("Click for Your Categories");
					}
				});
		
				$("#currentCategory").on("click", function() {
					console.log("clicked current category")
					
				}).change(function() {
					var text = $("#currentCategory").val();
					if (text != "") {
						//console.log("text is: " + text + ". determine how to populate budgeted amount for this category");
						var category = categoriesMap.get(text);
						console.log('category: ' + category.name);
						console.log('budgeted amt: ' + category.amount);
						console.log('currency: ' + category.currency);
						var budgetText = category.amount;
						var currency = category.currency;
						switch (text) {
							case "Mortgage" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Utilities" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Transportation" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Insurance" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Food" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Subscriptions" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
							case "Bills" : $("#budgetedAmt").val(budgetText); $('[id="currentCurrency"]').val(currency); break;
						}		
					}
					else {
						$("#budgetedAmt").val("");
						$("#currentCurrency").val("");
					}	
					
				});
		
				$("#budgetedAmt").focus(function() {
					console.log("budget amount has focus.");
					var text = $("#currentCategory").val();
					if (text != "") {
						console.log("determine how to populate budgeted amount for this category");
					} else {
						console.log("user still needs to provide a category name");
					}
				});
				
				// Last line of on ready function
			});  // End on ready function
		</script>
	</body>
</html>