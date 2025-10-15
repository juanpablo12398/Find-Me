package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avistamientos", schema = "public")
public class AvistamientoEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "avistador_id", nullable = false)
    private UUID avistadorId;

    @Column(name = "desaparecido_id", nullable = false)
    private UUID desaparecidoId;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(nullable = false)
    private Boolean verificado = false;

    @Column(nullable = false)
    private Boolean publico = true;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    public AvistamientoEntity() {}

    public AvistamientoEntity(UUID id, UUID avistadorId, UUID desaparecidoId, Double latitud, Double longitud, LocalDateTime fechaHora, String descripcion, String fotoUrl, Boolean verificado, Boolean publico, LocalDateTime creadoEn) {
        this.id = id;
        this.avistadorId = avistadorId;
        this.desaparecidoId = desaparecidoId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        this.verificado = verificado;
        this.publico = publico;
        this.creadoEn = creadoEn;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAvistadorId() { return avistadorId; }
    public void setAvistadorId(UUID avistadorId) { this.avistadorId = avistadorId; }

    public UUID getDesaparecidoId() { return desaparecidoId; }
    public void setDesaparecidoId(UUID desaparecidoId) { this.desaparecidoId = desaparecidoId; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Boolean getVerificado() { return verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }

    public Boolean getPublico() { return publico; }
    public void setPublico(Boolean publico) { this.publico = publico; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
