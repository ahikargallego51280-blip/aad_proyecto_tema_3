package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.GestionDAO;
import org.example.gestionProyectos.dao.PiezaDAO;
import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.dao.ProyectoDAO;
import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;

import java.time.LocalDate;
import java.util.List;

public class GestionService {

    private final GestionDAO gestionDAO;
    private final ProveedorDAO proveedorDAO;
    private final PiezaDAO piezaDAO;
    private final ProyectoDAO proyectoDAO;

    public GestionService() {
        this.gestionDAO = new GestionDAO();
        this.proveedorDAO = new ProveedorDAO();
        this.piezaDAO = new PiezaDAO();
        this.proyectoDAO = new ProyectoDAO();
    }

    public Long registrar(Gestion gestion) {

        validarGestion(gestion);
        Proveedor proveedor = proveedorDAO.buscarPorCodigo(gestion.getProveedor().getCodigo());

        if (proveedor == null) {
            throw new IllegalArgumentException("No existe ningún proveedor con ese código.");
        }

        Pieza pieza = piezaDAO.buscarPorCodigo(gestion.getPieza().getCodigo());

        if (pieza == null) {
            throw new IllegalArgumentException("No existe ninguna pieza con ese código.");
        }

        Proyecto proyecto = proyectoDAO.buscarPorCodigo(gestion.getProyecto().getCodigo());

        if (proyecto == null) {
            throw new IllegalArgumentException("No existe ningún proyecto con ese código.");
        }

        Gestion nuevaGestion = new Gestion(gestion.getFecha(), gestion.getCantidad(), proveedor, pieza, proyecto);
        return gestionDAO.guardar(nuevaGestion);
    }

    public boolean eliminar(Long id) {

        validarId(id);
        return gestionDAO.eliminar(id);
    }

    public boolean actualizar(Long id, LocalDate fecha, Integer cantidad, String proveedorCodigo, String piezaCodigo, String proyectoCodigo) {

        validarId(id);
        validarDatos(fecha, cantidad);
        proveedorCodigo = validarCodigoRelacionado(proveedorCodigo, "proveedor");
        piezaCodigo = validarCodigoRelacionado(piezaCodigo, "pieza");
        proyectoCodigo = validarCodigoRelacionado(proyectoCodigo, "proyecto");
        comprobarReferencias(proveedorCodigo, piezaCodigo, proyectoCodigo);
        return gestionDAO.actualizar(id, fecha, cantidad, proveedorCodigo, piezaCodigo, proyectoCodigo);
    }

    public Gestion buscarPorId(Long id) {

        validarId(id);
        return gestionDAO.buscarPorId(id);
    }

    public List<Gestion> buscarPorProveedor(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "proveedor");
        return gestionDAO.buscarPorProveedor(codigo);
    }

    public List<Gestion> buscarPorPieza(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "pieza");
        return gestionDAO.buscarPorPieza(codigo);
    }

    public List<Gestion> buscarPorProyecto(String codigo) {

        codigo = validarCodigoRelacionado(codigo, "proyecto");
        return gestionDAO.buscarPorProyecto(codigo);
    }

    public List<Gestion> listarTodos() {
        return gestionDAO.listarTodos();
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
            throw new IllegalArgumentException("No existe ningún proveedor con ese código.");
        }

        if (piezaDAO.buscarPorCodigo(piezaCodigo) == null) {
            throw new IllegalArgumentException("No existe ninguna pieza con ese código.");
        }

        if (proyectoDAO.buscarPorCodigo(proyectoCodigo) == null) {
            throw new IllegalArgumentException("No existe ningún proyecto con ese código.");
        }
    }

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El identificador del suministro no es válido.");
        }
    }
}
