package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.PiezaDAO;
import org.example.gestionProyectos.entities.Pieza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PiezaServiceTest {

    private PiezaDAO piezaDAO;
    private PiezaService piezaService;

    @BeforeEach
    void setUp() {
        piezaDAO = mock(PiezaDAO.class);
        piezaService = new PiezaService(piezaDAO);
    }

    @Test
    void registrarPiezaValida() {
        Pieza pieza = new Pieza(" P001 ", " Tornillo ", new BigDecimal("2.50"), " Descripción de prueba ");

        piezaService.registrar(pieza);

        assertEquals("P001", pieza.getCodigo());
        assertEquals("Tornillo", pieza.getNombre());
        assertEquals(new BigDecimal("2.50"), pieza.getPrecio());
        assertEquals("Descripción de prueba", pieza.getDescripcion());

        verify(piezaDAO).buscarPorCodigo("P001");
        verify(piezaDAO).guardar(pieza);
    }

    @Test
    void registrarPiezaConCodigoDuplicado() {
        Pieza pieza = new Pieza("P001", "Tornillo", new BigDecimal("2.50"), "Descripción");

        when(piezaDAO.buscarPorCodigo("P001")).thenReturn(pieza);

        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(pieza));

        verify(piezaDAO).buscarPorCodigo("P001");
        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaNula() {
        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(null));

        verifyNoInteractions(piezaDAO);
    }

    @Test
    void registrarPiezaConCodigoVacio() {
        Pieza pieza = new Pieza(" ", "Tornillo", new BigDecimal("2.50"), "Descripción");

        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(pieza));

        verifyNoInteractions(piezaDAO);
    }

    @Test
    void registrarPiezaConPrecioCero() {
        Pieza pieza = new Pieza("P001", "Tornillo", BigDecimal.ZERO, "Descripción");

        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(pieza));

        verifyNoInteractions(piezaDAO);
    }

    @Test
    void registrarPiezaConPrecioNegativo() {
        Pieza pieza = new Pieza("P001", "Tornillo", new BigDecimal("-1.00"), "Descripción");

        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(pieza));

        verifyNoInteractions(piezaDAO);
    }

    @Test
    void registrarPiezaConMasDeDosDecimales() {
        Pieza pieza = new Pieza("P001", "Tornillo", new BigDecimal("2.555"), "Descripción");

        assertThrows(IllegalArgumentException.class, () -> piezaService.registrar(pieza));

        verifyNoInteractions(piezaDAO);
    }

    @Test
    void buscarPiezaPorCodigo() {
        Pieza pieza = new Pieza("P001", "Tornillo", new BigDecimal("2.50"), "Descripción");

        when(piezaDAO.buscarPorCodigo("P001")).thenReturn(pieza);

        Pieza resultado = piezaService.buscarPorCodigo(" P001 ");

        assertNotNull(resultado);
        assertEquals("P001", resultado.getCodigo());

        verify(piezaDAO).buscarPorCodigo("P001");
    }

    @Test
    void eliminarPieza() {
        when(piezaDAO.eliminar("P001")).thenReturn(true);

        boolean resultado = piezaService.eliminar(" P001 ");

        assertTrue(resultado);
        verify(piezaDAO).eliminar("P001");
    }
}
