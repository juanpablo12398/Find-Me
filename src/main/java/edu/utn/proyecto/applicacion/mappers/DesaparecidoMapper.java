package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DesaparecidoMapper {

    // De DTO de entrada → Entidad de Dominio
    public Desaparecido fromRequestToDomain(DesaparecidoRequestDTO dto) {
        return new Desaparecido(
                dto.getNombre(),
                dto.getApellido(),
                dto.getDni(),
                dto.getDescripcion(),
                dto.getFotoUrl()
        );
    }

    // De lista de DTOs de entrada → lista de Entidades de Dominio
    public List<Desaparecido> fromRequestToDomain(List<DesaparecidoRequestDTO> dtos) {
        return dtos.stream()
                .map(this::fromRequestToDomain)
                .collect(Collectors.toList());
    }

    // De Entidad de Dominio → DTO de Respuesta
    public DesaparecidoResponseDTO fromDomainToResponse(Desaparecido desaparecido) {
        DesaparecidoResponseDTO response = new DesaparecidoResponseDTO(
                desaparecido.getNombre(),
                desaparecido.getApellido(),
                desaparecido.getDni(),
                desaparecido.getDescripcion(),
                desaparecido.getFoto()
        );
        response.setId(desaparecido.getId());
        response.setFechaDesaparicion(desaparecido.getFechaDesaparicion());
        return response;
    }

    // De lista de Dominio → lista de DTO de Respuesta
    public List<DesaparecidoResponseDTO> fromDomainToResponse(List<Desaparecido> lista) {
        return lista.stream()
                .map(this::fromDomainToResponse)
                .collect(Collectors.toList());
    }

    // ResponseDTO → FrontDTO (para devolver al frontend)
    public DesaparecidoFrontDTO fromResponseToFront(DesaparecidoResponseDTO dto) {
        // Formateamos la fecha para el front:
        String fechaFormateada = dto.getFechaDesaparicion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return new DesaparecidoFrontDTO(
                dto.getNombre(),
                dto.getApellido(),
                dto.getDni(),
                dto.getDescripcion(),
                dto.getFoto(),
                fechaFormateada
        );
    }

    // De lista de ResponseDTO → lista de FrontDTO
    public List<DesaparecidoFrontDTO> fromResponseListToFrontList(List<DesaparecidoResponseDTO> lista) {
        return lista.stream()
                .map(this::fromResponseToFront)
                .collect(Collectors.toList());
    }
}

