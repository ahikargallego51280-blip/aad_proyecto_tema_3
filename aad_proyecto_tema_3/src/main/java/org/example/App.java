package org.example;

import org.example.gestionProyectos.menus.ProveedorMenu;
import org.example.gestionProyectos.util.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            ProveedorMenu proveedorMenu = new ProveedorMenu(scanner);

            proveedorMenu.mostrar();

        } finally {

            HibernateUtil.shutdown();

            System.out.println("\nAplicación finalizada.");
        }
    }
}
