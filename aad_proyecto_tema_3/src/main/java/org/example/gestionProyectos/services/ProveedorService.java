package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.entities.Proveedor;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class ProveedorService {

    private final ProveedorDAO proveedorDAO;

    public ProveedorService() {
        this.proveedorDAO = new ProveedorDAO();
    }

    public void registrar(Proveedor proveedor) {

        validarProveedor(proveedor);

        if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese código.");
        }

        proveedorDAO.guardar(proveedor);
    }

    public boolean eliminar(String codigo) {

        validarCodigo(codigo);

        try {
            return proveedorDAO.eliminar(codigo.trim());

        } catch (ConstraintViolationException e) {
            throw new IllegalStateException("No se puede eliminar el proveedor porque tiene suministros asociados.");
        }
    }

    public boolean actualizar(Proveedor proveedor) {

        validarProveedor(proveedor);
        return proveedorDAO.actualizar(proveedor);
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
}
