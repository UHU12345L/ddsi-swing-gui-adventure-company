package Vista;

import Modelo.Aventura;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Clase de utilidad con los métodos que gestionan la tabla (JTable) de aventuras.
 * @author Laura
 */
public class GestionTablasAventura {

    private static DefaultTableModel modeloTablaAventuras;

    /**
     * Crea el modelo de la tabla (celdas no editables) y se lo asigna al JTable.
     * @param vAventuras vista donde está el JTable de aventuras
     */
    public static void inicializarTablaAventuras(VistaGestionAventuras vAventuras) {
        modeloTablaAventuras = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vAventuras.jTableAventuras.setModel(modeloTablaAventuras);
    }

    /**
     * Configura la tabla: cabeceras, ancho de columnas y tipo de selección.
     * @param vAventuras vista donde está el JTable de aventuras
     */
    public static void dibujarTablaAventuras(VistaGestionAventuras vAventuras) {
        String[] columnasTabla = {"Aventura", "Nombre", "Descripción", "Tipo",
            "Dificultad", "Duración (min)", "Precio (€)", "Edad Mínima"};
        modeloTablaAventuras.setColumnIdentifiers(columnasTabla);

        vAventuras.jTableAventuras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        vAventuras.jTableAventuras.getTableHeader().setResizingAllowed(false);
        vAventuras.jTableAventuras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] anchuras = {70, 150, 280, 90, 80, 100, 80, 90};
        TableColumnModel modeloColumna = vAventuras.jTableAventuras.getColumnModel();
        for (int i = 0; i < anchuras.length; i++) {
            TableColumn columna = modeloColumna.getColumn(i);
            int ancho = anchuras[i];
            columna.setMinWidth(ancho);
            columna.setPreferredWidth(ancho);
        }
    }

    /**
     * Borra todas las filas de la tabla.
     */
    public static void vaciarTablaAventuras() {
        while (modeloTablaAventuras.getRowCount() > 0) {
            modeloTablaAventuras.removeRow(0);
        }
    }

    /**
     * Añade a la tabla una fila por cada aventura de la lista.
     * @param aventuras lista de aventuras obtenida de la BD
     */
    public static void rellenarTablaAventuras(List<Aventura> aventuras) {
        Object[] fila = new Object[8];
        for (Aventura a : aventuras) {
            fila[0] = a.getIdAventura();
            fila[1] = a.getNombre();
            fila[2] = a.getDescripcion();
            fila[3] = a.getTipo();
            fila[4] = a.getDificultad();
            fila[5] = a.getDuracion();
            fila[6] = a.getPrecio();      // BigDecimal: se muestra tal cual (p. ej. 45.00)
            fila[7] = a.getEdadMinima();
            modeloTablaAventuras.addRow(fila);
        }
    }
}
