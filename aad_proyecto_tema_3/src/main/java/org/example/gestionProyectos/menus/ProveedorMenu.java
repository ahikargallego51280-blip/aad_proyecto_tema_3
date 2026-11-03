package org.example.gestionProyectos.menus;

import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.services.ProveedorService;

import java.util.List;
import java.util.Scanner;

public class ProveedorMenu {

    private final Scanner scanner;
    private final ProveedorService proveedorService;

    public ProveedorMenu(Scanner scanner) {
        this.scanner = scanner;
        this.proveedorService = new ProveedorService();
    }

    public void mostrar() {

        boolean volver = false;

        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("          GESTIÓN DE PROVEEDORES");
            System.out.println("========================================");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Eliminar proveedor");
            System.out.println("3. Modificar proveedor");
            System.out.println();
            System.out.println("4. Buscar proveedor por código");
            System.out.println("5. Buscar proveedor por nombre");
            System.out.println("6. Buscar proveedor por dirección");
            System.out.println();
            System.out.println("7. Mostrar todos los proveedores");
            System.out.println();
            System.out.println("0. Volver al menú anterior");
            System.out.println("========================================");

            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    registrarProveedor();
                    break;

                case "2":
                    eliminarProveedor();
                    break;

                case "3":
                    modificarProveedor();
                    break;

                case "4":
                    buscarPorCodigo();
                    break;

                case "5":
                    buscarPorNombre();
                    break;

                case "6":
                    buscarPorDireccion();
                    break;

                case "7":
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

    private void registrarProveedor() {

        System.out.println("\n--- REGISTRAR PROVEEDOR ---");

        try {
            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Dirección postal: ");
            String direccion = scanner.nextLine();

            Proveedor proveedor = new Proveedor(codigo, nombre, apellidos, direccion);

            proveedorService.registrar(proveedor);
            System.out.println("\nProveedor registrado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {
            mostrarError("registrar el proveedor", e);
        }
    }

    private void eliminarProveedor() {

        System.out.println("\n--- ELIMINAR PROVEEDOR ---");

        try {
            System.out.print("Introduce el código del proveedor: ");
            String codigo = scanner.nextLine();

            Proveedor proveedor = proveedorService.buscarPorCodigo(codigo);

            if (proveedor == null) {
                System.out.println("No existe ningún proveedor con ese código.");
                return;
            }

            imprimirProveedor(proveedor);

            System.out.print("\n¿Deseas eliminar este proveedor? (S/N): ");
            String confirmacion = scanner.nextLine().trim();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean eliminado = proveedorService.eliminar(codigo);

            if (eliminado) {
                System.out.println("Proveedor eliminado correctamente.");
            } else {
                System.out.println("No se ha podido encontrar el proveedor.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("eliminar el proveedor", e);
        }
    }

    private void modificarProveedor() {

        System.out.println("\n--- MODIFICAR PROVEEDOR ---");

        try {

            System.out.print("Introduce el código del proveedor: ");
            String codigo = scanner.nextLine();
            Proveedor proveedor = proveedorService.buscarPorCodigo(codigo);

            if (proveedor == null) {
                System.out.println("No existe ningún proveedor con ese código.");
                return;
            }

            System.out.println("\nDatos actuales:");
            imprimirProveedor(proveedor);

            System.out.println("\nIntroduce los nuevos datos:");

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Nuevos apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Nueva dirección postal: ");
            String direccion = scanner.nextLine();

            Proveedor actualizado = new Proveedor(codigo, nombre, apellidos, direccion);

            boolean modificado = proveedorService.actualizar(actualizado);

            if (modificado) {
                System.out.println("\nProveedor modificado correctamente.");
            } else {
                System.out.println("\nNo se ha podido encontrar el proveedor.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {
            mostrarError("modificar el proveedor", e);
        }
    }

    private void buscarPorCodigo() {

        System.out.println("\n--- BUSCAR POR CÓDIGO ---");

        try {
            System.out.print("Introduce el código: ");
            String codigo = scanner.nextLine();
            Proveedor proveedor = proveedorService.buscarPorCodigo(codigo);

            if (proveedor == null) {
                System.out.println("No se ha encontrado ningún proveedor.");
            } else {
                imprimirProveedor(proveedor);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {
            mostrarError("buscar el proveedor", e);
        }
    }

    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR POR NOMBRE ---");

        try {
            System.out.print("Introduce el nombre: ");
            String nombre = scanner.nextLine();
            List<Proveedor> proveedores = proveedorService.buscarPorNombre(nombre);

            mostrarResultados(proveedores);

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar proveedores por nombre", e);
        }
    }

    private void buscarPorDireccion() {

        System.out.println("\n--- BUSCAR POR DIRECCIÓN ---");

        try {

            System.out.print("Introduce la dirección: ");
            String direccion = scanner.nextLine();

            List<Proveedor> proveedores = proveedorService.buscarPorDireccion(direccion);
            mostrarResultados(proveedores);

        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar proveedores por dirección", e);
        }
    }

    private void mostrarTodos() {

        System.out.println("\n--- LISTADO DE PROVEEDORES ---");

        try {

            List<Proveedor> proveedores = proveedorService.listarTodos();

            mostrarResultados(proveedores);

        } catch (Exception e) {

            mostrarError("obtener los proveedores", e);
        }
    }

    private void mostrarResultados(List<Proveedor> proveedores) {

        if (proveedores.isEmpty()) {
            System.out.println("\nNo se han encontrado proveedores.");
            return;
        }

        System.out.println("\nProveedores encontrados: " + proveedores.size());

        for (Proveedor proveedor : proveedores) {
            imprimirProveedor(proveedor);
        }
    }

    private void imprimirProveedor(Proveedor proveedor) {

        System.out.println("----------------------------------------");
        System.out.println("Código: " + proveedor.getCodigo());
        System.out.println("Nombre: " + proveedor.getNombre());
        System.out.println("Apellidos: " + proveedor.getApellidos());
        System.out.println("Dirección: " + proveedor.getDireccionPostal());
        System.out.println("----------------------------------------");
    }

    private void mostrarError(String operacion, Exception e) {

        System.out.println("\nNo se ha podido " + operacion + ".");
        System.out.println("Motivo: " + e.getMessage());
    }
}
