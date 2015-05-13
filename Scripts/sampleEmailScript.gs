/*
Usage: Copy and paste script below into Script Editor under tools. Create a script for Spreadsheet and run 
this script AFTER modifying according to the spreadsheet, on form submit. To add trigger, click on Triggers button
and set them up. Remember to setup trigger from the account you want to send emails from. 
*/

function sendEmail(event) {
 
    // Remember to replace below email with your own email address

    var myemail = "uwmsa2.0@gmail.com";

    var email = "";
    var name = "";
    var row = "";
    // Update the link of this addtocalendar and facebook link for each event
    var addtocalendar = "<a target=\"_blank\" href=\"https://www.google.com/calendar/event?action=TEMPLATE&tmeid=Z2Y0NDM5b2duMXZra2NhZXAxY3FxOWlvOTggaWNjOTlzMjFmaDUyN3I3NWdhaWVibGtkaDBAZw&tmsrc=icc99s21fh527r75gaieblkdh0%40group.calendar.google.com\">Add to Google Calendar</a>";
    // The variable e holds all the form values in an array.<a target="_blank" href="https://www.google.com/calendar/event?action=TEMPLATE&tmeid=NDI2cjNsMjE5ajlmcGFiNDNoMmwzODkwdTggaWNjOTlzMjFmaDUyN3I3NWdhaWVibGtkaDBAZw&tmsrc=icc99s21fh527r75gaieblkdh0%40group.calendar.google.com"></a>
    // Loop through the array and append values to the body.
    var facebooklink = "<a href='https://www.facebook.com/events/716985641729070/'>RSVP on Facebook</a>";

    var columns, mysheet;
        var counter = 0;

    try {
        mysheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1TWuIywMbT6jHn5yWIGJXUzGfKAQ0XpQ_pEfz8YXjkaE/edit#gid=1020810492").getActiveSheet();
        columns = mysheet.getRange(1, 1, mysheet.getLastRow(), mysheet.getLastColumn()).getValues();
        for (i in columns) {
          row = columns[i];
          var room =  "St. Pauls Alumni Hall";
          //Figure out column number of email Address, fullname/firstname and email status
          var emailAddress = row[3];  
          var fullname = row[1]; 
          var randomNum = Math.floor(Math.random() * 999999999999) + 100000000000;
          var emailstatus = row[9];
          
          counter++;
          if( emailAddress != "Email Address" && emailAddress != "" && emailstatus != "Sent" && emailstatus != "Failed" )
          {
            
            MailApp.sendEmail({
                 to: emailAddress,
                 subject: "First Years Welcome Dinner Sign-up Confirmation",
                 htmlBody: "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><td align='center'>"
                             + "<img style='width: 50%; max-width: 400px;' src='http://sandbox.uwmsa.com/wp-content/uploads/2014/05/MSA-Logo.png' /><br /><br /><b>" 
                             + fullname + "</b>, this is to confirm that you're signed up for the First Years Dinner on <b>September 30th </b> at <b>" + room + "</b>." 
              + "  Doors open at 6:45 PM and we will be collecting ticket payments at the door. We hope to see you there!<br />"
                             + "<br />" + addtocalendar + " | " + facebooklink + "<br />"
                             + "</td></tr></table>",
                 replyTo: myemail,
                 name: "UWaterloo MSA",
                 //inlineImages:
                   //{
                     //header: msaLogoBlob,
                   //}
               });
            
            mysheet.getRange(counter, 10).setValue("Sent");
            break;
          }
          
        }
    } catch (e) {
        if( mysheet )
          mysheet.getRange(counter, 10).setValue("Failed");
    }
}
