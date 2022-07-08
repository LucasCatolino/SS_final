package core;

import models.Car;
import models.Vector;
import java.util.Set;
import java.util.TreeSet;

public class PassiveHeuristic extends Heuristic {

	static final private double PASSIVE_CONST = 5;
	static final private double MAX_V = 22;
	static final private double MIN_V = 14;


	public PassiveHeuristic() {
		super(PASSIVE_CONST, MAX_V,MIN_V);
	}







}
