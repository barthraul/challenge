package bold.challenge;

import org.apache.commons.collections.map.MultiValueMap;

public class ParsingThread extends Thread {
	private Thread t;
	private String threadName;
	   
	public ParsingThread(String name){
		threadName = name;	       
	    System.out.println("Creating " +  threadName );
	}
	public void run(){
	    System.out.println("Running " +  threadName );
	    System.out.println("Thread " +  threadName + " exiting.");
	}
	public void start (){
	    System.out.println("Starting " +  threadName );
	    if (t == null) {
	    	t = new Thread (this, threadName);
	        t.start ();
	    }
	}	   
	public void buildPairs(String line, String room, Integer roomValue, MultiValueMap pairs){
	    if(checkDeparmentFilterCondition(!line.split(";")[roomValue].isEmpty(), 
	    		MainProcess.getConsideredDep().contains(line.split(";")[0]), MainProcess.getConsideredRooms().contains("all"))){           		
	    	pairs.put(line.split(";")[0]+room, line);        			
	    }
	}
	public boolean checkDeparmentFilterCondition(boolean notEmpty, boolean containsRoom, boolean containsAll){
		if(notEmpty && (containsRoom || containsAll))
			return true;
		else
			return false;
	}
}