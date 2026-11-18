package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class ProveedorDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorDAO.class);

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

            logger.info("Proveedor guardado correctamente. Código: {}", proveedor.getCodigo());

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al guardar el proveedor. Código: {}", proveedor.getCodigo(), e);
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
                logger.warn("No se encontró el proveedor que se intentaba eliminar. Código: {}", codigo);
                return false;
            }

            session.remove(proveedor);

            transaction.commit();

            logger.info("Proveedor eliminado correctamente. Código: {}", codigo);

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al eliminar el proveedor. Código: {}", codigo, e);
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
                logger.warn("No se encontró el proveedor que se intentaba actualizar. Código: {}", proveedor.getCodigo());
                return false;
            }

            existente.setNombre(proveedor.getNombre());
            existente.setApellidos(proveedor.getApellidos());
            existente.setDireccionPostal(proveedor.getDireccionPostal());

            transaction.commit();

            logger.info("Proveedor actualizado correctamente. Código: {}", proveedor.getCodigo());

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al actualizar el proveedor. Código: {}", proveedor.getCodigo(), e);
            throw e;
        }
    }

    public Proveedor buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            Proveedor proveedor = session.get(Proveedor.class, codigo);

            if (proveedor == null) {
                logger.warn("No se encontró ningún proveedor con código: {}", codigo);
            } else {
                logger.info("Proveedor encontrado. Código: {}", codigo);
            }

            return proveedor;

        } catch (RuntimeException e) {
            logger.error("Error al buscar el proveedor por código: {}", codigo, e);
            throw e;
        }
    }

    public List<Proveedor> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {

            List<Proveedor> proveedores = session.createQuery("FROM Proveedor p WHERE LOWER(p.nombre) LIKE :nombre", Proveedor.class).setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%").getResultList();

            logger.info("Búsqueda de proveedores por nombre '{}'. Resultados: {}", nombre, proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proveedores por nombre: {}", nombre, e);
            throw e;
        }
    }

    public List<Proveedor> buscarPorDireccion(String direccion) {

        try (Session session = sessionFactory.openSession()) {

            List<Proveedor> proveedores = session.createQuery("FROM Proveedor p WHERE LOWER(p.direccionPostal) LIKE :direccion", Proveedor.class).setParameter("direccion", "%" + direccion.trim().toLowerCase(Locale.ROOT) + "%").getResultList();

            logger.info("Búsqueda de proveedores por dirección '{}'. Resultados: {}", direccion, proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proveedores por dirección: {}", direccion, e);
            throw e;
        }
    }

    public List<Proveedor> listarTodos() {

        try (Session session = sessionFactory.openSession()) {

            List<Proveedor> proveedores = session.createQuery("FROM Proveedor p ORDER BY p.nombre", Proveedor.class).getResultList();

            logger.info("Listado de proveedores completado. Total: {}", proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al listar los proveedores.", e);
            throw e;
        }
    }
}
