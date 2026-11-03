package org.example.gestionProyectos.util;

import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")

                    .addAnnotatedClass(Proveedor.class)
                    .addAnnotatedClass(Pieza.class)
                    .addAnnotatedClass(Proyecto.class)
                    .addAnnotatedClass(Gestion.class)

                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Error al crear SessionFactory:");
            ex.printStackTrace();

            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
