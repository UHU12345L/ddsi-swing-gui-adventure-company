package Vista;

import Modelo.Guia;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Clase de utilidad con los métodos que gestionan la tabla (JTable) de guías.
 * @author Laura
 */
public class GestionTablasGuia {

    private static DefaultTableModel modeloTablaGuias;

    // fechaAlta es java.util.Date (no LocalDate), por eso se formatea con SimpleDateFormat
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Crea el modelo de la tabla (celdas no editables) y se lo asigna al JTable.
     * @param vGuias vista donde está el JTable de guías
     */
    public static void inicializarTablaGuias(VistaGestionGuias vGuias) {
        modeloTablaGuias = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vGuias.jTableGuias.setModel(modeloTablaGuias);
    }

    /**
     * Configura la tabla: cabeceras, ancho de columnas y tipo de selección.
     * @param vGuias vista donde está el JTable de guías
     */
    public static void dibujarTablaGuias(VistaGestionGuias vGuias) {
        String[] columnasTabla = {"Guía", "Nombre", "Apellidos", "Teléfono", "Email", "Fecha de Alta"};
        modeloTablaGuias.setColumnIdentifiers(columnasTabla);

        vGuias.jTableGuias.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        vGuias.jTableGuias.getTableHeader().setResizingAllowed(false);
        vGuias.jTableGuias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] anchuras = {60, 120, 200, 110, 250, 110};
        TableColumnModel modeloColumna = vGuias.jTableGuias.getColumnModel();
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
    public static void vaciarTablaGuias() {
        while (modeloTablaGuias.getRowCount() > 0) {
            modeloTablaGuias.removeRow(0);
        }
    }

    /**
     * Añade a la tabla una fila por cada guía de la lista.
     * @param guias lista de guías obtenida de la BD
     */
    public static void rellenarTablaGuias(List<Guia> guias) {
        Object[] fila = new Object[6];
        for (Guia g : guias) {
            fila[0] = g.getIdGuia();
            fila[1] = g.getNombre();
            fila[2] = g.getApellidos();
            fila[3] = g.getTelefono();
            fila[4] = g.getEmail();
            if (g.getFechaAlta() == null) {
                fila[5] = null;
            } else {
                fila[5] = FORMATO_FECHA.format(g.getFechaAlta());   // Date -> "15/04/2024"
            }
            modeloTablaGuias.addRow(fila);
        }
    }
}
