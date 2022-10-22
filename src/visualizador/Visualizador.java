/*
 * Visualizador.java
 *
 * Created on 6 de septiembre de 2009, 20:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visualizador;

import java.util.Locale;
import javax.swing.*;
/**
 *
 * @author Paola
 */
public class Visualizador/* extends JApplet*/{
    static JFrame frame;
   /* public void init(){
        getContentPane().add(new JLabel ("Visualizador"));
    }
    /**
     * Creates a new instance of Visualizador
     */
    public Visualizador() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //JApplet applet = new Visualizador ();
         Locale locale = null;
        //el primer parametro es el idioma, el segundo es el país, siempre en MAYUSCULAS.
        // ejemplo: es "español", en "ingles", US "Estados Unidos", MX "Mexico"
        locale = new Locale("en","US");
        Locale.setDefault(locale);
        frame = new FV ();
       // frame.getContentPane().add(applet);
        //frame.setSize(750,600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        frame.setLocationRelativeTo(frame);
        /*applet.init();
        applet.start();*/
        //Locale lDefecto = Locale.getDefault();
        //Locale.setDefault(Locale.ENGLISH);
        frame.setVisible(true);
    }
    
}
