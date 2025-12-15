package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;
import java.util.UUID;

public class AvistamientoFrontDTO {

    private UUID id;

    // Datos geográficos
    private Double latitud;
    private Double longitud;

    // Datos del avistamiento
    private String fechaFormateada;  // "dd/MM/yyyy HH:mm"
    private String descripcion;
    private String fotoUrl;
    private Boolean verificado;

    // Datos del desaparecido (para el popup del mapa)
    private UUID desaparecidoId;
    private String desaparecidoNombre;
    private String desaparecidoApellido;
    private String desaparecidoFoto;

    // Datos del avistador (para mostrar "Reportado por...")
    private String avistadorNombre;

    public AvistamientoFrontDTO() {}

    public AvistamientoFrontDTO(
            UUID id,
            Double latitud,
            Double longitud,
            String fechaFormateada,
            String descripcion,
            String fotoUrl,
            Boolean verificado
    ) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaFormateada = fechaFormateada;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        this.verificado = verificado;
    }

    public AvistamientoFrontDTO(
            UUID id,
            Double latitud,
            Double longitud,
            String fechaFormateada,
            String descripcion,
            String fotoUrl,
            Boolean verificado,
            UUID desaparecidoId,
            String desaparecidoNombre,
            String desaparecidoApellido,
            String desaparecidoFoto,
            String avistadorNombre
    ) {
        this(id, latitud, longitud, fechaFormateada, descripcion, fotoUrl, verificado);
        this.desaparecidoId = desaparecidoId;
        this.desaparecidoNombre = desaparecidoNombre;
        this.desaparecidoApellido = desaparecidoApellido;
        this.desaparecidoFoto = desaparecidoFoto;
        this.avistadorNombre = avistadorNombre;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getFechaFormateada() { return fechaFormateada; }
    public void setFechaFormateada(String fechaFormateada) { this.fechaFormateada = fechaFormateada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Boolean getVerificado() { return verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }

    public UUID getDesaparecidoId() { return desaparecidoId; }
    public void setDesaparecidoId(UUID desaparecidoId) { this.desaparecidoId = desaparecidoId; }

    public String getDesaparecidoNombre() { return desaparecidoNombre; }
    public void setDesaparecidoNombre(String desaparecidoNombre) { this.desaparecidoNombre = desaparecidoNombre; }

    public String getDesaparecidoApellido() { return desaparecidoApellido; }
    public void setDesaparecidoApellido(String desaparecidoApellido) { this.desaparecidoApellido = desaparecidoApellido; }

    public String getDesaparecidoFoto() { return desaparecidoFoto; }
    public void setDesaparecidoFoto(String desaparecidoFoto) { this.desaparecidoFoto = desaparecidoFoto; }

    public String getAvistadorNombre() { return avistadorNombre; }
    public void setAvistadorNombre(String avistadorNombre) { this.avistadorNombre = avistadorNombre; }
}
