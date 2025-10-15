package edu.utn.proyecto.applicacion.validation.avistamiento;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;  // ← Agregar
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AvistamientoCreatePolicy implements Validator<AvistamientoRequestDTO> {

    private final IRepoDeAvistadores repoAvistadores;
    private final IRepoDeDesaparecidos repoDesaparecidos;
    private final List<Rule<AvistamientoRequestDTO>> rules;

    public AvistamientoCreatePolicy(
            IRepoDeAvistadores repoAvistadores,
            IRepoDeDesaparecidos repoDesaparecidos,
            List<Rule<AvistamientoRequestDTO>> rules) {
        this.repoAvistadores = repoAvistadores;
        this.repoDesaparecidos = repoDesaparecidos;
        this.rules = rules;
    }

    @Override
    public void validate(AvistamientoRequestDTO dto) {

        // 1) Ejecutar todas las reglas (coordenadas, descripción, etc.)
        for (var rule : rules) {
            rule.check(dto);
        }

        // 2) Verificar que el avistador existe
        if (!repoAvistadores.findById(dto.getAvistadorId()).isPresent()) {
            throw DomainException.of(
                    AvistamientoError.AVISTADOR_NOT_FOUND.key,
                    AvistamientoError.AVISTADOR_NOT_FOUND.status,
                    "El avistador no existe"
            );
        }

        // 3) Verificar que el desaparecido existe
        if (!repoDesaparecidos.findById(dto.getDesaparecidoId()).isPresent()) {
            throw DomainException.of(
                    AvistamientoError.DESAPARECIDO_NOT_FOUND.key,
                    AvistamientoError.DESAPARECIDO_NOT_FOUND.status,
                    "El desaparecido no existe"
            );
        }

    }
}
