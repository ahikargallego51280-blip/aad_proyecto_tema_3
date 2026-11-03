package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Locale;

public class PiezaDAO {

    private final SessionFactory sessionFactory;

    public PiezaDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void guardar(Pieza pieza) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            session.persist(pieza);
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
            Pieza pieza = session.get(Pieza.class, codigo);

            if (pieza == null) {
                transaction.commit();
                return false;
            }

            session.remove(pieza);
            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public boolean actualizar(Pieza pieza) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Pieza existente = session.get(Pieza.class, pieza.getCodigo());

            if (existente == null) {
                transaction.commit();
                return false;
            }

            existente.setNombre(pieza.getNombre());
            existente.setPrecio(pieza.getPrecio());
            existente.setDescripcion(pieza.getDescripcion());

            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public Pieza buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Pieza.class, codigo);
        }
    }

    public List<Pieza> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Pieza p WHERE LOWER(p.nombre) LIKE :nombre", Pieza.class).setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%").getResultList();
        }
    }

    public List<Pieza> listarTodos() {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Pieza p ORDER BY p.nombre", Pieza.class).getResultList();
        }
    }
}
