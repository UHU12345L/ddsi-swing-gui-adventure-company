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
    //lista todos los guias (para la tabla)
    public List<Guia> listaGuias(Session sesion){
        return sesion.createNamedQuery("Guia.findAll", Guia.class).getResultList();
    }
}
