package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.entities.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceTest {

    private ProveedorDAO proveedorDAO;
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp() {
        proveedorDAO = mock(ProveedorDAO.class);
        proveedorService = new ProveedorService(proveedorDAO);
    }

    @Test
    void registrarProveedorValido() {

        Proveedor proveedor = new Proveedor(" P001 ", " Juan ", " Pérez ", " Calle Mayor 10 ");

        proveedorService.registrar(proveedor);

        assertEquals("P001", proveedor.getCodigo());
        assertEquals("Juan", proveedor.getNombre());
        assertEquals("Pérez", proveedor.getApellidos());
        assertEquals("Calle Mayor 10", proveedor.getDireccionPostal());

        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(proveedorDAO).guardar(proveedor);
    }

    @Test
    void registrarProveedorConCodigoDuplicado() {

        Proveedor proveedor = new Proveedor("P001", "Juan", "Pérez", "Calle Mayor 10");

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);

        assertThrows(IllegalArgumentException.class, () -> proveedorService.registrar(proveedor));

        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(proveedorDAO, never()).guardar(any());
    }

    @Test
    void registrarProveedorNulo() {

        assertThrows(IllegalArgumentException.class, () -> proveedorService.registrar(null));

        verifyNoInteractions(proveedorDAO);
    }

    @Test
    void registrarProveedorConCodigoVacio() {

        Proveedor proveedor = new Proveedor(" ", "Juan", "Pérez", "Calle Mayor 10");

        assertThrows(IllegalArgumentException.class, () -> proveedorService.registrar(proveedor));

        verifyNoInteractions(proveedorDAO);
    }

    @Test
    void registrarProveedorConCodigoDemasiadoLargo() {

        String codigo = "P".repeat(21);

        Proveedor proveedor = new Proveedor(codigo, "Juan", "Pérez", "Calle Mayor 10");

        assertThrows(IllegalArgumentException.class, () -> proveedorService.registrar(proveedor));

        verifyNoInteractions(proveedorDAO);
    }

    @Test
    void buscarProveedorPorCodigo() {

        Proveedor proveedor = new Proveedor("P001", "Juan", "Pérez", "Calle Mayor 10");

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);

        Proveedor resultado = proveedorService.buscarPorCodigo(" P001 ");

        assertNotNull(resultado);
        assertEquals("P001", resultado.getCodigo());

        verify(proveedorDAO).buscarPorCodigo("P001");
    }

    @Test
    void eliminarProveedor() {

        when(proveedorDAO.eliminar("P001")).thenReturn(true);

        boolean resultado = proveedorService.eliminar(" P001 ");

        assertTrue(resultado);

        verify(proveedorDAO).eliminar("P001");
    }
}
