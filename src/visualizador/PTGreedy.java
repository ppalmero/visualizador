/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.util.ArrayList;

/**
 *
 * @author Pablazo
 */
public class PTGreedy extends PseudoTriangulacion {

    private final int INFINITY = 100000000;

    public void inicializar(Conjunto puntos) {
        if (puntos.size() > 2){
            super.inicializar(puntos);
            ConstruyeSolucion();
            obj = CalcularPeso();
            cant_pt_Sol = cant_pt;
            generarAristas();
        }
    }

    void ParticionarCara(int f) {
        int ph, hayfact;
        int pi = 0;
        int pj = 0;
        int pk = 0;
        int p_mejor_greedy_1, p_mejor_greedy_2, pk_mejor_greedy, p_mejor_1, p_mejor_2;
        int i, j, h, l;
        double dist_mejor_greedy_1, dist_mejor_greedy_2, dist_mejor_1, dist_mejor_2, dist_act;
        int[] aux = new int[no_of_nodes];
        ArrayList<Nodo1> Pares_dist = new ArrayList();

        // recupero todos los puntos interiores de la cara F en aux
        i = 0;
        for (j = 0; j < no_of_nodes; j++) {
            if (posPuntos[j].cara == f) {
                aux[i] = j; // j-numero de punto
                i++; // cuento cuantos hay en aux
            }
        }

// PUNTOS INTERIORES
        if (i != 0) // indica que hay puntos interiores
        {
            // Sigo: hay puntos interiores, hay que elegir el que mejor peso tenga
            dist_mejor_greedy_1 = INFINITY;
            dist_mejor_greedy_2 = INFINITY;
            p_mejor_greedy_1 = -1;
            p_mejor_greedy_2 = -1;
            pk_mejor_greedy = -1;

            // para todo punto interior
            for (j = 0; j < i; j++) {
                // tomo un punto interior
                pk = aux[j];

                // calcular los factibles de pk en esa cara
                hayfact = CalcularFactibles(f, pk, -1);

                for (h = 0; (factibles[h] != -1); h++) {
                    ph = factibles[h];
                    Pares_dist.add(h, new Nodo1(dist[pk][ph], ph));
                    /*Pares_dist.get(h).setAngulo(dist[pk][ph]);
                    Pares_dist.get(h).setID(ph);*/
                }
                Pares_dist.add(h, new Nodo1(-1, -1));
                /*Pares_dist.get(h).setAngulo(-1);
                Pares_dist.get(h).setID(-1);*/

                for (l = 0; ((hayfact != 0) && (l < h)); l++)//(Pares_dist.get(l+1).getAngulo() != -1)); l++)
                {
                    if (Pares_dist.get(l).getAngulo() > Pares_dist.get(l + 1).getAngulo()) {
                        Pares_dist.add(h + 1, new Nodo1(Pares_dist.get(l).getAngulo(), Pares_dist.get(l).getID()));
                        /*Pares_dist.get(h+1).setAngulo(Pares_dist.get(l).getAngulo());
                        Pares_dist.get(h+1).setID(Pares_dist.get(l).getID());*/
                        Pares_dist.get(l).setAngulo(Pares_dist.get(l + 1).getAngulo());
                        Pares_dist.get(l).setID(Pares_dist.get(l + 1).getID());
                        Pares_dist.get(l + 1).setAngulo(Pares_dist.get(h + 1).getAngulo());
                        Pares_dist.get(l + 1).setID(Pares_dist.get(h + 1).getID());
                        l = -1;
                    }
                }

                // selecionar las dos mejores para el punto pk. ***CAMBIÉ 1 POR 0 Y 2 POR 1***
                if ((Pares_dist.get(0).getAngulo() + Pares_dist.get(1).getAngulo()) < (dist_mejor_greedy_1 + dist_mejor_greedy_2)) {
                    dist_mejor_greedy_1 = Pares_dist.get(0).getAngulo();
                    dist_mejor_greedy_2 = Pares_dist.get(1).getAngulo();
                    p_mejor_greedy_1 = Pares_dist.get(0).getID();
                    p_mejor_greedy_2 = Pares_dist.get(1).getID();
                    pk_mejor_greedy = pk;
                }
            }  // for

// actualizo los parametros para dividir la cara
            pk = pk_mejor_greedy;
            pi = p_mejor_greedy_1;
            pj = p_mejor_greedy_2;

        } //( i!= 0)

// hasta acá - caso: hay puntos interiores

        /********************************************************************************/
// PUNTOS DEL BORDE
//	i--; // me queda la cantidad exacta de puntos interiores
// si no hay puntos interiores,
        if (i == 0) {
            // recupero todos los puntos del borde de la cara en aux
            j = 0;
            while (caras[f][j] != -1) {
                aux[i] = caras[f][j]; // nº de punto indicado en la cara F en la posición j del borde
                j++;
                i++; // cuento cuantos hay en aux
            }


            // hay que elegir el que mejor peso tenga
            dist_mejor_greedy_1 = INFINITY;
            p_mejor_greedy_1 = -1;
            pk_mejor_greedy = -1; // constante

            for (j = 0; j < i; j++) {
                // tomo un punto del borde
                pk = aux[j];

                // calcular los factibles de pk en esa cara
                hayfact = CalcularFactibles(f, pk, -1);

                if (hayfact != 0) {
                    // para todos los puntos del borde elijo los dos mas cercanos
                    dist_mejor_1 = INFINITY;
                    p_mejor_1 = -1;

                    // para todos los factibles de pk, busco la menor distancia
                    for (h = 0; (factibles[h] != -1); h++) {
                        ph = factibles[h];
                        dist_act = dist[pk][ph];
                        if (dist_act < dist_mejor_1) {
                            dist_mejor_1 = dist_act;
                            p_mejor_1 = ph;
                        }
                    }

                    // veo si lo mejor del punto pk es mejor que lo conseguido por otros puntos del borde
                    if (dist_mejor_1 < dist_mejor_greedy_1) {
                        dist_mejor_greedy_1 = dist_mejor_1;
                        p_mejor_greedy_1 = p_mejor_1;
                        pk_mejor_greedy = pk;
                    }
                }// hayfact
            } // for
            // actualizo los parametros para dividir la cara
            pk = -1;
            pi = p_mejor_greedy_1;
            pj = pk_mejor_greedy;
        } // if i=0
// hasta acá - caso: no hay puntos interiores

        /************************************/
// dividir la cara
        DividirCara(f, pk, pi, pj);

// LibreF ya se actualizo en dividircara
// notar que en caras me queda la historia completa de divisiones realizadas.
    }
}
