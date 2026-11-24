package Vista;

import Modelo.Cliente;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Clase de utilidad con los métodos que gestionan la tabla (JTable) de clientes.
 * @author Laura
 */
public class GestionTablasCliente {

    // El modelo guarda los datos de la tabla; al cambiarlo, la tabla se actualiza sola
    private static DefaultTableModel modeloTablaClientes;

    // Formato con el que se muestra la fecha de nacimiento en la tabla
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Crea el modelo de la tabla y se lo asigna al JTable de la vista.
     * Se llama una sola vez, desde el constructor del controlador.
     * @param vClientes vista donde está el JTable de clientes
     */
    public static void inicializarTablaClientes(VistaGestionClientes vClientes) {
        modeloTablaClientes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {   // se sobrescribe para que
                return false;                                       // ninguna celda sea editable
            }
        };
        vClientes.jTableClientes.setModel(modeloTablaClientes);    // se enlazan tabla y modelo
    }

    /**
     * Configura la tabla: cabeceras, ancho de columnas y tipo de selección.
     * @param vClientes vista donde está el JTable de clientes
     */
    public static void dibujarTablaClientes(VistaGestionClientes vClientes) {
        String[] columnasTabla = {"Cliente", "Nombre", "Apellidos", "Teléfono",
            "Fecha de Nacimiento", "Email", "Idioma", "Categoría"};
        modeloTablaClientes.setColumnIdentifiers(columnasTabla);   // nombres de las cabeceras (lo hace el modelo)

        // Ajusta el ancho de las columnas al JTable cuando el tamaño es algo menor
        vClientes.jTableClientes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Evita que el usuario cambie el ancho de las columnas con el ratón
        vClientes.jTableClientes.getTableHeader().setResizingAllowed(false);
        // Solo se puede seleccionar un cliente cada vez
        vClientes.jTableClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] anchuras = {60, 110, 170, 100, 130, 230, 60, 100};
        // Se obtiene el modelo de columnas del JTable (lo hace la tabla, no el modelo de datos)
        TableColumnModel modeloColumna = vClientes.jTableClientes.getColumnModel();
        for (int i = 0; i < anchuras.length; i++) {
            TableColumn columna = modeloColumna.getColumn(i);   // columna en la posición i
            int ancho = anchuras[i];
            columna.setMinWidth(ancho);         // ancho mínimo: la columna nunca será menor
            columna.setPreferredWidth(ancho);   // ancho preferido: el que intentará mantener
        }
    }

    /**
     * Borra todas las filas de la tabla.
     */
    public static void vaciarTablaClientes() {
        while (modeloTablaClientes.getRowCount() > 0) {   // getRowCount(): filas que quedan
            modeloTablaClientes.removeRow(0);              // se borra siempre la primera
        }
    }

    /**
     * Añade a la tabla una fila por cada cliente de la lista.
     * @param clientes lista de clientes obtenida de la BD
     */
    public static void rellenarTablaClientes(List<Cliente> clientes) {
        Object[] fila = new Object[8];   // una posición por columna
        for (Cliente c : clientes) {
            fila[0] = c.getIdCliente();
            fila[1] = c.getNombre();
            fila[2] = c.getApellidos();
            fila[3] = c.getTelefono();
            if (c.getFechaNacimiento() == null) {
                fila[4] = null;
            } else {
                fila[4] = c.getFechaNacimiento().format(FORMATO_FECHA);   // LocalDate -> "15/04/1990"
            }
            fila[5] = c.getEmail();
            fila[6] = c.getIdiomaPreferido();
            fila[7] = c.getCategoria();
            modeloTablaClientes.addRow(fila);   // addRow copia los valores, así que se puede reutilizar fila
        }
    }
}
