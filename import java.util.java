import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Producto {
    private int id;
    private String nombre;
    private int cantidad;

    public Producto(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    
    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', cantidad=" + cantidad + "}";
    }
}

class InventarioException extends Exception {
    public static final String PRODUCT_NOT_FOUND_EXCEPTION = "Producto no encontrado. Se intento buscar o eliminar un producto que no existe.";
    public static final String EMPTY_INVENTORY_EXCEPTION = "Inventario vacio. Se intento eliminar un producto de un inventario vacio.";

    public InventarioException(String message) {
        super(message);
    }
}

class ErrorLogger {
    
    public static void logExpectedError(String mensaje) {
        try (PrintWriter out = new PrintWriter(new FileWriter("expected_errors_log.txt", true))) {
            out.println("[EXPECTED ERROR]: " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en expected_errors_log.txt");
        }
    }

    // Registra errores inesperados (ej. NullPointerException, ArithmeticException)
    public static void logUnexpectedError(Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter("inventario_log.txt", true))) {
            out.println("[UNEXPECTED ERROR]: " + e.getMessage());
            e.printStackTrace(out); // Imprimir la traza completa para el programador
        } catch (IOException ex) {
            System.err.println("Error al escribir en inventario_log.txt");
        }
    }
}

class Inventario {
    // HashMap donde la clave es el id del producto
    private Map<Integer, Producto> productos;

    public Inventario() {
        this.productos = new HashMap<>();
    }

    public void agregarProducto(Producto p) {
        productos.put(p.getId(), p);
        System.out.println("Producto agregado: " + p.getNombre());
    }

    public Producto buscarProducto(int id) throws InventarioException {
        if (!productos.containsKey(id)) {
            throw new InventarioException(InventarioException.PRODUCT_NOT_FOUND_EXCEPTION);
        }
        return productos.get(id);
    }

    // Se asume parámetro (id : int) dado el funcionamiento del HashMap
    public void eliminarProducto(int id) throws InventarioException {
        if (productos.isEmpty()) {
            throw new InventarioException(InventarioException.EMPTY_INVENTORY_EXCEPTION);
        }
        if (!productos.containsKey(id)) {
            throw new InventarioException(InventarioException.PRODUCT_NOT_FOUND_EXCEPTION);
        }
        productos.remove(id);
        System.out.println("Producto eliminado con id: " + id);
    }
}

public class Main {
    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        // 1. Probar EMPTY_INVENTORY_EXCEPTION (Excepción esperada)
        try {
            System.out.println("Intentando eliminar producto en inventario vacío...");
            inventario.eliminarProducto(10);
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // Agregar un producto para las siguientes pruebas
        inventario.agregarProducto(new Producto(1, "Teclado", 15));

        // 2. Probar PRODUCT_NOT_FOUND_EXCEPTION (Excepción esperada)
        try {
            System.out.println("Intentando buscar un producto que no existe...");
            inventario.buscarProducto(99); 
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // 3. Probar comportamiento exitoso
        try {
            Producto encontrado = inventario.buscarProducto(1);
            System.out.println("Exito. " + encontrado);
            inventario.eliminarProducto(1);
        } catch (InventarioException e) {
            ErrorLogger.logExpectedError(e.getMessage());
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        // 4. Probar Error Inesperado (Ejemplo: División por cero)
        try {
            System.out.println("Simulando error inesperado del sistema...");
            int error = 1 / 0; 
        } catch (Exception e) {
            ErrorLogger.logUnexpectedError(e);
        }

        System.out.println("Ejecucion terminada. Revisa los archivos .txt generados.");
    }
}
