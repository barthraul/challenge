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
	private static Integer quantity;
	private static List<String> consideredRooms;
	private static List<String> consideredDep;
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		//checkInputs(args);
        //String originCSV = args[0];
        //String[] consideredDep = args[1].split(",");
        //consideredRooms = Arrays.asList(args[2].split(","));
		String originCSV = "D:/Raul/Bold/Desafio/dataset-400000.csv";
		String destCSVPath = "D:/Raul/Bold/Desafio/";
        setConsideredRooms(Arrays.asList("all".split(",")));
        setConsideredDep(Arrays.asList("HR1,HR5".split(",")));       
        BufferedReader br = null;
        String line = "";

        MultiValueMap pairs = MultiValueMap.decorate(new HashMap<String, String>());
        try {                              
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
	
	public static void printResults(long duration){
		System.out.println("\nDuration: " +duration+" milliseconds");
        System.out.println("Files Generated: " +getQuantityGeneratedFiles()+" files");
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
			Integer filesGenerated = 0;
			ArrayList<String> values = new ArrayList<String>();
            while(it.hasNext()){
            	key = it.next();                	
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
            setQuantityGeneratedFiles(filesGenerated);
		 }
         catch (IOException e) {e.printStackTrace();}
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
	
	public static void setQuantityGeneratedFiles(Integer qty){
		quantity = qty;
	}
	
	public static Integer getQuantityGeneratedFiles(){
		return quantity;
	}
}