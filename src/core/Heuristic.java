package core;


import models.Car;
import models.Vector;

import java.util.Set;

public abstract class Heuristic {


	private final double targetV;
	private final double aggressiveness;
	public Heuristic(double aggressiveConstant, double maxV, double minV) {
		this.targetV = minV + Math.random()* (maxV-minV);
		this.aggressiveness =  aggressiveConstant;
	}

	public Vector getTarget(Car c, Set<Car> currentLane,int laneNumber, Set<Car> leftLane, Set<Car> rightLane, double dt){
		if(currentLane.isEmpty()){
			System.out.println("Current lane is empty!\n");
			return new Vector(c.getPosition().getX() + 100,0);
		}
		Car frontCar = (Car) currentLane.toArray()[0];
		if(frontCar.getVelocity().getX() > c.getVelocity().getX()){
			return new Vector(getTargetBehindCar(c,frontCar),0);
		}
		if(leftLane != null && willChange(c,leftLane,frontCar.getVelocity().getX(),c.isAggressive(),dt)){
				c.setLane(laneNumber+1);
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
		else if(rightLane != null && willChange(c,rightLane,frontCar.getVelocity().getX(),c.isAggressive(),dt)){
			c.setLane(laneNumber-1);
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
				if(c.getPosition().getX() < currentCar.getPosition().getX()) {
					if (c.getVelocity().getX() > currentCar.getVelocity().getX() ) {
						if(!isAggressive || checkCrash(c,currentCar,dt)){
							return false;
						}
					}
				}else if(c.getPosition().getX() > currentCar.getPosition().getX()){
					if(c.getVelocity().getX() < currentCar.getVelocity().getX()){
						if(frontCarV > currentCar.getVelocity().getX())
							return false;
					}
				}
			}
		}
		return true;
	}


	private double nextPos(Car c, double dt){
		return dt*c.getVelocity().getX() + c.getPosition().getX();
	}
	private boolean checkCrash(Car c1, Car c2,double dt){
		return nextPos(c1,dt) - nextPos(c2,dt) < (c1.getRadio()+ c2.getRadio())*1.2;
	}


	public double getTargetV() {
		return targetV;
	}


}
