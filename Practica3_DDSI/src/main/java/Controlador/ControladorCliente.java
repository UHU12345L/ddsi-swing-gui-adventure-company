package Controlador;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Vista.GestionTablasCliente;
import Vista.VistaCliente;
import Vista.VistaGestionClientes;
import Vista.VistaMensajes;
import java.time.LocalDate;
import java.time.Period;
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
public class ControladorCliente {
    private final SessionFactory sessionFactory;
    private final ClienteDAO clienteDAO;
    private final VistaCliente vistaCliente;
    private final VistaMensajes vistaMensajes;
    private final Scanner teclado;
    private final VistaGestionClientes vClientes;   // panel con el JTable de clientes
    
    ControladorCliente(SessionFactory sessionFactory, VistaGestionClientes vClientes){
        this.sessionFactory = sessionFactory;
        this.vClientes = vClientes;
        this.clienteDAO = new ClienteDAO();
        this.vistaCliente = new VistaCliente();
       this.vistaMensajes = new VistaMensajes();
        this.teclado = new Scanner(System.in);
        //menu();   // el menú de consola ya no se usa: ahora todo va por la ventana
        GestionTablasCliente.inicializarTablaClientes(vClientes);   // el modelo se crea una sola vez
    }

    /**
     * Prepara el panel de clientes: configura la tabla y la rellena con los datos de la BD.
     * Se llama cada vez que se abre Gestión de Clientes desde el menú.
     */
    public void InicializarVista() {
        GestionTablasCliente.dibujarTablaClientes(vClientes);
        refrescaTablaClientes();
    }

    /**
     * Pide al DAO todos los clientes y los vuelca en la tabla.
     */
    private void refrescaTablaClientes() {
        Session sesion = null;
        Transaction tr = null;
        try {
            sesion = sessionFactory.openSession();
            tr = sesion.beginTransaction();
            List<Cliente> lClientes = clienteDAO.listaClientes(sesion);
            GestionTablasCliente.vaciarTablaClientes();         // se borran las filas anteriores
            GestionTablasCliente.rellenarTablaClientes(lClientes);
            tr.commit();
        } catch (Exception ex) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            vistaMensajes.Mensaje(null, "Error en la petición de clientes\n" + ex.getMessage(), "AVISO", "error");
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close();
            }
        }
    }
    
    private void menu(){
        
        int opcion;
        do{
            vistaCliente.muestraMenu();
            opcion=Integer.parseInt(teclado.nextLine()); // lee la línea entera (con su Enter) y la pasa a int
            switch(opcion) {
                case 1: altaCliente();
                break;  
                
                case 2:
                    break;
                default:
                    //vistaMensajes.mensajeConsola("Opción no válida");
                    break;
            }
        } while(opcion !=2);
    }
    
    private void altaCliente(){
        //pedir nombre, apellidos, telefono, fecha nacimiento, email, idioma preferido, categoria
        //vistaMensajes.pideDato("Nombre");
        String nombre = teclado.nextLine();
       // vistaMensajes.pideDato("Apellidos");
        String apellidos = teclado.nextLine();
        //vistaMensajes.pideDato("Teléfono (Enter para dejarlo vacío)");
        String telefono = teclado.nextLine();
        if (telefono.isBlank()) {
            telefono = null;
        }
        //vistaMensajes.pideDato("Fecha de nacimiento (dd/mm/aaaa)");
        String textoFecha = teclado.nextLine();
        //vistaMensajes.pideDato("Email (Enter para dejarlo vacío)");
        String email = teclado.nextLine();
        if (email.isBlank()) {
            email = null;   // NULL y no "", por el UNIQUE del email
        }
        //vistaMensajes.pideDato("Idioma preferido (ES, EN, FR, DE)");
        String idioma = teclado.nextLine().toUpperCase();
        //vistaMensajes.pideDato("Categoría (GENERAL, INFANTIL, ESTUDIANTE, SENIOR)");
        String categoria = teclado.nextLine().toUpperCase();

        LocalDate fechaNacimiento;
        
        
        //validaciones: fecha tiene q ser antes de hoy, categoria coherente con edad
        try {
            String[] partes = textoFecha.split("/");   // "15/04/1990" -> {"15", "04", "1990"}
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);
            fechaNacimiento = LocalDate.of(anio, mes, dia);   // falla si la fecha no existe (31/02...)
        } catch (Exception e) {
           // vistaMensajes.mensajeConsola("Fecha no válida. Use el formato dd/mm/aaaa");
            return;
        }

        // R1: estrictamente anterior a hoy
        if (!fechaNacimiento.isBefore(LocalDate.now())) {
           // vistaMensajes.mensajeConsola("La fecha de nacimiento debe ser anterior a la fecha actual (R1)");
            return;
        }
        // R2: categoría coherente con la edad
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (categoria.equals("INFANTIL") && edad > 14) {
           // vistaMensajes.mensajeConsola("La categoría INFANTIL exige 14 años o menos (R2)");
            return;
        }
        if (categoria.equals("SENIOR") && edad < 65) {
           // vistaMensajes.mensajeConsola("La categoría SENIOR exige 65 años o más (R2)");
            return;
        }
        if (!idioma.equals("ES") && !idioma.equals("EN") && !idioma.equals("FR") && !idioma.equals("DE")) {
           // vistaMensajes.mensajeConsola("Idioma no válido");
            return;
        }
        if (!categoria.equals("GENERAL") && !categoria.equals("INFANTIL")
                && !categoria.equals("ESTUDIANTE") && !categoria.equals("SENIOR")) {
           // vistaMensajes.mensajeConsola("Categoría no válida");
            return;
        }
           
        
        //operacion con BD
        Session sesion=null;
        Transaction tr=null;
        
        try{
            sesion=sessionFactory.openSession();
            tr=sesion.beginTransaction();
            
            if(email!=null && clienteDAO.existeEmail(sesion,email)){
                tr.rollback();
              //  vistaMensajes.mensajeConsola("Ya existe un cliente con ese email");
            }else{
                String idCliente=clienteDAO.generaIdCliente(sesion);
                 Cliente cliente = new Cliente(idCliente, nombre, apellidos, telefono,
                        fechaNacimiento, email, idioma, categoria);
                clienteDAO.insertaCliente(sesion, cliente);   // el DAO hace el persist
                tr.commit();
               // vistaMensajes.mensajeConsola("Cliente " + idCliente + " insertado correctamente");
                
            }
            
        }catch(HibernateException e){
            if(tr!=null && tr.isActive()){
                tr.rollback();
            }
           // vistaMensajes.mensajeConsola("Error en la inserción del cliente: " + e.getMessage());
        }finally{
            if(sesion!=null && sesion.isOpen()){
                sesion.close();
            }
        }
    }
}
