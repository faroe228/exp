
// todo 
// this works, but there has got to be a simpler way
// to update config xml to support cors
// so we can directly have rest access from a browsesr

// This works (verified output with BeyondCompare tool)

import javax.xml.namespace.QName;
import groovy.xml.XmlUtil;
import groovy.util.XmlNodePrinter;

public class ConfigCors implements Runnable {
    
    File distDir; // caller sets this

    public void run() {
        // caller creates and runs e.g. someClassVar.newInstance( 'distDir': someValue ).run();
        
        def gi = groovy.inspect.swingui.ObjectBrowser.&inspect; // for debug
        
        assert distDir != null ;
        
        // todone remove hardcoded path
        // def origFile = new File('build/orientdb-community-2.0.11/config/orientdb-server-config.xml');
        def origFile = new File( distDir, 'config/orientdb-server-config.xml' );
        assert origFile.exists();
        
        String sXml  =  origFile.text;
        
        def xmlParser = new XmlParser();
        
        def root = xmlParser.parseText( sXml );
        
        def listenersNode = root.network.listeners.first()
        def listenerNode  = listenersNode.children().find {  it.'@protocol' == 'http'  };
        
        def pmsNode = listenerNode.parameters[0] ; // parameters node
        
        // 
        def somethingToDo = pmsNode.children().find { 
            it.'@name'  == 'network.http.additionalResponseHeaders' &&
            it.'@value'.contains('Access-Control-Allow-Origin:*') && 
            it.'@value'.contains('Access-Control-Allow-Credentials: true')
        } == null ;
        
        // println XmlUtil.serialize( found ) ;
        // println found.attribute('value');
        // Access-Control-Allow-Origin:* ;Access-Control-Allow-Credentials: true
        // println XmlUtil.serialize( pmsNode ); 
        
        if ( somethingToDo ) {
           
        
            xmlParser.createNode( pmsNode, new QName('parameter'), [
                'name'  : 'network.http.additionalResponseHeaders',
                'value' : 'Access-Control-Allow-Origin:* ;Access-Control-Allow-Credentials: true'
                ] 
            );
            
            // String sNewXml = XmlUtil.serialize( root ); 
            // ### above does not work
            // ### prolog is not correct - missing standalone="yes" and missing line feed
            
            // ### this block was for debug
            // def outFile = new File("build2/orientdb-community-2.0.11/config/orientdb-server-config.xml");
            // outFile.parentFile.mkdirs();
            // outFile.text = sNewXml ;
            
            def outFile = new File( origFile.path ); // overwrite orig file
            
            def sw = new StringWriter();
            def pw = new PrintWriter( sw );
            
            String sProlog =  '''    
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>    
            '''.trim();
            
            pw.println( sProlog );
            
            
            String space = new String( 32 as byte );
            def xmlNodePrinter = new XmlNodePrinter( pw, space*4 );
            xmlNodePrinter.setPreserveWhitespace(true);
            
            xmlNodePrinter.print(root);
            
            println "### adding cors support to: ${ outFile }"
            
            outFile.text = sw.toString();
        }
    
    }
    
    /*
    public static void main(String[] args) {
        println( "### ext: ${ext}" );
        new ConfigCors().run();
    }
    */
}

