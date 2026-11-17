package Aplicacion;

import Controlador.ControladorConexion;
import javax.swing.SwingUtilities;

public class Pr3_2627 {

    public static void main(String[] args) {
        /*
         * Se inicia la aplicación creando el controlador de conexión.
         * La interfaz se crea en el hilo de Swing con invokeLater.
         */
        SwingUtilities.invokeLater(() -> new ControladorConexion());
    }
}