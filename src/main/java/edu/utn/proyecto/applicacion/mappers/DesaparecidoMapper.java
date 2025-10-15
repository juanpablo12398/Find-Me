package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.common.normalize.DateTimeNormalizer;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DesaparecidoMapper {

    private final TextNormalizer txtNorm;
    private final DniNormalizer dniNorm;
    private final UrlNormalizer urlNorm;
    private final DateTimeNormalizer dateNorm;

    public DesaparecidoMapper(TextNormalizer txtNorm,
                              DniNormalizer dniNorm,
                              UrlNormalizer urlNorm,
                              DateTimeNormalizer dateNorm) {
        this.txtNorm = txtNorm;
        this.dniNorm = dniNorm;
        this.urlNorm = urlNorm;
        this.dateNorm = dateNorm;
    }

    public void normalizeRequestInPlace(DesaparecidoRequestDTO dto) {
        dto.setNombre      (txtNorm.upperNoAccents(dto.getNombre()));
        dto.setApellido    (txtNorm.upperNoAccents(dto.getApellido()));
        dto.setDni         (dniNorm.normalize(dto.getDni()));
        dto.setDescripcion (txtNorm.normalize(dto.getDescripcion()));
        dto.setFotoUrl     (urlNorm.normalizeOptional(dto.getFotoUrl()));
    }

    public Desaparecido fromRequestToDomain(DesaparecidoRequestDTO dto) {
        return new Desaparecido(
                dto.getNombre(),
                dto.getApellido(),
                dto.getDni(),
                dto.getDescripcion(),
                dto.getFotoUrl()
        );
    }

    public List<Desaparecido> fromRequestToDomain(List<DesaparecidoRequestDTO> dtos) {
        return dtos.stream().map(this::fromRequestToDomain).collect(Collectors.toList());
    }

    public DesaparecidoResponseDTO fromDomainToResponse(Desaparecido d) {

        return new DesaparecidoResponseDTO(
                d.getId(),
                d.getNombre(),
                d.getApellido(),
                d.getDni(),
                d.getFechaDesaparicion(),
                d.getDescripcion(),
                d.getFoto()
        );
    }

    public List<DesaparecidoResponseDTO> fromDomainToResponse(List<Desaparecido> lista) {
        return lista.stream().map(this::fromDomainToResponse).collect(Collectors.toList());
    }

    // -------- Presentación (Front) --------

    public DesaparecidoFrontDTO fromResponseToFront(DesaparecidoResponseDTO dto) {
        final String fechaFormateada = dateNorm.formatForDisplay(dto.getFechaDesaparicion()); // null-safe
        return new DesaparecidoFrontDTO(
                dto.getNombre(),
                dto.getApellido(),
                dto.getDni(),
                dto.getDescripcion(),
                dto.getFoto(),
                fechaFormateada
        );
    }

    public List<DesaparecidoFrontDTO> fromResponseListToFrontList(List<DesaparecidoResponseDTO> lista) {
        return lista.stream().map(this::fromResponseToFront).collect(Collectors.toList());
    }
}

