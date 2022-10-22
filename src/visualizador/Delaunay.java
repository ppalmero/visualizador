package visualizador;

// Clase Triangulación de Delaunay

// Implementa el algoritmo de triangulación por construcción incremental
// aleatoria (partiendo de 1 triángulo que contenga todos los puntos)


import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;


// -----------------------------------------------------------
// THREAD INSERTAR
// -----------------------------------------------------------

// Este thread inserta un vértice en la triangulación y la recalcula
// (Sólo llamar a este thread cuando se esté en metodo paso a paso y se quiera
// insertar un vértice)

class Delaunay extends Triangulacion{
    
   private final int INIT = 50;
   private int capacidadAristas,capacidadPuntos;
   public Arista[] listaAristas;
   public int numAristas = 0;
   public Punto [] listaPuntos; // array de vértices
   private ArrayList puntosVecinos;
   public int numPuntos = 0; // nº de vértices que hay en listaVert


   private Punto n, se, sw;

   public final Color COLOR_ARISTA = Color.black,
                       COLOR_ARISTA_FLIP = Color.orange,
                       COLOR_ARISTA_3 = Color.lightGray,
                       COLOR_CIRCULO_ARISTA_PRUEBA = Color.blue,
                       COLOR_CIRCULO_ARISTA_MALA = Color.red,
                       COLOR_TRIANGULO = Color.green;
   //private myCanvas mc;

   // --- Variables para el thread ---
   //private insertarDelaunay insDel;
   public boolean esperandoPaso = false, //indica si está esperando a que se pulse el botón "Sig Paso". USAR SÓLO EN MODO PASO A PASO
                  threadActivo = false; // indica si el thread se esta ejecutando
   public int retardo;
   public boolean insertarTodoPasoAPaso = false; // Indica si hay que visualizar el proceso de inserción de TODOS los vértices desde
                                                 // el principio (sólo necesario para Delaunay; el resto lo hace siempre)

/////////////
// MÉTODOS //
/////////////


   //public void inicializar (/*myCanvas m, int ret*/){
      /*mc = m;*/
      //retardo = ret;
   public void inicializar (){
      capacidadAristas = INIT;
      capacidadPuntos = INIT;
      numAristas = 0;
      listaAristas = new Arista[INIT];
      listaPuntos = new Punto[INIT*2];
      puntosVecinos = new ArrayList();

      n = new Punto(0, -30000);//CAMBIÉ 3000 POR 30000
      se = new Punto(30000, 40000);//cambié 2 x 4
      sw = new Punto(-40000, 40000);

      Arista e1 = new Arista(sw, null, null);
      Arista e2 = new Arista(n, null, null);
      Arista e3 = new Arista(se, null, null);
      Arista e4 = new Arista(sw, null, null);
      Arista e5 = new Arista(n, null, null);
      Arista e6 = new Arista(se, null, null);

      e1.fijarSig(e3);
      e3.fijarSig(e5);
      e5.fijarSig(e1);

      e1.fijarTwin(e2); //GEMELOS
      e3.fijarTwin(e4);
      e5.fijarTwin(e6);

      insertarArista(e1);
      insertarArista(e2);
      insertarArista(e3);
      insertarArista(e4);
      insertarArista(e5);
      insertarArista(e6);
   }

   public void insertarPunto(Punto v)
   {
      if(yaExistia(v)) return; // comprueba que el vértice que se quiere insertar no existía ya

      //insertarOrdenadoPunto(v);

      Arista e = enQueTriangulo(v);  // busca el triángulo dentro del que se encuentra el vértice que se inserta
      /*if (mc.pasoAPaso == true)  // si PASO A PASO -> crear thread de inserción de vértice
      {
          insDel = new insertarDelaunay (this, v, e);
          insDel.start();
          threadActivo = true;
      }
      else
      {*/
          Arista e1 = new Arista(v, null, null);
          Arista e2 = new Arista(e.obtenerDest(), null, null);
          Arista e3 = new Arista(v, null, null);
          Arista e4 = new Arista(e.obtenerSig().obtenerDest(), null, null);
          Arista e5 = new Arista(v, null, null);
          Arista e6 = new Arista(e.obtenerTwin().obtenerDest(), null, null);

          // e1, e3, e5 = Aristas desde los vértices del triángulo donde cayó
          //              "v" hasta el punto "v"
          // e2, e4, e6 = Igual que e1, e3, e5 pero en sentido opuesto

          e1.fijarSig(e6);
          e2.fijarSig(e.obtenerSig());
          e3.fijarSig(e2);
          e4.fijarSig(e.obtenerSig().obtenerSig());
          e5.fijarSig(e4);
          e6.fijarSig(e);
          e.obtenerSig().obtenerSig().fijarSig(e5);
          e.obtenerSig().fijarSig(e3);
          e.fijarSig(e1);

          e1.fijarTwin(e2);
          e3.fijarTwin(e4);
          e5.fijarTwin(e6);

          insertarArista(e1);
          insertarArista(e2);
          insertarArista(e3);
          insertarArista(e4);
          insertarArista(e5);
          insertarArista(e6);

          legaliza(e6.obtenerSig());  // Comprueba si hay que hacer flip de las
          legaliza(e2.obtenerSig());  // 3 aristas del triángulo donde se
          legaliza(e4.obtenerSig());  // introdujo "v"

          /*if (!mc.alVuelo)
             mc.habilitarTriangular ();
      }*/
   }


   private void legaliza(Arista e)
   {
   // Comprueba que la Arista "e" es legal (si sigue el criterio del círculo)
   // Si no es así, le aplica un flip

      if(aristaFicticia(e)) return;

      if(dentroDeCirculo(e.obtenerDest(), e.obtenerSig().obtenerDest(),
		  e.obtenerTwin().obtenerDest(), e.obtenerTwin().obtenerSig().obtenerDest()))
      {

	  flip(e);
          //e.setColor(Color.GREEN);
          legaliza(e.obtenerSig().obtenerSig());
          legaliza(e.obtenerTwin().obtenerSig());
      }
   }

   public Arista enQueTriangulo(Punto t)
   // Devuelve una Arista del triángulo que contiene a "t"
   {
      Punto start = new Punto(0, 0);
      Arista e = listaAristas[0];

      while(isOnLeft(t, e.obtenerTwin().obtenerDest(), e.obtenerDest()))
      {
	 if(isOnLeft(e.obtenerSig().obtenerDest(), start, t))
	 {
	    e = e.obtenerSig().obtenerTwin();
	 }
	 else
	 {
	    e = e.obtenerSig().obtenerSig().obtenerTwin();
	 }
      }
      return e.obtenerTwin();
   }

   public int obtenerNumAristas()
   {
      return numAristas;
   }

   public Arista obtenerEdge(int i)
   {
      return listaAristas[i];
   }

   public void insertarArista (Arista e)
   // Crea una nueva Arista y agranda la lista si es necesario
   {
      if(numAristas >= capacidadAristas)
      {
	 capacidadAristas = capacidadAristas * 2;
	 Arista[] temp = new Arista[capacidadAristas];
	 for(int i = 0; i < numAristas; i++)
	 {
	    temp[i] = listaAristas[i];
	 }
	 listaAristas = temp;
      }
      listaAristas[numAristas] = e;
      numAristas++;
   }

   private boolean isOnLeft(Punto t, Punto a, Punto b)
   {
      //returns true if t is on the left of the line from a to b
      return (a.getX() * (b.getY() - t.getY())
              - a.getY() * (b.getX() - t.getX())
              + ((b.getX() * t.getY()) - (b.getY() * t.getX()))
             > 0);
   }


   public boolean aristaFicticia (Arista e)
   // Indica si la Arista pertenece a una de las del triángulo inicial
   {
      for(int i = 0; i < 6; i++)
      {
	 if(e == listaAristas[i]) return true;
      }
      return false;
   }

   public boolean dentroDeCirculo (Punto p, Punto q, Punto r, Punto s)
   {
   //Devuelve si s está dentro del círculo formado por p, q, y r
      double ax = q.getX() - p.getX();
      double ay = q.getY() - p.getY();
      double aa = (ax * ax) + (ay * ay);
      double bx = r.getX() - p.getX();
      double by = r.getY() - p.getY();
      double bb = (bx * bx) + (by * by);
      double cx = s.getX() - p.getX();
      double cy = s.getY() - p.getY();
      double cc = (cx * cx) + (cy * cy);
      return ((  aa * (bx * cy - by * cx)
               - bb * (ax * cy - ay * cx)
               + cc * (ax * by - ay * bx)) < 0);
   }

   public Punto centroCirculo (Punto p, Punto q, Punto r)
   {
      double px = (double)p.getX();
      double py = -(double)p.getY();
      double qx = (double)q.getX();
      double qy = -(double)q.getY();
      double rx = (double)r.getX();
      double ry = -(double)r.getY();
      double a1 = -0.5 * ((px * px) - (qx * qx) + (py * py) - (qy * qy));
      double b1 = px - qx;
      double c1 = py - qy;
      double a2 = -0.5 * ((qx * qx) - (rx * rx) + (qy * qy) - (ry * ry));
      double b2 = qx - rx;
      double c2 = qy - ry;
      double w = (b1 * c2) - (b2 * c1);
      double x = -((a1 * c2) - (a2 * c1));
      double y = (a1 * b2) - (a2 * b1);
      Punto v = new Punto((x/w), (y/w));
      return v;
   }

   public void flip(Arista e)
   {
   // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
      e.fijarDest(e.obtenerSig().obtenerDest());
      e.obtenerTwin().fijarDest(e.obtenerTwin().obtenerSig().obtenerDest());
      e.obtenerSig().obtenerSig().fijarSig(e.obtenerTwin().obtenerSig());
      e.obtenerTwin().obtenerSig().obtenerSig().fijarSig(e.obtenerSig());
      e.fijarSig(e.obtenerSig().obtenerSig());
      e.obtenerTwin().fijarSig(e.obtenerTwin().obtenerSig().obtenerSig());
      e.obtenerSig().obtenerSig().fijarSig(e);
      e.obtenerTwin().obtenerSig().obtenerSig().fijarSig(e.obtenerTwin());
   }

   public boolean flip1Arista(Arista e)
   {
   // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
      Arista aux = new Arista(e.obtenerDest(), e.obtenerSig(), e.obtenerTwin());

          e.fijarDest(e.obtenerSig().obtenerDest());
          e.obtenerTwin().fijarDest(e.obtenerTwin().obtenerSig().obtenerDest());
          e.obtenerSig().obtenerSig().fijarSig(e.obtenerTwin().obtenerSig());
          e.obtenerTwin().obtenerSig().obtenerSig().fijarSig(e.obtenerSig());
          e.fijarSig(e.obtenerSig().obtenerSig());
          e.obtenerTwin().fijarSig(e.obtenerTwin().obtenerSig().obtenerSig());
          e.obtenerSig().obtenerSig().fijarSig(e);
          e.obtenerTwin().obtenerSig().obtenerSig().fijarSig(e.obtenerTwin());

      if((cortaAristas(listaAristas, new Arista(e.obtenerDest(), e.obtenerSig(), e.obtenerTwin()))) || (!esInterna(e))){
          flip(e);
          return false;
      }
      else
          return true;

   }

   public boolean esInterna (Arista a){
        return !((int)a.obtenerDest().getX() < -100 || (int)a.obtenerDest().getX() > FV.panel.getWidth() * 2||
                 (int)a.obtenerTwin().obtenerDest().getX() < -100 || (int)a.obtenerTwin().obtenerDest().getX() > FV.panel.getWidth() * 2||
                 (int)-a.obtenerDest().getY() < -100 || (int)-a.obtenerDest().getY() > FV.panel.getHeight() * 2 ||
                 (int)-a.obtenerTwin().obtenerDest().getY() < -100 || (int)-a.obtenerTwin().obtenerDest().getY() > FV.panel.getHeight() * 2);
    }

   public boolean cortaAristas(Arista[] aristas, Arista a){
	  boolean corta=false;
	  Arista ar=null;
	  double sig1,sig2;
	  double corteY;
	  double corteX;
	  int i=0;
          int j = 0;
	  Punto p1= a.obtenerTwin().obtenerDest();
	  Punto p2=a.obtenerDest();
	  while(!corta && i < numAristas){
		  ar=(Arista) aristas[i];
		  Punto p=ar.obtenerTwin().obtenerDest();
		  Punto q=ar.obtenerDest();
                  if (((p.igualQue(p1)) && (q.igualQue(p2))) || ((q.igualQue(p1)) && (p.igualQue(p2)))){
                      if (j == 0){
                          j++;
                          i++;
                          i++;
                      }
                      else
                        corta = true;
                  }
                  else{
                      sig1=Det(p,q,p1);
                      sig2=Det(p,q,p2);
                      if (((sig1<0)&&(sig2>0)) || ((sig1>0)&&(sig2<0))){
                              double vx=p2.x-p1.x;
                              double vy=p2.y-p1.y;
                              double wx=q.x-p.x;
                              double wy=q.y-p.y;
                              if (vy==0){
                                      corteY=(p.x-p1.x+(p1.y*vx)-(p.y*wx/wy))/((vx)-(wx/wy));
                                      corteX=((corteY-p1.y)*vx)+p1.x;
                              }
                              else if (wy==0){
                                      corteY=(p.x-p1.x+(p1.y*vx/vy)-(p.y*wx))/((vx/vy)-(wx));
                                      corteX=((corteY-p1.y)*vx/vy)+p1.x;
                              }
                              else{
                                      corteY=(p.x-p1.x+(p1.y*vx/vy)-(p.y*wx/wy))/((vx/vy)-(wx/wy));
                                      corteX=((corteY-p1.y)*vx/vy)+p1.x;
                              }
                              Punto ptoCorte=new Punto(corteX,corteY);
                              if ((p.distance(ptoCorte)<=p.distance(q))&&(q.distance(ptoCorte)<=q.distance(p))){
                                      corta=true;
                              }
                      }
                      i++;
                      i++;
                  
                  }
                  
	  }
	  return corta;
  }

   public double Det(Punto a,Punto b,Punto z){
 	/*return ((b.getX()*(-z.getY())+z.getX()*(-a.getY())+a.getX()*(-b.getY()))-
 			(b.getX()*(-a.getY())+z.getX()*(-b.getY())+a.getX()*(-z.getY())));*/
       return (((a.x - z.x) * (b.y - z.y)) - ((b.x - z.x) * (a.y - z.y)));
 }

   public boolean yaExistia(Punto v)
   // Indica si ya existía el vértice "v" en alguna parte de la lista de aristas
   {
      for(int i = 0; i < numAristas; i++)
      {
	 if(listaAristas[i].obtenerDest().getX() == v.getX() &&
	    listaAristas[i].obtenerDest().getY() == v.getY())
	 {
	    return true;
	 }
      }
      return false;
   }

   public int identificaArista(Punto v1, Punto v2){//AGREGADO PARA BUSCAR UNA ARISTA, DADO DOS PUNTOS

      for(int i = 0; i < numAristas; i++){
	 if(listaAristas[i].obtenerDest().getX() == v1.getX() &&  listaAristas[i].obtenerDest().getY() == v1.getY())
	 {
             if(listaAristas[i].obtenerTwin().obtenerDest().getX() == v2.getX() &&  listaAristas[i].obtenerTwin().obtenerDest().getY() == v2.getY())
                return i;
	 }
      }
      return -1;
   }

   public int getNumeroPuntos(){
        return puntosVecinos.size();
    }

    public Punto getPunto(int i){
       return (Punto)puntosVecinos.get(i);
    }

    public void agregarVecinos(Arista a) {
        Punto origen, destino;
        if (!puntosVecinos.contains(a.obtenerTwin().obtenerDest())){
            puntosVecinos.add(a.obtenerTwin().obtenerDest());
            origen = a.obtenerTwin().obtenerDest();
        }
        else
            origen = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerTwin().obtenerDest()));

        if (!puntosVecinos.contains(a.obtenerDest())){
            puntosVecinos.add(a.obtenerDest());
            destino = a.obtenerDest();
        }
        else
            destino = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerDest()));

        origen.agregarVecino(destino);
        destino.agregarVecino(origen);

    }

    public void insertarOrdenadoPunto(Punto vi)
   // crea una nueva Arista, la introduce en el array de Puntos y aumenta el
   // tamaño de ese array si fuera necesario
   {
      Punto [] temp;
      int i=-1,j=0;
      boolean insertado = false;

      if(numPuntos >= capacidadPuntos)
      {
	 capacidadPuntos *= 2;
      }
      temp = new Punto[capacidadPuntos];

      if (numPuntos == 0)
      {
          temp [0] =  (vi);
      }
      else
      {
          for(i = 0; i < numPuntos; i++)
          {
               if ( ((vi.getX() < listaPuntos[i].getX()) ||
                     (vi.getX() == listaPuntos[i].getX() &&
                      vi.getY() > listaPuntos[i].getY()))  && !insertado)
               {
                   temp[j] =  (vi);
                   j++;
                   insertado = true;
               }
               temp[j] = listaPuntos[i];
               j++;
          }
      }
      if (j == i)
      {
          temp [j] = (vi);
      }
      listaPuntos = temp;
      numPuntos++;
   }

    public boolean contieneArista(Arista a) {
        for(int i = 6; i < obtenerNumAristas(); i = i + 2){
            if ((listaAristas[i].obtenerDest().igualQue(a.obtenerOrig()) && listaAristas[i].obtenerTwin().obtenerDest().igualQue(a.obtenerDest())) ||
                (listaAristas[i].obtenerDest().igualQue(a.obtenerDest()) && listaAristas[i].obtenerTwin().obtenerDest().igualQue(a.obtenerOrig()))){
                return true;
            }


        }
        return false;
    }


// ------ MÉTODOS GRÁFICOS DE LA TRIANGULACIÓN ------
// (ninguno borra la pantalla. Para eso se utiliza "redibujar")

   /*public void dibujarAristas()
   // Dibuja EN EL BUFFER DE IMAGEN las aristas que contiene la triangulación de Delaunay
   {
      for(int i = 6; i < numAristas; i = i + 2)
      {

              mc.prepararArista (listaAristas[i].obtenerDest().getX(),
                               -listaAristas[i].obtenerDest().getY(),
                                listaAristas[i].obtenerTwin().obtenerDest().getX(),
                               -listaAristas[i].obtenerTwin().obtenerDest().getY(),
                                COLOR_ARISTA);
      }
   }


   public void dibujarPuntos()
   // Dibuja EN EL BUFFER DE IMAGEN los vértices que contiene la triangulación de Delaunay
   {
      for(int i = 6; i < numAristas; i = i + 2)
      {
           mc.prepararPunto(listaAristas[i].obtenerDest().getX(),
                             -listaAristas[i].obtenerDest().getY());
           mc.prepararPunto(listaAristas[i].obtenerTwin().obtenerDest().getX(),
                             -listaAristas[i].obtenerTwin().obtenerDest().getY());

      }
   }


// ------ INTERFAZ CON EL THREAD ------


   public void dibujarCirculoAristaPrueba (Arista e, Color c)
   // Dibuja EN PANTALLA el círculo que engloba al triángulo que forman
   // las aristas e, la siguiente y la "twin". También redibuja la Arista que ya existía
   // antes de empezar la inserción. TODO LO DIBUJA DEL COLOR c
   {
      Punto centro = new Punto(0,0);
      int dist;

      centro = centroCirculo (e.obtenerDest(), e.obtenerSig().obtenerDest(), e.obtenerTwin().obtenerDest());

      dist = (int) java.lang.Math.sqrt(
            (double)(e.obtenerSig().obtenerDest().getX() - centro.getX()) *
            (double)(e.obtenerSig().obtenerDest().getX() - centro.getX()) +
	    (double)(e.obtenerSig().obtenerDest().getY() - centro.getY()) *
            (double)(e.obtenerSig().obtenerDest().getY() - centro.getY()));

      mc.prepararCirculo(centro.getX()-dist, -centro.getY()-dist, dist*2, c);
      mc.prepararAristaPrueba (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                               e.obtenerTwin().obtenerDest().getX(), -e.obtenerTwin().obtenerDest().getY(), c);
      mc.mostrarImagen();

      if (c == COLOR_CIRCULO_ARISTA_PRUEBA)
            mc.mostrarMensaje ("Comprobando si la Arista es valida");
      else
            mc.mostrarMensaje ("Arista no valida");

    }

    public void dibujarAristaFlip (Arista e)
    // Dibuja EN PANTALLA la Arista resultante de hacer flip a "e"
    {
         mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                            e.obtenerTwin().obtenerDest().getX(), -e.obtenerTwin().obtenerDest().getY(),
                            COLOR_ARISTA_FLIP);
         mc.mostrarMensaje ("Flip de la Arista no valida");
         mc.mostrarImagen();
    }


    public void dibujarTriangulo (Arista e)
    // Dibuja EN PANTALLA el triángulo al que pretenece la Arista "e":
    // e + siguiente de e + twin de e
    {
       if(!(e.obtenerDest().getX() < 0 || e.obtenerDest().getX() > mc.ANCHO_PIXELS ||
            e.obtenerSig().obtenerDest().getX() < 0 || e.obtenerSig().obtenerDest().getX() > mc.ANCHO_PIXELS ||
            e.obtenerTwin().obtenerDest().getX() < 0 || e.obtenerTwin().obtenerDest().getX() > mc.ANCHO_PIXELS ||
            -e.obtenerDest().getY() < 0 || -e.obtenerDest().getY() > mc.ALTO_PIXELS ||
            -e.obtenerSig().obtenerDest().getY() < 0 || -e.obtenerSig().obtenerDest().getY() > mc.ALTO_PIXELS ||
            -e.obtenerTwin().obtenerDest().getY() < 0 || -e.obtenerTwin().obtenerDest().getY() > mc.ALTO_PIXELS)){

            mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                               e.obtenerSig().obtenerDest().getX(), -e.obtenerSig().obtenerDest().getY(),
                               COLOR_TRIANGULO);

            mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                               e.obtenerTwin().obtenerDest().getX(), -e.obtenerTwin().obtenerDest().getY(),
                               COLOR_TRIANGULO);

            mc.prepararArista (e.obtenerSig().obtenerDest().getX(), -e.obtenerSig().obtenerDest().getY(),
                               e.obtenerTwin().obtenerDest().getX(), -e.obtenerTwin().obtenerDest().getY(),
                               COLOR_TRIANGULO);
            mc.mostrarImagen();
            mc.mostrarMensaje ("Triangulo que contiene el punto");
       }
       else
            mc.mostrarMensaje ("El punto no se encuentra dentro de ningún triángulo");
    }


    public void dibujarTresAristas (Arista e1, Arista e3, Arista e5)
    // Dibuja EN PANTALLA las aristas e1, e3 ,e5
    {
         mc.prepararAristaPrueba (e1.obtenerDest().getX(), -e1.obtenerDest().getY(),
                            e1.obtenerTwin().obtenerDest().getX(), -e1.obtenerTwin().obtenerDest().getY(),
                            COLOR_ARISTA_3);
         mc.prepararAristaPrueba (e3.obtenerDest().getX(), -e3.obtenerDest().getY(),
                            e3.obtenerTwin().obtenerDest().getX(), -e3.obtenerTwin().obtenerDest().getY(),
                            COLOR_ARISTA_3);
         mc.prepararAristaPrueba (e5.obtenerDest().getX(), -e5.obtenerDest().getY(),
                            e5.obtenerTwin().obtenerDest().getX(), -e5.obtenerTwin().obtenerDest().getY(),
                            COLOR_ARISTA_3);

         mc.mostrarImagen();
         mc.mostrarMensaje ("Obtener 3 nuevos triangulos");
    }


   public void insertarTodoPasoAPaso ()
   // Crea un thread de inserción y le indica que haga un bucle para ir triangulando
   // todos los puntos de la nube desde el principio
   {
      if (mc.pasoAPaso == true)  // si PASO A PASO -> crear thread de inserción de vértice
      {
          inicializar (mc, retardo); // Vacía la traingulación para empezar a triangular desde el principio
          insDel = new insertarDelaunay (this, null, null);
          insertarTodoPasoAPaso = true;
          insDel.start();
          threadActivo = true;
      }
   }


   public void redibujar ()
   // Borra el paso anterior: Borra el recuadro de mensajes; borra la ventana y
   // dibuja EN EL BUFFER DE IMAGEN los vértices y la triangulación de Delaunay
   // SÓLO SE USA PARA PASO-A-PASO
   {
       mc.mostrarMensaje ("");
       mc.borrarCanvas();
       dibujarPuntosMc();
       dibujarAristas();
   }


   public boolean devolverEsperandoPaso ()
   // Devuelve si el thread se encuentra esperando pulsación del botón "Sig Paso"
   {
       return (esperandoPaso);
   }


   public void fijarRetardo (int ret)
   {
       retardo = ret;
   }


   public void despertar ()
   // Despierta al thread (se ha pulsado el botón "Sig Paso" o se quiere acabar
   // la triangulación del último punto insertado)
   {
       insDel.resume();
   }


   public void terminarPasoAPaso ()
   // Termina la simulación de paso a paso forzando al thread a que
   // se ejecute sin pausas
   {
       if (esperandoPaso == true)
       {
             insDel.terminarInsercion = true;
             despertar ();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }


   public void borrarCanvasMc ()
   {
       mc.borrarCanvas();
   }

   public void dibujarPuntosMc ()
   {
       mc.dibujarPuntos();
   }


   public void mostrarImagenMc ()
   {
       mc.mostrarImagen();
   }


   public int obtenerNumPuntosMc ()
   {
       return mc.obtenerNumPuntos();
   }


   public Punto obtenerPuntoMc (int i)
   {
       return mc.obtenerPunto (i);
   }


   public boolean obtenerAlVueloMc ()
   {
       return mc.obtenerAlVuelo();
   }


   public void habilitarTriangularMc ()
   {
       mc.habilitarTriangular();
   }


   public void habilitarSigPasoMc ()
   {
       mc.habilitarSigPaso();
   }


   public void deshabilitarSigPasoMc ()
   {
       mc.deshabilitarSigPaso();
   }


   public void prepararPuntoMc (int f, int y)
   {
       mc.prepararPunto (f, y);
   }


   public void deshabilitarAcabarMc ()
   {
       mc.deshabilitarAcabar();
   }


   public void habilitarAcabarMc ()
   {
       mc.habilitarAcabar();
   }


   public void destruirThread ()
   {
       if (threadActivo)
       {
           insDel.stop();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }

}
class insertarDelaunay extends Thread
{
   Delaunay d;
   Punto v;
   Arista e;
   boolean terminarInsercion = false; // Indica si hay que suspender el modo paso a paso
                              // y realizar la inserción sin dibujar los pasos
                              // ni parar
   int retardo; // retardo en milisegundos de cada paso. Si es 0 ->
                // -> espera pulsación "Sig Paso"

   public insertarDelaunay (Delaunay del, Punto ver, Arista ed)
   {
      d = del;
      v = ver;
      e = ed;
   }


   public void run()
   {
      if (d.insertarTodoPasoAPaso)
      {
          d.borrarCanvasMc();
          d.dibujarPuntosMc();
          d.mostrarImagenMc();
          for (int i=0; i<d.obtenerNumPuntosMc(); i++)
          {
             if(!d.yaExistia(d.obtenerPuntoMc(i)))
             {
                Arista e = d.enQueTriangulo(d.obtenerPuntoMc(i));  // busca el triángulo dentro del que se encuentra el vértice que se inserta
                auxRun (d.obtenerPuntoMc(i), e);
             }
          }
          d.insertarTodoPasoAPaso = false;

          if (!d.obtenerAlVueloMc())
             d.habilitarTriangularMc();

      }

      else
      {
          Arista e = d.enQueTriangulo(v);
          auxRun (v,e);

          if (!d.obtenerAlVueloMc())
             d.habilitarTriangularMc();
      }
      d.threadActivo = false;
   }

  private void auxRun (Punto v, Arista e)
  {
      d.prepararPuntoMc ((int)v.getX(),(int)-v.getY());  // dibuja punto que se está insertando (aún no insertado)
      d.mostrarImagenMc();

      if (terminarInsercion == false)
      {
         d.dibujarPuntosMc();
         d.dibujarTriangulo (e);
         retardar ();
      }
      Arista e1 = new Arista(v, null, null);
      Arista e2 = new Arista(e.obtenerDest(), null, null);
      Arista e3 = new Arista(v, null, null);
      Arista e4 = new Arista(e.obtenerSig().obtenerDest(), null, null);
      Arista e5 = new Arista(v, null, null);
      Arista e6 = new Arista(e.obtenerTwin().obtenerDest(), null, null);

      e1.fijarSig(e6);
      e2.fijarSig(e.obtenerSig());
      e3.fijarSig(e2);
      e4.fijarSig(e.obtenerSig().obtenerSig());
      e5.fijarSig(e4);
      e6.fijarSig(e);
      e.obtenerSig().obtenerSig().fijarSig(e5);
      e.obtenerSig().fijarSig(e3);
      e.fijarSig(e1);

      e1.fijarTwin(e2);
      e3.fijarTwin(e4);
      e5.fijarTwin(e6);

      d.insertarArista(e1);
      d.insertarArista(e2);
      d.insertarArista(e3);
      d.insertarArista(e4);
      d.insertarArista(e5);
      d.insertarArista(e6);

      if (terminarInsercion == false)
      {
            d.prepararPuntoMc ((int)v.getX(),(int)-v.getY());  // dibuja punto que se está insertando (aún no insertado)
            d.dibujarPuntosMc();
            d.dibujarTresAristas (e1,e3,e5);
            retardar();
      }

      legaliza(e6.obtenerSig());
      legaliza(e2.obtenerSig());
      legaliza(e4.obtenerSig());

      d.deshabilitarAcabarMc(); // Cuando acaba la triangulación, deshabilitar el botón
                                // Como el botón "Siguiente paso" se habilita y deshabilita en cada paso, no hay otra forma de saber cuándo deshabilitar "Acabar"

      d.redibujar();         // borra el último paso
      d.mostrarImagenMc();

   }

   private void legaliza(Arista e)
   {
      //legaliza the listaAristas so that the triangles satisfy the circle criterion

      if(d.aristaFicticia(e)) return;

      if (terminarInsercion == false)
      {
           d.dibujarPuntosMc();
           d.dibujarCirculoAristaPrueba (e, d.COLOR_CIRCULO_ARISTA_PRUEBA);
           retardar ();
      }

      if(d.dentroDeCirculo(e.obtenerDest(), e.obtenerSig().obtenerDest(),
		  e.obtenerTwin().obtenerDest(), e.obtenerTwin().obtenerSig().obtenerDest()))
      {

          if (terminarInsercion == false)
          {
                d.dibujarPuntosMc();
                d.dibujarCirculoAristaPrueba (e, d.COLOR_CIRCULO_ARISTA_MALA);
                retardar ();
          }

	  d.flip(e);

          if (terminarInsercion == false)
          {
                d.dibujarPuntosMc();
                d.dibujarAristaFlip (e);
                retardar ();
          }

          legaliza(e.obtenerSig().obtenerSig());
          legaliza(e.obtenerTwin().obtenerSig());
      }
   }

   public void retardar ()
   // Pausa la ejecución del thread, ya sea durante un momento o hasta que se
   // le indique un "resume" (pulsación de botón "Sig Paso")
   {
        d.esperandoPaso = true;

        if (d.retardo > 0)
        {
              try
              {
                    d.habilitarAcabarMc();
	            Thread.sleep(d.retardo);
              }
              catch (InterruptedException ie)
              {
              	    return;
	      }
        }
        else
        {
              d.habilitarSigPasoMc();
              this.suspend();
        }

        d.deshabilitarSigPasoMc();
        d.esperandoPaso = false;
        d.redibujar();
   }*/


}
