package org.example;

import org.example.gestionProyectos.menus.MenuPrincipal;
import org.example.gestionProyectos.util.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        boolean hibernateInicializado = false;

        try {

            HibernateUtil.getSessionFactory();
            hibernateInicializado = true;

            try (Scanner scanner = new Scanner(System.in)) {

                MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);
                menuPrincipal.mostrar();
            }

        } catch (ExceptionInInitializerError e) {

            System.err.println("No se ha podido inicializar Hibernate.");
            Throwable causa = e.getCause();

            if (causa != null && causa.getMessage() != null) {
                System.err.println("Motivo: " + causa.getMessage());
            }

        } catch (Exception e) {

            System.err.println("Se ha producido un error inesperado en la aplicación.");

            if (e.getMessage() != null) {
                System.err.println("Motivo: " + e.getMessage());
            }

        } finally {

            if (hibernateInicializado) {
                try {
                    HibernateUtil.shutdown();
                } catch (RuntimeException e) {
                    System.err.println("Error al cerrar la conexión con Hibernate: " + e.getMessage());
                }
            }

            System.out.println("\nAplicación finalizada.");
        }
    }
}
