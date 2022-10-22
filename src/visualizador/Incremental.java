package visualizador;

// Triangulación Incremental Preordenada

// Clase que realiza una triangulación incremental con preordenación de los
// vértices que se van insertando desde la clase myCanvas.

import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Vector;


// -----------------------------------------------------------
// THREAD INSERTAR
// -----------------------------------------------------------

// Este thread inserta un vértice en la triangulación y la recalcula
// (Sólo llamar a este thread cuando se esté en modo paso a paso y se quiera
// insertar un vértice)

class Incremental extends Triangulacion{
   private final int INIT = 50;
   private int capacidadAristas, // capacidad del array de Aristas
               capacidadPuntos; // capacidad del array de vértices ordenado
   public Arista[] listaAristas; // array con todas las Aristas de la triangulación.
           //Además, también CONTIENE LAS DEL CIERRE: "next" es Arista siguiente
           // DEL CIERRE y "prev" es la anterior. La última Arista introducida
           // para cada nuevo punto insertado SIEMPRE será del cierre, por lo que
           // a partir de ésta se pueden obtener las Aristas del cierre
   public Punto [] listaPuntos; // array de vértices
   private ArrayList puntosVecinos;
   public int numAristas= 0 ,  // nº de Aristas que hay en listaAristas
               numPuntos = 0; // nº de vértices que hay en listaVert

   public final Color COLOR_Arista = Color.black,
                      COLOR_Arista_INSERTADA = Color.green,
                      COLOR_Arista_FINAL = Color.red,
                      COLOR_Punto_INSERTADO = Color.cyan;

   /*/ --- Variables para el thread ---
   private insertarIncremental insInc;
   public boolean esperandoPaso = false, //indica si está esperando a que se pulse el botón "Sig Paso". USAR SÓLO EN MODO PASO A PASO
                  threadActivo = false; // indica si el thread está en ejecución (si existe)
   public int retardo; // retardo en milésimas de segundo que se espera antes de realizar cada paso de la inserción de un pto.
   public boolean insertarTodoPasoAPaso = false; // Indica si hay que visualizar el proceso de inserción de TODOS los vértices desde
                                                 // el principio (sólo necesario para Delaunay; el resto lo hace siempre)
    * */

// MÉTODOS ------------------------------------------------------

   public void inicializar ()
   {
      capacidadPuntos = INIT;
      capacidadAristas = INIT;
      puntosVecinos = new ArrayList();
   }

   public void insertarPunto(Punto v)
   // Inserta el vértice "v" y sus correspondientes Aristas en la triangulación
   {
      int i;
      Arista ei,     // Arista a trazar desde el último punto insertado hasta
                   // el correspondiente vértice del cierre en cada iteración
                   // hasta encontrar el pto de soporte
           eSup,   // Arista del cierre cuyo pto inferior es "viCierre"
           eInf,   // Arista del cierre cuyo pto superior es "viCierre"
           eCierreSup, // Arista trazada desde el último punto insertado hasta
                       // el punto de soporte superior
           eCierreInf, // Arista trazada desde el último punto insertado hasta
                       // el punto de soporte inferior
           ePrimero;   // Arista desde el último pto insertado hasta el pivote
      Punto vi,   // punto que, en cada iteración del bucle de "insertarPunto", actuará
                   // como último punto insertado
      viCierre,    // pto del cierre con el que se va probando hasta dar con el
                   // soporte. Empieza siendo el pivote.
      pivote;      // último pto que se introdujo en el cierre ( = pto del cierre
                   // con coordenada X mayor)

v =     (Punto) v.rotado(getGrado()).clone();
      if(yaExistia(v)) return; // comprueba que el vértice que se quiere insertar no existía ya

      insertarOrdenadoPunto (v);
      listaAristas = new Arista[capacidadAristas];      // cada vez que se inserta un vértice...
      numAristas = 0;                          // ...se calculan todas las Aristas

      trianguloInicial();


        for (i=3; i<numPuntos; i++)
        {
          vi = listaPuntos[i];


          // Encontrar eSup y eInf a partir de la última Arista que se introdujo en el cierre
          if (listaAristas [numAristas-1].obtenerDest().getX() > listaAristas [numAristas-1].obtenerOrig().getX() ||
              (listaAristas [numAristas-1].obtenerDest().getX() == listaAristas [numAristas-1].obtenerOrig().getX() &&
               listaAristas [numAristas-1].obtenerDest().getY() < listaAristas [numAristas-1].obtenerOrig().getY()) )
          {
               eInf = listaAristas [numAristas-1];
               pivote = eInf.obtenerDest();
               eSup = eInf.obtenerSig();
          }
          else
          {
               eSup = listaAristas [numAristas-1];
               pivote = eSup.obtenerOrig();
               eInf = eSup.obtenerPrev();
          }

          ePrimero = new Arista (pivote, vi);
          insertarArista (ePrimero);
          ei = ePrimero;
          viCierre = pivote;

          // Traza Aristas desde el vértice insertado (vi) hasta los ptos del cierre
          // (partiendo del pivote hacia abajo)

          if (!izquierda (pivote,vi,eInf.obtenerOrig()))
          {
              do
              {
                   viCierre = eInf.obtenerOrig();
                   if (izquierda (viCierre,vi,eInf.obtenerPrev().obtenerOrig()) == false)
                   {
                        ei = new Arista (viCierre, vi);
                        insertarArista (ei);
                        eInf = eInf.obtenerPrev();
                   }
                   else break;
              } while (true);
              if (!viCierre.igualQue(pivote))
              {
                   ei = new Arista (viCierre,vi);
                   insertarArista (ei);
                   eInf = eInf.obtenerPrev();
              }
              eCierreInf = ei;
          }
          else
              eCierreInf = ePrimero;


          // Traza Aristas desde el vértice insertado (vi) hasta los ptos del cierre
          // (partiendo del pivote hacia arriba)

          viCierre = pivote;

          if (!izquierda (vi,pivote,eSup.obtenerDest()))
          {
              do
              {
                  viCierre = eSup.obtenerDest();
                  if (izquierda (vi,viCierre,eSup.obtenerSig().obtenerDest()) == false)
                  {
                       if (!viCierre.igualQue(ePrimero.obtenerOrig()))
                       {
                           ei = new Arista (viCierre, vi);
                           insertarArista (ei);
                       }
                       eSup = eSup.obtenerSig();
                  }
                  else break;
              } while (true);
          }
          if (!viCierre.igualQue (pivote))
          {
              ei = new Arista (vi,viCierre);
              insertarArista (ei);
              eCierreSup = ei;
              eSup = eSup.obtenerSig();
          }
          else
          {
              ePrimero.fijarDest (viCierre);
              ePrimero.fijarOrig (vi);
              eCierreSup = ePrimero;
          }

          // Introducir en el cierre las 2 Aristas que van al pto soporte
          // inferior y superior
          eSup.fijarPrev (eCierreSup);
          eInf.fijarSig (eCierreInf);

          eCierreSup.fijarPrev(eCierreInf);
          eCierreSup.fijarSig(eSup);
          eCierreInf.fijarSig(eCierreSup);
          eCierreInf.fijarPrev(eInf);
        }

        
      

   }


   public void trianguloInicial ()
   // Inserta las 3 primeras Aristas de la triangulación y define el sentido del
   // cierre
   {
      Arista e1, e2, e3;    // tres primeras Aristas de la triangulación
      boolean sentidoCCW; //True si el sentido en que se recorre el cierre
                          //convexo es opuesto a las agujas del reloj

      if (numPuntos > 1)
      {
          e1 = new Arista (listaPuntos[0], listaPuntos[1]);
          insertarArista (e1);
      }
      if (numPuntos > 2)
      {
          e2 = new Arista (listaPuntos[2], listaPuntos[0]);
          insertarArista (e2);
          e3 = new Arista (listaPuntos[1], listaPuntos[2]);
          insertarArista (e3);

          sentidoCCW = izqOLinea (listaPuntos[0], listaPuntos[1], listaPuntos [2]);

          if (sentidoCCW == true)
          {
              listaAristas[0].fijarSig (listaAristas[2]);
              listaAristas[0].fijarPrev (listaAristas[1]);
              listaAristas[1].fijarSig (listaAristas[0]);
              listaAristas[1].fijarPrev (listaAristas[2]);
              listaAristas[2].fijarSig (listaAristas[1]);
              listaAristas[2].fijarPrev (listaAristas[0]);
          }
          else
          {
              listaAristas[0].fijarOrig (listaPuntos[1]);
              listaAristas[0].fijarDest (listaPuntos[0]);
              listaAristas[1].fijarOrig (listaPuntos[0]);
              listaAristas[1].fijarDest (listaPuntos[2]);
              listaAristas[2].fijarOrig (listaPuntos[2]);
              listaAristas[2].fijarDest (listaPuntos[1]);

              listaAristas[0].fijarPrev (listaAristas[2]);
              listaAristas[0].fijarSig (listaAristas[1]);
              listaAristas[1].fijarPrev (listaAristas[0]);
              listaAristas[1].fijarSig (listaAristas[2]);
              listaAristas[2].fijarPrev (listaAristas[1]);
              listaAristas[2].fijarSig (listaAristas[0]);
          }
       }
   }


   public int obtenerNumAristas()
   // Devuelve el número de Aristas introducidas hasta el momento
   {
      return numAristas;
   }


   public Arista obtenerArista(int i)
   // devuelve la Arista introducida en el lugar "i"
   {
      return listaAristas[i];
   }


   public void insertarArista(Arista e)
   {
   // crea una nueva Arista, la introduce en el array de Aristas y aumenta el
   // tamaño de ese array si fuera necesario

      if(numAristas >= capacidadAristas)
      {
	 capacidadAristas += INIT;
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


   public void insertarOrdenadoPunto(Punto vi)
   {
   // crea una nueva Arista, la introduce en el array de Puntos y aumenta el
   // tamaño de ese array si fuera necesario
      Punto [] temp;
      int i=-1,j=0;
      boolean insertado = false;

      if(numPuntos >= capacidadPuntos)
      {
	 capacidadPuntos += INIT;
      }
      temp = new Punto[capacidadPuntos];

      if (numPuntos == 0)
      {
          temp [0] = new Punto (vi);
      }
      else
      {
          for(i = 0; i < numPuntos; i++)
          {
               if ( ((vi.getX() < listaPuntos[i].getX()) ||
                     (vi.getX() == listaPuntos[i].getX() &&
                      vi.getY() > listaPuntos[i].getY()))  && !insertado)
               {
                   temp[j] = new Punto (vi);
                   j++;
                   insertado = true;
               }
               temp[j] = listaPuntos[i];
               j++;
          }
      }
      if (j == i)
      {
          temp [j] = new Punto (vi);
      }
      listaPuntos = temp;
      numPuntos++;
   }


   public boolean yaExistia(Punto vi)
   // Devuelve si un vértice ya se encontraba en la triangulación
   {
      return (false);
   }


   public boolean izquierda (Punto a, Punto b, Punto c)
   // Devuelve true si el vértice C quedan a la izquierda de la
   // recta (a,b) en sentido a -> b, o si c está en línea con ab
   {
       return (izqOLinea (a,b,c));
   }


   private int areaConSigno( Punto a, Punto b, Punto c )
   // Devuelve el signo del área del triángulo abc
   {
      double area2;

      area2 = ( b.getX() - a.getX() ) * (double)( c.getY() - a.getY() ) -
              ( c.getX() - a.getX() ) * (double)( b.getY() - a.getY() );


      /* el área debe ser un entero */
      if      ( area2 >  0.5 ) return  1;
      else if ( area2 < -0.5 ) return -1;
      else                     return  0;
   }


   public boolean izqOLinea( Punto a, Punto b , Punto c)
   {
       return  areaConSigno( a, b, c) >= 0;
   }

   public Arista obtenerEdge(int i)
   // devuelve la Arista introducida en el lugar "i"
   {
      return listaAristas[i];
   }

   //AGREGADOS POR MÍ

   public boolean flip1Arista(Arista e, int j)
   {
   // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
      Vector puntosF = new Vector();
      Arista auxN;
      Punto punto1 = new Punto (0,0);
      Punto punto2 = new Punto (0,0);

      for (int i=0; i<numAristas; i++){
          if (listaAristas[i] != e)
              if ((listaAristas[i].obtenerOrig().igualQue(e.obtenerOrig())) || (listaAristas[i].obtenerOrig().igualQue(e.obtenerDest()))){
                  puntosF.addElement(listaAristas[i].obtenerDest());
              }
              else
                  if ((listaAristas[i].obtenerDest().igualQue(e.obtenerOrig())) || (listaAristas[i].obtenerDest().igualQue(e.obtenerDest()))){
                      puntosF.addElement(listaAristas[i].obtenerOrig());
                  }
      }

      punto1.asignar(buscarPunto (puntosF, e)); //Busca el primer punto para flipar
      punto2.asignar(buscarPunto (puntosF, e)); //Busca el segundo

      if ((punto1.igualQue(new Punto(0,0))) || (punto2.igualQue(new Punto(0,0))))//Si alguno devolvió el punto (0,0) => no se puede flipar
          return false;
      else{
          auxN = new Arista (punto1,punto2);//Crea la nueva arista
          if (cortaAristas(listaAristas, auxN, j)){
              return false;
          }
          else{
              listaAristas[j] = new Arista (punto1,punto2);//Reemplaza la arista anterior
              return true;
          }
      }

   }

   public boolean esFlipeable (Arista e, int j)
   {
   // Suponiendo que e no es Arista del triángulo incial, hace el flip de e
      Vector puntosF = new Vector();
      Arista auxN;
      Punto punto1 = new Punto (0,0);
      Punto punto2 = new Punto (0,0);

      for (int i=0; i<numAristas; i++){
          if (listaAristas[i] != e)
              if ((listaAristas[i].obtenerOrig().igualQue(e.obtenerOrig())) || (listaAristas[i].obtenerOrig().igualQue(e.obtenerDest()))){
                  puntosF.addElement(listaAristas[i].obtenerDest());
              }
              else
                  if ((listaAristas[i].obtenerDest().igualQue(e.obtenerOrig())) || (listaAristas[i].obtenerDest().igualQue(e.obtenerDest()))){
                      puntosF.addElement(listaAristas[i].obtenerOrig());
                  }
      }

      punto1.asignar(buscarPunto (puntosF, e)); //Busca el primer punto para flipar
      punto2.asignar(buscarPunto (puntosF, e)); //Busca el segundo

      if ((punto1.igualQue(new Punto(0,0))) || (punto2.igualQue(new Punto(0,0))))//Si alguno devolvió el punto (0,0) => no se puede flipar
          return false;
      else{
          auxN = new Arista (punto1,punto2);//Crea la nueva arista
          if (cortaAristas(listaAristas, auxN, j)){
              return false;
          }
          else{
              //listaAristas[j] = new Arista (punto1,punto2);//Reemplaza la arista anterior
              return true;
          }
      }

   }

   public boolean cortaAristas(Arista[] aristas, Arista a, int k){
	  boolean corta=false;
	  Arista ar=null;
	  double sig1,sig2;
	  double corteY;
	  double corteX;
	  int i=0;
	  Punto p1= a.obtenerOrig();
	  Punto p2= a.obtenerDest();
	  while(!corta && i < numAristas){
              if (i != k){//Para no comparar con la misma arista antes de flipar
		  ar=(Arista) aristas[i];
		  Punto p=ar.obtenerOrig();
		  Punto q=ar.obtenerDest();
                  if (((p.igualQue(p1)) && (q.igualQue(p2))) || ((q.igualQue(p1)) && (p.igualQue(p2)))){//Consulta si la arista flipada ya se encuentra en la lista de aristas
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
                              Punto ptoCorte=new Punto((int)corteX,(int)corteY);
                              if ((p.distance(ptoCorte)<=p.distance(q))&&(q.distance(ptoCorte)<=q.distance(p))){
                                      corta=true;
                              }
                      }
                      i++;
                  }
              }
              else
                  i++;
	  }
	  return corta;
  }

   public double Det(Punto a,Punto b,Punto z){
 	/*return ((b.getX()*(-z.getY())+z.getX()*(-a.getY())+a.getX()*(-b.getY()))-
 			(b.getX()*(-a.getY())+z.getX()*(-b.getY())+a.getX()*(-z.getY())));*/
        return (((a.x - z.x) * (b.y - z.y)) - ((b.x - z.x) * (a.y - z.y)));
 }

   public int identificaArista(Punto v1, Punto v2){//AGREGADO PARA BUSCAR UNA ARISTA, DADO DOS PUNTOS

      for(int i = 0; i < numAristas; i++){
	 if((listaAristas[i].obtenerDest().getX() == v1.getX() &&  listaAristas[i].obtenerDest().getY() == v1.getY())
	   &&(listaAristas[i].obtenerOrig().getX() == v2.getX() &&  listaAristas[i].obtenerOrig().getY() == v2.getY()))
                return i;

         else
             if((listaAristas[i].obtenerDest().getX() == v2.getX() &&  listaAristas[i].obtenerDest().getY() == v2.getY())
               &&(listaAristas[i].obtenerOrig().getX() == v1.getX() &&  listaAristas[i].obtenerOrig().getY() == v1.getY()))
                return i;

      }
      return -1;
   }

    private Punto buscarPunto(Vector puntosF, Arista a) {
        Punto auxP = new Punto(0,0);
        while (!puntosF.isEmpty()){
            auxP.asignar((Punto) puntosF.remove(0));
            if (puntosF.contains(auxP)){/*AGREGAR LA CONDICIÓN SOBRE TRIÁNGULO VACÍO:
                                         * FORMAR EL TRIÁNGULO
                                         * RECORRER EL VECTO CON LOS PUNTOS QUE RESTAN PREGUNTANDO SI ESTÁN DENTRO DEL TRIÁNGULO
                                         * SI NO SE ENCUENTRA NINGUNO ENTONCES ES UN PUNTO DE FLIPEO
                                         * SI SE ENCUENTRA, VOLVER AL WHILE PRINCIPAL
                                         */
                Triangulo t = new Triangulo(a.obtenerOrig(),a.obtenerDest(),auxP);
                if (EsTrianguloVacio(t))
                    return auxP;
            }
        }
        return new Punto(0,0);
    }

    public boolean EsTrianguloVacio(Triangulo triang)
    {
        double extSup = -triang.verticeA.getY();
        if(-triang.verticeB.getY() > extSup)
            extSup = -triang.verticeB.getY();
        if(-triang.verticeC.getY() > extSup)
            extSup = -triang.verticeC.getY();
        double extInf = -triang.verticeA.getY();
        if(-triang.verticeB.getY() < extInf)
            extInf = -triang.verticeB.getY();
        if(-triang.verticeC.getY() < extInf)
            extInf = -triang.verticeC.getY();
        double extDer = triang.verticeA.getX();
        if(triang.verticeB.getX() > extDer)
            extDer = triang.verticeB.getX();
        if(triang.verticeC.getX() > extDer)
            extDer = triang.verticeC.getX();
        double extIzq = triang.verticeA.getX();
        if(triang.verticeB.getX() < extIzq)
            extIzq = triang.verticeB.getX();
        if(triang.verticeC.getX() < extIzq)
            extIzq = triang.verticeC.getX();
        for(int i = 0; i < numPuntos; i++)
        {
            Punto ptoAux = listaPuntos[i];
            if(-ptoAux.getY() < extSup && -ptoAux.getY() > extInf && ptoAux.getX() < extDer && ptoAux.getX() > extIzq)
                if(CalculoLado(triang.verticeA, triang.verticeB, ptoAux) > 0)
                {
                    if(CalculoLado(triang.verticeB, triang.verticeC, ptoAux) > 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) > 0)
                        return false;
                } else
                if(CalculoLado(triang.verticeB, triang.verticeC, ptoAux) < 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) < 0)
                    return false;
        }

        return true;
    }

    public static double CalculoLado(Punto extremo1, Punto extremo2, Punto punto)
    {
        return (double)((Math.rint(extremo2.getX() * (-punto.getY()) - (-extremo2.getY()) * punto.getX()) - (extremo1.getX() * (-punto.getY()) - (-extremo1.getY()) * punto.getX())) + (extremo1.getX() * (-extremo2.getY()) - (-extremo1.getY()) * extremo2.getX()));
    }
    
    public int getNumeroPuntos(){
        return puntosVecinos.size();
    }

    public Punto getPunto(int i){
       return (Punto)puntosVecinos.get(i);
   }

    public void agregarVecinos(Arista a) {
        Punto origen, destino;
        if (!puntosVecinos.contains(a.obtenerOrig())){
            puntosVecinos.add(a.obtenerOrig());
            origen = a.obtenerOrig();
        }
        else
            origen = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerOrig()));

        if (!puntosVecinos.contains(a.obtenerDest())){
            puntosVecinos.add(a.obtenerDest());
            destino = a.obtenerDest();
        }
        else
            destino = (Punto) puntosVecinos.get(puntosVecinos.indexOf(a.obtenerDest()));

        origen.agregarVecino(destino);
        destino.agregarVecino(origen);

    }
    
    Arista obtenerRotatedEdge(int i) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return (new Arista(listaAristas[i].obtenerOrig().contraRotado(grado),
                           listaAristas[i].obtenerDest().contraRotado(grado)));
    }
/*
// --------------------------- Métodos gráficos ----------------------------

   public void dibujarAristas()
   // Dibuja EN EL BUFFER DE IMAGEN las Aristas que contiene la triangulación
   {
      for(int i = 0; i < numAristas; i++)
      {

              mc.prepararArista (listaAristas[i].obtenerOrig().getX(),
                                -listaAristas[i].obtenerOrig().getY(),
                                 listaAristas[i].obtenerDest().getX(),
                                -listaAristas[i].obtenerDest().getY(),
                                COLOR_Arista);
      }
   }


   public void dibujarPuntos()
   // Dibuja EN EL BUFFER DE IMAGEN los vértices que contiene la triangulación de Delaunay
   {
      for(int i = 0; i < numPuntos; i++)
      {
           mc.prepararPunto(listaPuntos[i].getX(),
                             -listaPuntos[i].getY());
      }
   }


   public void redibujar ()
   // Borra el recuadro de mensajes; borra la ventana y
   // dibuja EN EL BUFFER DE IMAGEN los vértices y la triangulación de Delaunay
   {
       mc.mostrarMensaje ("");
       mc.borrarCanvas();
       dibujarPuntos();
       dibujarAristas();
   }


//------------------------ Interfaz con el thread -------------------------

   public boolean devolverEsperandoPaso ()
   // Devuelve si el thread se encuentra esperando pulsación del botón "Sig Paso"
   {
       return (esperandoPaso);
   }


   public void fijarRetardo (int ret)
   // Fija el valor "ret" para la variable "retardo"
   {
       retardo = ret;
   }


   public void despertar ()
   // Despierta al thread (se ha pulsado el botón "Sig Paso")
   {
       insInc.resume();
   }


   public void terminarPasoAPaso ()
   // Termina la simulación de paso a paso forzando al thread a que
   // se ejecute sin pausas
   {
       if (esperandoPaso == true)
       {
             insInc.terminarInsercion = true;
             despertar ();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }


   public void dibujarArista (Arista e, Color c, String s)
   // Dibuja EN PANTALLA la Arista "e" con color "c"
   {
         mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                            e.obtenerOrig().getX(), -e.obtenerOrig().getY(), c);
         mc.mostrarMensaje (s);
         mc.mostrarImagen();
   }


   public void dibujarPunto (Punto v, Color c, String s)
   // Dibuja EN PANTALLA la Arista "e" con color "c"
   {
         mc.prepararPunto (v.getX(), -v.getY(), c);
         mc.mostrarMensaje (s);
         mc.mostrarImagen();
   }


   public void insertarTodoPasoAPaso ()
   {
        if (mc.pasoAPaso == true)  // si PASO A PASO -> crear thread de inserción de vértice
        {
           listaAristas = new Arista[capacidadAristas];      // cada vez que se inserta un vértice...
           numAristas = 0;                          // ...se calculan todas las Aristas
           listaPuntos = null;
           numPuntos = 0;

           insInc = new insertarIncremental (mc, this, null);
           insertarTodoPasoAPaso = true;
           insInc.start();
           threadActivo = true;
        }
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


   public boolean obtenerAlVueloMc ()
   {
       return mc.obtenerAlVuelo();
   }


   public Punto obtenerPuntoMc (int i)
   {
       return mc.obtenerPunto (i);
   }


   public void habilitarTriangularMc ()
   {
       mc.habilitarTriangular();
   }


   public boolean obtenerPasoAPasoMc()
   {
       return mc.pasoAPaso;
   }


   public void habilitarSigPasoMc ()
   {
       mc.habilitarSigPaso();
   }


   public void deshabilitarSigPasoMc ()
   {
       mc.deshabilitarSigPaso();
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
           insInc.stop();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }

}
class insertarIncremental extends Thread
{
   Incremental in;
   Punto v;
   myCanvas mc;
   boolean terminarInsercion = false; // Indica si hay que suspender el modo paso a paso
                              // y realizar la inserción sin dibujar los pasos
                              // ni parar
   int retardo; // retardo en milisegundos de cada paso. Si es 0 ->
                // -> espera pulsación "Sig Paso"

   public insertarIncremental (myCanvas ca, Incremental inc, Punto ver)
   {
      in = inc;
      v = ver;
      mc = ca;
   }


   public void run()
   {
      if (in.insertarTodoPasoAPaso)
      {
          in.borrarCanvasMc();
          in.dibujarPuntosMc();
          in.mostrarImagenMc();

          for (int i=0; i<in.obtenerNumPuntosMc(); i++)
          {
              if(!in.yaExistia(v)) // comprueba que el vértice que se quiere insertar no existía ya
                    in.insertarOrdenadoPunto (in.obtenerPuntoMc(i));
          }
          in.trianguloInicial();
          auxRun (in.obtenerPuntoMc(in.obtenerNumPuntosMc()-1));
          in.insertarTodoPasoAPaso = false;

          if (!in.obtenerAlVueloMc())
             in.habilitarTriangularMc();
      }

      else
      {
          auxRun (v);
          if (!in.obtenerAlVueloMc())
             in.habilitarTriangularMc();
      }
      in.threadActivo = false;
   }


   private void auxRun (Punto v)
   {
      int i;
      Arista ei,     // Arista a trazar desde el último punto insertado hasta
                   // el correspondiente vértice del cierre en cada iteración
                   // hasta encontrar el pto de soporte
           eSup,   // Arista del cierre cuyo pto inferior es "viCierre"
           eInf,   // Arista del cierre cuyo pto superior es "viCierre"
           eCierreSup, // Arista trazada desde el último punto insertado hasta
                       // el punto de soporte superior
           eCierreInf, // Arista trazada desde el último punto insertado hasta
                       // el punto de soporte inferior
           ePrimero;   // Arista desde el último pto insertado hasta el pivote
      Punto vi,   // punto que, en cada iteración del bucle de "insertarPunto", actuará
                   // como último punto insertado
      viCierre,    // pto del cierre con el que se va probando hasta dar con el
                   // soporte. Al principio será siguiente el anterior al pivote
      pivote;      // último pto que se introdujo en el cierre ( = pto del cierre
                   // con coordenada X mayor)

     //  Parada paso-a-paso: dibujar pto. insertado
     if (in.numPuntos >= 3 && terminarInsercion == false)
     {
          in.dibujarArista (in.listaAristas[0], in.COLOR_Arista_INSERTADA, "");
          in.dibujarArista (in.listaAristas[1], in.COLOR_Arista_INSERTADA, "");
          in.dibujarArista (in.listaAristas[2], in.COLOR_Arista_INSERTADA,
            "Se ordenan los ptos. lexicográficamente y se unen los 3 primeros ptos.");
          retardar ();
     }

     for (i=3; i<in.numPuntos; i++)
     {
          vi = in.listaPuntos[i];

          //  Parada paso-a-paso: dibujar pto. insertado
          if (terminarInsercion == false)
          {
                 in.dibujarPunto (vi, in.COLOR_Punto_INSERTADO,
                    "Punto a insertar");
                 retardar ();
          }


          // Encontrar eSup y eInf a partir de la última Arista que se introdujo en el cierre
          if (in.listaAristas [in.numAristas-1].obtenerDest().getX() > in.listaAristas [in.numAristas-1].obtenerOrig().getX() ||
              (in.listaAristas [in.numAristas-1].obtenerDest().getX() == in.listaAristas [in.numAristas-1].obtenerOrig().getX() &&
               in.listaAristas [in.numAristas-1].obtenerDest().getY() < in.listaAristas [in.numAristas-1].obtenerOrig().getY()) )
          {
               eInf = in.listaAristas [in.numAristas-1];
               pivote = eInf.obtenerDest();
               eSup = eInf.obtenerSig();
          }
          else
          {
               eSup = in.listaAristas [in.numAristas-1];
               pivote = eSup.obtenerOrig();
               eInf = eSup.obtenerPrev();
          }

          ePrimero = new Arista (pivote, vi);
          in.insertarArista (ePrimero);

          // Parada paso-a-paso: Arista hasta el último pto del cierre
          if (terminarInsercion == false)
          {
                in.redibujar();
                in.dibujarArista (ePrimero, in.COLOR_Arista_INSERTADA,
                                 "Arista hasta el ultimo pto. del cierre");
                retardar ();
          }

          ei = ePrimero;
          viCierre = pivote;

          // Traza Aristas desde el vértice insertado (vi) hasta los ptos del cierre
          // (partiendo del pivote hacia abajo)

          if (!in.izquierda (pivote,vi,eInf.obtenerOrig()))
          {
              do
              {
                   viCierre = eInf.obtenerOrig();
                   if (in.izquierda (viCierre,vi,eInf.obtenerPrev().obtenerOrig()) == false)
                   {
                        ei = new Arista (viCierre, vi);
                        in.insertarArista (ei);
                        eInf = eInf.obtenerPrev();
                        //  Parada paso-a-paso: añadir Arista y buscar pto.sop.inf.
                        if (terminarInsercion == false)
                        {
                             in.dibujarArista (ei, in.COLOR_Arista_INSERTADA,
                                 "Se añade Arista y se sigue buscando soporte inferior");
                             retardar ();
                        }
                   }
                   else break;
              } while (true);
              if (!viCierre.igualQue(pivote))
              {
                   ei = new Arista (viCierre,vi);
                   in.insertarArista (ei);
                   eInf = eInf.obtenerPrev();
              }
              eCierreInf = ei;
              // Parada paso-a-paso: añadir última Arista buscando pto.sop.inf
              if (terminarInsercion == false)
              {
                  in.dibujarArista (ei, in.COLOR_Arista_INSERTADA,
                      "Se añade Arista y se sigue buscando soporte inferior");
                  retardar();
              }
          }
          else
              eCierreInf = ePrimero;

          // Parada paso-a-paso: encontrado pto.sop.inf
          if (terminarInsercion == false)
          {
                in.dibujarArista (eCierreInf, in.COLOR_Arista_FINAL,
                    "Encontrado punto de soporte inferior");
                retardar ();
          }

          // Traza Aristas desde el vértice insertado (vi) hasta los ptos del cierre
          // (partiendo del pivote hacia arriba)

          viCierre = pivote;

          if (!in.izquierda (vi,pivote,eSup.obtenerDest()))
          {
              do
              {
                  viCierre = eSup.obtenerDest();
                  if (in.izquierda (vi,viCierre,eSup.obtenerSig().obtenerDest()) == false)
                  {
                       if (!viCierre.igualQue(ePrimero.obtenerOrig()))
                       {
                           ei = new Arista (viCierre, vi);
                           in.insertarArista (ei);
                           //  Parada paso-a-paso: añadir Arista y buscar pto.sop.sup.
                           if (terminarInsercion == false)
                           {
                              in.dibujarArista (ei, in.COLOR_Arista_INSERTADA,
                                 "Se añade Arista y se sigue buscando soporte superior");
                              retardar ();
                           }
                       }
                       eSup = eSup.obtenerSig();
                  }
                  else break;
              } while (true);
          }
          if (!viCierre.igualQue (pivote))
          {
              ei = new Arista (vi,viCierre);
              in.insertarArista (ei);
              eCierreSup = ei;
              eSup = eSup.obtenerSig();
              // Parada paso-a-paso: añadir última Arista buscando pto.sop.sup
              if (terminarInsercion == false)
              {
                  in.dibujarArista (ei, in.COLOR_Arista_INSERTADA,
                     "Se añade Arista y se sigue buscando soporte superior");
                  retardar();
              }
          }
          else
          {
              ePrimero.fijarDest (viCierre);
              ePrimero.fijarOrig (vi);
              eCierreSup = ePrimero;
          }

          //  Parada paso-a-paso: encontrado pto.sop.sup.
          if (terminarInsercion == false)
          {
               in.dibujarArista (eCierreSup, in.COLOR_Arista_FINAL,
                    "Encontrado punto de soporte superior");
               retardar ();
          }

          // Introducir en el cierre las 2 Aristas que van al pto soporte
          // inferior y superior
              eSup.fijarPrev (eCierreSup);
              eInf.fijarSig (eCierreInf);

              eCierreSup.fijarPrev(eCierreInf);
              eCierreSup.fijarSig(eSup);
              eCierreInf.fijarSig(eCierreSup);
              eCierreInf.fijarPrev(eInf);
       }

          in.deshabilitarAcabarMc(); // Cuando acaba la triangulación, deshabilitar el botón
                                // Como el botón "Siguiente paso" se habilita y deshabilita en cada paso, no hay otra forma de saber cuándo deshabilitar "Acabar"

          in.redibujar();         // borra el último paso
          in.mostrarImagenMc();
          if (!in.obtenerAlVueloMc())
             in.habilitarTriangularMc();

   }


   public void retardar ()
   // Pausa la ejecución del thread, ya sea durante un momento o hasta que se
   // le indique un "resume" (pulsación de botón "Sig Paso")
   {
        in.esperandoPaso = true;
        if (in.retardo > 0)
        {
              try
              {
                    in.habilitarAcabarMc();
	            Thread.sleep(in.retardo);
              }
              catch (InterruptedException ie)
              {
              	    return;
	      }
        }
        else
        {
              in.habilitarSigPasoMc();
              this.suspend();
        }

        in.deshabilitarSigPasoMc();
        in.esperandoPaso = false;
        in.redibujar();
   }*/

}

