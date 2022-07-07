package core;

import models.Particle;
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
    private static final double Z_VISUAL_FIELD = 4; //m
    private static final double H_VISUAL_FIELD = 6; //m

    //variables del sistema
    private List<Particle> particles;
    private int totalNumber;
    private double spaceRadio;
    
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
        
        int zombieNumber = 1;
        int personNumber = totalNumber - zombieNumber;

        while(!endCondition(currentTime, personNumber)){

            List<Particle> newPosition = new ArrayList<>();

            zombieNumber = 0;
            personNumber = 0;

            //agarra una particula y la analiza con todas las demas
            for ( Particle currentP : particles ) {

                Set<Particle> nearerZombies = new TreeSet<>(createComparator(currentP));  // zombies dentro del campo de vision de currentP
                Set<Particle> contactZombies = new TreeSet<>(createComparator(currentP)); // zombies que estan tocando a currentP
                Set<Particle> nearerHumans = new TreeSet<>(createComparator(currentP));   // humanos dentro del campo de vision de currentP
                Set<Particle> contactHumans = new TreeSet<>(createComparator(currentP));  // humanos que estan tocando a currentP

                //obtengo la particulas dentro de su campo visual y oredenas de mas cercanas a lejanas
                if(currentP.isZombie()){
                    getNearerParticles(currentP, Z_VISUAL_FIELD, nearerZombies, contactZombies, nearerHumans, contactHumans);
                }else{
                    getNearerParticles(currentP, H_VISUAL_FIELD, nearerZombies, contactZombies, nearerHumans, contactHumans);
                }

                //creo una nueva particula con los parametro con paso temporal despues
                Particle newP = currentP.next(nearerZombies, contactZombies, nearerHumans, contactHumans, dt);


                //metricas de la corrida actual
                if(newP.isZombie()) {
                    zombieNumber++;
                }else {
                    personNumber++;
                }

                //lo guardo en el nuevo espacio
                newPosition.add(newP);

            }//termina el for

            if(totalNumber != zombieNumber + personNumber){
                System.out.println("FALLO");
                return;
            }

            currentTime += dt;
            particles = newPosition;

            fillToFile(currentTime);
        }
        writeOutputTxt();
    }

    private void fillToFile(double time) {
    	toFile.add("" + totalNumber + "\n");
    	toFile.add("" + String.format("%.2f",time) + "\n");
    	
    	int zombie= 0;
    	int person= 0;
    	for (Iterator iterator = particles.iterator(); iterator.hasNext();) {
			Particle particle = (Particle) iterator.next();
    		zombie= particle.isZombie() ? 1 : 0;
			person= (zombie == 1) ? 0 : 1;
			toFile.add("" + String.format("%.2f", particle.getPosition().getX()) + "\t"
					+ String.format("%.2f", particle.getPosition().getY()) + "\t"
					+ String.format("%.2f", particle.getRadio()) + "\t" + zombie + "\t" + person + "\n");
    	}	
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
    
    private void getNearerParticles(Particle currentP, double visualField, Set<Particle> nearerZombies,
                                    Set<Particle> contactZombies, Set<Particle> nearerHumans, Set<Particle> contactHumans){
        for ( Particle p: particles ) {
            if((!currentP.equals(p)) && (currentP.getDistanceTo(p) <= visualField)){
                if(currentP.getDistanceTo(p) <= currentP.getRadio() + p.getRadio()) {
                    //se estan tocando
                    if(p.isZombie()) {
                        contactZombies.add(p);
                    }else{
                        contactHumans.add(p);
                    }
                }else{
                    //esta en el campo visual y no se estan tocando
                    if(p.isZombie()) {
                        nearerZombies.add(p);
                    }else{
                        nearerHumans.add(p);
                    }
                }
            }
        }
    }

    //ordena de las que estan mas cerca de p a mas lejos
    private static Comparator<Particle> createComparator(Particle p) {
        final Particle finalP = new Particle(p);
        return (p0, p1) -> {
            double ds0 = p0.getDistanceTo(finalP);
            double ds1 = p1.getDistanceTo(finalP);
            return Double.compare(ds0, ds1);
        };
    }

    private boolean endCondition(double currentTime, int personNumber){
        return personNumber == 0 || currentTime >= MAX_SIMULATION_TIME;
    }

    private void fileReader(String staticFile, String dynamicFile){
    	//open static file
        InputStream staticStream = Algorithm.class.getClassLoader().getResourceAsStream(staticFile);
        assert staticStream != null;
        Scanner staticScanner = new Scanner(staticStream);
        
        int personNumber= Integer.parseInt(staticScanner.next()); //First line N
        int zombieNumber= 1;
        //variable auxiliares
        spaceRadio = Double.parseDouble(staticScanner.next()); //Second line R
        double particleRadio = Double.parseDouble(staticScanner.next()); //Second line particle R
        staticScanner.close();

        this.totalNumber= personNumber + zombieNumber;
        
	 	//open dynamic file
        InputStream dynamicStream = Algorithm.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;
        Scanner dynamicScanner = new Scanner(dynamicStream);
        
        dynamicScanner.next(); //First line time
        
        //Second line has X Y for zombie particle
        double xZombie= Double.parseDouble(dynamicScanner.next());
        double yZombie= Double.parseDouble(dynamicScanner.next());
        
        particles = new ArrayList<>();
        Particle zombie= new Particle(new Vector(xZombie, yZombie), new Vector(0, 0), particleRadio, true, spaceRadio);
        particles.add(zombie);
        
        while (dynamicScanner.hasNext()) {
        	//Each line has X Y for person particle
			double xPerson= Double.parseDouble(dynamicScanner.next());
			double yPerson= Double.parseDouble(dynamicScanner.next());
			
			Particle person= new Particle(new Vector(xPerson, yPerson), new Vector(0, 0), particleRadio, false, spaceRadio);
			particles.add(person);
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
		String dynamicFile= (dynamicInput.length() == 0) ? "dynamic.txt" : dynamicInput;
		
		System.out.println("Starting with " + staticFile + ", " + dynamicFile);
		
		Algorithm algorithm= new Algorithm(staticFile, dynamicFile);
		algorithm.run();
		System.out.println("End");
	}

}
