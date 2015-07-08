class echoArgs {    
    /*
    gradlew runScript "-PscriptPath=lpt/echoArgs.groovy" -PP0=how  -PP1=now "-PP2=brown cow"
    # arg0 would be: how
    # arg1 would be: now
    # arg2 would be: brown cow 
    ...
    */
    
	public static void main(String[] args) {
	    args.eachWithIndex { String item, int idx -> 
	        println "### $idx    $item"
	    }
	}
	
}