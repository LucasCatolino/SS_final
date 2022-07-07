package core;

import core.Heuristic;
import models.Particle;
import models.Vector;

import java.util.List;
import java.util.Set;

public class PersonHeuristic extends Heuristic {

	static final private double CRITICAL_DIST_FROM_WALL = 1.5;
	static final private double ZOMBIE_IMPACT = 5;

	public PersonHeuristic(double spaceRadio) {
		super(spaceRadio);
	}

	@Override
	public Vector getTarget(Particle p, Set<Particle> nearerZombies, Set<Particle> contactZombies,
							Set<Particle> nearerHumans, Set<Particle> contactHumans) {

		//se quedan quietos si no ven a ningún zombie
		if(nearerZombies.isEmpty() || !contactZombies.isEmpty()) {
			return null;
		}

		Vector bestDir = new Vector(0,0);
		for( Particle z : nearerZombies){
			bestDir.add(z.getPosition().sub(p.getPosition()).opposite().getVersor().multiply(1.0 / z.getDistanceTo(p)).multiply(ZOMBIE_IMPACT));
		}
		for( Particle h : nearerHumans){
			bestDir.add(h.getPosition().sub(p.getPosition()).opposite().getVersor().multiply(1.0 / h.getDistanceTo(p)));
		}


		if(!contactHumans.isEmpty()){
			bestDir = new Vector(-bestDir.getY(),bestDir.getX());
		}

		if(checkPosition2(p) < CRITICAL_DIST_FROM_WALL || contactHumans.size() > 1 ){

			//vector con direcion al centro
			double xnoise = getSpaceRadio() + (Math.random() - 0.5);
			double ynoise = getSpaceRadio() + (Math.random() - 0.5);
			bestDir = new Vector(xnoise,ynoise);
			bestDir = bestDir.sub(p.getPosition());


			/*bestDir.set(-bestDir.getY(), bestDir.getX());
			//rando
			/*
			double chance = Math.random();
			if(chance <= (double) 1/2 ){

			}else {
				bestDir.set(-bestDir.getY(), -bestDir.getX());
			}

			 */

		}

		return Vector.add(bestDir,p.getPosition());
	}


	static private double checkPosition2(Particle p){
		double distance = Math.sqrt(Math.pow(p.getPosition().getX() - p.getSpaceRadio(), 2) + Math.pow(p.getPosition().getY() - p.getSpaceRadio(), 2));
		if(distance + p.getRadio() >  p.getSpaceRadio()){
			return -1;
		}else if(distance + p.getRadio() ==  p.getSpaceRadio()){
			return 0;
		}
		return  (distance + p.getRadio());
	}

}
