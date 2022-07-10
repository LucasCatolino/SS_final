package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import com.sun.tools.javac.util.ArrayUtils;

import core.Algorithm;

public class CalculateObs {
	
	private static final double TOTAL_DISTANCE= 1000;
	private static final int CANT_OF_OBS= 15;
	
	private static void calculateVelocity(String staticFile, String dynamicFile) {
		Locale.setDefault(Locale.US);
	    
		List<String> toFileDistanceP= new ArrayList<String>();
		List<String> toFileDistanceA= new ArrayList<String>();
		
		Map<Integer, Integer> pasivosId = new HashMap<>();
		pasivosId.put(0, 0);
		pasivosId.put(1, 1);
		pasivosId.put(2, 2);
		pasivosId.put(20, 3);
		pasivosId.put(21, 4);
		pasivosId.put(22, 5);
		pasivosId.put(40, 6);
		pasivosId.put(41, 7);
		pasivosId.put(42, 8);
		pasivosId.put(60, 9);
		pasivosId.put(61, 10);
		pasivosId.put(62, 11);
		pasivosId.put(80, 12);
		pasivosId.put(81, 13);
		pasivosId.put(82, 14);
		
		Map<Integer, Integer> agresivosId = new HashMap<>();
		agresivosId.put(3, 0);
		agresivosId.put(4, 1);
		agresivosId.put(5, 2);
		agresivosId.put(23, 3);
		agresivosId.put(24, 4);
		agresivosId.put(25, 5);
		agresivosId.put(43, 6);
		agresivosId.put(44, 7);
		agresivosId.put(45, 8);
		agresivosId.put(63, 9);
		agresivosId.put(64, 10);
		agresivosId.put(65, 11);
		agresivosId.put(83, 12);
		agresivosId.put(84, 13);
		agresivosId.put(85, 14);
		
		//open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        int carCount= Integer.parseInt(staticScanner.next()); //First line N
        double highwayLength= Double.parseDouble(staticScanner.next()); //Second line A
        int lanesCount = Integer.parseInt(staticScanner.next()); //Third line n
        double aggressivePart= Double.parseDouble(staticScanner.next()); //Forth line pa
        staticScanner.close();
		
		//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);

        //Distance obs
        double[] lastPositionP= new double[CANT_OF_OBS];
        double[] timeP= new double[CANT_OF_OBS];
        double[] totalDistanceP= new double[CANT_OF_OBS];
        
        double[] lastPositionA= new double[CANT_OF_OBS];
        double[] timeA= new double[CANT_OF_OBS];
        double[] totalDistanceA= new double[CANT_OF_OBS];
        
        initialize(lastPositionP);
        initialize(timeP);
        initialize(totalDistanceP);
        
        initialize(lastPositionA);
        initialize(timeA);
        initialize(totalDistanceA);
        
        //Initial values
    	dynamicScanner.next(); //skip N token
    	dynamicScanner.next(); //skip time token
    	
    	double timeToken= 0;
    	int idToken= 0;
    	double position= 0;
    	double distance= 0;
    	double displacement= 0;
    	
    	int auxIndex= 0;
    	
    	toFileDistanceP.add("0\t0\n");
    	toFileDistanceA.add("0\t0\n");
    	
    	for (int i = 0; i < carCount; i++) {
    		idToken= Integer.parseInt(dynamicScanner.next()); //id token
    		dynamicScanner.next(); //skip lane
    		position= Double.parseDouble(dynamicScanner.next()); //third token position;
			dynamicScanner.next(); //skip radius
    		dynamicScanner.next(); //skip velocity
    		if (pasivosId.containsKey(idToken)) {
    			auxIndex= pasivosId.get(idToken);
    			lastPositionP[auxIndex]= position;
			}
    		if (agresivosId.containsKey(idToken)) {
    			auxIndex= agresivosId.get(idToken);
    			lastPositionA[auxIndex]= position;
			}
    	}
    	
        while (dynamicScanner.hasNext()) {
        	dynamicScanner.next(); //skip N token
        	timeToken= Double.parseDouble(dynamicScanner.next()); //time token

        	for (int i = 0; i < carCount; i++) {
        		idToken= Integer.parseInt(dynamicScanner.next()); //id token
        		dynamicScanner.next(); //skip lane
        		position= Double.parseDouble(dynamicScanner.next()); //third token position
        		if (pasivosId.containsKey(idToken)) {
        			auxIndex= pasivosId.get(idToken);
        			if (totalDistanceP[auxIndex] < TOTAL_DISTANCE) {
        				distance= Math.abs(position - lastPositionP[auxIndex]);
        				distance= (distance >= 100) ? highwayLength - distance : distance;
        				//update arrays
        				displacement= totalDistanceP[auxIndex] + distance;
        				totalDistanceP[auxIndex]= displacement;
        				lastPositionP[auxIndex]= position;
        				timeP[auxIndex]= timeToken;
        				//fill distances file
        				if (auxIndex == 0) {
        					toFileDistanceP.add("" + timeToken + " \t" + String.format("%.2f",displacement) + "\n");
						}
					}
        		}
        		if (agresivosId.containsKey(idToken)) {
        			auxIndex= agresivosId.get(idToken);
        			if (totalDistanceA[auxIndex] < TOTAL_DISTANCE) {
        				distance= Math.abs(position - lastPositionA[auxIndex]);
        				distance= (distance >= 100) ? highwayLength - distance : distance;
        				//update arrays
        				displacement= totalDistanceA[auxIndex] + distance;;
        				totalDistanceA[auxIndex]= displacement;
        				lastPositionA[auxIndex]= position;
        				timeA[auxIndex]= timeToken;
        				//fill distances file
        				if (auxIndex == 0) {
        					toFileDistanceA.add("" + timeToken + " \t" + String.format("%.2f",displacement) + "\n");
						}
					}
        		}
        		dynamicScanner.next(); //skip radius
				dynamicScanner.next(); //skip velocity
			}
    	}

        dynamicScanner.close();
        
        int carsPerLane= (int) carCount / lanesCount;
            	
    	writeDistanceFile("distanceP", toFileDistanceP, carsPerLane, lanesCount, aggressivePart);
    	writeDistanceFile("distanceA", toFileDistanceA, carsPerLane, lanesCount, aggressivePart);
    	writeToFile("timesP", timeP, carsPerLane, lanesCount, aggressivePart);
    	writeToFile("timesA", timeA, carsPerLane, lanesCount, aggressivePart);
	}
	
	private static void initialize(double[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i]= 0;
		}
	}
	
    private static void writeDistanceFile(String fileName, List<String> toFile, int carCount, int lanesCount, double aggressivePart) {
    	Locale.setDefault(Locale.US);
		try {
			File file = new File("resources/" + fileName + "_" + carCount + "_" + lanesCount + "_" + aggressivePart + "_" + ".txt");
			FileWriter myWriter = new FileWriter("resources/" + fileName + "_" + carCount + "_" + lanesCount + "_" + aggressivePart + ".txt");
			for (Iterator iterator = toFile.iterator(); iterator.hasNext();) {
				String stringToFile= (String) iterator.next();
				try {
					myWriter.write(stringToFile);
				} catch (Exception e) {
					System.err.println("IOException");
				}
			}
			
			myWriter.close();
	    	System.out.println(fileName + " file created");
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
	}

	private static void writeToFile(String fileName, double[] time, int carCount, int lanesCount, double aggressivePart) {
		Locale.setDefault(Locale.US);
		try {
			File file = new File("resources/" + fileName + "_" + carCount + "_" + lanesCount + "_" + aggressivePart + ".txt");
			FileWriter myWriter = new FileWriter("resources/" + fileName + "_" + carCount + "_" + lanesCount + "_" + aggressivePart + ".txt");
			for (int i = 0; i < time.length; i++) {
				try {
					myWriter.write("" + String.format("%.2f",time[i]) + "\n");
				} catch (Exception e) {
					System.err.println("IOException");
				}
			}
			
			myWriter.close();
	    	System.out.println(fileName + " file created");
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
	}

	static public void main(String[] args) throws IOException {
    	System.out.println("Static");
		BufferedReader readerStatic = new BufferedReader(new InputStreamReader(System.in));
		String staticInput = readerStatic.readLine();
    	
    	System.out.println("Dynamic");
		BufferedReader readerDynamic= new BufferedReader(new InputStreamReader(System.in));
		String dynamicInput = readerDynamic.readLine();
				
		String staticFile= (staticInput.length() == 0) ? "static.txt" : staticInput;
		String dynamicFile= (dynamicInput.length() == 0) ? "dynamicEnd.txt" : dynamicInput;
		
		System.out.println("Starting with " + staticFile + ", " + dynamicFile);

		calculateVelocity(staticFile, dynamicFile);
		
		System.out.println("End");
    }
}
