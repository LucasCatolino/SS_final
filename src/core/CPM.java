package core;

import models.Car;
import models.Vector;

import java.util.TreeSet;

public class CPM {
    private static final double MAX_RADIO = 0.32;
    private static final double MIN_RADIO = 0.15;
    private static final double TOU = 0.5; //seg
    static private  final double CRASH_VELOCITY = 10; // m/s
    //private static final double BETA = 1;

    static public void apply(Car p, Vector target, double dt, TreeSet<Car> contactP){


        if(checkPosition(p) <= 0){
            //TODO: llegué al final de la ruta, volver al principio
        }

       else if(!contactP.isEmpty()){
            //get escape verse
            //TODO: a checkiar
            Vector escapeVerse = getEscapeVerse(p, contactP.first());



            //update Velocity (crash velocity)
            p.setVelocity(Vector.multiply(escapeVerse, CRASH_VELOCITY));

            //update vector position
            p.getPosition().add(Vector.multiply(p.getVelocity(), dt));

            ///update radio
            p.setRadio(MIN_RADIO);

        }else {
            //update speed
            double newSpeed =CRASH_VELOCITY*( (p.getRadio() - MIN_RADIO)/(MAX_RADIO - MIN_RADIO));

            //update Velocity
            Vector eTarget = Vector.sub(target, p.getPosition()).getVersor();
            p.setVelocity(Vector.multiply(eTarget, newSpeed));

            //update vector position
            p.getPosition().add(Vector.multiply(p.getVelocity(), dt));

            //update radio
            if(p.getRadio() < MAX_RADIO){
                p.setRadio(p.getRadio() + (MAX_RADIO /(TOU/dt)) );
            }
            if(p.getRadio() > MAX_RADIO){
                p.setRadio(MAX_RADIO);
            }
        }
    }

    static private Vector getEscapeVerse(Car p, Car contactP){
        Vector toReturn = Vector.sub(p.getPosition(), contactP.getPosition());

        return toReturn.getVersor();
    }

    //retorna 1 si esta dentro de la ruta, 0 si esta en el borde, -1 si esta afuera
    static private int checkPosition(Car p){
        //TODO: checkiar (final de la ruta)
     return 1;
    }












}
