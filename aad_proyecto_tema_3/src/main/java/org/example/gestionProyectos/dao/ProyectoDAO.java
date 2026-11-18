package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Proyecto;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class ProyectoDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProyectoDAO.class);

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

            logger.info("Proyecto guardado correctamente. Código: {}", proyecto.getCodigo());

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al guardar el proyecto. Código: {}", proyecto.getCodigo(), e);
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
                logger.warn("No se encontró el proyecto que se intentaba eliminar. Código: {}", codigo);
                return false;
            }

            session.remove(proyecto);

            transaction.commit();

            logger.info("Proyecto eliminado correctamente. Código: {}", codigo);

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al eliminar el proyecto. Código: {}", codigo, e);
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
                logger.warn("No se encontró el proyecto que se intentaba actualizar. Código: {}", proyecto.getCodigo());
                return false;
            }

            existente.setNombre(proyecto.getNombre());
            existente.setCiudad(proyecto.getCiudad());

            transaction.commit();

            logger.info("Proyecto actualizado correctamente. Código: {}", proyecto.getCodigo());

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al actualizar el proyecto. Código: {}", proyecto.getCodigo(), e);
            throw e;
        }
    }

    public Proyecto buscarPorCodigo(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            Proyecto proyecto = session.get(Proyecto.class, codigo);

            if (proyecto == null) {
                logger.warn("No se encontró ningún proyecto con código: {}", codigo);
            } else {
                logger.info("Proyecto encontrado. Código: {}", codigo);
            }

            return proyecto;

        } catch (RuntimeException e) {
            logger.error("Error al buscar el proyecto por código: {}", codigo, e);
            throw e;
        }
    }

    public List<Proyecto> buscarPorNombre(String nombre) {

        try (Session session = sessionFactory.openSession()) {

            List<Proyecto> proyectos = session.createQuery("FROM Proyecto p WHERE LOWER(p.nombre) LIKE :nombre", Proyecto.class).setParameter("nombre", "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%").getResultList();

            logger.info("Búsqueda de proyectos por nombre '{}'. Resultados: {}", nombre, proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proyectos por nombre: {}", nombre, e);
            throw e;
        }
    }

    public List<Proyecto> buscarPorCiudad(String ciudad) {

        try (Session session = sessionFactory.openSession()) {

            List<Proyecto> proyectos = session.createQuery("FROM Proyecto p WHERE LOWER(p.ciudad) LIKE :ciudad", Proyecto.class).setParameter("ciudad", "%" + ciudad.trim().toLowerCase(Locale.ROOT) + "%").getResultList();

            logger.info("Búsqueda de proyectos por ciudad '{}'. Resultados: {}", ciudad, proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proyectos por ciudad: {}", ciudad, e);
            throw e;
        }
    }

    public List<Proyecto> listarTodos() {

        try (Session session = sessionFactory.openSession()) {

            List<Proyecto> proyectos = session.createQuery("FROM Proyecto p ORDER BY p.nombre", Proyecto.class).getResultList();

            logger.info("Listado de proyectos completado. Total: {}", proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al listar los proyectos.", e);
            throw e;
        }
    }
}
