package visualizador;

//import grafos.*;


public class WTrig extends Object {
  public Punto p1;
  public Punto p2;
  public Punto p3;
  public Punto pC;
  public double R;

  public WTrig(Punto ap1, Punto ap2, Punto ap3) {
    p1 = ap1;
    p2 = ap2;
    p3 = ap3;
    double dx2 = p2.x - p1.x;
    double dy2 = p2.y - p1.y;
    double dr2 = dx2 * dx2 + dy2 * dy2;
    double dx3 = p3.x - p1.x;
    double dy3 = p3.y - p1.y;
    double dr3 = dx3 * dx3 + dy3 * dy3;
    double A = 2 * (dx2 * dy3 - dx3 * dy2);
    double dx = (dr2 * dy3 - dr3 * dy2) / A;
    double dy = (dx2 * dr3 - dx3 * dr2) / A;
    pC = new Punto((int)(p1.x + dx), (int)(p1.y + dy));
    R = Math.sqrt(dx * dx + dy * dy);
  }

}
