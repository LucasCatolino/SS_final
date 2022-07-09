package models;


import core.*;

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


    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public boolean isAggressive() {
        return isAggressive;
    }

    public void setAggressive(boolean aggressive) {
        isAggressive = aggressive;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getId() {
        return id;
    }

    //devuelve -1 si no está cerca de ninguna pared, 1 si esta cerca de la derecha, 0 si está cerca de la izquierda
    public int isCloseToLimit(){
        double hl = Algorithm.HIGHWAY_LENGTH;
        if(position.getX() + radio + 5  > hl){
            return 1;
        }
        if(position.getX() - radio - 5 < 0) {
            return 0;
        }
        return -1;
    }

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
    public Car next(int laneNumber, Set<Car> currentLane, Set<Car> leftLane, Set<Car> rightLane, double dt){

        Car newCar = new Car(this);


        //aplica la heurística
        Vector target = heuristic.getTarget(newCar ,currentLane, laneNumber,leftLane,rightLane,dt);

        //manejo de velocidades maxima ?????


        //si el target es null no muevo la particula
        CPM.apply(newCar, target, dt, new TreeSet<>());

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
