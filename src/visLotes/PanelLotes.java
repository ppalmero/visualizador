/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelLotes.java
 *
 * Created on 20/09/2011, 00:43:20
 */
package visLotes;

import EPS.EpsGraphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import visualizador.Arista;
import visualizador.Conjunto;
import visualizador.FV;
import visualizador.Punto;

/**
 *
 * @author Pablo
 */
public class PanelLotes extends javax.swing.JPanel {

    private static final int FLIPS = 0;
    private static final int POLIGONOS = 1;
    private float zoom = 1f;
    private Conjunto puntos;
    private int puntoSeleccionado = -1;
    private boolean guardarPNG = false;
    private int ver = POLIGONOS;
    private String nombreArchivoG;
    private Lote lote;
    private ArrayList<Arista> mostrar = null;
    private float dilacion;
    private int widthInicial = 1000;
    private int heightInicial = 1000;
    private boolean guardarEPS;

    /** Creates new form PanelLotes */
    public PanelLotes() {
        initComponents();
        puntos = new Conjunto();
        lote = new LotePoligonos();
    }

    public void paint(Graphics h) {
        super.paint(h);
        Graphics2D g = (Graphics2D) h;
        BufferedImage imagen = new BufferedImage(1100, 750, BufferedImage.TYPE_INT_RGB);
        Graphics2D iGuardar = imagen.createGraphics();
        Graphics2D eps = new EpsGraphics2D();
        iGuardar.setColor(Color.WHITE);
        eps.setColor(Color.WHITE);
        iGuardar.fillRect(0, 0, 1100, 750);
        g.scale(zoom, zoom);
        iGuardar.scale(0.7, 0.7);
        eps.scale(0.7, 0.7);

     
        
        g.setColor(new Color(200, 200, 200));
        iGuardar.setColor(new Color(200, 200, 200));
        eps.setColor(new Color(200, 200, 200));

        if (mostrar != null) {
            for (int i = 0; i < mostrar.size(); i++) {
                dibujarArista(g, puntos.get(mostrar.get(i).getPosPo()), puntos.get(mostrar.get(i).getPosPd()));
                dibujarArista(iGuardar, puntos.get(mostrar.get(i).getPosPo()), puntos.get(mostrar.get(i).getPosPd()));
                dibujarArista(eps, puntos.get(mostrar.get(i).getPosPo()), puntos.get(mostrar.get(i).getPosPd()));
            }

            if (lote.getPos() >= 0 && ver == FLIPS) {
                ArrayList<Arista> in = lote.getInAristas();
                ArrayList<Arista> out = lote.getOutAristas();
                for (int i = 0; i < out.size(); i++) {
                    g.setColor(Color.red);
                    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, new float[]{3.0f}, 0.0f));
                    dibujarArista(g, puntos.get(out.get(i).getPosPo()),
                            puntos.get(out.get(i).getPosPd()));
                    iGuardar.setColor(Color.red);
                    iGuardar.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, new float[]{3.0f}, 0.0f));
                    dibujarArista(iGuardar, puntos.get(out.get(i).getPosPo()),
                            puntos.get(out.get(i).getPosPd()));
                    eps.setColor(Color.red);
                    eps.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, new float[]{3.0f}, 0.0f));
                    dibujarArista(eps, puntos.get(out.get(i).getPosPo()),
                            puntos.get(out.get(i).getPosPd()));
                }
                for (int i = 0; i < in.size(); i++) {
                    g.setColor(Color.GREEN);
                    g.setStroke(new BasicStroke(2));
                    dibujarArista(g, puntos.get(in.get(i).getPosPo()),
                                     puntos.get(in.get(i).getPosPd()));
                    iGuardar.setColor(Color.GREEN);
                    iGuardar.setStroke(new BasicStroke(2));
                    dibujarArista(iGuardar, puntos.get(in.get(i).getPosPo()),
                                     puntos.get(in.get(i).getPosPd()));
                    eps.setColor(Color.GREEN);
                    eps.setStroke(new BasicStroke(2));
                    dibujarArista(eps, puntos.get(in.get(i).getPosPo()),
                                     puntos.get(in.get(i).getPosPd()));
                }
            }
        }
        
        for (int i = 0; i < puntos.size(); i++) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(3));
            g.drawOval((int) (puntos.get(i).getX()) - 2, (int) (-puntos.get(i).getY()) - 2, 5, 5);
            iGuardar.setColor(Color.BLACK);
            iGuardar.setStroke(new BasicStroke(3));
            iGuardar.drawOval((int) (puntos.get(i).getX()) - 2, (int) (-puntos.get(i).getY()) - 2, 5, 5);
            eps.setColor(Color.BLACK);
            eps.setStroke(new BasicStroke(3));
            eps.drawOval((int) (puntos.get(i).getX()) - 2, (int) (-puntos.get(i).getY()) - 2, 5, 5);
            
            if (puntoSeleccionado == i) {
                g.setFont(new Font("Arial", 1,(int)(12 / zoom)));
                g.drawString(puntos.get(i).getVReal(), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) - 10);
                iGuardar.setFont(new Font("Arial", 1,(int)(12 / zoom)));
                iGuardar.drawString(puntos.get(i).getVReal(), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) - 10);
                eps.setFont(new Font("Arial", 1,(int)(12 / zoom)));
                eps.drawString(puntos.get(i).getVReal(), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) - 10);
            }
            g.setFont(new Font("Arial", -1,(int)(10 / zoom)));
            g.drawString(String.valueOf(i), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) + 15);
            iGuardar.setFont(new Font("Arial", -1,(int)(10 / zoom)));
            iGuardar.drawString(String.valueOf(i), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) + 15);
            eps.setFont(new Font("Arial", -1,(int)(10 / zoom)));
            eps.drawString(String.valueOf(i), (int) puntos.get(i).getX(), (int) (-puntos.get(i).getY()) + 15);
        }

        if (guardarPNG) {
            try {
                ImageIO.write(imagen, "png", new File(nombreArchivoG));
                guardarPNG = false;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Save PNG", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (guardarEPS) {
            try {
                guardarEPS = false;
                BufferedWriter out = new BufferedWriter(new FileWriter(nombreArchivoG));
                out.write(eps.toString());
                out.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Save EPS", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:
        Punto raton = new Punto(evt.getX() / zoom, -evt.getY() / zoom);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        int i;
        for (i = 0; i < puntos.size(); i++) {

            Shape s = devolverOvalo(puntos.get(i), 10);
            if (s.contains(raton)) {
                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                puntoSeleccionado = i;
                repaint();
                break;
            }

        }
        if (i == puntos.size()) {
            puntoSeleccionado = -1;
            repaint();
        }
    }//GEN-LAST:event_formMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setZoom(float zoom) {
        this.zoom = zoom;
        Dimension d = new Dimension((int)(widthInicial * zoom), (int)(heightInicial * zoom));
        this.setPreferredSize(d);
        this.revalidate();
        repaint();
    }

    protected Shape devolverOvalo(Punto p, int ancho) {
        return new Ellipse2D.Double((p.getX() - ancho / 2), (-p.getY() - ancho / 2), ancho, ancho);
    }

    private void dibujarArista(Graphics2D h2d, Punto o, Punto d) {
        h2d.drawLine((int) (o.getX()),
                (int) (-o.getY()),
                (int) (d.getX()),
                (int) (-d.getY()));
    }

    public void addPunto(Punto p) {
        puntos.add(p);
    }

    void resetPuntos() {
        puntos = new Conjunto();
    }

    void guardarImagen(boolean b) {
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file- .png", "png");
        JFileChooser archivo = new JFileChooser(FV.getDirectorio());
        archivo.addChoosableFileFilter(filtro);
        filtro = new FileNameExtensionFilter("Image file- .eps", "eps");
        archivo.addChoosableFileFilter(filtro);
        if (archivo.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (archivo.getSelectedFile() != null) {
                if (archivo.getFileFilter().getDescription().equals("Image file- .eps")){
                    nombreArchivoG = archivo.getSelectedFile().getAbsolutePath() + ".eps";
                    guardarEPS = b;
                } else if (archivo.getFileFilter().getDescription().equals("Image file- .png")){
                    nombreArchivoG = archivo.getSelectedFile().getAbsolutePath() + ".png";
                    guardarPNG = b;
                }

                //Guarda el último directorio accedido
                FV.setDirectorio(archivo.getSelectedFile().getParent());
            }
        }
        repaint();
    }

    public void addInicial(ArrayList<Arista> a) {
        ver = FLIPS;
        lote = new LoteFlips();
        lote.setInicial(a);
        mostrar = lote.getInicial();
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    void addAristas(ArrayList<Arista> aristas) {
        lote.addAristas(aristas);
    }

    void removeAristas(ArrayList<Arista> aristas) {
        lote.removeAristas(aristas);
    }

    public int getPos() {
        return lote.getPos();
    }

    void setPos(String text) {
        mostrar = lote.setPos(Integer.parseInt(text));
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    void endPos() {
        mostrar = lote.getUltimo();
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    void forwardPos() {
        mostrar = lote.getSiguiente();
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    public float getPeso() {
        return lote.getPeso();
    }

    public float getDilacion() {
        return dilacion;
    }

    void backwardPos() {
        mostrar = lote.getAnterior();
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    void resetPos() {
        mostrar = lote.getInicial();
        if (ver == POLIGONOS){
            lote.setPos(0);
        }
        dilacion = lote.getDilacion(puntos);
        repaint();
    }

    void addOutAristas(ArrayList<Arista> aristas) {
        lote.addOutAristas(aristas);
    }

    void addInAristas(ArrayList<Arista> aristas) {
        lote.addInAristas(aristas);
    }

    void addPoligono(ArrayList<Arista> aristas) {
        lote.addPoligono(aristas);
    }

    void mostarPoligono() {
        ver = POLIGONOS;
        mostrar = lote.getInicial();
        lote.setPos(0);
        dilacion = lote.getDilacion(puntos);
        repaint();
    }
    
    
    Conjunto getPuntos(){
        return puntos;
    }
    
    ArrayList<Arista> getAristas(){
        ArrayList<Arista> aristas = new ArrayList<Arista>();
        for (int i = 0; i < mostrar.size(); i++){
            aristas.add(new Arista(puntos.get(mostrar.get(i).getPosPo()), puntos.get(mostrar.get(i).getPosPd())));
        }
        return aristas;
    }

    void resetPoligonos() {
        if (lote != null){
            lote.resetPoligonos();
        }
    }
}
