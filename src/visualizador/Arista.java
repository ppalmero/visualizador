package visualizador;

// CLASE ARISTA
import java.awt.Color;

public class Arista extends Object {

    private Punto dest = null;	//vértice destino de la Arista
    private Punto orig = null;	//vértice origen de la Arista
    private Arista sig = null;	//apunta a la Arista que comienza en el vértice dest
    private Arista twin = null;	//apunta a la Arista gemela (SÓLO PARA T.DELAUNAY)
    private Arista prev = null;     //apunta a la Arista anterior (Para triang != Delaunay)
    private Color color = Color.BLUE;
    private float pendiente;
    private int posPo = -1;
    private int posPd = -1;
    // no se puede utilizar twin para prev por el funcionamiento de setTwin

    public int getPosPd() {
        return posPd;
    }

    public void setPosPd(int posPd) {
        this.posPd = posPd;
    }

    public int getPosPo() {
        return posPo;
    }

    public void setPosPo(int posPo) {
        this.posPo = posPo;
    }

    public float getPendiente() {
        return pendiente;
    }

    public void setPendiente(float pendiente) {
        this.pendiente = pendiente;
    }

    public void setPendiente() {
        this.pendiente = (float)(((dest.y - orig.y) * 1.0f) / (dest.x - orig.x));//Cambio Double
    }

    public Arista(int po, int pd){
        this.posPo = po;
        this.posPd = pd;
    }

    public Arista(Punto v, Arista n, Arista t) {
        this.dest = v;
        this.sig = n;
        this.twin = t;
    }

    public Arista(Arista e) {
        this.dest = e.dest;
        this.sig = e.sig;
        this.twin = e.twin;
        this.orig = e.orig;
        this.prev = e.prev;
    }

    public void fijarDest(Punto v) {
        this.dest = v;
    }

    public void fijarSig(Arista e) {
        this.sig = e;
    }

    public void fijarTwin(Arista e) {
        this.twin = e;
        e.twin = this;
    }

    public Arista obtenerSig() {
        return this.sig;
    }

    public Arista obtenerTwin() {
        return this.twin;
    }

    public Punto obtenerDest() {
        return this.dest;
    }

    public String toString() {
        return "<" + obtenerOrig() + "," + obtenerDest() + ">";
    }

// ------------------------------ AÑADIDOS --------------------------------
    public Arista(Punto v, Punto w) {
        this.orig = v;
        this.dest = w;
        /*orig.agregarVecino(w);
        dest.agregarVecino(v);*/
    }

    public Arista(PuntoDouble v, PuntoDouble w) {
        this.orig = new Punto(v.x, v.y);
        this.dest = new Punto(w.x, w.y);
        /*orig.agregarVecino(w);
        dest.agregarVecino(v);*/
    }

    public void fijarOrig(Punto v) {
        this.orig = v;
    }

    public Punto obtenerOrig() {
        return this.orig;
    }

    public void fijarPrev(Arista e) {
        this.prev = e;
    }

    public Arista obtenerPrev() {
        return this.prev;
    }

    public boolean igualQue(Arista a) {
        return (this.obtenerOrig().igualQue(a.obtenerOrig())
                && this.obtenerDest().igualQue(a.obtenerDest()));
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return this.color;
    }

    public double longitud() {
        return orig.distance(dest);//FIJARSE SI FUNCIONA PARA DELAUNAY
    }

    void agregarVecino() {
        orig.agregarVecino(dest);
        dest.agregarVecino(orig);
    }

    void agregarVecinoDelone() {
        dest.agregarVecino(obtenerTwin().dest);
        obtenerTwin().dest.agregarVecino(dest);
    }
    
    public boolean equals(Object o){
        Arista a = (Arista) o;
        if ((posPo == a.getPosPo()) && (posPd == a.getPosPd())){
            return true;
        } else {
            return false;
        }
    }
}
