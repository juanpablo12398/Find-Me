package edu.utn.proyecto.domain.model.concreta;
import java.time.LocalDateTime;
import java.util.UUID;

public class Avistamiento {
    private UUID id;
    private UUID avistadorId;      // quién lo reportó
    private UUID desaparecidoId;   // a quién vio

    // Datos geográficos
    private Double latitud;
    private Double longitud;

    // Detalles del avistamiento
    private LocalDateTime fechaHora;
    private String descripcion;
    private String fotoUrl;
    private Boolean verificado;
    private Boolean publico;
    private LocalDateTime creadoEn;

    public Avistamiento() {
        this.fechaHora = LocalDateTime.now();
        this.creadoEn = LocalDateTime.now();
        this.verificado = false;
        this.publico = true;
    }

    public Avistamiento(UUID avistadorId, UUID desaparecidoId,
                        Double latitud, Double longitud, String descripcion) {
        this.avistadorId = avistadorId;
        this.desaparecidoId = desaparecidoId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
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
