package org.example.gestionProyectos.menus;

import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;
import org.example.gestionProyectos.services.GestionService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class GestionMenu {

    private final Scanner scanner;
    private final GestionService gestionService;

    public GestionMenu(Scanner scanner) {
        this.scanner = scanner;
        this.gestionService = new GestionService();
    }

    public void mostrar() {

        boolean volver = false;

        while (!volver) {

            System.out.println("\n========================================");
            System.out.println("          GESTIÓN DE SUMINISTROS");
            System.out.println("========================================");
            System.out.println("1. Registrar suministro");
            System.out.println("2. Eliminar suministro");
            System.out.println("3. Modificar suministro");
            System.out.println();
            System.out.println("4. Buscar suministro por ID");
            System.out.println("5. Buscar suministros por proveedor");
            System.out.println("6. Buscar suministros por pieza");
            System.out.println("7. Buscar suministros por proyecto");
            System.out.println();
            System.out.println("8. Mostrar todos los suministros");
            System.out.println();
            System.out.println("0. Volver al menú anterior");
            System.out.println("========================================");

            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    registrarSuministro();
                    break;

                case "2":
                    eliminarSuministro();
                    break;

                case "3":
                    modificarSuministro();
                    break;

                case "4":
                    buscarPorId();
                    break;

                case "5":
                    buscarPorProveedor();
                    break;

                case "6":
                    buscarPorPieza();
                    break;

                case "7":
                    buscarPorProyecto();
                    break;

                case "8":
                    mostrarTodos();
                    break;

                case "0":
                    volver = true;
                    System.out.println("Volviendo al menú anterior...");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        }
    }

    private void registrarSuministro() {

        System.out.println("\n--- REGISTRAR SUMINISTRO ---");

        try {

            System.out.println("\nIntroduce los datos del suministro:");

            System.out.print("Código del proveedor: ");
            String proveedorCodigo = scanner.nextLine();

            System.out.print("Código de la pieza: ");
            String piezaCodigo = scanner.nextLine();

            System.out.print("Código del proyecto: ");
            String proyectoCodigo = scanner.nextLine();

            System.out.print("Fecha (YYYY-MM-DD): ");
            LocalDate fecha = leerFecha();

            System.out.print("Cantidad suministrada: ");
            Integer cantidad = leerCantidad();

            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(proveedorCodigo);

            Pieza pieza = new Pieza();
            pieza.setCodigo(piezaCodigo);

            Proyecto proyecto = new Proyecto();
            proyecto.setCodigo(proyectoCodigo);

            Gestion gestion = new Gestion(fecha, cantidad, proveedor, pieza, proyecto);

            Long id = gestionService.registrar(gestion);

            System.out.println("\nSuministro registrado correctamente.");
            System.out.println("Identificador asignado: " + id);

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("registrar el suministro", e);
        }
    }

    private void eliminarSuministro() {

        System.out.println("\n--- ELIMINAR SUMINISTRO ---");

        try {

            System.out.print("Introduce el ID del suministro: ");
            Long id = leerId();

            Gestion gestion = gestionService.buscarPorId(id);

            if (gestion == null) {
                System.out.println("No existe ningún suministro con ese ID.");
                return;
            }

            imprimirGestion(gestion);

            System.out.print("\n¿Deseas eliminar este suministro? (S/N): ");
            String confirmacion = scanner.nextLine().trim();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean eliminado = gestionService.eliminar(id);

            if (eliminado) {
                System.out.println("Suministro eliminado correctamente.");
            } else {
                System.out.println("No se ha podido encontrar el suministro.");
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("eliminar el suministro", e);
        }
    }

    private void modificarSuministro() {

        System.out.println("\n--- MODIFICAR SUMINISTRO ---");

        try {

            System.out.print("Introduce el ID del suministro: ");
            Long id = leerId();

            Gestion gestion = gestionService.buscarPorId(id);

            if (gestion == null) {
                System.out.println("No existe ningún suministro con ese ID.");
                return;
            }

            System.out.println("\nDatos actuales:");
            imprimirGestion(gestion);
            System.out.println("\nIntroduce los nuevos datos:");
            System.out.print("Nuevo código del proveedor: ");
            String proveedorCodigo = scanner.nextLine();
            System.out.print("Nuevo código de la pieza: ");
            String piezaCodigo = scanner.nextLine();
            System.out.print("Nuevo código del proyecto: ");
            String proyectoCodigo = scanner.nextLine();
            System.out.print("Nueva fecha (YYYY-MM-DD): ");
            LocalDate fecha = leerFecha();
            System.out.print("Nueva cantidad: ");
            Integer cantidad = leerCantidad();

            boolean modificado = gestionService.actualizar(id, fecha, cantidad, proveedorCodigo, piezaCodigo, proyectoCodigo);

            if (modificado) {
                System.out.println("\nSuministro modificado correctamente.");
            } else {
                System.out.println("\nNo se ha podido encontrar el suministro.");
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("modificar el suministro", e);
        }
    }

    private void buscarPorId() {

        System.out.println("\n--- BUSCAR SUMINISTRO POR ID ---");

        try {

            System.out.print("Introduce el ID: ");
            Long id = leerId();

            Gestion gestion = gestionService.buscarPorId(id);

            if (gestion == null) {
                System.out.println("No se ha encontrado ningún suministro.");
            } else {
                imprimirGestion(gestion);
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar el suministro", e);
        }
    }

    private void buscarPorProveedor() {

        System.out.println("\n--- BUSCAR SUMINISTROS POR PROVEEDOR ---");

        try {

            System.out.print("Introduce el código del proveedor: ");
            String codigo = scanner.nextLine();
            List<Gestion> gestiones = gestionService.buscarPorProveedor(codigo);
            mostrarResultados(gestiones);

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar suministros por proveedor", e);
        }
    }

    private void buscarPorPieza() {

        System.out.println("\n--- BUSCAR SUMINISTROS POR PIEZA ---");

        try {

            System.out.print("Introduce el código de la pieza: ");
            String codigo = scanner.nextLine();
            List<Gestion> gestiones = gestionService.buscarPorPieza(codigo);
            mostrarResultados(gestiones);

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar suministros por pieza", e);
        }
    }

    private void buscarPorProyecto() {

        System.out.println("\n--- BUSCAR SUMINISTROS POR PROYECTO ---");

        try {
            System.out.print("Introduce el código del proyecto: ");
            String codigo = scanner.nextLine();
            List<Gestion> gestiones = gestionService.buscarPorProyecto(codigo);
            mostrarResultados(gestiones);

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());
        } catch (Exception e) {
            mostrarError("buscar suministros por proyecto", e);
        }
    }

    private void mostrarTodos() {

        System.out.println("\n--- LISTADO DE SUMINISTROS ---");

        try {
            List<Gestion> gestiones = gestionService.listarTodos();
            mostrarResultados(gestiones);
        } catch (Exception e) {

            mostrarError("obtener los suministros", e);
        }
    }

    private void mostrarResultados(List<Gestion> gestiones) {

        if (gestiones.isEmpty()) {
            System.out.println("\nNo se han encontrado suministros.");
            return;
        }

        System.out.println("\nSuministros encontrados: " + gestiones.size());

        for (Gestion gestion : gestiones) {
            imprimirGestion(gestion);
        }
    }

    private void imprimirGestion(Gestion gestion) {

        System.out.println("----------------------------------------");
        System.out.println("ID: " + gestion.getId());
        System.out.println("Proveedor: " + gestion.getProveedor().getCodigo() + " - " + gestion.getProveedor().getNombre() + " " + gestion.getProveedor().getApellidos());
        System.out.println("Pieza: " + gestion.getPieza().getCodigo() + " - " + gestion.getPieza().getNombre());
        System.out.println("Proyecto: " + gestion.getProyecto().getCodigo() + " - " + gestion.getProyecto().getNombre());
        System.out.println("Fecha: " + gestion.getFecha());
        System.out.println("Cantidad: " + gestion.getCantidad());
        System.out.println("----------------------------------------");
    }

    private LocalDate leerFecha() {

        String entrada = scanner.nextLine().trim();

        try {
            return LocalDate.parse(entrada);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Introduce una fecha válida con el formato YYYY-MM-DD.");
        }
    }

    private Integer leerCantidad() {

        String entrada = scanner.nextLine().trim();

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Introduce una cantidad numérica válida.");
        }
    }

    private Long leerId() {
        String entrada = scanner.nextLine().trim();

        try {

            return Long.parseLong(entrada);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Introduce un identificador numérico válido.");
        }
    }

    private void mostrarError(String operacion, Exception e) {
        System.out.println("\nNo se ha podido " + operacion + ".");
        System.out.println("Motivo: " + e.getMessage());
    }
}
