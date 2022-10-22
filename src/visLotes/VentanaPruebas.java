package visLotes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;
import visualizador.Arista;
import visualizador.Configuracion;
import visualizador.FV;
import visualizador.Punto;

/**
 *
 * @author Pablazo
 */
public class VentanaPruebas extends javax.swing.JFrame {

    private JFileChooser archivo;
    BufferedReader in;
    String nombre;
    String directorio;
    InputStream is;
    private static Properties properties = null;
    private static String propertyFileName = "visLotes/Directorio.txt";
    Rule columnView;
    Rule rowView;
    private float zoom = 1f;

    /** Creates new form VentanaPruebas */
    public VentanaPruebas() {
        nombre = new String();
        in = null;
        initComponents();
        //Create the row and column headers.
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);
        columnView.setPreferredHeight(15);
        columnView.setPreferredWidth((int) (1000));
        columnView.setZoom(zoom);
        rowView.setPreferredWidth(15);
        rowView.setPreferredHeight((int) (1000));
        rowView.setZoom(zoom);
        jScrollPane1.setColumnHeaderView(columnView);
        jScrollPane1.setRowHeaderView(rowView);
        jScrollPane1.setCorner(JScrollPane.LOWER_LEFT_CORNER, new Corner());
        jScrollPane1.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner());

        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(propertyFileName));

            directorio = new String();
            directorio = (String) properties.get("dir");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo leer: " + ex.getMessage(), "Leer archivo", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            System.out.println("No se encontr� el archivo con la direcci�n de los archivos de prueba");
        } catch (IOException ex) {
            Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
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
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTitulo = new visLotes.TituloPrueba();
        panelBotones = new javax.swing.JPanel();
        jbtCargarPuntos = new javax.swing.JButton();
        btnInicio = new javax.swing.JButton();
        btnRetroceder = new javax.swing.JButton();
        btnAvanzar = new javax.swing.JButton();
        btnFin = new javax.swing.JButton();
        txtMovimiento = new javax.swing.JTextField();
        btnMovimiento = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlLote = new visLotes.PanelLotes();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Simulations");
        getContentPane().add(pnlTitulo, java.awt.BorderLayout.PAGE_START);

        panelBotones.setPreferredSize(new java.awt.Dimension(680, 40));
        panelBotones.setLayout(new java.awt.GridBagLayout());

        jbtCargarPuntos.setText("Points");
        jbtCargarPuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCargarPuntosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        panelBotones.add(jbtCargarPuntos, gridBagConstraints);

        btnInicio.setText("<<");
        btnInicio.setToolTipText("Play");
        btnInicio.setEnabled(false);
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panelBotones.add(btnInicio, gridBagConstraints);

        btnRetroceder.setText("<");
        btnRetroceder.setToolTipText("Volver");
        btnRetroceder.setEnabled(false);
        btnRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetrocederActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panelBotones.add(btnRetroceder, gridBagConstraints);

        btnAvanzar.setText(">");
        btnAvanzar.setToolTipText("Avanzar");
        btnAvanzar.setEnabled(false);
        btnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panelBotones.add(btnAvanzar, gridBagConstraints);

        btnFin.setText(">>");
        btnFin.setToolTipText("Stop");
        btnFin.setEnabled(false);
        btnFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panelBotones.add(btnFin, gridBagConstraints);

        txtMovimiento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMovimiento.setText("0");
        txtMovimiento.setEnabled(false);
        txtMovimiento.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelBotones.add(txtMovimiento, gridBagConstraints);

        btnMovimiento.setText("Go to");
        btnMovimiento.setEnabled(false);
        btnMovimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimientoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panelBotones.add(btnMovimiento, gridBagConstraints);

        jLabel2.setText("Zoom");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panelBotones.add(jLabel2, gridBagConstraints);

        jSlider1.setMaximum(50);
        jSlider1.setMinimum(1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setValue(25);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        panelBotones.add(jSlider1, gridBagConstraints);

        jButton1.setText("Instances");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        panelBotones.add(jButton1, gridBagConstraints);

        jButton2.setText("Save image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelBotones.add(jButton2, gridBagConstraints);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/visLotes/volver.png"))); // NOI18N
        jButton3.setMinimumSize(new java.awt.Dimension(35, 25));
        jButton3.setPreferredSize(new java.awt.Dimension(35, 25));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelBotones.add(jButton3, gridBagConstraints);

        getContentPane().add(panelBotones, java.awt.BorderLayout.SOUTH);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlLote.setPreferredSize(new java.awt.Dimension(1000, 1000));

        javax.swing.GroupLayout pnlLoteLayout = new javax.swing.GroupLayout(pnlLote);
        pnlLote.setLayout(pnlLoteLayout);
        pnlLoteLayout.setHorizontalGroup(
            pnlLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        pnlLoteLayout.setVerticalGroup(
            pnlLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlLote);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCargarPuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCargarPuntosActionPerformed
        // TODO add your handling code here:
        archivo = new JFileChooser(FV.getDirectorio());
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Puntos (.pnt)", "pnt");
        archivo.addChoosableFileFilter(filtro);
        if (archivo.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
            FV.setDirectorio(archivo.getSelectedFile().getParent());
            leerPuntos(archivo.getSelectedFile().getAbsolutePath());
            /*FileOutputStream out = null;

            URL url = this.getClass().getClassLoader().getResource(propertyFileName);
            try {
                out = new FileOutputStream(url.getPath());
                properties.put("dir", archivo.getSelectedFile().getParent());
                properties.store(out, null);
                directorio = properties.getProperty("dir");
            } catch (IOException ex) {
                Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                System.out.print("No se encuentra el archivo para guardar la direcci�n de los datos" + ex.getMessage());
            } finally {
                leerPuntos(archivo.getSelectedFile().getAbsolutePath());
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }*/
        }
}//GEN-LAST:event_jbtCargarPuntosActionPerformed

    private void btnMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimientoActionPerformed
        //jPanel1.setMejor(false);
        pnlLote.setPos(txtMovimiento.getText());
        txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
}//GEN-LAST:event_btnMovimientoActionPerformed

    private void btnFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinActionPerformed
        pnlLote.endPos();
        txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
        pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
        pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
}//GEN-LAST:event_btnFinActionPerformed

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        // TODO add your handling code here:
        pnlLote.forwardPos();
        txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
        pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
        pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
}//GEN-LAST:event_btnAvanzarActionPerformed

    private void btnRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetrocederActionPerformed
        pnlLote.backwardPos();
        txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
        pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
        pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
}//GEN-LAST:event_btnRetrocederActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
        //jPanel1.setMejor(false);
        pnlLote.resetPos();
        txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
        pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
        pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
}//GEN-LAST:event_btnInicioActionPerformed

private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
    JSlider source = (JSlider) evt.getSource();
    if (!source.getValueIsAdjusting()) {
        zoom = 0.05f * source.getValue();
        /*switch ((int) zoom) {
            case 1:
                zoom = 0.5f;
                break;
            case 2:
                zoom = 1f;
                break;
            case 3:
                zoom = 1.5f;
                break;
            case 4:
                zoom = 2f;
                break;
        }*/
        
        //System.out.print(zoom + " - ");
        pnlLote.setZoom(zoom);
        pnlLote.repaint();
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);
        columnView.setPreferredWidth((int)(1000 * zoom));
        rowView.setPreferredHeight((int)(1000 * zoom));
        columnView.setZoom(zoom);
        rowView.setZoom(zoom);
        int w = jScrollPane1.getWidth();
        int h = jScrollPane1.getHeight();
        this.remove(jScrollPane1);
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setPreferredSize(new java.awt.Dimension(w, h));
        jScrollPane1.setViewportView(pnlLote);
        jScrollPane1.setColumnHeaderView(columnView);
        jScrollPane1.setRowHeaderView(rowView);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        repaint();
        getContentPane().repaint();
        pack();
    }
}//GEN-LAST:event_jSlider1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        archivo = new JFileChooser(FV.getDirectorio());
        /*FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Configuraciones (.cfg)", "cfg");
        archivo.addChoosableFileFilter(filtro);
        filtro = new FileNameExtensionFilter("Archivos de Flips (.flp)", "flp");
        archivo.addChoosableFileFilter(filtro);*/
        if (archivo.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
            FV.setDirectorio(archivo.getSelectedFile().getParent());
            String n = archivo.getSelectedFile().getAbsolutePath();
            //leerResultados(n);
            if (n.substring(n.lastIndexOf("."), n.length()).equals(".flp")) {
                leerResultadosFlips(n);
            } else if (n.substring(n.lastIndexOf("."), n.length()).equals(".cfg")) {
                leerResultadosConfiguraciones(n);
            } else {
                JOptionPane.showMessageDialog(null, "Error in extension file: .flp or .cfg" , "Read file", JOptionPane.ERROR_MESSAGE);
            }
            /*FileOutputStream out = null;

            URL url = this.getClass().getClassLoader().getResource(propertyFileName);
            try {
                out = new FileOutputStream(url.getPath());
                properties.put("dir", archivo.getSelectedFile().getParent());
                properties.store(out, null);
                directorio = properties.getProperty("dir");
            } catch (IOException ex) {
                Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                System.out.print("No se encuentra el archivo para guardar la direcci�n de los datos" + ex.getMessage());
            } finally {
                String n = archivo.getSelectedFile().getAbsolutePath();
                if (n.substring(n.lastIndexOf("."), n.length()).equals(".flp")) {
                    leerResultadosFlips(n);
                } else if (n.substring(n.lastIndexOf("."), n.length()).equals(".cfg")) {
                    leerResultadosConfiguraciones(n);
                }
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }*/
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        pnlLote.guardarImagen(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        FV.cargarDesdeLote(new Configuracion(pnlLote.getPuntos(), pnlLote.getAristas()));
    }//GEN-LAST:event_jButton3ActionPerformed

    public void leerPuntos(String n) {
        BufferedReader inFile;
        int pNoAgregados = 0;
        Punto p;
        try {
            inFile = new BufferedReader(new FileReader(n));
            String s = new String();
            pnlLote.resetPuntos();
            try {
                while ((s = inFile.readLine()) != null) {
                    String[] punto = s.split(" ");
                    p = new Punto(Math.round(Float.parseFloat(punto[0])), Math.round(Float.parseFloat(punto[1])));
                    p.setVReal(punto[0] + "," + punto[1]);
                    if ((p.getX() < 10000) && (-p.getY() < 10000)) {
                        pnlLote.addPunto(p);
                    } else {
                        pNoAgregados++;
                    }
                }

                pnlLote.repaint();

                jButton1.setEnabled(true);
                inFile.close();//

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo leer el archivo: " + ex.getMessage(), "Cargar lote", JOptionPane.ERROR_MESSAGE);
            }

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo leer el archivo: " + ex.getMessage(), "Cargar lote", JOptionPane.ERROR_MESSAGE);
        }
        if (pNoAgregados > 0) {
            JOptionPane.showMessageDialog(null, "No added " + pNoAgregados + " points", "Add Points", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnFin;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnMovimiento;
    private javax.swing.JButton btnRetroceder;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JButton jbtCargarPuntos;
    private javax.swing.JPanel panelBotones;
    private visLotes.PanelLotes pnlLote;
    private visLotes.TituloPrueba pnlTitulo;
    private javax.swing.JTextField txtMovimiento;
    // End of variables declaration//GEN-END:variables

    private void leerResultadosFlips(String n) {
        BufferedReader inFile;
        ArrayList<Arista> aristas = new ArrayList<Arista>();
        Arista ar = new Arista(-1, -1);
        String s = new String();
        try {//TITULO
            inFile = new BufferedReader(new FileReader(n));
            pnlTitulo.setTitulo("Flips Visualization");//cargarTitulo(titulo);
            pnlTitulo.setArchivo(archivo.getSelectedFile().getAbsolutePath());//cargarTitulo(titulo);
            //FIN TITULO
            //PSEUDO TRIANGULACION INICIAL
            s = inFile.readLine();
            Matcher m = Pattern.compile("[0-9]+").matcher(s);

            while (m.find()) {
                ar = new Arista(-1, -1);
                ar.setPosPo(Integer.parseInt(m.group()));
                if (m.find()) {
                    ar.setPosPd(Integer.parseInt(m.group()));
                } else {
                    System.out.println("Error in point x: " + ar.getPosPo());
                    break;
                }
                aristas.add(ar);
            }

            pnlLote.addInicial((ArrayList<Arista>) aristas.clone());
            //FIN PSEUDO TRIANGULACION INICIAL

            while ((s = inFile.readLine()) != null) {
                aristas = new ArrayList<Arista>();
                String[] flip = s.split("\\} \\{");
                m = Pattern.compile("[0-9]+").matcher(flip[0]);
                while (m.find()) {
                    ar = new Arista(-1, -1);
                    ar.setPosPo(Integer.parseInt(m.group()));
                    if (m.find()) {
                        ar.setPosPd(Integer.parseInt(m.group()));
                    } else {
                        System.out.println("Error in point x: " + ar.getPosPo());
                        break;
                    }
                    aristas.add(ar);
                }
                pnlLote.addOutAristas(aristas);

                aristas = new ArrayList<Arista>();
                m = Pattern.compile("[0-9]+").matcher(flip[1]);
                while (m.find()) {
                    ar = new Arista(-1, -1);
                    ar.setPosPo(Integer.parseInt(m.group()));
                    if (m.find()) {
                        ar.setPosPd(Integer.parseInt(m.group()));
                    } else {
                        System.out.println("Error in point x: " + ar.getPosPo());
                        break;
                    }
                    aristas.add(ar);
                }
                pnlLote.addInAristas(aristas);
            }

            txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
            pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
            pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
            habilitarControles(true);
            pnlLote.repaint();

        } catch (IOException ex) {
            Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void leerResultadosConfiguraciones(String n) {
        BufferedReader inFile;
        Matcher m;
        ArrayList<Arista> aristas = new ArrayList<Arista>();
        Arista ar = new Arista(-1, -1);
        String s = new String();
        try {//TITULO
            inFile = new BufferedReader(new FileReader(n));
            pnlTitulo.setTitulo("Configurations Visualization");//cargarTitulo(titulo);
            pnlTitulo.setArchivo(archivo.getSelectedFile().getAbsolutePath());//cargarTitulo(titulo);
            pnlLote.resetPoligonos();
            while ((s = inFile.readLine()) != null) {
                m = Pattern.compile("[0-9]+").matcher(s);
                aristas = new ArrayList<Arista>();
                while (m.find()) {
                    ar = new Arista(-1, -1);
                    ar.setPosPo(Integer.parseInt(m.group()));
                    if (m.find()) {
                        ar.setPosPd(Integer.parseInt(m.group()));
                    } else {
                        System.out.println("Error in point x: " + ar.getPosPo());
                        break;
                    }
                    aristas.add(ar);
                }
                pnlLote.addPoligono((ArrayList<Arista>) aristas.clone());
            }

            pnlLote.mostarPoligono();

            txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
            pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
            pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
            habilitarControles(true);
            pnlLote.repaint();

        } catch (IOException ex) {
            Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void habilitarControles(boolean b) {
        btnInicio.setEnabled(b);
        btnRetroceder.setEnabled(b);
        btnAvanzar.setEnabled(b);
        btnFin.setEnabled(b);
        //jLabel1.setEnabled(b);
        txtMovimiento.setEnabled(b);
        btnMovimiento.setEnabled(b);
    }

    private void leerResultados(String n) {
        BufferedReader inFile;
        ArrayList<Arista> aristas = new ArrayList<Arista>();
        Arista ar = new Arista(-1, -1);
        String s = new String();
        try {//TITULO
            inFile = new BufferedReader(new FileReader(n));
            pnlTitulo.setTitulo("Configurations Visualization");//cargarTitulo(titulo);
            pnlTitulo.setArchivo(archivo.getSelectedFile().getAbsolutePath());//cargarTitulo(titulo);
            //FIN TITULO
            //PSEUDO TRIANGULACION INICIAL
            s = inFile.readLine();
            Matcher m = Pattern.compile("[0-9]+").matcher(s);

            while (m.find()) {//Configuraci�n inicial
                ar = new Arista(-1, -1);
                ar.setPosPo(Integer.parseInt(m.group()));
                if (m.find()) {
                    ar.setPosPd(Integer.parseInt(m.group()));
                } else {
                    System.out.println("Error in point x: " + ar.getPosPo());
                    break;
                }
                aristas.add(ar);
            }
            
            s = inFile.readLine();
            
            if (s != null){
                if (s.startsWith("{")){
                    pnlLote.addInicial((ArrayList<Arista>) aristas.clone());

                    do {
                        aristas = new ArrayList<Arista>();
                        String[] flip = s.split("\\} \\{");
                        m = Pattern.compile("[0-9]+").matcher(flip[0]);
                        while (m.find()) {
                            ar = new Arista(-1, -1);
                            ar.setPosPo(Integer.parseInt(m.group()));
                            if (m.find()) {
                                ar.setPosPd(Integer.parseInt(m.group()));
                            } else {
                                System.out.println("Error in point x: " + ar.getPosPo());
                                break;
                            }
                            aristas.add(ar);
                        }
                        pnlLote.addOutAristas(aristas);

                        aristas = new ArrayList<Arista>();
                        m = Pattern.compile("[0-9]+").matcher(flip[1]);
                        while (m.find()) {
                            ar = new Arista(-1, -1);
                            ar.setPosPo(Integer.parseInt(m.group()));
                            if (m.find()) {
                                ar.setPosPd(Integer.parseInt(m.group()));
                            } else {
                                System.out.println("Error in point x: " + ar.getPosPo());
                                break;
                            }
                            aristas.add(ar);
                        }
                        pnlLote.addInAristas(aristas);
                    } while ((s = inFile.readLine()) != null);
                } else if (s.startsWith("(")){
                    pnlLote.resetPoligonos();
                    
                    pnlLote.addPoligono((ArrayList<Arista>) aristas.clone());
                    do {
                        m = Pattern.compile("[0-9]+").matcher(s);
                        aristas = new ArrayList<Arista>();
                        while (m.find()) {
                            ar = new Arista(-1, -1);
                            ar.setPosPo(Integer.parseInt(m.group()));
                            if (m.find()) {
                                ar.setPosPd(Integer.parseInt(m.group()));
                            } else {
                                System.out.println("Error in point x: " + ar.getPosPo());
                                break;
                            }
                            aristas.add(ar);
                        }
                        pnlLote.addPoligono((ArrayList<Arista>) aristas.clone());
                    } while ((s = inFile.readLine()) != null);

                    pnlLote.mostarPoligono();
                }
                txtMovimiento.setText(String.valueOf(pnlLote.getPos()));
                pnlTitulo.setDilacion(String.valueOf(pnlLote.getDilacion()));
                pnlTitulo.setPeso(String.valueOf(pnlLote.getPeso()));
                habilitarControles(true);
                pnlLote.repaint();
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*leerResultadosFlips(n);
        leerResultadosConfiguraciones(n);*/
    }
}