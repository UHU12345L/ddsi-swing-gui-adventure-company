package Controlador;

import Config.HibernateUtil;
import Vista.VistaGestionAventuras;
import Vista.VistaGestionClientes;
import Vista.VistaGestionGuias;
import Vista.VistaInicio;
import Vista.VistaPrincipal;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.hibernate.SessionFactory;

public class ControladorPrincipal implements ActionListener {

    private final SessionFactory sessionFactory;   // se usará en las partes siguientes
    private final VistaPrincipal vPrincipal;
    private final VistaInicio vInicio;
    private final VistaGestionAventuras vAventura;
    private final VistaGestionClientes vCliente;
    private final VistaGestionGuias vGuia;
    private CardLayout cardLayout;

    /**
     * Crea la ventana principal con sus paneles y la muestra.
     * @param sessionFactory la SessionFactory creada en ControladorConexion
     */
    public ControladorPrincipal(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        vPrincipal = new VistaPrincipal();
        vInicio = new VistaInicio();
        vAventura = new VistaGestionAventuras();
        vCliente = new VistaGestionClientes();
        vGuia = new VistaGestionGuias();

        // CardLayout: los paneles, como cartas, en la misma zona de la ventana
        cardLayout = new CardLayout();
        vPrincipal.getContentPane().setLayout(cardLayout);
        vPrincipal.add(vInicio, "Inicio");          // cada panel con su nombre
        vPrincipal.add(vGuia, "Guias");
        vPrincipal.add(vCliente, "Clientes");
        vPrincipal.add(vAventura, "Aventuras");

        addListeners();

        muestraPanel("Inicio");                     // al arrancar, el panel de inicio
        vPrincipal.setSize(800, 600);               // tamaño de la ventana
        vPrincipal.setLocationRelativeTo(null);     // centrada
        vPrincipal.setVisible(true);                // se muestra al final
    }

    /**
     * Registra el controlador en los ítems del menú y en la X de la ventana.
     */
    private void addListeners() {
        vPrincipal.jMenuItemPaginaPrincipal.addActionListener(this);
        vPrincipal.jMenuItemGestionAventuras.addActionListener(this);
        vPrincipal.jMenuItemGestionClientes.addActionListener(this);
        vPrincipal.jMenuItemGestionGuias.addActionListener(this);
        vPrincipal.jMenuItemSalirAplicacion.addActionListener(this);

        // La X de la ventana hace lo mismo que el menú Salir
        vPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {   // se ejecuta al pulsar la X
                salirAplicacion();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "VolverPaginaPrincipal":
                muestraPanel("Inicio");
                break;
            case "GestionAventuras":
                muestraPanel("Aventuras");
                break;
            case "GestionClientes":
                muestraPanel("Clientes");
                break;
            case "GestionGuias":
                muestraPanel("Guias");
                break;
            case "SalirAplicacion":
                salirAplicacion();
                break;
        }
    }

    /**
     * Muestra el panel indicado; el anterior deja de verse.
     * @param nombrePanel nombre con el que se añadió el panel ("Inicio", "Clientes"...)
     */
    private void muestraPanel(String nombrePanel) {
        cardLayout.show(vPrincipal.getContentPane(), nombrePanel);
    }

    /**
     * Cierra la SessionFactory, la ventana y el programa.
     */
    private void salirAplicacion() {
        HibernateUtil.close();   // libera la conexión con la BD
        vPrincipal.dispose();
        System.exit(0);
    }
}