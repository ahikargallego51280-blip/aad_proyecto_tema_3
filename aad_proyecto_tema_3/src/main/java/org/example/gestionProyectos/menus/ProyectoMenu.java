package org.example.gestionProyectos.menus;

import org.example.gestionProyectos.entities.Proyecto;
import org.example.gestionProyectos.services.ProyectoService;

import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class ProyectoMenu {

    private final Scanner scanner;
    private final ProyectoService proyectoService;

    public ProyectoMenu(Scanner scanner) {
        this.scanner = scanner;
        this.proyectoService = new ProyectoService();
    }


    public void mostrar() {

        boolean volver = false;

        while (!volver && scanner.hasNextLine()) {

            System.out.println("\n========================================");
            System.out.println("          GESTIÓN DE PROYECTOS");
            System.out.println("========================================");
            System.out.println("1. Registrar proyecto");
            System.out.println("2. Eliminar proyecto");
            System.out.println("3. Modificar proyecto");
            System.out.println();
            System.out.println("4. Buscar proyecto por código");
            System.out.println("5. Buscar proyecto por nombre");
            System.out.println("6. Buscar proyecto por ciudad");
            System.out.println();
            System.out.println("7. Mostrar todos los proyectos");
            System.out.println();
            System.out.println("0. Volver al menú anterior");
            System.out.println("========================================");

            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    registrarProyecto();
                    break;

                case "2":
                    eliminarProyecto();
                    break;

                case "3":
                    modificarProyecto();
                    break;

                case "4":
                    buscarPorCodigo();
                    break;

                case "5":
                    buscarPorNombre();
                    break;

                case "6":
                    buscarPorCiudad();
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


    private void registrarProyecto() {

        System.out.println("\n--- REGISTRAR PROYECTO ---");

        try {

            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ciudad: ");
            String ciudad = scanner.nextLine();

            Proyecto proyecto = new Proyecto(codigo, nombre, ciudad);

            proyectoService.registrar(proyecto);

            System.out.println("\nProyecto registrado correctamente.");

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("registrar el proyecto", e);
        }
    }

    private void eliminarProyecto() {

        System.out.println("\n--- ELIMINAR PROYECTO ---");

        try {

            System.out.print("Introduce el código del proyecto: ");
            String codigo = scanner.nextLine();

            Proyecto proyecto = proyectoService.buscarPorCodigo(codigo);

            if (proyecto == null) {
                System.out.println("No existe ningún proyecto con ese código.");
                return;
            }

            imprimirProyecto(proyecto);

            System.out.print("\n¿Deseas eliminar este proyecto? (S/N): ");
            String confirmacion = scanner.nextLine().trim();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean eliminado = proyectoService.eliminar(codigo);

            if (eliminado) {
                System.out.println("Proyecto eliminado correctamente.");
            } else {
                System.out.println("No se ha podido encontrar el proyecto.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("eliminar el proyecto", e);
        }
    }

    private void modificarProyecto() {

        System.out.println("\n--- MODIFICAR PROYECTO ---");

        try {

            System.out.print("Introduce el código del proyecto: ");
            String codigo = scanner.nextLine();

            Proyecto proyecto = proyectoService.buscarPorCodigo(codigo);

            if (proyecto == null) {
                System.out.println("No existe ningún proyecto con ese código.");
                return;
            }

            System.out.println("\nDatos actuales:");
            imprimirProyecto(proyecto);

            System.out.println("\nIntroduce los nuevos datos:");

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Nueva ciudad: ");
            String ciudad = scanner.nextLine();

            Proyecto actualizado = new Proyecto(codigo, nombre, ciudad);

            boolean modificado = proyectoService.actualizar(actualizado);

            if (modificado) {
                System.out.println("\nProyecto modificado correctamente.");
            } else {
                System.out.println("\nNo se ha podido encontrar el proyecto.");
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\nError de validación: " + e.getMessage());

        } catch (Exception e) {

            mostrarError("modificar el proyecto", e);
        }
    }

    private void buscarPorCodigo() {

        System.out.println("\n--- BUSCAR POR CÓDIGO ---");

        try {

            System.out.print("Introduce el código: ");
            String codigo = scanner.nextLine();

            Proyecto proyecto = proyectoService.buscarPorCodigo(codigo);

            if (proyecto == null) {
                System.out.println("No se ha encontrado ningún proyecto.");
            } else {
                imprimirProyecto(proyecto);
            }

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar el proyecto", e);
        }
    }

    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR POR NOMBRE ---");

        try {

            System.out.print("Introduce el nombre: ");
            String nombre = scanner.nextLine();
            List<Proyecto> proyectos = proyectoService.buscarPorNombre(nombre);
            mostrarResultados(proyectos);

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar proyectos por nombre", e);
        }
    }

    private void buscarPorCiudad() {

        System.out.println("\n--- BUSCAR POR CIUDAD ---");

        try {

            System.out.print("Introduce la ciudad: ");
            String ciudad = scanner.nextLine();

            List<Proyecto> proyectos = proyectoService.buscarPorCiudad(ciudad);
            mostrarResultados(proyectos);

        } catch (IllegalArgumentException e) {

            System.out.println("\n" + e.getMessage());

        } catch (Exception e) {

            mostrarError("buscar proyectos por ciudad", e);
        }
    }

    private void mostrarTodos() {

        System.out.println("\n--- LISTADO DE PROYECTOS ---");

        try {

            List<Proyecto> proyectos = proyectoService.listarTodos();
            mostrarResultados(proyectos);

        } catch (Exception e) {

            mostrarError("obtener los proyectos", e);
        }
    }

    private void mostrarResultados(List<Proyecto> proyectos) {

        if (proyectos.isEmpty()) {
            System.out.println("\nNo se han encontrado proyectos.");
            return;
        }

        System.out.println("\nProyectos encontrados: " + proyectos.size());

        for (Proyecto proyecto : proyectos) {
            imprimirProyecto(proyecto);
        }
    }

    private void imprimirProyecto(Proyecto proyecto) {

        System.out.println("----------------------------------------");
        System.out.println("Código: " + proyecto.getCodigo());
        System.out.println("Nombre: " + proyecto.getNombre());
        System.out.println("Ciudad: " + proyecto.getCiudad());
        System.out.println("----------------------------------------");
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
