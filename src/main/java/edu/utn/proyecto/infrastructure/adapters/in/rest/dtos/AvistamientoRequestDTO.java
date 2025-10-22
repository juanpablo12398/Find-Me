package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;
import jakarta.validation.constraints.*;

public class AvistamientoRequestDTO {

    @NotBlank(message = "El ID del avistador es obligatorio")
    private String avistadorId;

    @NotBlank(message = "El ID del desaparecido es obligatorio")
    private String desaparecidoId;

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

    public String getAvistadorId() {
        return avistadorId;
    }

    public String getDesaparecidoId() {
        return desaparecidoId;
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

    public void setAvistadorId(String avistadorId) {
        this.avistadorId = avistadorId;
    }

    public void setDesaparecidoId(String desaparecidoId) {
        this.desaparecidoId = desaparecidoId;
    }
}
