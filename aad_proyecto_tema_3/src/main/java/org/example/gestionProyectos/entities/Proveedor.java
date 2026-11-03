package org.example.gestionProyectos.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 150)
    private String apellidos;

    @Column(name = "direccion_postal", nullable = false, length = 255)
    private String direccionPostal;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private Set<Gestion> gestiones = new HashSet<>();

    public Proveedor() {
    }

    public Proveedor(String codigo, String nombre, String apellidos, String direccionPostal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccionPostal = direccionPostal;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccionPostal() {
        return direccionPostal;
    }

    public void setDireccionPostal(String direccionPostal) {
        this.direccionPostal = direccionPostal;
    }

    public Set<Gestion> getGestiones() {
        return gestiones;
    }

    public void addGestion(Gestion gestion) {
        gestion.setProveedor(this);
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccionPostal='" + direccionPostal + '\'' +
                '}';
    }
}
