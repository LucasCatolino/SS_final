package core;

import models.Particle;
import models.Vector;
import java.util.Set;
import java.util.TreeSet;

public class ZombieHeuristic extends Heuristic {

	private static final double MAX_RANDOM_COUNTER = 2000;// cantidad de dt hasta cambiar a un nuevo randomWalkTarget
	private static final int DEGREES= 360;
	private Vector randomWalkTarget;
	private double randomWalkCounter = -1;

	public ZombieHeuristic(double spaceRadio) {
		super(spaceRadio);
	}

	@Override
	public Vector getTarget(Particle p, Set<Particle> nearerZombies, Set<Particle> contactZombies,
							Set<Particle> nearerHumans, Set<Particle> contactHumans) {

		if(nearerHumans.isEmpty() && contactHumans.isEmpty()){
			return randomWalk(p, contactZombies);

		}
		//reinicio el contador de random walk
		randomWalkCounter = -1;

		return closestParticle(p, (TreeSet<Particle>) nearerHumans, (TreeSet<Particle>) contactHumans);
	}

	//----------
	private Vector closestParticle(Particle p, TreeSet<Particle> nearerHumans, TreeSet<Particle> contactHumans){
		if(!contactHumans.isEmpty()){
			return contactHumans.first().getPosition(); // No deberìa ir a random Walk?
		}
		return nearerHumans.first().getPosition();
	}


	private Vector randomWalk(Particle p , Set<Particle> contactZombies){
		//cambia de direccion
		if(randomWalkCounter == -1 || randomWalkCounter > MAX_RANDOM_COUNTER || !contactZombies.isEmpty()) {
			randomWalkCounter = 0;
			randomWalkTarget = getRandomWalkTarget();

		}else{//mantengo la direccion
			randomWalkCounter++;
		}
		return randomWalkTarget;
	}

	private Vector getRandomWalkTarget(){
		double distance= Math.random() * getSpaceRadio();
		double degrees= Math.random() * DEGREES;

		double x = distance * Math.cos(Math.toRadians(degrees)) + getSpaceRadio();
		double y = distance * Math.sin(Math.toRadians(degrees)) + getSpaceRadio();



		return  new Vector(x, y);

	}




}
