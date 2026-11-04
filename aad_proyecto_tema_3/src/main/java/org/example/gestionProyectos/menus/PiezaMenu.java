package org.example.gestionProyectos.menus;

import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.services.PiezaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class PiezaMenu {

    private final Scanner scanner;
    private final PiezaService piezaService;

    public PiezaMenu(Scanner scanner) {
        this.scanner = scanner;
        this.piezaService = new PiezaService();
    }

    public void mostrar() {

        boolean volver = false;

        while (!volver && scanner.hasNextLine()) {

            System.out.println("\n========================================");
            System.out.println("             GESTIÓN DE PIEZAS");
            System.out.println("========================================");
            System.out.println("1. Registrar pieza");
            System.out.println("2. Eliminar pieza");
            System.out.println("3. Modificar pieza");
            System.out.println();
            System.out.println("4. Buscar pieza por código");
            System.out.println("5. Buscar pieza por nombre");
            System.out.println();
            System.out.println("6. Mostrar todas las piezas");
            System.out.println();
            System.out.println("0. Volver al menú anterior");
            System.out.println("========================================");

            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    registrarPieza();
                    break;

                case "2":
                    eliminarPieza();
                    break;

                case "3":
                    modificarPieza();
                    break;

                case "4":
                    buscarPorCodigo();
                    break;

                case "5":
                    buscarPorNombre();
                    break;

                case "6":
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

    private void registrarPieza() {

        System.out.println("\n--- REGISTRAR PIEZA ---");

        try {

            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Precio: ");
            BigDecimal precio = leerPrecio();

            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();

            Pieza pieza = new Pieza(codigo, nombre, precio, descripcion);

            piezaService.registrar(pieza);

            System.out.println("\nPieza registrada correctamente.");

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("registrar la pieza", e);
        }
    }

    private void eliminarPieza() {

        System.out.println("\n--- ELIMINAR PIEZA ---");

        try {

            System.out.print("Introduce el código de la pieza: ");
            String codigo = scanner.nextLine();

            Pieza pieza = piezaService.buscarPorCodigo(codigo);

            if (pieza == null) {
                System.out.println("No existe ninguna pieza con ese código.");
                return;
            }

            imprimirPieza(pieza);

            System.out.print("\n¿Deseas eliminar esta pieza? (S/N): ");
            String confirmacion = scanner.nextLine().trim();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean eliminado = piezaService.eliminar(codigo);

            if (eliminado) {
                System.out.println("Pieza eliminada correctamente.");
            } else {
                System.out.println("No se ha podido encontrar la pieza.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {
            mostrarError("eliminar la pieza", e);
        }
    }

    private void modificarPieza() {

        System.out.println("\n--- MODIFICAR PIEZA ---");

        try {

            System.out.print("Introduce el código de la pieza: ");
            String codigo = scanner.nextLine();

            Pieza pieza = piezaService.buscarPorCodigo(codigo);

            if (pieza == null) {
                System.out.println("No existe ninguna pieza con ese código.");
                return;
            }

            System.out.println("\nDatos actuales:");
            imprimirPieza(pieza);

            System.out.println("\nIntroduce los nuevos datos:");

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Nuevo precio: ");
            BigDecimal precio = leerPrecio();

            System.out.print("Nueva descripción: ");
            String descripcion = scanner.nextLine();

            Pieza actualizada = new Pieza(codigo, nombre, precio, descripcion);

            boolean modificado = piezaService.actualizar(actualizada);

            if (modificado) {
                System.out.println("\nPieza modificada correctamente.");
            } else {
                System.out.println("\nNo se ha podido encontrar la pieza.");
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("modificar la pieza", e);
        }
    }

    private void buscarPorCodigo() {

        System.out.println("\n--- BUSCAR POR CÓDIGO ---");

        try {

            System.out.print("Introduce el código: ");
            String codigo = scanner.nextLine();

            Pieza pieza = piezaService.buscarPorCodigo(codigo);

            if (pieza == null) {
                System.out.println("No se ha encontrado ninguna pieza.");
            } else {
                imprimirPieza(pieza);
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());
        } catch (Exception e) {

            mostrarError("buscar la pieza", e);
        }
    }

    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR POR NOMBRE ---");

        try {

            System.out.print("Introduce el nombre: ");
            String nombre = scanner.nextLine();
            List<Pieza> piezas = piezaService.buscarPorNombre(nombre);

            mostrarResultados(piezas);

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar piezas por nombre", e);
        }
    }

    private void mostrarTodos() {

        System.out.println("\n--- LISTADO DE PIEZAS ---");

        try {

            List<Pieza> piezas = piezaService.listarTodos();
            mostrarResultados(piezas);

        } catch (Exception e) {

            mostrarError("obtener las piezas", e);
        }
    }

    private void mostrarResultados(List<Pieza> piezas) {

        if (piezas.isEmpty()) {
            System.out.println("\nNo se han encontrado piezas.");
            return;
        }

        System.out.println("\nPiezas encontradas: " + piezas.size());

        for (Pieza pieza : piezas) {
            imprimirPieza(pieza);
        }
    }

    private void imprimirPieza(Pieza pieza) {

        System.out.println("----------------------------------------");
        System.out.println("Código: " + pieza.getCodigo());
        System.out.println("Nombre: " + pieza.getNombre());
        System.out.println("Precio: " + pieza.getPrecio());
        System.out.println("Descripción: " + (pieza.getDescripcion() == null ? "Sin descripción" : pieza.getDescripcion()));
        System.out.println("----------------------------------------");
    }

    private BigDecimal leerPrecio() {

        String entrada = scanner.nextLine().trim().replace(',', '.');

        try {
            return new BigDecimal(entrada);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Introduce un precio válido. Ejemplo: 25.50");
        }
    }

    private void mostrarError(String operacion, Exception e) {

        if (e instanceof NoSuchElementException) {
            System.out.println("\nEntrada finalizada. Se ha cancelado la operación.");
            return;
        }

        System.out.println("\nNo se ha podido " + operacion + ".");

        if (e.getMessage() != null) {
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
