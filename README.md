Created an email server capable of sending and recieving emails via API.

Assignment Instructions:

1. Login -> POST /api/v1/email/login
If login is successful, return the primary key of that user. If unnsuccessful, return a 401 unauthorized.
There should be a few hard coded users. I created a 'utils' package and the put a Users class in there to hold my constants. The other constant that I am holding there is an array of emails.

2. Send email -> POST /api/v1/email/send
The post body should include the senders primary key (they got it from step 1), the username of the recipient, and the message, in JSON.
If the username exists, then this should be saved to to the array of emails. (to, from, message). Remember that both to and from should be UUIDs, neither should be usernames.

3. retrieve email -> POST /api/v1/email/inbox
The post body should include the primary key of the user. Retrieve only that users e-mails (the ones TO that user). Json response body should be an array of objects, and each object should include a 'from' (username, not key), and message.

4. retrieve outbox -> POST /api/v1/email/outbox
The post body should include the primary key of the user. Retrieve only that users sent e-mails (the ones FROM that user). Json response body should be and array of objects, and each object should include a 'to' (username, not key), and message.

Additional Steps:

1. New endpoint to receive external mail.
POST /api/v1/email/receiveExternalMail

BODY:
{"from":"username",
"to":"username1",
"message": "message}

header: 
key: api-key
value: Base 64 encoded "letMeIn"

Return 200 if the "to" user exists and we saved the email successfully to the inbox.
Return 400 if the "to" user does not exist

There should be a feature switch on this guy as well, and you should return a 503 service unavailable if the endpoint is turned off.

2: Modify our send endpoint:
When a user sends an email, if the "to" address does not exists, reject it.

We should modify this, so that if the to address does not exist, we should send that email to our external Email server. If that is successful, great, we are done. (There will be a bug here, that this email will not show up in the outbox). 
If we receive a 400 error from the external server, that means the user doesn't exist either on our server, or externally, we should return the same error we return today for recipient does not exist.

Hint: You will need to use RestTemplate to send external API calls.
The address of the external server must be in configuration.
There should be a feature switch to turn off sending externally. In this case, we would just say the user does not exist. 

https://ti-timeserver.herokuapp.com/api/v1/email/receiveExternalMail - point to this receive endpoint to test your apps ability to send externally.
