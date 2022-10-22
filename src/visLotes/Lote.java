/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visLotes;

import java.util.ArrayList;
import visualizador.Arista;
import visualizador.Configuracion;
import visualizador.Conjunto;

/**
 *
 * @author Pablo
 */
public class Lote {

    protected ArrayList<ArrayList<Arista>> inAristas;
    protected ArrayList<ArrayList<Arista>> outAristas;
    protected  ArrayList<Arista> inicial;
    protected int cantCambios = 0;
    protected int pos = -1;
    protected int peso;
    protected ArrayList<Arista> corriente;
    private ArrayList<Arista> aristasDilacion;
    protected ArrayList<ArrayList<Arista>> poligonos;

    public Lote() {
        inAristas = new ArrayList<ArrayList<Arista>>();
        outAristas = new ArrayList<ArrayList<Arista>>();
        inicial = new ArrayList<Arista>();
        corriente = new ArrayList<Arista>();
    }

    public ArrayList<Arista> getInAristas() {
        return inAristas.get(pos);
    }

    public ArrayList<Arista> getOutAristas() {
        return outAristas.get(pos);
    }

    public void addAristas(ArrayList<Arista> a){
        inAristas.add(a);
    }

    public void removeAristas(ArrayList<Arista> a){
        outAristas.add(a);
    }
    
    public void setInicial(ArrayList<Arista> a){
        pos = -1;
        inicial = (ArrayList<Arista>) a.clone();
        corriente = (ArrayList<Arista>) a.clone();
    }

    public ArrayList<Arista> getSiguiente() {
        return null;
    }

    public ArrayList<Arista> getAnterior() {
        return null;
    }

    public ArrayList<Arista> getUltimo() {
        return null;
    }

    public ArrayList<Arista> getInicial() {
        corriente = (ArrayList<Arista>) inicial.clone();
        pos = -1;
        return corriente;
    }

    public ArrayList<Arista> setPos(int p) {
        return null;
    }

    public int size(){
        return cantCambios;
    }

    public int getPos(){
        return pos;
    }

    float getDilacion(Conjunto p) {
        float dilacion = 0;
        peso = 0;
        ArrayList<Arista> aristas = new ArrayList<Arista>();
        for (int i = 0; i < corriente.size(); i++){
            aristas.add(new Arista(p.get(corriente.get(i).getPosPo()), p.get(corriente.get(i).getPosPd())));
            peso += aristas.get(i).longitud();
        }
        Configuracion c = new Configuracion();
        c.inicializar(p, aristas);
        aristasDilacion = c.dilacion();
        for (int i = 0; i < aristasDilacion.size(); i++){
            dilacion += aristasDilacion.get(i).longitud();
        }
        dilacion /= c.getPuntoOrigen().distance(c.getPuntoDestino());
        return dilacion;
    }

    public float getPeso() {
        return peso;
    }

    void addOutAristas(ArrayList<Arista> aristas) {
        outAristas.add(aristas);
    }

    void addInAristas(ArrayList<Arista> aristas) {
        inAristas.add(aristas);
        cantCambios++;
    }

    void addPoligono(ArrayList<Arista> aristas) {
        if (poligonos == null){
            inicial = new ArrayList<Arista>();
            inicial = (ArrayList<Arista>)aristas.clone();
            poligonos = new ArrayList<ArrayList<Arista>>();
        }
        cantCambios++;
        poligonos.add(aristas);
    }

    void resetPoligonos() {
        cantCambios = 0;
        pos = -1;
        inicial = null;
        poligonos = null;
    }


}
