package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import core.Algorithm;

public class
AnimationFilesCreator {
	
	private static void createAnimationFile(String staticFile, String dynamicFile) {
		String outName= dynamicFile.substring(0, dynamicFile.length() - 4);
		Double time;
		Double x;
		Double y;
		Double r;
		int zombie;
		int person;
		
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
        
        try {
            File file = new File("resources/" + outName + ".xyz");
            FileWriter myWriter = new FileWriter("resources/" + outName + ".xyz");
            while (dynamicScanner.hasNext()) {
            	
            	dynamicScanner.next(); //skip N token
            	//Time
            	time= Double.parseDouble(dynamicScanner.next());
            	
            	myWriter.write("" + (totalParticles + 5) + "\n"); //Add 5 for visualization
            	myWriter.write("" + time + "\n");
            	for (int i = 0; i < totalParticles; i++) {
                	//X Y R Zombie Person
            		x= Double.parseDouble(dynamicScanner.next());
            		y= Double.parseDouble(dynamicScanner.next());
            		r= Double.parseDouble(dynamicScanner.next());
            		zombie= Integer.parseInt(dynamicScanner.next());
            		person= Integer.parseInt(dynamicScanner.next());
            		myWriter.write("" + x + "\t" + y + "\t" + r + "\t" + zombie + "\t" + person + "\t0\t0\n");
				}
            	//Add 5 false particles for visualization
            	myWriter.write("0\t0\t0.1\t0\t0\t0\t0\n");
                myWriter.write("0\t" + (2*spaceRadio) + "\t0.1\t0\t0\t0\t0\n");
                myWriter.write("" + (2*spaceRadio) + "\t0\t0.1\t0\t0\t0\t0\n");
                myWriter.write("" + (2*spaceRadio) + "\t" + (2*spaceRadio) + "\t0.1\t0\t0\t0\t0\n");
                myWriter.write("" + (spaceRadio+0.001) + "\t" +	(spaceRadio+0.001) + "\t" + spaceRadio + "\t1\t1\t0\t0.8\n");
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
