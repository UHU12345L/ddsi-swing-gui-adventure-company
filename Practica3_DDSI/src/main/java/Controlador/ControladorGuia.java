package Controlador;

import Modelo.Guia;
import Modelo.GuiaDAO;
import Vista.GestionTablasGuia;
import Vista.VistaGestionGuias;
import Vista.VistaGuia;
import Vista.VistaMensajes;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Laura
 */
public class ControladorGuia {

    private final SessionFactory sessionFactory;
    private final GuiaDAO guiaDAO;
    private final VistaGuia vistaGuia;
    private final VistaMensajes vistaMensajes;
    private final Scanner teclado;    
    private final VistaGestionGuias vGuias;   // panel con el JTable de guías
    
    ControladorGuia(SessionFactory sessionFactory, VistaGestionGuias vGuias){
        this.sessionFactory = sessionFactory;
        this.vGuias = vGuias;
        this.guiaDAO = new GuiaDAO();
        this.vistaGuia = new VistaGuia();
        this.vistaMensajes = new VistaMensajes();
        this.teclado = new Scanner(System.in);
        //menu();   // el menú de consola ya no se usa: ahora todo va por la ventana
        GestionTablasGuia.inicializarTablaGuias(vGuias);   // el modelo se crea una sola vez
    }

    /**
     * Prepara el panel de guías: configura la tabla y la rellena con los datos de la BD.
     */
    public void InicializarVista() {
        GestionTablasGuia.dibujarTablaGuias(vGuias);
        refrescaTablaGuias();
    }

    /**
     * Pide al DAO todos los guías y los vuelca en la tabla.
     */
    private void refrescaTablaGuias() {
        Session sesion = null;
        Transaction tr = null;
        try {
            sesion = sessionFactory.openSession();
            tr = sesion.beginTransaction();
            List<Guia> lGuias = guiaDAO.listaGuias(sesion);
            GestionTablasGuia.vaciarTablaGuias();
            GestionTablasGuia.rellenarTablaGuias(lGuias);
            tr.commit();
        } catch (Exception ex) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            vistaMensajes.Mensaje(null, "Error en la petición de guías\n" + ex.getMessage(), "AVISO", "error");
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close();
            }
        }
    }
    
    private void menu(){
        
        int opcion;
        do{
            vistaGuia.muestraMenu();
            opcion=Integer.parseInt(teclado.nextLine()); // lee la línea entera (con su Enter) y la pasa a int
            switch(opcion) {
                case 1: modificarTelefono();
                break;  
                
                case 2:
                    break;
                default:
                   // vistaMensajes.mensajeConsola("Opción no válida");
                    break;
            }
        } while(opcion !=2);
    }
    
    private void modificarTelefono(){
        //vistaMensajes.pideDato("Correo");
        String email = teclado.nextLine();
        //vistaMensajes.pideDato("Teléfono nuevo");
        String telefono = teclado.nextLine();
        
        if(telefono.isBlank()){
            //vistaMensajes.mensajeConsola("El telefono no puede estar vacio");
            return;
        }
        Session sesion=null;
        Transaction tr=null;
        
        try{
            sesion=sessionFactory.openSession();
            tr=sesion.beginTransaction();
            
            guiaDAO.modificaTelefono(sesion,email,telefono);
            
            tr.commit();
            //vistaMensajes.mensajeConsola("Telefono modificado");
            
        }catch (NoResultException e) {
            // la excepción que pide el enunciado: el email no está en la BD
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
           // vistaMensajes.mensajeConsola("No existe ningún guía con el email " + email);
        } catch (HibernateException e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
           // vistaMensajes.mensajeConsola("Error al modificar el teléfono: " + e.getMessage());
        }finally{
            if(sesion!=null &&sesion.isOpen()){
            sesion.close();
        }
        }
    }
}
