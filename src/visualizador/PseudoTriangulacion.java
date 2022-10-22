/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Pablazo
 */
public class PseudoTriangulacion extends Configuracion {

    final int CANTPUNTOS = 300;
    Punto posPuntos[];
    int caras[][];
    int sol[][];
    double obj;
    int cant_pt_Sol;
    ArrayList<Arista> solAristas;
    int libreF;
    int no_of_nodes;
    int factibles[];
    double dist[][];
    private final int RAND_MAX = 32767;
    int cant_pt;
    int cantAristas;
    Random rnd;

    public void inicializar(Conjunto puntos) {
        caras = new int[1000][CANTPUNTOS + 1];
        sol = new int[CANTPUNTOS][CANTPUNTOS];
        dist = new double[CANTPUNTOS][CANTPUNTOS];
        factibles = new int[CANTPUNTOS + 1];
        rnd = new Random(1L);
        rnd.setSeed((long)System.currentTimeMillis());
        posPuntos = new Punto[CANTPUNTOS];
        solAristas = new ArrayList();
        cantAristas = 0;
        for (int i = 0; i < puntos.size(); i++) {
            posPuntos[i] = new Punto(puntos.get(i));
        }
        no_of_nodes = puntos.size();
        libreF = 1;
        obj = 0.0D;
        cant_pt_Sol = 0;
        Cierre_Convexo(puntos);
    }

    public int inicializar(Conjunto puntos, ArrayList<Arista> a) {
        return 0;
    }

    public int Interseca(Punto a, Punto b, Punto c, Punto d) {
        if (IntersecaPropio(a, b, c, d)) {
            return 1;
        }
        return !Entre(a, b, c) && !Entre(a, b, d) && !Entre(c, d, a) && !Entre(c, d, b) ? 0 : 1;
    }

    public boolean IntersecaPropio(Punto a, Punto b, Punto c, Punto d) {
        if (Colineal(a, b, c) || Colineal(a, b, d) || Colineal(c, d, a) || Colineal(c, d, b)) {
            return false;
        } else {
            return Xor(AlaIzquierda(a, b, c), AlaIzquierda(a, b, d)) && Xor(AlaIzquierda(c, d, a), AlaIzquierda(c, d, b));
        }
    }

    public boolean Xor(boolean x, boolean y) {
        return (!x) ^ (!y);
    }

    boolean AlaIzquierda(Punto a, Punto b, Punto c) {
        return AreaSign(a, b, c) > 0;
    }

    boolean Colineal(Punto a, Punto b, Punto c) {
        return AreaSign(a, b, c) == 0;
    }

    boolean Entre(Punto a, Punto b, Punto c) {
        if (!Colineal(a, b, c)) {
            return false;
        }
        if (a.x != b.x) {
            return a.x <= c.x && c.x <= b.x || a.x >= c.x && c.x >= b.x;
        } else {
            return a.y <= c.y && c.y <= b.y || a.y >= c.y && c.y >= b.y;
        }
    }

    int EsInterior(int f, int j) {
        int Rcross = 0;
        Punto p[] = new Punto[CANTPUNTOS];
        Punto q = new Punto(posPuntos[j]);
        int i;
        for (i = 0; caras[f][i] != -1; i++) {
            p[i] = new Punto( (posPuntos[caras[f][i]].getX() - q.getX()),  (-posPuntos[caras[f][i]].getY() - -q.getY()));//10/8/13 saqué (int) en Punto((int)..., (int)...)
        }

        int n = i;
        for (i = 0; i < n; i++) {
            if (p[i].getX() == 0.0D && -p[i].getY() == 0.0D) {
                return -1;
            }
            int i1 = ((i + n) - 1) % n;
            if ((-p[i].getY() > 0.0D) == (-p[i1].getY() > 0.0D)) {
                continue;
            }
            double x = ((p[i].getX() * -p[i1].getY()) - (p[i1].getX() * -p[i].getY())) / (-p[i1].getY() - -p[i].getY());
            if (x > 0.0D) {
                Rcross++;
            }
        }

        return Rcross % 2 != 1 ? 0 : 1;
    }

    void DividirCara(int f, int pk, int pi, int pj) {
        if (libreF == 152 || libreF == 151){
            System.out.print("");
        }
        int indpi = 0;
        int indpj = 0;
        int j;
        for (j = 0; caras[f][j] != -1; j++) {
            if (caras[f][j] == pi) {
                indpi = j;
            }
            if (caras[f][j] == pj) {
                indpj = j;
            }
        }

        if (indpj < indpi) {
            int aux = indpj;
            indpj = indpi;
            indpi = aux;
            aux = pj;
            pj = pi;
            pi = aux;
        }
        for (j = 0; j <= indpi; j++) {
            caras[libreF][j] = caras[f][j];
        }

        if (pk != -1) {
            caras[libreF][j] = pk;
            posPuntos[pk].cara = -1;
            j++;
        }
        int i;
        for (i = indpj; caras[f][i] != -1; i++) {
            caras[libreF][j] = caras[f][i];
            j++;
        }

        caras[libreF][j] = -1;
        caras[libreF][j + 1] = -f;
        i = 0;
        if (pk != -1) {
            caras[libreF + 1][0] = pk;
            i++;
        }
        for (j = indpi; j <= indpj; j++) {
            caras[libreF + 1][i] = caras[f][j];
            i++;
        }

        caras[libreF + 1][i] = -1;
        caras[libreF + 1][i + 1] = -f;
        for (i = 0; i < no_of_nodes; i++) {
            if (posPuntos[i].cara != f) {
                continue;
            }
            int aux = EsInterior(libreF, i);
            if (aux == 1) {
                posPuntos[i].cara = libreF;
            } else {
                posPuntos[i].cara = libreF + 1;
            }
        }

        libreF = libreF + 2;
    }

    double angulo(Nodo1 p1, Nodo1 p2, Nodo1 p3, Nodo1 p4) {
        double u[] = new double[2];
        double v[] = new double[2];
        u[0] = p2.x - p1.x;
        u[1] = p2.y - p1.y;
        v[0] = p4.x - p3.x;
        v[1] = p4.y - p3.y;
        double m1 = Math.sqrt(u[0] * u[0] + u[1] * u[1]);
        double m2 = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
        return Math.acos((u[0] * v[0] + u[1] * v[1]) / (m1 * m2));
    }

    void Cierre_Convexo(Conjunto puntos) {
        ArrayList<Integer> pos = new ConvexHull(puntos).getCierre();
        int j;
        for (j = 0; j < pos.size(); j++) {
            caras[0][j] = pos.get(j);
            posPuntos[pos.get(j)].cara = -1;
            //pos = (pos + 1) % canp;
        }

        caras[0][j] = -1;
    }

    void ParticionarCara(int f) {
    }

    void ConstruyeSolucion() {
        for (int i = 0; i < no_of_nodes; i++) {
            for (int j = 0; j < no_of_nodes; j++) {
                sol[i][j] = 0;
            }

        }

        for (int i = 1; i < 1000; i++) {
            for (int j = 0; j < no_of_nodes; j++) {
                caras[i][j] = 0;
            }

        }

        libreF = 1;
        for (int i = 0; i < no_of_nodes; i++) {
            posPuntos[i].cara = 0;
        }

        for (int i = 0; caras[0][i] != -1; i++) {
            posPuntos[caras[0][i]].cara = -1;
        }

        cant_pt = 0;
        int i = 0;
        try{
            for (i = 0; i < libreF; i++) {
                if (PTrVacioPuntosInteriores(i) != 0) {
                    UnirEnSol(i);
                    cant_pt++;
                } else {
                    ParticionarCara(i);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.print(e.getMessage() + " - - " + i);
        }
    }

    int PTrVacioPuntosInteriores(int f) {
        for (int i = 0; i < no_of_nodes; i++) {
            if (posPuntos[i].cara == f) {
                return 0;
            }
        }

        return VerCaraEsPTr(f);
    }

    int VerCaraEsPTr(int f) {
        int convexos = 0;
        for (int j = 0; caras[f][j] != -1; j++) {
            int p1 = caras[f][j];
            int p2;
            int p3;
            if (caras[f][j + 1] != -1) {
                p2 = caras[f][j + 1];
                if (caras[f][j + 2] != -1) {
                    p3 = caras[f][j + 2];
                } else {
                    p3 = caras[f][0];
                }
            } else {
                p2 = caras[f][0];
                p3 = caras[f][1];
            }
            double as = areasignada(posPuntos[p1], posPuntos[p3], posPuntos[p2]);
            if (as < 0.0D) {
                convexos++;
            }
        }

        return (convexos != 3)? 0 : 1;
    }

    void UnirEnSol(int f) {
        for (int j = 0; caras[f][j] != -1; j++) {
            int pi = caras[f][j];
            int pj;
            if (caras[f][j + 1] != -1) {
                pj = caras[f][j + 1];
            } else {
                pj = caras[f][0];
            }
            if (sol[pi][pj] != 1) {
                sol[pi][pj] = 1;
                sol[pj][pi] = 1;
                cantAristas++;
            }
        }
    }

    double CalcularPeso() {
        double peso = 0.0D;
        for (int i = 0; i < no_of_nodes; i++) {
            for (int j = i; j < no_of_nodes; j++) {
                if (sol[i][j] == 1) {
                    peso += posPuntos[i].distance(posPuntos[j]);
                }
            }

        }
        return peso;
    }

    int CalcularFactibles(int f, int pk, int p) {
        int l;
        for (l = 0; l < no_of_nodes; l++) {
            factibles[l] = 0;
        }

        l = 0;
        if (posPuntos[pk].cara != -1) {
            for (int i = 0; caras[f][i] != -1; i++) {
                int pi = caras[f][i];
                int interseco = 0;
                for (int j = 0; caras[f][j] != -1 && interseco != 1 && pi != p; j++) {
                    int po = caras[f][j];
                    int pf;
                    if (caras[f][j + 1] != -1) {
                        pf = caras[f][j + 1];
                    } else {
                        pf = caras[f][0];
                    }
                    if (pi != po && pi != pf && pi != p && Interseca(posPuntos[pi], posPuntos[pk], posPuntos[po], posPuntos[pf]) != 0) {
                        interseco = 1;
                    }
                }

                if (interseco == 0 && pi != p) {
                    factibles[l] = pi;
                    l++;
                }
            }

            factibles[l] = -1;
        }
        if (posPuntos[pk].cara == -1) {
            int i;
            for (i = 0; caras[f][i] != pk && caras[f][i] != -1; i++);
            int indpk = i;
            int antpk;
            if (i != 0) {
                antpk = i - 1;
            } else {
                for (; caras[f][i] != -1; i++);
                antpk = i - 1;
            }
            int postpk;
            if (caras[f][indpk + 1] == -1) {
                postpk = 0;
            } else {
                postpk = indpk + 1;
            }
            for (i = 0; caras[f][i] != -1; i++) {
                int pi = caras[f][i];
                if (pi == pk || pi == caras[f][antpk] || pi == caras[f][postpk] || pi == p) {
                    continue;
                }
                int interseco = 0;
                for (int j = 0; caras[f][j] != -1 && interseco != 1; j++) {
                    int po = caras[f][j];
                    int pf;
                    if (caras[f][j + 1] != -1) {
                        pf = caras[f][j + 1];
                    } else {
                        pf = caras[f][0];
                    }
                    if (pi != po && pi != pf && pk != po && pk != pf && Interseca(posPuntos[pk], posPuntos[pi], posPuntos[po], posPuntos[pf]) != 0) {
                        interseco = 1;
                    }
                }

                if (interseco != 0) {
                    continue;
                }
                double det1 = areasignada(posPuntos[caras[f][antpk]], posPuntos[pk], posPuntos[pi]);
                double det2 = areasignada(posPuntos[pk], posPuntos[caras[f][postpk]], posPuntos[pi]);
                if (det1 > 0.0D && det2 > 0.0D) {
                    factibles[l] = pi;
                    l++;
                }
            }

            factibles[l] = -1;
        }
        return l != 0 ? 1 : 0;
    }

    double areasignada(Punto pu1, Punto pu2, Punto pu3) {
        double salida = (pu1.x * pu2.y + pu2.x * pu3.y + pu3.x * pu1.y) - (pu1.x * pu3.y + pu2.x * pu1.y + pu3.x * pu2.y);
        return 0.5D * salida;
    }

    int AreaSign(Punto a, Punto b, Punto c) {
        double area2 = (double) (b.x - a.x) * (double) (c.y - a.y) - (double) (c.x - a.x) * (double) (b.y - a.y);
        if (area2 > 0.5D) {
            return 1;
        }
        return area2 >= -0.5D ? 0 : -1;
    }

    double areasignadaCC(Nodo1 pu1, Nodo1 pu2, Nodo1 pu3) {
        double salida = (pu1.x * pu2.y + pu2.x * pu3.y + pu3.x * pu1.y) - (pu1.x * pu3.y + pu2.x * pu1.y + pu3.x * pu2.y);
        return 0.5D * salida;
    }

    float random_c() {
        float d;
        do {
            d = rnd.nextFloat();
        } while ((double) d == 1.0D);
        return d;
    }

    public int cantidadAristas() {
        return cantAristas;
    }

    void generarAristas() {
        for (int i = 0; i < no_of_nodes; i++) {
            for (int j = i; j < no_of_nodes; j++) {
                if (sol[i][j] == 1) {
                    solAristas.add(new Arista(posPuntos[i], posPuntos[j]));
                }
            }
        }
    }

    public Arista getArista(int i) {
        return (Arista) solAristas.get(i);
    }

    public int cantPTriangulos() {
        return cant_pt_Sol;
    }

    public int getNumeroPuntos() {
        return no_of_nodes;
    }

    public void agregarVecinos(Arista a) {
        a.obtenerOrig().agregarVecino(a.obtenerDest());
        a.obtenerDest().agregarVecino(a.obtenerOrig());
    }

    public Punto getPunto(int i) {
        return posPuntos[i];
    }

    public boolean contieneArista(Arista a) {
        for (int i = 0; i < solAristas.size(); i++) {
            if ((solAristas.get(i).obtenerOrig().igualQue(a.obtenerOrig()) && solAristas.get(i).obtenerDest().igualQue(a.obtenerDest()))
                    || (solAristas.get(i).obtenerOrig().igualQue(a.obtenerDest()) && solAristas.get(i).obtenerDest().igualQue(a.obtenerOrig()))) {
                return true;
            }
        }
        return false;
    }

    public int obtenerNumAristas() {
        return cantAristas;
    }

    public Arista obtenerEdge(int i) {
        return (Arista) solAristas.get(i);
    }
}
