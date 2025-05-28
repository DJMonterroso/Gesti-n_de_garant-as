import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

public class Gestion_de_garantias {
    private static final Scanner sc = new Scanner(System.in);

    private static final Queue<Computadora> colaRecepcion = new LinkedList<>();
    private static final Queue<Computadora> colaInspeccion = new LinkedList<>();
    private static final Queue<Computadora> colaReparacion = new LinkedList<>();
    private static final Queue<Computadora> colaCalidad = new LinkedList<>();
    private static final Queue<Computadora> colaEntrega = new LinkedList<>();
    private static final List<Computadora> entregadas = new ArrayList<>();

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String ARCHIVO_DATOS = "datos_garantias.ser";

    public static void main(String[] args) {
        cargarDatos();
        limpiarPantalla();
        esperarEnter();
        int opcion;

        do {
            limpiarPantalla();
            mostrarMenu();
            opcion = leerEntero();
            switch (opcion) {
                case 1 -> {
                    if (confirmarAccion("¿Desea registrar una nueva computadora?")) {
                        do {
                            limpiarPantalla();
                            registrarComputadora();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea registrar otra computadora?"));
                        guardarDatos();
                    }
                }
                case 2 -> {
                    if (confirmarAccion("¿Desea procesar computadoras de Recepción a Inspección?")) {
                        do {
                            limpiarPantalla();
                            procesarRecepcion();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea procesar otra computadora en Recepción?"));
                        guardarDatos();
                    }
                }
                case 3 -> {
                    if (confirmarAccion("¿Desea procesar computadoras de Inspección a Reparación?")) {
                        do {
                            limpiarPantalla();
                            procesarInspeccion();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea procesar otra computadora en Inspección?"));
                        guardarDatos();
                    }
                }
                case 4 -> {
                    if (confirmarAccion("¿Desea procesar computadoras de Reparación a Control de Calidad?")) {
                        do {
                            limpiarPantalla();
                            procesarReparacion();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea procesar otra computadora en Reparación?"));
                        guardarDatos();
                    }
                }
                case 5 -> {
                    if (confirmarAccion("¿Desea procesar computadoras de Control de Calidad a Entrega?")) {
                        do {
                            limpiarPantalla();
                            procesarCalidad();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea procesar otra computadora en Control de Calidad?"));
                        guardarDatos();
                    }
                }
                case 6 -> {
                    if (confirmarAccion("¿Desea marcar una computadora como entregada?")) {
                        do {
                            limpiarPantalla();
                            marcarEntregada();
                            esperarEnter();
                        } while (preguntarRepetir("¿Desea marcar otra computadora como entregada?"));
                        guardarDatos();
                    }
                }
                case 7 -> {
                    limpiarPantalla();
                    ManejadorDeArchivos.verHistorial();
                    esperarEnter();
                }
                case 8 -> {
                    limpiarPantalla();
                    mostrarEstadoColas();
                    esperarEnter();
                }
                case 9 -> {
                    limpiarPantalla();
                    System.out.println("Saliendo... Hasta luego!");
                }
                default -> {
                    System.out.println("Opción inválida.");
                    esperarEnter();
                }
            }
        } while (opcion != 9);

        guardarDatos();
    }


        private static void mostrarMenu() {
        System.out.println("=== Sistema de Gestión de Garantías ===");
        System.out.println("1. Registrar nueva computadora");
        System.out.println("2. Procesar: Recepción -> Inspección");
        System.out.println("3. Procesar: Inspección -> Reparación");
        System.out.println("4. Procesar: Reparación -> Control de Calidad");
        System.out.println("5. Procesar: Control de Calidad -> Entrega");
        System.out.println("6. Marcar entrega de computadora");
        System.out.println("7. Mostrar el historial completo");
        System.out.println("8. Mostrar el estado de colas y entregadas");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

        private static boolean confirmarAccion(String mensaje) {
        String respuesta;
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            respuesta = sc.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) return true;
            if (respuesta.equals("n")) return false;
            System.out.println("Respuesta inválida. Ingrese S o N.");
        }
    }

    

    private static void registrarComputadora() {
        System.out.println("-- Registro de Computadora --");
        String tag = leerTextoNoVacio("ServiceTag (letras y números, max 10): ");
        String desc = leerDescripcionProblema();
        String fecha = leerFecha();
        String nombre = leerNombreCliente();
        String correo = leerCorreo();
        String telefono = leerTelefono();
        Computadora comp = new Computadora(tag, desc, fecha, nombre, correo, telefono);
        colaRecepcion.add(comp);
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora registrada: " + tag);
    }

    private static void procesarRecepcion() {
        System.out.println("-- Procesar Recepción --");
        if (colaRecepcion.isEmpty()) {
            System.out.println("No hay computadoras en Recepción.");
            return;
        }
        System.out.println("En Recepción:");
        for (Computadora c : colaRecepcion) System.out.println(c);
        String tag = leerTextoNoVacio("ServiceTag a procesar: ");
        Optional<Computadora> op = colaRecepcion.stream().filter(c -> c.getServiceTag().equals(tag)).findFirst();
        if (op.isEmpty()) {
            System.out.println("ServiceTag no encontrado.");
            return;
        }
        Computadora comp = op.get();
        colaRecepcion.remove(comp);
        comp.addHistory("Inspección iniciada: " + LocalDate.now().format(FORMATO));
        colaInspeccion.add(comp);
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora " + tag + " en Inspección.");
    }

    private static void procesarInspeccion() {
        System.out.println("-- Procesar Inspección --");
        if (colaInspeccion.isEmpty()) {
            System.out.println("No hay computadoras en Inspección.");
            return;
        }
        System.out.println("En Inspección:");
        for (Computadora c : colaInspeccion) System.out.println(c);
        String tag = leerTextoNoVacio("ServiceTag a procesar: ");
        Optional<Computadora> op = colaInspeccion.stream().filter(c -> c.getServiceTag().equals(tag)).findFirst();
        if (op.isEmpty()) {
            System.out.println("ServiceTag no encontrado.");
            return;
        }
        Computadora comp = op.get();
        String detalle = leerTextoNoVacio("Detalle de inspección: ");
        comp.addHistory("Inspección: " + detalle + " (" + LocalDate.now().format(FORMATO) + ")");
        colaInspeccion.remove(comp);
        colaReparacion.add(comp);
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora " + tag + " en Reparación.");
    }

    private static void procesarReparacion() {
        System.out.println("-- Procesar Reparación --");
        if (colaReparacion.isEmpty()) {
            System.out.println("No hay computadoras en Reparación.");
            return;
        }
        System.out.println("En Reparación:");
        for (Computadora c : colaReparacion) System.out.println(c);
        String tag = leerTextoNoVacio("ServiceTag a procesar: ");
        Optional<Computadora> op = colaReparacion.stream().filter(c -> c.getServiceTag().equals(tag)).findFirst();
        if (op.isEmpty()) {
            System.out.println("ServiceTag no encontrado.");
            return;
        }
        Computadora comp = op.get();
        String tec = leerTextoNoVacio("Técnico asignado: ");
        String proc = leerTextoNoVacio("Detalle proceso: ");
        comp.addHistory("Reparación: " + tec + " - " + proc + " (" + LocalDate.now().format(FORMATO) + ")");
        colaReparacion.remove(comp);
        colaCalidad.add(comp);
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora " + tag + " en Control de Calidad.");
    }

    private static void procesarCalidad() {
        System.out.println("-- Control de Calidad --");
        if (colaCalidad.isEmpty()) {
            System.out.println("No hay computadoras en Control de Calidad.");
            return;
        }
        System.out.println("En Control de Calidad:");
        for (Computadora c : colaCalidad) System.out.println(c);
        String tag = leerTextoNoVacio("ServiceTag a procesar: ");
        Optional<Computadora> op = colaCalidad.stream().filter(c -> c.getServiceTag().equals(tag)).findFirst();
        if (op.isEmpty()) {
            System.out.println("ServiceTag no encontrado.");
            return;
        }
        Computadora comp = op.get();
        String resp = leerTextoNoVacio("¿Aprobado? (S/N): ").toLowerCase();
        if (resp.equals("s")) {
            comp.addHistory("Control Calidad: APROBADO (" + LocalDate.now().format(FORMATO) + ")");
            colaCalidad.remove(comp);
            colaEntrega.add(comp);
        } else {
            comp.addHistory("Control Calidad: RECHAZADO (" + LocalDate.now().format(FORMATO) + ")");
            colaCalidad.remove(comp);
            colaReparacion.add(comp);
        }
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora " + tag + " procesada en Calidad.");
    }

    private static void marcarEntregada() {
        System.out.println("-- Marcar Entrega --");
        if (colaEntrega.isEmpty()) {
            System.out.println("No hay computadoras para entrega.");
            return;
        }
        System.out.println("En Entrega:");
        for (Computadora c : colaEntrega) System.out.println(c);
        String tag = leerTextoNoVacio("ServiceTag entregado: ");
        Optional<Computadora> op = colaEntrega.stream().filter(c -> c.getServiceTag().equals(tag)).findFirst();
        if (op.isEmpty()) {
            System.out.println("ServiceTag no encontrado.");
            return;
        }
        Computadora comp = op.get();
        comp.addHistory("Entregado: " + LocalDate.now().format(FORMATO));
        colaEntrega.remove(comp);
        entregadas.add(comp);
        ManejadorDeArchivos.guardarHistorial(comp);
        System.out.println("Computadora " + tag + " entregada.");
    }

    private static void mostrarEstadoColas() {
        System.out.println("Recepción: " + colaRecepcion.size());
        System.out.println("Inspección: " + colaInspeccion.size());
        System.out.println("Reparación: " + colaReparacion.size());
        System.out.println("Control de Calidad: " + colaCalidad.size());
        System.out.println("Entrega Pendiente: " + colaEntrega.size());
        System.out.println("Entregadas: " + entregadas.size());
    }

    private static String leerTextoNoVacio(String mensaje) {
        String entrada;
        do {
            System.out.print(mensaje);
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Este campo no puede quedar vacío.");
            }
        } while (entrada.isEmpty());
        return entrada;
    }

    private static int leerEntero() {
        while (true) {
            try {
                String input = leerTextoNoVacio("");
                int val = Integer.parseInt(input);
                if (val < 0) {
                    System.out.print("No se permiten números negativos. Ingrese un número válido: ");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    private static String leerCorreo() {
        String correo;
        do {
            correo = leerTextoNoVacio("Email del Cliente: ");
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Correo inválido.");
                correo = "";
            }
        } while (correo.isEmpty());
        return correo;
    }

    private static String leerTelefono() {
    String telefono;
    do {
        telefono = leerTextoNoVacio("Teléfono del Cliente (8 dígitos): ");
        if (!telefono.matches("^[0-9]{8}$")) {
            System.out.println("Teléfono inválido. Debe contener exactamente 8 dígitos.");
            telefono = "";
        }
    } while (telefono.isEmpty());
    return telefono;
}


    private static String leerNombreCliente() {
        String nombre;
        do {
            nombre = leerTextoNoVacio("Nombre del Cliente: ");
            if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                System.out.println("Nombre inválido. Solo letras y espacios.");
                nombre = "";
            }
        } while (nombre.isEmpty());
        return nombre;
    }

    private static String leerDescripcionProblema() {
        String desc;
        do {
            desc = leerTextoNoVacio("Descripción del Problema: ");
            if (desc.matches("^-?\\d+$") || desc.matches("^[\\p{Punct}]+$") || !desc.matches(".*[\\p{L}0-9].*")) {
                System.out.println("La descripción debe contener texto válido.");
                desc = "";
            }
        } while (desc.isEmpty());
        return desc;
    }

    private static String leerFecha() {
        String fecha;
        do {
            fecha = leerTextoNoVacio("Fecha de Recepción (dd-MM-yyyy): ");
            try {
                LocalDate.parse(fecha, FORMATO);
                return fecha;
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use dd-MM-yyyy.");
                fecha = "";
            }
        } while (fecha.isEmpty());
        return fecha;
    }

    private static boolean preguntarRepetir(String mensaje) {
        String respuesta;
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            respuesta = sc.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) return true;
            if (respuesta.equals("n")) return false;
            System.out.println("Respuesta inválida. Ingrese S o N.");
        }
    }

    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void esperarEnter() {
        System.out.println("\nPresione Enter para continuar...");
        sc.nextLine();
    }

    private static void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            out.writeObject(new ArrayList<>(colaRecepcion));
            out.writeObject(new ArrayList<>(colaInspeccion));
            out.writeObject(new ArrayList<>(colaReparacion));
            out.writeObject(new ArrayList<>(colaCalidad));
            out.writeObject(new ArrayList<>(colaEntrega));
            out.writeObject(entregadas);
        } catch (IOException e) {
            System.err.println("Error guardando datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            colaRecepcion.addAll((List<Computadora>) in.readObject());
            colaInspeccion.addAll((List<Computadora>) in.readObject());
            colaReparacion.addAll((List<Computadora>) in.readObject());
            colaCalidad.addAll((List<Computadora>) in.readObject());
            colaEntrega.addAll((List<Computadora>) in.readObject());
            entregadas.addAll((List<Computadora>) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando datos: " + e.getMessage());
        }
    }
}

class Computadora implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String serviceTag, descripcionProblema, fechaRecepcion;
    private final String nombreCliente, correoCliente, telefonoCliente;
    private final List<String> historial = new ArrayList<>();

    public Computadora(String tag, String desc, String fecha, String nombre, String correo, String telefono) {
        this.serviceTag = tag;
        this.descripcionProblema = desc;
        this.fechaRecepcion = fecha;
        this.nombreCliente = nombre;
        this.correoCliente = correo;
        this.telefonoCliente = telefono;
        addHistory("Recepción: " + fecha);
    }

    public String getServiceTag() { return serviceTag; }
    public List<String> getHistory() { return historial; }
    public void addHistory(String evento) { historial.add(evento); }
    public String getNombreCliente() { return nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public String getDescripcionProblema() {
    return descripcionProblema;
}

public String getFechaRecepcion() {
    return fechaRecepcion;
}


    @Override
    public String toString() {
        return String.format("[TAG:%s] %s (%s) Cliente:%s [%s, %s]", serviceTag, descripcionProblema, fechaRecepcion, nombreCliente, correoCliente, telefonoCliente);
    }
}

class ManejadorDeArchivos {
    private static final String ARCHIVO = "historial.txt";

    public static void guardarHistorial(Computadora comp) {
    try (PrintWriter pw = new PrintWriter(new FileWriter("historial.txt", true))) {
        pw.println("--- SERVICE TAG | " + comp.getServiceTag() + " ---");
        pw.println("Cliente: " + comp.getNombreCliente());
        pw.println("Correo: " + comp.getCorreoCliente());
        pw.println("Teléfono: " + comp.getTelefonoCliente());
        pw.println("Fecha Recepción: " + comp.getFechaRecepcion());
        pw.println("Descripción del Problema: " + comp.getDescripcionProblema());
        for (String e : comp.getHistory()) {
            pw.println(e);
        }
        pw.println();
    } catch (IOException e) {
        System.err.println("Error guardando historial: " + e.getMessage());
    }
}


    public static void verHistorial() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) System.out.println(linea);
        } catch (FileNotFoundException e) {
            System.out.println("No existe historial.");
        } catch (IOException e) {
            System.err.println("Error leyendo historial: " + e.getMessage());
        }
    }
}
