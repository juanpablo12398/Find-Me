package edu.utn.proyecto.applicacion.validation.desaparecido;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
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
        for (var r : rules) r.check(dto);

        var dni = dniNorm.normalize(dto.getDni());
        dto.setDni(dni);
        dto.setNombre(txtNorm.upperNoAccents(dto.getNombre()));
        dto.setApellido(txtNorm.upperNoAccents(dto.getApellido()));

        if (repo.existsByDni(dni)) {
            throw DomainException.of(
                    DesaparecidoError.DNI_DUP.key,
                    DesaparecidoError.DNI_DUP.status,
                    dni
            );
        }
    }
}
