package visualizador;

import java.awt.geom.Point2D;


public class Triangulo
{

    public Punto PuntoRestante(Punto pto1, Punto pto2)
    {
        if(pto1 == verticeA && pto2 == verticeB || pto2 == verticeA && pto1 == verticeB)
            return verticeC;
        if(pto1 == verticeC && pto2 == verticeB || pto2 == verticeC && pto1 == verticeB)
            return verticeA;
        if(pto1 == verticeA && pto2 == verticeC || pto2 == verticeA && pto1 == verticeC)
            return verticeB;
        else
            return null;
    }

    public Triangulo(Punto pA, Punto pB, Punto pC)
    {
        verticeA = pA;
        verticeB = pB;
        verticeC = pC;
    }

    public Triangulo(Point2D pA, Point2D pB, Point2D pC)
    {
        verticeA = (Punto)pA;
        verticeB = (Punto)pB;
        verticeC = (Punto)pC;
    }

    public Punto verticeA;
    public Punto verticeB;
    public Punto verticeC;

    public Punto getVerticeA() {
        return verticeA;
    }

    public Punto getVerticeB() {
        return verticeB;
    }

    public Punto getVerticeC() {
        return verticeC;
    }
}
