/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visLotes;

import java.util.ArrayList;
import visualizador.Arista;

/**
 *
 * @author Pablo
 */
class LotePoligonos extends Lote{

    public ArrayList<Arista> getSiguiente() {
        pos = (pos == cantCambios - 1)? pos : pos + 1;
        corriente = poligonos.get(pos);
        return corriente;
    }

    public ArrayList<Arista> getAnterior() {
        pos = (pos == 0)? pos : pos - 1;
        corriente = poligonos.get(pos);
        return corriente;
    }

    public ArrayList<Arista> getUltimo() {
        pos = cantCambios - 1;
        corriente = poligonos.get(pos);
        return corriente;
    }

    public ArrayList<Arista> setPos(int p) {
        pos = p;
        corriente = poligonos.get(pos);
        return corriente;
    }
}
