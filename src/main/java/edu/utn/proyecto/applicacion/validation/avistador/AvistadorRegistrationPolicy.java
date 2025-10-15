package edu.utn.proyecto.applicacion.validation.avistador;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AvistadorRegistrationPolicy implements Validator<AvistadorRequestDTO> {

    private final IRepoDeRenaper renaper;
    private final IRepoDeAvistadores repo;
    private final List<Rule<AvistadorRequestDTO>> rules;

    public AvistadorRegistrationPolicy(IRepoDeRenaper renaper,
                                       IRepoDeAvistadores repo,
                                       List<Rule<AvistadorRequestDTO>> rules) {
        this.renaper = renaper;
        this.repo = repo;
        this.rules = rules;
    }

    @Override
    public void validate(AvistadorRequestDTO dto) {
        // 1) Reglas negocio (incluye edad, formatos, etc.)
        for (var r : rules) r.check(dto);

        // 2) RENAPER: existencia
        var persona = renaper.findByDni(dto.getDni())
                .orElseThrow(() -> DomainException.of(
                        AvistadorError.PADRON_NOT_FOUND.key,
                        AvistadorError.PADRON_NOT_FOUND.status,
                        "No existe en padrón (RENAPER)."));

        // 3) RENAPER: coincidencia (comparación canónica, sin mutar el dto)
        String dtoNombre    = dto.getNombre();
        String dtoApellido  = dto.getApellido();
        String renNombre    = persona.getNombre();
        String renApellido  = persona.getApellido();

        if (!dtoNombre.equals(renNombre) || !dtoApellido.equals(renApellido)) {
            throw DomainException.of(
                    AvistadorError.PADRON_NO_MATCH.key,
                    AvistadorError.PADRON_NO_MATCH.status,
                    "Los datos no coinciden con el padrón.");
        }

        // 4) Duplicado por DNI
        if (repo.existsByDni(dto.getDni())) {
            throw DomainException.of(
                    AvistadorError.DNI_DUP.key,
                    AvistadorError.DNI_DUP.status,
                    "DNI ya registrado.");
        }
    }
}