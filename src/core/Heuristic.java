package core;

import models.Particle;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {

	private final double spaceRadio;

	public Heuristic(double spaceRadio) {
		this.spaceRadio = spaceRadio;
	}

	public abstract Vector getTarget(Particle p, Set<Particle> nearerZombies, Set<Particle> contactZombies,
									 Set<Particle> nearerHumans, Set<Particle> contactHumans);



	//retorna 1 si esta dentro de la circunferencia, 0 si esta en el borde, -1 si esta afuera
	protected int checkVector(Vector position){
		double distance = Math.sqrt(Math.pow(position.getX() - spaceRadio, 2) + Math.pow(position.getY() - spaceRadio, 2));
		if(distance > spaceRadio){
			return -1;
		}else if(distance == spaceRadio){
			return 0;
		}
		return  1;

	}

	public double getSpaceRadio() {
		return spaceRadio;
	}
}
