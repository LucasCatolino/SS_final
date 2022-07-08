package core;


import models.Car;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {
	private final double targetV;

	public Heuristic(double aggressiveConstant, double maxV, double minV) {
		targetV = minV + Math.random()* (maxV-minV);
	}

	public Vector getTarget(Car p, Set<Car> currentLane, Set<Car> leftLane, Set<Car> rightLane){
		if(currentLane.isEmpty()){
			p.setVelocity(targetV,0);
		}

		return null;
	}

}
