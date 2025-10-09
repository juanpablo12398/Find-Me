package edu.utn.proyecto.applicacion.validation.desaparecido;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import edu.utn.proyecto.common.exception.ConflictException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DesaparecidoCreatePolicy implements Validator<DesaparecidoRequestDTO> {

    private final IRepoDeDesaparecidos repo;
    private final DniNormalizer dniNorm;
    private final TextNormalizer txtNorm;
    private final List<Rule<DesaparecidoRequestDTO>> rules;

    public DesaparecidoCreatePolicy(IRepoDeDesaparecidos repo,
                                    DniNormalizer dniNorm,
                                    TextNormalizer txtNorm,
                                    List<Rule<DesaparecidoRequestDTO>> rules) {
        this.repo = repo;
        this.dniNorm = dniNorm;
        this.txtNorm = txtNorm;
        this.rules = rules;
    }

    @Override
    public void validate(DesaparecidoRequestDTO dto) {
        // 1) reglas particulares (p.ej. DesaparecidoLengthRule)
        for (var r : rules) r.check(dto);

        // 2) normalización
        var dni = dniNorm.normalize(dto.getDni());
        dto.setDni(dni);
        dto.setNombre(txtNorm.upperNoAccents(dto.getNombre()));
        dto.setApellido(txtNorm.upperNoAccents(dto.getApellido()));

        // 3) unicidad
        if (repo.existsByDni(dni)) {
            throw new ConflictException("Ya existe un desaparecido con DNI " + dni);
        }
    }
}
