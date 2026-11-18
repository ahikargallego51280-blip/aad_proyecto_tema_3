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

            ejecutarAplicacion();

        } catch (ExceptionInInitializerError e) {

            Throwable causa = e.getCause() != null ? e.getCause() : e;

            logger.error("No se ha podido inicializar Hibernate.", e);

            System.err.println("\nNo se ha podido inicializar Hibernate.");
            System.err.println("Motivo: " + causa.getMessage());

        } catch (Exception e) {

            logger.error("Se ha producido un error inesperado en la aplicación.", e);

            System.err.println("\nSe ha producido un error inesperado.");
            System.err.println("Motivo: " + e.getMessage());

        } finally {

            cerrarHibernate(hibernateInicializado);

            logger.info("Aplicación finalizada.");
            System.out.println("\nAplicación finalizada.");
        }
    }

    private static void ejecutarAplicacion() {

        try (Scanner scanner = new Scanner(System.in)) {

            MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);
            menuPrincipal.mostrar();
        }
    }

    private static void cerrarHibernate(boolean inicializado) {

        if (!inicializado) {
            return;
        }

        try {
            HibernateUtil.shutdown();
            logger.info("Hibernate cerrado correctamente.");

        } catch (RuntimeException e) {

            logger.error("Error al cerrar Hibernate.", e);

            System.err.println("Error al cerrar la conexión con Hibernate: " + e.getMessage());
        }
    }
}
