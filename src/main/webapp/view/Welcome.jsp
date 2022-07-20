<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="com.miBudget.entities.User" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Homepage</title>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <link href="../images/wallet.ico" rel="icon" type="image/x-icon">
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
        <style>
            /* Needed */
            body {
                display: block;
                margin: 8px;
            }
            .button {
                font-weight: bold;
            }
            .cursor {
                cursor:pointer;
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
            p.changingText {
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <%
            LocalTime time = LocalTime.now();
            int hours = time.getHour();
            if (hours >= 0 && hours < 12) { %>
                <h1 id="greeting" class="font1">Good morning, <i>${Firstname} ${Lastname}</i></h1>
            <% } else if (hours >= 12 && hours < 16) { %>
                <h1 id="greeting" class="font1">Good afternoon, <i>${Firstname} ${Lastname}</i></h1>
            <% } else { %>
                <h1 id="greeting" class="font1">Good evening, <i>${Firstname} ${Lastname}</i></h1>
            <% }
        %>
        <br/>
        <div style="display: inline-block;">

            <form action="accounts" method="get">
                <%--<input style="cursor:pointer;" type="submit" value="Accounts"/>--%>
                <button type="submit">Accounts</button>
            </form>
            <hr/>
            <!-- <a href="">Categories</a> -->
            <form action="cat" method="get">
                <button type="submit">Categories and Transactions</button>
            </form>
            <%--<input type="button" value="Categories and Transactions" onclick="goToCat()" >--%>
            <hr/>
            <input style="cursor:pointer;" type="submit" value="Goals"/>
            <hr/>
            <!-- Add this button to the profile page -->
            <div id="logout">
                <input type="button" id="logoutBtn" class="button cursor" value="Log out"/>
            </div>
            <hr/>
            <br/>
            <br/>
            <%--			</div>--%>
        </div>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <div style="display: inline-block; vertical-align: top;">
            <div style="display: block;">
                <p id="changingText" class="changingText">${change}</p>
            </div>
            <div style="display: block;">
                <img src="/miBudget/images/budgetconstructionimage.jpg" alt="budget under construction" width="150px" height="150px">
            </div>
        </div>
        <br/>
        <!-- Next line -->
        <p>Next line</p>

        <script>
            $(function () {
                let defaultText = 'This text will change after the user take actions';
                let setupText = 'Once you finish adding accounts, and creating categories, your budget will appear here.';
                //let currentText = $("[id='changingText']").text();
                $("[id='changingText']").fadeOut(20000, function () {
                    $("[id='changingText']").show().text(defaultText)
                        .css({'font-weight': 'bold'});
                });
                $('#logoutBtn').on("click", function() {
                    $.ajax({
                        type: "POST",
                        url: "/miBudget/logout/",
                        statusCode: {
                            200: function() {
                                window.location.href = "../index.html"
                            },
                            400: function() {
                                alert("400")
                            },
                            404: function() {
                                alert('not found');
                            }
                        }
                    });
                });
            });
            function goToCat() {
                $.ajax({
                    type: "Get",
                    url: "/miBudget/cat",
                    data: {}
                }).success(function (response) {
                }).error(function (response) {
                    console.log(response)
                }).done(function () {
                }).fail(function () {
                }).always(function (response) {
                });
            }

        </script>
    </body>
    <footer id="date" class="footer">${dateAndTime}</footer>
</html>