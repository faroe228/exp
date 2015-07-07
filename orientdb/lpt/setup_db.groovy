// import java.util.Random;
// gRand = new Random();

/*
idea for dojo store use

orientdb rid like #12:7 will not work with dojo data stores

convert orid from #12:7 to 2127

The first digit would be the cluster num string width.

var oridToInt = function( aOrId ) {
    var srid = aOrId.toString();
    var arr  = srid.split(':');
    
    var sClusterNum = arr[0].replace('#','');
    var sRecId      = arr[1];
    
    var sResult  = sClusterNum.length.toString() + sClusterNum + sRecId;
    return parseInt(sResult);    
} ;



*/

// curl --user admin:admin --header "Accept: text/csv" -d "select from general" "http://localhost:2480/command/general/sql"

// curl --user admin:admin --header "Accept: text/csv" -d "INSERT INTO general CONTENT {'name': 'John', 'surname': 'Doe'}" "http://localhost:2480/command/general/sql"

def fromJson   = { String strJson -> new groovy.json.JsonSlurper().parseText(strJson); } ;
def prettyJson = { String aStrJson -> groovy.json.JsonOutput.prettyPrint(aStrJson);    } ;
def toJson     = { Object obj, pretty = false -> String sJson = groovy.json.JsonOutput.toJson(obj); (!pretty) ? sJson : prettyJson(sJson); } ;

def nw = new Date().time; // now unix time

// ### configurable items beg ###

String username = "root"
String password = "admin"

String host     = "localhost" 
String port     = "2480"     

// allow override of host e.g. groovy "-Dhost=192.168.1.199" setup_db.groovy
String sp_host = System.properties.'host';
if ( sp_host != null ) {
        host = new String( sp_host ).trim();
}

String sUrlForSql = "http://${host}:${port}/command/general/sql".trim();

def sendPost( String aStrUrl, String aStrData, Map headersMap = [:], String aStrUid = '', String aStrPwd = '') {
    def noAuth  = (aStrUid == '') && (aStrPwd == '') ;
    
    URL url = new URL( aStrUrl ) ;
    // ### open
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    
    if ( !noAuth ) {
        String userpass  = aStrUid + ":" + aStrPwd;
        // oops - java.util.Base64 requires Java 8
        // String basicAuth = "Basic " + new String( java.util.Base64.getEncoder().encode( userpass.getBytes() ) );   
        String    basicAuth = "Basic " + userpass.bytes.encodeBase64().toString();  
        con.setRequestProperty ("Authorization", basicAuth);    
    }
    
    if ( !headersMap.isEmpty() ) {
        headersMap.each { item ->
            con.setRequestProperty( "${item.key}".toString(), "${item.value}".toString() );
        }
    }
    
    con.setRequestMethod("POST");
    con.setDoOutput(true); 
    
    // ### out
    def outStream = con.getOutputStream();
    
    if (aStrData != null) {
        outStream.write( aStrData.getBytes("UTF-8") );
    }
    
    // ### in
    def inStream = con.getInputStream();
    
    String sResp = inStream.text;
    
    inStream.close();
    outStream.close();
    
    con.disconnect();
    
    // println prettyJson( sResp ) ;
    return sResp;
} 

// #################################
def createDb = { String aDbName = 'general' 
    // curl --user root:admin -X POST "http://%hostIp%:2480/database/general/plocal/graph"
    
    String sUrl = "http://${host}:${port}/database/${aDbName}/plocal/graph";
    
    String sResp = sendPost( sUrl, null, [:], 'root', 'admin' );
    println sResp;
} ;

def swallow(Closure aClosure = {}) {
    try {
        aClosure();
    } catch(Exception ex) {
        // pass
        System.err.println( ex.getClass().name );
    }
}

///////////////////////////////////////////

// ### create db
swallow {
    createDb();
}

// curl --user admin:admin -d "create class general extends V" "http://%hostIp%:2480/command/general/sql"


// ### create class
swallow {
    println sendPost( sUrlForSql, 
        """ 
        create class general extends V
        """.trim(),
        ['Accept':'text/csv'], 'root', 'admin' );
}


// ### create some data

// curl --user admin:admin --header "Accept: text/csv" -d "INSERT INTO general CONTENT {'name': 'John %rnd%', 'surname': 'Doe %rnd%', 'age':%rnd% }" "http://%hostIp%:2480/command/general/sql"


def createRandomWord = { int theLen = 3 + (Math.random()*6).toInteger() ->
    
    
    def az = ('a' .. 'z').collect {it} ;
    def vs = 'aeiou'.collect { it } ;
    def cs = ('a' .. 'z').findAll { !(it in vs) } ;   
    
    def shuffle = {
        Collections.shuffle(az);
        Collections.shuffle(vs);
        Collections.shuffle(cs);
    } ;
    
    shuffle();
    
    def lst = [];
    // first time
    lst << az[0].toUpperCase();
    
    while( lst.size() < theLen ) {
        def lastWasVowel = lst.last().toLowerCase() in vs ;
        def nextLetter = lastWasVowel ? cs[0] : vs[0] ;
        
        String currentWord = lst.join('').toString().toLowerCase();
        
        // handle q and after qu
        if ( currentWord.endsWith('q') ) {
            nextLetter = 'u';
        } else if ( currentWord.endsWith('qu') ) {
            nextLetter = vs[0]; // add vowel
        }
        
        lst << nextLetter ;
        shuffle();
    }
    
    
    
    
    return ( lst.join('') ).toString();
} ;

int numRecsToInsert = 10000;
def t1 = new Date().time;
numRecsToInsert.times { int idx ->

    swallow {
        if ( idx.toString().endsWith('000') ) {
            println idx;
        }
        
        def num = (18 + Math.random()*100).toInteger();
        
        // println
        sendPost( sUrlForSql, 
        """
        
        INSERT INTO general CONTENT { 'first_name': '${createRandomWord(3)}', 'last_name': '${createRandomWord(6)}', 'age': ${num}, 'created': ${new Date().time} }
        
        """.trim(),
        ['Accept':'application/json'], 'admin', 'admin' );    
    }

}
def t2 = new Date().time;
def delta = (t2 - t1) / 1000.0;
println "### delta: ${delta}    RecsPerSec: ${ (numRecsToInsert/delta).toInteger() }"


