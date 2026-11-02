package org.example;

import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;

public class App 
{
    public static void main(String[] args) {

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            session.doWork(connection -> {
                try (var statement = connection.createStatement();
                     var result = statement.executeQuery("SELECT 1")) {

                    if (result.next()) {
                        System.out.println(
                                "Conexión con la base de datos correcta."
                        );
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
