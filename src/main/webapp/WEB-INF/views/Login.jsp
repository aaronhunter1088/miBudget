<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link href="${pageContext.request.contextPath}/images/wallet.ico" rel="icon" type="image/x-icon">
  <style>
    <!-- NEEDED -->
    body {
      display: block;
      margin: 8px;
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
<body bgcolor="#32CD32">
<form id="loginForm" action="/Login" method="POST">
  <div class="container" style="text-align: center; display: block; justify-content: space-between; width: 15%">
    <h1>Login</h1>
    <input name="validated" type="hidden" value="false"/>
    <input name="btnSelected" type="hidden" value=""/>
    <input name="cellphone" placeholder="Enter cellphone" required tabindex="1" type="text" value=""/>
    <br>
    <hr>
    <input name="password" placeholder="Enter password" required tabindex="2" type="password" value=""/>
    <br>
    <hr>
    <label><input checked="checked" name="remember-me" type="checkbox"/> Remember me</label>
    <br>
    <br>
  </div>
  <div class="container" style="text-align: center; display: block; justify-content: space-between; width: 15%">
    <input type="button" id="cancelBtn" class="button cursor" value="Cancel"/>
    <!--				<input type="submit" id="loginBtn" class="button cursor" value="Submit" onclick="return validateFields();"/>-->
    <button type="submit" id="loginBtn" class="button cursor" onclick="return validateFields();">Submit</button>
    <hr>
    <input type="button" id="forgotPasswordBtn" class="button cursor" value="Forgot Password"/>
  </div>
</form>
<script type="text/javascript">
  $(function() {
    $('.button').removeAttr('disabled')

    $('#cancelBtn').on('click', function() {
      window.location.href = "${pageContext.request.contextPath}/"
      console.log('clicked Cancel button. Returned to Homepage')
    });
    // $('#loginBtn').on('click', function() {
    // 	let loginForm = $("#loginForm")
    // 	// get cellphone
    // 	let cellphone = loginForm
    // 			.find('input[name=cellphone]')
    // 			.val();
    // 	// get password
    // 	let password = loginForm
    // 			.find('input[name=password]')
    // 			.val();
    // 	//console.log("cellphone: " + cellphone);
    // 	//console.log("password: " + password);
    // 	if (validateFields(cellphone, password)) {
    // 		//login(cellphone, password, true);
    // 		return true;
    // 	} else return false;
    // });
    $('#forgotPasswordBtn').on('click', function() {
      let loginForm = $("#loginForm")
      let cellphone = loginForm
              .find('input[name=cellphone]')
              .val();
      if (cellphone === "") {
        alert("Enter your cellphone and click Forgot Password again.");
        return false;
      }
      $.ajax({
        type: "POST",
        url: "/miBudget/password/forgot",
        async: true,
        data: {
          cellphone: cellphone,
          btnSelected: 'Forgot Password'
        },
        success: function() {
          console.log("success")
        },
        statusCode: {
          200: function(data) {
            window.location.reload();
            alert("An email has been sent with your existing password.")
          },
          400: function() {
            window.location.reload();
            alert("No user was found based on the given cellphone: " + cellphone)
          },
          404: function(data) {
            console.log("Failed with a 404");
            console.log(data);
          },
          500: function(data) {
            console.log("Failed with a 500");
            console.log(data);
          }
        }
      });
    });
  });
  function validateFields() {
    let loginForm = $("#loginForm")
    // get cellphone
    let cellphone = loginForm
            .find('input[name=cellphone]')
            .val();
    // get password
    let password = loginForm
            .find('input[name=password]')
            .val();
    if (validateLoginFields(cellphone, password)) {
      loginForm.find('input[name=Validated]').val("true");
      loginForm.find('input[name=btnSelected]').val(loginForm.find('input[name=loginBtn]').text());
      return true;
    } else return false;
  }
  function validateLoginFields(cellphone, password) {
    if (cellphone === "") {
      alert("Cellphone cannot be blank.");
      return false;
    }
    cellphone = cellphone.replaceAll('-', '');
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
    $.ajax({
      type: "POST",
      url: "/miBudget/login/",
      async: true,
      data: {
        cellphone: cellphone,
        password: password,
        btnSelected: 'Login',
        validated: validated
      },
      success: function() {
        console.log("Login success")
      },
      statusCode: {
        200: function() {
          $.ajax({
            type: "GET",
            url: "/miBudget/homepage/",
            async: true,
            success: function() {
              console.log("Homepage success")
            }
          });
        },
        400: function() {
          window.location.href = "Register.html"
        },
        404: function(data) {
          console.log(data);
        },
        500: function(data) {
          console.log(data);
        }
      }
    });
  }
</script>
</body>
</html>