


// curl --user admin:admin --header "Accept: text/csv" -d "select from general" "http://localhost:2480/command/general/sql"

def fromJson   = { String strJson -> new groovy.json.JsonSlurper().parseText(strJson); } ;
def prettyJson = { String aStrJson -> groovy.json.JsonOutput.prettyPrint(aStrJson);    } ;
def toJson     = { Object obj, pretty = false -> String sJson = groovy.json.JsonOutput.toJson(obj); (!pretty) ? sJson : prettyJson(sJson); } ;

// ### configurable items beg ###

String username = "root"
String password = "admin"

String host     = "localhost" 
String port     = "2480"      

String strSql = " select from general ".trim() ;

// ### configurable items end ###

String sUrl = "http://${host}:${port}/command/general/sql";

// String Data = "data=Hello+World!";


URL url = new URL( sUrl ) ;
HttpURLConnection con = (HttpURLConnection) url.openConnection();

String userpass  = username + ":" + password;
String basicAuth = "Basic " + new String( java.util.Base64.getEncoder().encode( userpass.getBytes() ) );

con.setRequestProperty ("Authorization", basicAuth);

// ### make resp csv with header line
// Accept: text/csv

// con.setRequestProperty("Accept", "text/csv");
// con.setRequestProperty("Accept", "application/json");

/*
name,surname,mi
"Jay","Miner",
"Ann","Ringdahl","Z"
"John","Doe",
*/

con.setRequestMethod("POST");
con.setDoOutput(true);

// ### out
def outStream = con.getOutputStream();

outStream.write( strSql.getBytes("UTF-8") );

// ### in
def inStream = con.getInputStream();

String sResp = inStream.text;

// ### cleanup 
inStream.close();
outStream.close();
con.disconnect();

println prettyJson( sResp ) ;

// println sResp ;