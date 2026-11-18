package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.GestionDAO;
import org.example.gestionProyectos.dao.PiezaDAO;
import org.example.gestionProyectos.dao.ProveedorDAO;
import org.example.gestionProyectos.dao.ProyectoDAO;
import org.example.gestionProyectos.entities.Gestion;
import org.example.gestionProyectos.entities.Pieza;
import org.example.gestionProyectos.entities.Proveedor;
import org.example.gestionProyectos.entities.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionServiceTest {

    private GestionDAO gestionDAO;
    private ProveedorDAO proveedorDAO;
    private PiezaDAO piezaDAO;
    private ProyectoDAO proyectoDAO;
    private GestionService gestionService;

    private Proveedor proveedor;
    private Pieza pieza;
    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        gestionDAO = mock(GestionDAO.class);
        proveedorDAO = mock(ProveedorDAO.class);
        piezaDAO = mock(PiezaDAO.class);
        proyectoDAO = mock(ProyectoDAO.class);

        gestionService = new GestionService(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);

        proveedor = new Proveedor("P001", "Juan", "Pérez", "Calle Mayor 10");

        pieza = new Pieza("PI001", "Tornillo", new BigDecimal("2.50"), "Descripción");

        proyecto = new Proyecto("PR001", "Edificio nuevo", "Vitoria");
    }

    @Test
    void registrarGestionValida() {
        LocalDate fecha = LocalDate.of(2026, 11, 17);

        Gestion gestion = new Gestion(fecha, 10, proveedor, pieza, proyecto);

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);
        when(piezaDAO.buscarPorCodigo("PI001")).thenReturn(pieza);
        when(proyectoDAO.buscarPorCodigo("PR001")).thenReturn(proyecto);
        when(gestionDAO.guardar(any(Gestion.class))).thenReturn(1L);

        Long resultado = gestionService.registrar(gestion);

        assertEquals(1L, resultado);

        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(piezaDAO).buscarPorCodigo("PI001");
        verify(proyectoDAO).buscarPorCodigo("PR001");

        ArgumentCaptor<Gestion> captor = ArgumentCaptor.forClass(Gestion.class);

        verify(gestionDAO).guardar(captor.capture());

        Gestion guardada = captor.getValue();

        assertEquals(fecha, guardada.getFecha());
        assertEquals(10, guardada.getCantidad());
        assertSame(proveedor, guardada.getProveedor());
        assertSame(pieza, guardada.getPieza());
        assertSame(proyecto, guardada.getProyecto());
    }

    @Test
    void registrarGestionNula() {
        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(null));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionSinFecha() {
        Gestion gestion = new Gestion(null, 10, proveedor, pieza, proyecto);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionConCantidadCero() {
        Gestion gestion = new Gestion(LocalDate.now(), 0, proveedor, pieza, proyecto);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionConCantidadNegativa() {
        Gestion gestion = new Gestion(LocalDate.now(), -5, proveedor, pieza, proyecto);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionSinProveedor() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, null, pieza, proyecto);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionSinPieza() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, null, proyecto);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionSinProyecto() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, pieza, null);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionConProveedorInexistente() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto);

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verify(proveedorDAO).buscarPorCodigo("P001");
        verifyNoInteractions(gestionDAO);
        verifyNoInteractions(piezaDAO, proyectoDAO);
    }

    @Test
    void registrarGestionConPiezaInexistente() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto);

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);
        when(piezaDAO.buscarPorCodigo("PI001")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(piezaDAO).buscarPorCodigo("PI001");
        verifyNoInteractions(gestionDAO, proyectoDAO);
    }

    @Test
    void registrarGestionConProyectoInexistente() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto);

        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);
        when(piezaDAO.buscarPorCodigo("PI001")).thenReturn(pieza);
        when(proyectoDAO.buscarPorCodigo("PR001")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gestionService.registrar(gestion));

        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(piezaDAO).buscarPorCodigo("PI001");
        verify(proyectoDAO).buscarPorCodigo("PR001");
        verifyNoInteractions(gestionDAO);
    }

    @Test
    void buscarGestionPorId() {
        Gestion gestion = new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto);

        when(gestionDAO.buscarPorId(1L)).thenReturn(gestion);

        Gestion resultado = gestionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());

        verify(gestionDAO).buscarPorId(1L);
    }

    @Test
    void buscarGestionConIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> gestionService.buscarPorId(0L));

        verifyNoInteractions(gestionDAO);
    }

    @Test
    void eliminarGestion() {
        when(gestionDAO.eliminar(1L)).thenReturn(true);

        boolean resultado = gestionService.eliminar(1L);

        assertTrue(resultado);
        verify(gestionDAO).eliminar(1L);
    }

    @Test
    void actualizarGestionInexistente() {
        when(gestionDAO.buscarPorId(1L)).thenReturn(null);

        boolean resultado = gestionService.actualizar(1L, LocalDate.now(), 20, "P001", "PI001", "PR001");

        assertFalse(resultado);

        verify(gestionDAO).buscarPorId(1L);
        verifyNoInteractions(proveedorDAO, piezaDAO, proyectoDAO);
        verify(gestionDAO, never()).actualizar(anyLong(), any(), any(), anyString(), anyString(), anyString());
    }

    @Test
    void actualizarGestionValida() {
        Gestion gestionExistente = new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto);

        when(gestionDAO.buscarPorId(1L)).thenReturn(gestionExistente);
        when(proveedorDAO.buscarPorCodigo("P001")).thenReturn(proveedor);
        when(piezaDAO.buscarPorCodigo("PI001")).thenReturn(pieza);
        when(proyectoDAO.buscarPorCodigo("PR001")).thenReturn(proyecto);
        when(gestionDAO.actualizar(1L, LocalDate.of(2026, 11, 17), 20, "P001", "PI001", "PR001")).thenReturn(true);

        boolean resultado = gestionService.actualizar(1L, LocalDate.of(2026, 11, 17), 20, " P001 ", " PI001 ", " PR001 ");

        assertTrue(resultado);

        verify(gestionDAO).buscarPorId(1L);
        verify(proveedorDAO).buscarPorCodigo("P001");
        verify(piezaDAO).buscarPorCodigo("PI001");
        verify(proyectoDAO).buscarPorCodigo("PR001");
        verify(gestionDAO).actualizar(1L, LocalDate.of(2026, 11, 17), 20, "P001", "PI001", "PR001");
    }

    @Test
    void actualizarGestionConCantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> gestionService.actualizar(1L, LocalDate.now(), 0, "P001", "PI001", "PR001"));

        verifyNoInteractions(gestionDAO, proveedorDAO, piezaDAO, proyectoDAO);
    }
}
