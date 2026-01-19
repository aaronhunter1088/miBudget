<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ page import="com.miBudget.entities.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/wallet.ico"/>
		<title>Categories and Transactions</title>
		<!-- jQuery -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
		<!-- JavaScript Bundle with Popper -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <style>
			<!-- NEEDED -->
			html {
				background-color: aqua;
			}
			body {
				display: block;
				margin: 8px;
				background-color: aqua;
			}
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
				cursor:pointer;
			}
			i, button {
				cursor:pointer;
			}
			.center {
				top: 50%;
				left: 50%;
				margin-top: -10px;
				margin-left: -10px;
			}
			<!-- Defines inner and outer divs -->
			.outer {
				position: fixed;
				display: block;
				width: 49%;
				height: 100%;
				background-color: black;
			}
			.inner {
				width: calc(100% - 10px);
				height: calc(100% - 10px);
				margin: 50px;
				display: inline-flex;
			}
			.cursor {
				cursor:pointer;
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
				border:none
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
	<body>
		<h1 class="font1">Categories and Transactions for <i>${user.getFirstName()} ${user.getLastName()}</i></h1>
		<br/>
		<div style="display: inline-block; width:30%">
			<input id="homepageBtn" type="button" class="button cursor" value="Homepage"/>
		</div>
		<div style="width: 525px; display: inline-block; overflow-wrap: break-word; word-wrap:break-word; word-break: break-all; vertical-align: top;" class="container">
			<p id="changingText" class="changingText">${change}</p>
		</div>
		<br/>
		<!-- Headings for DIVs -->
		<h3 style="display:inline;width:50%;float:left;text-align:center;" id="cHeader">New Categories</h3>
		<h3 style="display:inline;width:50%;float:right;text-align:center;" id="tHeader">Your Transactions</h3>
		<!-- Start New Categories Div -->
		<div id="categoriesDiv" class="border outer" style="width:50%;float:left; overflow-y:auto;">
			<div id="newCategoryDiv" class="outer">
				<div class="container" class="inner" style="margin-top:1em;">
					<div id="nameAndAmtDiv" class="" style="display:inline-flex">
						<div class="input-group" id="newCategoryNameDiv" style="display:inherit">
							<input class="form-control" id="newCategoryName" type="text" style="direction:LTR; margin-left:20px;" placeholder="Name of Category">
							<span class="input-group-addon" style="">
								<i onclick="clearInput('categoryName')" class="glyphicon glyphicon-remove" style="text-align:center; font-size:10px;"></i>
							</span>
						</div>
						<div class="input-group" id="newCategoryCurrencyDiv" style="display:inherit">
							<input class="form-control" id="newCategoryAmt" type="text" style="direction:LTR;" placeholder="Amount" tabindex="2" name="newCategoryAmount" title="New category amount" value="" required>
							<select class="form-select" style="font-weight: bold;" id="newCategoryCurrency">
								<option selected>Currency</option>
								<option value="1">USD</option>
								<option value="2">CAD</option>
								<option value="3">Mex$</option>
								<option value="4">Euro</option>
							</select>
						</div>
					</div>
				</div>
				<br/>
				<div class="container">
					<button type="button" id="newRuleBtn" class="btn btn-default btn-sm">New Rule</button>
					<button type="button" id="existingRulesBtn" class="btn btn-default btn-sm">Existing Rules</button>
				</div>
				<div class="container" class="inner" id="newRuleDiv" style="margin-top:1em;">
					<p>New Rule</p>
					<p>Define some rules to help your budget automatically map transactions to the proper category. Click
					the 'Existing Rules' button to see rules defined for this category.</p>
					<div id="newRules" style="display:block;">
						<div class="form-check" style="font-size:10px;">
							<input class="form-check-input" type="checkbox" value="Transaction Amount" id="transactionAmtCheckbox">
							<label class="form-check-label" for="transactionAmtCheckbox">
								Transaction Amount
							</label>
							<br/>
							<div class="input-group" id="categoryOption" style="" disabled="true">
								<input class="form-control" id="ruleTransAmount" type="text" style="direction:LTR; width:10em;" placeholder="Amount" tabindex="2" title="New category amount" value="" required>
								<select class="form-select" id="ruleTransCurrency" style="width:5em;">
									<option selected>Currency</option>
									<option value="1">USD</option>
									<option value="2">CAD</option>
									<option value="3">Mex$</option>
									<option value="4">Euro</option>
								</select>
							</div>
						</div>
						<br/>
						<div class="form-check" style="font-size:10px;">
							<input class="form-check-input" type="checkbox" value="Transaction Name" id="transactionNameCheckbox">
							<label class="form-check-label" for="transactionNameCheckbox">
								Transaction Name
							</label>
							<br/>
							<div class="input-group" style="display:inline-flex">
								<input class="form-control" id="ruleTransName" type="text" style="" placeholder="Name of Transaction">
								<span class="input-group-addon" style="">
								<i onclick="clearInput('ruleTransName')" class="glyphicon glyphicon-remove" style="font-size:10px;"></i>
							</span>
							</div>
						</div>
						<br/>
						<div class="container" style="text-align:center;">
							<button type="button" class="btn btn-outline-dark" id="saveRuleBtn">Save Rule</button>
							<button type="button" class="btn btn-outline-dark" id="wipeFormBtn">Wipe Form</button>
							<button type="button" class="btn btn-outline-dark" id="saveCategoryBtn">Save Category</button>
						</div>
					</div>
				</div>
				<div class="container" class="inner" id="existingRulesDiv" style="margin-top:1em;">
					<p>Existing Rules</p>
<%--					<div id="existingRulesDiv" style="height: 90px; width:99%; overflow-y: scroll;">--%>
						<table class="outerTable" id="existingRulesTable" style="overflow-y: scroll;">
							<tr id="existingRulesHeader" name="">
								<th>
									<h4 id="ruleNumber"></h4>
								</th>
								<th>
									<h4 id="ruleReason"></h4>
								</th>
								<th colspan="2">
									<h4 id="ruleBtns"></h4>
								</th>
							</tr>
							<%
								Category category = (Category) session.getAttribute("newCategoryName");
								List<Rule> rules = category != null ? category.getRules() : Collections.EMPTY_LIST;
								int i = 0;
								for (Rule rule : rules) {
									++i;
							%>
							<!-- [Rule #: <the rule> | Edit | Delete] -->
							<tr>
								<td>
									Rule <%= i %>
								</td>
								<% if (rule.getRuleName() != null) { %>
								<td>
									<%= rule.getRuleName() %>
								</td>
								<% } else { %>
								<td>
									<%= rule.getRuleAmount() %>
								</td>
								<% } %>
								<td id="<%= rule.getId() %>">
									<button type="button" class="btn btn-primary btn-sm">Edit</button>
								</td>
								<td id="<%= rule.getId() %>">
									<button type="button" class="btn btn-primary btn-sm">Delete</button>
								</td>
							</tr>
							<% } %>
						</table>
<%--					</div>--%>
				</div>
				<br/>
				<br/>
			</div>
			<!-- Whitespace -->
			<div id="currentCategoryDiv" class="outer">
				<div class="input-control">
					<input id="currentCategory"  class="form-control" list="categories1" type="text" style="width:244px; width:99%;" placeholder="Choose a Category" tabindex="1" name="categoryName" required/>
					<datalist id="categories1">
						<% User user = (User) session.getAttribute("user");
						   List<Category> categoriesList = user.getBudget().getCategories();
						   for (Category cat : categoriesList) { %>
						<option value="<%= cat.getName() %>"></option>
								<% } %>
					</datalist>
				</div>
				<br/>
				<div id="currentCategoryCurrencyBlock" style="width:99%;">
					<input id="currentCategoryCurrency" list="currencies" style="width:100px; display:inline-block; margin-left:50px;" placeholder="Currency" tabindex="2" name="currency" />
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
				<button type="submit" style="right:0%; left:71%; width:130px; white-space: nowrap;" id="saveRule2">Save Rule</button>

				<br/>

				<div id="group2">
					<p style="float:left;">If a transactions amount is:</p>
					<input id="transactionPrecedence2" name="group2" value="precedenceAmount" style="float:right;" type="radio">
					<input id="transactionPrecedence3" name="group2" value="precedenceName" style="float:right;" type="radio"/>
					<p style="float:left;">If a transactions name is like:</p>
					<br/>
					<div class="input-group" style="right:10px;">
						<input id="merchantsName3" class="form-control" type="text" style="width: 205px; display:block; left:0px;" placeholder="ex: McDs, Target, Sonic" tabindex="7" name="merchantsName" value="" required/>
						<span class="input-group-addon">
							<i id="glyphiconRemove" onclick="clearInput('merchantsName3')" class="glyphicon glyphicon-remove"></i>
					 	</span>
					</div>
				</div>
				<br/>
				<br/>
				<div id="displayRules" style="height: 90px; width:99%; overflow: scroll;">
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
		<div id="transactionsDiv" class="border outer" style="width:50%;float:right; overflow-y:auto; overflow-x:hidden;">
			<div id="getTransactionsDiv" class="outer">
				<form action="cat" method="post">
					<div style="width:100%; display:flex; flex-direction: row;">
						<input style="display: block; width:50%; float:left;" title="The From Date is required" class="form-control" id="FromDate" name="FromDate" placeholder="From" tabindex="#" type="text" value=""><br>
						<input style="display: block; width:50%; float:right;" title="The To Date is required" class="form-control" id="ToDate" name="ToDate" placeholder="To" tabindex="#" type="text" value=""><br>
					</div>
					<input type="hidden" name="validated" value="false"/>
					<input type="hidden" name="formId" value="transactions"/>
					<input type="hidden" name="methodName" value="get transactions"/>
					<input type="hidden" name="ignoredTransactions" value="<%= user.getIgnoredTransactions() == null || user.getIgnoredTransactions().isEmpty() ? 0 : user.getIgnoredTransactions().size() %>"/>
					<div style="display: flex; flex-direction: row;">
						<input type="text" id="numberOfTrans" name="numberOfTrans" class="form-control" style="width:50px; text-align:center; display: block;" title="Enter the number of transactions <=50 you wish to receive" placeholder="#"/>
						<input id="currentAccount" name="currentAccount" value="" title="Choose an Account to pull transactions from" class="form-control" list="accounts" autocomplete="off" type="text" style="width:75%; text-align:center;" placeholder="Choose an Account" tabindex="1" required/>
						<datalist id="accounts">
							<% Map<String, List<Account>> acctsMap = (HashMap<String, List<Account>>) session.getAttribute("institutionIdsAndAccounts");
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
			<div id="displayTransactionsDiv" class="mainTable inner">
				<table class="innerTable" id="innerTable" style="margin-left: auto; margin-right: auto; width:100%;">
					<%
						// Java code here
						List<Transaction> transactions = user.getTransactions();
						for (int z = 0; z < transactions.size(); z++) {
							Transaction transaction = transactions.get(z);
					%>
						<tr id="header" name="transactionName">
							<th colspan="2">
								<h4 id="TransactionMapping" style="text-align: center">Transaction <%= (z+1) %></h4>
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
		<script type="text/javascript">
			$(function() {
				console.log("starting onReady function...")
				var category = {
					name: '',
					amount: '',
					currency: '',
					rulesList: []
				};
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
					// $.ajax({
					//     'type': "GET",
					//     'url': "Services",
					//     'data': { 'method': 'getinstitutionIdsAndAccounts' },
					//     'success': function (data) {
					//         console.log("successful retrieval of acctAndInstitutionIdMap");
					//         //console.log(JSON.parse(data));
					// 		map = JSON.parse(data);
					//         var mapIter = Object.keys(JSON.parse(data));
					//
					//         for (const key in map) {
					// 			for (const acct in map[key]) {
					// 				//console.log(map[key][acct]);
					// 				list.push(map[key][acct]);
					// 				//console.log('list: ' + list);
					// 			}
					// 			acctsAndInsIdMap.set(key, list);
					//         }
					//     }
					// });
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
					var userId = '<%= ((User)session.getAttribute("user")).getId() %>';
					var userMainBudgetId = '<%= ((User)session.getAttribute("user")).getMainBudgetId() %>';
					$.ajax({
					    'type': "GET",
					    'url': "${pageContext.request.contextPath}/services/budgets/"+userId+"/"+userMainBudgetId+"/categories",
					    //'data': { 'method': 'getAllCategories' },
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
				}(jQuery);
				//console.log('categoriesMap: ' + categoriesMap);
				//console.log(categoriesMap);

				$('#currentCategoryDiv').hide();
				$('#existingRulesDiv').hide();
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
							if (text !== "") {
								//console.log("text is: " + text + ". determine how to populate budgeted amount for this category");
								var category = categoriesMap.get(text);
								//category = {name: _name, amount: _budgetAmt, currency: _currency};
								//categoriesM.set(_name, category);
								console.log('category: ' + category.name);
								console.log('budgeted amt: ' + category.amount);
								console.log('currency: ' + category.currency);
								var budgetText = category.amount;
								var currency = category.currency;
								switch (text) {
									case "Mortgage" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									case "Utilities" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									case "Transportation" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									case "Insurance" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									case "Food" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									case "Subscriptions" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
									// update to Bills
									case "Bill" : {
										$('[id="budgetedAmt"]').val(budgetText);
										$('[id="currentCategoryCurrency"]').val(currency);
										break;
									}
								}
							}
							else {
								$("#budgetedAmt").val("");
								$("#currentCurrency").val("");
							}

						});
				<!-- Button On New Categories Form -->
				$('#existingRulesBtn').on('click', function() {
					$('#newRuleDiv').hide();
					$('#existingRulesDiv').show();
				});
				$('#newRuleBtn').on('click', function() {
					$('#newRuleDiv').show();
					$('#existingRulesDiv').hide();
				});
				$('#saveRuleBtn').on('click', function () {
					var ruleTransAmtCheckbox = $('#transactionAmtCheckbox');
					var ruleTransNameCheckbox = $('#transactionNameCheckbox');
					var ruleTransAmt = $('#ruleTransAmount');
					var ruleTransCurrency = $('#ruleTransCurrency');
					var ruleTransName = $('#ruleTransName');
					var rule = {
						amount: ruleTransAmt.val(),
						currency: ruleTransCurrency.val(),
						transName: ruleTransName.val(),
					}
					console.log("rule: " + JSON.stringify(rule));
					if (ruleTransAmtCheckbox.is(":checked") && ruleTransNameCheckbox.is(":checked") ) {
						var length = category.rulesList.length;
						category.rulesList[length] = rule;
						console.log(category.rulesList[length]);
					} else if (ruleTransAmtCheckbox.is(":checked") && !ruleTransNameCheckbox.is(":checked")) {
						var length = category.rulesList.length;
						category.rulesList[length] = rule;
						console.log(category.rulesList[length]);
					} else if (!ruleTransAmtCheckbox.is(":checked") && ruleTransNameCheckbox.is(":checked")) {
						var length = category.rulesList.length;
						category.rulesList[length] = rule;
						console.log(category.rulesList[length]);
					}
				})

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
					if (this.text === 'Your Categories') this.title = 'Click to change to \'New Categories\'';
					else this.title = 'Click to change to \'Your Categories\'';
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
						.click(function()

						{
							$("#displayTransactionsDiv").empty();
							// clear transactions from user and session
							// $.ajax({
							// 	'type': "GET",
							// 	'url': "Services",
							// 	'data': { 'method': 'clearTransactions' },
							// 	'success': function (data) {
							// 		//console.log("successful retrieval of categories");
							// 		//console.log(data);
							// 		console.log("cleared transactions from user: " + JSON.parse(data));
							// 	}
							// });
						});

				$('#homepageBtn').on("click", function() {
					$.ajax({
						type: "GET",
						url: "${pageContext.request.contextPath}/homepage/",
						statusCode: {
							200: function(response) {
								console.log("Success: Go to Homepage")
								document.open();
								document.write(response);
								document.close();
							},
							400: function() {
								console.log("400: Bad request")
							},
							404: function() {
								console.log('404: not found');
							},
							500: function() {
								console.log('500: ISE')
							}
						}
					});
				});
			});  // End on ready function

			function performTransactionAction() {
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
			function clearInput(target) {
				//alert("Clearing this text box.")
				console.log('target before text: ' + $(target).text(''));
				console.log('target before val: ' + $(target).val(''));
				$(target.text).val("");
				console.log('target after: ' + $(target.text).text());

				if (target === "categoryName") {
					console.log('target before: ' + $('#categoryName').val());
					$('#categoryName').val("");
					console.log('target after: ' + $('#categoryName').val());
					
				} else if (target === "merchantsName") {
					console.log('target before: ' + $('#merchantsName').val());
					$('#merchantsName').val("");
					console.log('target after: ' + $('#merchantsName').val());
				} else if (target === "merchantsName2") {
					console.log('target before: ' + $('#merchantsName2').val());
					$('#merchantsName2').val("");
					console.log('target after: ' + $('#merchantsName2').val());
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

			} //validate field
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
		</script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
	</body>
	<footer id="date" class="footer">${dateAndTime}</footer>
</html>