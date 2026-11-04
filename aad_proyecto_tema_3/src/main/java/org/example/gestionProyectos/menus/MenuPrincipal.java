package org.example.gestionProyectos.menus;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;

    private final ProveedorMenu proveedorMenu;
    private final PiezaMenu piezaMenu;
    private final ProyectoMenu proyectoMenu;
    private final GestionMenu gestionMenu;

    public MenuPrincipal(Scanner scanner) {

        this.scanner = scanner;

        this.proveedorMenu = new ProveedorMenu(scanner);
        this.piezaMenu = new PiezaMenu(scanner);
        this.proyectoMenu = new ProyectoMenu(scanner);
        this.gestionMenu = new GestionMenu(scanner);
    }

    public void mostrar() {

        boolean salir = false;

        while (!salir) {

            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE GESTIÓN DE PROYECTOS");
            System.out.println("========================================");
            System.out.println("1. Gestión de proveedores");
            System.out.println("2. Gestión de piezas");
            System.out.println("3. Gestión de proyectos");
            System.out.println("4. Gestión de suministros");
            System.out.println("5. Ayuda");
            System.out.println("0. Salir");
            System.out.println("========================================");

            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    proveedorMenu.mostrar();
                    break;

                case "2":
                    piezaMenu.mostrar();
                    break;

                case "3":
                    proyectoMenu.mostrar();
                    break;

                case "4":
                    gestionMenu.mostrar();
                    break;

                case "5":
                    mostrarAyuda();
                    break;

                case "0":
                    salir = true;
                    System.out.println("\nCerrando la aplicación...");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        }
    }

    private void mostrarAyuda() {

        System.out.println("\n========================================");
        System.out.println("                  AYUDA");
        System.out.println("========================================");

        System.out.println("\nGESTIÓN DE PROVEEDORES");
        System.out.println("Permite registrar, eliminar, modificar y");
        System.out.println("consultar proveedores.");

        System.out.println("\nGESTIÓN DE PIEZAS");
        System.out.println("Permite registrar, eliminar, modificar y");
        System.out.println("consultar piezas.");

        System.out.println("\nGESTIÓN DE PROYECTOS");
        System.out.println("Permite registrar, eliminar, modificar y");
        System.out.println("consultar proyectos.");

        System.out.println("\nGESTIÓN DE SUMINISTROS");
        System.out.println("Permite registrar, eliminar, modificar y");
        System.out.println("consultar los suministros realizados.");
        System.out.println("Cada suministro relaciona un proveedor,");
        System.out.println("una pieza y un proyecto.");

        System.out.println("\nFORMATOS DE ENTRADA");
        System.out.println("Las fechas deben introducirse como YYYY-MM-DD.");
        System.out.println("Los precios admiten punto o coma decimal.");

        System.out.println("\nNAVEGACIÓN");
        System.out.println("Selecciona 0 en cualquier submenú para");
        System.out.println("volver al menú principal.");

        System.out.println("========================================");
    }
}
