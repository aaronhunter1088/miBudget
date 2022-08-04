<%@ include file="/WEB-INF/views/include.jsp" %>
<%@ page import="com.miBudget.entities.User" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/wallet.ico"/>
        <title>Homepage Springboot</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
        <!-- Optional theme -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">
        <!-- Latest compiled and minified JavaScript -->
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
        <!-- Pie chart -->
        <script src="//cdn.amcharts.com/lib/4/core.js"></script>
        <script src="//cdn.amcharts.com/lib/4/charts.js"></script>
        <script src="//cdn.amcharts.com/lib/4/maps.js"></script>
        <style>
            /* By Element */
            body {
                display: block;
                margin: 8px;
            }
            /* By Class */
            .button {
                font-weight: bold;
            }
            .cursor {
                cursor:pointer;
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
            /* By Element.property */
            h1.font1 {
                font-family: "Times New Roman", Times, serif;
                font-weight: bold;
                font-size: 2em;
                margin-block-start: 0.67em;
                margin-block-end: 0.67em;
                margin-inline-start: 0px;
                margin-inline-end: 0px;
            }
            /* By Element defined class */
            p.a {
                font-family: "Times New Roman", Times, serif;
            }
            p.b {
                font-family: Arial, Helvetica, sans-serif;
            }
            p.changingText {
                font-weight: bold;
            }
            /* By Id */
            #piechart {
                min-width: 310px;
                max-width: 800px;
                height: 400px;
                margin: 0 auto
            }
        </style>
    </head>
    <body>
        <%
            LocalTime time = LocalTime.now();
            int hours = time.getHour();
            if (hours >= 0 && hours < 12) { %>
        <h1 id="greeting" class="font1">Good morning, <i>${user.getFirstName()} ${user.getLastName()}</i></h1>
        <% } else if (hours >= 12 && hours < 16) { %>
        <h1 id="greeting" class="font1">Good afternoon, <i>${user.getFirstName()} ${user.getLastName()}</i></h1>
        <% } else { %>
        <h1 id="greeting" class="font1">Good evening, <i>${user.getFirstName()} ${user.getLastName()}</i></h1>
        <% }
        %>
        <br/>
        <div style="display: inline-block; width:30%">
            <input id="accountsBtn" type="button" class="button cursor" value="Accounts"/>
            <hr/>
            <input id="catBtn" type="button" class="button cursor" value="Categories and Transactions"/>
            <hr/>
            <input id="goalsBtn" type="button" class="button cursor" value="Goals"/>
            <hr/>
            <input id="logoutBtn" type="button" class="button cursor" value="Log out"/>
        </div>
        <div style="display: inline-block; vertical-align:top; width:70%;">
            <p id="changingText" class="changingText">${changingText}</p>
            <br/>
            <div id="piechart" style="width: 100%; height: 400px;"></div>
        </div>
        <script type="text/javascript">
            $(function () {
                console.log("starting onReady function...")
                //window.location.replace("testing")
                let arrayOfCategories = [];
                let setupPieChart = function () {
                    var userId = '<%= ((User)session.getAttribute("user")).getId() %>'
                    var mainBudgetId = '<%= ((User)session.getAttribute("user")).getMainBudgetId() %>';
                    // let categoriesMap = new Map();
                    // $.ajax({
                    //     type: "GET",
                    //     url: "/miBudget/services/budgets/"+userId+'/'+mainBudgetId+"/categories",
                    //     async: true,
                    //     success: function() {
                    //         console.log("success from /miBudget/services/budgets/"+userId+'/'+mainBudgetId+"/categories")
                    //     },
                    //     statusCode: {
                    //         200: function(data) {
                    //             console.log(data);
                    //             data = JSON.parse(data.entity);
                    //             for (var i = 0; i < data.length; i++) {
                    //                 var categoryName = data[i].name;
                    //                 var categoryCurrencyType = data[i].currency;
                    //                 var categoryBudgetAmount = data[i].budgetedAmt;
                    //                 console.log(categoryName);
                    //                 console.log(categoryBudgetAmount);
                    //                 console.log(categoryCurrencyType);
                    //                 categoriesMap.set(categoryName, {name: categoryName,
                    //                     amount: categoryBudgetAmount, currency: categoryCurrencyType});
                    //             }
                    //         },
                    //         400: function(data) {
                    //             console.log(data);
                    //         },
                    //         404: function(data) {
                    //             console.log(data);
                    //         },
                    //         500: function(data) {
                    //             console.log(data);
                    //         }
                    //     }
                    // });
                    $.ajax({
                        type: "GET",
                        url: "/miBudget/services/budgets/"+userId+'/'+mainBudgetId+"/categories",
                        async: true,
                        success: function() {
                            console.log("success from /miBudget/services/budgets/"+userId+'/'+mainBudgetId+"/categories")
                        },
                        statusCode: {
                            200: function(data) {
                                console.log("200")
                                data = JSON.parse(data.entity);
                                arrayOfCategories = data;
                                console.log(data);
                                var chartData = [];
                                for (var i = 0; i < data.length; i++) {
                                    var parsed = data[i];
                                    var categoryName = parsed.name;
                                    var categoryCurrencyType = parsed.currency;
                                    var categoryBudgetAmount = parsed.budgetedAmt;
                                    //console.log(categoryName);
                                    //console.log(categoryBudgetAmount);
                                    //console.log(categoryCurrencyType);
                                    chartData[i] = {"name": categoryName, "amount": categoryBudgetAmount + ' ' + categoryCurrencyType}
                                }
                                let chart = am4core.create(
                                    document.getElementById("piechart"),
                                    am4charts.PieChart
                                );
                                chart.legend = new am4charts.Legend();
                                chart.data = chartData;
                                // Add and configure Series
                                let pieSeries = chart.series.push(new am4charts.PieSeries());
                                // Setting up data fields in Pie series
                                pieSeries.dataFields.category = "name";
                                pieSeries.dataFields.value = "amount";
                            },
                            400: function(data) {
                                console.log(data);
                            },
                            404: function(data) {
                                console.log(data);
                            },
                            500: function(data) {
                                console.log(data);
                            }
                        }
                    });
                    return arrayOfCategories;
                }(jQuery);
                let defaultText = 'This text will change after the user take actions';
                $("[id='changingText']").fadeOut(10000, function () {
                    $("[id='changingText']").show().text(defaultText)
                        .css({'font-weight': 'bold'});
                });
                $('#accountsBtn').on("click", function() {
                    console.log("Clicked Accounts button")
                    window.location.href = "${pageContext.request.contextPath}/accounts"
                })
                $('#catBtn').on("click", function() {
                    console.log("Clicked Categories and Transactions button")
                    window.location.href = "${pageContext.request.contextPath}/cat"
                });
                $('#logoutBtn').on("click", function() {
                    logoutAjax();
                });
            });
            function logoutAjax() {
                $.ajax({
                    headers: {
                        accept: "application/json",
                        contentType: "application/json"
                    },
                    type: "POST",
                    url: "${pageContext.request.contextPath}/logout",
                    async: true,
                    dataType: "application/json",
                    crossDomain: true,
                    data: {},
                    statusCode: {
                        200: function(data) {
                            console.log(JSON.stringify(data));
                            window.location.href = "${pageContext.request.contextPath}/"
                        },
                        204: function(data) {
                            console.log(JSON.stringify(data));
                            window.location.href = "${pageContext.request.contextPath}/"
                        },
                        400: function() {
                            alert("400")
                        },
                        404: function() {
                            alert('not found');
                        }
                    }
                });
            }
        </script>
    </body>
    <footer id="date" class="footer">${dateAndTime}</footer>
</html>