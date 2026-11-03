
package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Locale;

public class ProveedorDAO {

    private final SessionFactory sessionFactory;

    public ProveedorDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void guardar(Proveedor proveedor) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(proveedor);

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

            Proveedor proveedor = session.get(Proveedor.class, codigo);

            if (proveedor == null) {
                transaction.commit();
                return false;
            }

            session.remove(proveedor);

            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public boolean actualizar(Proveedor proveedor) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Proveedor existente = session.get(Proveedor.class, proveedor.getCodigo());

            if (existente == null) {
                transaction.commit();
                return false;
            }

            existente.setNombre(proveedor.getNombre());
            existente.setApellidos(proveedor.getApellidos());
            existente.setDireccionPostal(proveedor.getDireccionPostal());

            transaction.commit();

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public Proveedor buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Proveedor.class, codigo);
        }
    }

    public List<Proveedor> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("FROM Proveedor p WHERE LOWER(p.nombre) LIKE :nombre", Proveedor.class)
                    .setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%")
                    .getResultList();
        }
    }

    public List<Proveedor> buscarPorDireccion(String direccion) {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("FROM Proveedor p WHERE LOWER(p.direccionPostal) LIKE :direccion", Proveedor.class)
                    .setParameter("direccion", "%" + direccion.trim().toLowerCase(Locale.ROOT) + "%")
                    .getResultList();
        }
    }

    public List<Proveedor> listarTodos() {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery("FROM Proveedor p ORDER BY p.nombre", Proveedor.class).getResultList();
        }
    }
}
