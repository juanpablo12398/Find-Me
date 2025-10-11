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
        // 1) Reglas negocio (incluye edad)
        for (var r : rules) r.check(dto);

        // 2) Normalizar
        final String dni = dniNorm.normalize(dto.getDni());
        final String nombre = txtNorm.upperNoAccents(dto.getNombre());
        final String apellido = txtNorm.upperNoAccents(dto.getApellido());

        // 3) RENAPER: existencia
        var persona = renaper.findByDni(dni)
                .orElseThrow(() -> DomainException.of(
                        AvistadorError.PADRON_NOT_FOUND.key,
                        AvistadorError.PADRON_NOT_FOUND.status,
                        "No existe en padrón (RENAPER)."));

        // 4) RENAPER: coincidencia
        String nombreRenaper = txtNorm.upperNoAccents(persona.getNombre());
        String apellidoRenaper = txtNorm.upperNoAccents(persona.getApellido());
        if (!nombre.equals(nombreRenaper) || !apellido.equals(apellidoRenaper)) {
            throw DomainException.of(
                    AvistadorError.PADRON_NO_MATCH.key,
                    AvistadorError.PADRON_NO_MATCH.status,
                    "Los datos no coinciden con el padrón.");
        }

        // 5) Duplicado
        if (repo.existsByDni(dni)) {
            throw DomainException.of(
                    AvistadorError.DNI_DUP.key,
                    AvistadorError.DNI_DUP.status,
                    "DNI ya registrado.");
        }

        // 6) Dejar DTO normalizado
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
    }
}