<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>
<h1>Profile for ${Firstname} ${Lastname}</h1>
<button id="link-button">Link Account</button>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
<script src="https://cdn.plaid.com/link/v2/stable/link-initialize.js"></script>
<script type="text/javascript">
(function($) {
  var handler = Plaid.create({
    clientName: 'Plaid Walkthrough Demo',
    env: 'sandbox',
    // Replace with your public_key from the Dashboard
    key: 'f0503c4bc8e63cc6c37f07dbe0ae2b',
    product: ["transactions"],
    // Optional, use webhooks to get transaction and error updates
    webhook: 'https://requestb.in',
    onLoad: function() {
      // Optional, called when Link loads
    },
    onSuccess: function(public_token, metadata) {
      System.out.println("public_token: " + public_token);
      System.out.println("metadata obj: " + metadata);
      
      // Send the public_token to your app server.
      // The metadata object contains info about the institution the
      // user selected and the account ID or IDs, if the Select Account
      // view is enabled.
      $.post('/authenticate', {
        public_token: public_token,
      });
    },
    onExit: function(err, metadata) {
      // The user exited the Link flow.
      if (err != null) {
        // The user encountered a Plaid API error prior to exiting.
      }
      // metadata contains information about the institution
      // that the user selected and the most recent API request IDs.
      // Storing this information can be helpful for support.
    },
    onEvent: function(eventName, metadata) {
      // Optionally capture Link flow events, streamed through
      // this callback as your users connect an Item to Plaid.
      // For example:
      // eventName = "TRANSITION_VIEW"
      // metadata  = {
      //   link_session_id: "123-abc",
      //   mfa_type:        "questions",
      //   timestamp:       "2017-09-14T14:42:19.350Z",
      //   view_name:       "MFA",
      // }
    }
  });

  $('#link-button').on('click', function(e) {
    handler.open();
  });
})(jQuery);
</script>

<form action="Welcome" method="get">
<p> <!-- Add a space between the buttons -->
<button type="submit">Welcome Page</button>
<hr>
<p>Items for '${Firstname}' '${Lastname}' </p>
<p>
<p>Accounts : '${Accounts}' </p>
</body>
</html>

