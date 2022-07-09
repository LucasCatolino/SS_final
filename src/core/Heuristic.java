package core;


import models.Car;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {

	static final double MIN_TIME_IN_LANE = 3;
	private double timeFromLastLaneChange = Math.random()*(MIN_TIME_IN_LANE);
	private final double targetV;
	private final double aggressiveness;
	private double currentV;

	public void setHasCrashed(boolean hasCrashed) {
		this.hasCrashed = hasCrashed;
	}

	private boolean hasCrashed;

	public Heuristic(double aggressiveConstant, double maxV, double minV) {
		this.targetV = minV + Math.random()* (maxV-minV);
		this.aggressiveness =  aggressiveConstant;
		this.currentV = targetV;
		this.hasCrashed = false;
	}

	public Vector getTarget(Car c, Set<Car> currentLane,int laneNumber, Set<Car> leftLane, Set<Car> rightLane, double dt){
		if(hasCrashed){
			currentV = 0;
			return new Vector(0,0);
		}

		if(c.isCloseToLimit() == 1){
			//estoy en el final
			if(Algorithm.carsCloseToLimit[c.getLane()] == null){
				//no hay ningun auto al principio
				currentV = targetV;
			}else {
				Car auxCar = Algorithm.carsCloseToLimit[c.getLane()];
				auxCar.getPosition().set(auxCar.getPosition().getX() + 500, 0);
				slowDownCar(auxCar, c);
			}
			return new Vector(c.getPosition().getX() + 100,0);
		}

		if(currentLane.isEmpty()){
			currentV = targetV;
			return new Vector(c.getPosition().getX() + 100,0);
		}

		Car frontCar = (Car) currentLane.toArray()[0];
		if(checkCrash(frontCar,c,0)){
			hasCrashed = true;
			frontCar.getHeuristic().setHasCrashed(true);
			currentV = 0;
			return new Vector(0,0);
		}

		slowDownCar(frontCar,c);


		if(frontCar.getVelocity().getX() > c.getVelocity().getX()){
			return new Vector(c.getPosition().getX(),0);
		}

		timeFromLastLaneChange += dt;

		if(leftLane != null && willChange(c,leftLane,frontCar.getVelocity().getX(),c.isAggressive(),dt) && timeFromLastLaneChange > MIN_TIME_IN_LANE){
				c.setLane(laneNumber+1);
				timeFromLastLaneChange = 0;
				if(leftLane.isEmpty()){
					return new Vector(c.getPosition().getX() + 100,0);
				}
				for(Car car:leftLane){
					if(car.getPosition().getX() > c.getPosition().getX()){
						frontCar = car;

						break;
					}
				}

		}
		else if(rightLane != null && willChange(c,rightLane,frontCar.getVelocity().getX(),c.isAggressive(),dt) && timeFromLastLaneChange > MIN_TIME_IN_LANE){
			c.setLane(laneNumber-1);
			timeFromLastLaneChange = 0;
			if(rightLane.isEmpty()){
				return new Vector(c.getPosition().getX() + 100,0);
			}
			for(Car car:rightLane){
				if(car.getPosition().getX() > c.getPosition().getX()){
					frontCar = car;
					break;
				}
			}

		}
		slowDownCar(frontCar,c);
		return new Vector(getTargetBehindCar(c,frontCar),0);
	}

	public void slowDownCar(Car frontCar, Car c){
		if(getDistanceToCar(frontCar,c) < aggressiveness){
			currentV = frontCar.getVelocity().getX() *( 0.8);
		}
	}
	public double getDistanceToCar(Car c1, Car c2){
		return c1.getDistanceTo(c2) - c1.getRadio() - c2.getRadio();
	}

	public double getTargetBehindCar(Car c, Car frontCar){
		return frontCar.getPosition().getX() - c.getVelocity().getX()*aggressiveness;
	}

	public boolean willChange(Car currentCar, Set<Car> carsInView,double frontCarV, boolean isAggressive, double dt){
		if(currentCar.isCloseToLimit() >= 0){
			return false;
		}
		if(carsInView.isEmpty()){
			return true;
		}
		else{
			for(Car c : carsInView){
				if(checkLaneChangeCrash(c,currentCar,dt)){
					return false;
				}
				if(isAhead(currentCar,c) && isFaster(c,currentCar)) { // Si estoy mas adelante que el auto, pero el es mas rapido
					if(!isAggressive) { //Si soy un auto pasivo
						return false;
					}
				}else if(!isAhead(currentCar,c) && !isFaster(c,currentCar)){
					if(frontCarV > c.getVelocity().getX())
						return false;
				}
			}
		}
		return true;


	}


	private boolean isAhead(Car c1, Car c2){
		return c1.getPosition().getX() > c2.getPosition().getX();
	}

	private boolean isFaster(Car c1, Car c2){
		return c1.getVelocity().getX() > c2.getVelocity().getX();
	}
	private double nextPos(Car c, double dt){
		return dt*c.getVelocity().getX() + c.getPosition().getX();
	}


	private boolean checkCrash(Car c1, Car c2,double dt){
		return Math.abs(nextPos(c1,dt) - nextPos(c2,dt)) < (c1.getRadio()+ c2.getRadio());
	}

	private boolean checkLaneChangeCrash(Car c1, Car c2,double dt){
		return Math.abs(nextPos(c1,dt) - nextPos(c2,dt)) < (c1.getRadio()+ c2.getRadio()) * 1.3;
	}



	public double getCurrentV(){
		return currentV;
	}
	public double getTargetV() {
		return targetV;
	}


}
