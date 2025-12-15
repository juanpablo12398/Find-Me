package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;
import java.util.UUID;

public class SessionUserDTO {
    private UUID id;
    private String dni;
    private String email;
    private String nombre;

    public SessionUserDTO(UUID id, String dni, String email, String nombre) {
        this.id = id;
        this.dni = dni;
        this.email = email;
        this.nombre = nombre;
    }

    public UUID getId() { return id; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
}