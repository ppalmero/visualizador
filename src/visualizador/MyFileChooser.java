/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizador;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Pablo
 */
class MyFileChooser extends JFileChooser {
    private boolean reemplazar = false;

    public boolean isReemplazar() {
        return reemplazar;
    }

    public JDialog createDialog(Component parent) throws HeadlessException {
        return super.createDialog(parent);
    }

    public MyFileChooser(String currentDirectoryPath, String title, FileNameExtensionFilter ... filtro) {
        super(currentDirectoryPath);
        this.setDialogTitle(title);
        if (filtro != null){
            for (int i = 0; i < filtro.length; i++){
                this.addChoosableFileFilter(filtro[i]);
            }
        }
        this.setDialogType(JFileChooser.SAVE_DIALOG);
        final JDialog dialog = this.createDialog(null);
        this.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = (JFileChooser) evt.getSource();
                if (JFileChooser.APPROVE_SELECTION.equals(evt.getActionCommand())) {
                    File selectedFile = chooser.getSelectedFile();
                    if (selectedFile.exists()) {
                        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "The destination file already exists. Overwrite?", "Destination file exists", JOptionPane.YES_NO_OPTION)) {
                            dialog.dispose();
                        }
                    } else {
                        dialog.dispose();
                    }
                } else if (JFileChooser.CANCEL_SELECTION.equals(evt.getActionCommand())) {
                    dialog.dispose();
                    chooser.setSelectedFile(null);
                }
            }
        });

        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }
    /*public String getFileFilterSelected(){
        return this.fileFilter.extensions[0];
    }*/
            
}
