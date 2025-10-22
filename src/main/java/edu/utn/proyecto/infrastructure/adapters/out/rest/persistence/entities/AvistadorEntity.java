// AvistadorEntity.java
package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "avistadores", schema = "public",
        uniqueConstraints = @UniqueConstraint(name="uq_avistadores_dni", columnNames="dni"))
public class AvistadorEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String dni;

    @Column(nullable=false)
    private String nombre;

    @Column(nullable=false)
    private String apellido;

    private String direccion;
    private Integer edad;
    private String email;
    private String telefono;

    @Column(nullable=false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    public AvistadorEntity() {}

    public AvistadorEntity(UUID id, String dni, String nombre, String apellido, String direccion, Integer edad, String email, String telefono, LocalDateTime creadoEn) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.edad = edad;
        this.email = email;
        this.telefono = telefono;
        this.creadoEn = creadoEn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
