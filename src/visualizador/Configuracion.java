/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.util.ArrayList;
import medidasDeCallidad.Calidad;

/**
 *
 * @author Pablo
 */
public class Configuracion {

    private ArrayList puntosVecinos = new ArrayList();
    private Conjunto puntos;
    private ArrayList<Arista> aristas = new ArrayList<Arista>();

    public Configuracion(Conjunto puntos, ArrayList<Arista> aristas) {
        this.puntos = puntos;
        this.aristas = aristas;
    }

    public Configuracion() {
    }

    public ArrayList<Arista> getAristas() {
        return aristas;
    }
    private Punto puntoOrigen;
    private Punto puntoDestino;

    public Punto getPuntoDestino() {
        return puntoDestino;
    }

    public Punto getPuntoOrigen() {
        return puntoOrigen;
    }

    public int cantidadAristas() {
        return aristas.size();
    }

    public Arista getArista(int i) {
        return aristas.get(i);
    }

    public boolean contieneArista(Arista aristaAux) {
        return false;
    }

    public void inicializar(Conjunto puntos) {
    }
    
    public void inicializar() {//agregado 22/09/12
    }
    
    public void calculaGreedyT(Conjunto c) {//agregado 22/09/12
    }

    public int inicializar(Conjunto p, ArrayList<Arista> a) {
        this.puntos = p;
        this.aristas = a;
        return 0;
    }

    public void inicializar(ArrayList<Arista> a) {
        aristas = a;
    }

    public int cantPTriangulos() {
        return 0;
    }

    public int obtenerNumAristas() {
        return aristas.size();
    }

    public int getNumeroPuntos() {
        return puntosVecinos.size();
    }

    public void agregarVecinos(Arista a) {
        Punto origen, destino;
        if (!puntosVecinos.contains(a.obtenerOrig())) {
            puntosVecinos.add(a.obtenerOrig());
            origen = a.obtenerOrig();
        } else {
            origen = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerOrig()));
        }

        if (!puntosVecinos.contains(a.obtenerDest())) {
            puntosVecinos.add(a.obtenerDest());
            destino = a.obtenerDest();
        } else {
            destino = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerDest()));
        }

        origen.agregarVecino(destino);
        destino.agregarVecino(origen);
    }

    public Arista obtenerEdge(int i) {
        return aristas.get(i);
    }

    public Punto getPunto(int i) {
        return (Punto) puntosVecinos.get(i);
    }

    public ArrayList<Arista> dilacion(Conjunto p) {
        Calidad c = new Calidad();
        c.dilacionT(p, this);
        puntoOrigen = c.getPuntoOrigen();
        puntoDestino = c.getPuntoDestino();
        return c.getDijkstra();
    }

    public ArrayList<Arista> dilacion() {
        Calidad c = new Calidad();
        c.dilacionT(puntos, this);
        puntoOrigen = c.getPuntoOrigen();
        puntoDestino = c.getPuntoDestino();
        return c.getDijkstra();
    }

    void removeArista(int k) {
        aristas.remove(k);
    }

    void addArista(Arista ari) {
        aristas.add(ari);
    }

    Conjunto getPuntos() {
        return puntos;
    }

    public boolean EsTrianguloVacio(Triangulo triang, Punto p) {
        double extSup = -triang.verticeA.getY();
        if (-triang.verticeB.getY() > extSup) {
            extSup = -triang.verticeB.getY();
        }
        if (-triang.verticeC.getY() > extSup) {
            extSup = -triang.verticeC.getY();
        }
        double extInf = -triang.verticeA.getY();
        if (-triang.verticeB.getY() < extInf) {
            extInf = -triang.verticeB.getY();
        }
        if (-triang.verticeC.getY() < extInf) {
            extInf = -triang.verticeC.getY();
        }
        double extDer = triang.verticeA.getX();
        if (triang.verticeB.getX() > extDer) {
            extDer = triang.verticeB.getX();
        }
        if (triang.verticeC.getX() > extDer) {
            extDer = triang.verticeC.getX();
        }
        double extIzq = triang.verticeA.getX();
        if (triang.verticeB.getX() < extIzq) {
            extIzq = triang.verticeB.getX();
        }
        if (triang.verticeC.getX() < extIzq) {
            extIzq = triang.verticeC.getX();
        }

        Punto ptoAux = p;
        if (-ptoAux.getY() < extSup && -ptoAux.getY() > extInf && ptoAux.getX() < extDer && ptoAux.getX() > extIzq) {
            if (CalculoLado(triang.verticeA, triang.verticeB, ptoAux) > 0) {
                if (CalculoLado(triang.verticeB, triang.verticeC, ptoAux) > 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) > 0) {
                    return false;
                }
            } else if (CalculoLado(triang.verticeB, triang.verticeC, ptoAux) < 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) < 0) {
                return false;
            }
        }


        return true;
    }

    public static double CalculoLado(Punto extremo1, Punto extremo2, Punto punto) {
        return  (((extremo2.getX() * (-punto.getY()) - (-extremo2.getY()) * punto.getX()) - (extremo1.getX() * (-punto.getY()) - (-extremo1.getY()) * punto.getX())) + (extremo1.getX() * (-extremo2.getY()) - (-extremo1.getY()) * extremo2.getX()));
    }
}
