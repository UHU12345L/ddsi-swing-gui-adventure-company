package Vista;

import Modelo.Turno;
import java.util.List;

/**
 *
 * @author Laura
 */


public class VistaTurno {

    public void muestraMenu() {
        System.out.println();
        System.out.println("*********** Turnos ***********");
        System.out.println("1. Mostrar turnos ordenados por capacidad");
        System.out.println("2. Volver");
        System.out.print("Opción: ");
    }

    //Recibe del controlador la lista de turnos y la muestra
    public void muestraTurnos(List<Turno> turnos) {
        System.out.println("#Turno" + "\t" + "Capacidad" + "\t" + "Fecha y hora" + "\t" + "Aventura" + "\t" + "Guía");
        for (Turno t : turnos) {
            System.out.println(t.getIdTurno()
                    + "\t" + t.getCapacidad()
                    + "\t" + t.getFechaHora().getDayOfMonth() + "/" + t.getFechaHora().getMonthValue() + "/" + t.getFechaHora().getYear()
                    + " " + t.getFechaHora().getHour() + ":" + t.getFechaHora().getMinute()
                    + "\t" + t.getAventura().getNombre()
                    + "\t" + t.getGuia().getNombre());
        }
    }
}
