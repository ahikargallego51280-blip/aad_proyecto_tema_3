package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.ProyectoDAO;
import org.example.gestionProyectos.entities.Proyecto;
import org.hibernate.exception.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ProyectoService {

    private final ProyectoDAO proyectoDAO;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoService.class);

    public ProyectoService() {
        this(new ProyectoDAO());
    }

    public ProyectoService(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = java.util.Objects.requireNonNull(proyectoDAO);
    }


    public void registrar(Proyecto proyecto) {

        validarProyecto(proyecto);

        if (proyectoDAO.buscarPorCodigo(proyecto.getCodigo()) != null) {
            logger.warn("Intento de registrar un proyecto con código duplicado: {}", proyecto.getCodigo());
            throw new IllegalArgumentException("Ya existe un proyecto con ese código.");
        }

        proyectoDAO.guardar(proyecto);
        logger.info("Proyecto registrado correctamente: {}", proyecto.getCodigo());
    }


    public boolean eliminar(String codigo) {

        validarCodigo(codigo);

        try {
            boolean eliminado = proyectoDAO.eliminar(codigo.trim());

            if (eliminado) {
                logger.info("Proyecto eliminado: {}", codigo.trim());
            } else {
                logger.warn("No se encontró el proyecto que se intentaba eliminar: {}", codigo.trim());
            }

            return eliminado;

        } catch (RuntimeException e) {

            if (esViolacionIntegridad(e)) {
                throw new IllegalStateException("No se puede eliminar el proyecto porque tiene suministros asociados.");
            }

            throw e;
        }
    }


    public boolean actualizar(Proyecto proyecto) {

        validarProyecto(proyecto);

        boolean actualizado = proyectoDAO.actualizar(proyecto);

        if (actualizado) {
            logger.info("Proyecto actualizado: {}", proyecto.getCodigo());
        } else {
            logger.warn("No se encontró el proyecto que se intentaba actualizar: {}", proyecto.getCodigo());
        }

        return actualizado;
    }


    public Proyecto buscarPorCodigo(String codigo) {

        validarCodigo(codigo);

        return proyectoDAO.buscarPorCodigo(codigo.trim());
    }


    public List<Proyecto> buscarPorNombre(String nombre) {

        validarBusqueda(nombre, "nombre");

        return proyectoDAO.buscarPorNombre(nombre.trim());
    }


    public List<Proyecto> buscarPorCiudad(String ciudad) {

        validarBusqueda(ciudad, "ciudad");

        return proyectoDAO.buscarPorCiudad(ciudad.trim());
    }


    public List<Proyecto> listarTodos() {

        return proyectoDAO.listarTodos();
    }


    private void validarProyecto(Proyecto proyecto) {

        if (proyecto == null) {
            throw new IllegalArgumentException("Los datos del proyecto no pueden estar vacíos.");
        }

        proyecto.setCodigo(validarCampo(proyecto.getCodigo(), "código", 20));

        proyecto.setNombre(validarCampo(proyecto.getNombre(), "nombre", 100));

        proyecto.setCiudad(validarCampo(proyecto.getCiudad(), "ciudad", 100));
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
            throw new IllegalArgumentException("Debes introducir una " + campo + " para realizar la búsqueda.");
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
