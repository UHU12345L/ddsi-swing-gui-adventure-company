package Modelo;

import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Laura
 */
public class ClienteDAO {
    //insertaCliente con persist
    public void insertaCliente(Session sesion, Cliente cliente){
        sesion.persist(cliente);
    }
    //existe email
    public boolean existeEmail(Session sesion, String email){
        List<Cliente> clientes=sesion.createNamedQuery("Cliente.findByEmail", Cliente.class).setParameter("email", email).getResultList();
        return !clientes.isEmpty();
    }
    //genera id cliente
    public String generaIdCliente(Session session){
        String max=session.createQuery("SELECT MAX(c.idCliente) FROM Cliente c", String.class).getSingleResult();
        
        int siguiente;
        if(max==null){
            siguiente=1;
        }else{
            siguiente=Integer.parseInt(max.substring(2))+1;
        }
        return String.format("CL%03d",siguiente);
    }
    //lista todos los clientes (para la tabla)
    public List<Cliente> listaClientes(Session sesion){
        return sesion.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
    }
}
