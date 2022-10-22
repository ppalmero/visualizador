package visualizador;

// Triangulacion
// Es una especie de interfaz que deben seguir las triangulaciones que se implementen.
// La diferencia es que se utiliza como clase padre de las triangulaciones, utilizando
// herencia basada en polimirfismo din�mico para que las llamadas a todos los m�todos
// y variables de las distintas triangulaciones sean iguales sin importar su tipo (que
// se detecta autom�ticamente) 
import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;

public class Triangulacion extends Configuracion {

    private final int INIT = 50;
    private int capacidadAristas, // capacidad del array de Aristas
            capacidadPuntos; // capacidad del array de v�rtices
    public Arista[] listaAristas; // array con las Aristas.
    // Los 6 primeros elementos se reservan al tri�ngulo
    // imaginario inicial de la triangulaci�n de Delaunay
    public Punto[] listaPuntos; // array con los v�rtices
    private ArrayList puntosVecinos;
    public int numAristas, // lleva la cuenta del n� Aristas introducidos
            numPuntos;
    public final Color COLOR_Arista = Color.black;
    public boolean esperandoPaso = false, //indica si est� esperando a que se pulse el bot�n "Sig Paso". USAR S�LO EN MODO PASO A PASO
            threadActivo = false; // indica si el thread est� en ejecuci�n (si existe)
    public int retardo; // retardo en mil�simas de segundo que se espera antes de realizar cada paso de la inserci�n de un pto.
    public boolean insertarTodoPasoAPaso = false; // Indica si hay que visualizar el proceso de inserci�n de TODOS los v�rtices desde
    // el principio (s�lo necesario para Delaunay; el resto lo hace siempre)
    protected double grado = 0;

// M�TODOS ------------------------------------------------------
    public void inicializar() {
    }
    // Inicializa la triangulaci�n con los valores que le vengan dados del canvas.
    // Es necesario utilizar un "inicializar" en lugar de un constructor
    // "Triangulacion (myCanvas, int) para poder usar polimorfismo

    public void insertarPunto(Punto v) {
    }
    // Inserta el v�rtice "v" en la triangulaci�n
    // comprueba que el v�rtice que se quiere insertar no exist�a ya

    public int obtenerNumAristas() // Devuelve el n�mero de Aristas introducidas hasta el momento
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
    // tama�o de ese array si fuera necesario

    private boolean yaExiste(Punto v) // Devuelve si un v�rtice ya se encontraba en la triangulaci�n
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
    // Dibuja EN EL BUFFER DE IMAGEN las Aristas que contiene la triangulaci�n
    
    
    public void dibujarPuntos() {}
    // Dibuja EN EL BUFFER DE IMAGEN los v�rtices que contiene la triangulaci�n de Delaunay
    
    public boolean devolverEsperandoPaso ()
    // Devuelve si el thread se encuentra esperando pulsaci�n del bot�n "Sig Paso"
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
    // Despierta al thread (se ha pulsado el bot�n "Sig Paso")
    
    public void terminarPasoAPaso () {}
    // Termina la simulaci�n de paso a paso forzando al thread a que
    // se ejecute sin pausas
    
    public void insertarTodoPasoAPaso () {}
    // Crea un thread de inserci�n y le indica que haga un bucle para ir triangulando
    // todos los puntos de la nube desde el principio
    
    public void destruirThread () {}*/
    // Destruye el thread que realiza la simulaci�n de paso a paso. Usar s�lo cuando se pulsa el bot�n borrar

    Arista obtenerRotatedEdge(int i) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return (listaAristas[i]);
    }
}
