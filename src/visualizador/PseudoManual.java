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
public class PseudoManual extends PseudoTriangulacion {
    //private final int RAND_MAX = 32767;

    ArrayList<Arista> aristas = new ArrayList();
    private boolean error = false;

    public int inicializar(Conjunto puntos, ArrayList<Arista> a) {
        super.inicializar(puntos);
        aristas = a;
       
        removerAristasCasco();
        if (error) {
            return -1;
        }
        ConstruyeSolucion();
        if (aristas.isEmpty() && !error) {
            obj = CalcularPeso();
            cant_pt_Sol = cant_pt;
            generarAristas();
            return 1;
        } else {
            return 0;
        }

    }

    boolean DividirCaraVacia(int f, int pk, int pi, int pj) {
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

        for (j = 0; caras[f][j] != -1; j++) {
            if (EsInterior(libreF, caras[f][j]) == 1 || EsInterior(libreF + 1, caras[f][j]) == 1) {
                break;
            }
        }

        if (caras[f][j] != -1) {
            return false;
        }

        libreF = libreF + 2;
        return true;
    }

    void ParticionarCara(int f) {
        int pi = -1;
        int pj = -1;
        int pk = 0;
        int aux[] = new int[210];
        int i = 0, hayfact, cantfact, j, h = 0, l = 0, k;
        // recupero todos los puntos interiores de la cara f en aux
        for (j = 0; j < no_of_nodes; j++) {
            if (posPuntos[j].cara == f) {
                aux[i] = j; // j-numero de punto
                i++; // cuento cuantos hay en aux
            }
        }
// PUNTOS INTERIORES: elegir de a uno, luego elegir uno del borde y fijarse si la arista existe, si existe, elegir otro del borde
        //si ambas aristas existe entonces dividir cara
        //si no, elegir el punto siguiente.
        j = 0;

        if (i != 0)// indica que hay puntos interiores
        {
            for (k = 0; k < i; k++) {
                //int k = (int)(random_c() * (float)i);//suponiendo que devuelve valores entre 0 e i-1
                pk = aux[k];
                // calcular los factibles de pk en esa cara
                hayfact = CalcularFactibles(f, pk, -1);
                for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
                if (hayfact != 0) {
                    int arista1 = -1;
                    for (l = 0; factibles[l] != -1; l++) {
                        if ((arista1 = existe_arista(posPuntos[pk], posPuntos[factibles[l]], -1)) != -1) {
                            pi = factibles[l];
                            break;
                        }
                    }
                    if (factibles[l] == -1) {
                        error = true;
                    } else {
                        for (int fact = l + 1; factibles[fact] != -1; fact++) {
                            if ((existe_arista(posPuntos[pk], posPuntos[factibles[fact]], arista1)) != -1) {
                                pj = factibles[fact];
                                break;
                            }
                        }
                    }
                }
                /*pi = factibles[h];
                pj = factibles[j];*/
                if (pi != -1 && pj != -1) {
                    error = false;//3/10/12
                    break;
                }

            }

            if (!error) {
                DividirCara(f, pk, pi, pj);
            }
            // hasta acá - caso: hay puntos interiores
        } else if (i == 0) {
            // PUNTOS DEL BORDE
            //	i--; // me queda la cantidad exacta de puntos interiores
            pk = -1;// pk es -1 ya que no hay punto interior
            // recupero todos los puntos del borde de la cara en aux
            for (j = 0; caras[f][j] != -1;) {
                aux[i] = caras[f][j];// nº de punto indicado en la cara f en la posición j del borde
                j++;
                i++;// cuento cuantos hay en aux
            }

            /*for(hayfact = 0; hayfact == 0; hayfact = CalcularFactibles(f, pk, -1))// ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            
            // calcular los factibles de pk en esa cara
            // tomo un punto del borde
            {// ELEGIR ALEATORIAMENTE UN pi BORDE
            int k = (int)(random_c() * (float)i);
            pk = aux[k];
            }*/
            for (k = 0; k < i; k++) {
                pk = aux[k];
                hayfact = CalcularFactibles(f, pk, -1);
                for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
                if (hayfact != 0) {
                    //double dMayor = -1;
                    for (l = 0; factibles[l] != -1; l++) {
                        if (existe_arista(posPuntos[pk], posPuntos[factibles[l]], -2) != -1) {
                            pi = factibles[l];
                            pj = pk;
                            pk = -1;
                            if (DividirCaraVacia(f, pk, pi, pj)) {
                                break;
                            } else {
                                pk = pj;
                                aristas.add(new Arista(posPuntos[pk], posPuntos[factibles[l]]));
                            }
                        }
                    }
                    if (factibles[l] != -1) {
                        break;
                    }

                }
                //pi = factibles[h];
                //pj = factibles[j];

            }
            if (k == i) {
                error = true;
            }
            /*hayfact = 0;
            while (hayfact == 0)
            {
            // ELEGIR ALEATORIAMENTE UN pi BORDE
            k = (int)(random_c() * (float)i); //suponiendo que devuelve valores entre 0 e i-1
            pk = aux[k];
            
            // ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            
            // calcular los factibles de pk en esa cara
            // tomo un punto del borde
            
            hayfact = CalcularFactibles( f, pk, -1);
            }
            
            // contar los factibles
            for(cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if(hayfact != 0)
            j = (int)(random_c() * (float)cantfact);
            pj = factibles[j];
            pi = pk;
            pk = -1;*/

        }
        // hasta acá - caso: no hay puntos interiores

        /************************************/
        //dividir la cara
        // LibreF ya se actualizo en dividircara , notar que en caras me queda la historia completa de divisiones realizadas.
    }

    boolean ParticionarPTr(int f) {
        int pi = -1;
        int pj = -1;
        int pk = 0;
        int aux[] = new int[210];
        int i = 0, hayfact, cantfact, j, h = 0, l = 0, k;
        // recupero todos los puntos interiores de la cara f en aux
        /*for(j = 0; j < no_of_nodes; j++)
        if(posPuntos[j].cara == f)
        {
        aux[i]= j; // j-numero de punto
        i++; // cuento cuantos hay en aux
        }
        // PUNTOS INTERIORES: elegir de a uno, luego elegir uno del borde y fijarse si la arista existe, si existe, elegir otro del borde
        //si ambas aristas existe entonces dividir cara
        //si no, elegir el punto siguiente.
        j = 0;
        
        if(i != 0)// indica que hay puntos interiores
        {
        for (k = 0; k < i; k++){
        //int k = (int)(random_c() * (float)i);//suponiendo que devuelve valores entre 0 e i-1
        pk = aux[k];
        // calcular los factibles de pk en esa cara
        hayfact = CalcularFactibles(f, pk, -1);
        for(cantfact = 0; factibles[cantfact] != -1; cantfact++);
        if(hayfact != 0){
        //double dMayor = -1;
        for (l = 0; factibles[l] != -1; l++){
        if (existe_arista (posPuntos [pk], posPuntos [factibles[l]])){
        pi = factibles[l];
        break;
        }
        }
        for(int fact = l+1; factibles[fact] != -1; fact++){
        if (existe_arista (posPuntos [pk], posPuntos [fact])){
        pj = factibles[fact];
        break;
        }
        }
        }
        /*pi = factibles[h];
        pj = factibles[j];
        if (pi != -1 && pj != -1){
        break;
        }
        }
        
        }
        // hasta acá - caso: hay puntos interiores
        
        /********************************************************************************/

        // PUNTOS DEL BORDE

        //	i--; // me queda la cantidad exacta de puntos interiores
        if (i == 0) {
            pk = -1;// pk es -1 ya que no hay punto interior
            // recupero todos los puntos del borde de la cara en aux
            for (j = 0; caras[f][j] != -1;) {
                aux[i] = caras[f][j];// nº de punto indicado en la cara f en la posición j del borde
                j++;
                i++;// cuento cuantos hay en aux
            }

            /*for(hayfact = 0; hayfact == 0; hayfact = CalcularFactibles(f, pk, -1))// ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            
            // calcular los factibles de pk en esa cara
            // tomo un punto del borde
            {// ELEGIR ALEATORIAMENTE UN pi BORDE
            int k = (int)(random_c() * (float)i);
            pk = aux[k];
            }*/
            for (k = 0; k < i; k++) {
                pk = aux[k];
                hayfact = CalcularFactiblesPTr(f, pk, -1);
                for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
                if (hayfact != 0) {
                    //double dMayor = -1;
                    for (l = 0; factibles[l] != -1; l++) {
                        if (existe_arista(posPuntos[pk], posPuntos[factibles[l]], -2) != -1) {
                            pi = factibles[l];
                            pj = pk;
                            pk = -1;
                            if (DividirCaraVacia(f, pk, pi, pj)) {
                                pi = -1; //agregado 17/11
                                break;
                            } else {
                                pk = pj;
                                aristas.add(new Arista(posPuntos[pk], posPuntos[factibles[l]]));
                            }
                        }
                    }
                    if (factibles[l] != -1) {
                        break;
                    }

                }
                //pi = factibles[h];
                //pj = factibles[j];
                pk = -1;
            }

            /*hayfact = 0;
            while (hayfact == 0)
            {
            // ELEGIR ALEATORIAMENTE UN pi BORDE
            k = (int)(random_c() * (float)i); //suponiendo que devuelve valores entre 0 e i-1
            pk = aux[k];
            
            // ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            
            // calcular los factibles de pk en esa cara
            // tomo un punto del borde
            
            hayfact = CalcularFactibles( f, pk, -1);
            }
            
            // contar los factibles
            for(cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if(hayfact != 0)
            j = (int)(random_c() * (float)cantfact);
            pj = factibles[j];
            pi = pk;
            pk = -1;*/

        }
        // hasta acá - caso: no hay puntos interiores

        /************************************/
        //dividir la cara
        if (pi != -1) {
            DividirCara(f, pk, pi, pj);
            return true;
        } else {
            return false;
        }

        // LibreF ya se actualizo en dividircara , notar que en caras me queda la historia completa de divisiones realizadas.
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
        for (int i = 0; i < libreF; i++) {
            if (PTrVacioPuntosInteriores(i) != 0) {
                if (!ParticionarPTr(i)) {
                    UnirEnSol(i);
                    cant_pt++;
                }
                if (error) {
                    break;
                }
            } else {
                ParticionarCara(i);

                if (error) {
                    break;
                }
            }
        }
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
                if (det1 > 0.0D || det2 > 0.0D) //5/10 por error en ejemplo que manda oli
                {
                    factibles[l] = pi;
                    l++;
                }
            }

            factibles[l] = -1;
        }
        return l != 0 ? 1 : 0;
    }

    int CalcularFactiblesPTr(int f, int pk, int p) {
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

    private int existe_arista(Punto punto, Punto punto0, int p) {
        for (int cont = 0; cont < aristas.size(); cont++) {
            if (((aristas.get(cont).obtenerOrig().igualQue(punto)) && (aristas.get(cont).obtenerDest().igualQue(punto0)))
                    || ((aristas.get(cont).obtenerOrig().igualQue(punto0)) && (aristas.get(cont).obtenerDest().igualQue(punto)))) {
                switch (p) {
                    case -1:
                        p = cont;
                        break;
                    case -2:
                        aristas.remove(cont);
                        break;
                    default: {
                        aristas.remove(cont);
                        if (cont < p) {
                            aristas.remove(p - 1);
                        } else {
                            aristas.remove(p);
                        }
                    }
                }
                return p;
            }

        }
        return -1;
    }

    private void removerAristasCasco() {
        int i, cont, cantA = 0;
        for (i = 0; caras[0][i + 1] != -1; i++) {
            for (cont = 0; cont < aristas.size(); cont++) {
                if (((aristas.get(cont).obtenerOrig().igualQue(posPuntos[caras[0][i]]) && aristas.get(cont).obtenerDest().igualQue(posPuntos[caras[0][i + 1]])))
                        || ((aristas.get(cont).obtenerOrig().igualQue(posPuntos[caras[0][i + 1]])) && (aristas.get(cont).obtenerDest().igualQue(posPuntos[caras[0][i]])))) {
                    aristas.remove(cont);
                    cont--;
                    cantA++;
                    break;
                }
            }
            if (cont == aristas.size()) {
                error = true;
                break;
            }
        }
        for (cont = 0; cont < aristas.size(); cont++) {
            if (((aristas.get(cont).obtenerOrig().igualQue(posPuntos[caras[0][i]]) && aristas.get(cont).obtenerDest().igualQue(posPuntos[caras[0][0]])))
                    || ((aristas.get(cont).obtenerOrig().igualQue(posPuntos[caras[0][0]])) && (aristas.get(cont).obtenerDest().igualQue(posPuntos[caras[0][i]])))) {
                aristas.remove(cont);
                cantA++;
                break;
            }
        }
        if (cantA != i + 1) {
            error = true;
        }

    }
}
