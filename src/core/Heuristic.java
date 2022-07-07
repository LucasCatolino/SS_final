package core;


import models.Car;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {

	public Heuristic(double aggressiveConstant, double maxV, double minV) {

	}

	public Vector getTarget(Car p, Set<Car> currentLane, Set<Car> leftLane, Set<Car> rightLane){
		return null;
	}

}
