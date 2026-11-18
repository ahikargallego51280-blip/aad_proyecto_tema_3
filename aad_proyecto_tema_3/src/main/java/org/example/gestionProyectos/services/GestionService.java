package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.GestionDAO;
import org.example.gestionProyectos.dao.PiezaDAO;
import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.dao.ProyectoDAO;
import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class GestionService {

    private static final Logger logger = LoggerFactory.getLogger(GestionService.class);

    private final GestionDAO gestionDAO;
    private final ProveedorDAO proveedorDAO;
    private final PiezaDAO piezaDAO;
    private final ProyectoDAO proyectoDAO;

    public GestionService() {
        this(new GestionDAO(), new ProveedorDAO(), new PiezaDAO(), new ProyectoDAO());
    }

    public GestionService(GestionDAO gestionDAO, ProveedorDAO proveedorDAO, PiezaDAO piezaDAO, ProyectoDAO proyectoDAO) {
        this.gestionDAO = java.util.Objects.requireNonNull(gestionDAO);
        this.proveedorDAO = java.util.Objects.requireNonNull(proveedorDAO);
        this.piezaDAO = java.util.Objects.requireNonNull(piezaDAO);
        this.proyectoDAO = java.util.Objects.requireNonNull(proyectoDAO);
    }

    public Long registrar(Gestion gestion) {

        validarGestion(gestion);

        Proveedor proveedor = proveedorDAO.buscarPorCodigo(gestion.getProveedor().getCodigo());

        if (proveedor == null) {
            logger.warn("No se pudo registrar el suministro: no existe el proveedor {}.", gestion.getProveedor().getCodigo());
            throw new IllegalArgumentException("No existe ningún proveedor con ese código.");
        }

        Pieza pieza = piezaDAO.buscarPorCodigo(gestion.getPieza().getCodigo());

        if (pieza == null) {
            logger.warn("No se pudo registrar el suministro: no existe la pieza {}.", gestion.getPieza().getCodigo());
            throw new IllegalArgumentException("No existe ninguna pieza con ese código.");
        }

        Proyecto proyecto = proyectoDAO.buscarPorCodigo(gestion.getProyecto().getCodigo());

        if (proyecto == null) {
            logger.warn("No se pudo registrar el suministro: no existe el proyecto {}.", gestion.getProyecto().getCodigo());
            throw new IllegalArgumentException("No existe ningún proyecto con ese código.");
        }

        Gestion nuevaGestion = new Gestion(gestion.getFecha(), gestion.getCantidad(), proveedor, pieza, proyecto);

        Long id = gestionDAO.guardar(nuevaGestion);

        logger.info("Suministro registrado. ID: {}, proveedor: {}, pieza: {}, proyecto: {}, cantidad: {}", id, proveedor.getCodigo(), pieza.getCodigo(), proyecto.getCodigo(), gestion.getCantidad());

        return id;
    }

    public boolean eliminar(Long id) {

        validarId(id);

        boolean eliminado = gestionDAO.eliminar(id);

        if (eliminado) {
            logger.info("Suministro eliminado. ID: {}", id);
        } else {
            logger.warn("No se encontró el suministro que se intentaba eliminar. ID: {}", id);
        }

        return eliminado;
    }

    public boolean actualizar(Long id, LocalDate fecha, Integer cantidad, String proveedorCodigo, String piezaCodigo, String proyectoCodigo) {

        validarId(id);
        validarDatos(fecha, cantidad);

        if (gestionDAO.buscarPorId(id) == null) {
            logger.warn("No se pudo actualizar el suministro porque no existe. ID: {}", id);
            return false;
        }

        proveedorCodigo = validarCodigoRelacionado(proveedorCodigo, "proveedor");
        piezaCodigo = validarCodigoRelacionado(piezaCodigo, "pieza");
        proyectoCodigo = validarCodigoRelacionado(proyectoCodigo, "proyecto");

        comprobarReferencias(proveedorCodigo, piezaCodigo, proyectoCodigo);

        boolean actualizado = gestionDAO.actualizar(id, fecha, cantidad, proveedorCodigo, piezaCodigo, proyectoCodigo);

        if (actualizado) {
            logger.info("Suministro actualizado. ID: {}", id);
        } else {
            logger.warn("No se pudo actualizar el suministro. ID: {}", id);
        }

        return actualizado;
    }

    public Gestion buscarPorId(Long id) {

        validarId(id);

        Gestion gestion = gestionDAO.buscarPorId(id);

        if (gestion == null) {
            logger.warn("No se encontró ningún suministro con ID: {}", id);
        } else {
            logger.info("Suministro consultado. ID: {}", id);
        }

        return gestion;
    }

    public List<Gestion> buscarPorProveedor(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "proveedor");

        List<Gestion> gestiones = gestionDAO.buscarPorProveedor(codigo);

        logger.info("Búsqueda de suministros por proveedor {}. Resultados: {}", codigo, gestiones.size());

        return gestiones;
    }

    public List<Gestion> buscarPorPieza(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "pieza");

        List<Gestion> gestiones = gestionDAO.buscarPorPieza(codigo);

        logger.info("Búsqueda de suministros por pieza {}. Resultados: {}", codigo, gestiones.size());

        return gestiones;
    }

    public List<Gestion> buscarPorProyecto(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "proyecto");

        List<Gestion> gestiones = gestionDAO.buscarPorProyecto(codigo);

        logger.info("Búsqueda de suministros por proyecto {}. Resultados: {}", codigo, gestiones.size());

        return gestiones;
    }

    public List<Gestion> listarTodos() {

        List<Gestion> gestiones = gestionDAO.listarTodos();

        logger.info("Listado de todos los suministros. Total: {}", gestiones.size());

        return gestiones;
    }

    private void validarGestion(Gestion gestion) {

        if (gestion == null) {
            throw new IllegalArgumentException("Los datos del suministro no pueden estar vacíos.");
        }

        validarDatos(gestion.getFecha(), gestion.getCantidad());

        if (gestion.getProveedor() == null) {
            throw new IllegalArgumentException("Debes seleccionar un proveedor.");
        }

        if (gestion.getPieza() == null) {
            throw new IllegalArgumentException("Debes seleccionar una pieza.");
        }

        if (gestion.getProyecto() == null) {
            throw new IllegalArgumentException("Debes seleccionar un proyecto.");
        }

        validarCodigoRelacionado(gestion.getProveedor().getCodigo(), "proveedor");
        validarCodigoRelacionado(gestion.getPieza().getCodigo(), "pieza");
        validarCodigoRelacionado(gestion.getProyecto().getCodigo(), "proyecto");
    }

    private void validarDatos(LocalDate fecha, Integer cantidad) {

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del suministro es obligatoria.");
        }

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad suministrada debe ser mayor que cero.");
        }
    }

    private String validarCodigoRelacionado(String codigo, String entidad) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes introducir el código del " + entidad + ".");
        }

        String codigoLimpio = codigo.trim();

        if (codigoLimpio.length() > 20) {
            throw new IllegalArgumentException("El código del " + entidad + " no puede superar los 20 caracteres.");
        }

        return codigoLimpio;
    }

    private void comprobarReferencias(String proveedorCodigo, String piezaCodigo, String proyectoCodigo) {

        if (proveedorDAO.buscarPorCodigo(proveedorCodigo) == null) {
            logger.warn("No existe el proveedor {} asociado al suministro.", proveedorCodigo);
            throw new IllegalArgumentException("No existe ningún proveedor con ese código.");
        }

        if (piezaDAO.buscarPorCodigo(piezaCodigo) == null) {
            logger.warn("No existe la pieza {} asociada al suministro.", piezaCodigo);
            throw new IllegalArgumentException("No existe ninguna pieza con ese código.");
        }

        if (proyectoDAO.buscarPorCodigo(proyectoCodigo) == null) {
            logger.warn("No existe el proyecto {} asociado al suministro.", proyectoCodigo);
            throw new IllegalArgumentException("No existe ningún proyecto con ese código.");
        }
    }

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El identificador del suministro no es válido.");
        }
    }
}
