/*
 * Created on 6 de septiembre de 2009, 21:53
 */
package visualizador;

/**
 * @author  Pablo
 */
import EPS.EpsGraphics2D;
import DCELPack.GenerarDCEL;
import TMA3SUM.TMA;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import poligonizacion.Convexo;
import poligonizacion.Simple;
import poligonizacion.Poligono;

public class JPanelDibujo extends JPanel implements MouseListener, MouseMotionListener {

    private final int DELAUNAY = 1;
    private final int POLSIMPLE = 1;
    private final int POLCONVEXO = 2;
    private float zoom = 1.0f;
    private boolean borrar = false;
    private Punto puntoElegido1;
    private int posPE1;
    private Punto puntoElegido2;
    private Punto puntoVoronoi; //Para pintar regiones voronoi
    private Triangulacion t = null;
    private Delaunay tDelaunay = null;
    private Configuracion tManual = new Configuracion();
    private Configuracion pta = null;
    private Conjunto l;
    private Conjunto lPol;
    private boolean voronoiActivo = false;
    private boolean fliparActivo = false;
    private int dilacionActivo = 0;
    private boolean xy = false;
    private boolean nro = true;
    private Voronoi v;
    //private double[] areas = new double[1000];
    private ArrayList dijkstra; //Aristas del camino mínimo
    private Punto puntoOrigen, puntoDestino;
    private int dibujar = 0;
    private int pseudoT = 0;
    private boolean agregarP = true;
    private boolean agregarA = false;
    private boolean borrarP = false;
    private boolean borrarA = false;
    private int mostrarPToT = 3;
    private boolean mostrarGraph = true;
    //private boolean mostrarPol = true;
    private boolean guardarPNG = false;
    private boolean guardarEPS = false;
    private boolean hayImagenFondo = false;
    private boolean hayImagenPunto = false;
    private String nombreArchivoG;
    private String archivoImagenFondo;
    private boolean tDelaunayActivo = true;
    String directorio;
    /*private static Properties properties = null;
    private static String propertyFileName = "visLotes/Directorio.txt";*/
    /*TMA*/
    ArrayList<Arista> tma = new ArrayList<Arista>();
    /*DIA DEL INVESTIGADOR*/
    Delaunay tDelaunayVoronoi = null;
    private Color color;
    PintarTriangulos pintarTri = new PintarTriangulos();
    private boolean pintar = false;
    /*DIA DEL INVESTIGADOR*/
    /*POLIGONIZAR*/
    private int poligonizar = 0;
    private Poligono pSim = null;
    private Poligono pCon = null;
    private boolean verPSim = false;
    private boolean verPCon = false;
    int[] polOpt = new int[3];
    /*FIN POLIGONIZAR*/
    /*PASO A PASO*/
    int pap = 0;
    /*FIN PASO A PASO*/
    private int widthInicial = 0;
    private int heightInicial = 0;
    private String archivoImagenPunto = null;
    /*DCEL*/
    private GenerarDCEL dcel;
    /*FIN DCEL*/
    private ConvexHull ch;
    /*Triangulación incremental con inclinación*/
    private int grado = 0;
    /*Triangulación incremental con inclinación*/
    /*TSP-*/
    //private TSP tsp;
    /*TSP*/
    private Color ptColor = Color.ORANGE;
    private Color tColor = Color.BLUE;
    private Color pColor = new Color(179, 179, 0);
    private Color mcolor = Color.GREEN;

    public void setTma(boolean ver) {
        if (ver) {
            if (l.size() > 2) {
                //this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                //this.setBackground(Color.gray);
                pensando(true);
                tma = (new TMA(l)).getResultado();
                pensando(false);
            } else {
                tma = new ArrayList<Arista>();
            }
        } else {
            tma = new ArrayList<Arista>();
        }
        //this.tma = tma;
        repaint();
    }

    public JPanelDibujo() {
        initComponents();
        addMouseListener(this);
        addMouseMotionListener(this);
        puntoElegido1 = null;
        puntoElegido2 = null;
        l = new Conjunto();
        lPol = new Conjunto();
        //areas[0] = 0;
        dijkstra = new ArrayList();
        /*properties = new Properties();
        try {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(propertyFileName);
        //JOptionPane.showMessageDialog(null,System.getProperty("user.dir"));
        
        if (is != null) {
        //JOptionPane.showMessageDialog(null, "leyó el archivo");
        }
        properties.load(is);
        directorio = new String();
        directorio = (String) properties.get("dir");
        } catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "No se pudo leer: " + ex.getMessage(), "Leer archivo", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
        JOptionPane.showMessageDialog(null, "No se pudo leer: " + ex.getMessage(), "Leer archivo", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Error en IO" + ex.getMessage());
        }*/
    }

    public Conjunto getL() {
        return l;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setPreferredSize(new java.awt.Dimension(600, 500));
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paint(Graphics h) {
        super.paint(h);
        Graphics2D h2d = (Graphics2D) h;
        double distancia = 0.0d;
        int noMostrarPeso = 1;
        BufferedImage imagen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = imagen.createGraphics();
        Graphics2D eps = new EpsGraphics2D();
        //eps.create(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        eps.setColor(Color.WHITE);
        //eps.fillRect(0, 0, this.getWidth(), this.getHeight());
        h2d.scale(zoom, zoom);
        g.scale(zoom, zoom);
        eps.scale(zoom, zoom);
        /*eps.setColor(Color.RED);
        eps.drawLine(0,0,100,100);
        String EPSCode = eps.toString();
        System.out.println(EPSCode);*/

        if (borrar) {
            borrar = false;
            puntoElegido1 = null;
            posPE1 = 0;
            puntoElegido2 = null;
            puntoVoronoi = null;
            t = null;//new Triangulacion();
            tDelaunay = null;
            tManual = new Configuracion();//.clear();// = new ArrayList();
            pta = null;//new PseudoTriangulacion();
            l.clear();// = new Conjunto();
            lPol.clear();
            voronoiActivo = false;
            fliparActivo = false;
            dilacionActivo = 0;
            xy = false;
            nro = true;
            v = null;
            dijkstra.clear();
            puntoOrigen = null;
            puntoDestino = null;
            dibujar = 0;
            pseudoT = 0;
            agregarP = true;
            agregarA = false;
            borrarP = borrarA = false;
            mostrarPToT = 3;
            mostrarGraph = true;
            guardarPNG = guardarEPS = hayImagenFondo = hayImagenPunto = false;
            archivoImagenFondo = nombreArchivoG = null;
            tDelaunayActivo = true;
            directorio = null;
            tma.clear();
            //tDelaunayVoronoi = null;
            color = null;
            pintarTri.init();
            pintar = false;
            poligonizar = 0;
            pSim = null;
            pCon = null;
            verPSim = false;
            verPCon = false;
            polOpt = new int[3];
            widthInicial = 0;
            heightInicial = 0;
            archivoImagenPunto = null;
            dcel = null;
            ch = null;
            grado = 0;
        } else {
            if (hayImagenFondo) {
                Image miImagen = (Toolkit.getDefaultToolkit()).getImage(archivoImagenFondo);
                h2d.drawImage(miImagen, (this.getWidth() - miImagen.getWidth(this)) / 2, (this.getHeight() - miImagen.getHeight(this)) / 2, this);
                g.drawImage(miImagen, (this.getWidth() - miImagen.getWidth(this)) / 2, (this.getHeight() - miImagen.getHeight(this)) / 2, this);
                //eps.drawImage(miImagen, (this.getWidth() - miImagen.getWidth(this)) / 2, (this.getHeight() - miImagen.getHeight(this)) / 2, this);
            }

            if (!l.isEmpty() || !lPol.isEmpty()) {

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(6);
                /*TSP
                if (tsp != null && pap < tsp.getSoluciones().size()) {
                ArrayList<ArrayList<Integer>> soluciones = tsp.getSoluciones();
                ArrayList<Integer> posiciones = soluciones.get(pap);
                int distancias = 0;
                for (int i = 0; i < posiciones.size(); i++) {
                dibujarArista(h2d, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                dibujarArista(g, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                dibujarArista(eps, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                distancias += l.get(posiciones.get(i)).distance(l.get(posiciones.get((i + 1) % posiciones.size())));
                }
                FV.panelSur1.setTextLabel(String.valueOf(distancias));
                }
                /*TSP*/

                /*Dia del investigador*/
                if (pintar) {
                    for (int i = 0; i < pintarTri.caras.size(); i++) {
                        h2d.setColor(pintarTri.colores.get(i));
                        h2d.fillPolygon(pintarTri.caras.get(i));
                        g.setColor(pintarTri.colores.get(i));
                        g.fillPolygon(pintarTri.caras.get(i));
                        eps.setColor(pintarTri.colores.get(i));
                        eps.fillPolygon(pintarTri.caras.get(i));
                    }
                }
                /*Dia del investigador*/

                FV.panelSur1.setTextCantP(String.valueOf(l.size()));
                if ((voronoiActivo) && (l.size() > 0)) {//DIAGRAMA DE VORONOI
                    v = new Voronoi();
                    v.calculaVoronoi(l);
                    ArrayList<WEdge> pol = new ArrayList<WEdge>();
                    for (Enumeration e = v.edgeDB.keys(); e.hasMoreElements();) {
                        WEdge vSig = (WEdge) e.nextElement();
                        WEdge w = (WEdge) v.edgeDB.get(vSig);
                        if (puntoVoronoi != null && (vSig.p1.igualQue(puntoVoronoi) || vSig.p2.igualQue(puntoVoronoi))) {
                            h2d.setColor(new Color(217, 255, 00));
                            h2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER,
                                    1.0f, new float[]{10.0f}, 0.0f));
                            h2d.drawLine((int) w.p1.getX(), -(int) w.p1.getY(), (int) w.p2.getX(), -(int) w.p2.getY());
                            pol.add(w);
                        } else {
                            h2d.setColor(new Color(217, 255, 00));
                            h2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER,
                                    1.0f, new float[]{10.0f}, 0.0f));
                            h2d.drawLine((int) w.p1.getX(), -(int) w.p1.getY(), (int) w.p2.getX(), -(int) w.p2.getY());
                        }

                        /*g.setColor(new Color(217, 255, 00));
                        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                1.0f, new float[]{10.0f}, 0.0f));*/
                        g.setColor(new Color(217, 255, 00));
                        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                1.0f, new float[]{10.0f}, 0.0f));
                        g.drawLine((int) w.p1.getX(), -(int) w.p1.getY(), (int) w.p2.getX(), -(int) w.p2.getY());
                        eps.setColor(new Color(217, 255, 00));
                        eps.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                1.0f, new float[]{10.0f}, 0.0f));
                        eps.drawLine((int) w.p1.getX(), -(int) w.p1.getY(), (int) w.p2.getX(), -(int) w.p2.getY());
                    }
                    /*DIA DEL INVESTIGADOR*/
                    if (puntoVoronoi != null) {
                        dibujarPoligono(h2d, pol);
                    }
                    /*DIA DEL INVESTIGADOR*/
                }

                /*POLIGONIZACIÓN*/
                if (verPCon && pCon != null) {//polígono convexo usa sus propios puntos
                    h2d.setColor(new Color(128, 128, 192));
                    h2d.setStroke(new BasicStroke(2));
                    g.setColor(new Color(128, 128, 192));
                    g.setStroke(new BasicStroke(2));
                    eps.setColor(new Color(128, 128, 192));
                    eps.setStroke(new BasicStroke(2));
                    ArrayList<WEdge> pol = new ArrayList<WEdge>();
                    for (int i = 0; i < pCon.size(); i++) {
                        dibujarArista(h2d, pCon.getArista(i), 0);
                        WEdge w = new WEdge(pCon.getArista(i).obtenerOrig(), pCon.getArista(i).obtenerDest());
                        pol.add(w);
                    }
                    dibujarPoligono(g, (ArrayList<WEdge>) pol.clone());
                    dibujarPoligono(eps, (ArrayList<WEdge>) pol.clone());
                    dibujarPoligono(h2d, pol);
                    if (nro) {
                        for (int i = 0; i < lPol.size(); i++) {
                            int lx = (int) lPol.get(i).getX();
                            int ly = (int) -lPol.get(i).getY();
                            String numPunto = "" + i;
                            //String coordPunto = "(" + lx + "," + ly + ")";
                            h2d.setColor(Color.DARK_GRAY);
                            h2d.draw(devolverOvalo(lPol.get(i), 7));
                            h2d.drawString(numPunto, (lx > 950) ? lx - 15 : lx + 5, (ly > 980) ? ly : ly + 15);
                            g.setColor(Color.DARK_GRAY);
                            g.draw(devolverOvalo(lPol.get(i), 7));
                            g.drawString(numPunto, lx + 10, ly + 5);
                            eps.setColor(Color.DARK_GRAY);
                            eps.draw(devolverOvalo(lPol.get(i), 7));
                            eps.drawString(numPunto, lx + 10, ly + 5);
                        }
                    }
                }
                if (verPSim && pSim != null) {
                    h2d.setColor(pColor);
                    h2d.setStroke(new BasicStroke(2));
                    g.setColor(pColor);
                    g.setStroke(new BasicStroke(2));
                    eps.setColor(pColor);
                    eps.setStroke(new BasicStroke(2));
                    ArrayList<WEdge> pol = new ArrayList<WEdge>();
                    for (int i = 0; i < pSim.size(); i++) {
                        dibujarArista(h2d, pSim.getArista(i), 0);
                        WEdge w = new WEdge(pSim.getArista(i).obtenerOrig(), pSim.getArista(i).obtenerDest());
                        pol.add(w);
                    }
                    dibujarPoligono(g, (ArrayList<WEdge>) pol.clone());
                    dibujarPoligono(eps, (ArrayList<WEdge>) pol.clone());
                    dibujarPoligono(h2d, pol);
                }
                /* FIN POLIGONIZACIÓN*/

                h2d.setColor(Color.blue);
                h2d.setStroke(new BasicStroke(2));//dia del investigador 2
                g.setColor(Color.blue);
                g.setStroke(new BasicStroke(2));
                eps.setColor(Color.blue);
                eps.setStroke(new BasicStroke(2));
                
                if (mostrarGraph && tManual.cantidadAristas() != 0){
                    if ((t != null && mostrarPToT > 1) || (pta != null && (mostrarPToT == 1 || (mostrarPToT == 3)))){
                        noMostrarPeso = 0;
                    }
                } else if ((t != null && pta != null) && mostrarPToT == 3){
                    noMostrarPeso = 0;
                }
                
                if (mostrarGraph && tManual != null) {
                    FV.panelSur1.setTextCantA(String.valueOf(tManual.cantidadAristas()));//size()));

                    h2d.setColor(mcolor);
                    g.setColor(mcolor);
                    eps.setColor(mcolor);

                    for (int i = 0; i < tManual.cantidadAristas(); i++) {//size(); i++) {
                        dibujarArista(h2d, tManual.getArista(i), 0);
                        dibujarArista(g, tManual.getArista(i), 0);
                        dibujarArista(eps, tManual.getArista(i), 0);
                        //distancia += tManual.getArista(i).obtenerOrig().distance(tManual.getArista(i).obtenerDest());
                        distancia += Math.pow((tManual.getArista(i).obtenerOrig().x - tManual.getArista(i).obtenerDest().x), 2) + Math.pow((tManual.getArista(i).obtenerOrig().y - tManual.getArista(i).obtenerDest().y), 2);
                    }
                    FV.panelSur1.setTextPeso(nf.format(distancia * noMostrarPeso));//String.valueOf(distancia));

                    if (puntoElegido2 != null) {
                        puntoElegido2 = null;
                        puntoElegido1 = null;
                    }

                }// else {
                if ((pseudoT > 0) && (mostrarPToT == 1 || mostrarPToT == 3)) {
                    h2d.setColor(ptColor);
                    g.setColor(ptColor);
                    eps.setColor(ptColor);
                    for (int i = 0; i < pta.cantidadAristas(); i++) {
                        dibujarArista(h2d, pta.getArista(i), 0);
                        dibujarArista(g, pta.getArista(i), 0);
                        dibujarArista(eps, pta.getArista(i), 0);
                        //distancia += pta.getArista(i).obtenerOrig().distance(pta.getArista(i).obtenerDest());
                        distancia += Math.pow((pta.getArista(i).obtenerOrig().x - pta.getArista(i).obtenerDest().x), 2) + Math.pow((pta.getArista(i).obtenerOrig().y - pta.getArista(i).obtenerDest().y), 2);
                    }
                    FV.panelSur1.setTextPeso(nf.format(distancia * noMostrarPeso));//(String.valueOf(distancia));
                    FV.panelSur1.setTextCantPT(String.valueOf(pta.cantPTriangulos()));
                    FV.panelSur1.setTextCantA(String.valueOf(pta.cantidadAristas()));
                }

                if (mostrarPToT == 2 || mostrarPToT == 3) {
                    switch (dibujar) {

                        case 1: {//Delaunay
                            int contarAristas = 0, contarADelaunay = 0;
                            for (int i = 6; i < t.obtenerNumAristas()/* && i < pap*/; i = i + 2) {
                                if (esInterna(t.obtenerEdge(i))) {
                                    contarAristas++;
                                    if ((fliparActivo) && (t.flip1Arista(t.obtenerEdge(i)))) {
                                        t.flip1Arista(t.obtenerEdge(i));
                                        h2d.setColor(Color.CYAN);
                                        g.setColor(Color.CYAN);
                                        eps.setColor(Color.CYAN);
                                    } else {
                                        Arista aristaAux = new Arista(t.obtenerEdge(i).obtenerDest(), t.obtenerEdge(i).obtenerTwin().obtenerDest());
                                        if (mostrarPToT == 3 && (pta != null) && pta.cantidadAristas() > 0 && pta.contieneArista(aristaAux)) {
                                            h2d.setColor(Color.LIGHT_GRAY);
                                            g.setColor(Color.LIGHT_GRAY);
                                            eps.setColor(Color.LIGHT_GRAY);
                                            contarADelaunay++;
                                        } else {
                                            h2d.setColor(tColor);
                                            g.setColor(tColor);
                                            eps.setColor(tColor);
                                        }
                                    }

                                    dibujarAristaD(h2d, t.obtenerEdge(i));
                                    dibujarAristaD(g, t.obtenerEdge(i));
                                    dibujarAristaD(eps, t.obtenerEdge(i));
                                    //distancia += t.obtenerEdge(i).obtenerDest().distance(t.obtenerEdge(i).obtenerTwin().obtenerDest());
                                    distancia += Math.pow((t.obtenerEdge(i).obtenerDest().x - t.obtenerEdge(i).obtenerTwin().obtenerDest().x), 2) + Math.pow((t.obtenerEdge(i).obtenerDest().y - t.obtenerEdge(i).obtenerTwin().obtenerDest().y), 2);

                                }
                            }
                            FV.panelSur1.setTextPeso(nf.format(distancia * noMostrarPeso));//(String.valueOf(distancia));
                            if (pta != null) {
                                FV.panelSur1.setTextCantA(String.valueOf(contarAristas + pta.cantidadAristas() - contarADelaunay));
                            } else {
                                FV.panelSur1.setTextCantA(String.valueOf(contarAristas));
                            }
                        }
                        break;
                        case 2:
                        case 3:
                        case 4:
                        case 5: {//Otras triangulaciones
                            int contarADelaunay = 0;
                            for (int i = 0; i < t.obtenerNumAristas() /*&& i < pap*/; i++) {
                                h2d.setColor(tColor);
                                g.setColor(tColor);
                                eps.setColor(tColor);
                                if ((fliparActivo) && (t.esFlipeable(t.obtenerEdge(i), i))) {
                                    if (tDelaunay == null) {
                                        setFlipar(true);
                                    }
                                    if (tDelaunay.contieneArista(t.obtenerEdge(i)) && tDelaunayActivo) {
                                        h2d.setColor(Color.BLACK);
                                        g.setColor(Color.BLACK);
                                        eps.setColor(Color.BLACK);
                                    } else {
                                        h2d.setColor(Color.CYAN);
                                        g.setColor(Color.CYAN);
                                        eps.setColor(Color.CYAN);
                                    }
                                } else if (mostrarPToT == 3 && pta != null && pta.cantidadAristas() > 0) {
                                    if (t instanceof Incremental) {
                                        if (pta.contieneArista(t.obtenerRotatedEdge(i))) {
                                            h2d.setColor(Color.LIGHT_GRAY);
                                            g.setColor(Color.LIGHT_GRAY);
                                            eps.setColor(Color.LIGHT_GRAY);
                                        }
                                    } else {
                                        if (pta.contieneArista(t.obtenerEdge(i))) {
                                            h2d.setColor(Color.LIGHT_GRAY);
                                            g.setColor(Color.LIGHT_GRAY);
                                            eps.setColor(Color.LIGHT_GRAY);
                                        }
                                    }

                                } else {
                                    h2d.setColor(tColor);
                                    g.setColor(tColor);
                                    eps.setColor(tColor);
                                }

                                dibujarArista(h2d, t.obtenerEdge(i), t.getGrado());
                                dibujarArista(g, t.obtenerEdge(i), t.getGrado());
                                dibujarArista(eps, t.obtenerEdge(i), t.getGrado());
                                //distancia += t.obtenerEdge(i).obtenerOrig().distance(t.obtenerEdge(i).obtenerDest());
                                distancia += Math.pow((t.obtenerEdge(i).obtenerOrig().x - t.obtenerEdge(i).obtenerDest().x), 2) + Math.pow((t.obtenerEdge(i).obtenerOrig().y - t.obtenerEdge(i).obtenerDest().y), 2);
                            }
                            FV.panelSur1.setTextPeso(nf.format(distancia * noMostrarPeso));//(String.valueOf(distancia));
                            if (pta != null) {
                                FV.panelSur1.setTextCantA(String.valueOf(t.obtenerNumAristas() + pta.cantidadAristas() - contarADelaunay));
                            } else {
                                FV.panelSur1.setTextCantA(String.valueOf(t.obtenerNumAristas()));
                            }
                        }
                        break;
                    }
                }
                //}

                if (!fliparActivo) {
                    if (dilacionActivo > 0) {
                        distancia = 0.0d;
                        h2d.setStroke(new BasicStroke(4));
                        h2d.setColor(Color.RED);
                        g.setStroke(new BasicStroke(4));
                        g.setColor(Color.RED);
                        eps.setStroke(new BasicStroke(4));
                        eps.setColor(Color.RED);
                        /*if (((mostrarPToT == 1 && pta != null) || (mostrarPToT == 2 && t != null)) ^ 
                        (mostrarPToT == 3 && (t != null ^ pta != null)) ^ 
                        ((mostrarGraph && tManual.cantidadAristas() > 0))){
                        /*if (mostrarPToT == 2 && t != null){
                        calcularDilacion(t);
                        } else if (mostrarPToT == 1 && pta != null){
                        calcularDilacion(pta);
                        } else if (mostrarPToT == 3 && (t != null ^ pta != null)){
                        if (t != null){
                        calcularDilacion(t);
                        } else {
                        calcularDilacion(pta);
                        }
                        } else if (tManual.cantidadAristas() > 0){
                        calcularDilacion(tManual);
                        }*/

                        if (dijkstra.size() > 0) {
                            for (int i = 0; i < dijkstra.size(); i++) {
                                if (t instanceof Incremental) {
                                    dibujarArista(h2d, (Arista) dijkstra.get(i), t.getGrado());
                                    dibujarArista(g, (Arista) dijkstra.get(i), t.getGrado());//ojo con el grado
                                    dibujarArista(eps, (Arista) dijkstra.get(i), t.getGrado());
                                } else {
                                    dibujarArista(h2d, (Arista) dijkstra.get(i), 0);
                                    dibujarArista(g, (Arista) dijkstra.get(i), 0);//ojo con el grado
                                    dibujarArista(eps, (Arista) dijkstra.get(i), 0);
                                }
                                distancia += ((Arista) dijkstra.get(i)).obtenerOrig().distance(((Arista) dijkstra.get(i)).obtenerDest());
                            }
                            if (dijkstra.size() > 0) {
                                FV.panelSur1.setTextDilacion(nf.format(distancia / puntoOrigen.distance(puntoDestino)));//(String.valueOf(Math.rint((distancia / puntoOrigen.distance(puntoDestino)) * 100000000.0d) / 100000000.0));
                            }
                        }
                    }
                }

                /*TMA*/
                h2d.setStroke(new BasicStroke(2));
                g.setStroke(new BasicStroke(2));
                eps.setStroke(new BasicStroke(2));
                h2d.setColor(Color.MAGENTA);
                g.setColor(Color.MAGENTA);
                eps.setColor(Color.MAGENTA);
                for (int i = 0; i < tma.size(); i++) {
                    dibujarArista(h2d, tma.get(i), 0);
                    dibujarArista(g, tma.get(i), 0);
                    dibujarArista(eps, tma.get(i), 0);
                }
                /*FIN TMA*/

                /*CONVEX HULL*/
                h2d.setColor(new Color(128, 0, 255));
                h2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        3.0f, new float[]{15.0f}, 0.0f));
                g.setColor(new Color(128, 0, 255));
                g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        3.0f, new float[]{15.0f}, 0.0f));
                eps.setColor(new Color(128, 0, 255));
                eps.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        3.0f, new float[]{15.0f}, 0.0f));
                if (ch != null) {
                    ArrayList<Integer> posiciones = ch.getCierre();
                    for (int i = 0; i < posiciones.size(); i++) {
                        dibujarArista(h2d, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                        dibujarArista(g, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                        dibujarArista(eps, new Arista(l.get(posiciones.get(i)), l.get(posiciones.get((i + 1) % posiciones.size()))), 0);
                    }
                }
                /*FIN CONVEX HULL*/

                h2d.setStroke(new BasicStroke(2));
                g.setStroke(new BasicStroke(2));
                eps.setStroke(new BasicStroke(2));

                if (nro) {
                    for (int i = 0; i < l.size(); i++) {
                        int lx = (int) l.get(i).getX();
                        int ly = (int) -l.get(i).getY();
                        String numPunto = "" + i;
                        String coordPunto = "(" + lx + "," + ly + ")";
                        /*DIA DEL INVESTIGADOR*/
                        if (hayImagenPunto && archivoImagenPunto != null) {
                            Image miImagen = (Toolkit.getDefaultToolkit()).getImage(archivoImagenPunto);
                            h2d.drawImage(miImagen, (lx - miImagen.getWidth(this) / 2), (ly - miImagen.getHeight(this) / 2), this);
                            g.drawImage(miImagen, (lx - miImagen.getWidth(this) / 2), (ly - miImagen.getHeight(this) / 2), this);
                            //eps.drawImage(miImagen, (lx - miImagen.getWidth(this) / 2), (ly - miImagen.getHeight(this) / 2), this);
                            /*DIA DEL INVESTIGADOR*/
                        } else {
                            if ((xy) || ((puntoElegido1 != null) && (puntoElegido1.igualQue(l.get(i))))) {
                                numPunto += coordPunto;
                                h2d.setPaint(Color.red);
                                h2d.draw(devolverOvalo(l.get(i), 10));
                                //h2d.drawString(numPunto, lx + 10, ly);
                                g.setPaint(Color.red);
                                g.draw(devolverOvalo(l.get(i), 10));
                                //g.drawString(numPunto, lx + 10, ly);
                                eps.setPaint(Color.red);
                                eps.draw(devolverOvalo(l.get(i), 10));
                                //g.drawString(numPunto, lx + 10, ly);
                                if (lx > 950) {
                                    lx -= 60;
                                }
                            } else {
                                h2d.setPaint(Color.black);
                                g.setPaint(Color.black);
                                eps.setPaint(Color.black);
                            }
                            h2d.fill(devolverOvalo(l.get(i), 7));
                            g.fill(devolverOvalo(l.get(i), 7));
                            eps.fill(devolverOvalo(l.get(i), 7));

                            h2d.drawString(numPunto, (lx > 950) ? lx - 15 : lx + 5, (ly > 980) ? ly : ly + 15);
                            g.drawString(numPunto, lx + 10, ly + 5);
                            eps.drawString(numPunto, lx + 10, ly + 5);
                        }
                    }
                }
                
                /*if (mostrarPToT == 3 && pta != null && dibujar > 0){
                    FV.panelSur1.setTextPeso("0");
                }*/
                if (guardarPNG) {
                    try {
                        guardarPNG = false;
                        ImageIO.write(imagen, "png", new File(nombreArchivoG));
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
        }
    }

    private void dibujarPoligono(Graphics2D h2d, ArrayList<WEdge> aristas) {
        Polygon region = new Polygon();
        Punto pu = new Punto();
        Punto pExt = null;
        if (aristas.size() > 0) {
            for (int j = 0; j < aristas.size(); j++) {
                if (!esInterna(aristas.get(j).p1) && (!esInterna(aristas.get(j).p2))) {
                    aristas.remove(j);
                    j--;
                }
            }
            pu = new Punto(aristas.get(0).p1.getX(), -aristas.get(0).p1.getY());
            region.addPoint((int) pu.getX(), (int) -pu.getY());
            if (!esInterna(pu)) {
                pExt = new Punto(pu.getX(), -pu.getY());
            }
            pu = new Punto(aristas.get(0).p2.getX(), -aristas.get(0).p2.getY());
            region.addPoint((int) pu.getX(), (int) -pu.getY());
            /*if (!esInterna(pu)) {
            pExt = new Punto(pu.getX(), -pu.getY());
            }*/
        }

        while (aristas.size() > 1) {
            for (int j = 1; j < aristas.size(); j++) {
                if (aristas.get(j).p1.igualQue(pu)) {
                    pu = new Punto(aristas.get(j).p2.getX(), -aristas.get(j).p2.getY());
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    aristas.remove(j);
                    j = aristas.size();
                } else if (aristas.get(j).p2.igualQue(pu)) {
                    pu = new Punto(aristas.get(j).p1.getX(), -aristas.get(j).p1.getY());
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    aristas.remove(j);
                    j = aristas.size();
                } else if (!esInterna(pu) && !esInterna(aristas.get(j).p1)) {
                    region.addPoint((int) aristas.get(j).p1.getX(), (int) -aristas.get(j).p1.getY());
                    pu = new Punto(aristas.get(j).p2.getX(), -aristas.get(j).p2.getY());
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    aristas.remove(j);
                    j = aristas.size();
                } else if (!esInterna(pu) && !esInterna(aristas.get(j).p2)) {
                    region.addPoint((int) aristas.get(j).p2.getX(), (int) -aristas.get(j).p2.getY());
                    pu = new Punto(aristas.get(j).p1.getX(), -aristas.get(j).p1.getY());
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    aristas.remove(j);
                    j = aristas.size();
                } else if (!esInterna(pu) && pExt != null) {
                    pu = new Punto(pExt.getX(), -pExt.getY());
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    aristas.remove(j);
                    j = aristas.size();
                } else if (!esInterna(pu) && pExt != null) {
                }
                if (j == aristas.size() - 1) {
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                    pu = pExt;
                    region.addPoint((int) pu.getX(), (int) -pu.getY());
                }
            }
        }
        h2d.setComposite(makeComposite(0.5F));
        h2d.fillPolygon(region);
        h2d.setComposite(makeComposite(1.0F));
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    private void dibujarArista(Graphics2D h2d, Arista a, double grado) {
        h2d.drawLine((int) a.obtenerOrig().contraRotado(grado).getX(),
                (int) -a.obtenerOrig().contraRotado(grado).getY(),
                (int) a.obtenerDest().contraRotado(grado).getX(),
                (int) -a.obtenerDest().contraRotado(grado).getY());
    }

    private void dibujarAristaD(Graphics2D h2d, Arista a) {
        h2d.drawLine((int) a.obtenerDest().getX(),
                (int) -a.obtenerDest().getY(),
                (int) a.obtenerTwin().obtenerDest().getX(),
                (int) -a.obtenerTwin().obtenerDest().getY());
    }

    private boolean esInterna(Arista a) {
        return !((int) a.obtenerDest().getX() < -100 || (int) a.obtenerDest().getX() > this.getWidth() * 4
                || (int) a.obtenerTwin().obtenerDest().getX() < -100 || (int) a.obtenerTwin().obtenerDest().getX() > this.getWidth() * 4
                || (int) -a.obtenerDest().getY() < -100 || (int) -a.obtenerDest().getY() > this.getHeight() * 4
                || (int) -a.obtenerTwin().obtenerDest().getY() < -100 || (int) -a.obtenerTwin().obtenerDest().getY() > this.getHeight() * 4);
    }

    private boolean esInterna(WEdge e) {
        return !((int) e.p1.getX() < 0 || (int) e.p1.getX() > this.getWidth()
                || (int) e.p2.getX() < 0 || (int) e.p2.getX() > this.getWidth()
                || (int) -e.p1.getY() < 0 || (int) -e.p1.getY() > this.getHeight()
                || (int) -e.p2.getY() < 0 || (int) -e.p2.getY() > this.getHeight());
    }

    private boolean esInterna(Punto p) {
        /*return !((int) p.getX() < 0 || (int) p.getX() > this.getWidth()
        || (int) -p.getY() < 0 || (int) -p.getY() > this.getHeight());*///Cambio porque no se veían las partes inferiores del polígono en zoom pequeño
        return !((int) p.getX() < -100 || (int) p.getX() > this.getWidth() * 4
                || (int) -p.getY() < -100 || (int) -p.getY() > this.getHeight() * 4);
    }

    public void triangular() {
        int conttri = 0;
        try {
            pensando(true);
            switch (dibujar) {
                case 1:
                    t = new Delaunay();
                    break;
                case 2:
                    t = new Divide();
                    break;
                case 3:
                    t = new Abanico();
                    //t = new RandomT();
                    break;
                case 4:
                    t = new Incremental();
                    t.setGrado(grado);
                    break;
                case 5:
                    t = new Greedy();
                    break;
            }

            t.inicializar();
            if (dibujar == 5) {
                t.calculaGreedyT(l);
            } else {
                while (conttri < (l.size())) {
                    t.insertarPunto(l.get(conttri));
                    conttri++;
                }
            }

            if (dilacionActivo != 0) {
                calcularDilacion();
            }

            //pap = t.obtenerNumAristas();

        } finally {
            pensando(false);
            repaint();
        }

    }

    public void pseudoTriangular() {
        if (pseudoT > 0) {
            if (l.size() > 250) {
                JOptionPane.showMessageDialog(null, "Too many points", "Pseudotriangulation", JOptionPane.ERROR_MESSAGE);
                //pta = null;
                repaint();
            } else {
                try {
                    pensando(true);
                    switch (pseudoT) {
                        case 1:
                            pta = new PTAleatoria();
                            break;
                        case 2:
                            pta = new PTGreedy();
                            break;
                        case 3:
                            pta = new PTDistMax();
                    }
                    pta.inicializar(l);
                    FV.panelSur1.habilitarCantPT(true);

                    //guardarEnLote(true, pta);
                    if (dilacionActivo != 0) {
                        calcularDilacion();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Pseudotriangulation", JOptionPane.ERROR_MESSAGE);
                } finally {
                    pensando(false);
                }

                repaint();
            }
        }
    }

    public void esPTr() {
        if (l.size() > 0 && tManual.cantidadAristas() > 0) {
            try {
                pensando(true);
                pta = new PseudoManual();
                switch (pta.inicializar(l, (ArrayList<Arista>) tManual.getAristas().clone())) {//(ArrayList<Arista>) tManual.clone())) {
                    case 1:
                        JOptionPane.showMessageDialog(null, "The Pseudotriangulation is correct", "¿Pseudotriangulation?", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 0:
                        JOptionPane.showMessageDialog(null, "The Pseudotriangulation isn't correct", "¿Pseudotriangulation?", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -1:
                        JOptionPane.showMessageDialog(null, "Error in convex hull", "¿Pseudotriangulation?", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Too many points", "Pseudotriangulation", JOptionPane.ERROR_MESSAGE);
            } finally {
                pensando(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must perform a manual Polygon to consult for Pseudotriangulation", "¿Pseudotriangulation?", JOptionPane.WARNING_MESSAGE);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent evt) {
        mousePresionado(evt);
        /*Punto raton = new Punto(evt.getX() / zoom, -evt.getY() / zoom);
        puntoElegido2 = null;
        int i;
        FV.panelSur1.habilitarT(true);
        /*dia del investigador*
        if (pintar) {
        Polygon zona = new Polygon();
        if (dibujar == 1) {
        Delaunay d = (Delaunay) t;
        Arista ar = d.enQueTriangulo(new Punto((int) raton.getX(), (int) raton.getY()));
        zona.addPoint((int) ar.obtenerDest().getX(), -(int) ar.obtenerDest().getY());
        zona.addPoint((int) ar.obtenerTwin().obtenerDest().getX(), -(int) ar.obtenerTwin().obtenerDest().getY());
        zona.addPoint((int) ar.obtenerSig().obtenerDest().getX(), -(int) ar.obtenerSig().obtenerDest().getY());
        } else {
        for (int j = 0; j < dcel.getDcel().getFaceList().size() - 1; j++) {
        Triangulo triang = dcel.getDcel().getFace(j);
        if (!t.EsTrianguloVacio(triang, new Punto((int) raton.getX(), (int) raton.getY()))) {
        zona.addPoint((int) triang.getVerticeA().getX(), (int) -triang.getVerticeA().getY());
        zona.addPoint((int) triang.getVerticeB().getX(), (int) -triang.getVerticeB().getY());
        zona.addPoint((int) triang.getVerticeC().getX(), (int) -triang.getVerticeC().getY());
        }
        }
        }
        colores.add(color);
        caras.add(zona);/*dia del investigador*
        }  else if ((dibujar == 0) && (pseudoT == 0) && (poligonizar == 0)) {//CAMBIÉ || POR &&
        if (!borrarA) {
        for (i = 0; i < l.size(); i++) {
        Shape s = devolverOvalo(l.get(i), 10);
        if (s.contains(raton)) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borrarP) {
        for (int k = 0; k < tManual.cantidadAristas(); k++) {
        if (tManual.getArista(k).obtenerOrig().igualQue(l.get(i)) || tManual.getArista(k).obtenerDest().igualQue(l.get(i))) {
        tManual.removeArista(k);
        k--;
        calcularDilacion(tManual);
        }
        }
        l.remove(l.get(i));
        repaint();
        } else {
        if (puntoElegido1 != null && agregarA) {//TRIANGULACIÓN MANUAL
        puntoElegido2 = puntoElegido1;
        puntoElegido1 = l.get(i);
        posPE1 = i;
        if (puntoElegido1 != puntoElegido2) {
        Punto[] pu = new Punto[2];
        pu[0] = puntoElegido1;
        pu[1] = puntoElegido2;
        Arista ari = new Arista(puntoElegido1, puntoElegido2);
        if (!cortaAristas(tManual.getAristas(), ari) && !contieneArista(ari)) {
        tManual.addArista(ari);
        calcularDilacion(tManual);
        repaint();
        } else {
        JOptionPane.showMessageDialog(null, "No se puede agregar esa Arista", "Error", JOptionPane.ERROR_MESSAGE);
        puntoElegido1 = puntoElegido2;
        }
        } else {
        repaint();
        }
        } else {
        puntoElegido1 = l.get(i);
        posPE1 = i;
        repaint();
        }
        i = l.size() + 1; //PARA SABER QUE ENCONTRÓ EL PUNTO
        }
        }
        }
        if (i == l.size()) {//NO SE SELECCIONÓ NINGÚN PUNTO
        if (agregarP) {
        Punto pu = new Punto((int) raton.getX(), (int) raton.getY());
        l.add(pu); //agrega el punto a la lista
        }
        puntoElegido1 = null;
        puntoElegido2 = null;
        repaint();
        }
        } else {
        for (int j = 0; j < tManual.cantidadAristas(); j++) {
        ArrayList r = devolverRombo((int) raton.getX(), (int) -raton.getY());
        if (cortaAristas(r, tManual.getArista(j))) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tManual.removeArista(j);
        calcularDilacion(tManual);
        repaint();
        break;
        } else {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        }
        }
        } else if (dibujar > 0) {//TRIANGULACIONES <=5
        if (fliparActivo) {
        if (dibujar == 1) {//Delaunay
        for (int j = 6; j < t.obtenerNumAristas(); j = j + 2) {
        if (esInterna(t.obtenerEdge(j))) {
        Punto pCentral = reducirD(t.obtenerEdge(j));
        Shape s = devolverOvalo(pCentral, 20);
        if (s.contains(raton)) {
        
        Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(mouse);
        if (!t.flip1Arista(t.obtenerEdge(j))) {
        JOptionPane.showMessageDialog(null, "La arista no se puede flipar", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
        repaint();
        }
        break;
        } else {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        }
        }
        } else {
        for (int j = 0; j < t.obtenerNumAristas(); j++) {
        Punto pCentral = reducir(t.obtenerEdge(j));
        Shape s = devolverOvalo(pCentral, 20);
        if (s.contains(raton)) {
        Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(mouse);
        if (!t.flip1Arista(t.obtenerEdge(j), j)) {
        JOptionPane.showMessageDialog(null, "La arista no se puede flipar", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
        repaint();
        }
        break;
        } else {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        }
        }
        } else {
        for (i = 0; i < l.size(); i++) {
        Shape s = devolverOvalo(l.get(i), 10);
        if (s.contains(raton)) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borrarP) {
        l.remove(l.get(i));
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        i = l.size() + 1;
        } else {
        puntoElegido1 = l.get(i);
        posPE1 = i;
        i = l.size() + 1;
        }
        }
        }
        if (i == l.size()) {//NO SE SELECCIONÓ NINGÚN PUNTO
        puntoElegido1 = null;
        puntoElegido2 = null;
        if (agregarP) {
        Punto pu = new Punto((int) raton.getX(), (int) raton.getY());
        l.add(pu);
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        } else {
        repaint();
        }
        }
        }
        } else if (pseudoT > 0) {
        for (i = 0; i < l.size(); i++) {
        Shape s = devolverOvalo(l.get(i), 10);
        if (s.contains(raton)) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borrarP) {
        l.remove(l.get(i));
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        i = l.size() + 1;
        } else {
        puntoElegido1 = l.get(i);
        posPE1 = i;
        i = l.size() + 1;
        }
        }
        }
        if (i == l.size()) {//NO SE SELECCIONÓ NINGÚN PUNTO
        puntoElegido1 = null;
        puntoElegido2 = null;
        if (agregarP) {
        Punto pu = new Punto((int) raton.getX(), (int) raton.getY());//Antes usaba puntoElegido1, VER si hay que hacer igual en Triangulación
        l.add(pu);//Tratar de usar puntoElegido1 para que quede seleccionado. Cuidado con mDragged
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        } else {
        repaint();
        }
        }
        } else if (poligonizar > 0) {
        for (i = 0; i < l.size(); i++) {
        Shape s = devolverOvalo(l.get(i), 10);
        if (s.contains(raton)) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borrarP) {
        l.remove(l.get(i));
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        i = l.size() + 1;
        } else {
        puntoElegido1 = l.get(i);
        posPE1 = i;
        i = l.size() + 1;
        }
        }
        }
        if (i == l.size()) {//NO SE SELECCIONÓ NINGÚN PUNTO
        puntoElegido1 = null;
        puntoElegido2 = null;
        if (agregarP) {
        Punto pu = new Punto((int) raton.getX(), (int) raton.getY());//Antes usaba puntoElegido1, VER si hay que hacer igual en Triangulación
        l.add(pu);//Tratar de usar puntoElegido1 para que quede seleccionado. Cuidado con mDragged
        if (t != null) {
        triangular();
        }
        if (pta != null) {
        pseudoTriangular();
        }
        if (p != null) {
        calcularPoligono();
        }
        if (ch != null) {
        ch = new ConvexHull(l);
        }
        } else {
        repaint();
        }
        }
        }*/
    }

    private void mousePresionado(MouseEvent evt) {
        Punto raton = new Punto(evt.getX() / zoom, -evt.getY() / zoom);
        puntoElegido2 = null;
        int i;
        FV.panelSur1.habilitarT(true);
        /*dia del investigador*/
        if (pintar) {
            Polygon zona = new Polygon();
            if (dibujar == DELAUNAY) {
                Delaunay d = (Delaunay) t;
                Arista ar = d.enQueTriangulo(new Punto((int) raton.getX(), (int) raton.getY()));
                if (esInterna(ar)) {
                    zona.addPoint((int) ar.obtenerDest().getX(), -(int) ar.obtenerDest().getY());
                    zona.addPoint((int) ar.obtenerTwin().obtenerDest().getX(), -(int) ar.obtenerTwin().obtenerDest().getY());
                    zona.addPoint((int) ar.obtenerSig().obtenerDest().getX(), -(int) ar.obtenerSig().obtenerDest().getY());
                }
            } else {
                for (int j = 0; j < dcel.getDcel().getFaceList().size() - 1; j++) {
                    Triangulo triang = dcel.getDcel().getFace(j);
                    if (!t.EsTrianguloVacio(triang, new Punto((int) raton.getX(), (int) raton.getY()))) {
                        zona.addPoint((int) triang.getVerticeA().getX(), (int) -triang.getVerticeA().getY());
                        zona.addPoint((int) triang.getVerticeB().getX(), (int) -triang.getVerticeB().getY());
                        zona.addPoint((int) triang.getVerticeC().getX(), (int) -triang.getVerticeC().getY());
                    }
                }
            }
            pintarTri.colores.add(color);
            pintarTri.caras.add(zona);/*dia del investigador*/
            pintarTri.puntos.add(raton);
        } else if (borrarA) {//else if ((dibujar == 0) && (pseudoT == 0) && (poligonizar == 0)) {//CAMBIÉ || POR &&
            for (int j = 0; j < tManual.cantidadAristas(); j++) {
                ArrayList r = devolverRombo((int) raton.getX(), (int) raton.getY());
                if (cortaAristas(r, tManual.getArista(j))) {
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    tManual.removeArista(j);
                    //calcularDilacion(tManual);
                    repaint();
                    break;
                } else {
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        } else {
            for (i = 0; i < l.size(); i++) {
                Shape s = devolverOvalo(l.get(i), 10);
                if (s.contains(raton)) {
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    if (borrarP) {
                        for (int k = 0; k < tManual.cantidadAristas(); k++) {
                            if (tManual.getArista(k).obtenerOrig().igualQue(l.get(i)) || tManual.getArista(k).obtenerDest().igualQue(l.get(i))) {
                                tManual.removeArista(k);
                                k--;
                                //calcularDilacion(tManual);
                            }
                        }
                        l.remove(l.get(i));
                        queSeEstaMostrando(borrarP);
                    } else {
                        if (puntoElegido1 != null && agregarA) {//TRIANGULACIÓN MANUAL
                            puntoElegido2 = puntoElegido1;
                            puntoElegido1 = l.get(i);
                            posPE1 = i;
                            if (puntoElegido1 != puntoElegido2) {
                                Punto[] pu = new Punto[2];
                                pu[0] = puntoElegido1;
                                pu[1] = puntoElegido2;
                                Arista ari = new Arista(puntoElegido1, puntoElegido2);
                                if (!cortaAristas(tManual.getAristas(), ari) && !contieneArista(ari)) {
                                    tManual.addArista(ari);
                                    //calcularDilacion(tManual);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Can't add this Edge", "Error", JOptionPane.ERROR_MESSAGE);
                                    puntoElegido1 = puntoElegido2;
                                }
                            }
                        } else {
                            puntoElegido1 = l.get(i);
                            posPE1 = i;
                        }
                        i = l.size() + 1; //PARA SABER QUE ENCONTRÓ EL PUNTO
                    }
                }
            }
            if (i == l.size()) {//NO SE SELECCIONÓ NINGÚN PUNTO
                if (agregarP) {
                    Punto pu = new Punto((int) raton.getX(), (int) raton.getY());
                    pu.posicion = l.size();
                    l.add(pu); //agrega el punto a la lista
                }
                puntoElegido1 = null;
                puntoElegido2 = null;
            }
        }
        if (fliparActivo) {
            if (dibujar == DELAUNAY) {//Delaunay
                for (int j = 6; j < t.obtenerNumAristas(); j = j + 2) {
                    if (esInterna(t.obtenerEdge(j))) {
                        Punto pCentral = reducirD(t.obtenerEdge(j));
                        Shape s = devolverOvalo(pCentral, 20);
                        if (s.contains(raton)) {
                            //guardarEnLote(false, t.obtenerEdge(j));
                            Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
                            this.setCursor(mouse);
                            if (!t.flip1Arista(t.obtenerEdge(j))) {
                                JOptionPane.showMessageDialog(null, "Can't flip this Edge", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                //guardarEnLote(true, t.obtenerEdge(j));
                                repaint();
                            }
                            break;
                        } else {
                            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
            } else {
                for (int j = 0; j < t.obtenerNumAristas(); j++) {
                    Punto pCentral = reducir(t.obtenerEdge(j));
                    Shape s = devolverOvalo(pCentral, 20);
                    if (s.contains(raton)) {
                        Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
                        this.setCursor(mouse);
                        if (!t.flip1Arista(t.obtenerEdge(j), j)) {
                            JOptionPane.showMessageDialog(null, "Can't flip this Edge", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            repaint();
                        }
                        break;
                    } else {
                        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
        queSeEstaMostrando(agregarP);
        repaint();
    }

    private void queSeEstaMostrando(boolean aP) {
        if (t != null && !fliparActivo) {//***VER
            triangular();
        }
        if (pta != null) {
            pseudoTriangular();
        }
        if (tManual instanceof RandomT && aP){
            triangulacionAleatoria();
        }
        if (pSim != null && aP && poligonizar != POLCONVEXO) {
            calcularPoligono();
        }
        if (ch != null) {
            ch = new ConvexHull(l);
        }
        if (dilacionActivo > 0) {
            setDilacion(dilacionActivo);
        }
        if (tma.size() > 0) {
            setTma(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseDragged(MouseEvent evt) {
        Punto raton = new Punto(evt.getX() / zoom, -evt.getY() / zoom);
        if (!fliparActivo/* && poligonizar != POLCONVEXO*/) {
            if (puntoElegido1 != null) {
                puntoElegido1.setLocation(raton);
                l.get(posPE1).setLocation(raton);
                if ((dibujar > 0) || (pseudoT > 0) || (poligonizar > 0) || (tManual instanceof RandomT)) {
                    if (dibujar > 0) {
                        triangular();
                    }
                    if (pseudoT > 0) {
                        pseudoTriangular();
                    }
                    if (poligonizar == POLSIMPLE) {
                        calcularPoligono();
                    }
                    if (tManual instanceof RandomT){
                        triangulacionAleatoria();
                    }
                    /*} else if (tManual.cantidadAristas() > 0) {
                    calcularDilacion(tManual);*/
                } else {
                    repaint();
                }
            }
        } /*else if (((dibujar == 0) || (pseudoT == 0)) && (!agregarA) && (!agregarP)) {
        if (puntoElegido1 != null) {
        puntoElegido1.setLocation(raton);
        l.get(posPE1).setLocation(raton);
        repaint();
        }
        }*/
        if (tma.size() > 0) {
            setTma(true);
        }
        if (dilacionActivo > 0) {
            setDilacion(dilacionActivo);
        }
    }

    public void mouseMoved(MouseEvent evt) {
        Punto raton = new Punto(evt.getX() / zoom, -evt.getY() / zoom);
        /*Dia investigador
        if (voronoiActivo) {
            Punto puntero = new Punto(evt.getX(), evt.getY());
            double distMinima = Integer.MAX_VALUE;
            int posMinimo = Integer.MAX_VALUE;
            for (int i = 0; i < l.size(); i++) {
                Arista dist = new Arista(puntero, l.get(i));
                if (dist.longitud() < distMinima) {
                    distMinima = dist.longitud();
                    posMinimo = i;
                }
            }
            puntoVoronoi = l.get(posMinimo);
            repaint();
        }
        /*if (tDelaunayVoronoi != null){
        Arista triangulo = tDelaunayVoronoi.enQueTriangulo(puntero);
        
        Arista distancia1 = new Arista(puntero, triangulo.obtenerDest());
        
        Arista distancia2 = new Arista (puntero, triangulo.obtenerTwin().obtenerDest());
        Arista distancia3 = new Arista (puntero, triangulo.obtenerSig().obtenerDest());
        
        if (distancia1.longitud() > distancia2.longitud()){
        if (distancia2.longitud() > distancia3.longitud()){
        puntoElegido1 = distancia3.obtenerDest();
        } else {
        puntoElegido1 = distancia2.obtenerDest();
        }
        } else if (distancia1.longitud() < distancia3.longitud()){
        puntoElegido1 = distancia1.obtenerDest();
        } else{
        puntoElegido1 = distancia3.obtenerDest();
        }
        
        }*/
        /*Dia investigador*/

        FV.panelSur1.setTextPosx(String.valueOf(raton.getX()));
        FV.panelSur1.setTextPosy(String.valueOf(raton.getY()));
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (!pintar) {
            for (int i = 0; i < l.size(); i++) {
                Shape s = devolverOvalo(l.get(i), 10);
                if (s.contains(raton)) {
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        }
        if (fliparActivo) {
            if (dibujar == 1) {//DELAUNAY
                for (int i = 6; i < t.obtenerNumAristas(); i = i + 2) {
                    if (esInterna(t.obtenerEdge(i))) {
                        Punto pCentral = reducirD(t.obtenerEdge(i));
                        Shape s = devolverOvalo(pCentral, 20);
                        if (s.contains(raton)) {
                            Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
                            this.setCursor(mouse);
                            break;
                        } else {
                            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
            } else {
                for (int i = 0; i < t.obtenerNumAristas(); i++) {
                    Punto pCentral = reducir(t.obtenerEdge(i));
                    Shape s = devolverOvalo(pCentral, 20);
                    if (s.contains(raton)) {
                        Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
                        this.setCursor(mouse);
                        break;
                    } else {
                        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        } else if (borrarA) {
            for (int i = 0; i < tManual.cantidadAristas(); i++) {
                ArrayList r = devolverRombo((int) raton.getX(), (int) raton.getY());
                if (cortaAristas(r, tManual.getArista(i))) {
                    Cursor mouse = new Cursor(Cursor.HAND_CURSOR);
                    this.setCursor(mouse);
                    break;
                } else {
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }

    public void leerPuntos(String archivoPuntos) {
        int iVacio1 = -1;
        int pNoAgregados = 0;
        int xMayor = 0, yMayor = 0;
        BufferedReader in;

        Punto pu;
//        Arista a;
        try {
            in = new BufferedReader(new FileReader(archivoPuntos));
            String s = new String();
            while ((s = in.readLine()) != null) {
                if (s.indexOf("(") != -1) {
                    break;
                }
                iVacio1 = s.indexOf(" ");//BUSCA LA POSICIÓN DEL CARACTER VACÍO
                if (iVacio1 != -1) {
                    pu = new Punto(Double.parseDouble(s.substring(0, iVacio1)), Double.parseDouble(s.substring(iVacio1 + 1, s.length())));//Cambio Double
                    if (pu.getX() >= 0 && -pu.getY() >= 0) {
                        pu.posicion = l.size();
                        l.add(pu);
                        if (pu.getX() > xMayor) {
                            xMayor = (int) pu.getX() + 1;
                        }
                        if (-pu.getY() > yMayor) {
                            yMayor = (int) -pu.getY() + 1;
                        }
                    } else {
                        pNoAgregados++;
                    }
                }
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.print("No se encuentra el archivo para guardar la dirección de los datos" + ex.getMessage());
            }

            agregarP = false;
            if (xMayor > this.getWidth() || yMayor > this.getHeight()) {
                //setZoom(30);
                this.setPreferredSize(new Dimension(xMayor, yMayor));
                revalidate();
            }
            repaint();
            FV.panelSur1.habilitarT(true);
            //setZoom(30);
            /*dia investigador
            tDelaunayVoronoi = new Delaunay();
            tDelaunayVoronoi.inicializar();
            for (int conttri = 0; conttri < (l.size()); conttri++) {
            tDelaunayVoronoi.insertarPunto(l.get(conttri));
            }
            /*dia investigador*/
        } catch (IOException ex) {
            Logger.getLogger(JPanelDibujo.class.getName()).log(Level.SEVERE, null, ex);
            tManual = new Configuracion();//.clear();
            l.clear();
        }

        if (pNoAgregados > 0) {
            JOptionPane.showMessageDialog(null, "No were added " + pNoAgregados + " points", "Add Points", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void leerAristas(String archivoAristas) {
        Matcher m, ptr;
        int po, pd;
        String s = new String();
        int aNoAgregadas = 0;

        try {
            if (l.size() > 0) {
                int[][] tablaAristas = new int[l.size()][l.size()];
                BufferedReader in = new BufferedReader(new FileReader(archivoAristas));
                /*s = in.readLine();
                m = Pattern.compile("[0-9]+").matcher(s);
                
                while (m.find()) {//Configuración inicial
                Arista ar = new Arista(-1, -1);
                po = Integer.parseInt(m.group());
                ar.setPosPo(po);
                if (m.find()) {
                pd = Integer.parseInt(m.group());
                ar.setPosPd(pd);
                } else {
                System.out.println("Error al cargar un punto con x: " + ar.getPosPo());
                break;
                }
                try {
                if (tablaAristas[po][pd] == 1){
                aNoAgregadas++;
                } else {
                tablaAristas[po][pd] = 1;
                tablaAristas[pd][po] = 1;
                tManual.addArista(new Arista(l.get(po), l.get(pd)));
                }
                } catch (IndexOutOfBoundsException ex) {
                aNoAgregadas++;
                }
                }*/


                //ARISTAS POR RENGLÓN
                while ((s = in.readLine()) != null) {
                    m = Pattern.compile("([0-9]+, [0-9]+)").matcher(s);
                    if (m.find()) {
                        ptr = Pattern.compile("[0-9]+").matcher(m.group());

                        while (ptr.find()) {
                            po = Integer.parseInt(ptr.group());
                            if (ptr.find()) {
                                pd = Integer.parseInt(ptr.group());
                            } else {
                                System.out.println("Error al cargar un punto con x: " + po);
                                break;
                            }
                            try {
                                if (tablaAristas[po][pd] == 1) {
                                    aNoAgregadas++;
                                    System.out.println(po + " - " + pd);
                                } else {
                                    tablaAristas[po][pd] = 1;
                                    tablaAristas[pd][po] = 1;
                                    tManual.addArista(new Arista(l.get(po), l.get(pd)));
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                aNoAgregadas++;
                            }
                        }
                    }
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    System.out.print("No se encuentra el archivo para guardar la dirección de los datos" + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "There are no points loaded", "Add Edge", JOptionPane.ERROR_MESSAGE);
            }
            //calcularDilacion(tManual);
            FV.panelSur1.habilitarT(true);
            repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
            tManual = new Configuracion();//.clear();
            l.clear();
        }

        if (aNoAgregadas > 0) {
            JOptionPane.showMessageDialog(null, "No were added " + aNoAgregadas + " edges", "Add Edges", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void borrar() {
        borrar = true;
        repaint();
    }

    public void GenerarAle(int cant) {
        Random numR = new Random();
        int b = -1, c = -1;

        while (cant > 0) {
            c = numR.nextInt();
            if (c < 0) {
                c = c * (-1);
            }
            c = (c % this.getWidth());
            b = numR.nextInt();
            if (b < 0) {
                b = b * (-1);
            }
            b = (b % this.getHeight());
            Punto pu = new Punto(c, b);
            pu.posicion = l.size();
            l.add(pu); //agrega el punto a la lista
            cant--;
        }
        queSeEstaMostrando(!(b == -1 && c == -1));
        FV.panelSur1.habilitarT(true);
        repaint();
    }

    protected Shape devolverOvalo(Punto p, int ancho) {
        return new Ellipse2D.Double(p.getX() - ancho / 2, -p.getY() - ancho / 2, ancho, ancho);
    }

    public void setDibujar(int valor) {
        dibujar = valor;
        /*Dia del investigador*/
        //pintar = false;
        /*Dia del investigador*/
        if (dibujar > 0) {
            triangular();
        } else {
            repaint();
        }
    }

    public void setPseudoT(int valor) {
        pseudoT = valor;
        if (pseudoT > 0) {
            pseudoTriangular();
        } else {
            repaint();
        }
    }

    public void setPoligonizarSimple(boolean ver, int valor, int[] opciones) {
        /*verPSim = verPCon = ver;
        if (verPSim) {
        int polAnt = poligonizar;
        poligonizar = valor;
        polOpt = opciones;
        if (poligonizar > 0) {
        calcularPoligono();
        } else {
        if (polAnt == 2) {
        l = new Conjunto();
        polAnt = 0;
        }
        }
        }*/

        verPSim = ver;
        if (ver) {
            polOpt = opciones;
            try {
                pensando(true);
                pSim = new Simple(l, polOpt);
                poligonizar = 1;//14/12/13 no repoligonizaba al arrastrar punto
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Too many points", "Polygon", JOptionPane.ERROR_MESSAGE);
            } finally {
                pensando(false);
            }
            repaint();
        }

        repaint();
    }

    public void setVoronoi(boolean valor) {
        voronoiActivo = valor;
        repaint();
    }

    public void setFlipar(boolean valor) {
        fliparActivo = valor;
        puntoElegido1 = null;
        if (dibujar != 1) {
            tDelaunay = new Delaunay();
            tDelaunay.inicializar();
            for (int conttri = 0; conttri
                    < (l.size()); conttri++) {
                tDelaunay.insertarPunto(l.get(conttri));
            }
        }
        repaint();
    }

    public void setDilacion(int valor) {
        if (valor > 0) {
            dilacionActivo = valor;
            calcularDilacion();
            repaint();
        } else {
            if (dilacionActivo != 0) {
                dijkstra.clear();
                dilacionActivo = 0;
                FV.panelNorte2.setDilacion(false);
                FV.panelSur1.setTextDilacion("0");
                repaint();
            }

        }
        /*try{
        pensando(true);
        switch (dilacionActivo) {
        case 1: {
        calcularDilacion(pta);
        }
        break;
        case 2: {
        calcularDilacion(t);
        }
        break;
        case 3: {
        calcularDilacion(tManual);
        }
        }
        } finally {
        pensando(false);
        repaint();
        }*/
    }

    public void setXY(boolean b) {
        xy = b;
        repaint();
    }

    public void flipar() {
        t.flip(t.obtenerEdge(10));
        repaint();
    }

    private ArrayList devolverRombo(int x, int y) {
        ArrayList cuadro = new ArrayList();
        cuadro.add(new Arista(new Punto(x - 3, y - 3), new Punto(x + 3, y - 3)));
        cuadro.add(new Arista(new Punto(x + 3, y - 3), new Punto(x + 3, y + 3)));
        cuadro.add(new Arista(new Punto(x + 3, y + 3), new Punto(x - 3, y + 3)));
        cuadro.add(new Arista(new Punto(x - 3, y + 3), new Punto(x - 3, y - 3)));
        return (cuadro);
    }

    private boolean cortaAristas(ArrayList aristas, Arista a) {
        boolean corta = false;
        Arista ar = null;
        double sig1, sig2;
        double corteY;
        double corteX;
        int i = 0;
        Punto p1;
        /*if (dibujar == 1) {
        p1 = a.obtenerTwin().obtenerDest();
        } else {*/
        p1 = a.obtenerOrig();
        //}
        Punto p2 = a.obtenerDest();
        while (!corta && i < aristas.size()) {
            ar = (Arista) aristas.get(i);
            Punto o = ar.obtenerOrig();
            Punto d = ar.obtenerDest();
            sig1 = Det(o, d, p1);
            sig2 = Det(o, d, p2);
            if (((sig1 < 0) && (sig2 > 0)) || ((sig1 > 0) && (sig2 < 0))) {
                double vx = p2.x - p1.x;
                double vy = p2.y - p1.y;
                double wx = d.x - o.x;
                double wy = d.y - o.y;//lleva aristas al origen
                if (vy == 0) {
                    corteY = (o.x - p1.x + (p1.y * vx) - (o.y * wx / wy)) / ((vx) - (wx / wy));
                    corteX = ((corteY - p1.y) * vx) + p1.x;
                } else if (wy == 0) {
                    corteY = (o.x - p1.x + (p1.y * vx / vy) - (o.y * wx)) / ((vx / vy) - (wx));
                    corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                } else {
                    corteY = (o.x - p1.x + (p1.y * vx / vy) - (o.y * wx / wy)) / ((vx / vy) - (wx / wy));
                    corteX = ((corteY - p1.y) * vx / vy) + p1.x;
                }
                Punto ptoCorte = new Punto((int) corteX, (int) corteY);
                if ((o.distance(ptoCorte) <= o.distance(d)) && (d.distance(ptoCorte) <= d.distance(o))) {
                    corta = true;
                }
            }
            i++;
        }
        return corta;
    }

    private double Det(Punto a, Punto b, Punto z) {
        /*return ((b.getX() * z.getY() + z.getX() * a.getY() + a.getX() * b.getY())
        - (b.getX() * a.getY() + z.getX() * b.getY() + a.getX() * z.getY()));*/
        return (((a.x - z.x) * (b.y - z.y)) - ((b.x - z.x) * (a.y - z.y)));
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //Metodo:
    //	dilacionT: calcula la longitud de los caminos encontrados para cada estrategia
    //				y la dilacion/ratio con la distancia elegida para comparar
    //Entrada:
    //	pathSelected: 	tipo de distancia para comparar actualmente elegida (E_D,SP_D,SPG_D definidos en Geocomp)
    //	tipoR:			estrategia de ruteo seleccionada
    //	tipoG:			tipo de grafo seleccionado
    ///////////////////////////////////////////////////////////////////////////////////////
    /*public void dilacionT(/*int pathSelected,int tipoG) {
    double dist = 0;//, distAlg = 0;
    //double minDistSP=0.0d;
    double minDistAlg = 0.0d;
    double dilation = 0.0d;
    
    for (int i = 0; i < l.size(); i++) {
    l.get(i).eliminarVecinos();
    }
    
    if (dibujar == 1) {
    for (int i = 6; i < t.obtenerNumAristas(); i = i + 2) {
    if (esInterna(t.obtenerEdge(i))) {
    t.agregarVecinos(t.obtenerEdge(i));
    }
    }
    } else {
    for (int i = 0; i < t.obtenerNumAristas(); i++) {
    t.agregarVecinos(t.obtenerEdge(i));
    }
    }
    
    Punto saveOrigen = new Punto(0, 0);
    Punto saveDestino = new Punto(0, 0);
    for (int i = 0; i < t.getNumeroPuntos(); i++) {
    Punto p = (Punto) t.getPunto(i);
    for (int j = i + 1; j < t.getNumeroPuntos(); j++) {
    Punto q = (Punto) t.getPunto(j);
    if (!p.igualQue(q)) {//Point2d.equals
    //establecer origen-destino
    setOrigen(p);
    setDestino(q);
    //calculamos el camino de dijkstra
    //
    calculaDijkstraT(/*tipoG);
    double distSP = 0.0d;
    for (int k = 0; k < dijkstra.size(); k++) {
    Arista aristaAux = (Arista) dijkstra.get(k);
    distSP += aristaAux.longitud();
    }
    //calculamos la dist euclidea
    dist = p.distance(q);
    if ((distSP / dist) > dilation) {
    dilation = distSP / dist;
    //minDistSP=distSP;
    minDistAlg = dist;
    saveOrigen = puntoOrigen;
    saveDestino = puntoDestino;
    }
    }
    }
    }
    dist = minDistAlg;
    //distAlg=minDistSP;
    
    setOrigen(saveOrigen);
    setDestino(saveDestino);
    calculaDijkstraT(/*tipoG);
    }
    
    public void calculaDijkstraT(/*int tipo) {
    if (t.getNumeroPuntos() > 1) {
    dijkstra.clear();
    int INFINITO = Integer.MAX_VALUE;
    boolean usados[] = new boolean[t.getNumeroPuntos()];
    double d[] = new double[t.getNumeroPuntos()];//dilacionT
    int p[] = new int[t.getNumeroPuntos()];//precedentes
    int iOrigen = 0, iDestino = 0;
    for (int i = 0; i < t.getNumeroPuntos(); i++) {
    usados[i] = false;
    if (t.getPunto(i).igualQue(puntoOrigen)) {
    iOrigen = i;
    } else if (t.getPunto(i).igualQue(puntoDestino)) {
    iDestino = i;
    }
    d[i] = INFINITO;
    p[i] = iOrigen;
    }
    d[iOrigen] = 0;
    usados[iOrigen] = false;
    boolean terminado = false;
    while (!terminado) {
    boolean encontrado = false;
    double menorDist = INFINITO;
    int ind = 0;
    for (int i = 0; i < usados.length; i++) {
    if ((usados[i] == false) && (d[i] < menorDist)) {
    //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
    //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
    menorDist = d[i];
    encontrado = true;
    ind = i;
    }
    }
    if (encontrado && (ind != iDestino)) {
    usados[ind] = true;
    ArrayList ady = (ArrayList) t.getPunto(ind).getVecinos();
    for (int j = 0; j < ady.size(); j++) {
    int ind2 = 0;
    for (ind2 = 0; ind2 < t.getNumeroPuntos(); ind2++) {
    if (((Punto) ady.get(j)).igualQue(t.getPunto(ind2))) {
    break;
    }
    }
    if (d[ind2] > d[ind] + (t.getPunto(ind2)).distance(t.getPunto(ind))) {
    d[ind2] = d[ind] + (t.getPunto(ind2)).distance(t.getPunto(ind));
    p[ind2] = ind;
    }
    }
    } else {
    terminado = true;
    }
    }
    boolean salir = false;
    int iP = iDestino;
    while (!salir) {
    Punto p1 = new Punto(t.getPunto(iP));//Punto p1=((Punto)l.get(iP)).copiar();
    Punto p2 = new Punto(t.getPunto((p[iP])));//Punto p2=((Punto)l.get(p[iP])).copiar();
    dijkstra.add(new Arista(p1, p2));//dijkstra.add(new Arista(p1,p2,0));
    if (p[iP] == iOrigen) {
    salir = true;
    }
    iP = p[iP];
    }
    }
    }*/

    /*public void dilacionPT(/*int pathSelected,int tipoG) {
    double dist = 0;//, distAlg = 0;
    //double minDistSP=0.0d;
    double minDistAlg = 0.0d;
    double dilation = 0.0d;
    
    for (int i = 0; i < pta.getNumeroPuntos(); i++) {
    pta.getPunto(i).eliminarVecinos();
    }
    
    for (int i = 0; i < pta.cantidadAristas(); i++) {
    pta.agregarVecinos(pta.getArista(i));
    }
    
    Punto saveOrigen = new Punto(0, 0);
    Punto saveDestino = new Punto(0, 0);
    for (int i = 0; i < pta.getNumeroPuntos(); i++) {
    Punto p = (Punto) pta.getPunto(i);
    for (int j = i + 1; j < pta.getNumeroPuntos(); j++) {
    Punto q = (Punto) pta.getPunto(j);
    if (!p.igualQue(q)) {//Point2d.equals
    //establecer origen-destino
    setOrigen(p);
    setDestino(q);
    //calculamos el camino de dijkstra
    calculaDijkstraPT(/*tipoG);
    double distSP = 0.0d;
    for (int k = 0; k < dijkstra.size(); k++) {
    Arista aristaAux = (Arista) dijkstra.get(k);
    distSP += aristaAux.longitud();
    }
    //calculamos la dist euclidea
    dist = p.distance(q);
    if ((distSP / dist) > dilation) {
    dilation = distSP / dist;
    //minDistSP=distSP;
    minDistAlg = dist;
    saveOrigen = puntoOrigen;
    saveDestino = puntoDestino;
    }
    }
    }
    }
    dist = minDistAlg;
    //distAlg=minDistSP;
    
    setOrigen(saveOrigen);
    setDestino(saveDestino);
    calculaDijkstraPT(/*tipoG);
    }
    
    public void calculaDijkstraPT(/*int tipo) {
    if (pta.getNumeroPuntos() > 1) {
    dijkstra.clear();
    int INFINITO = Integer.MAX_VALUE;
    boolean usados[] = new boolean[pta.getNumeroPuntos()];
    double d[] = new double[pta.getNumeroPuntos()];//dilacionT
    int p[] = new int[pta.getNumeroPuntos()];//precedentes
    int iOrigen = 0, iDestino = 0;
    for (int i = 0; i < pta.getNumeroPuntos(); i++) {
    usados[i] = false;
    if (pta.getPunto(i).igualQue(puntoOrigen)) {
    iOrigen = i;
    } else if (pta.getPunto(i).igualQue(puntoDestino)) {
    iDestino = i;
    }
    d[i] = INFINITO;
    p[i] = iOrigen;
    }
    d[iOrigen] = 0;
    usados[iOrigen] = false;
    boolean terminado = false;
    while (!terminado) {
    boolean encontrado = false;
    double menorDist = INFINITO;
    int ind = 0;
    for (int i = 0; i < usados.length; i++) {
    if ((usados[i] == false) && (d[i] < menorDist)) {
    //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
    //menorDist=((Punto)puntos.get(i)).distance((Punto)puntos.get(iOrigen));
    menorDist = d[i];
    encontrado = true;
    ind = i;
    }
    }
    if (encontrado && (ind != iDestino)) {
    usados[ind] = true;
    ArrayList ady = (ArrayList) pta.getPunto(ind).getVecinos();
    for (int j = 0; j < ady.size(); j++) {
    int ind2 = 0;
    for (ind2 = 0; ind2 < pta.getNumeroPuntos(); ind2++) {
    if (((Punto) ady.get(j)).igualQue(pta.getPunto(ind2))) {
    break;
    }
    }
    if (d[ind2] > d[ind] + (pta.getPunto(ind2)).distance(pta.getPunto(ind))) {
    d[ind2] = d[ind] + (pta.getPunto(ind2)).distance(pta.getPunto(ind));
    p[ind2] = ind;
    }
    }
    } else {
    terminado = true;
    }
    }
    boolean salir = false;
    int iP = iDestino;
    while (!salir) {
    Punto p1 = new Punto(pta.getPunto(iP));//Punto p1=((Punto)l.get(iP)).copiar();
    Punto p2 = new Punto(pta.getPunto((p[iP])));//Punto p2=((Punto)l.get(p[iP])).copiar();
    dijkstra.add(new Arista(p1, p2));//dijkstra.add(new Arista(p1,p2,0));
    if (p[iP] == iOrigen) {
    salir = true;
    }
    iP = p[iP];
    }
    }
    }
    
    public void setOrigen(Punto p) {
    puntoOrigen = p;
    }
    
    public void setDestino(Punto p) {
    puntoDestino = p;
    }*/
    public void setAgregar(int a) {
        switch (a) {
            case 0: {
                agregarP = true;
                agregarA = false;
                setBorrar(-1);
            }
            break;
            case 1: {
                agregarP = false;
                agregarA = true;
                setBorrar(-1);
            }
            break;
            default: {
                agregarP = false;
                agregarA = false;
                //setBorrar(-1);//agregado 13/12/13 porque al cargar puntos aleatorios, si antes estaba borrando seguirá borrando.
            }
        }
    }

    public boolean guardarTXT() throws IOException {

        //FileOutputStream outP;
        String nombre = new String();

        MyFileChooser archivoPuntos = new MyFileChooser(FV.getDirectorio(), "Save points", new FileNameExtensionFilter("Text file- .txt", "txt"));

        if (archivoPuntos.getSelectedFile() != null) {//archivoPuntos.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION) {
            nombre = archivoPuntos.getSelectedFile().getAbsolutePath();
            /*if (!nombre.endsWith(".txt")) {
            nombre += ".txt";
            }*/
            BufferedWriter out = new BufferedWriter(new FileWriter(nombre));

            for (int i = 0; i < l.size(); i++) {
                out.write(String.valueOf(l.get(i).getX()) + " " + String.valueOf(-l.get(i).getY()));
                if (l.size() > i + 1) {
                    out.newLine();
                }
            }
            out.close();

            //Guarda el último directorio accedido
            FV.setDirectorio(archivoPuntos.getSelectedFile().getParent());

            if (dibujar > 0) {
                MyFileChooser archivoAristas = new MyFileChooser(FV.getDirectorio(), "Save edges triangulation", new FileNameExtensionFilter("Text file- .txt", "txt"));
                if (archivoAristas.getSelectedFile() != null) {

                    nombre = archivoAristas.getSelectedFile().getAbsolutePath();
                    BufferedWriter outA = new BufferedWriter(new FileWriter(nombre));
                    if (dibujar == DELAUNAY) {
                        for (int i = 6; i < t.obtenerNumAristas(); i = i + 2) {
                            if (esInterna(t.obtenerEdge(i))) {
                                outA.write("(" + l.getPos(t.obtenerEdge(i).obtenerDest()) + ", " + l.getPos(t.obtenerEdge(i).obtenerTwin().obtenerDest()) + ")");
                                if ((i + 2) < t.obtenerNumAristas()) {
                                    outA.newLine();
                                }
                            }
                        }
                    } else if (dibujar > DELAUNAY) {
                        for (int i = 0; i < t.obtenerNumAristas(); i++) {
                            outA.write("(" + l.getPos(t.obtenerEdge(i).obtenerOrig().contraRotado(t.getGrado())) + ", " + l.getPos(t.obtenerEdge(i).obtenerDest().contraRotado(t.getGrado())) + ")");
                            if ((i + 1) < t.obtenerNumAristas()) {
                                outA.newLine();
                            }
                        }
                    }
                    
                    outA.close();
                }
            }
            
            if (pseudoT > 0) {
                MyFileChooser archivoAristas = new MyFileChooser(FV.getDirectorio(), "Save edges pseudotriangulation", new FileNameExtensionFilter("Text file- .txt", "txt"));
                if (archivoAristas.getSelectedFile() != null) {

                    nombre = archivoAristas.getSelectedFile().getAbsolutePath();
                    BufferedWriter outA = new BufferedWriter(new FileWriter(nombre));
                    
                    if (pseudoT > 0) {
                        for (int i = 0; i < pta.cantidadAristas(); i++) {
                            outA.write("(" + l.getPos(pta.getArista(i).obtenerOrig()) + ", " + l.getPos(pta.getArista(i).obtenerDest()) + ")");
                            if ((i + 1) < pta.cantidadAristas()) {
                                outA.newLine();
                            }
                        }
                    }
                    
                    outA.close();
                }
            }
            
            if (tManual.cantidadAristas() > 0) {
                MyFileChooser archivoAristas = new MyFileChooser(FV.getDirectorio(), "Save edges manual graph", new FileNameExtensionFilter("Text file- .txt", "txt"));
                if (archivoAristas.getSelectedFile() != null) {

                    nombre = archivoAristas.getSelectedFile().getAbsolutePath();
                    BufferedWriter outA = new BufferedWriter(new FileWriter(nombre));
                    
                    if (tManual.cantidadAristas() > 0) {
                        for (int i = 0; i < tManual.cantidadAristas(); i++) {
                            outA.write("(" + l.getPos(tManual.getArista(i).obtenerOrig()) + ", " + l.getPos(tManual.getArista(i).obtenerDest()) + ")");
                            outA.newLine();
                        }
                    }
                    
                    outA.close();
                }
            }
            //Guarda el último directorio accedido
            FV.setDirectorio(archivoPuntos.getSelectedFile().getParent());
            return true;
        }
        return false;
    }

    void guardarPNG(boolean b) {
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file - .png", "png");
        MyFileChooser archivo = new MyFileChooser(FV.getDirectorio(), "Save image", filtro);
        if (archivo.getSelectedFile() != null) {
            nombreArchivoG = archivo.getSelectedFile().getAbsolutePath();
            /*if (!nombreArchivoG.endsWith(".png")) {
            nombreArchivoG += ".png";
            }*/
            guardarPNG = b;
            //Guarda el último directorio accedido
            FV.setDirectorio(archivo.getSelectedFile().getParent());
        }
        repaint();
    }

    void guardarEPS(boolean b) {
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file- .eps", "eps");
        MyFileChooser archivo = new MyFileChooser(FV.getDirectorio(), "Save image", filtro);
        if (archivo.getSelectedFile() != null) {
            nombreArchivoG = archivo.getSelectedFile().getAbsolutePath();
            guardarEPS = b;
            //Guarda el último directorio accedido
            FV.setDirectorio(archivo.getSelectedFile().getParent());
        }
        repaint();
    }
    
    void guardarImagen(boolean b){
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file- .png", "png");
        MyFileChooser archivo = new MyFileChooser(FV.getDirectorio(), "Save image", new FileNameExtensionFilter("Image file- .eps", "eps"), filtro);
        //filtro = new FileNameExtensionFilter("Image file - .png", "png");
        //archivo.addChoosableFileFilter(filtro);
        if (archivo.getSelectedFile() != null) {
            if (archivo.getFileFilter().getDescription().equals("Image file- .eps")){
                nombreArchivoG = archivo.getSelectedFile().getAbsolutePath();
                if (!nombreArchivoG.endsWith(".eps")){
                    nombreArchivoG += ".eps";
                }
                guardarEPS = b;
            } else if (archivo.getFileFilter().getDescription().equals("Image file- .png")){
                nombreArchivoG = archivo.getSelectedFile().getAbsolutePath() + ".png";
                if (!nombreArchivoG.endsWith(".png")){
                    nombreArchivoG += ".png";
                }
                guardarPNG = b;
            }
            
            //Guarda el último directorio accedido
            FV.setDirectorio(archivo.getSelectedFile().getParent());
        }
        repaint();
    }

    void setBorrar(int a) {
        switch (a) {
            case 0: {
                borrarP = true;
                borrarA = false;
                setAgregar(-1);
            }
            break;
            case 1: {
                borrarP = false;
                borrarA = true;
                setAgregar(-1);
            }
            break;
            default: {
                borrarP = false;
                borrarA = false;
            }
        }
    }

    void mostrar(int i) {
        mostrarPToT = mostrarPToT + i;
        if (dilacionActivo != 0) {
            calcularDilacion();
        }
        repaint();
    }

    void setMostrarGraph(boolean i) {
        mostrarGraph = i;
        if (dilacionActivo != 0) {
            calcularDilacion();
        }
        repaint();
    }

    private Punto reducirD(Arista a) {
        Punto central;
        int x1 = (int) a.obtenerDest().getX();
        int x2 = (int) a.obtenerTwin().obtenerDest().getX();
        int y1 = (int) -a.obtenerDest().getY();
        int y2 = (int) -a.obtenerTwin().obtenerDest().getY();
        if (x1 < x2) {
            x1 = x1 + ((x2 - x1) / 2);
        } else {
            x1 = x2 + ((x1 - x2) / 2);
        }
        if (y1 < y2) {
            y1 = y1 + ((y2 - y1) / 2);
        } else {
            y1 = y2 + ((y1 - y2) / 2);
        }
        central = new Punto(x1, y1);
        return central;
    }

    private Punto reducir(Arista a) {
        Punto central;
        int x1 = (int) a.obtenerOrig().contraRotado(grado).getX();
        int x2 = (int) a.obtenerDest().contraRotado(grado).getX();
        int y1 = (int) -a.obtenerOrig().contraRotado(grado).getY();
        int y2 = (int) -a.obtenerDest().contraRotado(grado).getY();
        if (x1 < x2) {
            x1 = x1 + ((x2 - x1) / 2);
        } else {
            x1 = x2 + ((x1 - x2) / 2);
        }
        if (y1 < y2) {
            y1 = y1 + ((y2 - y1) / 2);
        } else {
            y1 = y2 + ((y1 - y2) / 2);
        }
        central = new Punto(x1, y1);
        return central;
    }

    void cargarImagenFondo(boolean b) {
        if (hayImagenFondo = b) {
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file- .gif, .png, .jpg", "gif", "png", "jpg");
            JFileChooser archivo = new JFileChooser(FV.getDirectorio());
            archivo.addChoosableFileFilter(filtro);
            if (archivo.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
                archivoImagenFondo = archivo.getSelectedFile().getAbsolutePath();
                if (!archivoImagenFondo.endsWith(".jpg") && !archivoImagenFondo.endsWith(".gif") && !archivoImagenFondo.endsWith(".png")) {
                    archivoImagenFondo += ".jpg";
                }
                FV.panelNorte2.habilitarImagen(true);
                FV.setShowImage(true);
                //FileOutputStream outP = null;
                //URL url = this.getClass().getClassLoader().getResource(propertyFileName);
                FV.setDirectorio(archivo.getSelectedFile().getParent());
            } else {
                hayImagenFondo = false;
            }
            repaint();
        }
    }

    void setImagenFondo(boolean b) {
        hayImagenFondo = b;
        if (b && archivoImagenFondo == null) {
            hayImagenFondo = false;
            FV.panelNorte2.habilitarImagen(false);
            FV.setShowImage(false);
        } else if (b) {
            FV.panelNorte2.habilitarImagen(true);
            FV.setShowImagePunto(true);
        } else if (!b){
            FV.panelNorte2.habilitarImagen(false);
            FV.setShowImage(false);
        }
        repaint();
    }
    
    void cargarImagenPunto(boolean b) {
        if (hayImagenPunto = b) {
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de imagen - .gif, .png, .jpg", "gif", "png", "jpg");
            JFileChooser archivo = new JFileChooser(FV.getDirectorio());
            archivo.addChoosableFileFilter(filtro);
            if (archivo.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
                archivoImagenPunto = archivo.getSelectedFile().getAbsolutePath();
                if (!archivoImagenPunto.endsWith(".jpg") && !archivoImagenPunto.endsWith(".gif") && !archivoImagenPunto.endsWith(".png")) {
                    archivoImagenPunto += ".jpg";
                } 
                FV.panelNorte2.habilitarImagenPunto(true);
                FV.setShowImagePunto(true);
                FV.setDirectorio(archivo.getSelectedFile().getParent());
            } else {
                //cargarImagen = false;
                archivoImagenPunto = null;
                FV.panelNorte2.habilitarImagenPunto(false);
            }
        } else {
            archivoImagenPunto = null;
        }

        repaint();
    }

    void setImagenPunto(boolean b) {
        hayImagenPunto = b;
        if (b && archivoImagenPunto == null) {
            hayImagenFondo = false;
            FV.panelNorte2.habilitarImagenPunto(false);
            FV.setShowImagePunto(false);
        } else if (b) {
            FV.panelNorte2.habilitarImagenPunto(true);
            FV.setShowImagePunto(true);
        } else if (!b){
            FV.panelNorte2.habilitarImagenPunto(false);
            FV.setShowImagePunto(false);
        }
        repaint();
    }

    private boolean contieneArista(Arista a) {
        for (int i = 0; i
                < tManual.cantidadAristas(); i++) {
            if ((tManual.getArista(i).obtenerOrig().igualQue(a.obtenerOrig()) && tManual.getArista(i).obtenerDest().igualQue(a.obtenerDest()))
                    || (tManual.getArista(i).obtenerOrig().igualQue(a.obtenerDest()) && tManual.getArista(i).obtenerDest().igualQue(a.obtenerOrig()))) {
                return true;
            }
        }
        return false;
    }

    void settDelaunayActivo(boolean selected) {
        tDelaunayActivo = selected;
        repaint();
    }
    /*Dia del investigador*/

    void setColor(Color jcc) {
        if (dibujar >= 1) {
            color = jcc;
            pintar = true;
        } else {
            //JOptionPane.showMessageDialog(null, "Only can be painted triangulations", "Paint", JOptionPane.ERROR_MESSAGE);
        }
    }

    void setPintar(Boolean b) {
        pintar = b;
        if (pintar){
            puntoElegido1 = null;
            if (dibujar > 1) {
                dcel = new GenerarDCEL(t, l, t.getGrado());
                if (pintarTri.caras.size() > 0){
                    pintarTri.caras = new ArrayList<Polygon>();
                    for (Punto raton : pintarTri.puntos){
                        Polygon zona = new Polygon();
                        for (int j = 0; j < dcel.getDcel().getFaceList().size() - 1; j++) {
                            Triangulo triang = dcel.getDcel().getFace(j);
                            if (!t.EsTrianguloVacio(triang, new Punto((int) raton.getX(), (int) raton.getY()))) {
                                zona.addPoint((int) triang.getVerticeA().getX(), (int) -triang.getVerticeA().getY());
                                zona.addPoint((int) triang.getVerticeB().getX(), (int) -triang.getVerticeB().getY());
                                zona.addPoint((int) triang.getVerticeC().getX(), (int) -triang.getVerticeC().getY());
                                pintarTri.caras.add(zona);
                            }
                        }
                    }
                }
            } else if (dibujar == 1){
                pintarTri.caras = new ArrayList<Polygon>();
                for (Punto raton : pintarTri.puntos){
                    Polygon zona = new Polygon();
                    Delaunay d = (Delaunay) t;
                    Arista ar = d.enQueTriangulo(new Punto((int) raton.getX(), (int) raton.getY()));
                    if (esInterna(ar)) {
                        zona.addPoint((int) ar.obtenerDest().getX(), -(int) ar.obtenerDest().getY());
                        zona.addPoint((int) ar.obtenerTwin().obtenerDest().getX(), -(int) ar.obtenerTwin().obtenerDest().getY());
                        zona.addPoint((int) ar.obtenerSig().obtenerDest().getX(), -(int) ar.obtenerSig().obtenerDest().getY());
                        pintarTri.caras.add(zona);
                    }
                }
            }
        } else {
            //FV.panelNorte2.habilitarPintar(false);
        }
        repaint();
    }
    /*Dia del investigador*/

    private void calcularPoligono() {
        if (poligonizar > 0) {
            try {
                pensando(true);
                switch (poligonizar) {
                    case 1:
                        pSim = new Simple(l, polOpt);
                        break;
                    case 2:
                        FV.panelSur1.habilitarT(true);
                        pCon = new Convexo(polOpt[0]);
                        getPuntosPol();
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Too many points", "Polygon", JOptionPane.ERROR_MESSAGE);
            } finally {
                pensando(false);
            }
            repaint();
        }
    }

    private void getPuntosPol() {
        lPol = new Conjunto();
        for (int i = 0; i < pCon.size(); i++) {
            lPol.add(new Punto((int) pCon.getArista(i).obtenerOrig().getX(), (int) -pCon.getArista(i).obtenerOrig().getY()));
        }
    }

    void avanzarPap() {
        pap++;
        repaint();
    }

    void retrocederPap() {
        if (pap > 0) {
            pap--;
            repaint();
        }
    }

    private void calcularDilacion(Configuracion t) {
        if (dilacionActivo > 0) {
            try {
                pensando(true);
                dijkstra = t.dilacion(l);
                puntoOrigen = t.getPuntoOrigen();
                puntoDestino = t.getPuntoDestino();
            } finally {
                pensando(false);
                //repaint();    
            }
        }
    }

    void setNro(boolean selected) {
        nro = selected;
        repaint();
    }

    void setZoom(int z) {
        zoom = z / 30.0f;
        setWidthInicial(this.getWidth());
        setHeightInicial(this.getHeight());
        Dimension d = new Dimension((int) (widthInicial * zoom), (int) (heightInicial * zoom));
        if (d.getHeight() < FV.getSizeArea().getHeight()) {
            d.setSize(FV.getSizeArea().getWidth() - 100, FV.getSizeArea().getHeight() - 100);
        }
        this.setPreferredSize(d);
        //getParent().getParent().setPreferredSize(new Dimension(200, 600));
        this.revalidate();
        repaint();
    }

    void setWidthInicial(int w) {
        if (widthInicial == 0) {
            widthInicial = w;
        }
    }

    void setHeightInicial(int h) {
        if (heightInicial == 0) {
            heightInicial = h;
        }
    }

    void cargarDesdeLote(Configuracion configuracion) {
        tManual = configuracion;
        l = configuracion.getPuntos();
        repaint();
    }

    void cH(boolean ver) {
        if (ver && l.size() > 2) {
            ch = new ConvexHull(l);
        } else {
            ch = null;
        }
        repaint();
    }

    void setGrado(int g) {
        grado = g;
    }

    /*void TSP() {
    tsp = new TSP();
    repaint();
    }*/
    public void addFilesToExistingZip(File zipFile,
            File[] files) throws IOException {
        // get a temp file
        File tempFile = File.createTempFile(zipFile.getName(), null);
        // delete it, otherwise you cannot rename your existing zip to it.
        tempFile.delete();

        boolean renameOk = zipFile.renameTo(tempFile);
        if (!renameOk) {
            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        }
        byte[] buf = new byte[1024];

        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            boolean notInFiles = true;
            for (File f : files) {
                if (f.getName().equals(name) || name.equals((String) "visLotes/" + f.getName())) {
                    notInFiles = false;
                    break;
                }
            }
            if (notInFiles) {
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(name));
                // Transfer bytes from the ZIP file to the output file
                int len;
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            entry = zin.getNextEntry();
        }
        // Close the streams        
        zin.close();
        // Compress the files
        for (int i = 0; i < files.length; i++) {
            InputStream in = new FileInputStream(files[i]);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry("visLotes" + File.separator + files[i].getName()));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
        }
        // Complete the ZIP file
        out.close();
        tempFile.delete();
    }

    void changeColor(String conf, Color selectedColor) {
        if (conf.equals("pt")) {
            ptColor = selectedColor;
        } else if (conf.equals("t")) {
            tColor = selectedColor;
        } else if (conf.equals("p")) {
            pColor = selectedColor;
        } else {
            mcolor = selectedColor;
        }
        repaint();
    }

    void setVerP(boolean selected) {
        verPSim = selected;
        repaint();
    }

    void triangulacionAleatoria() {
        if (l.size() > 2){
            try {
                pensando(true);
                tManual = new RandomT();
                tManual.inicializar();
                tManual.calculaGreedyT(l);
                if (dilacionActivo != 0) {
                    calcularDilacion();
                }
            } finally {
                pensando(false);
                repaint();
            }
        } else {
            tManual = null;
            //mostrarGraph = false;
        }
    }

    private void pensando(boolean b) {
        if (b) {
            FV.getFrames()[0].setCursor(Cursor.WAIT_CURSOR);
        } else {
            FV.getFrames()[0].setCursor(Cursor.DEFAULT_CURSOR);
        }
    }

    void setPoligonizarConvexo(boolean ver, int i, int[] opciones) {
        verPCon = ver;
        if (ver) {
            try {
                pensando(true);
                FV.panelSur1.habilitarT(true);
                pCon = new Convexo(opciones[0]);
                getPuntosPol();
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Too many points", "Polygon", JOptionPane.ERROR_MESSAGE);
            } finally {
                pensando(false);
            }
        }
        repaint();
    }

    void calcularDilacion() {
        if (mostrarGraph && tManual.cantidadAristas() > 0) {
            if ((t == null && pta == null)
                    || (mostrarPToT == 1 && pta == null)
                    || (mostrarPToT == 2 && t == null)) {
                calcularDilacion(tManual);
            } else {
                setDilacion(0);
            }
        } else if ((mostrarPToT == 1 || mostrarPToT == 3) && (pta != null)) {
            if (!(mostrarPToT == 3 && t != null)) {
                calcularDilacion(pta);
            } else {
                setDilacion(0);
            }
        } else if ((mostrarPToT == 2 || mostrarPToT == 3) && (t != null)) {
            calcularDilacion(t);
        } else {
            setDilacion(0);
        }
    }

    private void guardarEnLote(boolean flip, Configuracion a) {
        try {
            BufferedWriter out;
        
            out = new BufferedWriter(new FileWriter("D:/Facu/BD/Lotes de prueba/presentacion/lotegenerado.cfg", true));
            /*if (t instanceof Delaunay){
                out.write("{(" + a.obtenerTwin().obtenerDest().posicion + ", " + a.obtenerDest().posicion + ")} " );
            }
            if (flip){
                out.newLine();
            }*/
            
            out.write("{");
            for (int i = 0; i < a.cantidadAristas(); i++){
                out.write("(" + a.getArista(i).obtenerOrig().posicion + ", " + a.getArista(i).obtenerDest().posicion + ") ");
            }
            out.write("}");
            out.newLine();
            //
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(JPanelDibujo.class.getName()).log(Level.SEVERE, null, ex);
        }

            
    }
}
