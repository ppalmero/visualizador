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
 */
public class Convexo extends Poligono{

    public static void main(String[] args) {
        Convexo c = new Convexo(60);
    }

    public Convexo(int cantPuntos) {
        generador(cantPuntos);
    }
    int M = 80;  // cantidad maxima de vertices  cambiar cuando cambias tamaño ...
    static final int screen = 400;
//#define true 1
//#define false 0
    int b1 = -screen; // rango de coordenadas , aca puedo cambiar y poner  el rango que quiera , asi como esta
    int b2 = screen; // genera puntos  entre  -1200 y 1200,
//Generador de  poligonos convexos. Parte de un triangulo  aleatorio  y va  agregando puntos uno a uno .
    ArrayList<PuntoDouble> p = new ArrayList<PuntoDouble>(M);
    int cant_ver = 0;
    int ind = 0;
    int max_ver;
    float seed;
    boolean secortan = false;
    
    /*int pun_iguales ( punto p1, punto p2)   {
	if ((fabs(p1.x-p2.x) <= 0.00001 )&&(fabs(p1.y-p2.y) <= 0.00001 )) return(1);
	            else return(0);
    }

     * 
     */
    
    public double getDecimal(int numeroDecimales, double decimal) {
        decimal = decimal * (java.lang.Math.pow(10, numeroDecimales));
        decimal = java.lang.Math.round(decimal);
        decimal = decimal / java.lang.Math.pow(10, numeroDecimales);

        return decimal;
    }
    
    /* calcula el area signada de tres puntos*/
    /*si el valor del area signada es :
    as<0 entonces  p3 esta a la derecha  de la recta determinada por los puntos p1 y p2
    as>0 entonces  p3 esta a la izquierda  de la recta determinada por los puntos p1 y p2
    as=0 entonces  p3 esta sobre la recta determinada por los puntos p1 y p2*/
    private double areasignada(PuntoDouble pu1, PuntoDouble pu2, PuntoDouble pu3) {
        double salida;

        salida = ((pu1.x * pu2.y + pu2.x * pu3.y + pu3.x * pu1.y) - (pu1.x * pu3.y + pu2.x * pu1.y + pu3.x * pu2.y));

        return ((0.5 * salida));
    }

    private double rnd(double low, double high) {/*toma un  random entre low y high */
        double i;

        if (low >= high) {
            return low;
        }
        if (((i = (1.0 * Math.random()) * (high - low) + low)) > high) {
            i = high;
        }
        return i;
    }

    /* calcula el producto vectorial entre tres puntos */
    double vectorial(PuntoDouble p1, PuntoDouble p2, PuntoDouble p3) {
        PuntoDouble u = new PuntoDouble();
        PuntoDouble v = new PuntoDouble();

        u.x = (p2.x - p1.x);        //Calculamos el primer vector
        u.y = (p2.y - p1.y);        //Calculamos el segundo vector
        v.x = (p3.x - p1.x);
        v.y = (p3.y - p1.y);

        return ((u.x * v.y) - (v.x * u.y));
    }

    /*indica si las rectas determinadas por los  puntos r1 r2 y los puntos  q1 y q2 y
    calcula el punto en que se cortan cx,cy */
    private PuntoDouble cortan_rectas(PuntoDouble r1, PuntoDouble r2, PuntoDouble q1, PuntoDouble q2)//, Punto *p_corte)
    {
        double v1, v2, w1, w2;
        double cx = 0.0, cy = 0.0;
        PuntoDouble p;
        PuntoDouble p_corte = new PuntoDouble();

        v1 = r2.x - r1.x;
        v2 = r2.y - r1.y;
        w1 = q2.x - q1.x;
        w2 = q2.y - q1.y;

        if ((v1 * w2) == (v2 * w1)) {
            secortan = false;
            //return (null); //Son paralelas
        } else { // calcula el punto de corte entre las dos rectas
            cx = (q1.x * (q2.y * (r1.x - r2.x) - r1.x * r2.y + r1.y * r2.x) - q2.x * (q1.y * (r1.x - r2.x) - r1.x * r2.y + r1.y * r2.x)) / (q1.x * (r1.y - r2.y) + q1.y * (r2.x - r1.x) + q2.x * (r2.y - r1.y) + q2.y * (r1.x - r2.x));

            cy = (q1.x * q2.y * (r1.y - r2.y) - q1.y * (q2.x * (r1.y - r2.y) + r1.x * r2.y - r1.y * r2.x) + q2.y * (r1.x * r2.y - r1.y * r2.x)) / (q1.x * (r1.y - r2.y) + q1.y * (r2.x - r1.x) + q2.x * (r2.y - r1.y) + q2.y * (r1.x - r2.x));

            //p_corte = new PuntoDouble(cx, cy);
            /*p_corte.x=cx;
            p_corte.y=cy;*/
            p_corte = new PuntoDouble(cx, cy);
            p = new PuntoDouble(cx, cy);

            if (p.igualQue(q1) || p.igualQue(q2)) {
                secortan = true;
                //return (p_corte);
            } else if ((vectorial(r1, p, q1) / vectorial(r1, p, q2) < 0.0)) {
                secortan = true;
                //return (p_corte);
            } else {
                secortan = false;
                //return (null);// no se cortan
            }
        }
        return p_corte;
    }

    /* ordena el poligonos  en orden contario al de las  agujas del reloj empezando
    con el vetrice de menor ordenada ( coordenada y) */
    void ordena_MenorY(int cver) {
        int menor = 0;
        int i;
        ArrayList<PuntoDouble> a = new ArrayList<PuntoDouble>(M);//M

        // Detecto posicion del punto con menor coordenada Y
        for (i = 0; i < cver; i++) {
            if (p.get(i).y < p.get(menor).y) {
                menor = i;
            } else {
                if (getDecimal(4, p.get(i).y) == getDecimal(4, p.get(menor).y)) {
                    if (p.get(i).x < p.get(menor).x) {
                        menor = i;
                    }
                }
            }
        }
        //copio  p en un arreglo auxiliar desde pos menor
        for (i = 0; i < cver; i++) {
            a.add(new PuntoDouble(p.get(menor)));
            menor = (menor + 1) % cant_ver;
        }

        for (i = 0; i < cver; i++) {
            p.set(i, new PuntoDouble(a.get(i))); // recupero arreglo auxiliar  en p
        }
    }

    /* hace lugar en el arreglo para guardar un vertice*/
    void shiftea(int pos) {//ES CIRCULAR?
        int i;
        i = cant_ver;

        while (i > pos) {
            if (i >= p.size()){
                p.add(new PuntoDouble(p.get(i - 1)));
            } else{
                p.set(i, new PuntoDouble(p.get((i - 1) % cant_ver)));
            }
            i--;
        }
    }

    boolean es_convexo(int cver) {   /* testea si un poligono es o no convexo*/

        int v;
        double asignada;

        boolean convexo = true;


        for (v = 1; v <= cver - 1; v++) {//v = 0; v <= cver -1

            asignada = areasignada(p.get((v - 1) % cver), p.get(v % cver), p.get((v + 1) % cver));

            if (asignada < 0) {
                convexo = false;
            }
        }
        return convexo;
    }

    /***************************************************************/
    /*                GENERADOR POLIGONOS                          */
    /***************************************************************/
    void generador(int Mver) {
        int pos = 0, i = 0, v = 0, i_bor = 0, bor = 0;
        boolean exito;
        boolean b_en_OX = false, c_en_OX = false;
        boolean corta_ant = false;
        boolean corta_sig = false;
        boolean c_en_BOX = false, b_en_BOX = false;

        double as1, as2, as3 = 0.0;
        double lamda, d_b = 0.0, d_c = 0.0;


        PuntoDouble aux, aux1;       // puntos auxiliares
        PuntoDouble origen;
        ArrayList<PuntoDouble> ptosborde = new ArrayList<PuntoDouble>(4);

        PuntoDouble ptocorte;

        PuntoDouble bari; // baricentro del trinagulo
        PuntoDouble a = new PuntoDouble(), b, c, X, Y; //puntos auxiliares para generacion a corte con lado i , b corte con lado i-1,
        // c punto corte con i+1 x punto direccion, y punto corte con l

        PuntoDouble pfin = new PuntoDouble();
        PuntoDouble e1, e2, e3, e4; // bordes

        int ind_sig, ind_ant, ind_sig2 = 0;

        origen = new PuntoDouble();

        e1 = new PuntoDouble(b1, b1);

        e2 = new PuntoDouble(b2, b1);

        e3 = new PuntoDouble(b2, b2);

        e4 = new PuntoDouble(b1, b2);

        ptosborde.add(e1);
        ptosborde.add(e2);
        ptosborde.add(e3);
        ptosborde.add(e4);

// Genero el triangulo inicial aleatorio
        do {
            cant_ver = 0;
            // genero 3 puntos aleatorios
            //.. aca  podemos  cambiar el rango de x e y para que sean positivos
            p = new ArrayList<PuntoDouble>();
            p.add(new PuntoDouble(rnd(-screen, screen), rnd(-screen, screen)));

            cant_ver++;

            // genero 2do vertice aleatorio
            do {
                aux = new PuntoDouble(rnd(-screen, screen), rnd(-screen, screen));
            } while (aux.igualQue(p.get(0)));

            p.add(new PuntoDouble(aux));
            cant_ver++;

            // genero 3er vertice aleatorio
            // controlo que el 3er punto no sea igual a p0 ni a p1 y que no sea colineal con ellos
            do {
                aux.x = rnd(-screen, screen);
                aux.y = rnd(-screen, screen);
            } while (aux.igualQue(p.get(0)) || aux.igualQue(p.get(1)) || (areasignada(p.get(0), p.get(1), aux) == 0.0));

            p.add(aux);
            cant_ver++;

        } while (!es_convexo(cant_ver));

        ordena_MenorY(cant_ver); //ordeno vertices del triangulo
        //mostrar(cant_ver);

        //Calculo el baricentro de los tres puntos
        bari = new PuntoDouble((p.get(0).x + p.get(1).x + p.get(2).x) / 3, (p.get(0).y + p.get(1).y + p.get(2).y) / 3);

        // traslado  baricentro al origen
        origen.x = (bari.x - bari.x);
        origen.y = (bari.y - bari.y);
        // traslado triangulo al origen
        for (i = 0; i < 3; i++) {
            aux.x = p.get(i).x - bari.x;
            aux.y = p.get(i).y - bari.y;
            p.set(i, new PuntoDouble(aux));
        }

        // Paso general........con control de generacion

        while (cant_ver < Mver) { //hago  hasta que no haya generado M puntos

            exito = false;

            while (!exito) { // agregar un nuevo punto

                //genero un punto aleatorio X  que
                //determina la direccion hacia donde voy a generar el nuevo vertice
                X = new PuntoDouble(rnd(b1, b2), rnd(b1, b2));

                //determino que lado del poligono existente corta la recta OX
                ind = 0;
                for (v = 0; v <= (cant_ver - 1); v++) { /* a punto de corte del lado ind,ind+1 con OX*/
                    a = new PuntoDouble(cortan_rectas(p.get(v), p.get((v + 1) % cant_ver), origen, X));
                    if (secortan) {
                        ind = v; // guarda el  lado que corta la recta Origen-Pgenerado(OX)
                        v = cant_ver;
                    }
                }

                //determino que lado de la caja corta la recta  OX
                //bordes de la caja e1, e2, e3, e4

                i_bor = 0;
                for (bor = 0; bor < 4; bor++) {
                    Y = new PuntoDouble(cortan_rectas(ptosborde.get(bor), ptosborde.get((bor + 1) % 4), origen, X));
                    if (secortan) {
                        i_bor = bor; // guarda el  lado que corta la recta (OX)
                        bor = 4;
                    }
                }

                //en ind se inicia el lado que corta OP con lado (ind,ind+1)
                // calculo incio lado siguiente , lado anterior ,
                ind_sig = (ind + 1) % cant_ver;

                ind_sig2 = (ind + 2) % cant_ver;

                if (ind == 0) {
                    ind_ant = (cant_ver - 1);
                } else {
                    ind_ant = (ind - 1);
                }

                corta_ant = false;
                b = new PuntoDouble(cortan_rectas(p.get(ind_ant), p.get(ind), origen, X));
                corta_ant = secortan;//(b != null ? true : false);
                // punto de corte del lado ind-1,ind con OX lado anterior

                corta_sig = false;
                c = new PuntoDouble(cortan_rectas(p.get(ind_sig), p.get(ind_sig2), origen, X));
                corta_sig = secortan;//(c != null ? true : false);
                // punto de corte del lado ind+1,ind+2 con OX lado siguiente

                // verifico que b y c esten dentro de la caja
                b_en_BOX = false;
                if (((b.x >= b1) && (b.x <= b2)) && ((b.y >= b1) && (b.y <= b2))) {
                    b_en_BOX = true;
                }
                c_en_BOX = false;
                if (((c.x >= b1) && (c.x <= b2)) && ((c.y >= b1) && (c.y <= b2))) {
                    c_en_BOX = true;
                }

                // veo  si estan si  b y c en OX, si estan calculo distancia al origen
                b_en_OX = false;
                if ((b.x >= 0) && (b.x < X.x)) {
                    d_b = ((b.x * b.x) + (b.y * b.y));
                    b_en_OX = true;
                }
                c_en_OX = false;
                if ((c.x >= 0) && (c.x < X.x)) {
                    d_c = ((c.x * c.x) + (c.y * c.y));
                    c_en_OX = true;
                }


                if (b_en_BOX && c_en_BOX) {
                    //si estan los dos en OX elijo el mas ceraçcao al origen
                    if (b_en_OX && c_en_OX) {
                        if (d_b <= d_c) {
                            pfin = new PuntoDouble(b);
                        } else {
                            pfin = new PuntoDouble(c);
                        }
                    } // si esta b  en OX  elijo a b
                    else if (b_en_OX && !c_en_OX) {
                        pfin = new PuntoDouble(b);
                    } // si esta c  en OX elijo a c
                    else if (!b_en_OX && c_en_OX) {
                        pfin = new PuntoDouble(c);
                    } // si no est ninguno de los dos en OX elijo a Y (punto d corte con borde)
                    else {
                        pfin = X;
                    }
                } else {
                    if (b_en_BOX && !c_en_BOX) { // si esta b  en OX elijo a b
                        if (b_en_OX) {
                            pfin = new PuntoDouble(b);
                        }
                    } else {
                        if (!b_en_BOX && c_en_BOX) {// si esta c  en OX elijo a c
                            if (c_en_OX) {
                                pfin = new PuntoDouble(c);
                            }
                        } else { // si no estan ninguno de los dos en OX elijo a Y (pcorte con borde)
                            if (!b_en_BOX && !c_en_BOX) {
                                pfin = new PuntoDouble(X);
                            }
                        }
                    }
                }



                // ya puedo generar el vertice nuevo del poligono

                //el segmento a-pfin es donde voy a generar el vertice

                // lambda parametro entre 0 y 1 ayuda para controlar la forma del poligono convexo

                // si genero  lamba entre 1/2 y 1  mas cercano a inicio segmento   obtengo poligonos mas alargados
                // si genero lamda entre 0 y 1/2 mas cercano a pfin obtengo poligonos mas redondeados

                lamda = rnd(0.5, 1); // aca cambiar parametros segun lo  que quiero

                aux1 = new PuntoDouble((lamda * a.x) + ((1 - lamda) * pfin.x), (lamda * a.y) + ((1 - lamda) * pfin.y));
                /*aux1.x= ((lamda*a.x)+ ((1-lamda)*pfin.x));
                aux1.y= ((lamda*a.y)+ ((1-lamda)*pfin.y));*/

                // controlar  que el punto generado este:
                as1 = areasignada(p.get(ind), p.get(ind_sig), aux1); //derecha de ind,ind+1
                as2 = areasignada(p.get(ind_ant), p.get(ind), aux1); //a la  izquierda de ind-1,ind
                as3 = areasignada(p.get(ind_sig), p.get(ind_sig2), aux1); // a la izquierda de ind+1,ind+2

                if ((as1 < 0) && (as2 > 0) && (as3 > 0)) {
                    // agregar vertice  pto entre p[ind]y p[ind+1]
                    if (ind != cant_ver - 1) {// si el lado no es el ultimo
                        pos = ((ind + 1) % cant_ver);
                        shiftea(pos);//hago lugar para guardarlo
                    } else {
                        pos = ind + 1;  // si ind es el  eultimo  entoncs inserto despues del ultimo.
                    }
                    /*p[pos].x=aux1.x; //guardo el vertice
                    p[pos].y=aux1.y;*/
                    if (pos == p.size()){
                        p.add(new PuntoDouble(aux1));
                    } else {
                        p.set(pos, new PuntoDouble(aux1));
                    }

                    cant_ver++; // agrego un vertice

                    exito = true;

                }
            }
        }
    }
    public int size(){
        return p.size();
    }
    
    public Arista getArista(int pos){
        return new Arista(new PuntoDouble (p.get(pos).x + 400, p.get(pos).y + 400), new PuntoDouble(p.get((pos + 1) % p.size()).x + 400, p.get((pos + 1) % p.size()).y + 400));
    }
    
    public ArrayList<PuntoDouble> getPuntos(){
        return p;
    }
}
