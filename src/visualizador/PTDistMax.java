/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

// Referenced classes of package visualizador:
//            PseudoTriangulacion, Punto, Nodo1, Arista,
//            Conjunto
public class PTDistMax extends PseudoTriangulacion {

    @Override
    public void inicializar(Conjunto puntos) {
        if (puntos.size() > 2){
            super.inicializar(puntos);
            ConstruyeSolucion();
            obj = CalcularPeso();
            cant_pt_Sol = cant_pt;
            generarAristas();
        }
    }

    @Override
    void ParticionarCara(int f) {
        int pi = 0;
        int pj = 0;
        int pk = 0;
        int aux[] = new int[no_of_nodes];
        int i = 0, hayfact, cantfact, j, h = 0, l = 0;
        // recupero todos los puntos interiores de la cara F en aux
        for (j = 0; j < no_of_nodes; j++) {
            if (posPuntos[j].cara == f) {
                aux[i] = j; // j-numero de punto
                i++; // cuento cuantos hay en aux
            }
        }
// PUNTOS INTERIORES
        j = 0;

        if (i != 0)// indica que hay puntos interiores
        {
            int k = (int) (random_c() * (float) i);//suponiendo que devuelve valores entre 0 e i-1
            pk = aux[k];
            // calcular los factibles de pk en esa cara
            hayfact = CalcularFactibles(f, pk, -1);
            for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if (hayfact != 0) {
                double dMayor = -1;
                for (l = 0; factibles[l] != -1; l++) {
                    for (int fact = l; factibles[fact] != -1; fact++) {
                        if (posPuntos[factibles[l]].distance(posPuntos[factibles[fact]]) > dMayor) {
                            dMayor = posPuntos[factibles[l]].distance(posPuntos[factibles[fact]]);
                            j = fact;
                            h = l;
                        }
                    }
                }
            }
            pi = factibles[h];
            pj = factibles[j];

            /*/ ELEGIR ALEATORIAMENTE UN pk INTERIOR
            
            l = (int)(random_c() * (float)cantfact);//suponiendo que devuelve valores entre 0 e i-1
            pi = factibles[l];
            // ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            j = l;
            while (j == l){
            //j = (int)(random_c() * (float)cantfact);
            //int d = -1;
            double dMayor = -1;
            for(int fact = 0; factibles[fact] != -1; fact++){
            if (posPuntos[pi].distance(posPuntos[factibles[fact]]) > dMayor){
            dMayor = posPuntos[pi].distance(posPuntos[fact]);
            j = fact;
            }
            }
            
            }
            //for(j = l; j == l; j = (int)(random_c() * (float)cantfact));
            pj = factibles[j];
            }*/
        }
        // hasta acá - caso: hay puntos interiores

        /********************************************************************************/
        // PUNTOS DEL BORDE
        //	i--; // me queda la cantidad exacta de puntos interiores
        if (i == 0) {
            pk = -1;// pk es -1 ya que no hay punto interior
            // recupero todos los puntos del borde de la cara en aux
            for (j = 0; caras[f][j] != -1;) {
                aux[i] = caras[f][j];// nº de punto indicado en la cara F en la posición j del borde
                j++;
                i++;// cuento cuantos hay en aux
            }

            for (hayfact = 0; hayfact == 0; hayfact = CalcularFactibles(f, pk, -1))// ELEGIR ALEATORIAMENTE UN pj BORDE  - ojo no elegir el mismo
            // calcular los factibles de pk en esa cara
            // tomo un punto del borde
            {// ELEGIR ALEATORIAMENTE UN pi BORDE
                int k = (int) (random_c() * (float) i);
                pk = aux[k];
            }

            // contar los factibles
            for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if (hayfact != 0) {
                j = (int) (random_c() * (float) cantfact);
            }
            pj = factibles[j];
            pi = pk;
            pk = -1;
        }
        // hasta acá - caso: no hay puntos interiores

        /************************************/
        //dividir la cara
        DividirCara(f, pk, pi, pj);
        // LibreF ya se actualizo en dividircara , notar que en caras me queda la historia completa de divisiones realizadas.
    }
}