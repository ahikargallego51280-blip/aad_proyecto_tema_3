
package org.example;

import org.example.gestionProyectos.menus.MenuPrincipal;
import org.example.gestionProyectos.util.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);

            menuPrincipal.mostrar();

        } catch (Exception e) {

            System.err.println(
                    "Se ha producido un error inesperado en la aplicación."
            );

            System.err.println("Motivo: " + e.getMessage());

        } finally {

            HibernateUtil.shutdown();

            System.out.println("\nAplicación finalizada.");
        }
    }
}
