package org.example.gestionProyectos.dao;

import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;
import org.example.gestionProyectos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class GestionDAO {

    private static final Logger logger = LoggerFactory.getLogger(GestionDAO.class);

    private final SessionFactory sessionFactory;

    public GestionDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Long guardar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Proveedor proveedor = session.get(Proveedor.class, gestion.getProveedor().getCodigo());
            Pieza pieza = session.get(Pieza.class, gestion.getPieza().getCodigo());
            Proyecto proyecto = session.get(Proyecto.class, gestion.getProyecto().getCodigo());

            if (proveedor == null || pieza == null || proyecto == null) {
                logger.warn("No se pudo guardar el suministro: faltan entidades relacionadas.");
                throw new IllegalArgumentException("No se han encontrado todas las entidades relacionadas.");
            }

            Gestion nuevaGestion = new Gestion(gestion.getFecha(), gestion.getCantidad(), proveedor, pieza, proyecto);

            session.persist(nuevaGestion);
            transaction.commit();

            logger.info("Suministro guardado. ID: {}, proveedor: {}, pieza: {}, proyecto: {}", nuevaGestion.getId(), proveedor.getCodigo(), pieza.getCodigo(), proyecto.getCodigo());

            return nuevaGestion.getId();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al guardar el suministro.", e);
            throw e;
        }
    }

    public boolean eliminar(Long id) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Gestion gestion = session.get(Gestion.class, id);

            if (gestion == null) {
                transaction.commit();
                logger.warn("No se encontró el suministro que se intentaba eliminar. ID: {}", id);
                return false;
            }

            session.remove(gestion);

            transaction.commit();

            logger.info("Suministro eliminado correctamente. ID: {}", id);

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al eliminar el suministro. ID: {}", id, e);
            throw e;
        }
    }

    public boolean actualizar(Long id, LocalDate fecha, Integer cantidad, String proveedorCodigo, String piezaCodigo, String proyectoCodigo) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Gestion existente = session.get(Gestion.class, id);

            if (existente == null) {
                transaction.commit();
                logger.warn("No se encontró el suministro que se intentaba actualizar. ID: {}", id);
                return false;
            }

            Proveedor proveedor = session.get(Proveedor.class, proveedorCodigo);
            Pieza pieza = session.get(Pieza.class, piezaCodigo);
            Proyecto proyecto = session.get(Proyecto.class, proyectoCodigo);

            if (proveedor == null || pieza == null || proyecto == null) {
                logger.warn("No se pudo actualizar el suministro {}: faltan entidades relacionadas.", id);
                throw new IllegalArgumentException("No se han encontrado todas las entidades relacionadas.");
            }

            existente.setFecha(fecha);
            existente.setCantidad(cantidad);
            existente.setProveedor(proveedor);
            existente.setPieza(pieza);
            existente.setProyecto(proyecto);

            transaction.commit();

            logger.info("Suministro actualizado correctamente. ID: {}", id);

            return true;

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error al actualizar el suministro. ID: {}", id, e);
            throw e;
        }
    }

    public Gestion buscarPorId(Long id) {

        try (Session session = sessionFactory.openSession()) {

            Gestion gestion = session.createQuery("""
                    SELECT DISTINCT g
                    FROM Gestion g
                    JOIN FETCH g.proveedor
                    JOIN FETCH g.pieza
                    JOIN FETCH g.proyecto
                    WHERE g.id = :id
                    """, Gestion.class).setParameter("id", id).uniqueResult();

            if (gestion == null) {
                logger.warn("No se encontró ningún suministro con ID: {}", id);
            } else {
                logger.info("Suministro encontrado. ID: {}", id);
            }

            return gestion;

        } catch (RuntimeException e) {
            logger.error("Error al buscar el suministro. ID: {}", id, e);
            throw e;
        }
    }

    public List<Gestion> buscarPorProveedor(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            List<Gestion> gestiones = session.createQuery("""
                    SELECT DISTINCT g
                    FROM Gestion g
                    JOIN FETCH g.proveedor
                    JOIN FETCH g.pieza
                    JOIN FETCH g.proyecto
                    WHERE g.proveedor.codigo = :codigo
                    ORDER BY g.fecha DESC
                    """, Gestion.class).setParameter("codigo", codigo).getResultList();

            logger.info("Búsqueda de suministros por proveedor {}. Resultados: {}", codigo, gestiones.size());

            return gestiones;

        } catch (RuntimeException e) {
            logger.error("Error al buscar suministros por proveedor: {}", codigo, e);
            throw e;
        }
    }

    public List<Gestion> buscarPorPieza(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            List<Gestion> gestiones = session.createQuery("""
                    SELECT DISTINCT g
                    FROM Gestion g
                    JOIN FETCH g.proveedor
                    JOIN FETCH g.pieza
                    JOIN FETCH g.proyecto
                    WHERE g.pieza.codigo = :codigo
                    ORDER BY g.fecha DESC
                    """, Gestion.class).setParameter("codigo", codigo).getResultList();

            logger.info("Búsqueda de suministros por pieza {}. Resultados: {}", codigo, gestiones.size());

            return gestiones;

        } catch (RuntimeException e) {
            logger.error("Error al buscar suministros por pieza: {}", codigo, e);
            throw e;
        }
    }

    public List<Gestion> buscarPorProyecto(String codigo) {

        try (Session session = sessionFactory.openSession()) {

            List<Gestion> gestiones = session.createQuery("""
                    SELECT DISTINCT g
                    FROM Gestion g
                    JOIN FETCH g.proveedor
                    JOIN FETCH g.pieza
                    JOIN FETCH g.proyecto
                    WHERE g.proyecto.codigo = :codigo
                    ORDER BY g.fecha DESC
                    """, Gestion.class).setParameter("codigo", codigo).getResultList();

            logger.info("Búsqueda de suministros por proyecto {}. Resultados: {}", codigo, gestiones.size());

            return gestiones;

        } catch (RuntimeException e) {
            logger.error("Error al buscar suministros por proyecto: {}", codigo, e);
            throw e;
        }
    }

    public List<Gestion> listarTodos() {

        try (Session session = sessionFactory.openSession()) {

            List<Gestion> gestiones = session.createQuery("""
                    SELECT DISTINCT g
                    FROM Gestion g
                    JOIN FETCH g.proveedor
                    JOIN FETCH g.pieza
                    JOIN FETCH g.proyecto
                    ORDER BY g.fecha DESC, g.id DESC
                    """, Gestion.class).getResultList();

            logger.info("Listado de suministros completado. Total: {}", gestiones.size());

            return gestiones;

        } catch (RuntimeException e) {
            logger.error("Error al listar los suministros.", e);
            throw e;
        }
    }
}
