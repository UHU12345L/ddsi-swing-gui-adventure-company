package Controlador;

import Modelo.GuiaDAO;
import Vista.VistaGuia;
import Vista.VistaMensajes;
import jakarta.persistence.NoResultException;
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
    
    ControladorGuia(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.guiaDAO = new GuiaDAO();
        this.vistaGuia = new VistaGuia();
        this.vistaMensajes = new VistaMensajes();
        this.teclado = new Scanner(System.in);
        menu();
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
