package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.common.normalize.DateTimeNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvistamientoMapper {

    private final TextNormalizer txtNorm;
    private final UrlNormalizer urlNorm;
    private final DateTimeNormalizer dateNorm;

    public AvistamientoMapper(TextNormalizer txtNorm,
                              UrlNormalizer urlNorm,
                              DateTimeNormalizer dateNorm) {
        this.txtNorm = txtNorm;
        this.urlNorm = urlNorm;
        this.dateNorm = dateNorm;
    }

    public void normalizeRequestInPlace(AvistamientoRequestDTO dto) {
        dto.setDescripcion(txtNorm.sentenceCase(dto.getDescripcion()));
        dto.setFotoUrl(urlNorm.normalizeOptional(dto.getFotoUrl()));
    }

    public AvistamientoFrontDTO fromDomainToFrontBasic(Avistamiento a) {
        String fechaHoraFmt = dateNorm.formatForDisplay(a.getFechaHora());
        return new AvistamientoFrontDTO(
                a.getId(),
                a.getLatitud(),
                a.getLongitud(),
                fechaHoraFmt,
                a.getDescripcion(),
                a.getFotoUrl(),
                a.getVerificado()
        );
    }

    public Avistamiento fromRequestToDomain(AvistamientoRequestDTO dto) {
        Avistamiento a = new Avistamiento();
        a.setAvistadorId(dto.getAvistadorId());
        a.setDesaparecidoId(dto.getDesaparecidoId());
        a.setLatitud(dto.getLatitud());
        a.setLongitud(dto.getLongitud());
        a.setDescripcion(dto.getDescripcion());
        a.setFotoUrl(dto.getFotoUrl());
        a.setPublico(dto.getPublico() != null ? dto.getPublico() : true);
        return a;
    }

    public AvistamientoResponseDTO fromDomainToResponse(Avistamiento a) {
        AvistamientoResponseDTO dto = new AvistamientoResponseDTO();
        dto.setId(a.getId());
        dto.setAvistadorId(a.getAvistadorId());
        dto.setDesaparecidoId(a.getDesaparecidoId());
        dto.setLatitud(a.getLatitud());
        dto.setLongitud(a.getLongitud());
        dto.setFechaHora(a.getFechaHora());
        dto.setDescripcion(a.getDescripcion());
        dto.setFotoUrl(a.getFotoUrl());
        dto.setVerificado(a.getVerificado());
        dto.setPublico(a.getPublico());
        dto.setCreadoEn(a.getCreadoEn());
        return dto;
    }

    public List<AvistamientoResponseDTO> fromDomainListToResponseList(List<Avistamiento> lista) {
        return lista.stream().map(this::fromDomainToResponse).collect(Collectors.toList());
    }

    // -------- FRONT --------
    public AvistamientoFrontDTO fromResponseToFront(AvistamientoResponseDTO res) {
        String fechaHoraFmt = dateNorm.formatForDisplay(res.getFechaHora());
        return new AvistamientoFrontDTO(
                res.getId(),
                res.getLatitud(),
                res.getLongitud(),
                fechaHoraFmt,
                res.getDescripcion(),
                res.getFotoUrl(),
                res.getVerificado()
        );
    }

    public List<AvistamientoFrontDTO> fromResponseToFrontList(List<AvistamientoResponseDTO> lista) {
        return lista.stream().map(this::fromResponseToFront).collect(Collectors.toList());
    }
}
