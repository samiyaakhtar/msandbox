function postToParse() {
    var className = "Vote";
    var columns, mysheet;
    var counter = 0;
    var spreadsheetUrl = "https://docs.google.com/spreadsheets/d/1pqa8cPmQfKVHYI4yn6iwopUXe446mWteantqFw9fBes/edit#gid=958855036";

    try {
        mysheet = SpreadsheetApp.openByUrl(spreadsheetUrl).getActiveSheet();
        columns = mysheet.getRange(1, 1, mysheet.getLastRow(), mysheet.getLastColumn()).getValues();

        var electionQueryParams = {
          "name": "Spring 2015"
        };
        var resultFromElectionQuery = parseQuery("Election", electionQueryParams);
        var electionPtr = resultFromElectionQuery[0];
        var electionJson = {
                              "__type": "Pointer",
                              "className": "Election",
                              "objectId": electionPtr.objectId
        };

        for (i in columns) {
          row = columns[i];
          var persons = [row[1], row[2], row[3]]; 
          var postedStatus = row[8];
          counter++;
          if( (postedStatus == "" || postedStatus == undefined) && counter > 1 )
          {
            for (k in persons) {
              var params = {
                "election" : electionJson,
                "candidate" : persons[k],
                "position" : "Spring 2015 exec"
              };
              parseInsert(className, params);
            }

            mysheet.getRange(counter, 9).setValue("Posted");
            //break;
          }

        }
    } catch (e) {
        if( mysheet )
          mysheet.getRange(counter, 9).setValue(e.message);
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