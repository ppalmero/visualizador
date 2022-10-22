/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package medidasDeCallidad;

/**
 *
 * @author Pablo
 */
import java.util.ArrayList;
import visualizador.Conjunto;
import visualizador.Punto;
import visualizador.Arista;
import visualizador.Configuracion;

public class Calidad {
    Punto puntoOrigen, puntoDestino;

    public Punto getPuntoDestino() {
        return puntoDestino;
    }

    public Punto getPuntoOrigen() {
        return puntoOrigen;
    }
    ArrayList<Arista> dijkstra;

    public ArrayList<Arista> getDijkstra() {
        return dijkstra;
    }

    public void setOrigen(Punto p) {
        puntoOrigen = new Punto(p);
    }

    public void setDestino(Punto p) {
        puntoDestino = new Punto(p);
    }
    
    public void dilacionT(Conjunto l, Configuracion t/*int pathSelected,int tipoG*/) {
        double dist = 0;//, distAlg = 0;
        //double minDistSP=0.0d;
        double minDistAlg = 0.0d;
        double dilation = 0.0d;

        for (int i = 0; i < l.size(); i++) {
            l.get(i).eliminarVecinos();
        }

        
        for (int i = 0; i < t.obtenerNumAristas(); i++) {
            t.agregarVecinos(t.obtenerEdge(i));
        }

        Punto saveOrigen = new Punto(0, 0);
        Punto saveDestino = new Punto(0, 0);
        for (int i = 0; i < t.getNumeroPuntos(); i++) {
            Punto p = (Punto) t.getPunto(i);
            if (p.getX()> 10000 || p.getX() < -10000 || -p.getY() > 10000 || -p.getY() < -10000){
                continue;
            }
            for (int j = i + 1; j < t.getNumeroPuntos(); j++) {
                Punto q = (Punto) t.getPunto(j);
                if (q.getX()> 10000 || q.getX() < -10000 || -q.getY() > 10000 || -q.getY() < -10000){
                    continue;
                }
                if (!p.igualQue(q)) {//Point2d.equals
                    //establecer origen-destino
                    setOrigen(p);
                    setDestino(q);
                    //calculamos el camino de dijkstra
                    //
                    calculaDijkstraT(t);
                    double distSP = 0.0d;
                    for (int k = 0; k < dijkstra.size(); k++) {
                        Arista aristaAux = (Arista) dijkstra.get(k);
                        distSP += aristaAux.longitud();
                    }
                    //calculamos la dist euclidea
                    dist = p.distance(q);
                    if ((distSP / dist) > dilation) {
                        dilation = distSP / dist;
                        //minDistSP=distSP;
                        minDistAlg = dist;
                        saveOrigen = puntoOrigen;
                        saveDestino = puntoDestino;
                    }
                }
            }
        }
        dist = minDistAlg;
        //distAlg=minDistSP;

        setOrigen(saveOrigen);
        setDestino(saveDestino);
        calculaDijkstraT(t);
    }

    public void calculaDijkstraT(Configuracion t) {
        if (t.getNumeroPuntos() > 1) {
            dijkstra = new ArrayList<Arista>();//.clear();
            int INFINITO = Integer.MAX_VALUE;
            boolean usados[] = new boolean[t.getNumeroPuntos()];
            double d[] = new double[t.getNumeroPuntos()];//dilacionT
            int p[] = new int[t.getNumeroPuntos()];//precedentes
            int iOrigen = 0, iDestino = 0;
            for (int i = 0; i < t.getNumeroPuntos(); i++) {
                usados[i] = false;
                if (t.getPunto(i).igualQue(puntoOrigen)) {
                    iOrigen = i;
                } else if (t.getPunto(i).igualQue(puntoDestino)) {
                    iDestino = i;
                }
                d[i] = INFINITO;
                p[i] = iOrigen;
            }
            d[iOrigen] = 0;
            usados[iOrigen] = false;
            boolean terminado = false;
            while (!terminado) {
                boolean encontrado = false;
                double menorDist = INFINITO;
                int ind = 0;
                for (int i = 0; i < usados.length; i++) {
                    if ((usados[i] == false) && (d[i] < menorDist)) {
                        //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
                        //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
                        menorDist = d[i];
                        encontrado = true;
                        ind = i;
                    }
                }
                if (encontrado && (ind != iDestino)) {
                    usados[ind] = true;
                    ArrayList ady = (ArrayList) t.getPunto(ind).getVecinos();
                    for (int j = 0; j < ady.size(); j++) {
                        int ind2 = 0;
                        for (ind2 = 0; ind2 < t.getNumeroPuntos(); ind2++) {
                            if (((Punto) ady.get(j)).igualQue(t.getPunto(ind2))) {
                                break;
                            }
                        }
                        if (d[ind2] > d[ind] + (t.getPunto(ind2)).distance(t.getPunto(ind))) {
                            d[ind2] = d[ind] + (t.getPunto(ind2)).distance(t.getPunto(ind));
                            p[ind2] = ind;
                        }
                    }
                } else {
                    terminado = true;
                }
            }
            boolean salir = false;
            int iP = iDestino;
            while (!salir) {
                Punto p1 = new Punto(t.getPunto(iP));//Punto p1=((Punto)l.get(iP)).copiar();
                Punto p2 = new Punto(t.getPunto((p[iP])));//Punto p2=((Punto)l.get(p[iP])).copiar();
                dijkstra.add(new Arista(p1, p2));//dijkstra.add(new Arista(p1,p2,0));
                if (p[iP] == iOrigen) {
                    salir = true;
                }
                iP = p[iP];
            }
        }
    }

    /*public void dilacionPT() {
        double dist = 0;//, distAlg = 0;
        //double minDistSP=0.0d;
        double minDistAlg = 0.0d;
        double dilation = 0.0d;

        for (int i = 0; i < pta.getNumeroPuntos(); i++) {
            pta.getPunto(i).eliminarVecinos();
        }

        for (int i = 0; i < pta.cantidadAristas(); i++) {
            pta.agregarVecinos(pta.getArista(i));
        }

        Punto saveOrigen = new Punto(0, 0);
        Punto saveDestino = new Punto(0, 0);
        for (int i = 0; i < pta.getNumeroPuntos(); i++) {
            Punto p = (Punto) pta.getPunto(i);
            for (int j = i + 1; j < pta.getNumeroPuntos(); j++) {
                Punto q = (Punto) pta.getPunto(j);
                if (!p.igualQue(q)) {//Point2d.equals
                    //establecer origen-destino
                    setOrigen(p);
                    setDestino(q);
                    //calculamos el camino de dijkstra
                    calculaDijkstraPT();
                    double distSP = 0.0d;
                    for (int k = 0; k < dijkstra.size(); k++) {
                        Arista aristaAux = (Arista) dijkstra.get(k);
                        distSP += aristaAux.longitud();
                    }
                    //calculamos la dist euclidea
                    dist = p.distance(q);
                    if ((distSP / dist) > dilation) {
                        dilation = distSP / dist;
                        //minDistSP=distSP;
                        minDistAlg = dist;
                        saveOrigen = puntoOrigen;
                        saveDestino = puntoDestino;
                    }
                }
            }
        }
        dist = minDistAlg;
        //distAlg=minDistSP;

        setOrigen(saveOrigen);
        setDestino(saveDestino);
        calculaDijkstraPT();
    }

    public void calculaDijkstraPT() {
        if (pta.getNumeroPuntos() > 1) {
            dijkstra.clear();
            int INFINITO = Integer.MAX_VALUE;
            boolean usados[] = new boolean[pta.getNumeroPuntos()];
            double d[] = new double[pta.getNumeroPuntos()];//dilacionT
            int p[] = new int[pta.getNumeroPuntos()];//precedentes
            int iOrigen = 0, iDestino = 0;
            for (int i = 0; i < pta.getNumeroPuntos(); i++) {
                usados[i] = false;
                if (pta.getPunto(i).igualQue(puntoOrigen)) {
                    iOrigen = i;
                } else if (pta.getPunto(i).igualQue(puntoDestino)) {
                    iDestino = i;
                }
                d[i] = INFINITO;
                p[i] = iOrigen;
            }
            d[iOrigen] = 0;
            usados[iOrigen] = false;
            boolean terminado = false;
            while (!terminado) {
                boolean encontrado = false;
                double menorDist = INFINITO;
                int ind = 0;
                for (int i = 0; i < usados.length; i++) {
                    if ((usados[i] == false) && (d[i] < menorDist)) {
                        //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
                        //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
                        menorDist = d[i];
                        encontrado = true;
                        ind = i;
                    }
                }
                if (encontrado && (ind != iDestino)) {
                    usados[ind] = true;
                    ArrayList ady = (ArrayList) pta.getPunto(ind).getVecinos();
                    for (int j = 0; j < ady.size(); j++) {
                        int ind2 = 0;
                        for (ind2 = 0; ind2 < pta.getNumeroPuntos(); ind2++) {
                            if (((Punto) ady.get(j)).igualQue(pta.getPunto(ind2))) {
                                break;
                            }
                        }
                        if (d[ind2] > d[ind] + (pta.getPunto(ind2)).distance(pta.getPunto(ind))) {
                            d[ind2] = d[ind] + (pta.getPunto(ind2)).distance(pta.getPunto(ind));
                            p[ind2] = ind;
                        }
                    }
                } else {
                    terminado = true;
                }
            }
            boolean salir = false;
            int iP = iDestino;
            while (!salir) {
                Punto p1 = new Punto(pta.getPunto(iP));//Punto p1=((Punto)l.get(iP)).copiar();
                Punto p2 = new Punto(pta.getPunto((p[iP])));//Punto p2=((Punto)l.get(p[iP])).copiar();
                dijkstra.add(new Arista(p1, p2));//dijkstra.add(new Arista(p1,p2,0));
                if (p[iP] == iOrigen) {
                    salir = true;
                }
                iP = p[iP];
            }
        }
    }*/
}
