package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.entities.Proveedor;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProveedorService {

    private final ProveedorDAO proveedorDAO;
    private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);

    public ProveedorService() {
        this(new ProveedorDAO());
    }

    public ProveedorService(ProveedorDAO proveedorDAO) {
        this.proveedorDAO = java.util.Objects.requireNonNull(proveedorDAO);
    }

    public void registrar(Proveedor proveedor) {

        validarProveedor(proveedor);

        if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) != null) {
            logger.warn("Intento de registrar un proveedor con código duplicado: {}", proveedor.getCodigo());
            throw new IllegalArgumentException("Ya existe un proveedor con ese código.");
        }

        proveedorDAO.guardar(proveedor);
        logger.info("Proveedor registrado correctamente: {}", proveedor.getCodigo());
    }


    public boolean eliminar(String codigo) {

        validarCodigo(codigo);

        try {
            boolean eliminado = proveedorDAO.eliminar(codigo.trim());

            if (eliminado) {
                logger.info("Proveedor eliminado: {}", codigo.trim());
            } else {
                logger.warn("No se encontró el proveedor que se intentaba eliminar: {}", codigo.trim());
            }

            return eliminado;

        } catch (RuntimeException e) {

            if (esViolacionIntegridad(e)) {
                throw new IllegalStateException("No se puede eliminar el proveedor porque tiene suministros asociados.");
            }

            throw e;
        }
    }


    public boolean actualizar(Proveedor proveedor) {
        validarProveedor(proveedor);
        boolean actualizado = proveedorDAO.actualizar(proveedor);

        if (actualizado) {
            logger.info("Proveedor actualizado: {}", proveedor.getCodigo());
        } else {
            logger.warn("No se encontró el proveedor que se intentaba actualizar: {}", proveedor.getCodigo());
        }

        return actualizado;
    }

    public Proveedor buscarPorCodigo(String codigo) {
        validarCodigo(codigo);
        return proveedorDAO.buscarPorCodigo(codigo.trim());
    }

    public List<Proveedor> buscarPorNombre(String nombre) {
        validarBusqueda(nombre, "nombre");
        return proveedorDAO.buscarPorNombre(nombre.trim());
    }

    public List<Proveedor> buscarPorDireccion(String direccion) {
        validarBusqueda(direccion, "dirección");
        return proveedorDAO.buscarPorDireccion(direccion.trim());
    }

    public List<Proveedor> listarTodos() {
        return proveedorDAO.listarTodos();
    }


    private void validarProveedor(Proveedor proveedor) {

        if (proveedor == null) {
            throw new IllegalArgumentException("Los datos del proveedor no pueden estar vacíos.");
        }

        proveedor.setCodigo(validarCampo(proveedor.getCodigo(), "código", 20));
        proveedor.setNombre(validarCampo(proveedor.getNombre(), "nombre", 100));
        proveedor.setApellidos(validarCampo(proveedor.getApellidos(), "apellidos", 150));
        proveedor.setDireccionPostal(validarCampo(proveedor.getDireccionPostal(), "dirección postal", 255));
    }

    private String validarCampo(String valor, String campo, int longitudMaxima) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }

        String valorLimpio = valor.trim();

        if (valorLimpio.length() > longitudMaxima) {
            throw new IllegalArgumentException("El campo " + campo + " no puede superar los " + longitudMaxima + " caracteres.");
        }

        return valorLimpio;
    }

    private void validarCodigo(String codigo) {
        validarCampo(codigo, "código", 20);
    }

    private void validarBusqueda(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes introducir un " + campo + " para realizar la búsqueda.");
        }
    }


    private boolean esViolacionIntegridad(Throwable error) {

        Throwable actual = error;

        while (actual != null) {

            if (actual instanceof ConstraintViolationException) {
                return true;
            }

            if (actual instanceof SQLException sqlException) {

                String sqlState = sqlException.getSQLState();

                if (sqlState != null && sqlState.startsWith("23")) {
                    return true;
                }
            }

            actual = actual.getCause();
        }

        return false;
    }

}
