function iCreateCode( name, email ) {
  var ts = "msaSecretCode" + name + email; // change "mySecretCode" to a unique string for this form only
  return Utilities.base64Encode( Utilities.computeDigest( Utilities.DigestAlgorithm.MD2 , Utilities.base64Encode( ts ) )).replace( /==/, "")
 }

function sendEmail() {
    var msaLogoBlob = UrlFetchApp
  .fetch("http://sandbox.uwmsa.com/wp-content/uploads/2014/05/MSA-Logo.png")
  .getBlob()
  .setName("msaLogoBlob");
  var ramadanLogoBlob = UrlFetchApp
  .fetch("http://uwmsa.com/wp-content/uploads/2014/06/04fcaa66fbbc6d564427a697f4025639.png")
  .getBlob()
  .setName("ramadanLogoBlob");
    // Remember to replace abc@youmail.com with your own email address
    // This will be used to send you a email notification in case of error on form
    // for example if someone entered a wrong email address.

    var myemail = "uwmsa2.0@gmail.com";

    //Leave this field blank - it will be auto populated from "Email" field in 
    // your contact form. Make sure you have a "Email" Field in your contact form.
    var email = "";
    var name = "";
    var row = "";
    var addtocalendar = "<a target=\"_blank\" href=\"https://www.google.com/calendar/event?action=TEMPLATE&tmeid=NGUybHBsMmNsamY1cWRoMG90N29vOGdvNjggaWNjOTlzMjFmaDUyN3I3NWdhaWVibGtkaDBAZw&tmsrc=icc99s21fh527r75gaieblkdh0%40group.calendar.google.com\">Add to Google Calendar</a>";
    // The variable e holds all the form values in an array.<a target="_blank" href="https://www.google.com/calendar/event?action=TEMPLATE&tmeid=NDI2cjNsMjE5ajlmcGFiNDNoMmwzODkwdTggaWNjOTlzMjFmaDUyN3I3NWdhaWVibGtkaDBAZw&tmsrc=icc99s21fh527r75gaieblkdh0%40group.calendar.google.com"></a>
    // Loop through the array and append values to the body.
    var facebooklink = "<a href='https://www.facebook.com/events/802337206478289/'>RSVP on Facebook</a>";

    var columns, mysheet;
        var counter = 0;

    try {
        mysheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1UmnuRCTWX2PANkD36mOBNtEVqNkFGThQ4VS9e9Wpivg/edit#gid=1570942809").getActiveSheet();
        columns = mysheet.getRange(1, 1, mysheet.getLastRow(), mysheet.getLastColumn()).getValues();
        for (i in columns) {
          row = columns[i];
          var room =  "Festival Room, South Campus Hall";
          var emailAddress = row[2];  
          var fullname = row[1]; 
          var randomNum = iCreateCode(fullname, emailAddress);
          var emailStatus = row[4];
          var gender = row[3];
          
          counter++;
          if( counter > 1 && emailStatus == "" && counter <= 350 )
          {
            mysheet.getRange(counter, 6).setValue(randomNum.toString());
            
            MailApp.sendEmail({
                 to: emailAddress,
                 subject: "The Grand Iftar Sign-up Confirmation",
                 htmlBody: "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td align='center'>"
                             + "<img style='width: 80%; max-width: 600px;' src='http://uwmsa.com/wp-content/uploads/2014/07/MSA-Logo1.png' /><br /><br /><b>" 
                             + fullname + "</b>, this is to confirm that you're signed up for the Grand Iftar on <b>July 24th </b> at <b>" + room + "</b>." 
                             + "<br /><br />Please show this email at the door, either on your phone/tablet or printed. We hope to see you there!<br />"
                             + "<br />" + addtocalendar + " | " + facebooklink + "<br />"
                             + "<img src='https://chart.googleapis.com/chart?chs=150x150&cht=qr&chl=" + randomNum + "' /><br />"
                             + "</td></tr></table>",
                 replyTo: myemail,
                 name: "UWaterloo MSA"
               });
            
            mysheet.getRange(counter, 5).setValue("Sent");
            
            var params = {
              "name" : fullname,
              "gender" : gender,
              "email" : emailAddress,
              "qrcode" : "" + randomNum,
              "entered" : false,
              "eventName" : "Grand First Iftar"
            };
            
            parseInsert("Ticket", params);
            
            //break;
          }
          
        }
    } catch (e) {
        if( mysheet )
          mysheet.getRange(counter, 5).setValue(e.message);
    }
}

function getAPIKeys() {
  var keys = {
    "application_id" : "PqeydcZKCSCFJ0xv7hH73Xhj8IADhcLDAG7xy86X",
    "rest_api_key"   : "4ljEpW26B6qtfbT7PaPzZluWmg74Tu6fT9BOoLPQ"
  };

  return keys;
}

function parseInsert(className, params) {
  var url = "https://api.parse.com/1/classes/" + className;
  var payload = Utilities.jsonStringify(params);
  var options = {
    "method"  : "post",
    "payload" : payload,
    "headers" : makeHeaders(),
    "contentType" : "application/json"
  };

  var resp = UrlFetchApp.fetch(url, options);
  var result;
  if (resp.getResponseCode() != 200) {
    Logger.log(resp.getContentText());
    result = false;
  } else {
    result = Utilities.jsonParse(resp.getContentText());
  }

  return result;
}

function makeHeaders() {
  var keys = getAPIKeys();
  var headers = {
    "X-Parse-Application-Id": keys["application_id"],
    "X-Parse-REST-API-Key": keys["rest_api_key"]
  };

  return headers;
}

