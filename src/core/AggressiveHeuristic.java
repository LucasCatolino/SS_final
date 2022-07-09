package core;


public class AggressiveHeuristic extends Heuristic {

	static final private double AGGRESSIVE_CONST = 1.0;
	static final private double MAX_TARGET_V = 28;
	static final private double MIN_TARGET_V = 22; //por ahora

	public AggressiveHeuristic() {
		super(AGGRESSIVE_CONST,MAX_TARGET_V,MIN_TARGET_V);
	}
}
