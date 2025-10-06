package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.domain.model.concretas.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class AvistadorMapper {

    // Request -> Dominio
    public Avistador fromRequestToDomain(AvistadorRequestDTO dto) {
        Avistador d = new Avistador();
        d.setDni(dto.getDni());
        d.setNombre(dto.getNombre());
        d.setApellido(dto.getApellido());
        d.setEdad(dto.getEdad());
        d.setDireccion(dto.getDireccion());
        d.setEmail(dto.getEmail());
        d.setTelefono(dto.getTelefono());
        d.setCreadoEn(LocalDateTime.now());
        return d;
    }

    // Dominio -> Response
    public AvistadorResponseDTO fromDomainToResponse(Avistador d) {
        AvistadorResponseDTO r = new AvistadorResponseDTO();
        r.setId(d.getId());
        r.setDni(d.getDni());
        r.setNombre(d.getNombre());
        r.setApellido(d.getApellido());
        r.setEdad(d.getEdad());
        r.setDireccion(d.getDireccion());
        r.setEmail(d.getEmail());
        r.setTelefono(d.getTelefono());
        r.setCreadoEn(d.getCreadoEn());
        return r;
    }
}