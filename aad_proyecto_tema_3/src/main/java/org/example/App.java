package org.example;

import org.example.gestionProyectos.menus.MenuPrincipal;
import org.example.gestionProyectos.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        boolean hibernateInicializado = false;

        try {

            logger.info("Iniciando aplicación de gestión de proyectos.");

            HibernateUtil.getSessionFactory();
            hibernateInicializado = true;

            logger.info("Hibernate inicializado correctamente.");

            try (Scanner scanner = new Scanner(System.in)) {

                MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);
                menuPrincipal.mostrar();
            }

        } catch (ExceptionInInitializerError e) {

            logger.error("No se ha podido inicializar Hibernate.", e);

            System.err.println("No se ha podido inicializar Hibernate.");

            Throwable causa = e.getCause();

            if (causa != null && causa.getMessage() != null) {
                System.err.println("Motivo: " + causa.getMessage());
            }

        } catch (Exception e) {

            logger.error("Se ha producido un error inesperado en la aplicación.", e);

            System.err.println("Se ha producido un error inesperado en la aplicación.");

            if (e.getMessage() != null) {
                System.err.println("Motivo: " + e.getMessage());
            }

        } finally {

            if (hibernateInicializado) {
                try {
                    HibernateUtil.shutdown();
                    logger.info("Hibernate cerrado correctamente.");
                } catch (RuntimeException e) {
                    logger.error("Error al cerrar la conexión con Hibernate.", e);
                    System.err.println("Error al cerrar la conexión con Hibernate: " + e.getMessage());
                }
            }

            logger.info("Aplicación finalizada.");
            System.out.println("\nAplicación finalizada.");
        }
    }
}
