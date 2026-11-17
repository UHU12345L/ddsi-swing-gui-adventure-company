package Modelo;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Laura
 */
public class GuiaDAO {
    //modificar telefono
    public void modificaTelefono(Session sesion, String email, String telefono){
        Guia guia=sesion.createNamedQuery("Guia.findByEmail", Guia.class).setParameter("email",email).getSingleResult();
        guia.setTelefono(telefono);      
    }
}
