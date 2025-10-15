package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "desaparecidos")
public class DesaparecidoEntity {
    @Id
    @GeneratedValue
    // @JdbcTypeCode(SqlTypes.UUID)
    // Si hay problemas con el mapeo de UUID, descomentar la línea anterior
    private UUID id;
    private String nombre;
    private String apellido;
    private String dni;
    private String descripcion;
    private String fotoUrl;
    private LocalDateTime fechaDesaparicion;

    public DesaparecidoEntity() {}

    public DesaparecidoEntity(UUID id, String nombre, String apellido, String dni, String descripcion, String fotoUrl, LocalDateTime fechaDesaparicion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        this.fechaDesaparicion = fechaDesaparicion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public LocalDateTime getFechaDesaparicion() {
        return fechaDesaparicion;
    }

    public void setFechaDesaparicion(LocalDateTime fechaDesaparicion) {
        this.fechaDesaparicion = fechaDesaparicion;
    }
}
