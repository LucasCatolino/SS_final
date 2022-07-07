package models;

import java.util.Objects;

public class Vector {
    private double x;
    private double y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector( Vector v) {
        x = v.x;
        y = v.y;
    }

    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDistanceTo(Vector v){
        return getDistanceTo(v.x, v.y);
    }


    public double getDistanceTo(double x, double y){
        return Math.sqrt(Math.pow((this.x - x),2) + Math.pow((this.y - y),2));
    }

    public Vector getVersor(){
        return new Vector(getX()/getModule(), getY()/getModule());
    }

    public double getModule(){
        return Math.sqrt(Math.pow((x),2) + Math.pow((y),2));
    }

    public Vector opposite(){
        return new Vector(-x,-y);
    }

    public void add(Vector v){
        this.x += v.x;
        this.y += v.y;
    }

    public void multiply(double e){
        set(x*e,y*e);
    }

    public void sub(Vector v){
        set(x - v.x, y - v.y);
    }

    static public Vector add(Vector v1, Vector v2){
        Vector toReturn = new Vector(v1);
        toReturn.add(v2);
        return  toReturn;
    }

    static public Vector sub(Vector v1, Vector v2){
        Vector toReturn = new Vector(v1);
        toReturn.sub(v2);
        return toReturn;
    }

    static public Vector multiply(Vector v1, double e){
        Vector toReturn = new Vector(v1);
        toReturn.multiply(e);
        return toReturn;
    }

    @Override
    public String toString() {
        return "{" +
                "x= " + x +
                ", y= " + y +
                '}';
    }
}
