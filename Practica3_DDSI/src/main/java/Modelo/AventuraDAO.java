package Modelo;

import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Laura
 */
public class AventuraDAO {
    //lista todas las aventuras (para la tabla)
    public List<Aventura> listaAventuras(Session sesion){
        return sesion.createNamedQuery("Aventura.findAll", Aventura.class).getResultList();
    }
}
