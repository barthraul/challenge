package bold.challenge;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.map.MultiValueMap;

/*
 * Raul Barth
 * This project aims to process a csv file containing departments and rooms as:
 * 		department;date;roomA;roomB;roomC;roomD;roomE;roomF;code
 * INPUT: csf file
 * OUPUT: list of csv files
 */

public class MainProcess extends Thread{
	private static String roomA = "roomA", roomB = "roomB", roomC = "roomC", roomD = "roomD", roomE = "roomE", roomF = "roomF";
	static ParsingThread threadRoomA, threadRoomB, threadRoomC, threadRoomD, threadRoomE, threadRoomF;
	private static List<String> consideredRooms;
	private static List<String> consideredDep;
	private static String originCSV;
	private static String destCSVPath;
	public static Integer numberOfPairs = 0, filesGenerated = 0;
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		// Check inputs and set up 
		checkInputs(args);
        originCSV = args[0];
        destCSVPath = args[1];
        setConsideredRooms(Arrays.asList(args[2].split(",")));
        setConsideredDep(Arrays.asList(args[3].split(",")));  
        BufferedReader br = null;
        String line = "";
       
        // Initialize threads and build department x room pairs
        MultiValueMap pairs = MultiValueMap.decorate(new HashMap<String, String>());
        try {    
        	System.out.println("\n----------------THREADS----------------");
        	InitializeThreads();
            br = new BufferedReader(new FileReader(originCSV));
            while ((line = br.readLine()) != null) {
            	if(checkRoomFilterCondition(roomA))
            		threadRoomA.buildPairs(line,buildFileRoomName(roomA), 2, pairs);
            	if(checkRoomFilterCondition(roomB))
            		threadRoomB.buildPairs(line,buildFileRoomName(roomB), 3, pairs);	
            	if(checkRoomFilterCondition(roomC))
            		threadRoomC.buildPairs(line,buildFileRoomName(roomC), 4, pairs);	
            	if(checkRoomFilterCondition(roomD))
            		threadRoomD.buildPairs(line,buildFileRoomName(roomD), 5, pairs);
            	if(checkRoomFilterCondition(roomE))
            		threadRoomE.buildPairs(line,buildFileRoomName(roomE), 6, pairs);
            	if(checkRoomFilterCondition(roomF))	
            		threadRoomF.buildPairs(line,buildFileRoomName(roomF), 7, pairs);
            }  
            // generate output, the csv files
            buildFiles(pairs, destCSVPath);
        } catch (FileNotFoundException e) {System.out.println("Input File not found!");} 
        catch (IOException e) {e.printStackTrace();} 
        finally {
            if (br != null) {
                try {br.close();} 
                catch (IOException e) {System.out.println("Error when closing the input file...");}
            }
        }     
        long endTime = System.currentTimeMillis();
        printResults(endTime - startTime);        
	}
		
	public static void checkInputs(String[] args){
		if (args.length < 4) {
		      System.err.println("Usage: BoldChallenge <inputFile> <outputFile> <listOfRooms> <listOfDepart>" +
		    		  			 "\n  <listOfRooms> <listOfDepart> should contain \"all\" when you do not want to filter");
		      System.exit(1);
		}
	}
	
	private static void InitializeThreads(){
		threadRoomA = new ParsingThread(roomA); threadRoomA.start();
        threadRoomB = new ParsingThread(roomB); threadRoomB.start();
        threadRoomC = new ParsingThread(roomC); threadRoomC.start();
        threadRoomD = new ParsingThread(roomD); threadRoomD.start();
        threadRoomE = new ParsingThread(roomE); threadRoomE.start();
        threadRoomF = new ParsingThread(roomF); threadRoomF.start();
	}

	@SuppressWarnings("unchecked")
	private static void buildFiles(MultiValueMap pairs, String destCSVPath){
		try {
			Iterator<String> it = pairs.keySet().iterator();
			String key;
			FileWriter fw;
			BufferedWriter bw;
			File file;
			ArrayList<String> values = new ArrayList<String>();
            while(it.hasNext()){
            	key = it.next();   
            	numberOfPairs++;
	            file = new File(destCSVPath+key+".csv");
	    		if (!file.exists())
	    			file.createNewFile();
	    		fw = new FileWriter(file.getAbsoluteFile());
	    		bw = new BufferedWriter(fw);    				    				    			
	    		pairs.getCollection(key).forEach(name -> values.add(name.toString()));
	    		for(int i=0;i<values.size();i++){
	    			bw.append(values.get(i));
	    			bw.append("\n");
	    		}   			   			
	    		bw.close();
	    		filesGenerated++;
	    		values.clear();
            }
		 }
         catch (IOException e) {e.printStackTrace();}
    }
	
	public static void printResults(long duration){
		System.out.println("\n----------------RESULTS----------------");
		System.out.println("DURATION: " +duration+" milliseconds");
        System.out.println("GENERATED FILES: " +filesGenerated+" files");
        System.out.println("NUMBER OF PAIRS (department x room): " +numberOfPairs);
        System.out.println("FILES GENERATED AT: " +destCSVPath);
	}
	
	public static String buildFileRoomName(String roomType){
		return "-"+roomType;
	}
	
	public static boolean checkRoomFilterCondition(String roomType){
		if(getConsideredRooms().contains(roomType) || getConsideredRooms().contains("all"))
			return true;
		else
			return false;
	}

	public static List<String> getConsideredDep() {
		return consideredDep;
	}

	public static void setConsideredDep(List<String> considered) {
		consideredDep = considered;
	}
	
	public static List<String> getConsideredRooms() {
		return consideredRooms;
	}

	public static void setConsideredRooms(List<String> considered) {
		consideredRooms = considered;
	}
}