<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.miBudget.daos.*" %>
<%@ page import="com.miBudget.entities.*" %>
<%@ page import="com.miBudget.entities.Category" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.json.*" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="com.miBudget.entities.Transaction" %>
<%@ page import="com.miBudget.entities.User" %>
<%@ page import="com.miBudget.entities.Account" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" charset=utf-8" http-equiv="Content-Type">
		<link href="images/wallet.ico" rel="icon" type="image/x-icon">
		<title>Categories and Transactions</title>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
        <style>
			<!-- NEEDED -->
			body {
				display: block;
				margin: 8px;
			}
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
				cursor:pointer;
			}
			i, button {
				/*height:21px;*/
				/*width:100px;*/
				/*margin: -20px -50px;*/
				/*position:relative;*/
				/*top:50%;*/
				/*left:50%;*/
				/*font-family: Arial, Helvetica, sans-serif;*/
				/*font-size: 1em;*/
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
			.cursor {
				cursor:pointer;
			}
			.updateButton {
			}
			.mainTable {
				/*border: 1px solid #000000;*/
				visibility: visible;
			}
			th, td {
				border: 1px solid #000000;
				visibility: visible;
			}
			.transactionTable {
				visibility: hidden;
				border:none;

			}
			.transactionTicket {
				/*width:100%;*/
				/*height:100%;*/
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
			.wrap {
				white-space: normal;
				font-size:large ;
				width:100px;
				height: 50px;
				text-align: center;
			}
		</style>
	</head>
	<body style="height: 800px;">
		<h1 class="font1">Categories and Transactions for <i>${Firstname} ${Lastname}</i></h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% UserDAO userDAO;
			ItemDAO itemDAO;
			AccountDAO accountDAO;
			BudgetDAO budgetDAO;
			CategoryDAO categoryDAO;
		    TransactionDAO transactionDAO; %>

		<div style="display: inline-block;">
			<form action="profile" method="get">
				<button type="submit">Profile Page</button>
			</form>
			<hr/>
		</div>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<div style="display: inline-block; vertical-align: top;" class="container">
			<div style="display: block;" class="container">
				<p id="changingText" class="changingText">${change}</p>
			</div>
		</div>
		<br/>
		<!-- Start of Main Div -->
		<h3 style="display:inline;width:50%;float:left;text-align:center;" id="cHeader">New Categories</h3>
		<h3 style="display:inline;width:50%;float:right;text-align:center;" id="tHeader">Your Transactions</h3>
		<!-- Start New Categories Div -->
		<div id="categoriesDiv" class="border" style="height: 526px; width:49%; float:left;"> <!-- 425px -->
			<%--<p id="checked" style="float:right; font-size:90%;" value="New Category" name="newCategory">Click for Your Categories</p>
			<br/> --%>
			<div id="newCategoryDiv" style="width:95%; margin-left:20px; margin-top:20px; display:inline-block;">
				<div id="row1" style="display:inline-block;">
					<div class="input-group" id="nameOption" style="display:inline-block;">
						<div style="display:inline-block;">
                            <input class="form-control" id="categoryName" type="text" style="direction:LTR; width: 150px; margin-left:20px;" placeholder="Name of Category" tabindex="1" name="categoryName" title="This is the name of the new category" value="" required>
                            <span class="input-group-addon" style="width:25px; height:34px;">
									<i onclick="clearInput('categoryName')" class="glyphicon glyphicon-remove"></i>
                            </span>
                        </div>
                    </div>
					<div class="input-group" id="amountOption" style="display:inline-block; vertical-align:top;">
						<div style="display:inline-block;" class="input-group">
							<input class="form-control" id="categoryAmt" type="text" style="direction:RTL; width: 150px; margin-left:20px;" placeholder="Budgeted Amount" tabindex="2" name="budgetAmt" title="This is the budgeted amount" value="" required>
							<div style="display:inline-block;" class="input-group-btn">
								<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Choose <span class="caret"></span></button>
								<ul class="dropdown-menu dropdown-menu-right">
									<li><a href="#">USD</a></li>
									<li><a href="#">Mex$</a></li>
									<li><a href="#">Euro</a></li>
									<li><a href="#">CAD</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<br/>
				<br/>
				<p>Rules takes precedence when new transactions come in and will affect this Category! You can define both types of rules but you must choose which one will take precedence.</p>
				<br/>
				<div id="ruleGroup">
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
					<button type="submit" style="right; left:71%; width:130px; white-space: nowrap;" id="saveRule">Save Rule</button>
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
						<option value="<%= cat.getName() %>"></option>
								<% } %>
						<option value="Ignore"></option>
						<option value="Income"></option>
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
		</div>
		<!-- End New Categories Div -->
		<!-- Form Line Divider -->
		<!-- Start Transactions Div -->
		<div id="transactionsDiv" class="border" style="height: 526px; width:49%; float:right; overflow-y:scroll;">
			<div id="getTransactionsDiv" style="width:100%;">
				<form action="cat" method="post">
					<div style="width:100%; display:flex; flex-direction: row;">
						<input style="display: block; width:50%; float:left;" title="The From Date is not required" class="form-control" id="FromDate" name="FromDate" placeholder="From" tabindex="#" type="text" value=""><br>
						<input style="display: block; width:50%; float:right;" title="The To Date is not required" class="form-control" id="ToDate" name="ToDate" placeholder="To" tabindex="#" type="text" value=""><br>
					</div>
					<input type="hidden" name="validated" value="false"/>
					<input type="hidden" name="formId" value="transactions"/>
					<input type="hidden" name="methodName" value="get transactions"/>
					<input type="hidden" name="ignoredTransactions" value="<%= user.getIgnoredTransactions().isEmpty() ? 0 : user.getIgnoredTransactions().size() %>"/>
					<div style="display: flex; flex-direction: row;">
						<input type="text" id="numberOfTrans" name="numberOfTrans" class="form-control" style="width:50px; text-align:center; display: block;" title="Enter the number of transactions <=50 you wish to receive" placeholder="#"/>
						<input id="currentAccount" name="currentAccount" value="" title="Choose an Account to pull transactions from" class="form-control" list="accounts" autocomplete="off" type="text" style="width:75%; text-align:center;" placeholder="Choose an Account" tabindex="1" required/>
						<datalist id="accounts">
							<% HashMap<String, ArrayList<Account>> acctsMap = (HashMap<String, ArrayList<Account>>) session.getAttribute("acctsAndInstitutionIdMap");
								for (String insId : acctsMap.keySet()) {
									for (Account acct : acctsMap.get(insId)) {
										String name = acct.getAccountName(); %>
							<option label="Account" value="<%= name %>"></option>
							<% } } %>
							<option label="Transactions" value="Ignored Transactions"></option>
						</datalist>
						<input type="hidden" id="currentAccountHidden" name="currentAccountHidden" value=""/>
						<button id="getTransactions" type="submit" title="Get" onclick="return validateFields()" class="form-control" style="width:130px; display: block;">Get</button>
					</div>
				</form>
			</div> <!-- end getTransactionsDiv -->
			<!-- Copy table as done in Accounts.jsp -->
			<div id="displayTransactionsDiv" class="mainTable"  style="width:100%;">
				<table class="innerTable" id="innerTable" style="margin-left: auto; margin-right: auto; width:100%;">
					<%
						// Java code here
						ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("usersTransactions");
						for (int i = 0; i < transactions.size(); i++) {
							Transaction transaction = transactions.get(i);
					%>
						<tr id="header" name="transactionName">
							<th colspan="2">
								<h4 id="TransactionMapping" style="text-align: center">Transaction <%= (i+1) %></h4>
							</th>
						</tr>
						<form action="cat" method="post">
							<tr id="merchantRow">
								<td>
									<!-- Merchant Name: <merchantName> (Editable) -->
									<label for="merchantName" style="margin-right:10px;">Merchant Name:</label>
									<input type="text" size="50" style="float: right; text-align:right;" id="merchantName" value="<%= transaction.getName() %>"/>
								</td>
							</tr>
							<tr id="amountRow">
								<td>
                                    <!-- Amount: <transactionAmount> (Non editable) -->
									<label for="amount" style="margin-right:10px;">Amount:</label>
									<input readonly type="text" size="10" style="float: right; text-align:right;" id="amount" value="<%= transaction.getAmount() %>"/>
                                    <%--<p style="float: right; text-align:right;" id="amount"><%= transaction.getAmount() %></p>--%>
								</td>
							</tr>
							<tr id="categoryAndSaveRow" style="margin-left:10px;">
								<td>
									<table class="transactionTable" style="width:100%; text-align: center">
										<tr class="wrap">
											<td style="width:100%;">
												<input id="categorySelected" name="categorySelected" value=""
													   title="Choose a Category" class="form-control" list="categories1" autocomplete="off" type="text"
													   style="display: block; margin-right:10px;" placeholder="Choose a Category" tabindex="#" required/>
												<input type="hidden" id="saveMethodHidden" name="saveMethodHidden" value=""/>
											</td>
											<td>
												<button style="height:100%; display: inline-block;" tabindex="#" type="submit" onclick="return performTransactionAction()">Save</button>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</form>
					<% } %>
				</table>
			</div>
		</div>
		<!-- end MainDiv -->
		<br/>
		<br/>
		<script>
			function performTransactionAction()
			{
				let cat1 = $("#categorySelected option:selected").val();
				let cat2 = $("#categorySelected option:selected").value;
				let cat3 = $("#categorySelected option:selected").text;
				let cat4 = $("#categorySelected option:selected").innerText;
				let cat5 = $("#categorySelected option:selected").innerHTML;
				//let cat6 = $("#categorySelected option:selected").value(); not a function
				let cat6;
				$("#categorySelected").on('change',function() {
					let opt = $('option[value="'+$(this).val()+'"]');
					cat6.value(opt);
				});
				let cat7 = $("#categorySelected").val();
				console.log("cat1: " + cat1);
				console.log("cat2: " + cat2);
				console.log("cat3: " + cat3);
				console.log("cat4: " + cat4);
				console.log("cat5: " + cat5);
				console.log("cat6: " + cat6);
				console.log("cat7: " + cat7);
				if (category === "Ignore")
				{
					$("#saveMethodHidden")
							.find('input[name=saveMethodHidden]')
							.val("ignore");
					return true;
				}
				else if (category === "Bill")
				{
					$("#saveMethodHidden")
							.find('input[name=saveMethodHidden]')
							.val("bill");
					return true;
				}
				else
				{
					console.log("unable to post to CAT");
					return false;
				}

			};
			function clearInput(input) {
				//alert("Clearing this text box.")
				let target = input;
				console.log('target: ' + target);
				
				if (target === "categoryName") {
					console.log('target before: ' + $("#categoryName").val());
					$("#categoryName").val("");
					console.log('target after: ' + $("#categoryName").val());
					
				} else if (target === "merchantsName") {
					console.log('target before: ' + $("#merchantsName").val());
					$("#merchantsName").val("");
					console.log('target after: ' + $("#merchantsName").val());
				} else if (target === "merchantsName2") {
					console.log('target before: ' + $("#merchantsName2").val());
					$("#merchantsName2").val("");
					console.log('target after: ' + $("#merchantsName2").val());
				}
			}
			function validateFields() {
				let validate = false;

				let fromDate = $("#FromDate").val();
				let toDate = $("#ToDate").val();
				console.log("fromDate: " + fromDate + "\ntoDate: " + toDate);
				// Check transactions count has value
				let count = $("#numberOfTrans").val();
				if (isNaN(count)) return false;
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
				let chosenOption = $('#currentAccount').val();
				if (chosenOption === "undefined" || chosenOption === "" || chosenOption == null) {
					console.log("chosenOption: " + null);
					validate = false;
				} else {
					console.log("chosenOption: " + chosenOption);
					validate = true;
				}

				if (validate) {
					console.log("form is good to send");
					//$(formName).find('input[name=validated]').val("true");
					console.log("making post request to CAT");
					return true;
					// $.ajax({
					//     type: "POST",
					//     url: "cat",
					//     data: {
					//         fromDate: fromDate,
					//         toDate: toDate,
					//         numberOfTrans: count,
					// 		currentAccount: accountName,
					// 		validated: true,
					// 		formName: formName,
					// 		methodName: methodName
					//     }
					// }).success(function (response) {
					// 	console.log("get transactions response: " + response.responseText);
					// 	reloadPage(response);
					// 	//updateTransactionsTickets();
					// 	updateTransactionsTable();
					// 	console.log("end of success");
					// }).error(function (response) {
					// 	var res = JSON.stringify(response.responseText);
					// 	$("[id='changingText']").text(res).css({ 'font-weight': 'bold' }).fadeOut(8000, function() {
					// 		$("[id='changingText']").show().text('This text will change after taking an action.')
					// 				.css({ 'font-weight' : 'bold'});
					// 	});
					// }).done(function () {
					// }).always(function (response) {
					// 	console.log("responseType: " + response.responseType);
					// });
				}
				else if (!validate) {
					console.log("Unable to post to CAT");
					return false;
				}

			} //validate fields

			function updateTransactionsTickets() {
				$('#transactionsContainerDiv').each(function() {
					// let length = $(this).value().length;
					// console.log($(this).value());
					// $(this).width(length+5);
					let transaction = $(this);
					transaction.show();
				});
			}
			function updateTransactionsTable() {
				//$("[id='#innerTable']").load(" #innerTable > *");
				$( "#displayTransactionsDiv" ).load(window.location.href + " #displayTransactionsDiv" );
				// let displayTransactionsDiv = $("[id='#displayTransactionsDiv']")
				// displayTransactionsDiv.hide();
				// displayTransactionsDiv.show();
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

			function reloadPage(response) {
				console.log('running reloadPage()')
			    let res = JSON.stringify(response.responseText);
				$("[id='changingText']").show().text(res).fadeOut(8000, function() {
					$("[id='changingText']").show().text('This text will change after taking an action.')
							.css({ 'font-weight' : 'bold'});
				});
			}
		
			// onReady function
			$(function() {
				console.log("starting onReady function...")
				$('#currentAccount').on('input',function() {
				    let opt = $('option[value="'+$(this).val()+'"]');
				    let acct = opt.attr('value');
				    console.log('opt: ' + opt);
				    console.log('acct: ' + acct);
				    $('#currentAccountHidden').val(acct);
				    //console.log('input value: ' + opt.attr('value'));
				    console.log('currentAccountHidden value: ' + $('#currentAccountHidden').val());
				    //alert(opt.length ? opt.attr('value') : 'NO OPTION');
				  });

				$( "#FromDate" ).datepicker();
				$( "#ToDate" ).datepicker();

			    let acctsAndInsIdMap = function () {
					var list = [];
					var map = new Map();
					var acctsAndInsIdMap = new Map();
				    $.ajax({
				        'type': "GET",
				        'url': "Services",
				        'data': { 'method': 'getAcctsAndInstitutionIdMap' },
				        'success': function (data) {
					        console.log("successful retrieval of acctAndInstitutionIdMap");
					        //console.log(JSON.parse(data));
							map = JSON.parse(data);
					        var mapIter = Object.keys(JSON.parse(data));
					        
				            for (const key in map) {
								for (const acct in map[key]) {
									//console.log(map[key][acct]);
									list.push(map[key][acct]);
									//console.log('list: ' + list);
								}
								acctsAndInsIdMap.set(key, list);
				            }
				        }
				    });
				    return acctsAndInsIdMap;
				}(jQuery);
				//console.log('acctsAndInsIdMap');
				if (acctsAndInsIdMap.size !== 0) {
					for (const key in acctsAndInsIdMap) {
						console.log('key: ' + key);
						for (const acct in acctsAndInsIdMap[key]) {
							console.log('acct: ' + acct);

						}
		            }
				}
				
				let categoriesMap = function () {
				    var categoriesM = new Map();
				    $.ajax({
				        'type': "GET",
				        'url': "Services",
				        'data': { 'method': 'getAllCategories' },
				        'success': function (data) {
					        //console.log("successful retrieval of categories");
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
				//console.log('categoriesMap: ' + categoriesMap);
				//console.log(categoriesMap);
				
				$('#currentCategoryDiv').hide();
				//$("#transactionsTable").hide();
		
				$('#checked').on("mouseover", function() {
					 document.body.style.cursor="pointer";
				}).on("mouseout", function() {
					document.body.style.cursor="default";}).on("click", function() {
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
		
				$('#currentCategory')
						.on("click", function() {
					console.log("clicked current category")
					
				})
						.change(function() {
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
		
				$('#budgetedAmt').focus(function() {
					console.log("budget amount has focus.");
					var text = $("#currentCategory").val();
					if (text != "") {
						console.log("determine how to populate budgeted amount for this category");
					} else {
						console.log("user still needs to provide a category name");
					}
				});

				var text = $("[id='changingText']").text();
				var goodText = "You have successfully loaded";
				var goodLength = goodText.length;
				matchGoodText = text.substring(0, goodLength);
				if ( matchGoodText === goodText ) {
					$("[id='changingText']").fadeOut(8000, function() {
						$("[id='changingText']").show().text('This text will change after taking an action.')
								.css({ 'font-weight' : 'bold'});
					});
				}
				else if (text !== 'This text will change after taking an action.') {
					// Default
					$("[id='changingText']").fadeOut(20000, function() {
						$("[id='changingText']").show().text('This text will change after taking an action.')
								.css({ 'font-weight' : 'bold'});
					});
				}
				else {
						// Don't fade the text
				}

				$("[id='cHeader']").on("mouseover", function() {
					document.body.style.cursor="pointer";
					if (this.text === 'Your Categories') this.title = 'Change to New Categories';
					else this.title = 'Change to Your Categories';
				}).on("mouseout", function() {
					document.body.style.cursor="default";})
				.click(function() {
					if ($("[id='cHeader']").text() === "New Categories")  {
						console.log("activating current categories list...");
						$('#newCategoryDiv').hide();
						$('#currentCategoryDiv').show();
						$("[id='cHeader']").text("Your Categories");
					} else {
						console.log("activating new categories view...");
						$('#currentCategoryDiv').hide();
						$('#newCategoryDiv').show();
						$("[id='cHeader']").text("New Categories");
					}
				});

				$("[id='tHeader']").on("mouseover", function() {
					document.body.style.cursor="pointer";
					this.title = "Clear all transactions"
				}).on("mouseout", function() {
					document.body.style.cursor="default";})
				.click(function() {
					$("#displayTransactionsDiv").empty();
					// clear transactions from user and session
					$.ajax({
						'type': "GET",
						'url': "Services",
						'data': { 'method': 'clearTransactions' },
						'success': function (data) {
							//console.log("successful retrieval of categories");
							//console.log(data);
							console.log("cleared transactions from user: " + JSON.parse(data));
						}
					});
				});
			});  // End on ready function
		</script>

	</body>
	<footer id="date" class="footer">${dateAndTime}</footer>
</html>