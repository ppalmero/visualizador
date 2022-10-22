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
public class LoteFlips extends Lote{

    public ArrayList<Arista> getSiguiente() {
        pos++;
        if (pos <= cantCambios - 1){
            removerAristas(outAristas.get(pos));
            agregarAristas(inAristas.get(pos));
        } else{
            pos--;
        }
        return corriente;
    }

    public ArrayList<Arista> getAnterior() {
        pos--;
        if (pos >= -1){
            removerAristas(inAristas.get(pos + 1));
            agregarAristas(outAristas.get(pos + 1));
        } else{
            pos++;
        }
        return corriente;
    }

    public ArrayList<Arista> getUltimo() {
        while (pos < cantCambios - 1){
            getSiguiente();
        }
        return getSiguiente();
    }

    public ArrayList<Arista> setPos(int p) {
        if (p > pos) {
            while (pos < p) {
                getSiguiente();//calcularSiguiente();//DENTRO DEL PROCEDIMIENTO SE INCREMENTA POS
            }
        } else {
            if ((pos - p) < p) {
                while (pos > p) {
                    getAnterior();//calcularPrevio();//DENTRO DEL PROCEDIMIENTO SE DECREMENTA POS
                }
            } else {
                corriente = (ArrayList<Arista>) inicial.clone();
                pos = 0;
                while (pos < p) {
                    getSiguiente();//calcularSiguiente();//DENTRO DEL PROCEDIMIENTO SE INCREMENTA POS
                }
            }
        }

        return corriente;
    }

    private void agregarAristas(ArrayList<Arista> inA) {
        for (int i = 0; i < inA.size(); i++){
            corriente.add(inA.get(i));
        }
    }

    private void removerAristas(ArrayList<Arista> outA) {
        for (int i = 0; i < outA.size(); i++){
            corriente.remove(outA.get(i));
        }
    }
}
