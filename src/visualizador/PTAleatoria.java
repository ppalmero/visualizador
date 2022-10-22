package visualizador;

// Referenced classes of package visualizador:
//            PseudoTriangulacion, Punto, Nodo1, Arista,
//            Conjunto
public class PTAleatoria extends PseudoTriangulacion {

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
        int pi = 0;
        int pj = 0;
        int pk = 0;
        int aux[] = new int[no_of_nodes];//210
        int i = 0;
        for (int j = 0; j < no_of_nodes; j++) {
            if (posPuntos[j].cara == f) {
                aux[i] = j;
                i++;
            }
        }

        if (i != 0) {
            int k = (int) (random_c() * (float) i);
            pk = aux[k];
            int hayfact = CalcularFactibles(f, pk, -1);
            int cantfact;
            for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if (hayfact != 0) {
                int l = (int) (random_c() * (float) cantfact);
                pi = factibles[l];
                int j;
                for (j = l; j == l; j = (int) (random_c() * (float) cantfact));
                pj = factibles[j];
            }
        }
        if (i == 0) {
            pk = -1;
            int j;
            for (j = 0; caras[f][j] != -1;) {
                aux[i] = caras[f][j];
                j++;
                i++;
            }

            int hayfact;
            for (hayfact = 0; hayfact == 0; hayfact = CalcularFactibles(f, pk, -1)) {
                int k = (int) (random_c() * (float) i);
                pk = aux[k];
            }

            int cantfact;
            for (cantfact = 0; factibles[cantfact] != -1; cantfact++);
            if (hayfact != 0) {
                j = (int) (random_c() * (float) cantfact);
            }
            pj = factibles[j];
            pi = pk;
            pk = -1;
        }
        DividirCara(f, pk, pi, pj);
    }
}