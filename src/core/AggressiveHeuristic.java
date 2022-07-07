package core;


public class AggressiveHeuristic extends Heuristic {

	static final private double AGGRESSIVE_CONST = 5;
	static final private double MAX_V = 100;
	static final private double MIN_V = 80;

	public AggressiveHeuristic() {
		super(AGGRESSIVE_CONST,MAX_V,MIN_V);
	}
}
