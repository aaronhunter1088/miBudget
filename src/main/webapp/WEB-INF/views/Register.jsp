<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sign Up</title>
  <link href="${pageContext.request.contextPath}/images/wallet.ico" rel="icon" type="image/x-icon">
  <style>
    <!-- NEEDED -->
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
    ::placeholder {
      color: black;
      font-weight: bold;
      text-align: center;
    }
  </style>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
</head>
<body bgcolor="#1E90FF">

<form id="registerForm" action="Register", method="POST">
  <div class="container" style="text-align: center; display: block; justify-content: space-between; width: 15%">
    <h1>Sign Up</h1>

    <input name="validated" type="hidden" value="false">
    <input name="btnSelected" type="hidden" value="Cancel">

    <input name="firstName" placeholder="Enter first name" required="required" tabindex="1" type="text"><br>
    <hr>
    <input name="lastName" placeholder="Enter last name" tabindex="2" type="text"><br>
    <hr>
    <input name="cellphone" maxlength="10" placeholder="Enter cellphone" tabindex="3" type="text"><br>
    <hr>
    <input name="email" placeholder="Enter email" tabindex="4" type="text"> <label id="emailLabel"></br><b><i>This will be used as backup!</i></b> </label>
    <hr>
    <b><input name="password" placeholder="Enter password" tabindex="5" type="password"><br></b>
    <hr>
    <b><input name="passwordRepeat" placeholder="Repeat password" tabindex="6" type="password"><br></b>
    <hr>
    <b><label><input type="checkbox" name="remember" style="margin-bottom:15px" type="checkbox"> Remember me</label></b>
    <p style="text-align: -webkit-center;">
      <b>By creating an account, you agree to our <a href="#" style="color:darkblue" title="TODO: Create a page showing Terms & Conditions!">Terms & Conditions</a>.</b>
    </p>
  </div>
  <div class="container" style="text-align: center; display: block; justify-content: space-between; width: 15%">
    <input type="button" id="cancelBtn" class="button cursor" value="Cancel"/>
    <input type="button" id="registerBtn" class="button cursor" value="Register"/>
  </div>
</form>
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
    cellphone = cellphone.replaceAll('-', '');
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
      type: "POST",
      url: "/miBudget/register",
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
      success: function() {
        console.log("success")
      },
      statusCode: {
        200: function(data) {
          document.open();
          document.write(data);
          document.close();
        },
        400: function() {
          window.location.href = "Register.html"
        },
        404: function() {
          alert('not found');
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