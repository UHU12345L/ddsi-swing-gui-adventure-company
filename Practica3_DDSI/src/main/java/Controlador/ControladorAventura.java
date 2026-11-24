package Controlador;

import Modelo.Aventura;
import Modelo.AventuraDAO;
import Vista.GestionTablasAventura;
import Vista.VistaGestionAventuras;
import Vista.VistaMensajes;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Laura
 */
public class ControladorAventura {

    private final SessionFactory sessionFactory;
    private final AventuraDAO aventuraDAO;
    private final VistaMensajes vistaMensajes;
    private final VistaGestionAventuras vAventuras;   // panel con el JTable de aventuras

    ControladorAventura(SessionFactory sessionFactory, VistaGestionAventuras vAventuras) {
        this.sessionFactory = sessionFactory;
        this.vAventuras = vAventuras;
        this.aventuraDAO = new AventuraDAO();
        this.vistaMensajes = new VistaMensajes();
        GestionTablasAventura.inicializarTablaAventuras(vAventuras);   // el modelo se crea una sola vez
    }

    /**
     * Prepara el panel de aventuras: configura la tabla y la rellena con los datos de la BD.
     */
    public void InicializarVista() {
        GestionTablasAventura.dibujarTablaAventuras(vAventuras);
        refrescaTablaAventuras();
    }

    /**
     * Pide al DAO todas las aventuras y las vuelca en la tabla.
     */
    private void refrescaTablaAventuras() {
        Session sesion = null;
        Transaction tr = null;
        try {
            sesion = sessionFactory.openSession();
            tr = sesion.beginTransaction();
            List<Aventura> lAventuras = aventuraDAO.listaAventuras(sesion);
            GestionTablasAventura.vaciarTablaAventuras();
            GestionTablasAventura.rellenarTablaAventuras(lAventuras);
            tr.commit();
        } catch (Exception ex) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            vistaMensajes.Mensaje(null, "Error en la petición de aventuras\n" + ex.getMessage(), "AVISO", "error");
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close();
            }
        }
    }
}
