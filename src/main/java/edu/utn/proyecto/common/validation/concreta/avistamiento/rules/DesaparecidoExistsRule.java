package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.springframework.stereotype.Component;

@Component
public class DesaparecidoExistsRule implements Rule<AvistamientoRequestDTO> {

    private final IRepoDeDesaparecidos repo;

    public DesaparecidoExistsRule(IRepoDeDesaparecidos repo) {
        this.repo = repo;
    }

    @Override
    public void check(AvistamientoRequestDTO dto) {
        if (dto.getDesaparecidoId() == null ||
                !repo.findById(dto.getDesaparecidoId()).isPresent()) {
            throw DomainException.of(
                    AvistamientoError.DESAPARECIDO_NOT_FOUND.key,
                    AvistamientoError.DESAPARECIDO_NOT_FOUND.status,
                    "El desaparecido no existe"
            );
        }
    }
}
