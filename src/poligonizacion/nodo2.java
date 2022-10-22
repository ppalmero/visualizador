/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poligonizacion;

import visualizador.PuntoDouble;

/**
 *
 * @author Pablo
 */
public class nodo2 {
    public PuntoDouble p = new PuntoDouble();
    public double ang;

    nodo2(nodo2 nodo2) {
        p = new PuntoDouble(nodo2.p);
        ang = nodo2.ang;
    }

    nodo2() {
        
    }
}
