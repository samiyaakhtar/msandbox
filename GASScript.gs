function postToParse() {
    var className = "Event";  
    var columns, mysheet;
    var counter = 0;

    try {
        mysheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1uSyOVStMIWfegb5ZdxwX2-aVE5hZWrKkziH5MrB89s0/edit#gid=1862562745").getActiveSheet();
        columns = mysheet.getRange(1, 1, mysheet.getLastRow(), mysheet.getLastColumn()).getValues();
        for (i in columns) {
          row = columns[i];
          var postedStatus = row[9];
          var title = row[1];
          var description = row[2];
          var start = row[3];
          var end = row[4];
          var location = row[5];
          var category = row[6];
          var facebookEvent = row[7];
          
          counter++;
          if( postedStatus != "Posted" && postedStatus != "Failed" && counter > 1 )
          {
            var endDate = new Date(end);
            var startDate = new Date(start);
            var params = { 
                "title" : title, 
                "description" : description, 
                "location" : location, 
                "startTime" : {"__type":"Date","iso":startDate.toJSON()},
                "endTime" : {"__type":"Date","iso":endDate.toJSON()},
                "category" : category, 
                "facebookEvent" : facebookEvent
             };
            parseInsert(className, params);
            
            mysheet.getRange(counter, 10).setValue("Posted");
            //break;
          }
          
        }
    } catch (e) {
        if( mysheet )
          mysheet.getRange(counter, 10).setValue("Failed");
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
