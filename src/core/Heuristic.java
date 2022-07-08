package core;


import models.Car;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {

	static final double MIN_TIME_IN_LANE = 3;
	private double timeFromLastLaneChange = 0;
	private final double targetV;
	private final double aggressiveness;
	private double currentV;

	public Heuristic(double aggressiveConstant, double maxV, double minV) {
		this.targetV = minV + Math.random()* (maxV-minV);
		this.aggressiveness =  aggressiveConstant;
		this.currentV = targetV;
	}

	public Vector getTarget(Car c, Set<Car> currentLane,int laneNumber, Set<Car> leftLane, Set<Car> rightLane, double dt){

		timeFromLastLaneChange += dt;
		if(currentLane.isEmpty()){
			currentV = targetV;
			return new Vector(c.getPosition().getX() + 100,0);
		}
		Car frontCar = (Car) currentLane.toArray()[0];
		currentV = frontCar.getVelocity().getX() * 0.9;

		if(frontCar.getVelocity().getX() > c.getVelocity().getX()){
			return new Vector(c.getPosition().getX(),0);
		}
		if(leftLane != null && willChange(c,leftLane,frontCar.getVelocity().getX(),c.isAggressive(),dt) && timeFromLastLaneChange < MIN_TIME_IN_LANE){
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
		else if(rightLane != null && willChange(c,rightLane,frontCar.getVelocity().getX(),c.isAggressive(),dt) && timeFromLastLaneChange < MIN_TIME_IN_LANE){
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
		currentV = frontCar.getVelocity().getX() * 0.9;
		return new Vector(getTargetBehindCar(c,frontCar),0);
	}


	public double getTargetBehindCar(Car c, Car frontCar){
		return frontCar.getPosition().getX() - c.getVelocity().getX()*aggressiveness;
	}

	public boolean willChange(Car currentCar, Set<Car> carsInView,double frontCarV, boolean isAggressive, double dt){

		if(carsInView.isEmpty()){
			return true;
		}
		else{
			for(Car c : carsInView){
				if(checkCrash(c,currentCar,dt)){
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
		return Math.abs(nextPos(c1,dt) - nextPos(c2,dt)) < (c1.getRadio()+ c2.getRadio())*1.2;
	}


	public double getCurrentV(){
		return currentV;
	}
	public double getTargetV() {
		return targetV;
	}


}
