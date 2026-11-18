package org.example.gestionProyectos.services;

import org.example.gestionProyectos.dao.ProyectoDAO;
import org.example.gestionProyectos.entities.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoServiceTest {

    private ProyectoDAO proyectoDAO;
    private ProyectoService proyectoService;

    @BeforeEach
    void setUp() {
        proyectoDAO = mock(ProyectoDAO.class);
        proyectoService = new ProyectoService(proyectoDAO);
    }

    @Test
    void registrarProyectoValido() {
        Proyecto proyecto = new Proyecto(" PR001 ", " Edificio nuevo ", " Vitoria ");

        proyectoService.registrar(proyecto);

        assertEquals("PR001", proyecto.getCodigo());
        assertEquals("Edificio nuevo", proyecto.getNombre());
        assertEquals("Vitoria", proyecto.getCiudad());

        verify(proyectoDAO).buscarPorCodigo("PR001");
        verify(proyectoDAO).guardar(proyecto);
    }

    @Test
    void registrarProyectoConCodigoDuplicado() {
        Proyecto proyecto = new Proyecto("PR001", "Edificio nuevo", "Vitoria");

        when(proyectoDAO.buscarPorCodigo("PR001")).thenReturn(proyecto);

        assertThrows(IllegalArgumentException.class, () -> proyectoService.registrar(proyecto));

        verify(proyectoDAO).buscarPorCodigo("PR001");
        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoNulo() {
        assertThrows(IllegalArgumentException.class, () -> proyectoService.registrar(null));

        verifyNoInteractions(proyectoDAO);
    }

    @Test
    void registrarProyectoConCodigoVacio() {
        Proyecto proyecto = new Proyecto(" ", "Edificio nuevo", "Vitoria");

        assertThrows(IllegalArgumentException.class, () -> proyectoService.registrar(proyecto));

        verifyNoInteractions(proyectoDAO);
    }

    @Test
    void registrarProyectoConCodigoDemasiadoLargo() {
        Proyecto proyecto = new Proyecto("P".repeat(21), "Edificio nuevo", "Vitoria");

        assertThrows(IllegalArgumentException.class, () -> proyectoService.registrar(proyecto));

        verifyNoInteractions(proyectoDAO);
    }

    @Test
    void registrarProyectoConNombreVacio() {
        Proyecto proyecto = new Proyecto("PR001", " ", "Vitoria");

        assertThrows(IllegalArgumentException.class, () -> proyectoService.registrar(proyecto));

        verifyNoInteractions(proyectoDAO);
    }

    @Test
    void buscarProyectoPorCodigo() {
        Proyecto proyecto = new Proyecto("PR001", "Edificio nuevo", "Vitoria");

        when(proyectoDAO.buscarPorCodigo("PR001")).thenReturn(proyecto);

        Proyecto resultado = proyectoService.buscarPorCodigo(" PR001 ");

        assertNotNull(resultado);
        assertEquals("PR001", resultado.getCodigo());

        verify(proyectoDAO).buscarPorCodigo("PR001");
    }

    @Test
    void buscarProyectosPorCiudad() {
        when(proyectoDAO.buscarPorCiudad("Vitoria")).thenReturn(java.util.List.of(new Proyecto("PR001", "Edificio nuevo", "Vitoria")));

        var resultado = proyectoService.buscarPorCiudad(" Vitoria ");

        assertEquals(1, resultado.size());
        assertEquals("Vitoria", resultado.get(0).getCiudad());

        verify(proyectoDAO).buscarPorCiudad("Vitoria");
    }

    @Test
    void buscarPorCiudadVacia() {
        assertThrows(IllegalArgumentException.class, () -> proyectoService.buscarPorCiudad(" "));

        verifyNoInteractions(proyectoDAO);
    }

    @Test
    void eliminarProyecto() {
        when(proyectoDAO.eliminar("PR001")).thenReturn(true);

        boolean resultado = proyectoService.eliminar(" PR001 ");

        assertTrue(resultado);
        verify(proyectoDAO).eliminar("PR001");
    }

    @Test
    void actualizarProyecto() {
        Proyecto proyecto = new Proyecto("PR001", "Edificio actualizado", "Bilbao");

        when(proyectoDAO.actualizar(proyecto)).thenReturn(true);

        boolean resultado = proyectoService.actualizar(proyecto);

        assertTrue(resultado);
        verify(proyectoDAO).actualizar(proyecto);
    }
}
