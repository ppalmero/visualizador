package visualizador;

// Triangulacion
// Es una especie de interfaz que deben seguir las triangulaciones que se implementen.
// La diferencia es que se utiliza como clase padre de las triangulaciones, utilizando
// herencia basada en polimirfismo dinámico para que las llamadas a todos los métodos
// y variables de las distintas triangulaciones sean iguales sin importar su tipo (que
// se detecta automáticamente) 
import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;

public class Triangulacion extends Configuracion {

    private final int INIT = 50;
    private int capacidadAristas, // capacidad del array de Aristas
            capacidadPuntos; // capacidad del array de vértices
    public Arista[] listaAristas; // array con las Aristas.
    // Los 6 primeros elementos se reservan al triángulo
    // imaginario inicial de la triangulación de Delaunay
    public Punto[] listaPuntos; // array con los vértices
    private ArrayList puntosVecinos;
    public int numAristas, // lleva la cuenta del nº Aristas introducidos
            numPuntos;
    public final Color COLOR_Arista = Color.black;
    public boolean esperandoPaso = false, //indica si está esperando a que se pulse el botón "Sig Paso". USAR SÓLO EN MODO PASO A PASO
            threadActivo = false; // indica si el thread está en ejecución (si existe)
    public int retardo; // retardo en milésimas de segundo que se espera antes de realizar cada paso de la inserción de un pto.
    public boolean insertarTodoPasoAPaso = false; // Indica si hay que visualizar el proceso de inserción de TODOS los vértices desde
    // el principio (sólo necesario para Delaunay; el resto lo hace siempre)
    protected double grado = 0;

// MÉTODOS ------------------------------------------------------
    public void inicializar() {
    }
    // Inicializa la triangulación con los valores que le vengan dados del canvas.
    // Es necesario utilizar un "inicializar" en lugar de un constructor
    // "Triangulacion (myCanvas, int) para poder usar polimorfismo

    public void insertarPunto(Punto v) {
    }
    // Inserta el vértice "v" en la triangulación
    // comprueba que el vértice que se quiere insertar no existía ya

    public int obtenerNumAristas() // Devuelve el número de Aristas introducidas hasta el momento
    {
        return numAristas;
    }

    public Arista obtenerEdge(int i) // devuelve la Arista introducida en el lugar "i"
    {
        return listaAristas[i];
    }

    public void insertarArista(Arista e) {
    }
    // crea una nueva Arista, la introduce en el array de Aristas y aumenta el
    // tamaño de ese array si fuera necesario

    private boolean yaExiste(Punto v) // Devuelve si un vértice ya se encontraba en la triangulación
    {
        return (false);
    }

    public void calculaGreedyT(Conjunto c) {
    }

    public void flip(Arista i) {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public int identificaArista(Punto v1, Punto v2) {
        return -1;
    }

    public boolean flip1Arista(Arista e, int j) {
        return false;
    }

    public boolean flip1Arista(Arista e) {
        return false;
    }

    public boolean esFlipeable(Arista e, int j) {
        return false;
    }

    public boolean esInterna(Arista a) {
        return false;
    }

    public boolean cortaAristas(Arista[] aristas, Arista a) {
        return false;
    }

    public double Det(Punto a, Punto b, Punto z) {
        return 0;
    }

    public int getNumeroPuntos() {
        return puntosVecinos.size();
    }

    public Punto getPunto(int i) {
        return (Punto) puntosVecinos.get(i);
    }

    public void agregarPuntoVecino(Punto p) {
        return;
    }

    public void agregarVecinos(Arista obtenerEdge) {
    }
    
    public double getGrado() {
        return grado;
    }

    public void setGrado(double grado) {
        this.grado = grado;
    }


    /*public void dibujarAristas(){}
    // Dibuja EN EL BUFFER DE IMAGEN las Aristas que contiene la triangulación
    
    
    public void dibujarPuntos() {}
    // Dibuja EN EL BUFFER DE IMAGEN los vértices que contiene la triangulación de Delaunay
    
    public boolean devolverEsperandoPaso ()
    // Devuelve si el thread se encuentra esperando pulsación del botón "Sig Paso"
    {
    return (false);
    }
    
    public boolean devolverInsertandoPunto ()
    // Devuelve si el thread se encuentra en el proceso de insertar
    {
    return (false);
    }
    
    public boolean devolverAcabado ()
    // Devuelve el valor de la variable InsertandoPaso
    {
    return (false);
    }
    
    public void fijarRetardo (int ret){}
    // Fija el valor "ret" para la variable "retardo"
    
    
    public void despertar () {}
    // Despierta al thread (se ha pulsado el botón "Sig Paso")
    
    public void terminarPasoAPaso () {}
    // Termina la simulación de paso a paso forzando al thread a que
    // se ejecute sin pausas
    
    public void insertarTodoPasoAPaso () {}
    // Crea un thread de inserción y le indica que haga un bucle para ir triangulando
    // todos los puntos de la nube desde el principio
    
    public void destruirThread () {}*/
    // Destruye el thread que realiza la simulación de paso a paso. Usar sólo cuando se pulsa el botón borrar

    Arista obtenerRotatedEdge(int i) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return (listaAristas[i]);
    }
}
