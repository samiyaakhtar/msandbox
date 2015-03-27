function postToParse() {
    var className = "Vote";
    var columns, mysheet;
    var counter = 0;
    var spreadsheetUrl = "https://docs.google.com/spreadsheets/d/1uSyOVStMIWfegb5ZdxwX2-aVE5hZWrKkziH5MrB89s0/edit#gid=1862562745";

    try {
        mysheet = SpreadsheetApp.openByUrl(spreadsheetUrl).getActiveSheet();
        columns = mysheet.getRange(1, 1, mysheet.getLastRow(), mysheet.getLastColumn()).getValues();

        var electionQueryParams = {
          "name": "Spring 2015"
        };
        var electionPtr = parseQuery("Election", electionQueryParams)[0];

        for (i in columns) {
          row = columns[i];
          var persons = ["person1", "person2"]; //TODO: get the names from spreadsheet
          counter++;
          if( (postedStatus == "" || postedStatus == undefined) && counter > 1 )
          {
            for (person in persons) {
              var params = {
                "election" : electionPtr,
                "candidate" : person,
                "position" : "Spring 2015 exec"
              };
              parseInsert(className, params);
            }

            mysheet.getRange(counter, 10).setValue("Posted");
            //break;
          }

        }
    } catch (e) {
        if( mysheet )
          mysheet.getRange(counter, 10).setValue("Failed");
    }
}

function parseQuery(className, params) {
  var encoded_params = encodeURIComponent(Utilities.jsonStringify(params));
  var url = "https://api.parse.com/1/classes/" + className + "?where=" + encoded_params;
  var options = {
    "method"  : "get",
    "headers" : makeHeaders(),
  };

  var resp = UrlFetchApp.fetch(url, options);
  var result;
  if (resp.getResponseCode() != 200) {
    result = false;
  } else {
    result = Utilities.jsonParse(resp.getContentText())["results"];
  }

  return result;
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
