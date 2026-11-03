package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Proyecto;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Locale;

public class ProyectoDAO {

    private final SessionFactory sessionFactory;

    public ProyectoDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void guardar(Proyecto proyecto) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            session.persist(proyecto);
            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public boolean eliminar(String codigo) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Proyecto proyecto = session.get(Proyecto.class, codigo);

            if (proyecto == null) {
                transaction.commit();
                return false;
            }

            session.remove(proyecto);
            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public boolean actualizar(Proyecto proyecto) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Proyecto existente = session.get(Proyecto.class, proyecto.getCodigo());

            if (existente == null) {
                transaction.commit();
                return false;
            }

            existente.setNombre(proyecto.getNombre());
            existente.setCiudad(proyecto.getCiudad());
            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public Proyecto buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Proyecto.class, codigo);
        }
    }

    public List<Proyecto> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Proyecto p WHERE LOWER(p.nombre) LIKE :nombre", Proyecto.class).setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%").getResultList();
        }
    }

    public List<Proyecto> buscarPorCiudad(String ciudad) {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Proyecto p WHERE LOWER(p.ciudad) LIKE :ciudad", Proyecto.class).setParameter("ciudad", "%" + ciudad.trim().toLowerCase(Locale.ROOT) + "%").getResultList();
        }
    }

    public List<Proyecto> listarTodos() {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Proyecto p ORDER BY p.nombre", Proyecto.class).getResultList();
        }
    }
}
