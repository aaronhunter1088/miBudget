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
	<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="icon" type="image/x-icon" href="wallet.ico">
		<title>Categories and Transactions</title>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<style>
			img {
				width : 50px;
				height: 50px;
				border-style : solid;
				cursor:pointer;
			}
			.images {
				display: all; /* all */
			}
			button{
			    height:21px; 
			    width:100px; 
			    margin: -20px -50px; 
			    position:relative;
			    top:50%; 
			    left:50%;
			    cursor:pointer;
			}
			.updateButton {
			}
			.mainTable, th, td {
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
			p.a {
			    font-family: "Times New Roman", Times, serif;
			}
			p.b {
			    font-family: Arial, Helvetica, sans-serif;
			}
			.outertable {}
			.innertable {
				visibility: hidden; /* visible */
			}
			.acct {}
			.border {
				border-style : solid;
			}
			::placeholder {
				text-align: center;
				color: black;
				font-weight: bold;
			}
			::list {
				position: left;
			}
		</style>
	</head>
	<body>
		<h1 class="font1">Categories and Transactions for <i>${Firstname} ${Lastname}</i></h1>
		<br/>
		<% User user = (User)session.getAttribute("user"); %>
		<% MiBudgetDAOImpl miBudgetDAOImpl = new MiBudgetDAOImpl(); %>
		<% AccountDAOImpl accountDAOImpl = new AccountDAOImpl(); %>
		<% ItemDAOImpl itemsDAOImpl = new ItemDAOImpl(); %>
		<div id="profileDiv" style="float:left; margin-left:50px;">
			<form action="Profile" method="get"> <!-- think about chaning this call to get -->
<!-- 			<input class="button" type="submit" value="Profile">
 -->			<button type="submit">Profile</button>
			</form>
		</div>
		<br/>
		<div style="text-align:center;width:100%">
			<h3 style="display:inline;width:50%;float:left;text-align:center;">Categories</h3>
			<h3 style="display:inline;width:50%;float:right;text-align:center;">Transactions</h3>
		</div>
			
		<div id="categoryForms" class="border" style="width:49%; float:left;"> <!-- 425px -->
			<form id="newCategoryForm" onsubmit="" method="post" action="CAT">
				<div style="float:left; width:260px;" id="innerCategoryDiv">
					<div class="input-group" style="width:240px;">
						<span class="input-group-addon">
							<i id="glyphiconRemove" onclick="clearInput('categoryName')" class="glyphicon glyphicon-remove"></i>
						</span>
						<input class="form-control" id="categoryName" type="text" placeholder="Name of Category" tabindex="1" name="categoryName" required>
	 				</div>
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
						<input type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Budgeted Amount" tabindex="3" name="categoryAmt" value="" required>
					</div>
					<br/>
					<p style="text-align:center;">Rules</p>
					<p>Rules takes precedence when new transactions come in and will affect this Category! You can define both types of rules but you must choose which one will take precedence.</p>
					<br/>
							
					<div>
						<input id="transactionPrecendence" name="precedence" style="float:right;" type="radio">
							<p style="float:left;">If a transactions amount is:</p>
							<br/>
							<div id="currencyBlock">
								<input list="currencies" style="width:100px; display:block; float:left;" placeholder="Currency" tabindex="4" name="currency" />
								<!-- TODO: Once one is selected, list disappears basically. Figure out how to keep entire list. -->
								<datalist id="currencies">
								  <option value="USD">
								  <option value="CAD">
								  <option value="Euro">
								  <option value="Mex$">
								</datalist>
								<input type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Budgeted Amount" tabindex="5" name="categoryAmt" value="" required/>
							</div>
								
						<input id="transactionPrecendence" name="precedence" style="float:right;" type="radio">
							<p style="float:left;">If a transactions name is like:</p>
							<br/>
							<div class="input-group">
								<span class="input-group-addon">
									<i id="glyphiconRemove" onclick="clearInput('merchantsName')" class="glyphicon glyphicon-remove"></i>
								</span>
								<input id="merchantsName" class="form-control" type="text" style="width: 205px; display:block; margin-left:0px;" placeholder="ex: McDs, Target, Sonic" tabindex="6" name="merchantsName" value="" required/>
							</div>
								
					</div>	
					<br/>
					<button type="submit" style="width:180px; algin-items:center" tabindex="7" id="saveNewCategory">Save New Category</button>
				</div>
			</form>
					
			<form id="currentCategoryForm" onsubmit="" method="post" action="CAT">
				<div style="float:left; width:260px; text-align:center;" id="innerCategoryDiv">
					<div class="input-control">
						<input id="currentCategory"  class="form-control" list="categories" type="text" style="width:244px;" placeholder="Choose a Category" tabindex="1" name="categoryName" required/>
							<datalist id="categories">
								<% ArrayList<Category> categoriesList = miBudgetDAOImpl.getAllCategories(user);
									for (Category cat : categoriesList) { %>
									    <option value="<%= cat.getName() %>">
								<% } %>
							</datalist>
					</div>
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
						<input id="budgetedAmt" type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Budgeted Amount" tabindex="3" name="categoryAmt" value="" required>
					</div>
					<br/>
					<p style="text-align:center;">Rules</p>
					<p>Rules takes precedence when new transactions come in and will affect this Category! You can define both types of rules but you must choose which one will take precedence.</p>
					<br/>
							
					<div>
						<input id="transactionPrecendence" name="precedence" style="float:right;" type="radio">
						<p style="float:left;">If a transactions amount is:</p>
						<br/>
						<input list="currencies" style="width:100px; display:block; float:left;" placeholder="Currency" tabindex="4" name="currency" />
							<datalist id="currencies">
								<option value="USD">
								<option value="CAD">
								<option value="Euro">
								<option value="Mex$">
							</datalist>
						<input type="text" style="direction:RTL; width: 144px; display:block; margin-left:100px;" placeholder="Budgeted Amount" tabindex="5" name="categoryAmt" value="" required/>
							
						<input id="transactionPrecendence" name="precedence" style="float:right;" type="radio"/>
						<p style="float:left;">If a transactions name is like:</p>
						<br/>
						<div class="input-group">
							 <span class="input-group-addon">
							 	<i id="glyphiconRemove" onclick="clearInput('merchantsName2')" class="glyphicon glyphicon-remove"></i>
							 </span>
							 <input id="merchantsName2" class="form-control" type="text" style="width: 205px; display:block; margin-left:0px;" placeholder="ex: McDs, Target, Sonic" tabindex="7" name="merchantsName" value="" required/>
						</div>
					</div>
					<br/>
					<br/>
					<button type="submit" style="float:left; left:19%; width:130px; white-space: nowrap;" id="deleteSavedCategory">Delete Category</button>
					<button type="submit" style="float:left; left:70%; width:130px; white-space: nowrap;" id="savedUpdatedCategory">Save Category</button>
					<br/>
					<br/>
				</div>
			</form>
			<p id="checked" style="float:right; font-size:90%;" value="New Category" name="newCategory">Click for Your Categories</p>
					
					
					
		</div>
				
		<!-- Form Line Divider -->
				
		<!-- Transactions form area -->
		<div class="border" style="width:49%; float:right;">
			<!-- <form id="transactions" onsubmit="return validateFields()" method="post" action="CAT"> -->
				<div id="transactions" style="width:99%; text-align:center;">
					<datalist id="accounts">
						<% HashMap<String, ArrayList<Account>> acctsMap = (HashMap<String, ArrayList<Account>>)
										session.getAttribute("acctsAndInstitutionIdMap");
						   for (String insId : acctsMap.keySet()) {
						       for (Account acct : acctsMap.get(insId)) { 
							       String name = acct.getNameOfAccount(); %>
								   <option label="Account" value="<%= name %>"/>
							   <% } %>
						   <% } 
						%>
					</datalist>
					<input type="hidden" name="validated" value="false"/>
					<input type="hidden" name="formId" value="transactions"/>
					<input type="text" name="numberOfTrans" class="form-control" style="width:50px; display:inline-block;" title="Enter the number of transactions you wish to receive" placeholder="#"/>
					<input id="currentAccount" name="currentAccount" class="form-control" list="accounts" type="text" style="width:200px; display: inline-block;" placeholder="Choose an Account" tabindex="1" name="account" required/>
					
					<input type="submit" onclick="validateFields('transactions')" class="form-control" style="width:130px; display:inline-block;" value="Get Transactions"/>
				 </div> 
			<!-- </form> -->
					
			<div id="transactionsTable">
					
			</div>
		</div>
	</body>
		<script>
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
				let validate = true;
				let formName = "#"+name; // #transactions
				
				if (formName == "#transactions") {
					// Check transactions count has value
					let count = $("#transactions")
					.find('input[name=numberOfTrans]')
					.val();
			
					if (count != "") {
						console.log("count: " + count);
					} else
						validate = false;
			
					// Check that an account was chosen
					let account = $("#transactions")
					.find('input[id=currentAccount]')
					.val(); 
			
					if (account != "") {
						console.log("account: " + account);
					} else
						validate = false;
					//let checkbox = $("#showET").prop("checked"); // true or false
					//console.log("checkbox checked?: " + checkbox);
					// either is okay. required when submitting transactions form
			
					if (validate) {
						console.log("form is good to send");
						//$(formName).find('input[name=validated]').val("true");
						console.log("making post request to CAT");
						$.ajax({
					        'type': "POST",
					        'url': "CAT",
					        'data': { 
						        'numberOfTrans': count,
								'currentAccount': account,
								'validated': true,
								'formId': name
					        },				    
					        'success': function (data) {
						        console.log("response from CAT");
						        console.log(data);
					        }
					    });
					}
					
				}
				
				//return validate;
				//return false;
			};
		
			// onReady function
			$(function() {
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
								console.log('key: ' + key);
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
				
				$("#transactions").find('input[name=numberOfTrans]').on('change', function () {
					if ($("#transactions").find('input[name=numberOfTrans]').val() == "") {
						$("#transactions").find('input[name=validated]').val("false");
					}
				});
				
				$("#transactions").find('input[id=currentAccount]').on('change', function () {
					if ($("#transactions").find('input[id=currentAccount]').val() == "" || 
						$("#transactions").find('input[id=currentAccount]').val() == "Choose an Account" )
		
						$("#transactions").find('input[name=validated]').val("false");
				});
				
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
				}(jQuery);
				console.log('categoriesMap');
				console.log(categoriesMap);
				
				$("#currentCategoryForm").hide();
				$("#transactionsTable").hide();
		
				$("#checked").on("mouseover", function() {
					 document.body.style.cursor="pointer";
				}).on("mouseout", function() {
					document.body.style.cursor="default";
				});
				
				$("#checked").on("click", function() {
					if ($("#checked").text() == "Click for Your Categories")  {
						console.log("activating current categories list...");
						$("#newCategoryForm").hide();
						$("#currentCategoryForm").show();
						$("#checked").text("Click for New Categories");
						
					} else {
						console.log("activating new categories view...");
						$("#currentCategoryForm").hide();
						$("#newCategoryForm").show();
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
							case "Mortgage" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Utilities" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Transportation" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Insurance" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Food" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Subscriptions" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
							case "Bills" : $("#budgetedAmt").val(budgetText); $("#currentCurrency").val(currency); break;
						}		
					} else {
						$("#budgetedAmt").val("");
						$("#currentCurreny").val("");
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
				$("#transactions").on('submit', function() {
					
				});
				// Last line of on ready function
			}); // End on ready function
		</script>
</html>