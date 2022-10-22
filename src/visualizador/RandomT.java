/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Pablazo
 */
public class RandomT extends Triangulacion {

    private final int INIT = 50;
    ArrayList grafoGreedyT;
    public Punto[] listaPuntos; // array de vértices
    private ArrayList puntosVecinos;
    public int numPuntos = 0, capacidadPuntos; // nº de vértices que hay en listaVert

    public void inicializar() {
        grafoGreedyT = new ArrayList();
        capacidadPuntos = INIT;
        puntosVecinos = new ArrayList();
    }

    public void calculaGreedyT(Conjunto puntos) {

        listaPuntos = new Punto[puntos.size()];
        /*for (int i = 0; i < puntos.size()-1; i++) {
        listaPuntos[numPuntos] = new Punto (puntos.get(i));
        numPuntos++;
        }*/
        Punto q = null;
        ArrayList aristasOrden = new ArrayList();
        Arista a = null;
        grafoGreedyT.clear();
        if (puntos.size() > 1) {
            //crear una lista de aristas ordenadas por longitud
            for (int i = 0; i < puntos.size() - 1; i++) {
                Punto p = (Punto) puntos.get(i);
                for (int j = i + 1; j < puntos.size(); j++) {
                    q = (Punto) puntos.get(j);
                    if (!p.equals(q)) {
                        //insertarOrden(aristasOrden, p, q);
                        aristasOrden.add(new Arista(p, q));
                    }
                }
            }

            //Añadir las aristas al grafo de menor a mayor sin que corten las ya añadidas
            while (aristasOrden.size() > 0) {
                int r = (int) (Math.random() * (aristasOrden.size() - 1));
                a = (Arista) aristasOrden.get(r);
                if (!cortaAristas(grafoGreedyT, a)) {
                    Arista aristaAux = new Arista((Punto) a.obtenerOrig().clone(), (Punto) a.obtenerDest().clone());
                    grafoGreedyT.add(aristaAux);
                    if (!yaExistia(aristaAux.obtenerOrig())) {
                        insertarOrdenadoPunto(aristaAux.obtenerOrig());
                    }
                    if (!yaExistia(aristaAux.obtenerDest())) {
                        insertarOrdenadoPunto(aristaAux.obtenerDest());
                    }
                }
                aristasOrden.remove(r);
            }
        }

    }

    public boolean cortaAristas(ArrayList aristas, Arista a) {
        boolean corta = false;
        Arista ar = null;
        double sig1, sig2;
        double corteY;
        double corteX;
        int i = 0;
        Punto p1 = a.obtenerOrig();
        Punto p2 = a.obtenerDest();
        while (!corta && i < aristas.size()) {
            ar = (Arista) aristas.get(i);
            Punto p = ar.obtenerOrig();
            Punto q = ar.obtenerDest();
            sig1 = Det(p, q, p1);
            sig2 = Det(p, q, p2);
            if (((sig1 < 0) && (sig2 > 0)) || ((sig1 > 0) && (sig2 < 0))) {
                double vx = p2.x - p1.x;
                double vy = p2.y - p1.y;
                double wx = q.x - p.x;
                double wy = q.y - p.y;
                if (vy == 0) {
                    corteY = (p.x - p1.x + (p1.y * vx) - (p.y * wx / wy)) / ((vx) - (wx / wy));
                    corteX = ((corteY - p1.y) * vx) + p1.x;
                } else if (wy == 0) {
                    corteY = (p.x - p1.x + (p1.y * vx / vy) - (p.y * wx)) / ((vx / vy) - (wx));
                    corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                } else {
                    corteY = (p.x - p1.x + (p1.y * vx / vy) - (p.y * wx / wy)) / ((vx / vy) - (wx / wy));
                    corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                }
                Punto ptoCorte = new Punto(corteX, corteY);
                if ((p.distance(ptoCorte) <= p.distance(q)) && (q.distance(ptoCorte) <= q.distance(p))) {
                    corta = true;
                }
            }
            i++;
        }
        return corta;
    }

    private void insertarOrden(ArrayList<Arista> aristas, Punto p, Punto q) {
        boolean fin = false;
        Arista a = null;
        int i = 0;
        int li = 0, ls = aristas.size() - 1;
        int m = (li + ls) / 2;
        if (aristas.size() > 0) {
            while (li < ls) {
                if (p.distance(q) < aristas.get(m).obtenerOrig().distance(aristas.get(m).obtenerDest())) {
                    ls = m - 1;
                } else {
                    li = m + 1;
                }
                m = (li + ls) / 2;
            }
            if (p.distance(q) < aristas.get(m).obtenerOrig().distance(aristas.get(m).obtenerDest())) {
                aristas.add(m, new Arista(p, q));
            } else {
                aristas.add(m + 1, new Arista(p, q));
            }
            return;
        }
        /*while(!fin&&i<aristas.size()){
        a=(Arista) aristas.get(i);
        if (p.distance(q)<a.obtenerOrig().distance(a.obtenerDest())){
        fin=true;
        }
        else
        i++;
        }*/
        aristas.add(i, new Arista(p, q));
    }

    public double Det(Punto a, Punto b, Punto z) {
        /*return ((b.getX()*z.getY()+z.getX()*a.getY()+a.getX()*b.getY())-
        (b.getX()*a.getY()+z.getX()*b.getY()+a.getX()*z.getY()));*/
        return (((a.x - z.x) * (b.y - z.y)) - ((b.x - z.x) * (a.y - z.y)));
    }

    public Arista getArista(int i) // devuelve la Arista introducida en el lugar "i"
    {
        return (Arista) grafoGreedyT.get(i);
    }
    
    public ArrayList<Arista> getAristas() {
        return grafoGreedyT;
    }
    
    public Arista obtenerEdge(int i){
        return (Arista) grafoGreedyT.get(i);
    }

    public int cantidadAristas() // Devuelve el número de Aristas introducidas hasta el momento
    {
        return grafoGreedyT.size();
    }
    
    public int obtenerNumAristas(){
        return grafoGreedyT.size();
    }
    
    void removeArista(int k) {
        grafoGreedyT.remove(k);
    }
    
    void addArista(Arista ari) {
        grafoGreedyT.add(ari);
    }

//AGREGADOS POR MÍ
    public boolean flip1Arista(Arista e, int j) {
        // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
        Vector puntosF = new Vector();
        Arista auxN;
        Punto punto1 = new Punto(0, 0);
        Punto punto2 = new Punto(0, 0);

        for (int i = 0; i < grafoGreedyT.size(); i++) {
            if (((Arista) grafoGreedyT.get(i)) != e) {
                if ((((Arista) grafoGreedyT.get(i)).obtenerOrig().igualQue(e.obtenerOrig())) || (((Arista) grafoGreedyT.get(i)).obtenerOrig().igualQue(e.obtenerDest()))) {
                    puntosF.addElement(((Arista) grafoGreedyT.get(i)).obtenerDest());
                } else if ((((Arista) grafoGreedyT.get(i)).obtenerDest().igualQue(e.obtenerOrig())) || (((Arista) grafoGreedyT.get(i)).obtenerDest().igualQue(e.obtenerDest()))) {
                    puntosF.addElement(((Arista) grafoGreedyT.get(i)).obtenerOrig());
                }
            }
        }

        punto1.asignar(buscarPunto(puntosF, e)); //Busca el primer punto para flipar
        punto2.asignar(buscarPunto(puntosF, e)); //Busca el segundo

        if ((punto1.igualQue(new Punto(0, 0))) || (punto2.igualQue(new Punto(0, 0))))//Si alguno devolvió el punto (0,0) => no se puede flipar
        {
            return false;
        } else {
            auxN = new Arista(punto1, punto2);//Crea la nueva arista
            if (cortaAristas(grafoGreedyT, auxN, j)) {
                return false;
            } else {
                grafoGreedyT.remove(j);
                grafoGreedyT.add(j, new Arista(punto1, punto2));//Reemplaza la arista anterior
                return true;
            }
        }

    }

    public boolean esFlipeable(Arista e, int j) {
        // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
        Vector puntosF = new Vector();
        Arista auxN;
        Punto punto1 = new Punto(0, 0);
        Punto punto2 = new Punto(0, 0);

        for (int i = 0; i < grafoGreedyT.size(); i++) {
            if (((Arista) grafoGreedyT.get(i)) != e) {
                if ((((Arista) grafoGreedyT.get(i)).obtenerOrig().igualQue(e.obtenerOrig())) || (((Arista) grafoGreedyT.get(i)).obtenerOrig().igualQue(e.obtenerDest()))) {
                    puntosF.addElement(((Arista) grafoGreedyT.get(i)).obtenerDest());
                } else if ((((Arista) grafoGreedyT.get(i)).obtenerDest().igualQue(e.obtenerOrig())) || (((Arista) grafoGreedyT.get(i)).obtenerDest().igualQue(e.obtenerDest()))) {
                    puntosF.addElement(((Arista) grafoGreedyT.get(i)).obtenerOrig());
                }
            }
        }

        punto1.asignar(buscarPunto(puntosF, e)); //Busca el primer punto para flipar
        punto2.asignar(buscarPunto(puntosF, e)); //Busca el segundo

        if ((punto1.igualQue(new Punto(0, 0))) || (punto2.igualQue(new Punto(0, 0))))//Si alguno devolvió el punto (0,0) => no se puede flipar
        {
            return false;
        } else {
            auxN = new Arista(punto1, punto2);//Crea la nueva arista
            if (cortaAristas(grafoGreedyT, auxN, j)) {
                return false;
            } else {
                //grafoGreedyT.remove(j);
                //grafoGreedyT.add(j,new Arista (punto1,punto2));//Reemplaza la arista anterior
                return true;
            }
        }

    }

    public boolean cortaAristas(ArrayList aristas, Arista a, int k) {
        boolean corta = false;
        Arista ar = null;
        double sig1, sig2;
        double corteY;
        double corteX;
        int i = 0;
        Punto p1 = a.obtenerOrig();
        Punto p2 = a.obtenerDest();
        while (!corta && i < aristas.size()) {
            if (i != k) {//Para no comparar con la misma arista antes de flipar
                ar = (Arista) aristas.get(i);
                Punto p = ar.obtenerOrig();
                Punto q = ar.obtenerDest();
                if (((p.igualQue(p1)) && (q.igualQue(p2))) || ((q.igualQue(p1)) && (p.igualQue(p2)))) {//Consulta si la arista flipada ya se encuentra en la lista de aristas
                    corta = true;
                } else {
                    sig1 = Det(p, q, p1);
                    sig2 = Det(p, q, p2);
                    if (((sig1 < 0) && (sig2 > 0)) || ((sig1 > 0) && (sig2 < 0))) {
                        double vx = p2.x - p1.x;
                        double vy = p2.y - p1.y;
                        double wx = q.x - p.x;
                        double wy = q.y - p.y;
                        if (vy == 0) {
                            corteY = (p.x - p1.x + (p1.y * vx) - (p.y * wx / wy)) / ((vx) - (wx / wy));
                            corteX = ((corteY - p1.y) * vx) + p1.x;
                        } else if (wy == 0) {
                            corteY = (p.x - p1.x + (p1.y * vx / vy) - (p.y * wx)) / ((vx / vy) - (wx));
                            corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                        } else {
                            corteY = (p.x - p1.x + (p1.y * vx / vy) - (p.y * wx / wy)) / ((vx / vy) - (wx / wy));
                            corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                        }
                        Punto ptoCorte = new Punto(corteX, corteY);
                        if ((p.distance(ptoCorte) <= p.distance(q)) && (q.distance(ptoCorte) <= q.distance(p))) {
                            corta = true;
                        }
                    }
                    i++;
                }
            } else {
                i++;
            }
        }
        return corta;
    }

    public int identificaArista(Punto v1, Punto v2) {//AGREGADO PARA BUSCAR UNA ARISTA, DADO DOS PUNTOS

        for (int i = 0; i < grafoGreedyT.size(); i++) {
            if ((((Arista) grafoGreedyT.get(i)).obtenerDest().getX() == v1.getX() && ((Arista) grafoGreedyT.get(i)).obtenerDest().getY() == v1.getY())
                    && (((Arista) grafoGreedyT.get(i)).obtenerOrig().getX() == v2.getX() && ((Arista) grafoGreedyT.get(i)).obtenerOrig().getY() == v2.getY())) {
                return i;
            } else if ((((Arista) grafoGreedyT.get(i)).obtenerDest().getX() == v2.getX() && ((Arista) grafoGreedyT.get(i)).obtenerDest().getY() == v2.getY())
                    && (((Arista) grafoGreedyT.get(i)).obtenerOrig().getX() == v1.getX() && ((Arista) grafoGreedyT.get(i)).obtenerOrig().getY() == v1.getY())) {
                return i;
            }

        }
        return -1;
    }

    private Punto buscarPunto(Vector puntosF, Arista a) {
        Punto auxP = new Punto(0, 0);
        while (!puntosF.isEmpty()) {
            auxP.asignar((Punto) puntosF.remove(0));
            if (puntosF.contains(auxP)) {/*AGREGAR LA CONDICIÓN SOBRE TRIÁNGULO VACÍO:
                 * FORMAR EL TRIÁNGULO
                 * RECORRER EL VECTO CON LOS PUNTOS QUE RESTAN PREGUNTANDO SI ESTÁN DENTRO DEL TRIÁNGULO
                 * SI NO SE ENCUENTRA NINGUNO ENTONCES ES UN PUNTO DE FLIPEO
                 * SI SE ENCUENTRA, VOLVER AL WHILE PRINCIPAL
                 */
                Triangulo t = new Triangulo(a.obtenerOrig(), a.obtenerDest(), auxP);
                if (EsTrianguloVacio(t)) {
                    return auxP;
                }
            }
        }
        return new Punto(0, 0);
    }

    public boolean EsTrianguloVacio(Triangulo triang) {
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
        for (int i = 0; i < numPuntos; i++) {
            Punto ptoAux = listaPuntos[i];
            if (-ptoAux.getY() < extSup && -ptoAux.getY() > extInf && ptoAux.getX() < extDer && ptoAux.getX() > extIzq) {
                if (CalculoLado(triang.verticeA, triang.verticeB, ptoAux) > 0) {
                    if (CalculoLado(triang.verticeB, triang.verticeC, ptoAux) > 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) > 0) {
                        return false;
                    }
                } else if (CalculoLado(triang.verticeB, triang.verticeC, ptoAux) < 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public static double CalculoLado(Punto extremo1, Punto extremo2, Punto punto) {
        return (((extremo2.getX() * (-punto.getY()) - (-extremo2.getY()) * punto.getX()) - (extremo1.getX() * (-punto.getY()) - (-extremo1.getY()) * punto.getX())) + (extremo1.getX() * (-extremo2.getY()) - (-extremo1.getY()) * extremo2.getX()));
    }

    public int getNumeroPuntos() {
        return puntosVecinos.size();
    }

    public Punto getPunto(int i) {
        return (Punto) puntosVecinos.get(i);
    }

    public void insertarOrdenadoPunto(Punto vi) // crea una nueva Arista, la introduce en el array de Puntos y aumenta el
    // tamaño de ese array si fuera necesario
    {
        /*Punto [] temp;
        int i=-1,j=0;
        boolean insertado = false;
        
        if(numPuntos >= capacidadPuntos)
        {
        capacidadPuntos *= 2;
        }
        temp = new Punto[capacidadPuntos];
        
        if (numPuntos == 0)
        {
        temp [0] =  (vi);
        }
        else
        {
        for(i = 0; i < numPuntos; i++)
        {
        if ( ((vi.getX() < listaPuntos[i].getX()) ||
        (vi.getX() == listaPuntos[i].getX() &&
        vi.getY() > listaPuntos[i].getY()))  && !insertado)
        {
        temp[j] =  (vi);
        j++;
        insertado = true;
        }
        temp[j] = listaPuntos[i];
        j++;
        }
        }
        if (j == i)
        {
        temp [j] = (vi);
        }
        listaPuntos = temp;*/

        int li = 0, ls = numPuntos - 1;
        int m = (li + ls) / 2;
        if (numPuntos > 0) {
            while (li < ls) {
                if (((vi.getX() < listaPuntos[m].getX())
                        || (vi.getX() == listaPuntos[m].getX()
                        && vi.getY() > listaPuntos[m].getY()))) {
                    ls = m - 1;
                } else {
                    li = m + 1;
                }
                m = (li + ls) / 2;
            }
            if (((vi.getX() < listaPuntos[m].getX())
                        || (vi.getX() == listaPuntos[m].getX()
                        && vi.getY() > listaPuntos[m].getY()))) {
                for (int i = numPuntos; i > m; i--){
                    listaPuntos[i] = listaPuntos[i - 1];
                }
                listaPuntos[m] = vi;
            } else {
                for (int i = numPuntos; i > m + 1; i--){
                    listaPuntos[i] = listaPuntos[i - 1];
                }
                listaPuntos[m + 1] = vi;
            }
            numPuntos++;
            return;
        }
        listaPuntos[0] = vi;
        numPuntos++;
    }

    public boolean yaExistia(Punto v) // Indica si ya existía el vértice "v" en alguna parte de la lista de aristas
    {
        for (int i = 0; i < numPuntos; i++) {
            if (listaPuntos[i].getX() == v.getX()
                    && listaPuntos[i].getY() == v.getY()) {
                return true;
            }
        }
        return false;
    }

    public void agregarVecinos(Arista a) {
        Punto origen = new Punto(0, 0);
        Punto destino = new Punto(0, 0);
        if (!puntosVecinos.contains(a.obtenerOrig())) {
            puntosVecinos.add(a.obtenerOrig());
            //origen.asignar(a.obtenerOrig());
            origen = a.obtenerOrig();
        } else {
            origen = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerOrig()));
        }

        if (!puntosVecinos.contains(a.obtenerDest())) {
            puntosVecinos.add(a.obtenerDest());
            //destino.asignar(a.obtenerDest());
            destino = a.obtenerDest();
        } else {
            destino = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerDest()));
        }

        origen.agregarVecino(destino);
        destino.agregarVecino(origen);

    }
}
