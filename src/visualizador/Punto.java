/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablazo
 */
public class Punto extends Point2D {

    public double x;
    public double y;
    public double xRotado = 0;
    public double yRotado = 0;
    public int cara;
    String vReal;
    public static int GREEDY_T = 16384;
    ArrayList<Punto> vecinos;
    int posicion = -1; // Agregado luego del error del 31/12/12 en el convexhull viejo

    public Punto(int cordx, int cordy) {
        x = cordx;
        y = cordy;
        vecinos = new ArrayList<Punto>();
    }

    public Punto(double cordx, double cordy) {
        x = (double) cordx;
        y = (double) cordy;
        vReal = String.valueOf(cordx) + ", " + String.valueOf(cordy);
        vecinos = new ArrayList<Punto>();
    }

    public Punto(Punto p) {
        x = p.x;
        y = p.y;
        posicion = p.posicion;
        xRotado = p.xRotado;
        yRotado = p.yRotado;
        vecinos = new ArrayList<Punto>();
    }

    public Punto() {
        x = 0;
        y = 0;
        vecinos = null;
    }

    public boolean yaEsVecino(Punto p) {
        return this.vecinos.contains(p);
    }

    public boolean agregarVecino(Punto p) {
        if (vecinos.size() > 0) {
            if (vecinos.contains((Punto) p)) {
                return false;
            }
        }

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

    public boolean igualQue(Punto v) {
        return (this.x == v.x && this.y == v.y);
    }

    public void asignar(Punto v) {
        this.x = v.x;
        this.y = v.y;
        this.xRotado = v.xRotado;
        this.yRotado = v.yRotado;
        this.posicion = v.posicion;
    }

    public void eliminarVecinos() {
        vecinos.clear();
    }

    public ArrayList getVecinos() {
        return vecinos;
    }

    public int tamAdyacentes() {
        return vecinos.size();
    }

    public Object getVecino(int i) {
        return vecinos.get(i);
    }

    public void setVReal(String s) {
        vReal = s;
    }

    public String getVReal() {
        return vReal;
    }

    public boolean menorQue(Punto q, double angulo) {
        double pX, pY, qX, qY;
        angulo = Math.toRadians(angulo);
        pX = (this.x * Math.cos(angulo)) - (this.y * Math.sin(angulo));
        pY = (this.x * Math.sin(angulo)) + (this.y * Math.cos(angulo));
        qX = (q.x * Math.cos(angulo)) - (q.y * Math.sin(angulo));
        qY = (q.x * Math.sin(angulo)) + (q.y * Math.cos(angulo));
        return ((pX < qX) || (pX == qX && pY > qY));
    }

    public Punto rotado(double angulo) {
//        this.xRotado = this.x;
//        this.yRotado = this.y;
        angulo = Math.toRadians(angulo);
        Punto p = new Punto((int) ((this.x * Math.cos(angulo)) - (this.y * Math.sin(angulo))), (int) ((this.x * Math.sin(angulo)) + (this.y * Math.cos(angulo))));
        p.xRotado = this.x;
        p.yRotado = this.y;
        return p;
    }

    public Punto contraRotado(double angulo) {
        /*angulo = Math.toRadians(angulo) * -1;
        return new Punto((int)((this.x * Math.cos(angulo)) - (this.y * Math.sin(angulo))), (int)((this.x * Math.sin(angulo)) + (this.y * Math.cos(angulo))));*/
        if (angulo != 0) {
            return (new Punto(this.xRotado, this.yRotado));
        } else {
            return this;
        }
    }

    
}
