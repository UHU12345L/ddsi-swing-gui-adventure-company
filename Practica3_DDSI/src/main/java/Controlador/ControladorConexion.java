package Controlador;

import Config.HibernateUtil;
import Vista.VistaConexion;
import Vista.VistaMensajes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.SessionFactory;

public class ControladorConexion implements ActionListener {   // ActionListener: puede recibir clics

    private final VistaMensajes vMensaje;
    private final VistaConexion vConexion;

    /**
     * Constructor del controlador de conexión: crea y muestra la ventana de acceso.
     */
    public ControladorConexion() {
        vMensaje = new VistaMensajes();
        vConexion = new VistaConexion();
        addListeners();
        // Entrar es el botón por defecto: pulsar Intro equivale a pulsarlo
        vConexion.getRootPane().setDefaultButton(vConexion.jButtonEntrarAplicacion);
        // Se configura y se muestra la ventana (setVisible siempre al final)
        vConexion.pack();                        // tamaño justo para que quepa todo
        vConexion.setLocationRelativeTo(null);   // centrada en la pantalla
        vConexion.setResizable(false);           // no se puede redimensionar
        vConexion.setVisible(true);              // se muestra
    }

    /**
     * Registra este controlador como "escuchador" de los botones de la vista.
     */
    private void addListeners() {
        vConexion.jButtonEntrarAplicacion.addActionListener(this);      // this = este controlador
        vConexion.jButtonSalirDialogoConexion.addActionListener(this);
    }

    /**
     * Se ejecuta automáticamente al pulsar un botón registrado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {   // el actionCommand del botón pulsado
            case "EntrarAplicacion":
                String user = vConexion.jTextFieldUsuario.getText().trim();          // getText(): contenido del campo
                String pass = new String(vConexion.jPasswordField.getPassword());    // getPassword() devuelve char[]
                SessionFactory sessionFactory;
                try {
                    sessionFactory = HibernateUtil.buildSessionFactory(user, pass);
                } catch (Exception ex) {
                    // Credenciales incorrectas o sin red: se muestra la causa y la ventana sigue abierta
                    vMensaje.Mensaje(vConexion, ex.getMessage(), "AVISO", "error");
                    return;
                }
                vMensaje.Mensaje(vConexion, "Conexión correcta con Hibernate\nVa a acceder a la aplicación",
                        "AVISO", "info");
                try {
                    new ControladorPrincipal(sessionFactory);
                    vConexion.dispose();   // la ventana de conexión solo se cierra si la principal se creó bien
                } catch (Exception ex) {
                    HibernateUtil.close();
                    vMensaje.Mensaje(vConexion, "Error al iniciar la aplicación: " + ex.getMessage(),
                            "AVISO", "error");
                }
                break;
            case "SalirDialogoConexion":
                vMensaje.Mensaje(vConexion, "Salida correcta de la aplicación", "AVISO", "info");
                vConexion.dispose();   // cierra la ventana
                System.exit(0);        // termina el programa
                break;
        }
    }
}