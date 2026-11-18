package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class PiezaDAO {

    private static final Logger logger = LoggerFactory.getLogger(PiezaDAO.class);

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

            logger.info("Pieza guardada correctamente. Código: {}", pieza.getCodigo());

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al guardar la pieza. Código: {}", pieza.getCodigo(), e);
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
                logger.warn("No se encontró la pieza que se intentaba eliminar. Código: {}", codigo);
                return false;
            }

            session.remove(pieza);

            transaction.commit();

            logger.info("Pieza eliminada correctamente. Código: {}", codigo);

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al eliminar la pieza. Código: {}", codigo, e);
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
                logger.warn("No se encontró la pieza que se intentaba actualizar. Código: {}", pieza.getCodigo());
                return false;
            }

            existente.setNombre(pieza.getNombre());
            existente.setPrecio(pieza.getPrecio());
            existente.setDescripcion(pieza.getDescripcion());

            transaction.commit();

            logger.info("Pieza actualizada correctamente. Código: {}", pieza.getCodigo());

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al actualizar la pieza. Código: {}", pieza.getCodigo(), e);
            throw e;
        }
    }

    public Pieza buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            Pieza pieza = session.get(Pieza.class, codigo);

            if (pieza == null) {
                logger.warn("No se encontró ninguna pieza con código: {}", codigo);
            } else {
                logger.info("Pieza encontrada. Código: {}", codigo);
            }

            return pieza;

        } catch (RuntimeException e) {
            logger.error("Error al buscar la pieza por código: {}", codigo, e);
            throw e;
        }
    }

    public List<Pieza> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {

            List<Pieza> piezas = session.createQuery("FROM Pieza p WHERE LOWER(p.nombre) LIKE :nombre", Pieza.class).setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%").getResultList();

            logger.info("Búsqueda de piezas por nombre '{}'. Resultados: {}", nombre, piezas.size());

            return piezas;

        } catch (RuntimeException e) {
            logger.error("Error al buscar piezas por nombre: {}", nombre, e);
            throw e;
        }
    }

    public List<Pieza> listarTodos() {

        try (Session session = sessionFactory.openSession()) {

            List<Pieza> piezas = session.createQuery("FROM Pieza p ORDER BY p.nombre", Pieza.class).getResultList();

            logger.info("Listado de piezas completado. Total: {}", piezas.size());

            return piezas;

        } catch (RuntimeException e) {
            logger.error("Error al listar las piezas.", e);
            throw e;
        }
    }
}
