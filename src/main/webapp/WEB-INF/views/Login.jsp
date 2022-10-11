<%@ include file="/WEB-INF/views/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="${pageContext.request.contextPath}/images/wallet.ico" rel="icon" type="image/x-icon">
        <title>Login</title>
        <style>
            <!-- NEEDED -->
            html {
              background-color: #32CD32;
            }
            body {
              justify-content: center;
              display: flex;
                margin-right: auto;
                margin-left: auto;
              background-color: #32CD32;
              width:fit-content;
              height:fit-content;
            }
            ::placeholder {
              color: black;
              font-weight: bold;
              text-align: center;
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
        <div id="loginForm" style="text-align:center;">
            <h1>Login</h1>
            <input id="validated" type="hidden" value="false"/>
            <input id="btnSelected" type="hidden" value="Cancel"/>
            <input id="cellphone" placeholder="Enter cellphone" required tabindex="1" type="text" value=""/>
            <br>
            <hr>
            <input id="password" placeholder="Enter password" required tabindex="2" type="password" value=""/>
            <br>
            <hr>
            <label><input checked="checked" id="remember-me" type="checkbox"/> Remember me</label>
            <br>
            <br>
            <input type="button" id="cancelBtn" class="button cursor" value="Cancel"/>
            <input type="button" id="loginBtn" class="button cursor" value="Submit"/>
            <hr>
            <input type="button" id="forgotPasswordBtn" class="button cursor" value="Forgot Password"/>
        </div>
    </body>
    <script type="text/javascript">
        $(function() {
            $('.button').removeAttr('disabled')
            $('#cancelBtn').on('click', function() {
                window.location.href = "${pageContext.request.contextPath}/"
                console.log('clicked Cancel button. Returned to Homepage')
            });
            $('#loginBtn').on('click', function() {
                let loginForm = $("#loginForm")
                // get cellphone
                let cellphone = loginForm
                    .find('input[id=cellphone]')
                    .val();
                // get password
                let password = loginForm
                    .find('input[id=password]')
                    .val();
                if (validateLoginFields(cellphone, password)) {
                    loginForm.find('input[id=Validated]').val("true");
                    loginForm.find('input[id=btnSelected]').val(loginForm.find('input[id=loginBtn]').text());
                    login(cellphone, password, true);
                }
            });
        })
        function validateLoginFields(cellphone, password) {
            if (cellphone === "") {
                alert("Cellphone cannot be blank.");
                return false;
            }
            cellphone = cellphone.toString().replaceAll("-", "");
            if (isNaN(cellphone)) {
                alert("Invalid cellphone format. Cellphones cannot include letters.");
                return false;
            }
            else if (cellphone.length !== 10) {
                alert("Invalid cellphone format. Please provide 10 digits.");
                return false;
            }
            if (password === "") {
                alert("Password cannot be blank.");
                return false;
            }
            return true;
        }
        function login(cellphone, password, validated) {
            //let csrfHeader = $("meta[name='_csrf_header']").attr("content");
            //let csrfToken = $("meta[name='_csrf']").attr("content");
            //console.log("csrfHeader: " + csrfHeader)
            //console.log("csrfToken: " + csrfToken);
            $.ajax({
                headers: {
                    accept: "application/json",
                    contentType: "application/json"
                    // csrfHeader: csrfToken
                },
                type: "POST",
                url: "${pageContext.request.contextPath}/login",
                async: true,
                dataType: "application/json",
                crossDomain: true,
                data: {
                    cellphone: cellphone,
                    password: password,
                    btnSelected: 'Login',
                    validated: validated
                },
                statusCode: {
                    200: function(data) {
                        console.log(JSON.stringify(data))
                        window.location.href = "${pageContext.request.contextPath}/homepage";
                    },
                    400: function() {
                        window.location.href = "${pageContext.request.contextPath}/register"
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
    </script>
</html>