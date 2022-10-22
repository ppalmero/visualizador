/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poligonizacion;

import java.util.ArrayList;
import visualizador.Arista;
import visualizador.Conjunto;
import visualizador.Punto;
import visualizador.PuntoDouble;

/**
 *
 * @author Pablo
 * ++++Los Cast por int fueron agregados desde cambiar Punto Double
 */
public class Simple extends Poligono {

    int max_punt = 100;
    private final int[] opciones;
    private double area;

    private ArrayList<PuntoDouble> generarPuntos() {
        ArrayList<PuntoDouble> pAle = new ArrayList<PuntoDouble>();
        for (int i = 0; i < max_punt; i++) {
            pAle.add(new PuntoDouble(Math.random() * 600, Math.random() * 600));
        }
        return pAle;
    }

    public Simple(Conjunto puntos, int[] opciones) {
        this.opciones = opciones;
        max_punt = puntos.size();
        CantPNube = puntos.size();
        for (int v = 0; v < puntos.size(); v++) {
            nubeS[v] = new nnube();
            nubeS[v].p = new PuntoDouble(puntos.get(v).x, puntos.get(v).y);
            //nubeS[v].p.y = puntos.get(v).y;
            nubeS[v].marca = 1;
        }
        construyeP(MAX, max_punt);
    }
    static final int MAX = 200;
    static final int INFINITY = Integer.MAX_VALUE;
    static final int Tamcierre = 200;
    static final double pi = 3.141592;
    static final int RAND_MAX = 32767;
    /*definir*/
//definir nodo3 como punto 
//typedef
    double[] tPointd;
    /*fin definir*/
    nnube[] nubeS = new nnube[MAX];
    int[] poligono = new int[MAX + 1]; //poligono corriente 
    PuntoDouble[] polcierre = new PuntoDouble[MAX]; // estructura auxiliar para el cierre convexo 
    int[] solucion = new int[MAX + 1];  // poligonizacion construida , solucion 
    int[] Lcandidatos = new int[MAX - 3];
    int[] mejorsol = new int[MAX + 1];
    Punto[] AristasVisibles = new Punto[MAX];
    int CantPNube = 0;// cantidad de puntos de la nube
    int CantPS = 0;//cantidad de puntos de la solucion 
    int CantP = 0; // cantidad puntos poligono 
    int CantPC = 0; // cantidad puntos poligono candidato
    int CantArVis = 0;
    int CCandidatos = 0;
    int MaxPS = 0;
    int pc = -1;

    /*char *archivo;
    char *salida;
    
    FILE *rep,*rep2; */
    int[] trinicial = new int[3];

    double MIN(double x, double y) {
        return (x < y ? x : y);
    }

    double MAY(double x, double y) {
        return (x > y ? x : y);
    }

    public double getDecimal(int numeroDecimales, double decimal) {
        decimal = decimal * (java.lang.Math.pow(10, numeroDecimales));
        decimal = java.lang.Math.round(decimal);
        decimal = decimal / java.lang.Math.pow(10, numeroDecimales);

        return decimal;
    }
//funcion  que calcula la interseccion de segmnetos 
/*---------------------------------------------------------------------
    SegSegInt: Finds the point of intersection p between two closed
    segments ab and cd.  Returns p and a char with the following meaning:
    'e': The segments collinearly overlap, sharing a point.
    'v': An endpoint (vertex) of one segment is on the other segment,
    but 'e' doesn't hold.
    '1': The segments intersect properly (i.e., they share a point and
    neither 'v' nor 'e' holds).
    '0': The segments do not intersect (i.e., they share no points).
    Note that two collinear segments that share just one point, an endpoint
    of each, returns 'e' rather than 'v' as one might expect.
    ---------------------------------------------------------------------*/

    void Assigndi(double[] p, PuntoDouble a)//tPointd;
    {
        p[0] = a.x;
        p[1] = a.y;
    }
    /*---------------------------------------------------------------------
    Returns TRUE iff point c lies on the closed segement ab.
    Assumes it is already known that abc are collinear.
    ---------------------------------------------------------------------*/

    boolean Between(PuntoDouble a, PuntoDouble b, PuntoDouble c) {
        PuntoDouble ba, ca;

        /* If ab not vertical, check betweenness on x; else on y. */
        if (a.x != b.x) {
            return ((a.x <= c.x) && (c.x <= b.x))
                    || ((a.x >= c.x) && (c.x >= b.x));
        } else {
            return ((a.y <= c.y) && (c.y <= b.y))
                    || ((a.y >= c.y) && (c.y >= b.y));
        }
    }

    int AreaSign(PuntoDouble a, PuntoDouble b, PuntoDouble c) {
        double area2;
        area2 = (b.x - a.x) * (double) (c.y - a.y)
                - (c.x - a.x) * (double) (b.y - a.y);

        /* The area should be an integer. */
        if (area2 > 0.5) {
            return 1;
        } else if (area2 < -0.5) {
            return -1;
        } else {
            return 0;
        }
    }

    boolean Collinear(PuntoDouble a, PuntoDouble b, PuntoDouble c) {
        return AreaSign(a, b, c) == 0;
    }

    char ParallelInt(PuntoDouble a, PuntoDouble b, PuntoDouble c, PuntoDouble d, double[] p)//tPointd
    {
        if (!Collinear(a, b, c)) {
            return '0';
        }

        if (Between(a, b, c)) {
            Assigndi(p, c);
            return 'e';
        }
        if (Between(a, b, d)) {
            Assigndi(p, d);
            return 'e';
        }
        if (Between(c, d, a)) {
            Assigndi(p, a);
            return 'e';
        }
        if (Between(c, d, b)) {
            Assigndi(p, b);
            return 'e';

        }
        return '0';
    }

//funcion auxiliar que calcula la interseccion de segmnetos 
    char SegSegInt(PuntoDouble a, PuntoDouble b, PuntoDouble c, PuntoDouble d, double[] p)//tPointd
    {
        double s, t;       /* The two parameters of the parametric eqns. */
        double num, denom;  /* Numerator and denoninator of equations. */
        char code = ' ';

        /* Return char characterizing intersection. */
        denom = getDecimal(5, a.x * (double) (d.y - c.y)
                + b.x * (double) (c.y - d.y)
                + d.x * (double) (b.y - a.y)
                + c.x * (double) (a.y - b.y));

        /* If denom is zero, then segments are parallel: handle separately. */
        if (denom == 0.0) {
            return ParallelInt(a, b, c, d, p);
        }

        num = getDecimal(5, a.x * (double) (d.y - c.y)
                + c.x * (double) (a.y - d.y)
                + d.x * (double) (c.y - a.y));

        if ((num == 0.0) || (num == denom)) {
            code = 'v';
        }
        s = num / denom;
        //printf("num=%lf, denom=%lf, s=%lf\n", num, denom, s);

        num = getDecimal(5, -(a.x * (double) (c.y - b.y)
                + b.x * (double) (a.y - c.y)
                + c.x * (double) (b.y - a.y)));
        if ((num == 0.0) || (num == denom)) {
            code = 'v';
        }
        t = num / denom;
        //printf("num=%lf, denom=%lf, t=%lf\n", num, denom, t);

        if ((0.0 < s) && (s < 1.0)
                && (0.0 < t) && (t < 1.0)) {
            code = '1';
        } else if ((0.0 > s) || (s > 1.0)
                || (0.0 > t) || (t > 1.0)) {
            code = '0';
        }

        p[0] = a.x + s * (b.x - a.x);
        p[1] = a.y + s * (b.y - a.y);

        return code;
    }
    /*------------------------------------------------*/
// controla si un punto esta dentro de un poligono 
// punto q dentro del poligono T

    char InPoly(PuntoDouble q, PuntoDouble[] T, int n)//punto *T
    {
        int i = 0, i1, j;      /* point index; i1 = i-1 mod n */

        double x;          /* x intersection of e with ray */
        int Rcross = 0; /* number of right edge/ray crossings */
        int Lcross = 0; /* number of left edge/ray crossings */

        PuntoDouble[] P = new PuntoDouble[MAX];

        /* Shift so that q is the origin. Note this destroys the polygon.
        This is done for pedogical clarity. */

        for (j = 0; j < n; j++) {
            P[j] = new PuntoDouble();
            P[j].x = T[j].x;
            P[j].y = T[j].y;
        }
        for (i = 0; i < n; i++) {

            P[i].x = P[i].x - q.x;
            P[i].y = P[i].y - q.y;
        }
        /* For each edge e=(i-1,i), see if crosses ray. */
        for (i = 0; i < n; i++) {
            /* First see if q=(0,0) is a vertex. */
            if (P[i].x == 0 && P[i].y == 0) {
                return 'v';
            }
            i1 = (i + n - 1) % n;
            // printf("e=(%d,%d)\t", i1, i);

            /* if e "straddles" the x-axis... */
            /* The commented-out statement is logically equivalent to the one 
            following. */
            /* if( ( ( P[i][Y] > 0 ) && ( P[i1][Y] <= 0 ) ) ||
            ( ( P[i1][Y] > 0 ) && ( P[i] [Y] <= 0 ) ) ) { */

            if ((P[i].y > 0) != (P[i1].y > 0)) {

                /* e straddles ray, so compute intersection with ray. */
                x = (P[i].x * (double) P[i1].y - P[i1].x * (double) P[i].y)
                        / (double) (P[i1].y - P[i].y);
                //  printf("straddles: x = %g\t", x);

                /* crosses ray if strictly positive intersection. */
                if (x > 0) {
                    Rcross++;
                }
            }
            //printf("Right cross=%d\t", Rcross);

            /* if e straddles the x-axis when reversed... */
            /* if( ( ( P[i] [Y] < 0 ) && ( P[i1][Y] >= 0 ) ) ||
            ( ( P[i1][Y] < 0 ) && ( P[i] [Y] >= 0 ) ) )  { */

            if ((P[i].y < 0) != (P[i1].y < 0)) {

                /* e straddles ray, so compute intersection with ray. */
                x = (P[i].x * (double) P[i1].y - P[i1].x * (double) P[i].y)
                        / (double) (P[i1].y - P[i].y);
                //printf("straddles: x = %g\t", x);

                /* crosses ray if strictly positive intersection. */
                if (x < 0) {
                    Lcross++;
                }
            }
            //  printf("Left cross=%d\n", Lcross);
        }

        /* q on the edge if left and right cross are not the same parity. */
        if ((Rcross % 2) != (Lcross % 2)) {
            return 'e';
        }

        /* q inside iff an odd number of crossings. */
        if ((Rcross % 2) == 1) {
            return 'i';
        } else {
            return 'o';
        }
    }

//----------------------------------------------------------------------
////funcion auxiliar que calcula el producto vectorial de dos vectores 
/*-----------------------------------------------------*/
    double vectorial(PuntoDouble p1, PuntoDouble p2, PuntoDouble p3) {
        PuntoDouble u = new PuntoDouble(), v = new PuntoDouble();

        u.x = (p2.x - p1.x);                //Calculamos el primer vector
        u.y = (p2.y - p1.y);                //Calculamos el segundo vector
        v.x = (p3.x - p1.x);
        v.y = (p3.y - p1.y);

        return ((u.x * v.y) - (v.x * u.y));
    }

    /*-----------------------------------------------------*/
//funcion  que calcula el area signada de tres puntos 
    double areasignada(PuntoDouble pu1, PuntoDouble pu2, PuntoDouble pu3) {
        double salida;

        salida = ((pu1.x * pu2.y + pu2.x * pu3.y + pu3.x * pu1.y) - (pu1.x * pu3.y + pu2.x * pu1.y + pu3.x * pu2.y));

        return ((0.5 * salida));

    }

    ;

//funcion auxiliar que cacula un entero random  
//devuelve un entero entre high y low
int rnd(int low, int high) {/*toma un  random entre low y high */
        double i;

        if (low >= high) {
            return low;
        }
        i = ((1.0 * Math.random() /*/ (RAND_MAX)*/) * ((high - low) + low));
        if (i > high) {
            i = high;
        }
        if (low == 0) {
            if ((i + 0.5) > high) {
                i = high;
            }
        }
        return (int) i;
    }

    /****************************************************************************************
    Funcion que calcula el angulo que forman dos vectores. Los  vectores
    estan determinados por dos puntos.
     ****************************************************************************************/
    double angulo(PuntoDouble p1, PuntoDouble p2, PuntoDouble p3, PuntoDouble p4) {
        double[] u = new double[2];
        double[] v = new double[2];
        double m1, m2;

        u[0] = p2.x - p1.x;
        u[1] = p2.y - p1.y;
        v[0] = p4.x - p3.x;
        v[1] = p4.y - p3.y;
        //Modulos
        m1 = Math.sqrt((u[0] * u[0]) + (u[1] * u[1]));
        m2 = Math.sqrt((v[0] * v[0]) + (v[1] * v[1]));

        return (Math.acos(((u[0] * v[0]) + (u[1] * v[1])) / (m1 * m2)));
    }

//------------------------------------------------------------------------------
    //funcion auxiliar que calcula si un punto esta dentro de un poligono dado
    int Dentro(PuntoDouble p, PuntoDouble[] poli, int ver)//Puntodouble *poli
    {
        int counter = 0;
        int i;
        double xinters;
        PuntoDouble p1, p2;

        p1 = poli[0];
        for (i = 1; i < ver; i++) {
            p2 = poli[i % ver];

            if (p.y > MIN(p1.y, p2.y)) {
                if (p.y <= MAY(p1.y, p2.y)) {
                    if (p.x <= MAY(p1.x, p2.x)) {
                        if (p1.y != p2.y) {
                            xinters = (p.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
                            if (p1.x == p2.x || p.x <= xinters) {
                                counter++;
                            }
                        }
                    }
                }
            }
            p1 = p2;
        }

        if (counter % 2 == 0) {
            return (0); /*fuera*/
        } else {
            return (1);/*dentro*/
        }
    }

    ;

//funcion auxiliar que calcula el cierre convexo de un conjunto de puntos 

void Cierre_Convexo(PuntoDouble[] polcierre) {
        nodo2[] cierre = new nodo2[MAX];
        double as;
        nodo2 temp;
        PuntoDouble p1 = new PuntoDouble(), p2 = new PuntoDouble(), menorY;
        int i, ii, j, k, canp, pos, ver, maspasos;

        //Copiamos los vertices del poligono P en el arreglo cierre


        ii = 0;
        while (ii < CantP) {
            //copio vertices poligono en arreglo cierre
            cierre[ii] = new nodo2();
            cierre[ii].p.x = nubeS[poligono[ii]].p.x;
            cierre[ii].p.y = nubeS[poligono[ii]].p.y;;
            cierre[ii].ang = 0.0;
            ii++;
        }
        //cantidad puntos candidatos al  cierre canp
        canp = ii;

        // ordenamos el cierre por absisas - ordeno  a X
        i = 0;
        while (i <= canp - 1) {
            j = i + 1;
            while (j <= canp - 1) {
                if (cierre[j].p.x < cierre[i].p.x) {
                    temp = new nodo2(cierre[i]);
                    cierre[i] = new nodo2(cierre[j]);
                    cierre[j] = new nodo2(temp);
                } else if (cierre[j].p.x == cierre[i].p.x) {
                    if (cierre[j].p.y < cierre[i].p.y) {
                        temp = new nodo2(cierre[i]);
                        cierre[i] = new nodo2(cierre[j]);
                        cierre[j] = new nodo2(temp);
                    }
                }
                j++;
            }
            i++;
        };

        //Calculamos los angulos  en  cierre[0] tengo punto con menor coordenada x
        p1.x = (cierre[0].p.x);
        p1.y = (cierre[0].p.y);
        p2.x = (cierre[0].p.x);
        p2.y = ((cierre[0].p.y) + 2);

        for (i = 0; i <= canp - 1; i++) {
            cierre[i].ang = angulo(p1, p2, p1, cierre[i].p);
        }

        // ordenamos cierre de mayor a menor segun los angulos
        i = 0;
        while (i <= canp - 1) {
            j = i + 1;
            while (j <= canp - 1) {
                if (cierre[j].ang > cierre[i].ang) {
                    temp = new nodo2(cierre[i]);
                    cierre[i] = new nodo2(cierre[j]);
                    cierre[j] = new nodo2(temp);
                }
                j++;
            }
            i++;
        }

        maspasos = 1;

        // Calcula el ciere convexo del conjunto de puntos guardado en cierre
        i = 0;
        while (maspasos == 1) {
            as = areasignada(cierre[(i % canp)].p, cierre[(i + 1) % canp].p, cierre[(i + 2) % canp].p);

            if (as <= 0) { //eliminar(pi+1);
                for (k = i + 1; k <= canp - 1; k++) {
                    cierre[k] = (cierre[k + 1]);//new nodo2
                }
                canp = canp - 1;

                if (i == 0) {
                    i++;
                } else {
                    i--;
                }
            } else {
                i++;
            }

            if (i < (canp - 2)) {
                maspasos = 1;
            } else {
                maspasos = 0;
            }

        }

        // busco la posicion  con el punto con menor ordenada (y) para almacenarlo
        pos = 0;
        menorY = new PuntoDouble(cierre[0].p);
        for (i = 1; i <= canp - 1; i++) {
            if (cierre[i].p.y < menorY.y) {
                pos = i;
                menorY = new PuntoDouble(cierre[i].p);
            } else if (cierre[i].p.y == menorY.y) {
                if (cierre[i].p.x < menorY.x) {
                    pos = i;
                    menorY = new PuntoDouble(cierre[i].p);
                }
            }
        }

// en pos tengo el de coordenada Y  mas chica, guardo desde pos , la cantidad de puntos del cierre
//copio en el poligono P el cierre convexo "cierreP". 

        j = 0;
        while (j <= canp - 1) {
            polcierre[j] = new PuntoDouble();
            polcierre[j].x = cierre[pos].p.x;;
            polcierre[j].y = cierre[pos].p.y;//copio cierre en poligono
            j++;
            pos = (pos + 1) % canp;

        }
        CantPC = j;

    }

    //fin Cierre_Convexo
//funcion auxiliar que calcula si dos puntos son iguales 
    boolean Iguales(PuntoDouble p1, PuntoDouble p2) {
        if (((Math.abs(p1.x - p2.x) <= 0.00001)) && ((Math.abs(p1.y - p2.y) <= 0.00001)))//long double
        {
            return (true);
        } else {
            return (false);
        }
    }

    ;

/****************************************************************************************
 Funcion que calcula el area de un poligono
 ****************************************************************************************/
  
double PolygonArea(int[] pol, int n) /* int *pol calculo del area de un poligono*/ {
        int i, j; //n numero de vertices del poligono 
        double area = 0;

        for (i = 0; i < n; i++) {

            j = (i + 1) % n;

            area = area + ((nubeS[pol[i]].p.x) * nubeS[pol[j]].p.y);

            area = area - ((nubeS[pol[i]].p.y) * (nubeS[pol[j]].p.x));
        }

        area /= 2;
        return (area < 0 ? -area : area);
    }

    /****************************************************************************************
    funcion que  calcula el area de un triangulo determinado por sus  tres 
    vértices.
     ****************************************************************************************/
    double areatriangulo(PuntoDouble p1, PuntoDouble p2, PuntoDouble p3) {
        double sp, l1, l2, l3;

        l1 = Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y)));
        l2 = Math.sqrt(((p1.x - p3.x) * (p1.x - p3.x)) + ((p1.y - p3.y) * (p1.y - p3.y)));
        l3 = Math.sqrt(((p3.x - p2.x) * (p3.x - p2.x)) + ((p3.y - p2.y) * (p3.y - p2.y)));

        sp = ((l1 + l2 + l3) / 2.0);
        if ((sp * (sp - l1) * (sp - l2) * (sp - l3)) <= 0) {
            return (0.0);
        } else {
            return (Math.sqrt(sp * (sp - l1) * (sp - l2) * (sp - l3)));
        }
    }

    double Distance_point_seg(double cx, double cy, double ax, double ay,
            double bx, double by) {

        /*
        
        Subject 1.02: How do I find the distance from a point to a line?
        
        
        Let the point be C (Cx,Cy) and the line be AB (Ax,Ay) to (Bx,By).
        Let P be the point of perpendicular projection of C on AB.  The parameter
        r, which indicates P's position along AB, is computed by the dot product 
        of AC and AB divided by the square of the length of AB:
        
        (1)    AC dot AB
        r = ---------  
        ||AB||^2
        
        r has the following meaning:
        
        r=0      P = A
        r=1      P = B
        r<0      P is on the backward extension of AB
        r>1      P is on the forward extension of AB
        0<r<1    P is interior to AB
        
        The length of a line segment in d dimensions, AB is computed by:
        
        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 + ... + (Bd-Ad)^2)
        
        so in 2D:  
        
        L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
        
        and the dot product of two vectors in d dimensions, U dot V is computed:
        
        D = (Ux * Vx) + (Uy * Vy) + ... + (Ud * Vd)
        
        so in 2D:  
        
        D = (Ux * Vx) + (Uy * Vy) 
        
        So (1) expands to:
        
        (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
        r = -------------------------------
        L^2
        
        The point P can then be found:
        
        Px = Ax + r(Bx-Ax)
        Py = Ay + r(By-Ay)
        
        And the distance from A to P = r*L.
        
        Use another parameter s to indicate the location along PC, with the 
        following meaning:
        s<0      C is left of AB
        s>0      C is right of AB
        s=0      C is on AB
        
        Compute s as follows:
        
        (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay)
        s = -----------------------------
        L^2
        
        
        Then the distance from C to P = |s|*L.
        
         */
        double distanceLine, distanceSegment;

        double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
        double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
        double r = r_numerator / r_denomenator;

        double px = ax + r * (bx - ax);
        double py = ay + r * (by - ay);

        double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay)) / r_denomenator;

        distanceLine = Math.abs(s) * Math.sqrt(r_denomenator);

// (xx,yy) is the point on the lineSegment closest to (cx,cy)

        double xx = px;
        double yy = py;

        if ((r >= 0) && (r <= 1)) {
            distanceSegment = distanceLine;
        } else {

            double dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
            double dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
            if (dist1 < dist2) {
                xx = ax;
                yy = ay;
                distanceSegment = Math.sqrt(dist1);
            } else {
                xx = bx;
                yy = by;
                distanceSegment = Math.sqrt(dist2);
            }
        }
        return (distanceSegment);
    }

    /*===========================================================================*/

    /*         Construye poligono simple aleatori a partir de un conjunto de puntos dado
    
    Parte de un triangulo aleatorio y en cada paso agrega  un punto  factible  elegido al azar */

    /* VER : Paper  RPG  de Auer y Held -  implementa heuristica steady growth  */

    /*===========================================================================
    void Leer_Nube_S(char *archivo)
    // lee puntos desde un archivo de entrada y los almacena en arreglo nubeS[]
    {
    FILE *fp;
    int v;
    punto p1;
    
    if (( fp= fopen(archivo, "r")) == NULL)
    {
    printf ("\n no se puede abrir el archivo de la nube de puntos ....\n" );
    getchar();
    exit(1);
    }
    
    fscanf(fp, "%d \n" , &CantPNube);
    CCandidatos=CantPNube;
    for (v= 0; v <CantPNube; v++){
    fscanf(fp, "%lf %lf", &(p1.x),&(p1.y));
    //printf( "%lf %lf \n", (p1.x),(p1.y));
    nubeS[v].p.x=p1.x;
    nubeS[v].p.y=p1.y;
    nubeS[v].marca=1;
    }
    fclose(fp);
    }
    
    /*===========================================================================*/
    // elimina el punto de la lista de candidatos 
/*===========================================================================*/
    void quitar_punto_candidato(int pos) {

        Lcandidatos[pos] = Lcandidatos[(CCandidatos - 1)];
        CCandidatos--;
        Lcandidatos[CCandidatos] = -1;

    }

    /*===========================================================================*/
    //Genera_triangulo_ menor area
/*===========================================================================*/
    void Generar_Triangulo_Menor_Area() // calcula  triangulo inicial de menor area
    {
        int aux1 = 0, aux2 = 0, aux3 = 0; //indice en nubeS de los puntos que formaran el triangulo 
        PuntoDouble p;
        double as1 = 0.0, as2 = 0.0, as3 = 0.0;
        boolean fin = false;
        int exterior = 1, i = 0, k, u = 0;
        int[] triangulo = new int[3];
        int[] triMenor = new int[3];
        double vec;
        double areaT, areaT1;
        double areaTMenor = INFINITY;

        // Armo todos posibles triangulos hasta encontrar uno vacio de puntos de S					   
        aux1 = 0;
        while ((aux1 < max_punt - 2)) {
            aux2 = aux1 + 1;

            while ((aux2 < max_punt - 1)) {
                aux3 = aux2 + 1;

                while ((aux3 < max_punt)) {
                    // controlo que no haya puntos alineados 
                    exterior = 1;
                    vec = vectorial(nubeS[aux1].p, nubeS[aux2].p, nubeS[aux3].p);
                    if (vec == 0) {
                        exterior = 0;
                        /*printf("vec=0.puntos alineados..........................%d,%d,%d", aux1, aux2, aux3);
                        getchar();*/
                    }//si los puntos no están alineados, p. vectorial es distinto de cero
                    else {
                        if (vec > 0)//se construye el triángulo con sentido positivo 
                        {
                            triangulo[0] = aux1;
                            triangulo[1] = aux2;
                            triangulo[2] = aux3;
                        } else {
                            triangulo[0] = aux1;
                            triangulo[1] = aux3;
                            triangulo[2] = aux2;
                        }
                    }


                    // controlo que el triangulo no tenga puntos de S en su interior 

                    i = 0;
                    while ((i < max_punt) && (exterior == 1)) {
                        if (((nubeS[i].p.x != nubeS[aux1].p.x) || (nubeS[i].p.y != nubeS[aux1].p.y)) && //si el punto en nubeS[i] no es ninguno de los puntos del triangulo 
                                ((nubeS[i].p.x != nubeS[aux2].p.x) || (nubeS[i].p.y != nubeS[aux2].p.y))
                                && ((nubeS[i].p.x != nubeS[aux3].p.x) || (nubeS[i].p.y != nubeS[aux3].p.y))) {
                            p = nubeS[i].p;
                            //pruebo si el triangulo tiene puntos interiores , punto  esta en el interior del triangulo p1p2p3
                            // si esta a la izq de p1p2 a la izq de p2p3 y a la izq de p3p1 

                            as1 = areasignada(nubeS[triangulo[u % 3]].p, nubeS[triangulo[((u + 1) % 3)]].p, p);// AS > 0 indica que p se encuenta a la izq  de la recta determinado por p1p2

                            as2 = areasignada(nubeS[triangulo[((u + 1) % 3)]].p, nubeS[triangulo[((u + 2) % 3)]].p, p);

                            as3 = areasignada(nubeS[triangulo[((u + 2) % 3)]].p, nubeS[triangulo[(u % 3)]].p, p);

                            if ((as1 > 0) && (as2 > 0) && (as3 > 0)) {
                                exterior = 0;// el punto esta dentro del triangulo 
                            }
                        }

                        i = i + 1;

                    }

                    if (exterior == 1) {  //  triangulo no tiene puntos interiores triangulo sirve 

                        // Calcular AREA 
                        areaT = areatriangulo(nubeS[triangulo[0]].p, nubeS[triangulo[1]].p, nubeS[triangulo[2]].p);
                        areaT1 = PolygonArea(triangulo, 3);

                        //si areaTnuevo es menor AreaT corriente guardo nuevo 
                        if (areaT < areaTMenor) {
                            triMenor[0] = triangulo[0];
                            triMenor[1] = triangulo[1];
                            triMenor[2] = triangulo[2];

                            areaTMenor = areaT;
                            /*printf( "triangulo menor :\n ");
                            printf( "AreaTmenor :%.20f \n",areaTMenor );*/
                            //for(k=0;k<=2;k++)
                            //printf( "pos : %d  x:  %.2f   y: %.2f \n \n",triangulo[k], nubeS[triangulo[k]].p.x, nubeS[triangulo[k]].p.y);

                        }

                    }

                    aux3 = aux3 + 1;
                }

                aux2 = aux2 + 1;
            }

            aux1 = aux1 + 1;
        }

        nubeS[triMenor[0]].marca = 0; /*Quitar_puntos_de_Nube*/
        nubeS[triMenor[1]].marca = 0;
        nubeS[triMenor[2]].marca = 0;
        /*printf( "triangulo inicial:\n ");
        printf( "AreaTmenor :%.25f \n",areaTMenor );
        for(k=0;k<=2;k++)
        printf( "pos : %d  x:  %.2f   y: %.2f \n ",triMenor[k], nubeS[triMenor[k]].p.x, nubeS[triMenor[k]].p.y);
         */
        i = 0;
        for (k = 0; k < max_punt; k++) { // armo la lista de candidatos 
            if (nubeS[k].marca != 0) {
                Lcandidatos[i] = k;
                i++;
            }
        }
        CCandidatos = i;


        for (k = 0; k <= 2; k++) {
            solucion[k] = triMenor[k];
            poligono[k] = triMenor[k];
            trinicial[k] = triMenor[k];
        }

    }

    /*===========================================================================*/
    //Genera_triangulo_ eligiendo 1 Punto_Azar 2 mas cercanos 
/*===========================================================================*/
    void Generar_triangulo_PAzar_Pmascercanos() // calcula  triangulo inicial eligiendo primer punto azar y dos siguientes entre los mas cercanos  
    // tres puntos mas cercanos en la nube. 
    {
        int aux1 = 0, aux2 = 0, aux3 = 0; //indice en nubeS de los puntos que formaran el triangulo 
        PuntoDouble p;
        double as1 = 0.0, as2 = 0.0, as3 = 0.0;
        boolean fin = false;
        int exterior = 1, j = 0, i = 0, pk, k, u = 0;
        int x1, x2, x3;
        int[] triangulo = new int[3];
        double Dj, vec;
        nodo4[] distancias = new nodo4[MAX]; //arreglo de distancias 
        nodo4 temp;

        pk = rnd(0, max_punt - 1);

        //primer punto del triangulo elegido al azar 
        aux1 = (pk % max_punt);

        //printf( "punto elegido: %d   x: %.2f  y: %.2f \n\n ",aux1,nubeS[aux1].p.x,nubeS[aux1].p.y ); 

        //calcular distancias de aux1 a todo los demas puntos 
        for (j = 0; j < max_punt; j++) {
            Dj = Math.pow((nubeS[aux1].p.x - nubeS[j].p.x), 2) + Math.pow((nubeS[aux1].p.y - nubeS[j].p.y), 2);// distancia entre dos puntos 
            distancias[j] = new nodo4();
            distancias[j].dist = Dj;
            distancias[j].p = j;
        }
        // ordenamos el arreglo  por distancias  
        i = 0;
        while (i <= max_punt - 1) {
            j = i + 1;
            while (j <= max_punt - 1) {
                if (distancias[j].dist < distancias[i].dist) {
                    temp = distancias[i];
                    distancias[i] = distancias[j];
                    distancias[j] = temp;
                }
                j++;
            }
            i++;
        }

        // printf( "\narreglo distancias ordenado \n ");

        //for(k=0;k<max_punt;k++) printf( "punto : %d  distancia:  %.2f --- x: %.2f  y: %.2f \n --- \n ",
        //       distancias[k].p, distancias[k].dist ,nubeS[distancias[k].p].p.x, nubeS[distancias[k].p].p.y);

        // Armo todos posibles triangulo mas pequeño vacio de puntos de S   
        x1 = 0;
        aux1 = distancias[x1].p;
        while ((x1 < max_punt) && (fin == false)) {
            x2 = ((x1 + 1) % max_punt);
            aux2 = distancias[x2].p;

            while ((x2 < max_punt) && (fin == false)) {
                x3 = ((x2 + 1) % max_punt);
                aux3 = distancias[x3].p;

                while ((x3 < max_punt) && (fin == false)) {
                    exterior = 1;
                    vec = vectorial(nubeS[aux1].p, nubeS[aux2].p, nubeS[aux3].p);
                    if (vec == 0) {
                        exterior = 0;
                        /*printf("vec=0.puntos alineados..................%d, %d, %d,", aux1, aux2,aux3);
                        getchar();*/
                    }//si los puntos no están alineados, p. vectorial es distinto de cero
                    else {
                        if (vec > 0)//se construye el triángulo con sentido positivo 
                        {
                            triangulo[0] = aux1;
                            triangulo[1] = aux2;
                            triangulo[2] = aux3;
                        } else {
                            triangulo[0] = aux1;
                            triangulo[1] = aux3;
                            triangulo[2] = aux2;
                        }
                    }
                    // controlo que el triangulo no tenga puntos de S en su interior 
                    i = 0;
                    while ((i < max_punt) && (exterior == 1)) {
                        if (((nubeS[i].p.x != nubeS[aux1].p.x) || (nubeS[i].p.y != nubeS[aux1].p.y)) && //si el punto en nubeS[i] no es ninguno de los puntos del triangulo 
                                ((nubeS[i].p.x != nubeS[aux2].p.x) || (nubeS[i].p.y != nubeS[aux2].p.y))
                                && ((nubeS[i].p.x != nubeS[aux3].p.x) || (nubeS[i].p.y != nubeS[aux3].p.y))) {
                            p = nubeS[i].p;
                            //pruebo si el triangulo tiene puntos interiores , punto  esta en el interior del triangulo p1p2p3
                            // si esta a la izq de p1p2 a la izq de p2p3 y a la izq de p3p1 

                            as1 = areasignada(nubeS[triangulo[u % 3]].p, nubeS[triangulo[((u + 1) % 3)]].p, p);// AS > 0 indica que p se encuenta a la izq  de la recta determinado por p1p2

                            as2 = areasignada(nubeS[triangulo[((u + 1) % 3)]].p, nubeS[triangulo[((u + 2) % 3)]].p, p);

                            as3 = areasignada(nubeS[triangulo[((u + 2) % 3)]].p, nubeS[triangulo[(u % 3)]].p, p);

                            if ((as1 > 0) && (as2 > 0) && (as3 > 0)) {
                                exterior = 0;// el punto esta dentro del triangulo 
                            }
                        }

                        i = i + 1;
                    }

                    if (exterior == 1) {
                        fin = true; //  triangulo no tiene puntos interiores triangulo sirve 
                        nubeS[triangulo[0]].marca = 0; /*Quitar_puntos_de_Nube*/
                        nubeS[triangulo[1]].marca = 0;
                        nubeS[triangulo[2]].marca = 0;

                        /*
                        printf( "\n triangulo inicial:\n ");
                        for(k=0;k<=2;k++) printf( "pos : %d  x:  %.2f   y: %.2f \n ",triangulo[k], nubeS[triangulo[k]].p.x, nubeS[triangulo[k]].p.y);
                         */

                        i = 0;
                        for (k = 0; k < CantPNube; k++) { // armo la lista de candidatos 
                            if (nubeS[k].marca != 0) {
                                Lcandidatos[i] = k;
                                i++;
                            }
                        }
                        CCandidatos = i;
                    }

                    aux3 = aux3 + 1;
                }
                aux2 = aux2 + 1;
            }
            aux1 = aux1 + 1;
        }

        for (k = 0; k <= 2; k++) {
            solucion[k] = triangulo[k];
            poligono[k] = triangulo[k];
            trinicial[k] = triangulo[k];
        }
    }
    /*===========================================================================*/
    //Genera_triangulo_ eligiendo Puntos_Azar
/*===========================================================================*/

    void Generar_triangulo_Puntos_Azar() // calcula  triangulo inicial factible eligiendo tres puntos cualesquiera de la nube. 
    {
        int aux1 = 0, aux2 = 0, aux3 = 0; //indice en nubeS de los puntos que formaran el triangulo 
        PuntoDouble p;
        double as1 = 0.0, as2 = 0.0, as3 = 0.0;
        boolean fin = false;
        int exterior = 1, i = 0, k, u = 0;
        int[] triangulo = new int[3];
        double vec;

        k = rnd(0, max_punt - 1);

        aux1 = (k % max_punt); //primer punto del triangulo elegido al azar 

        // Armo todos posibles triangulos hasta encontrar el primero vacio de puntos de S

        while ((aux1 < max_punt) && (fin == false)) {
            aux2 = ((aux1 + 1) % max_punt);

            while ((aux2 < max_punt) && (fin == false)) {
                aux3 = ((aux2 + 1) % max_punt);

                while ((aux3 < max_punt) && (fin == false)) {
                    exterior = 1;
                    vec = vectorial(nubeS[aux1].p, nubeS[aux2].p, nubeS[aux3].p);
                    if (vec == 0) {
                        exterior = 0;
                        /*printf("vec=0 hay puntos alineados...............................");
                        getchar();*/
                    }//si los puntos no están alineados, producto vectorial es distinto de cero
                    else {
                        if (vec > 0)//se construye el triángulo con sentido positivo 
                        {
                            triangulo[0] = aux1;
                            triangulo[1] = aux2;
                            triangulo[2] = aux3;
                        } else {
                            triangulo[0] = aux1;
                            triangulo[1] = aux3;
                            triangulo[2] = aux2;
                        }
                    }

                    // controlo que el triangulo no tenga puntos de S en su interior 

                    i = 0;
                    while ((i < max_punt) && (exterior == 1)) {
                        if (((nubeS[i].p.x != nubeS[aux1].p.x) || (nubeS[i].p.y != nubeS[aux1].p.y)) && //si el punto en nubeS[i] no es ninguno de los puntos del triangulo 
                                ((nubeS[i].p.x != nubeS[aux2].p.x) || (nubeS[i].p.y != nubeS[aux2].p.y))
                                && ((nubeS[i].p.x != nubeS[aux3].p.x) || (nubeS[i].p.y != nubeS[aux3].p.y))) {
                            p = new PuntoDouble(nubeS[i].p);
                            //pruebo si el triangulo tiene puntos interiores , punto  esta en el interior del triangulo p1p2p3
                            // si esta a la izq de p1p2 a la izq de p2p3 y a la izq de p3p1 

                            as1 = areasignada(nubeS[triangulo[u % 3]].p, nubeS[triangulo[((u + 1) % 3)]].p, p);// AS > 0 indica que p se encuenta a la izq  de la recta determinado por p1p2

                            as2 = areasignada(nubeS[triangulo[((u + 1) % 3)]].p, nubeS[triangulo[((u + 2) % 3)]].p, p);

                            as3 = areasignada(nubeS[triangulo[((u + 2) % 3)]].p, nubeS[triangulo[(u % 3)]].p, p);

                            if ((as1 > 0) && (as2 > 0) && (as3 > 0)) {
                                exterior = 0;// el punto esta dentro del triangulo 
                            }
                        }

                        i = i + 1;

                    }
                    if (exterior == 1) {
                        fin = true; //  triangulo no tiene puntos interiores triangulo sirve 
                        nubeS[triangulo[0]].marca = 0; /*Quitar_puntos_de_Nube*/
                        nubeS[triangulo[1]].marca = 0;
                        nubeS[triangulo[2]].marca = 0;
                        // pintf( "triangulo inicial:\n ");
                        //for(k=0;k<=2;k++) printf( "pos : %d  x:  %.2f   y: %.2f \n ",triangulo[k], nubeS[triangulo[k]].p.x, nubeS[triangulo[k]].p.y);
                        i = 0;
                        for (k = 0; k < CantPNube; k++) { // armo la lista de candidatos 
                            if (nubeS[k].marca != 0) {
                                Lcandidatos[i] = k;
                                i++;
                            }
                        }
                        CCandidatos = i;
                    }

                    aux3 = aux3 + 1;
                }
                aux2 = aux2 + 1;
            }
            aux1 = aux1 + 1;
        }

        for (k = 0; k <= 2; k++) {
            solucion[k] = triangulo[k];
            poligono[k] = triangulo[k];
            trinicial[k] = triangulo[k];
        }

    }
    /*===========================================================================*/
    /*===========================================================================*/
    //Controla si un  punto p1 , ve la arista p2p3 
/*===========================================================================*/

    boolean vearistas(PuntoDouble p1, PuntoDouble p2, PuntoDouble p3) {
        PuntoDouble p;
        double[] paux = new double[MAX];
        char code1 = '?';
        char code2 = '?';
        int i = 0;
        int corta, ve = 1;
        int k, u = 0;
        PuntoDouble[] tri = new PuntoDouble[3];
        int exterior = 1;
        double as1 = 0.0, as2 = 0.0, as3 = 0.0;

        if (areasignada(p1, p2, p3) == 0) {
            ve = 0; // son colineales 
        } else {
            if (vectorial(p1, p2, p3) > 0)//se construye el triángulo con sentido positivo  
            {
                tri[0] = new PuntoDouble(p1);
                tri[1] = new PuntoDouble(p2);
                tri[2] = new PuntoDouble(p3);
            } else {
                tri[0] = new PuntoDouble(p1);
                tri[1] = new PuntoDouble(p3);
                tri[2] = new PuntoDouble(p2);
            }

            exterior = 1; // controlo que el triangulo no tenga puntos de la solucion en su interior 

            for (k = 0; k < CantPS; k++) {

                if (((nubeS[solucion[k]].p.x != p1.x) || (nubeS[solucion[k]].p.y != p1.y)) && //si el punto  no es ninguno de los puntos del triangulo 
                        ((nubeS[solucion[k]].p.x != p2.x) || (nubeS[solucion[k]].p.y != p2.y))
                        && ((nubeS[solucion[k]].p.x != p3.x) || (nubeS[solucion[k]].p.y != p3.y))) {
                    p = new PuntoDouble(nubeS[solucion[k]].p);

                    //pruebo si el triangulo tiene puntos interiores , punto  esta en el interior del triangulo p1p2p3
                    // si esta a la izq de p1p2 a la izq de p2p3 y a la izq de p3p1 
                    // AS > 0 indica que p se encuenta a la izq  de la recta determinado por p1p2

                    as1 = areasignada(tri[u % 3], tri[((u + 1) % 3)], p);

                    as2 = areasignada(tri[((u + 1) % 3)], tri[((u + 2) % 3)], p);

                    as3 = areasignada(tri[((u + 2) % 3)], tri[u % 3], p);

                    if ((as1 >= 0) && (as2 >= 0) && (as3 >= 0)) {
                        exterior = 0;// el punto esta dentro del triangulo 
                    }
                }

                i = i + 1;
                if (exterior == 0) {
                    i = 3;
                    ve = 0;
                }
            }
        }

        if (exterior == 1) {
            corta = 0;
            for (i = 0; i < CantPS; i++) {

                code1 = SegSegInt(p1, p2, nubeS[solucion[i]].p, nubeS[solucion[(i + 1) % CantPS]].p, paux); // intereseccion de segmentos 
                if ((code1 == '1')) /*||( code == 'e') )*/ {
                    corta = 1;
                }

                code2 = SegSegInt(p1, p3, nubeS[solucion[i]].p, nubeS[solucion[(i + 1) % CantPS]].p, paux);
                if ((code2 == '1')/*||( code == 'e')*/) {
                    corta = 1;
                }
            }
            if (corta == 1) {
                ve = 0;
            }
        }
        return (ve == 1);
    }

    /*===========================================================================*/
    /*===========================================================================*/
    //calcula el conjunto de aristas del poligono visibles desde p 
/*===========================================================================*/
    int CalculaAristasVisibles(PuntoDouble p) {
        int i = 0;
        int k = 0;
        while (i < CantPS) {

            if (vearistas(p, nubeS[solucion[i]].p, nubeS[solucion[(i + 1) % CantPS]].p)) {
                AristasVisibles[k] = new Punto();
                AristasVisibles[k].x = solucion[i]; //p1
                AristasVisibles[k].y = solucion[(i + 1) % CantPS];//p2
                k++;
            }
            i++;
        }
        return (k);
    }

    /*===========================================================================*/
    int Elegir_Arista_Azar() {
        int i = -1;//punto inicio arista 
        if (CantArVis == 0) {
            i = 0;
        } else if (CantArVis != 0) {
            i = rnd(0, CantArVis - 1);
        }
        return (i);
    }
    /*===========================================================================*/

    int Elegir_Arista_Greedy(int p)// agrega al poligono triangulo  menor area
    {
        int i = 0, k;//arista
        double areat;
        double minarea = INFINITY;

        if (CantArVis != 0) {

            if (CantArVis == 1) {
                i = 0;
            } else {

                for (k = 0; k < CantArVis; k++)//calcular arista que agregan triangulo de menor area
                {
                    areat = areatriangulo(nubeS[(int)AristasVisibles[k].x].p, nubeS[(int)AristasVisibles[k].y].p, nubeS[p].p);

                    if (areat < minarea) {
                        minarea = areat;
                        i = k;
                    }
                }
            }
        } else {
            i = -1;
        }

        return (i);

    }

    /*===========================================================================*/
    Punto Seleccionar_punto_Azar() {
        int k, c = 0;
        int i = 0;
        int av = 0;
        char ch;
        boolean puntoNofactible = true;
        int contp = 0;
        Punto pp = new Punto();

        while ((puntoNofactible) && (CCandidatos > 0)) {
            // pc posicion punto candidato 	 
            pc = rnd(0, (CCandidatos - 1)); // elijo un punto candidato al azar 

            pp.x = Lcandidatos[pc]; // obtengo el indice donde se encuentra el candidato en nubeS . 
            pp.y = pc; // pc posicion punto candidato 


            poligono[CantP] = (int)pp.x;
            CantP++;

            //calculo el cierre convexo de poligono , solucion + candidato 
            Cierre_Convexo(polcierre);

            // controlo que  no haya puntos de la nube dentro del CC
            c = 0;
            for (k = 0; k < CCandidatos; k++) {
                if (nubeS[Lcandidatos[k]].marca != 0) {
                    ch = InPoly(nubeS[Lcandidatos[k]].p, polcierre, CantPC);
                    if ((ch == 'i')) {
                        c = c + 1;
                        //printf(" dentro el punto:%d x:%.2f, y:%.2f \n ", k, nubeS[k].p.x, nubeS[k].p.y );
                        k = max_punt;
                        puntoNofactible = true;
                        CantP--;
                    }
                }
            }

            CantArVis = 0;

            if (c == 0) { //si no hay puntos de la nube dentro de CC entonces calculo aristas visibles desde p

                CantArVis = CalculaAristasVisibles(nubeS[(int)pp.x].p);

                if (CantArVis != 0) {
                    puntoNofactible = false;
                    return (pp);
                } else {
                    puntoNofactible = true;
                    CantP--;
                }

            }
        }
        if (puntoNofactible == false) {
            return (pp);
        }

        if ((puntoNofactible == true) && (CCandidatos > 0)) {
            pp.x = -1;
            return (pp);
        };

        return null;//agregado por PAblo
    }
    /*===========================================================================*/

    Punto Seleccionar_proximo_punto() {

        Punto pe; //pe es el punto elegido pe.p1 punto elegido y pe.p2 es lamposicion en Lcandidatos

        switch (opciones[1]) {
            case 0:
                pe = new Punto(Seleccionar_punto_Azar());
                break;
            default:
                pe = Seleccionar_punto_greedy();
        }

        return (pe);

    }
    /*===========================================================================*/

    Punto Seleccionar_punto_greedy() {
        int j_a, i_c;
        int j, k, c = 0;
        int i = 0;
        int av = 0;
        char ch;
        boolean puntoNofactible = true;

        Punto pp = new Punto();

        PuntoDouble a, b, q;
        nodo4[] Lcand_Dist = new nodo4[MAX];
        nodo4 temp;

        double D_Menor, Distance;

        //Copiar lista de candidatos en Lcand_Dist

        for (k = 0; k < CCandidatos; k++) {
            Lcand_Dist[k] = new nodo4();
            Lcand_Dist[k].p = Lcandidatos[k];
            Lcand_Dist[k].dist = 0;
            Lcand_Dist[k].posorig = k;
        }

        /*printf( "Arreglo Lcand_Dist inicial \n ");
        for(k=0;k<CCandidatos;k++) printf( "punto : %d , posorig: %d  , distancia:  %.2f --- x: %.2f  y: %.2f \n --- \n ",
        Lcand_Dist[k].p,  Lcand_Dist[k].posorig, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y);
         */

        //calcular distancias de punto candidato  al poligono corriente  
        i_c = 0;
        while (i_c < CCandidatos) {
            j_a = 0;
            D_Menor = INFINITY;

            while (j_a < CantPS) {
                q = nubeS[Lcand_Dist[i_c].p].p;// punto 

                a = nubeS[solucion[j_a]].p;//inicio segmento 
                b = nubeS[solucion[((j_a + 1) % CantPS)]].p;// fin segmento 

                Distance = Distance_point_seg(q.x, q.y, a.x, a.y, b.x, b.y);

                if (Distance < D_Menor) {
                    Lcand_Dist[i_c].dist = Distance;
                    D_Menor = Distance;
                }

                // printf( " punto: %d  Distance: %.2f  --- \n ", Lcand_Dist[i_c].p, Lcand_Dist[i_c].dist);
                j_a++;
            }
            i_c++;
        }

        /*  printf( "arreglo Lcand_Dist \n ");
        for(k=0;k<CCandidatos;k++) printf( "punto : %d  distancia:  %.2f --- x: %.2f  y: %.2f \n --- \n ",
        Lcand_Dist[k].p, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y); */

        //Ordenar la lista de puntos candidatos Lcand_Dist de acuerdo a su distancia al polígono corriente 
        i = 0;
        while (i < CCandidatos) {
            j = i + 1;
            while (j < CCandidatos) {
                if (Lcand_Dist[j].dist < Lcand_Dist[i].dist) {
                    temp = Lcand_Dist[i];
                    Lcand_Dist[i] = Lcand_Dist[j];
                    Lcand_Dist[j] = temp;
                }
                j++;
            }
            i++;
        }

        /*printf( "\n arreglo distancias ordenado \n ");
        
        for(k=0;k<CCandidatos;k++) printf( "punto en nubeS: %d - posoriginalLcand: %d  -distancia:%.2f --- x: %.2f  y: %.2f \n --- \n ",
        Lcand_Dist[k].p, Lcand_Dist[k].posorig, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y);
         */
        //printf( "\n==========================================================\n ");

        // busco entre los puntos mas cercanos el primero factible ..
        pc = -1;

        while ((puntoNofactible) && (CCandidatos > 0)) {
            pc = pc + 1;// pc posicion punto candidato en Lcand_Dist

            pp.x = Lcand_Dist[pc].p;
            // obtengo el indice donde se encuentra el candidato en nubeS .
            pp.y = Lcand_Dist[pc].posorig;

            // controlo que sea factible ...
            poligono[CantP] = (int)pp.x;
            CantP++;
            //calculo el cierre convexo de poligono , solucion + candidato 
            Cierre_Convexo(polcierre);
            // controlo que  no haya puntos de la nube dentro del CC
            c = 0;
            for (k = 0; k < CCandidatos; k++) {
                if (nubeS[Lcandidatos[k]].marca != 0) {
                    ch = InPoly(nubeS[Lcandidatos[k]].p, polcierre, CantPC);
                    if ((ch == 'i')) {
                        c = c + 1;
                        //printf(" dentro el punto:%d x:%.2f, y:%.2f \n ", k, nubeS[k].p.x, nubeS[k].p.y );
                        k = max_punt;
                        puntoNofactible = true;
                        CantP--;
                    }
                }
            }

            CantArVis = 0;
            if (c == 0) { //si no hay puntos de la nube dentro de CC entonces calculo aristas visibles desde p

                CantArVis = CalculaAristasVisibles(nubeS[(int)pp.x].p);

                if (CantArVis != 0) {
                    puntoNofactible = false;
                    return (pp);
                } else {
                    puntoNofactible = true;
                    CantP--;
                }
            }
        }

        if (puntoNofactible == false) {
            return (pp);
        }

        if ((puntoNofactible == true) && (CCandidatos > 0)) {
            pp.x = -1;
            return (pp);
        }
        return null;
    }


    /*===========================================================================
    ESTOS PROCEDIMEINETOS 
    me permiten elegir  area minima en cada paso .. hay que modificar  
    porque devuelve  punto y arista en un solo paso nodo 5 
    
    int Elegir_Arista_Greedy(int p, double *area)// agrega menor area
    {
    int i=0, k=0;//arista
    double areat;
    double minarea=INFINITY;
    
    if (CantArVis!=0){
    
    if (CantArVis==1){ i=AristasVisibles[0].ini;
     *area = areatriangulo(nubeS[AristasVisibles[k].ini].p,nubeS[AristasVisibles[k].fin].p, nubeS[p].p);
    
    }
    else { 
    for ( k=0; k<CantArVis; k++)//calcular aristas que agregan triangulo  de menor area
    {
    areat= areatriangulo(nubeS[AristasVisibles[k].ini].p,nubeS[AristasVisibles[k].fin].p, nubeS[p].p);
    
    if (areat < minarea){ minarea=areat; 
    i=AristasVisibles[k].ini;
     *area=minarea; }
    }
    }
    }     
    
    else i=-1;
    
    return(i);
    
    }
    
    /*===========================================================================
    // de todos los puntos factibles elegir  putno y arista de acuerdo al area que agregan  MENOR 
    // hay que modificar cuando se usa este criterio ya que no devuelve un punto  devuelve  punto  y punto inicio arista ( tipo nodo 5)
    // la eleccion del punto y la arista se hace junto  
    
    nodo5 Seleccionar_area_greedy() 
    
    {
    int j_a,i_c, k;
    int i ,j;
    int kg=0, fact=0;
    int ar_ini;
    bool puntoNofactible=true; 
    
    int c=0;
    char ch;
    
    int pf;
    nodo5 pp;
    punto a, b, q; 
    
    nodo4 Lcand_Dist[MAX];
    nodo4 temp;
    
    nodo5 Lcand_G[MAX];
    nodo5 temp1;
    
    double areat= INFINITY;
    
    double D_Menor, Distance;
    
    //Copiar lista de candidatos en Lcand_Dist
    
    for(k=0;k<CCandidatos;k++) {
    Lcand_Dist[k].p = Lcandidatos[k];
    Lcand_Dist[k].dist = 0;
    Lcand_Dist[k].posorig = k;
    }
    
    //calcular distancias de punto candidato  al poligono corriente  
    i_c=0; 
    while(i_c<CCandidatos)
    {
    j_a=0;
    D_Menor=INFINITY;
    
    while (j_a<CantPS)
    {
    q= nubeS[Lcand_Dist[i_c].p].p;// punto 
    
    a= nubeS[solucion[j_a]].p;//inicio segmento
    
    b= nubeS[solucion[((j_a+1)%CantPS)]].p;// fin segmento 
    
    Distance=Distance_point_seg(q.x, q.y, a.x,a.y,b.x, b.y);
    
    if( Distance < D_Menor){
    Lcand_Dist[i_c].dist=Distance;
    D_Menor= Distance;
    }
    j_a++;    
    }
    i_c++;
    }
    
    /*  printf( "\n arreglo Lcand_Dist \n ");
    for(k=0;k<CCandidatos;k++) 
    printf( "punto : %d  distancia:  %.2f --- x: %.2f  y: %.2f \n --- \n ",
    Lcand_Dist[k].p, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y); 
     *
    
    //Ordenar la lista de puntos candidatos Lcand_Dist de acuerdo a su distancia al polígono corriente 
    i=0;
    while(i<CCandidatos)
    {
    j=i+1;
    while (j<CCandidatos)
    {
    if( Lcand_Dist[j].dist < Lcand_Dist[i].dist)
    { temp=Lcand_Dist[i];
    Lcand_Dist[i]=Lcand_Dist[j];
    Lcand_Dist[j]=temp;
    }
    j++;
    }
    i++;
    }
    
    /*printf( "\n arreglo distancias ordenado segun distancia al poligono corriente  \n ");
    
    for(k=0;k<CCandidatos;k++)
    printf( "\n puntonubeS: %d - posorig: %d distancia:%.2f - x: %.2f y: %.2f \n ",
    Lcand_Dist[k].p, Lcand_Dist[k].posorig, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y);
    
    
    //printf( "\n==========================================================\n ");
    
    
    // busco entre los puntos candidatos ordenados mas cercanos los factibles ..
    pc=0;
    kg=0;
    while (pc < CCandidatos)
    {
    pp.ps=Lcand_Dist[pc].p; 	  // obtengo el indice donde se encuentra el candidato en nubeS .
    pp.po=Lcand_Dist[pc].posorig;
    
    // controlo que sea factible ... //unto_factible(pp.ps, CantP); 
    puntoNofactible=true; 
    poligono[CantP]= pp.ps; 
    CantP++;
    
    //calculo el cierre convexo de poligono , solucion + candidato 
    Cierre_Convexo(polcierre); 	
    
    // controlo que  no haya puntos de la nube dentro del CC
    c=0;
    for(k=0; k<CCandidatos; k++){
    if (nubeS[Lcandidatos[k]].marca!=0)
    {
    ch=InPoly(nubeS[Lcandidatos[k]].p, polcierre, CantPC);
    if ((ch=='i'))
    { c = c + 1; 
    //printf(" dentro el punto:%d x:%.2f, y:%.2f \n ", k, nubeS[k].p.x, nubeS[k].p.y );
    k=MAX; 
    puntoNofactible=true;
    
    }
    }
    }
    
    CantArVis=0;		
    if (c==0){ //si no hay puntos de la nube dentro de CC entonces calculo aristas visibles desde p
    
    CantArVis=CalculaAristasVisibles(nubeS[pp.ps].p);
    
    if (CantArVis!=0) puntoNofactible=false; 
    else     puntoNofactible=true;		       
    
    }
    
    if (puntoNofactible==false) {
    ar_ini = Elegir_Area_Greedy(pp.ps, &areat);
    pp.ar_in = ar_ini; 
    pp.area= areat;
    Lcand_G[kg]=pp; 
    kg++;
    fact=kg;
    }
    
    pc++;// pc posicion punto candidato en Lcand_Dist
    CantP--;	
    }
    
    /* printf( "\n\n arreglo puntos  factibles \n ");
    for(k=0;k<fact; k++) 
    printf( "\n punto en nubeS: %d - posorig: %d  ar_in:%d  area: %.2f-x: %.2f  y: %.2f \n ",
    Lcand_G[k].ps, Lcand_G[k].po, Lcand_G[k].ar_in ,Lcand_G[k].area, nubeS[Lcand_G[k].ps].p.x, nubeS[Lcand_G[k].ps].p.y);
    
    
    
    //Ordenar la lista de puntos factibles  Lcand_Dist de acuerdo al area que agregan  
    i=0;
    while(i<fact)
    {
    j=i+1;
    while (j<fact)
    {
    if(Lcand_G[j].area <= Lcand_G[i].area)
    { temp1 = Lcand_G[i];
    Lcand_G[i]=Lcand_G[j];
    Lcand_G[j]=temp1;
    }
    j++;
    }
    i++;
    }
    
    /* printf( "\n arreglo candidatos ordenado segun area   \n ");
    
    for(k=0;k<fact;k++) 
    printf( "punto en nubeS: %d - posorig: %d  -ar_in:%d  area: %.2f- x: %.2f  y: %.2f \n \n ",
    Lcand_G[k].ps, Lcand_G[k].po, Lcand_G[k].ar_in ,Lcand_G[k].area,nubeS[Lcand_G[k].ps].p.x, nubeS[Lcand_G[k].ps].p.y);
    
    
    return (Lcand_G[0]); 
    
    }
     */
    void Agregar_Punto_a_Solucion(int p, int p1) {
        int k = 0;

        int aux = 0;
        int pos = 0;

        while (aux < CantPS) {
            if (Iguales(nubeS[solucion[aux]].p, nubeS[p1].p) == true) {
                pos = aux;
                aux = CantPS;
            }
            aux++;
        }

        for (k = CantPS; k != pos + 1; k--) {
            solucion[k] = solucion[k - 1];
        }

        solucion[k] = p;
        CantPS++;

    }

    int Elegir_Area_Greedy(int p/*, double *area*/) {// retorna  posiicion de la arista visible en AristasVisibles[]
        //que con el punto  p  forma triangulo de menor  area
        int i = 0, k = 0;//arista
        double areat;
        double minarea = INFINITY;

        if (CantArVis != 0) {

            if (CantArVis == 1) {
                i = (int)AristasVisibles[0].x;//p1
                area = areatriangulo(nubeS[(int)AristasVisibles[k].x].p, nubeS[(int)AristasVisibles[k].y].p, nubeS[p].p);//p1, p2 en lugar de x, y

            } else {
                for (k = 0; k < CantArVis; k++)//calcular aristas que agregan triangulo  de menor area
                {
                    areat = areatriangulo(nubeS[(int)AristasVisibles[k].x].p, nubeS[(int)AristasVisibles[k].y].p, nubeS[p].p);//p1, p2 en lugar de x, y

                    if (areat < minarea) {
                        minarea = areat;
                        i = (int)AristasVisibles[k].x;//p1, p2 en lugar de x, y
                        area = minarea;
                    }
                }
            }
        } else {
            i = -1;
        }

        return (i);

    }

    /*===========================================================================*/
    nodo5 Seleccionar_area_greedy() {
        int j_a, i_c, k;
        int i, j;
        int kg = 0, fact = 0;
        int ar_ini;
        boolean puntoNofactible = true;

        int c = 0;
        char ch;

        int pf;
        nodo5 pp = new nodo5();
        PuntoDouble a, b, q;

        nodo4[] Lcand_Dist = new nodo4[MAX];
        nodo4 temp;

        nodo5[] Lcand_G = new nodo5[MAX];
        nodo5 temp1;

        double areat = INFINITY;
        area = INFINITY;

        double D_Menor, Distance;

        //Copiar lista de candidatos en Lcand_Dist

        for (k = 0; k < CCandidatos; k++) {
            Lcand_Dist[k] = new nodo4();
            Lcand_Dist[k].p = Lcandidatos[k];
            Lcand_Dist[k].dist = 0;
            Lcand_Dist[k].posorig = k;
        }

        //calcular distancias de punto candidato  al poligono corriente  
        i_c = 0;
        while (i_c < CCandidatos) {
            j_a = 0;
            D_Menor = INFINITY;

            while (j_a < CantPS) {
                q = nubeS[Lcand_Dist[i_c].p].p;// punto 

                a = nubeS[solucion[j_a]].p;//inicio segmento

                b = nubeS[solucion[((j_a + 1) % CantPS)]].p;// fin segmento 

                Distance = Distance_point_seg(q.x, q.y, a.x, a.y, b.x, b.y);

                if (Distance < D_Menor) {
                    Lcand_Dist[i_c].dist = Distance;
                    D_Menor = Distance;
                }
                j_a++;
            }
            i_c++;
        }

        /*  printf( "\n arreglo Lcand_Dist \n ");
        for(k=0;k<CCandidatos;k++) 
        printf( "punto : %d  distancia:  %.2f --- x: %.2f  y: %.2f \n --- \n ",
        Lcand_Dist[k].p, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y); 
         */

        //Ordenar la lista de puntos candidatos Lcand_Dist de acuerdo a su distancia al polígono corriente 
        i = 0;
        while (i < CCandidatos) {
            j = i + 1;
            while (j < CCandidatos) {
                if (Lcand_Dist[j].dist < Lcand_Dist[i].dist) {
                    temp = Lcand_Dist[i];
                    Lcand_Dist[i] = Lcand_Dist[j];
                    Lcand_Dist[j] = temp;
                }
                j++;
            }
            i++;
        }

        /*printf( "\n arreglo distancias ordenado segun distancia al poligono corriente  \n ");
        
        for(k=0;k<CCandidatos;k++)
        printf( "\n puntonubeS: %d - posorig: %d distancia:%.2f - x: %.2f y: %.2f \n ",
        Lcand_Dist[k].p, Lcand_Dist[k].posorig, Lcand_Dist[k].dist ,nubeS[Lcand_Dist[k].p].p.x, nubeS[Lcand_Dist[k].p].p.y);
        
        
        //printf( "\n==========================================================\n ");
         */

        // busco entre los puntos candidatos ordenados mas cercanos los que son factibles .. construyo  conjunto de puntos factibles 
        pc = 0;
        kg = 0;
        while (pc < CCandidatos) {
            pp.ps = Lcand_Dist[pc].p; 	  // obtengo el indice donde se encuentra el candidato en nubeS .
            pp.po = Lcand_Dist[pc].posorig;

            // controlo que sea factible ... //unto_factible(pp.ps, CantP); 
            puntoNofactible = true;
            poligono[CantP] = pp.ps;
            CantP++;

            //calculo el cierre convexo de poligono , solucion + candidato 
            Cierre_Convexo(polcierre);

            // controlo que  no haya puntos de la nube dentro del CC
            c = 0;
            for (k = 0; k < CCandidatos; k++) {
                if (nubeS[Lcandidatos[k]].marca != 0) {
                    ch = InPoly(nubeS[Lcandidatos[k]].p, polcierre, CantPC);
                    if ((ch == 'i')) {
                        c = c + 1;
                        //printf(" dentro el punto:%d x:%.2f, y:%.2f \n ", k, nubeS[k].p.x, nubeS[k].p.y );
                        k = MAX;
                        puntoNofactible = true;

                    }
                }
            }

            CantArVis = 0;
            if (c == 0) { //si no hay puntos de la nube dentro de CC entonces calculo aristas visibles desde p

                CantArVis = CalculaAristasVisibles(nubeS[pp.ps].p);

                if (CantArVis != 0) {
                    puntoNofactible = false;
                } else {
                    puntoNofactible = true;
                }

            }

            if (puntoNofactible == false) {
                ar_ini = Elegir_Area_Greedy(pp.ps/*,  & areat*/); // arista  visibkle con la que forma triangul de menor area 
                pp.ar_in = ar_ini;
                pp.area = area;
                Lcand_G[kg] = new nodo5(pp);
                kg++;
                fact = kg;
            }

            pc++;// pc posicion punto candidato en Lcand_Dist
            CantP--;
        }

        /* printf( "\n\n arreglo puntos  factibles \n ");
        for(k=0;k<fact; k++) 
        printf( "\n punto en nubeS: %d - posorig: %d  ar_in:%d  area: %.2f-x: %.2f  y: %.2f \n ",
        Lcand_G[k].ps, Lcand_G[k].po, Lcand_G[k].ar_in ,Lcand_G[k].area, nubeS[Lcand_G[k].ps].p.x, nubeS[Lcand_G[k].ps].p.y);
        
         */

        //Ordenar la lista de puntos factibles  Lcand_Dist de acuerdo al area que agregan  
        i = 0;
        while (i < fact) {
            j = i + 1;
            while (j < fact) {
                if (Lcand_G[j].area <= Lcand_G[i].area) {
                    temp1 = Lcand_G[i];
                    Lcand_G[i] = Lcand_G[j];
                    Lcand_G[j] = temp1;
                }
                j++;
            }
            i++;
        }

        /* printf( "\n arreglo candidatos ordenado segun area   \n ");
        
        for(k=0;k<fact;k++) 
        printf( "punto en nubeS: %d - posorig: %d  -ar_in:%d  area: %.2f- x: %.2f  y: %.2f \n \n ",
        Lcand_G[k].ps, Lcand_G[k].po, Lcand_G[k].ar_in ,Lcand_G[k].area,nubeS[Lcand_G[k].ps].p.x, nubeS[Lcand_G[k].ps].p.y);
        
         */
        return (Lcand_G[0]); // devuelve e l punto y arista  que aagregan menor area 

    }


    /*===========================================================================*/
// construyeP: construye  poligonizacion 
/*===========================================================================*/
    int construyeP(int iter, int max_punt) // construye  poligonizacion 
    {
        int k = 0, v;
        Punto p_sel;
        nodo5 p_selArG = new nodo5();
        int prox_p = -1;
        int pos_ca;
        int k_arista = 0;
        int no_factible = 0;
        int ar = 0;
        int CantPuntos;

        CantPS = 0;
        CantPuntos = CantPNube;
        CCandidatos = CantPNube;

        for (v = 0; v < CantPNube; v++) {
            nubeS[v].marca = 1;
        }

        // 1)  arma el triangulo inicial usando alguno de los criterios : 3p azar, 1p azar-y dos mas cercanos o  triangulo menor area

        switch (opciones[0]) {
            case 0:// elige un triangulo vacio de puntos de la nube, eligiendo los tres puntos en forma aleatoria 
                Generar_triangulo_Puntos_Azar();
                break;
            case 1:// elige un triangulo vacio de puntos de la nube de menor area 
                Generar_Triangulo_Menor_Area();
                break;
            default:// Elige el menor triangulo vacio de puntos de la nube, eligiendo el primer punto al azar y los otros dos entre los mas cercanos a él . 
                Generar_triangulo_PAzar_Pmascercanos();
        }

        CantPuntos = CantPuntos - 3;
        CantPS = 3;
        CantP = 3;
        MaxPS = 3;

        while (CantPuntos > 0) {

            // 2) seecciona el proximo punto a agregar 

            //p_sel = new Punto(Seleccionar_proximo_punto());
            switch (opciones[1]) {
                case 0:
                    p_sel = new Punto(Seleccionar_punto_Azar());
                    break;
                case 1:
                    p_sel = Seleccionar_punto_greedy();
                    break;
                default: {
                    p_selArG = Seleccionar_area_greedy();
                    p_sel = new Punto(p_selArG.ps, p_selArG.po);
                }
            }

            prox_p = (int)p_sel.x; // indice en nubeS del proximo punto elegido

            pos_ca = (int)p_sel.y;  // posiicion del proximo punto elegido en Lcand

            // 3)ahora debe elegir  la arista visible  que se reemplaza , criterio  greedy o azar 
            if (prox_p != -1) {

                switch (opciones[2]) {
                    case 0:
                        k_arista = Elegir_Arista_Azar();
                        break;// elige una arista visible AL AZAR-genera poligono aleatorio 
                    case 1:
                        k_arista = Elegir_Arista_Greedy(prox_p);
                        break;// elige una arista en forma GREEDY, la que agrega menor area al poligono tratando de minimizar el area del poligono 
                    default:
                        k_arista = p_selArG.ar_in;
                }

                if (k_arista != -1) {

                    if (opciones[2] != 2) {
                        Agregar_Punto_a_Solucion(prox_p, (int)AristasVisibles[k_arista].x);
                    } else {
                        Agregar_Punto_a_Solucion(prox_p, k_arista);
                    }

                    nubeS[prox_p].marca = 0; // Quitar_punto_de_Nube
                    CantPuntos--;
                    quitar_punto_candidato(pos_ca);

                    for (k = 0; k < CantPS; k++) {
                        poligono[k] = solucion[k]; //copia solucion en poligono corriente 
                    }
                    CantP = CantPS;
                    solucion[CantPS] = solucion[0];

                }
            }
        }


        if (CantPuntos == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int size() {
        return CantPS;
    }

    public Arista getArista(int pos) {
        return new Arista(nubeS[solucion[pos]].p, nubeS[solucion[(pos + 1) % solucion.length]].p);
    }

    public ArrayList<PuntoDouble> getPuntos() {
        return null;
    }
    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
    // TODO code application logic here
    PoIS poIS = new PoIS(generarPuntos(), opciones);
    }*/
}
