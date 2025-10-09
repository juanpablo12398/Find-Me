package edu.utn.proyecto.applicacion.validation.avistador;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import edu.utn.proyecto.avistador.exception.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AvistadorRegistrationPolicy implements Validator<AvistadorRequestDTO> {

    private final IRepoDeRenaper renaper;
    private final IRepoDeAvistadores repo;
    private final DniNormalizer dniNorm;
    private final TextNormalizer txtNorm;
    private final List<Rule<AvistadorRequestDTO>> rules;

    public AvistadorRegistrationPolicy(IRepoDeRenaper renaper,
                                       IRepoDeAvistadores repo,
                                       DniNormalizer dniNorm,
                                       TextNormalizer txtNorm,
                                       List<Rule<AvistadorRequestDTO>> rules) {
        this.renaper = renaper;
        this.repo = repo;
        this.dniNorm = dniNorm;
        this.txtNorm = txtNorm;
        this.rules = rules;
    }

    @Override
    public void validate(AvistadorRequestDTO dto) {
        // 0) reglas de negocio particulares (ej. mayor de edad)
        for (var r : rules) r.check(dto);

        // 1) normalización
        String dni = dniNorm.normalize(dto.getDni());
        String nombre = txtNorm.upperNoAccents(dto.getNombre());
        String apellido = txtNorm.upperNoAccents(dto.getApellido());

        // 2) padrón RENAPER
        var persona = renaper.findByDni(dni)
                .orElseThrow(() -> new PersonaNoEncontradaException(dni));

        if (!nombre.equals(txtNorm.upperNoAccents(persona.getNombre())) ||
                !apellido.equals(txtNorm.upperNoAccents(persona.getApellido()))) {
            throw new DatosNoCoincidenException();
        }

        // 3) duplicado
        if (repo.existsByDni(dni)) throw new DniDuplicadoException(dni);

        // 4) dejar DTO normalizado
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
    }
}