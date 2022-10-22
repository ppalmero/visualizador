package visualizador;

// Divide y vencerás

// Clase que realiza una triangulación por Divide y vencerás con preordenación
// de los vértices que se van insertando desde la clase myCanvas.

import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Vector;


// -----------------------------------------------------------
// THREAD INSERTAR DIVIDE
// -----------------------------------------------------------

// Este thread inserta un vértice en la triangulación y la recalcula
// (Sólo llamar a este thread cuando se esté en modo paso a paso y se quiera
// insertar un vértice)

public class Divide extends Triangulacion{
   private final int INIT = 10;
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
   private boolean sentidoCCW; //True si el sentido en que se recorre el cierre
                               //convexo es opuesto a las agujas del reloj

   public final Color COLOR_Arista = Color.black,
                      COLOR_Arista_INSERTADA = Color.green,
                      COLOR_LINEA_BORRADA = Color.white,
                      COLOR_LINEA_VERTICAL = Color.red,
                      COLOR_LINEA_ACTIVA = Color.blue,
                      COLOR_Punto_INSERTADO = Color.cyan;
   //private myCanvas mc;

   // --- Variables para el thread ---
   //private insertarDivide insDiv;
   public boolean esperandoPaso = false, //indica si está esperando a que se pulse el botón "Sig Paso". USAR SÓLO EN MODO PASO A PASO
                  threadActivo = false; // indica si el thread está en ejecución (si existe)
   public int retardo; // retardo en milésimas de segundo que se espera antes de realizar cada paso de la inserción de un pto.


/////////////
// MÉTODOS //
/////////////


   public void inicializar ()
   {
      capacidadPuntos = INIT;
      capacidadAristas = INIT;
      puntosVecinos = new ArrayList();
   }

   public void insertarPunto(Punto v)
   {
      listaAristas lAr;

      if (yaExistia(v)) return; // comprueba que el vértice que se quiere insertar no existía ya

      insertarOrdenadoPunto (v);
      listaAristas = new Arista [capacidadAristas];
      numAristas = 0;

      
          lAr = divide(listaPuntos, numPuntos);
          numAristas = lAr.nElem;
          listaAristas = lAr.listaAristas;
      
   }


   public listaAristas divide (Punto[] lVert, int nVert)
   // Divide la lista de Puntos en dos mitades, y devuelve el resultado de
   // unir mediante Aristas esas dos mitades
   {
      int mitad, coordXMitad;
      Punto [] mitad1, mitad2;
      listaAristas div1, div2;

      if (nVert <= 1 )
      {
          return new listaAristas (0);
      }

      if (nVert == 2 || nVert == 3)
      {
           return trianguloInicial(lVert, nVert);
      }

      else
      {
          mitad = nVert/2;
          mitad1 = partir (lVert,0,mitad);
          mitad2 = partir (lVert,mitad,nVert-mitad);
          coordXMitad = (int)mitad1[mitad-1].getX() + (int)mitad2[0].getX();
          coordXMitad /= 2;

          

          div1 = divide (mitad1, mitad);
          div2 = divide (mitad2, nVert-mitad);

          

          return mezclar (div1, div2);
      }
   }


   private Punto [] partir (Punto [] l, int primero, int nElem)
   // Devuelve una lista nueva copiando "nElem" elementos de la lista "l" que
   // se empezando por la posición "primero"
   {
       Punto [] resul = new Punto [nElem];
       for (int i = 0; i<nElem; i++)
       {
           resul[i] = l[i+primero];
       }
       return resul;
   }


   public listaAristas mezclar (listaAristas lAr1, listaAristas lAr2)
   // Devuelve la lista de Aristas resultado de unir los vértices contenidos en
   // las listas de Aristas lAr1 con lAr2 (1=izq, 2=der)
   {
      int i;
      boolean seguirIzq = true, // Indica si se puede seguir recorriendo el contorno izq en la direccion que corresponda en ese momento para trazar Aristas hacia el contorno derecho
              seguirDer = true; // Análogo a seguirIzq
      Arista ei,                  // Arista a insertar en cada iteración
           eSup1, eSup2,        // Finalmente indicarán las Aristas anterior y siguiente a cierreSup (1=izq y 2=der)
           eInf1, eInf2,        // Igual que eSup, pero con la parte inferior
           eCierreSup,          // Última Arista introducida en la parte superior entre las dos mitades (pertenece al cierre)
           eCierreInf,          // Última Arista introducida en la parte inferior entre las dos mitades (pertenece al cierre)
           ePrimero;            // Primera Arista que se traza entre las dos mitades (entre los 2 pivotes)
      Punto vi1, vi2,          // Vértices que forman los extremos de ei en cada iteración
             pivote1, pivote2;  // Pivotes de las 2 mitades (1=izq, 2 = der)
      listaAristas mezcla;      // Lista de Aristas con el esultado final de la mezcla de las 2 mitades

          // Encontrar eSup y eInf a partir de la última Arista que se introdujo en el cierre
          mezcla = unir (lAr1, lAr2);
          eInf1 = obtenerAristaDestinoPivoteMax(mezcla, 0, lAr1.nElem);  // Arista con destino = pivote1
          pivote1 = eInf1.obtenerDest();     // pto + a la derecha del contorno izq
          eSup1 = eInf1.obtenerSig();
          vi1 = pivote1;   // vértice de contorno izq que se va uniendo

          eInf2 = obtenerAristaDestinoPivoteMin(mezcla, lAr1.nElem, lAr2.nElem);  // Arista con destino = pivote2
          pivote2 = eInf2.obtenerDest();     // pto + a la izq. del contorno der.
          eSup2 = eInf2;
          vi2 = pivote2;   // con Aristas con v2 (del contorno der.)
          eInf2 = eInf2.obtenerSig();

          // primera Arista
          ei = new Arista (vi1, vi2);
          ePrimero = ei;
          mezcla = insertarArista (ei, mezcla);
          eCierreSup = ei;
          eCierreInf = ei;

          

          if (izquierda (vi1, vi2, eInf1.obtenerOrig()) )
              seguirIzq = false;

          if (izquierda (vi1, vi2, eInf2.obtenerSig().obtenerOrig()) )
              seguirDer = false;

          // Traza Aristas entre las 2 mitades, desde los pivotes hacia abajo
          while (seguirIzq || seguirDer)
          {

              // Si se puede trazar una Arista entre el siguiente vértice de la
              // izq y el actual de la der se hace y, si se puede, se prepara el
              // siguiente izquierdo para la próxima vuelta del bucle
              if (seguirIzq)
              {
                  vi1 = eInf1.obtenerOrig();
                  ei = new Arista (vi1, vi2);
                  mezcla = insertarArista (ei, mezcla);
                  eInf1 = eInf1.obtenerPrev();
                  if (lAr1.nElem == 1 || izquierda (vi1, vi2, eInf1.obtenerOrig()) )
                      seguirIzq = false;

                  

                  // También se comprueba si se podrá trazar una Arista hacia el
                  // siguiente vértice derecho desde el vértice izquierdo que
                  // acabamos de unir
                  if (izquierda (vi1, vi2, eInf2.obtenerSig().obtenerOrig()) )
                      seguirDer = false;
                  else
                      seguirDer = true;
              }

              if (seguirDer)
              {
                  if (lAr2.nElem == 1)
                      vi2 = eInf2.obtenerOrig();
                  else
                      vi2 = eInf2.obtenerDest();
                  ei = new Arista (vi1, vi2);
                  mezcla = insertarArista (ei, mezcla);
                  eInf2 = eInf2.obtenerSig();
                  if (lAr2.nElem == 1 || izquierda (vi1, vi2, eInf2.obtenerDest()) )
                      seguirDer = false;

                  

                  if (izquierda (vi1, vi2, eInf1.obtenerOrig()) )
                      seguirIzq = false;
                  else
                      seguirIzq = true;
              }
              eCierreInf = ei;
          }

          // Comprobaciones partiendo de la Arista que se trazó entre los 2 pivotes
          vi1 = pivote1;
          vi2 = pivote2;
          seguirIzq = true;
          seguirDer = true;

          // Si ePrimero es la Arista superior, invertir su sentido
          if (izquierda (vi2, vi1, eSup1.obtenerSig().obtenerOrig()) )
              seguirIzq = false;

          if (izquierda (vi2, vi1, eSup2.obtenerOrig()) )
              seguirDer = false;

          if (!seguirIzq && !seguirDer)
          {
              ePrimero.fijarOrig (pivote2);
              ePrimero.fijarDest (pivote1);
          }

          while (seguirIzq || seguirDer)
          {

              if (seguirIzq)
              {
                  if (lAr1.nElem == 1)
                      vi1 = eSup1.obtenerOrig();

                  else
                      vi1 = eSup1.obtenerDest();
                  ei = new Arista (vi2, vi1);
                  mezcla = insertarArista (ei, mezcla);
                  eSup1 = eSup1.obtenerSig();
                  if (lAr1.nElem == 1 || izquierda (vi2, vi1, eSup1.obtenerDest()) )
                      seguirIzq = false;

                  

                  if (izquierda (vi2, vi1, eSup2.obtenerOrig()) )
                      seguirDer = false;
                  else
                      seguirDer = true;
              }

              if (seguirDer)
              {
                  vi2 = eSup2.obtenerOrig();
                  ei = new Arista (vi2, vi1);
                  mezcla = insertarArista (ei, mezcla);
                  eSup2 = eSup2.obtenerPrev();
                  if (lAr2.nElem == 1 || izquierda (vi2, vi1, eSup2.obtenerOrig()) )
                     seguirDer = false;

                  

                  if (izquierda (vi2, vi1, eSup1.obtenerSig().obtenerOrig() ) )
                     seguirIzq = false;
                  else
                     seguirIzq = true;
              }

              eCierreSup = ei;
          }

          // Introducir en el cierre las 2 Aristas que van al pto soporte
          // inferior y superior
          eInf1.fijarSig (eCierreInf);
          eCierreInf.fijarSig (eInf2);
          eInf2.fijarPrev (eCierreInf);
          eCierreInf.fijarPrev (eInf1);

          eSup1.fijarPrev (eCierreSup);
          eCierreSup.fijarPrev (eSup2);
          eSup2.fijarSig (eCierreSup);
          eCierreSup.fijarSig (eSup1);

          if (lAr1.nElem == 1)
          {
              if (!izquierda (eInf1.obtenerOrig(), eInf1.obtenerDest(), eInf1.obtenerSig().obtenerDest()) &&
                  !izquierda (eInf1.obtenerOrig(), eInf1.obtenerDest(), eInf1.obtenerPrev().obtenerOrig()) )
              {
                 vi1 = new Punto (eInf1.obtenerOrig());
                 eInf1.fijarOrig (eInf1.obtenerDest());
                 eInf1.fijarDest (vi1);
              }
          }

          if (lAr2.nElem == 1)
          {
              if (!izquierda (eInf2.obtenerOrig(), eInf2.obtenerDest(), eInf2.obtenerSig().obtenerDest()) &&
                  !izquierda (eInf2.obtenerOrig(), eInf2.obtenerDest(), eInf2.obtenerPrev().obtenerOrig()) )
              {
                 vi2 = new Punto (eInf2.obtenerOrig());
                 eInf2.fijarOrig (eInf2.obtenerDest());
                 eInf2.fijarDest (vi2);
              }
          }

          if (eCierreSup.obtenerDest().getX() == eCierreInf.obtenerOrig().getX() &&
              eCierreSup.obtenerDest().getY() == eCierreInf.obtenerOrig().getY() )
          {
             eCierreSup.fijarSig (eCierreInf);
             eCierreInf.fijarPrev (eCierreSup);
          }

          if (eCierreSup.obtenerOrig().getX() == eCierreInf.obtenerDest().getX() &&
              eCierreSup.obtenerOrig().getY() == eCierreInf.obtenerDest().getY() )
          {
             eCierreInf.fijarSig (eCierreSup);
             eCierreSup.fijarPrev (eCierreInf);
          }

          return mezcla;
   }

   private listaAristas trianguloInicial (Punto [] lVert, int nVert)
   // Inserta las 3 primeras Aristas de la triangulación y define el sentido del
   // cierre
   {
      Arista e1, e2, e3;    // tres primeras Aristas de la triangulación
      boolean sentidoCCW; //True si el sentido en que se recorre el cierre
                          //convexo es opuesto a las agujas del reloj

      listaAristas lAr = new listaAristas(INIT);
      e1 = null;

      if (nVert >= 2)
      {
          

          e1 = new Arista (lVert[0], lVert[1]);
          e1.fijarSig (e1);
          e1.fijarPrev (e1);
          lAr = insertarArista (e1, lAr);

          
      }

      if (nVert == 3)
      {
          e2 = new Arista (lVert[2], lVert[0]);
          lAr = insertarArista (e2,lAr);
          e3 = new Arista (lVert[1], lVert[2]);
          lAr = insertarArista (e3,lAr);

          

          sentidoCCW = izqOLinea (lVert[0], lVert[1], lVert [2]);

          if (sentidoCCW == true)
          {
              lAr.listaAristas[0].fijarSig (lAr.listaAristas[2]);
              lAr.listaAristas[0].fijarPrev (lAr.listaAristas[1]);
              lAr.listaAristas[1].fijarSig (lAr.listaAristas[0]);
              lAr.listaAristas[1].fijarPrev (lAr.listaAristas[2]);
              lAr.listaAristas[2].fijarSig (lAr.listaAristas[1]);
              lAr.listaAristas[2].fijarPrev (lAr.listaAristas[0]);
          }
          else
          {
              lAr.listaAristas[0].fijarOrig (lVert[1]);
              lAr.listaAristas[0].fijarDest (lVert[0]);
              lAr.listaAristas[1].fijarOrig (lVert[0]);
              lAr.listaAristas[1].fijarDest (lVert[2]);
              lAr.listaAristas[2].fijarOrig (lVert[2]);
              lAr.listaAristas[2].fijarDest (lVert[1]);

              lAr.listaAristas[0].fijarPrev (lAr.listaAristas[2]);
              lAr.listaAristas[0].fijarSig (lAr.listaAristas[1]);
              lAr.listaAristas[1].fijarPrev (lAr.listaAristas[0]);
              lAr.listaAristas[1].fijarSig (lAr.listaAristas[2]);
              lAr.listaAristas[2].fijarPrev (lAr.listaAristas[1]);
              lAr.listaAristas[2].fijarSig (lAr.listaAristas[0]);
          }
       }

       return lAr;
   }


   private Arista obtenerAristaDestinoPivoteMax (listaAristas lAr, int orig, int nElem)
   // Devuelve la Arista de lAr cuyo vértice destino tiene mayor coordenada X.
   // Sólo busca desde el elemento orig hasta el orig+nElem
   {
       Arista i;
       Arista max;

       if (nElem == 1)
          return (lAr.listaAristas[orig]);

       max = new Arista (new Punto (0,0),new Punto (0,0));

       i = lAr.listaAristas[orig+nElem-1];
       do
       {
             if (max.obtenerDest().getX() < i.obtenerDest().getX())
                 max = i;
             i = i.obtenerSig();
       } while (i != lAr.listaAristas[orig+nElem-1]);
       return max;
   }


   private Arista obtenerAristaDestinoPivoteMin (listaAristas lAr, int orig, int nElem)
   // Devuelve la Arista de lAr cuyo vértice destino tiene menor coordenada X.
   // Sólo busca desde el elemento orig hasta el orig+nElem
   {
       Arista i;
       Arista min;

       if (nElem == 1)
       {
          Punto v = new Punto (lAr.listaAristas[orig].obtenerOrig());
          lAr.listaAristas[orig].fijarOrig(lAr.listaAristas [orig].obtenerDest());
          lAr.listaAristas[orig].fijarDest(v);
          return (lAr.listaAristas[orig]);
       }

       min = new Arista (new Punto (9999,0),new Punto (9999,0));

       i = lAr.listaAristas[orig+nElem-1];
       do
       {
             if (min.obtenerDest().getX() > i.obtenerDest().getX())
                 min = i;
             i = i.obtenerSig();
       } while (i != lAr.listaAristas[orig+nElem-1]);
       return min;
   }


   private listaAristas unir (listaAristas lAr1, listaAristas lAr2)
   {
       int i, j;

       listaAristas union = new listaAristas ((lAr1.nElem + lAr2.nElem)*2);
       for (i = 0; i < lAr1.nElem; i++)
           union.listaAristas [i] = lAr1.listaAristas [i];
       for (j = 0; j < lAr2.nElem; j++)
       {
           union.listaAristas [i] = lAr2.listaAristas [j];
           i++;
       }
       union.nElem = lAr1.nElem + lAr2.nElem;
       return union;
   }


   public int obtenerNumAristas()
   // Devuelve el número de Aristas introducidas hasta el momento
   {
      return numAristas;
   }

   public Arista obtenerEdge(int i)
   // devuelve la Arista introducida en el lugar "i"
   {
      return listaAristas[i];
   }


   public listaAristas insertarArista(Arista e, listaAristas lAr)
   // crea una nueva Arista, la introduce en el array de Aristas y aumenta el
   // tamaño de ese array si fuera necesario
   {
      insertarAristaOrig (e);

      if(lAr.nElem >= lAr.capacidad)
      {
	 listaAristas temp = new listaAristas (lAr.capacidad*2);
	 for(int i = 0; i < lAr.nElem; i++)
	 {
	    temp.listaAristas[i] = lAr.listaAristas[i];
	 }
         temp.nElem = lAr.nElem;
	 lAr = temp;
      }
      lAr.listaAristas[lAr.nElem] = e;
      lAr.nElem++;
      return lAr;
   }


   public void insertarAristaOrig(Arista e)
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


// --------------------------- Métodos gráficos ----------------------------

   /*public void dibujarAristas()
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


   public void dibujarListaAristas (listaAristas lAristas)
   // Dibujar las Aristas de la lista lAristas
   {
      for(int i = 0; i < lAristas.nElem; i++)
      {
              mc.prepararArista (lAristas.listaAristas[i].obtenerOrig().getX(),
                                -lAristas.listaAristas[i].obtenerOrig().getY(),
                                 lAristas.listaAristas[i].obtenerDest().getX(),
                                -lAristas.listaAristas[i].obtenerDest().getY(),
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
       if (!esperandoPaso)
       {
          mc.mostrarMensaje ("");
          mc.borrarCanvas();
          dibujarPuntos();
          dibujarAristas();
       }
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
       insDiv.resume();
   }


   public void terminarPasoAPaso ()
   // Termina la simulación de paso a paso forzando al thread a que
   // se ejecute sin pausas
   {
       if (esperandoPaso == true)
       {
             insDiv.terminarInsercion = true;
             despertar ();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }


   public void dibujarArista (Arista e, String s)
   // Dibuja EN PANTALLA la Arista "e" con color "COLOR_Arista_INSERTADA"
   // Tras el retardo, la deja en "COLOR_Arista")
   {
         mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                            e.obtenerOrig().getX(), -e.obtenerOrig().getY(),
                            COLOR_Arista_INSERTADA);
         mc.mostrarMensaje (s);
         mc.mostrarImagen();
         insDiv.retardar ();
         mc.prepararArista (e.obtenerDest().getX(), -e.obtenerDest().getY(),
                            e.obtenerOrig().getX(), -e.obtenerOrig().getY(),
                            COLOR_Arista);
    }


    public void dibujarPunto (Punto v, Color c, String s)
    // Dibuja EN PANTALLA la Arista "e" con color "c"
    {
         mc.prepararPunto (v.getX(), -v.getY(), c);
         mc.mostrarMensaje (s);
         mc.mostrarImagen();
    }


    public void dibujarLineaVertical (int f, String s)
    {
         mc.prepararLineaVertical (f, COLOR_LINEA_ACTIVA);
         mc.mostrarMensaje (s);
         mc.mostrarImagen ();
         insDiv.retardar();
         mc.prepararLineaVertical (f, COLOR_LINEA_VERTICAL);
    }


    public void borrarLineaVertical (int f)
    {
         mc.prepararLineaVertical (f, COLOR_LINEA_BORRADA);
    }


    public void insertarTodoPasoAPaso ()
    {
        if (mc.pasoAPaso == true)  // si PASO A PASO -> crear thread de inserción de vértice
        {
           listaAristas = new Arista[capacidadAristas];      // cada vez que se inserta un vértice...
           numAristas = 0;                          // ...se calculan todas las Aristas
           listaPuntos = null;
           numPuntos = 0;

           insDiv = new insertarDivide (this, null, 0);
           insertarTodoPasoAPaso = true;
           insDiv.start();
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
           insDiv.stop();
       }
       deshabilitarSigPasoMc();
       deshabilitarAcabarMc();
   }

}
class insertarDivide extends Thread
{
   Divide di;
   Punto v;
   myCanvas mc;
   Punto [] listaPuntos;
   listaAristas lAr;
   int numPuntos;
   boolean terminarInsercion = false; // Indica si hay que suspender el modo paso a paso
                              // y realizar la inserción sin dibujar los pasos
                              // ni parar
   int retardo; // retardo en milisegundos de cada paso. Si es 0 ->
                // -> espera pulsación "Sig Paso"

   public insertarDivide (Divide div, Punto [] lv, int nv)
   {
      di = div;
      listaPuntos = lv;
      numPuntos = nv;
   }


   public void run()
   {
      if (di.insertarTodoPasoAPaso)
      {
          di.borrarCanvasMc();
          di.dibujarPuntosMc();
          di.mostrarImagenMc();

          for (int i=0; i<di.obtenerNumPuntosMc(); i++)
          {
              if(!di.yaExistia(v)) // comprueba que el vértice que se quiere insertar no existía ya
                    di.insertarOrdenadoPunto (di.obtenerPuntoMc(i));
          }
          numPuntos = di.numPuntos;
          listaPuntos = di.listaPuntos;
          auxRun ();
          di.insertarTodoPasoAPaso = false;

          if (!di.obtenerAlVueloMc())
             di.habilitarTriangularMc();
      }

      else
      {
          auxRun ();
          if (!di.obtenerAlVueloMc())
             di.habilitarTriangularMc();
      }
      di.threadActivo = false;      
   }


   private void auxRun ()
   {
      lAr = di.divide(listaPuntos, numPuntos);
      di.numAristas = lAr.nElem;
      di.listaAristas = lAr.listaAristas;

      di.deshabilitarAcabarMc(); // Cuando acaba la triangulación, deshabilitar el botón
                                // Como el botón "Siguiente paso" se habilita y deshabilita en cada paso, no hay otra forma de saber cuándo deshabilitar "Acabar"

      di.borrarCanvasMc();
      di.dibujarAristas();
      di.dibujarPuntos();
      di.mostrarImagenMc();
      if (!di.obtenerAlVueloMc())
          di.habilitarTriangularMc();
   }


   public void retardar ()
   // Pausa la ejecución del thread, ya sea durante un momento o hasta que se
   // le indique un "resume" (pulsación de botón "Sig Paso")
   {
        di.esperandoPaso = true;
        if (di.retardo > 0)
        {
              try
              {
                    di.habilitarAcabarMc();
	            Thread.sleep(di.retardo);
              }
              catch (InterruptedException ie)
              {
              	    return;
	      }
        }
        else
        {
              di.habilitarSigPasoMc();
              this.suspend();
        }

        di.deshabilitarSigPasoMc();
        di.esperandoPaso = false;
   }

}*/

//----------------------
//
// CLASE LISTA-AristaS
//
//----------------------

// Hace falta una lista de Aristas además de un array de Aristas como "listaAristas"
// porque en las llamadas recursivas a dividir es necesario devolver el array de
// Aristas, el nº de elementos y la capacidad del mismo en un sólo argumento


class listaAristas
{
    Arista [] listaAristas;
    int nElem;
    int capacidad;

    public listaAristas (int cap)
    {
        listaAristas = new Arista [cap];
        nElem = 0;
        capacidad = cap;
    }
}
}

