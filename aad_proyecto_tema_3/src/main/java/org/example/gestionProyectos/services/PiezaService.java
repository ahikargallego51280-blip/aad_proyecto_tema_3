package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.PiezaDAO;
import org.example.gestionProyectos.entities.Pieza;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class PiezaService {

    private final PiezaDAO piezaDAO;

    public PiezaService() {
        this.piezaDAO = new PiezaDAO();
    }


    public void registrar(Pieza pieza) {

        validarPieza(pieza);

        if (piezaDAO.buscarPorCodigo(pieza.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una pieza con ese código.");
        }

        piezaDAO.guardar(pieza);
    }

    public boolean eliminar(String codigo) {

        validarCodigo(codigo);

        try {
            return piezaDAO.eliminar(codigo.trim());
        } catch (RuntimeException e) {

            if (esViolacionIntegridad(e)) {
                throw new IllegalStateException("No se puede eliminar la pieza porque tiene suministros asociados.");
            }

            throw e;
        }
    }

    public boolean actualizar(Pieza pieza) {

        validarPieza(pieza);
        return piezaDAO.actualizar(pieza);
    }

    public Pieza buscarPorCodigo(String codigo) {

        validarCodigo(codigo);
        return piezaDAO.buscarPorCodigo(codigo.trim());
    }

    public List<Pieza> buscarPorNombre(String nombre) {

        validarBusqueda(nombre);
        return piezaDAO.buscarPorNombre(nombre.trim());
    }

    public List<Pieza> listarTodos() {

        return piezaDAO.listarTodos();
    }


    private void validarPieza(Pieza pieza) {

        if (pieza == null) {
            throw new IllegalArgumentException("Los datos de la pieza no pueden estar vacíos.");
        }

        pieza.setCodigo(validarCampo(pieza.getCodigo(), "código", 20));
        pieza.setNombre(validarCampo(pieza.getNombre(), "nombre", 100));
        validarPrecio(pieza);

        if (pieza.getDescripcion() != null) {

            String descripcion = pieza.getDescripcion().trim();

            if (descripcion.length() > 1000) {
                throw new IllegalArgumentException("La descripción no puede superar los 1000 caracteres.");
            }

            pieza.setDescripcion(descripcion.isEmpty() ? null : descripcion);
        }
    }

    private void validarPrecio(Pieza pieza) {

        BigDecimal precio = pieza.getPrecio();

        if (precio == null) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }

        try {
            precio = precio.setScale(2, RoundingMode.UNNECESSARY);

        } catch (ArithmeticException e) {

            throw new IllegalArgumentException("El precio no puede tener más de dos decimales.");
        }

        if (precio.precision() > 10) {
            throw new IllegalArgumentException("El precio supera el máximo permitido.");
        }

        pieza.setPrecio(precio);
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

    private void validarBusqueda(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes introducir un nombre para realizar la búsqueda.");
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
