package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import core.Algorithm;

public class CalculateObs {
	
	private static final double TOTAL_DISTANCE= 1000;
	private static final int CANT_OF_OBS= 10;
	
	private static void calculateVelocity(String staticFile, String dynamicFile, String NLanes, String NPart, String aggressivePart) {
	    
		//open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        int carCount= Integer.parseInt(staticScanner.next()); //First line N
        double highwayLength= Double.parseDouble(staticScanner.next()); //Secon line A
        double lanesCount = Integer.parseInt(staticScanner.next()); //Third line n
        staticScanner.close();
		
		//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);

        //Distance obs
        double[] lastPosition= new double[CANT_OF_OBS];
        double[] time= new double[CANT_OF_OBS];
        double[] totalDistance= new double[CANT_OF_OBS];
        
        initialize(lastPosition);
        initialize(time);
        initialize(totalDistance);
        
        //Initial values
    	dynamicScanner.next(); //skip N token
    	dynamicScanner.next(); //skip time token
    	
    	for (int i = 0; i < lastPosition.length; i++) {
    		dynamicScanner.next(); //skip id
    		dynamicScanner.next(); //skip lane
    		lastPosition[i]= Double.parseDouble(dynamicScanner.next()); //third token position
    		dynamicScanner.next(); //skip radius
    		dynamicScanner.next(); //skip velocity
		}
    	
    	for (int i = CANT_OF_OBS; i < carCount; i++) {
    		dynamicScanner.next(); //skip id
    		dynamicScanner.next(); //skip lane
    		dynamicScanner.next(); //skip position
    		dynamicScanner.next(); //skip radius
    		dynamicScanner.next(); //skip velocity
		}
        
    	double timeToken= 0;
    	int idToken= 0;
    	double position= 0;
    	double distance= 0;
        while (dynamicScanner.hasNext()) {
        	dynamicScanner.next(); //skip N token
        	timeToken= Double.parseDouble(dynamicScanner.next()); //time token

        	for (int i = 0; i < carCount; i++) {
        		idToken= Integer.parseInt(dynamicScanner.next()); //id token
        		if (idToken < CANT_OF_OBS) {
        			if (totalDistance[idToken] < TOTAL_DISTANCE) {
        				dynamicScanner.next(); //skip lane
        				position= Double.parseDouble(dynamicScanner.next()); //third token position
        				dynamicScanner.next(); //skip radius
        				dynamicScanner.next(); //skip velocity
        				distance= Math.abs(position - lastPosition[idToken]);
        				distance= (distance >= 100) ? highwayLength - distance : distance;
        				
        				//update arrays
        				totalDistance[idToken]= totalDistance[idToken] + distance;
        				lastPosition[idToken]= position;
        				time[idToken]= timeToken;
        			} else {
        				dynamicScanner.next(); //skip lane
        				dynamicScanner.next(); //skip position
        				dynamicScanner.next(); //skip radius
        				dynamicScanner.next(); //skip velocity
        			}
        		} else {
        			dynamicScanner.next(); //skip lane
    				dynamicScanner.next(); //skip position
    				dynamicScanner.next(); //skip radius
    				dynamicScanner.next(); //skip velocity
        		}
			}
    	}

        dynamicScanner.close();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
    	LocalTime localTime = LocalTime.now();
    	String timestamp= dtf.format(localTime);
    	
    	writeToFile("distance", time, carCount, NLanes, aggressivePart, timestamp);
	}
	
    private static void initialize(double[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i]= 0;
		}
	}

	private static void writeToFile(String fileName, double[] time, int NPart, String NLanes, String aggressivePart, String stamp) {
		Locale.setDefault(Locale.US);
		try {
			File file = new File("resources/" + fileName + "_" + NLanes + "_" + NPart + "_" + aggressivePart + "_" + stamp + ".txt");
			FileWriter myWriter = new FileWriter("resources/" + fileName + "_" + NLanes + "_" + NPart + "_" + aggressivePart + "_" + stamp + ".txt");
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
		
		System.out.println("n lanes");
		BufferedReader readerN= new BufferedReader(new InputStreamReader(System.in));
		String NLanes = readerN.readLine();
		
		System.out.println("N particles");
		BufferedReader readerNPart= new BufferedReader(new InputStreamReader(System.in));
		String NPart = readerNPart.readLine();
		
		System.out.println("Percentage of agressive driverss");
		BufferedReader readerAggressive= new BufferedReader(new InputStreamReader(System.in));
		String aggressivePart = readerAggressive.readLine();
		
		String staticFile= (staticInput.length() == 0) ? "static.txt" : staticInput;
		String dynamicFile= (dynamicInput.length() == 0) ? "dynamicEnd.txt" : dynamicInput;
		
		System.out.println("Starting with " + staticFile + ", " + dynamicFile + ", lanes= " + NLanes
				+ ", particles= " + NPart + ", agressive= " + aggressivePart);

		calculateVelocity(staticFile, dynamicFile, NLanes, NPart, aggressivePart);
		
		System.out.println("End");
    }
}
