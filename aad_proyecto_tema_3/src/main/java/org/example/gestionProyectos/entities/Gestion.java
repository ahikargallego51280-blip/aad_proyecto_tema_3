package org.example.gestionProyectos.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "gestiones")
public class Gestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_codigo", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pieza_codigo", nullable = false)
    private Pieza pieza;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_codigo", nullable = false)
    private Proyecto proyecto;

    public Gestion() {
    }

    public Gestion(LocalDate fecha, Integer cantidad, Proveedor proveedor, Pieza pieza, Proyecto proyecto) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        setProveedor(proveedor);
        setPieza(pieza);
        setProyecto(proyecto);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        if (this.proveedor != null) {
            this.proveedor.getGestiones().remove(this);
        }

        this.proveedor = proveedor;

        if (proveedor != null) {
            proveedor.getGestiones().add(this);
        }
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        if (this.pieza != null) {
            this.pieza.getGestiones().remove(this);
        }

        this.pieza = pieza;

        if (pieza != null) {
            pieza.getGestiones().add(this);
        }
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        if (this.proyecto != null) {
            this.proyecto.getGestiones().remove(this);
        }

        this.proyecto = proyecto;

        if (proyecto != null) {
            proyecto.getGestiones().add(this);
        }
    }

    @Override
    public String toString() {
        return "Gestion{" + "id=" + id + ", fecha=" + fecha + ", cantidad=" + cantidad + '}';
    }
}
