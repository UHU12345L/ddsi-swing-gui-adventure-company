package Modelo;

import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Laura
 */
public class TurnoDAO {
    //lista turnos por capacidad
    public List<Turno> listaTurnosPorCapacidad(Session sesion){
        return sesion.createQuery("SELECT t FROM Turno t ORDER BY t.capacidad DESC", Turno.class).getResultList();
    }
}
