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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import core.Algorithm;
import models.Vector;

public class CalculateObs {
	
	private static final int TIME= 1;
	private static final double PERCENTAGE= 0.75;
	
	private static void calculateVelocity(String staticFile, String dynamicFile, Double zombieV) {
	    
		List<String> toFileDistance= new ArrayList<String>();
		List<String> toFileSpreadingIllness= new ArrayList<String>();
		
		//open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        int totalParticles= Integer.parseInt(staticScanner.next()) + 1; //+1 because static contains N for humans
        double spaceRadio= Double.parseDouble(staticScanner.next());
        staticScanner.close();
		
		//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);

        //Velocity obs
        double lastTime= 0;
        int zombiesLastCount= 0;
        double percentageOfZombies= 0;
        
        //Distance obs
        double originalZombieXPrev= 0;
        double originalZombieYPrev= 0;
        double originalZombieXAct= 0;
        double originalZombieYAct= 0;
        Vector prevPosition= new Vector(0, 0);
        Vector actPosition= new Vector(0, 0);
        double z= 0;
        
    	dynamicScanner.next(); //skip N token
    	dynamicScanner.next(); //skip time token
        originalZombieXPrev= Double.parseDouble(dynamicScanner.next()); //X token
    	originalZombieYPrev= Double.parseDouble(dynamicScanner.next()); //Y token
    	dynamicScanner.next(); //skip R token
		dynamicScanner.next(); //Zombie token
		dynamicScanner.next(); //skip Person token
		
		for (int i = 1; i < totalParticles; i++) {
        	//X Y R Zombie Person
    		dynamicScanner.next(); //skip X token
    		dynamicScanner.next(); //skip Y token
    		dynamicScanner.next(); //skip R token
    		int zombie= Integer.parseInt(dynamicScanner.next()); //Zombie token
    		dynamicScanner.next(); //skip Person token
    		if (zombie > 0) {
    			zombiesLastCount ++;
			}
		}
        
        while (dynamicScanner.hasNext()) {
        	zombiesLastCount= 1;
        	dynamicScanner.next(); //skip N token
        	//Time
        	lastTime= Double.parseDouble(dynamicScanner.next());
        	
        	originalZombieXAct= Double.parseDouble(dynamicScanner.next());
    		originalZombieYAct= Double.parseDouble(dynamicScanner.next());
    		
        	if (lastTime % TIME == 0) {        			
        		//Update vectors
        		prevPosition.setX(originalZombieXPrev);
        		prevPosition.setY(originalZombieYPrev);
        		actPosition.setX(originalZombieXAct);
        		actPosition.setY(originalZombieYAct);
        		
        		//Update distance
        		z+= actPosition.getDistanceTo(prevPosition);
        		
        		//Add data to print in file
        		toFileDistance.add("" + (totalParticles - 1) + "\t" + zombieV + "\t" + String.format("%.2f",lastTime) + "\t" + String.format("%.2f",z) + "\n");
        		
        		//Next time act will be prev
            	originalZombieXPrev= originalZombieXAct;
            	originalZombieYPrev= originalZombieYAct;
        	}
        	
        	dynamicScanner.next(); //skip R token
    		dynamicScanner.next(); //Zombie token
    		dynamicScanner.next(); //skip Person token
    		
        	for (int i = 1; i < totalParticles; i++) {
            	//X Y R Zombie Person
        		dynamicScanner.next(); //skip X token
        		dynamicScanner.next(); //skip Y token
        		dynamicScanner.next(); //skip R token
        		int zombie= Integer.parseInt(dynamicScanner.next()); //Zombie token
        		dynamicScanner.next(); //skip Person token
        		if (zombie == 1) {
        			zombiesLastCount ++;
				}
			}
        	
        	if (lastTime % TIME == 0) {
        		percentageOfZombies= (double)zombiesLastCount / (double)totalParticles;
    			toFileSpreadingIllness.add("" + (totalParticles - 1) + "\t" + zombieV + "\t" + String.format("%.2f", lastTime) + "\t" + String.format("%.2f", percentageOfZombies) + "\n");
        	}
		}
        dynamicScanner.close();
        
        //TODO: este observable se va
        /*
        lastTime= (lastTime == 0) ? 1 : lastTime;
        double velocity= zombiesLastCount / lastTime;
        
        String toPrint= "" + (totalParticles - 1) + "\t" + zombieV + "\t" + velocity + "\n";
        
        try {
            File file = new File("resources/velocities.txt");
            FileWriter myWriter = new FileWriter("resources/velocities.txt", true);
            myWriter.write(toPrint);
            myWriter.close();
            System.out.println("velocities file created");
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
        System.out.println(toPrint);
        */
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
    	LocalTime localTime = LocalTime.now();
    	String timestamp= dtf.format(localTime);
    	
    	writeToFile(toFileDistance, "distance", (totalParticles-1), zombieV, timestamp);
        writeToFile(toFileSpreadingIllness, "spreading", (totalParticles-1), zombieV, timestamp);
	}
	
    private static void writeToFile(List<String> toFile, String fileName, int N, double zombieV, String stamp) {
    	try {
			File file = new File("resources/" + fileName + "_" + N + "_" + zombieV + "_" + stamp + ".txt");
			FileWriter myWriter = new FileWriter("resources/" + fileName + "_" + N + "_" + zombieV + "_" + stamp + ".txt");
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

	static public void main(String[] args) throws IOException {
    	System.out.println("Static");
		BufferedReader readerStatic = new BufferedReader(new InputStreamReader(System.in));
		String staticInput = readerStatic.readLine();
    	
    	System.out.println("Dynamic");
		BufferedReader readerDynamic= new BufferedReader(new InputStreamReader(System.in));
		String dynamicInput = readerDynamic.readLine();
		
		System.out.println("Zombie max v (default 2)");
		BufferedReader readerV= new BufferedReader(new InputStreamReader(System.in));
		String vInput = readerV.readLine();
		
		String staticFile= (staticInput.length() == 0) ? "static.txt" : staticInput;
		String dynamicFile= (dynamicInput.length() == 0) ? "dynamicEnd.txt" : dynamicInput;
		Double zombieV= (vInput.length() == 0) ? 2 : Double.parseDouble(vInput);
		
		System.out.println("Starting with " + staticFile + ", " + dynamicFile + ", V_z= " + zombieV);

		calculateVelocity(staticFile, dynamicFile, zombieV);
		
		System.out.println("End");
    }
}
