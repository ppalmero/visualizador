package visualizador;

// Abanico

// Clase que realiza una triangulación en Abanico con preordenación de los
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


class Abanico extends Triangulacion{
   private final int INIT = 50;
   private int capacidadAristas, // capacidad del array de Aristas
               capacidadPuntos; // capacidad del array de vértices ordenado
   public Arista[] listaAristas; // array con todas las Aristas de la triangulación MENOS los no contiguos según su orden angular
   public Punto [] listaPuntos; // array de vértices
   private ArrayList puntosVecinos;
   public int numAristas= 0 ,  // nº de Aristas que hay en listaAristas
              numPuntos = 0; // nº de vértices que hay en listaVert

   public final Color COLOR_Arista = Color.black,
                      COLOR_Arista_INSERTADA = Color.green,
                      COLOR_Arista_ERRONEA = Color.red,
                      COLOR_Punto_INSERTADO = Color.cyan;

   // --- Variables para el thread ---
   //private insertarAbanico insAba;
   /*public boolean esperandoPaso = false, //indica si está esperando a que se pulse el botón "Sig Paso". USAR SÓLO EN MODO PASO A PASO
                  threadActivo = false; //indica si el thread está en ejecución
   public int retardo; // retardo en milésimas de segundo que se espera antes de realizar cada paso de la inserción de un pto.
*/
/////////////
// MÉTODOS //
/////////////


   public void inicializar ()
   {
      capacidadPuntos = INIT;
      capacidadAristas = INIT;
      listaPuntos = new Punto [capacidadPuntos];
      puntosVecinos = new ArrayList();
   }


   public void insertarPunto(Punto v)
   // Inserta el vértice "v" y sus correspondientes Aristas en la triangulación
   {
      int i, j;
      Arista vi, aux;
      int primeraAristaCierre;

      if(yaExistia(v)) return; // comprueba que el vértice que se quiere insertar no existía ya
      insertarOrdenadoPunto (v);
      numAristas = 0;                          // ...se calculan todas las Aristas
      listaAristas = new Arista[capacidadAristas];      // cada vez que se inserta un vértice...


       // Inserta Aristas entre el punto pivote y el resto de puntos
          for (i=1; i<numPuntos; i++)
          {
              vi = new Arista (listaPuntos[0], listaPuntos[i]);
              insertarArista (vi);
          }

          primeraAristaCierre = numAristas;

          // Inserta Aristas entre los puntos contiguos
          for (i=1; i<numPuntos-1; i++)
          {
              vi = new Arista (listaPuntos[i], listaPuntos[i+1]);
              vi.fijarSig (null);

              if (i == 1)
              {
                 listaAristas [0].fijarSig (vi);
                 vi.fijarPrev (listaAristas [0]);
              }
              else
              {
                 vi.fijarPrev (listaAristas [numAristas-1]);
                 listaAristas [numAristas-1].fijarSig (vi);
              }

              insertarArista (vi);
          }

          // Inserta Aristas entre los puntos NO contiguos si es posible
          if (numPuntos > 3)
          {
              i=primeraAristaCierre;
              aux = new Arista (listaAristas[i]);
              while (aux.obtenerSig() != null)
              {
                  aux = triangularBolsillo(aux);
              }
          }
   }


   public Arista triangularBolsillo (Arista ar)
   // Triangula el bolsillo formado por los puntos con índices i-1, i, i+1 de la
   // lista de puntos del cierre que se va formando.
   // Si puede trazar Arista entre i-1 e i+1, borra i de la lista de puntos exteriores
   {
      Arista aux = new Arista (ar);
      Arista sigAux = new Arista (aux.obtenerSig());
      if (izquierda (aux.obtenerOrig(), sigAux.obtenerDest(), aux.obtenerDest()))
      {
          return sigAux;
      }
      else
      {
          Arista vi = new Arista (aux.obtenerOrig(), sigAux.obtenerDest());
          vi.fijarSig (sigAux.obtenerSig());
          vi.fijarPrev (aux.obtenerPrev());
          insertarArista (vi);
          if (sigAux.obtenerSig() != null)
             sigAux.obtenerSig().fijarPrev (vi);
          if (vi.obtenerPrev() != null)
          {
             aux.obtenerPrev().fijarSig (vi);
             return aux.obtenerPrev();
          }
          else
          {
             return (vi);
          }
      }
   }


   public int obtenerNumAristas()
   // Devuelve el número de Aristas introducidas hasta el momento
   {
      return numAristas;
   }


   public void insertarArista(Arista e)
   // crea una nueva Arista, la introduce en el array de Aristas y aumenta el
   // tamaño de ese array si fuera necesario
   {

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
   // crea un nuevo Punto, la introduce en el array de Puntos y aumenta el
   // tamaño de ese array si fuera necesario.
   // El primer vértice será el que tenga mayor coordenada Y
      Punto [] temp;
      int i;

      if(numPuntos >= capacidadPuntos)
      {
	 capacidadPuntos += INIT;
         temp = new Punto[capacidadPuntos];
         for (i=0; i<numPuntos; i++)
             temp [i] = listaPuntos[i];

         listaPuntos = temp;
      }

      if (numPuntos > 0 && (vi.getY() > listaPuntos[0].getY() ||
                              (vi.getY() == listaPuntos[0].getY() &&
                               vi.getX() < listaPuntos[0].getX()) ) )
      {
         listaPuntos [numPuntos] = listaPuntos[0];
         listaPuntos [0] = vi;
      }
      else
      {
         listaPuntos [numPuntos] = vi;
      }

      numPuntos++;

      if (numPuntos > 2)
          reordenarPuntos();
   }


   private void reordenarPuntos ()
   // Ordena los vértices de listaPuntos dependiendo del ángulo que forman
   // con el primer elemento de esa lista
   {
       Ordena(1, numPuntos-1);
   }


   private void Ordena(int lo0, int hi0)
   // Aplica el algoritmo quicksort con criterios de medida de ángulos para la
   // ordenación de los vértices de ListaPuntos
   {
      if (lo0 >= hi0) {
	return;
      }
      Punto mid = new Punto(listaPuntos [hi0]);
      int lo = lo0;
      int hi = hi0 - 1;
      while (lo <= hi)
      {
	  while (lo<=hi && ((compararAngulos(listaPuntos [lo], mid)==1)||(compararAngulos(listaPuntos [lo], mid)==0))) {
	     lo++;
      }
	  while (lo<=hi && ((compararAngulos(listaPuntos [hi], mid)==-1)||(compararAngulos(listaPuntos [hi], mid)==0))) {
	    hi--;
	  }

	  if (lo < hi)
	    {
	      Intercambia(lo, hi);
	    }

	}
      Intercambia(lo,hi0);
      Ordena(lo0, lo-1);
      Ordena(lo+1, hi0);
    }


   private int compararAngulos(Punto tpi, Punto tpj)
   // Devuelve el ángulo que forma tpi y tpj
   // -1,0,+1 si p1 < p2, =, or > respectivamente ("<" significa menor ángulo)
   {
      int a;             //area
      int x, y;          //proyecciones de ri y rj en el 1er. cuadrante
      Punto pi, pj;
      pi = tpi;
      pj = tpj;
      Punto cabeza = new Punto(listaPuntos [0]);
      a = areaConSigno( cabeza, pi, pj );
      if (a > 0)
	return -1;
      else if (a < 0)
	return 1;
      else { // Colineal con listaPuntos[0]
	x =  (int) (Math.abs(pi.getX() - listaPuntos[0].getX()) - Math.abs(pj.getX() - listaPuntos[0].getX()));
	y =  (int) (Math.abs(pi.getY() - listaPuntos[0].getY()) - Math.abs(pj.getY() - listaPuntos[0].getY()));

	if ( (x < 0) || (y < 0) )
	  return -1;
	else if ( (x > 0) || (y > 0) ) {
	  return 1;
	}
	else return 0; // los puntos coinciden
	}
   }


   private void Intercambia (int a, int b)
   // Intercambia los vértices que se encuentran en las posiciones "a" y "b"
   // en "listaPuntos"
   {
      Punto aux = new Punto (listaPuntos[a]);
      listaPuntos [a] = listaPuntos [b];
      listaPuntos [b] = aux;
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

   public boolean esFlipeable(Arista e, int j){
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
                                         * RECORRER EL VECTOR CON LOS PUNTOS QUE RESTAN PREGUNTANDO SI ESTÁN DENTRO DEL TRIÁNGULO
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

    public boolean EsTrianguloVacio(Triangulo triang, Punto p)
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

            Punto ptoAux = p;
            if(-ptoAux.getY() < extSup && -ptoAux.getY() > extInf && ptoAux.getX() < extDer && ptoAux.getX() > extIzq)
                if(CalculoLado(triang.verticeA, triang.verticeB, ptoAux) > 0)
                {
                    if(CalculoLado(triang.verticeB, triang.verticeC, ptoAux) > 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) > 0)
                        return false;
                } else
                if(CalculoLado(triang.verticeB, triang.verticeC, ptoAux) < 0 && CalculoLado(triang.verticeC, triang.verticeA, ptoAux) < 0)
                    return false;


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

/*/ --------------------------- Métodos gráficos ----------------------------

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
   // Dibuja EN EL BUFFER DE IMAGEN los vértices que contiene la triangulación
   {
      for(int i = 0; i < numPuntos; i++)
      {
           mc.prepararPunto(listaPuntos[i].getX(),
                             -listaPuntos[i].getY());
      }
   }


   public void redibujar ()
   {
       mc.mostrarMensaje ("");
       mc.borrarCanvas();
       dibujarPuntosMc();
       dibujarAristas();
   }
   // Borra el recuadro de mensajes; borra la ventana y
   // dibuja EN EL BUFFER DE IMAGEN los vértices y la triangulación de Delaunay


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
       insAba.resume();
   }


   public void terminarPasoAPaso ()
   // Termina la simulación de paso a paso forzando al thread a que
   // se ejecute sin pausas
   {
       if (esperandoPaso == true)
       {
             insAba.terminarInsercion = true;
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


   public void mostrarMensaje (String s)
   // Muestra en la parte inferior de la ventana el mensaje "s"
   {
        mc.mostrarMensaje (s);
   }


   public void insertarTodoPasoAPaso ()
   {
       if (mc.pasoAPaso == true)  // si PASO A PASO -> crear thread de inserción de vértice
       {
           listaAristas = new Arista[capacidadAristas];      // cada vez que se inserta un vértice...
           numAristas = 0;                          // ...se calculan todas las Aristas
           listaPuntos = null;
           numPuntos = 0;
           listaPuntos = new Punto [capacidadPuntos];

           insAba = new insertarAbanico (this);
           insertarTodoPasoAPaso = true;
           insAba.start();
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


   public void habilitarAcabarMc ()
   {
       mc.habilitarAcabar();
   }


   public void deshabilitarAcabarMc ()
   {
       mc.deshabilitarAcabar();
   }


   public void destruirThread ()
   {
       if (threadActivo)
       {
           insAba.stop();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }

}
class insertarAbanico extends Thread
{
   Abanico ab;
   Punto v;
   boolean terminarInsercion = false; // Indica si hay que suspender el modo paso a paso
                              // y realizar la inserción sin dibujar los pasos
                              // ni parar
   int retardo; // retardo en milisegundos de cada paso. Si es 0 ->
                // -> espera pulsación "Sig Paso"

   public insertarAbanico (Abanico aba)
   {
      ab = aba;
   }

   public void run()
   {
      if (ab.insertarTodoPasoAPaso)
      {
          ab.borrarCanvasMc();
          ab.dibujarPuntosMc();
          ab.mostrarImagenMc();

          for (int i=0; i<ab.obtenerNumPuntosMc(); i++)
          {
              if(!ab.yaExistia(v)) // comprueba que el vértice que se quiere insertar no existía ya
                    ab.insertarOrdenadoPunto (ab.obtenerPuntoMc (i));
          }

          auxRun ();
          ab.insertarTodoPasoAPaso = false;

          if (!ab.obtenerAlVueloMc())
             ab.habilitarTriangularMc();
      }

      else
      {
          auxRun ();
          if (!ab.obtenerAlVueloMc())
             ab.habilitarTriangularMc();
      }
      ab.threadActivo = false;
   }


   private void auxRun ()
   {
      int i, j, primeraAristaCierre, numAux;
      Arista vi, aux, auxAnt;
      String s;

      if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
      {
          ab.mostrarMensaje ("Ordenando puntos según el ángulo que forman");
          retardar();
      }

      if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
      {
          ab.dibujarPunto (ab.listaPuntos[0],ab.COLOR_Punto_INSERTADO,
                "Se toma el punto superior de la nube de puntos como pivote");
          retardar();
      }

      for (i=1; i<ab.numPuntos; i++)
      {
          vi = new Arista (ab.listaPuntos[0], ab.listaPuntos[i]);
          ab.insertarArista (vi);

          if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
          {
              ab.dibujarArista (vi, ab.COLOR_Arista_INSERTADA,
                   "Insertando Arista desde el pivote hacia punto externo");
              retardar();
          }
      }

      primeraAristaCierre = ab.numAristas;

      // Inserta Aristas entre los puntos contiguos
      for (i=1; i<ab.numPuntos-1; i++)
      {
          vi = new Arista (ab.listaPuntos[i], ab.listaPuntos[i+1]);
          vi.fijarSig (null);

          if (i == 1)
          {
             ab.listaAristas [0].fijarSig (vi);
             vi.fijarPrev (ab.listaAristas [0]);
          }
          else
          {
             vi.fijarPrev (ab.listaAristas [ab.numAristas-1]);
             ab.listaAristas [ab.numAristas-1].fijarSig (vi);
          }

          ab.insertarArista (vi);

          if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
          {
              ab.dibujarArista (vi, ab.COLOR_Arista_INSERTADA,
                   "Insertando Aristas entre puntos externos contiguos");
              retardar();
          }
      }



      // Inserta Aristas entre los puntos NO contiguos si es posible
      if (ab.numPuntos > 3)
      {
          i=primeraAristaCierre;
          numAux = ab.numAristas;
          aux = new Arista (ab.listaAristas[i]);
          auxAnt = aux;
          while (aux.obtenerSig() != null)
          {
              aux = ab.triangularBolsillo(aux);

              if (numAux == ab.numAristas)
              {
                 if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
                 {
                    ab.dibujarPunto (aux.obtenerOrig(), ab.COLOR_Punto_INSERTADO,"Punto EXTERIOR");
                    retardar();
                 }
              }
              else
              {
                 if (ab.obtenerPasoAPasoMc() == true && terminarInsercion == false)
                 {
                    ab.dibujarPunto (auxAnt.obtenerDest(), ab.COLOR_Punto_INSERTADO,"");
                    if (!aux.igualQue(ab.listaAristas[primeraAristaCierre]))
                       s = "Punto INTERIOR. Eliminarlo del cierre, trazar Arista y retroceder";
                    else
                       s = "Punto INTERIOR. Eliminarlo del cierre, trazar Arista. No retroceder";

                    ab.dibujarArista (ab.listaAristas[ab.numAristas-1], ab.COLOR_Arista_INSERTADA,s);
                    retardar();
                 }
              }

              numAux = ab.numAristas;
              auxAnt = aux;

          }
      }

      ab.deshabilitarAcabarMc(); // Cuando acaba la triangulación, deshabilitar el botón
                                // Como el botón "Siguiente paso" se habilita y deshabilita en cada paso, no hay otra forma de saber cuándo deshabilitar "Acabar"

      ab.redibujar();         // borra el último paso
      ab.mostrarImagenMc();
      if (!ab.obtenerAlVueloMc())
          ab.habilitarTriangularMc();
   }


   public void retardar ()
   // Pausa la ejecución del thread, ya sea durante un momento o hasta que se
   // le indique un "resume" (pulsación de botón "Sig Paso")
   {
        ab.esperandoPaso = true;
        if (ab.retardo > 0)
        {
              try
              {
                    ab.habilitarAcabarMc();
	            Thread.sleep(ab.retardo);
              }
              catch (InterruptedException ie)
              {
              	    return;
	      }
        }

        else
        {
              ab.habilitarSigPasoMc();
              this.suspend();
        }

        ab.deshabilitarSigPasoMc();
        ab.esperandoPaso = false;
        ab.redibujar();
   }
*/
}

