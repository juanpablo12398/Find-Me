package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;
import jakarta.validation.constraints.*;
import java.util.UUID;

public class AvistamientoRequestDTO {

    @NotNull(message = "El ID del avistador es obligatorio")
    private UUID avistadorId;

    @NotNull(message = "El ID del desaparecido es obligatorio")
    private UUID desaparecidoId;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String fotoUrl;  // Opcional

    private Boolean publico;  // Si no viene, default TRUE

    // Constructor vacío
    public AvistamientoRequestDTO() {}

    // Getters y Setters
    public UUID getAvistadorId() {
        return avistadorId;
    }

    public void setAvistadorId(UUID avistadorId) {
        this.avistadorId = avistadorId;
    }

    public UUID getDesaparecidoId() {
        return desaparecidoId;
    }

    public void setDesaparecidoId(UUID desaparecidoId) {
        this.desaparecidoId = desaparecidoId;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
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

    public Boolean getPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }
}
