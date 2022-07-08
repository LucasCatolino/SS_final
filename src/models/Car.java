package models;


import core.AggressiveHeuristic;
import core.Heuristic;
import core.CPM;
import core.PassiveHeuristic;

import java.util.*;

public class Car {

    static private int nextId = 0;

    private double radio;
    private Vector position;
    private Vector velocity;
    private Heuristic heuristic;
    private boolean isAggressive;
    private int lane;
    private final int id;

    public Car(Car car){
        position = new Vector(car.position.getX(), car.position.getY());
        velocity = new Vector(car.velocity.getX(), car.velocity.getY());
        this.id = car.id;
        heuristic = car.heuristic;
        this.lane = car.lane;
        this.radio = car.radio;
        this.isAggressive = car.isAggressive;
    }

    public Car(Vector position, Vector velocity, int lane, double radio, boolean isAggressive) {
        this.position = position;
        this.velocity = velocity;
        this.radio = radio;
        this.lane = lane;
        this.id = getNextId();
        this.isAggressive = isAggressive;
        if(isAggressive){
            this.heuristic = new AggressiveHeuristic();
        }else{
            this.heuristic = new PassiveHeuristic();
        }

    }

    static private int getNextId(){
        int toReturn = nextId;
        nextId++;
        return toReturn;
    }

    //-------------------------------------------------

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public double getDistanceTo(Car p){
        return position.getDistanceTo(p.getPosition());
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(double velocityX, double velocityY){
        velocity.setX(velocityX);
        velocity.setY(velocityY);
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }



    //------------------------------------------

    //devuelve una particula con la nueva posicion;
    public Car next(Set<Car> currentLane, Set<Car> leftLane, Set<Car> rightLane, double dt){

        Car newCar = new Car(this);


        //aplica la heurística
        Vector target = heuristic.getTarget(newCar , currentLane,leftLane,rightLane);

        //manejo de velocidades maxima ?????


        //si el target es null no muevo la particula
        CPM.apply(newCar, target, dt,null);

        //retorno la nueva particula con la nueva posiciones
        return newCar;
    }


    //-------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car particle = (Car) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
