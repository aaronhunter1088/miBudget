<%@ include file="/WEB-INF/views/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/wallet.ico"/>
        <title>miBudget Springboot</title>
        <style>
            html {
                background-image: url("${pageContext.request.contextPath}/images/background.jpg");
                background-position: center center;
                background-repeat:  no-repeat;
                background-attachment: fixed;
                background-size:  cover;
            }
            <!-- NEEDED -->
            body {
                display: block;
                margin: 8px;
                background-position: center center;
                background-repeat:  no-repeat;
                background-attachment: fixed;
                background-size:  cover;
            }
            .button {
                font-weight: bold;
            }
            .cursor {
                cursor:pointer;
            }
        </style>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
    </head>
    <body>
        <h1>Welcome to miBudget (Vault)!!!</h1>
        <h3>miBudget is a personal budget assistant.<br>As you spend money,
            miBudget will place transaction into categories, as you define where <br>the transactions will go. All you ever need to do is to define new
            transaction-mappings to <br>categories as new transaction-mappings are made. Then you simply view your
            budget to <br>see you are doing, where you can save money, slow spending, and more.
        </h3>
        <br/>
        <input type="button" id="loginBtn" class="button cursor" value="Login"/>
        <br/>
        <br/>
        <input type="button" id="registerBtn" class="button cursor" value="Register"/>
        <br/>
    </body>
    <script type="text/javascript">
        $(function() {
            $('.button').removeAttr('disabled');
            $('#loginBtn').on("click", function() {
                console.log('clicked Login button.')
                window.location.href = "${pageContext.request.contextPath}/login"
            });
            $('#registerBtn').on("click", function() {
                console.log('clicked Register button.')
                window.location.href = "${pageContext.request.contextPath}/register"
            });
        });
    </script>
</html>
