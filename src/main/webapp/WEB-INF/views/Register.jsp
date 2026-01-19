<%@ include file="/WEB-INF/views/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="${pageContext.request.contextPath}/images/wallet.ico" rel="icon" type="image/x-icon">
        <title>Sign Up</title>
        <style>
            <!-- NEEDED -->
            html {
              background-color: #1E90FF;
            }
            body {
              justify-content: center;
                margin-right: auto;
                margin-left: auto;
              text-align: center;
              background-color: #1E90FF;
              width: fit-content;
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
<%--        <form id="registerForm" action="Register", method="POST">--%>
        <div id="loginForm" style="text-align:center;">
            <h1>Sign Up</h1>
            <input name="validated" type="hidden" value="false">
            <input name="btnSelected" type="hidden" value="Cancel">
            <input name="firstName" placeholder="Enter first name" required="required" tabindex="1" type="text">
            <br>
            <hr>
            <input name="lastName" placeholder="Enter last name" tabindex="2" type="text">
            <br>
            <hr>
            <input name="cellphone" maxlength="10" placeholder="Enter cellphone" tabindex="3" type="text">
            <hr>
            <input name="email" placeholder="Enter email" tabindex="4" type="text"> <label id="emailLabel">
            <br>
            <b><i>This will be used <br>for backup purposes only!</i></b></label>
            <hr>
            <input name="password" placeholder="Enter password" tabindex="5" type="password"><br>
            <br>
            <hr>
            <input name="passwordRepeat" placeholder="Repeat password" tabindex="6" type="password">
            <hr>
<%--            <b><label><input type="checkbox" name="remember" style="margin-bottom:15px" type="checkbox"> Remember me</label></b>--%>
            <p style="text-align: -webkit-center;">
                <b>By creating an account, <br>you agree to our <br> <a href="#" style="color:darkblue" title="TODO: Create a page showing Terms & Conditions!">Terms & Conditions</a>.</b>
            </p>
            <input type="button" id="cancelBtn" class="button cursor" value="Cancel"/>
            <input type="button" id="registerBtn" class="button cursor" value="Register"/>
        </div>
<%--        </form>--%>
    </body>
    <script type="text/javascript">
      $(function() {
        $('.button').removeAttr('disabled');
        $('#cancelBtn').on('click', function() {
          window.location.href = "${pageContext.request.contextPath}/"
          console.log('clicked Cancel button. Returned to Homepage')
        });
        $('#registerBtn').on("click", function() {
          let registerForm = $("#registerForm")
          // Check First Name
          let firstName = registerForm // get the form
                  .find('input[name=firstName]') // locate the input element
                  .val(); // get its value
          // Check Last Name
          let lastName = registerForm
                  .find('input[name=lastName]')
                  .val();
          // Check cellphone
          let cellphone = registerForm
                  .find('input[name=cellphone]')
                  .val();
          // Check email; can be blank, but if present validate it!
          let email = registerForm
                  .find('input[name=email]')
                  .val();
          // Check if password-repeat is the same as password
          let password = registerForm
                  .find('input[name=password]')
                  .val();
          let passwordRepeat = registerForm
                  .find('input[name=passwordRepeat]')
                  .val();
          //console.log(firstName);
          //console.log(lastName);
          //console.log(cellphone);
          //console.log(email);
          //console.log(password);
          if (validateFields(firstName, lastName, cellphone, email, password, passwordRepeat)) {
            register(firstName, lastName, cellphone, email, password, passwordRepeat, true);
          } else {
            register(firstName, lastName, cellphone, email, password, passwordRepeat, false);
          }
        });
      });
      function validateFields(firstName, lastName, cellphone, email, password, passwordRepeat) {
        let registerForm = $("#registerForm")
        if (firstName === "") {
          alert("First Name cannot be blank.");
          return false;
        }
        if (lastName === "") {
          alert("Last Name cannot be blank.");
          return false;
        }
        if (cellphone === "") {
          alert("Cellphone cannot be blank.");
          return false;
        }
        cellphone = cellphone.replace(/-/g, '');
        if (cellphone.length !== 10) {
          alert("Invalid cellphone format. Please provide 10 digits.");
          return false;
        }
        if (isNaN(cellphone)) {
          alert("Invalid cellphone format. Cellphones cannot include letters.");
          return false;
        }
        if (email === "") { alert("Email will be blank!"); }
        else {
          if (email.indexOf('@') <= -1) { // provided a value but no @ symbol
            alert("Invalid email format. You must include the \'@\' character.");
            return false;
          }
          if (email.indexOf('.') <= -1) { // provided a value but no dot symbol
            alert("Invalid email format. You must include a \'.\' like in \'.com\'.");
            return false;
          }
        }
        if (password !== passwordRepeat) {
          alert("Repeated password does not match Password!");
          return false;
        }
        registerForm.find('input[name=validated]').val("true");
        registerForm.find('input[name=btnSelected]').val(registerForm.find('input[name=registerBtn]').text());
        return true;
      }
      function register(firstName, lastName, cellphone, email, password, passwordRepeat, validated) {
        $.ajax({
          headers: {
            accept: "application/json",
            contentType: "application/json"
            // csrfHeader: csrfToken
          },
          type: "POST",
          url: "${pageContext.request.contextPath}/register",
          async: true,
          dataType: "application/json",
          crossDomain: true,
          data: {
            firstName: firstName,
            lastName: lastName,
            cellphone: cellphone,
            email: email,
            password: password,
            passwordRepeat: passwordRepeat,
            btnSelected: 'Register',
            validated: validated
          },
          statusCode: {
            200: function(data) {
              console.log(JSON.stringify(data));
              window.location.href = "${pageContext.request.contextPath}/homepage";
            },
            400: function(data) {
              alert("Failed to register user: " + JSON.stringify(data));
            },
            404: function(data) {
              alert("Not found: " + JSON.stringify(data));
            },
            500: function(data) {
              console.log(JSON.stringify(data));
            }
          }
        });
      }
    </script>
</html>