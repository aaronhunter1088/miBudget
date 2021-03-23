<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Login - NO LONGER USED</title>
        <style>
            <!-- NEEDED -->
            body {
                display: block;
                margin: 8px;
            }
        </style>
    </head>

    <body bgcolor="#32CD32">
        <h1>Login</h1>
        <form action="Login" method="post" action="Profile.jsp">
            <div class="container">
                <input type="text" placeholder="Enter cellphone" name="Cellphone" value="${Cellphone}" required>
                <br/>
                <hr/>
                <input type="password" placeholder="Enter Password" name="Password" value="${Password}" required>
                <br/>
                <hr/>
                <label>
                    <input type="checkbox" checked="checked" name="remember"> Remember me
                </label>
                <br/>
                <br/>
                <button type="submit">Login</button>
                <!-- Divider -->
                <input class="button" type="button" onclick="window.location.replace('index.html')" value="Cancel" />
                <br/>
                <hr/>
                <input class="button" type="button" onclick="window.location.replace('ForgotPassword.jsp')" value="Forgot Password" />
                <!-- <span class="forgotpassword"><a href="ForgotPassword.html">Forgot password?</a></span> -->
            </div>
        </form>
    </body>
</html>