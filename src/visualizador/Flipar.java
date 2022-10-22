/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizador;

/**
 *
 * @author Pablazo
 */
public class Flipar {
    Arista[] aristas;
    Arista[] aristasF;
    private final int CANTARISTAS = 200;
    Flipar (){
        aristas = new Arista[CANTARISTAS];
        aristasF = new Arista[CANTARISTAS];
    }
    public Arista fliparArista (int pos, Triangulacion t){
        int cont = 0;
        int posOrig1 = -1;
        int posOrig2 = -1;
        int posDest1 = -1;
        int posDest2 = -1;
        Punto posOrig = null;
        Punto posDest = null;
        Punto origen  = new Punto(t.obtenerEdge(pos).obtenerOrig());
        Punto destino = new Punto(t.obtenerEdge(pos).obtenerDest());
        while (cont < t.numAristas){
            if (cont == pos)
                cont++;
            else{
                if ((origen.igualQue(t.obtenerEdge(cont).obtenerOrig()))){
                    int cont2 = 0;
                    while (cont2 < t.numAristas){
                        if ((t.obtenerEdge(cont).obtenerDest().igualQue(t.obtenerEdge(cont2).obtenerOrig())) &&
                             t.obtenerEdge(cont2).obtenerDest().igualQue(destino)){
                             posOrig = new Punto (t.obtenerEdge(cont).obtenerDest());
                        }
                        else
                            if ((t.obtenerEdge(cont).obtenerDest().igualQue(t.obtenerEdge(cont2).obtenerDest())) &&
                                 t.obtenerEdge(cont2).obtenerOrig().igualQue(destino)){
                                 posOrig = new Punto (t.obtenerEdge(cont).obtenerDest());
                            }
                    }
                }
                else
                    if ((origen.igualQue(t.obtenerEdge(cont).obtenerDest()))){
                        int cont2 = 0;
                        while (cont2 < t.numAristas){
                            if ((t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerOrig())) &&
                                 t.obtenerEdge(cont2).obtenerDest().igualQue(destino)){
                                 posOrig = new Punto (t.obtenerEdge(cont).obtenerOrig());
                            }
                            else
                                if ((t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerDest())) &&
                                     t.obtenerEdge(cont2).obtenerOrig().igualQue(destino)){
                                     posOrig = new Punto (t.obtenerEdge(cont).obtenerOrig());
                                }
                        }
                    }
                cont++;
            }
        }
            while (cont < t.numAristas){
                if (cont == pos)
                    cont++;
                else{
                    if ((origen.igualQue(t.obtenerEdge(cont).obtenerOrig()))){
                        int cont2 = 0;
                        while (cont2 < t.numAristas){
                            if ((t.obtenerEdge(cont).obtenerDest().igualQue(t.obtenerEdge(cont2).obtenerOrig())) &&
                                 t.obtenerEdge(cont2).obtenerDest().igualQue(destino)){
                                 posDest = new Punto (t.obtenerEdge(cont).obtenerDest());
                            }
                            else
                                if ((t.obtenerEdge(cont).obtenerDest().igualQue(t.obtenerEdge(cont2).obtenerDest())) &&
                                     t.obtenerEdge(cont2).obtenerOrig().igualQue(destino)){
                                     posDest = new Punto (t.obtenerEdge(cont).obtenerDest());
                                }
                        }
                    }
                    else
                        if ((origen.igualQue(t.obtenerEdge(cont).obtenerDest()))){
                            int cont2 = 0;
                            while (cont2 < t.numAristas){
                                if ((t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerOrig())) &&
                                     t.obtenerEdge(cont2).obtenerDest().igualQue(destino)){
                                     posDest = new Punto (t.obtenerEdge(cont).obtenerOrig());
                                }
                                else
                                    if ((t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerDest())) &&
                                         t.obtenerEdge(cont2).obtenerOrig().igualQue(destino)){
                                         posDest = new Punto (t.obtenerEdge(cont).obtenerOrig());
                                    }
                            }
                        }
                    cont++;
                }
            }

                    /*||
                 (t.obtenerEdge(pos).obtenerOrig().igualQue(t.obtenerEdge(cont).obtenerDest()))){
                int cont2 = 0;
                while (cont2 < t.numAristas){
                    if (((t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerOrig())) ||
                        (t.obtenerEdge(cont).obtenerOrig().igualQue(t.obtenerEdge(cont2).obtenerDest()))) &&
                        (!t.obtenerEdge(cont2).igualQue(t.obtenerEdge(cont))))
                     {

                    }
                }
            }*/
                
        
        return (new Arista(posOrig,posDest));
    
    }
    private void aristasFlipeables (){

    }
}