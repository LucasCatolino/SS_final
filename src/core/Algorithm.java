package core;

import models.Car;
import models.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Algorithm {
    //constant
    private static final double dt =0.01; //seg
    private static final double MAX_SIMULATION_TIME = 500; //seg
    private static final double VISUAL_FIELD = 5; //m
    private static final double REACTION_TIME = 0.75; //seg

    //variables del sistema
    private List<Car> cars;

    private  double laneWidth;
    private double highwayLength;
    private int lanesCount;
    private int carCount;



    //para archivar
    private List<String> toFile;

    public Algorithm(String staticFile, String dynamicFile) {
    	Locale.setDefault(Locale.US);
        fileReader(staticFile, dynamicFile);
        toFile= new ArrayList<String>();
    }

    public void run(){

        double currentTime = 0;

        fillToFile(currentTime);
        

        while(!endCondition(currentTime)){

            List<Car> nextIteration = new ArrayList<>();

            //agarra una particula y la analiza con todas las demas
            for ( Car currentCar : cars) {

                Set<Car> rightLane = new TreeSet<>(createComparator(currentCar)); 
                Set<Car> leftLane = new TreeSet<>(createComparator(currentCar));   
                Set<Car> currentLane = new TreeSet<>(createComparator(currentCar));  


                getCarsInView(currentCar, VISUAL_FIELD, currentLane, leftLane, rightLane);


                //creo una nueva particula con los parametro con paso temporal despues
                Car newCar = currentCar.next(currentLane,leftLane,rightLane, dt);


                //lo guardo en el nuevo espacio
                nextIteration.add(newCar);

            }//termina el for

            currentTime += dt;
            cars = nextIteration;

            fillToFile(currentTime);
        }
        writeOutputTxt();
    }

    private void fillToFile(double time) {
    	/*toFile.add("" + carCount + "\n");
    	toFile.add("" + String.format("%.2f",time) + "\n");

    	int zombie= 0;
    	int person= 0;
    	for (Iterator iterator = cars.iterator(); iterator.hasNext();) {
			Car particle = (Car) iterator.next();
    		zombie= particle.isZombie() ? 1 : 0;
			person= (zombie == 1) ? 0 : 1;
			toFile.add("" + String.format("%.2f", particle.getPosition().getX()) + "\t"
					+ String.format("%.2f", particle.getPosition().getY()) + "\t"
					+ String.format("%.2f", particle.getRadio()) + "\t" + zombie + "\t" + person + "\n");
    	}

    	 */
	}
    
    private void writeOutputTxt() {
    	try {
            File file = new File("resources/dynamicEnd.txt");
            FileWriter myWriter = new FileWriter("resources/dynamicEnd.txt");
            for (Iterator iterator = toFile.iterator(); iterator.hasNext();) {
				String stringToFile= (String) iterator.next();
				try {
            		myWriter.write(stringToFile);
            	} catch (Exception e) {
            		System.err.println("IOException");
            	}
			}
            myWriter.close();
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
	}
    
    private void getCarsInView(Car currentCar, double visualField, Set<Car> currentLane,
                                    Set<Car> leftLane, Set<Car> rightLane){
        for ( Car c: cars) {
            //TODO:implementar
        }
    }

    //ordena de las que estan mas cerca de p a mas lejos
    private static Comparator<Car> createComparator(Car p) {
        final Car finalP = new Car(p);
        return (p0, p1) -> {
            double ds0 = p0.getDistanceTo(finalP);
            double ds1 = p1.getDistanceTo(finalP);
            return Double.compare(ds0, ds1);
        };
    }

    private boolean endCondition(double currentTime){
        return currentTime >= MAX_SIMULATION_TIME;
    }

    private void fileReader(String staticFile, String dynamicFile){
    	//open static file
        /*
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        
        int personNumber= Integer.parseInt(staticScanner.next()); //First line N
        int zombieNumber= 1;
        //variable auxiliares
        spaceRadio = Double.parseDouble(staticScanner.next()); //Second line R
        double particleRadio = Double.parseDouble(staticScanner.next()); //Second line particle R
        staticScanner.close();

        this.carCount = personNumber + zombieNumber;
        
	 	//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);
        
        dynamicScanner.next(); //First line time
        
        //Second line has X Y for zombie particle
        double xZombie= Double.parseDouble(dynamicScanner.next());
        double yZombie= Double.parseDouble(dynamicScanner.next());
        
        cars = new ArrayList<>();
        Car car= new Car(new Vector(xZombie, yZombie), new Vector(0, 0), particleRadio, );
        cars.add(car);
        
        while (dynamicScanner.hasNext()) {
        	//Each line has X Y for person particle
			double xPerson= Double.parseDouble(dynamicScanner.next());
			double yPerson= Double.parseDouble(dynamicScanner.next());
			
			Car person= new Car(new Vector(xPerson, yPerson), new Vector(0, 0), particleRadio, false, spaceRadio);
			cars.add(person);
		}
        
        dynamicScanner.close();

         */
    }

    static public void main(String[] args) throws IOException {
		System.out.println("Static");
		BufferedReader readerStatic = new BufferedReader(new InputStreamReader(System.in));
		String staticInput = readerStatic.readLine();
		
		System.out.println("Dynamic");
		BufferedReader readerDynamic= new BufferedReader(new InputStreamReader(System.in));
		String dynamicInput = readerDynamic.readLine();
				
		String staticFile= (staticInput.length() == 0) ? "static.txt" : staticInput;
		String dynamicFile= (dynamicInput.length() == 0) ? "dynamic.txt" : dynamicInput;
		
		System.out.println("Starting with " + staticFile + ", " + dynamicFile);
		
		Algorithm algorithm= new Algorithm(staticFile, dynamicFile);
		algorithm.run();
		System.out.println("End");
	}

}
