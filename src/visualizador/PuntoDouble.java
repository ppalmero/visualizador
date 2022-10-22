/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizador;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablazo
 */
public class PuntoDouble extends Point2D {
    public double x;
    public double y;
    public int cara;
    String vReal;
    public static int GREEDY_T= 16384;
    ArrayList<PuntoDouble> vecinos;
    public PuntoDouble (double cordx, double cordy){
        x = cordx;
        y = cordy;
        vecinos = new ArrayList<PuntoDouble>();
    }

    public PuntoDouble(PuntoDouble p) {
        x = p.x;
        y = p.y;
        vecinos = new ArrayList<PuntoDouble>();
    }
    public PuntoDouble(){
        x = 0;
        y = 0;
        vecinos = null;
    }
    public boolean yaEsVecino(PuntoDouble p){
        return this.vecinos.contains(p);
    }
    public boolean agregarVecino(PuntoDouble p){
        if (vecinos.size()> 0)
            if (vecinos.contains((PuntoDouble)p))
                return false;

        vecinos.add(p);
        return true;
    }

    public double getX() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return (x);
    }

    public double getY() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return (-y);
    }

    public void setLocation(double cordX, double cordY) {
        //throw new UnsupportedOperationException("Not supported yet.");
        x = cordX;
        y = cordY;
    }

    public boolean igualQue (PuntoDouble v){
        //return (this.x == v.x  &&  this.y == v.y);
        return ((Math.abs(this.x - v.x) <= 0.00001 )&&(Math.abs(this.y - v.y) <= 0.00001 ));
    }

    public void asignar (PuntoDouble v){
         this.x = v.x;
         this.y = v.y;
    }

    public void eliminarVecinos(){
        vecinos.clear();
    }

    public ArrayList getVecinos(){
        return vecinos;
    }

    public int tamAdyacentes() {
        return vecinos.size();
    }

    public Object getVecino(int i) {
        return vecinos.get(i);
    }

    public void setVReal(String s){
        vReal = s;
    }

    public String getVReal(){
        return vReal;
    }

}
