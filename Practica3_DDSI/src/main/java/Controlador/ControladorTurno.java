package Controlador;

import Modelo.Turno;
import Modelo.TurnoDAO;
import Vista.VistaMensajes;
import Vista.VistaTurno;
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
public class ControladorTurno {

    private final SessionFactory sessionFactory;
    private final TurnoDAO turnoDAO;
    private final VistaTurno vistaTurno;
    private final VistaMensajes vistaMensajes;
    private final Scanner teclado;

    ControladorTurno(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.turnoDAO = new TurnoDAO();
        this.vistaTurno = new VistaTurno();
        this.vistaMensajes = new VistaMensajes();
        this.teclado = new Scanner(System.in);
        menu();
    }

    private void menu() {

        int opcion;
        do {
            vistaTurno.muestraMenu();
            opcion = Integer.parseInt(teclado.nextLine()); // lee la línea entera (con su Enter) y la pasa a int
            switch (opcion) {
                case 1:
                    mostrarTurnosPorCapacidad();
                    break;

                case 2:
                    break;
                default:
                   // vistaMensajes.mensajeConsola("Opción no válida");
                    break;
            }
        } while (opcion != 2);
    }

    private void mostrarTurnosPorCapacidad() {
        Session sesion = null;
        Transaction tr = null;

        try {
            sesion = sessionFactory.openSession();
            tr = sesion.beginTransaction();

            List<Turno> turnos = turnoDAO.listaTurnosPorCapacidad(sesion);   // el DAO hace la consulta
            if (turnos.isEmpty()) {
                //vistaMensajes.mensajeConsola("No hay turnos");
            } else {
                vistaTurno.muestraTurnos(turnos);
            }

            tr.commit();

        } catch (HibernateException e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            //vistaMensajes.mensajeConsola("Error al obtener los turnos: " + e.getMessage());
        } finally {
            if (sesion != null && sesion.isOpen()) {
                sesion.close();
            }
        }
    }
}
