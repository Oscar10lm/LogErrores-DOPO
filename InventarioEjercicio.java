import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase que representa un producto dentro del inventario.
 * @author Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
 * @version 29/04/26
 */
class Producto {
    private int id;
    private String nombre;
    private int cantidad;

    /**
     * Constructor para crear un nuevo producto.
     * @param id El identificador unico del producto.
     * @param nombre El nombre del producto.
     * @param cantidad La cantidad inicial de unidades.
     */
    public Producto(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el identificador del producto.
     * @return El id del producto.
     */
    public int getId() { return id; }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() { return nombre; }

}

/**
 * Clase que maneja las excepciones personalizadas del inventario.
 * @author Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
 * @version 29/04/26
 */
class InventarioException extends Exception {
    public static final String PRODUCT_NOT_FOUND_EXCEPTION = "Producto no encontrado. Se intento buscar o eliminar un producto que no existe.";
    public static final String EMPTY_INVENTORY_EXCEPTION = "Inventario vacio. Se intento eliminar un producto de un inventario vacio.";

    /**
     * Constructor de la excepcion.
     * @param message Mensaje detallado del error.
     */
    public InventarioException(String message) {
        super(message);
    }
}

/**
 * Clase utilitaria para registrar los errores en archivos de texto.
 * @author Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
 * @version 29/04/26
 */
class ErrorLogger {

    /**
     * Registra los errores controlados o esperados en un archivo log.
     * @param mensaje El texto descriptivo del error a registrar.
     */
    public static void logExpectedError(String mensaje) {
        // Abre un flujo de escritura añadiendo al final del archivo para preservar los registros
        try (PrintWriter out = new PrintWriter(new FileWriter("expected_errors_log.txt", true))) {
            out.println("[EXPECTED ERROR]: " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en expected_errors_log.txt");
        }
    }

    /**
     * Registra errores imprevistos del sistema junto con su traza de ejecucion.
     * @param e La excepcion inesperada capturada.
     */
    public static void logUnexpectedError(Exception e) {
        // Se efectua la escritura en el archivo inventario_log.txt imprimiendo tanto el mensaje como la pila de llamadas
        try (PrintWriter out = new PrintWriter(new FileWriter("inventario_log.txt", true))) {
            out.println("[UNEXPECTED ERROR]: " + e.getMessage());
            e.printStackTrace(out);
        } catch (IOException ex) {
            System.err.println("Error al escribir en inventario_log.txt");
        }
    }
}

/**
 * Clase que administra la logica de almacenamiento y gestion de productos.
 * @author Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
 * @version 29/04/26
 */
class Inventario {
    private Map<Integer, Producto> productos;

    /**
     * Inicializa la estructura de datos para contener los productos.
     */
    public Inventario() {
        // Se instancia el contenedor empleando un HashMap con la clave de tipo entero y el valor de tipo Producto
        this.productos = new HashMap<>();
    }

    /**
     * Agrega un nuevo producto a la coleccion del inventario.
     * @param p Objeto de tipo Producto que se desea almacenar.
     */
    public void agregarProducto(Producto p) {
        // Inserta el producto en el mapa utilizando su propio identificador como clave de localizacion
        productos.put(p.getId(), p);
        System.out.println("Producto agregado: " + p.getNombre());
    }

    /**
     * Localiza un producto especifico a traves de su identificador.
     * @param id El codigo numerico asociado al producto buscado.
     * @return El objeto Producto encontrado.
     * @throws InventarioException Si el producto consultado no figura en el registro actual.
     */
    public Producto buscarProducto(int id) throws InventarioException {
        // Verifica la existencia previa de la clave en el contenedor
        if (!productos.containsKey(id)) {
            // Interrumpe la ejecucion y lanza el aviso de no encontrado si el elemento falta
            throw new InventarioException(InventarioException.PRODUCT_NOT_FOUND_EXCEPTION);
        }
        // Retorna el valor mapeado a la clave solicitada
        return productos.get(id);
    }

    /**
     * Remueve un producto del contenedor de inventario.
     * @param id El identificador entero del producto a retirar.
     * @throws InventarioException Si el inventario carece de elementos o el identificador no existe.
     */
    public void eliminarProducto(int id) throws InventarioException {
        // Comprueba si la coleccion esta totalmente limpia para notificar el error de inventario vacio
        if (productos.isEmpty()) {
            throw new InventarioException(InventarioException.EMPTY_INVENTORY_EXCEPTION);
        }
        // Valida que el elemento especifico efectivamente este presente antes de borrarlo
        if (!productos.containsKey(id)) {
            throw new InventarioException(InventarioException.PRODUCT_NOT_FOUND_EXCEPTION);
        }
        // Extrae el elemento del mapa de forma definitiva
        productos.remove(id);
        System.out.println("Producto eliminado con id: " + id);
    }
}

/**
 * Clase principal de ejecucion y pruebas del sistema de inventario.
 * @author Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
 * @version 29/04/26
 */
public class InventarioEjercicio {
    /**
     * Metodo de arranque principal.
     * @param args Argumentos de linea de comandos proporcionados por consola.
     */
    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        // Intento de eliminacion en una estructura sin inicializar datos para forzar el error de contenedor vacio
        try {
            System.out.println("Intentando eliminar producto en inventario vacio...");
            inventario.eliminarProducto(10);
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // Insercion de un registro base para posibilitar el resto de las evaluaciones
        inventario.agregarProducto(new Producto(1, "Teclado", 15));

        // Busqueda de un registro inexistente para capturar la excepcion de elemento no encontrado
        try {
            System.out.println("Intentando buscar un producto que no existe...");
            inventario.buscarProducto(99); 
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // Ejecucion de un flujo esperado exitoso integrando busqueda y eliminacion normal
        try {
            Producto encontrado = inventario.buscarProducto(1);
            System.out.println("Exito. " + encontrado);
            inventario.eliminarProducto(1);
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // Induccion de un fallo critico de calculo matematico para probar el registro imprevisto del sistema
        try {
            System.out.println("Simulando error inesperado del sistema...");
            int error = 1 / 0; 
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        System.out.println("Ejecucion terminada. Revisa los archivos .txt generados en el directorio del proyecto.");
    }
}
