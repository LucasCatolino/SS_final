package core;

import models.Car;
import models.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.*;

public class Algorithm {
    //constant
    private static final double dt = 0.01; //seg
    private static final double MAX_SIMULATION_TIME = 500; //seg
    private static final double VISUAL_FIELD = 5; //m
    private static final double REACTION_TIME = 0.75; //seg

    //variables del sistema
    //private ArrayList<Car> cars;
    private ArrayList<Car>[] lanes;

    private double laneWidth;
    private double highwayLength;
    private int lanesCount;
    private int carCount;


    //para archivar
    private List<String> toFile;

    public Algorithm(String staticFile, String dynamicFile) {
        Locale.setDefault(Locale.US);
        fileReader(staticFile, dynamicFile);
        toFile = new ArrayList<String>();
    }

    public void run() {

        double currentTime = 0;

        fillToFile(currentTime);


        while (!endCondition(currentTime)) {

            ArrayList<Car>[] newLanes = (ArrayList<Car>[]) new ArrayList[lanesCount];
            //agarra una particula y la analiza con todas las demas
            for (int lane = 0; lane < lanesCount; lane++) {


                for (Car currentCar : lanes[lane]) {
                    Set<Car> rightLane = new TreeSet<>(createComparator(currentCar));
                    Set<Car> leftLane = new TreeSet<>(createComparator(currentCar));
                    Set<Car> currentLane = new TreeSet<>(createComparator(currentCar)); // solo el próximo?


                    getCarsInView(lane, currentCar, VISUAL_FIELD, currentLane, leftLane, rightLane);


                    Car newCar = currentCar.next(currentLane, leftLane, rightLane, dt);

                    if(checkPosition(newCar) < 1){
                        newCar.getPosition().sub(new Vector(highwayLength,0));
                    }

                    newLanes[newCar.getLane()].add(newCar);
                }
            }

            currentTime += dt;
            //
            lanes = newLanes;

            fillToFile(currentTime);
        }
        writeOutputTxt();
    }

    private void fillToFile(double time) {
    	toFile.add("" + carCount + "\n");
    	toFile.add("" + String.format("%.2f",time) + "\n");

    	for (int lane=0; lane < lanesCount; lane++){
    		for(Car currentCar : lanes[lane]){
    			toFile.add("" + String.format("%.2f", currentCar.getPosition().getX())
    				+ "\t" + String.format("%.2f", currentCar.getPosition().getY())
    				+ "\t" + String.format("%.2f", currentCar.getRadio())
    				+ "\t" + String.format("%.2f", currentCar.getVelocity().getX()) + "\n");
    		}
    	}
	}
    private void writeOutputTxt() {
        try {
            File file = new File("resources/dynamicEnd.txt");
            FileWriter myWriter = new FileWriter("resources/dynamicEnd.txt");
            for (Iterator iterator = toFile.iterator(); iterator.hasNext(); ) {
                String stringToFile = (String) iterator.next();
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

    private void getCarsInView(int lane, Car currentCar, double visualField, Set<Car> currentLane,
                               Set<Car> leftLane, Set<Car> rightLane) {

        if (lane > 0) {
            for (Car c : lanes[lane - 1]) {
                if (c.getDistanceTo(currentCar) <= VISUAL_FIELD + 2 * c.getRadio()) {
                    rightLane.add(c);
                }
            }
        }
        if (lane < lanesCount - 1) {
            for (Car c : lanes[lane + 1]) {
                if (c.getDistanceTo(currentCar) <= VISUAL_FIELD + 2 * c.getRadio()) {
                    leftLane.add(c);
                }
            }
        }

        for (Car c : lanes[lane]) {
            if (c.getDistanceTo(currentCar) <= VISUAL_FIELD + 2 * c.getRadio()) {
                currentLane.add(c);
            }

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

    private boolean endCondition(double currentTime) {
        return currentTime >= MAX_SIMULATION_TIME;
    }

    private void fileReader(String staticFile, String dynamicFile) {

        //open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);

        this.carCount = Integer.parseInt(staticScanner.next()); //First line N
        this.highwayLength = Double.parseDouble(staticScanner.next()); //Second line A
        this.lanesCount = Integer.parseInt(staticScanner.next()); //Third line n
        double particleRadio = Double.parseDouble(staticScanner.next()); //Forth line particle R
        staticScanner.close();

        this.lanes = new ArrayList[lanesCount];

        //open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);

        dynamicScanner.next(); //First line time

        int carsPerLane = carCount / lanesCount;

        double xCar = 0;
        double yCar = 0;
        int laneNumber = 0;
        int aggressiveCar = 0;
        boolean aggressive = false;

        while (dynamicScanner.hasNext()) {
            ArrayList<Car> cars = new ArrayList<>();
            for (int i = 0; i < carsPerLane; i++) {
                //Each line has X Y, lane and aggressive
                xCar = Double.parseDouble(dynamicScanner.next());
                yCar = Double.parseDouble(dynamicScanner.next());
                laneNumber = Integer.parseInt(dynamicScanner.next());
                aggressiveCar = Integer.parseInt(dynamicScanner.next());

                aggressive = (aggressiveCar == 1);

                Car car = new Car(new Vector(xCar, yCar), new Vector(24, 0), laneNumber, particleRadio, aggressive);

                cars.add(car);
            }
            lanes[laneNumber] = cars;
        }

        dynamicScanner.close();
    }


    private int checkPosition(Car c){
        return Double.compare(highwayLength, c.getPosition().getX());

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

        System.out.println("Dynamic");
        BufferedReader readerDynamic = new BufferedReader(new InputStreamReader(System.in));
        String dynamicInput = readerDynamic.readLine();

        String staticFile = (staticInput.length() == 0) ? "static.txt" : staticInput;
        String dynamicFile = (dynamicInput.length() == 0) ? "dynamic.txt" : dynamicInput;

        System.out.println("Starting with " + staticFile + ", " + dynamicFile);

        Algorithm algorithm = new Algorithm(staticFile, dynamicFile);
        //algorithm.run();
        System.out.println("End");
    }
}

