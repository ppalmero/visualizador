/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TMA3SUM;

import java.util.ArrayList;
import visualizador.Punto;
import visualizador.Arista;
import visualizador.Conjunto;

/**
 *
 * @author Pablo
 */
public class TMA {

    /**
     * @param args the command line arguments
     */
    static Punto p1 = new Punto(3, 3);
    static Punto p2 = new Punto(0, 0);
    static Punto p3 = new Punto(5, 2);
    static Punto p4 = new Punto(1, 6);
    ArrayList<Arista> aristas = new ArrayList<Arista>();
    ArrayList<Arista> aristasOrd = new ArrayList<Arista>();
    ArrayList<Integer> resultado = new ArrayList<Integer>();
    ArrayList<Arista> aristasRes;
    Conjunto puntos;

    public ArrayList<Arista> getResultado() {
        //ArrayList<Arista> aristasBruta = new ArrayList<Arista>();
        aristasRes = new ArrayList<Arista>();
        aristasRes.add(aristasOrd.get(resultado.get(0)));
        aristasRes.add(aristasOrd.get(resultado.get(1)));
        aristasRes.add(aristasOrd.get(resultado.get(2)));
        /*aristasBruta = calcularAreaBruta();
        aristasRes.add(aristasBruta.get(0));
        aristasRes.add(aristasBruta.get(1));
        aristasRes.add(aristasBruta.get(2));*/
        return aristasRes;
    }

    public TMA(Conjunto puntos) {
        generarAristas(puntos);
        ordenarAristas();
        aplicar3SUM();
    }

    public static void main(String[] args) {
        // TODO code application logic here


        /*generarAristas();
        ordenarAristas();
        aplicar3SUM();*/

    }

    private void generarAristas(Conjunto p) {
        Arista a;
        puntos = p;
        for (int i = 0; i < puntos.size() - 1; i++){
            for (int j = i + 1; j < puntos.size(); j++){
                a = new Arista(puntos.get(i), puntos.get(j));
                a.setPendiente();
                aristas.add(a);
            }
        }
        /*a = new Arista(p1, p2);
        a.setPendiente();
        aristas.add(a);
        a = new Arista(p1, p3);
        a.setPendiente();
        aristas.add(a);
        a = new Arista(p1, p4);
        a.setPendiente();
        aristas.add(a);
        a = new Arista(p2, p3);
        a.setPendiente();
        aristas.add(a);
        a = new Arista(p2, p4);
        a.setPendiente();
        aristas.add(a);
        a = new Arista(p3, p4);
        a.setPendiente();
        aristas.add(a);*/
    }

    private void ordenarAristas() {
        float menorX;
        int i = 0;
        int posMenorX = -1;
        menorX = Integer.MAX_VALUE;//aristas.get(i).getPendiente();

        while (i < aristas.size()) {
            if (menorX > aristas.get(i).getPendiente()) {
                menorX = aristas.get(i).getPendiente();
                posMenorX = i;
            }
            i++;
            if (i == aristas.size()) {
                i = 0;
                aristasOrd.add(aristas.get(posMenorX));
                aristas.remove(posMenorX);
                posMenorX = 0;
                menorX = Integer.MAX_VALUE;//aristas.get(i).getPendiente();
            }
        }
    }

    private void aplicar3SUM() {
        int i, j = 0, k = 0, f;
        float menorArea = Integer.MAX_VALUE, si, sj, sk, sTotal, sProm, area;
        boolean colinear = false;

        for (i = 0; i < aristasOrd.size() - 2; i++) {
            j = i + 1;
            k = aristasOrd.size() - 1;
            si = (aristasOrd.get(i).getPendiente());
            while (k > j) {
                sj = (aristasOrd.get(j).getPendiente());//abs
                sk = (aristasOrd.get(k).getPendiente());//abs
                if ((f = formanTriangulo(aristasOrd.get(i), aristasOrd.get(j), aristasOrd.get(k))) == 5) {
                    area = calcularArea(aristasOrd.get(i), aristasOrd.get(j));
                    if (area == 0){
                        System.out.println("Los tres puntos son colineales");
                    }
                    if (menorArea > area) {
                        menorArea = area;
                        resultado = new ArrayList<Integer>();
                        resultado.add(i);
                        resultado.add(j);
                        resultado.add(k);
                    }
                    if (aristasOrd.get(j).longitud() > aristasOrd.get(k).longitud()){//angulo(aristasOrd.get(i), aristasOrd.get(j)) < angulo(aristasOrd.get(i), aristasOrd.get(k))) {
                        k--;
                    } else {
                        j++;
                    }
                } else if (f == 1) {
                    k--;
                } else if (f == 2) {
                    j++;
                } else if (f == 3) {
                    if (calcularArea(aristasOrd.get(i), aristasOrd.get(j)) > calcularArea(aristasOrd.get(i), aristasOrd.get(k))){//(Math.abs(si - sj) > Math.abs(si - sk)) {
                        j++;
                    } else {
                        k--;
                    }
                } else {
                    k--;
                    j++;
                }
            }
        }
        
        System.out.println("La menor área 3sum es: " + menorArea);
    }

    private static int formanTriangulo(Arista a1, Arista a2, Arista a3) {
        int resultado = 0;
        /****FORMAN TRIÁNGULO****/
        if ((a1.obtenerOrig().equals(a2.obtenerOrig()))
                && ((a1.obtenerDest().equals(a3.obtenerOrig()) && (a2.obtenerDest().equals(a3.obtenerDest())))
                || (a1.obtenerDest().equals(a3.obtenerDest()) && (a2.obtenerDest().equals(a3.obtenerOrig()))))) {
            resultado = 5;
        } else if ((a1.obtenerOrig().equals(a2.obtenerDest()))
                && ((a1.obtenerDest().equals(a3.obtenerOrig()) && (a2.obtenerOrig().equals(a3.obtenerDest())))
                || (a1.obtenerDest().equals(a3.obtenerDest()) && (a2.obtenerOrig().equals(a3.obtenerOrig()))))) {
            resultado = 5;
        } else if ((a1.obtenerDest().equals(a2.obtenerDest()))
                && ((a1.obtenerOrig().equals(a3.obtenerOrig()) && (a2.obtenerOrig().equals(a3.obtenerDest())))
                || (a1.obtenerOrig().equals(a3.obtenerDest()) && (a2.obtenerOrig().equals(a3.obtenerOrig()))))) {
            resultado = 5;
        } else if ((a1.obtenerDest().equals(a2.obtenerOrig()))
                && ((a1.obtenerOrig().equals(a3.obtenerOrig()) && (a2.obtenerDest().equals(a3.obtenerDest())))
                || (a1.obtenerOrig().equals(a3.obtenerDest()) && (a2.obtenerDest().equals(a3.obtenerOrig()))))) {
            resultado = 5;
        } else {
            /****BUSCA ADYASCENTE****/
            if ((a1.obtenerOrig().equals(a2.obtenerOrig())) || (a1.obtenerOrig().equals(a2.obtenerDest()))
                    || (a1.obtenerDest().equals(a2.obtenerOrig())) || (a1.obtenerDest().equals(a2.obtenerDest()))) {
                resultado = 1;
            }
            if ((a1.obtenerOrig().equals(a3.obtenerOrig())) || (a1.obtenerOrig().equals(a3.obtenerDest()))
                    || (a1.obtenerDest().equals(a3.obtenerOrig())) || (a1.obtenerDest().equals(a3.obtenerDest()))) {
                /****BUSCA ADYASCENTE****/
                resultado += 2;
            }
        }

        return resultado;
    }

    private static float calcularArea(Arista a1, Arista a2) {
        float area;
        if (a1.obtenerOrig().igualQue(a2.obtenerOrig()) || a1.obtenerDest().igualQue(a2.obtenerOrig())) {
            area = calcularArea(a1.obtenerOrig(), a1.obtenerDest(), a2.obtenerDest());
        } else {
            area = calcularArea(a1.obtenerOrig(), a1.obtenerDest(), a2.obtenerOrig());
        }
        return area;
    }

    private static float calcularArea(Punto a, Punto b, Punto ab) {
        return Math.abs((float)(((a.x - ab.x) * (b.y - ab.y)) - ((b.x - ab.x) * (a.y - ab.y))) / 2);
    }

    private ArrayList<Arista> calcularAreaBruta() {
        float areaMenor = Integer.MAX_VALUE, area;
        ArrayList<Arista> resultadoBruto = new ArrayList<Arista>();
        int pi = 0, pj = 0, pk = 0;
        for (int i = 0; i < puntos.size() - 2; i++){
            for (int j = i + 1; j < puntos.size() - 1; j++){
                for (int k = j + 1; k < puntos.size(); k++){
                    area = calcularArea(puntos.get(i), puntos.get(j), puntos.get(k));
                    if (areaMenor > area){
                        areaMenor = area;
                        resultadoBruto = new ArrayList<Arista>();
                        resultadoBruto.add(new Arista(puntos.get(i), puntos.get(j)));
                        resultadoBruto.add(new Arista(puntos.get(i), puntos.get(k)));
                        resultadoBruto.add(new Arista(puntos.get(k), puntos.get(j)));
                        pi = i;
                        pj = j;
                        pk = k;
                    }
                }
            }
        }
        System.out.println("La menor área bruta es: " + areaMenor + ". Con los puntos: " + pi + ", " + pj + ", " + pk);
        return (resultadoBruto);
    }
}
