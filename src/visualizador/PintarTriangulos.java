/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Pablo
 */
public class PintarTriangulos {
    public ArrayList<Polygon> caras = new ArrayList<Polygon>();
    public ArrayList<Color> colores = new ArrayList<Color>();
    public ArrayList<Punto> puntos = new ArrayList<Punto>();

    void init() {
        caras = new ArrayList<Polygon>();
        colores = new ArrayList<Color>();
        puntos = new ArrayList<Punto>();
    }
}
