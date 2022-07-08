package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import core.Algorithm;

public class AnimationFilesCreator {
	
	private static double MAX_VEL= 24;
	private static final double LANE_WIDTH= 3.5;
	
	private static void createAnimationFile(String staticFile, String dynamicFile) {
		String outName= dynamicFile.substring(0, dynamicFile.length() - 4); //removes 'End' from name
		double time;
		double x;
		double y;
		double r;
		double velocity;
		double velocityGradient;
		
		//open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        int carCount= Integer.parseInt(staticScanner.next()); //First line N
        double highwayLength= Double.parseDouble(staticScanner.next()); //Second line A
        double lanesCount = Integer.parseInt(staticScanner.next()); //Third line n
        double topY= lanesCount * LANE_WIDTH;
        staticScanner.close();
		
		//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);
        
        try {
            File file = new File("resources/" + outName + ".xyz");
            FileWriter myWriter = new FileWriter("resources/" + outName + ".xyz");
            while (dynamicScanner.hasNext()) {
            	
            	dynamicScanner.next(); //skip N token
            	//Time
            	time= Double.parseDouble(dynamicScanner.next());
            	
            	myWriter.write("" + (carCount + 4) + "\n"); //Add 4 for visualization
            	myWriter.write("" + time + "\n");
            	for (int i = 0; i < carCount; i++) {
                	//X Y Radius Color
            		x= Double.parseDouble(dynamicScanner.next());
            		y= Double.parseDouble(dynamicScanner.next());
            		r= Double.parseDouble(dynamicScanner.next());
            		velocity= Double.parseDouble(dynamicScanner.next());
            		velocityGradient= 1 - (double) velocity/MAX_VEL;
            		
            		myWriter.write("" + x + "\t" + y + "\t" + r + "\t" + velocityGradient + "\t1\n");
				}
            	//Add 5 false particles for visualization
            	myWriter.write("0\t0\t0\t0\t0\n");
                myWriter.write("0\t" + topY + "\t0\t0\t0\n");
                myWriter.write("" + highwayLength + "\t0\t0\t0\t0\n");
                myWriter.write("" + highwayLength + "\t" + topY + "\t0\t0\t0\n");
    		}
            myWriter.close();
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
        dynamicScanner.close();
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

		createAnimationFile(staticFile, dynamicFile);
		
		System.out.println("End");
    }

}
