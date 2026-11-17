package Vista;

import java.awt.Component;
import javax.swing.JOptionPane;

public class VistaMensajes {

    /**
     * Muestra un mensaje en una ventana emergente.
     * @param C ventana sobre la que se muestra (puede ser null)
     * @param texto mensaje que se muestra
     * @param titulo título de la ventana del mensaje
     * @param tipoMensaje "info" (información) o "error"
     */
    public void Mensaje(Component C, String texto, String titulo, String tipoMensaje) {
        switch (tipoMensaje) {
            case "info":
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
                break;
            case "error":
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    /**
     * Muestra una ventana de confirmación con Sí, No y Cancelar.
     * @param C ventana sobre la que se muestra (puede ser null)
     * @param texto pregunta que se muestra
     * @return 0 = Sí, 1 = No, 2 = Cancelar, -1 = cerrada sin elegir
     */
    public int Dialogo(Component C, String texto) {
        int opcion = JOptionPane.showConfirmDialog(C, texto, "Atención",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return opcion;
    }
}