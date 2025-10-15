package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LoginPolicy implements Validator<LoginRequestDTO> {

    private final IRepoDeRenaper renaper;
    private final IRepoDeAvistadores repo;
    private final List<Rule<LoginRequestDTO>> rules;

    public LoginPolicy(IRepoDeRenaper renaper,
                       IRepoDeAvistadores repo,
                       List<Rule<LoginRequestDTO>> rules) {
        this.renaper = renaper;
        this.repo = repo;
        this.rules = rules;
    }

    @Override
    public void validate(LoginRequestDTO dto) {
        // 1) Reglas (formato, requeridos, etc.)
        for (var r : rules) r.check(dto);

        // 2) Debe existir en RENAPER
        var persona = renaper.findByDni(dto.getDni()).orElseThrow(() ->
                DomainException.of(AuthError.PADRON_NOT_FOUND.key,
                        AuthError.PADRON_NOT_FOUND.status,
                        "No existe en padrón (RENAPER).")
        );

        // 3) Debe existir avistador
        var avistador = repo.findByDni(dto.getDni()).orElseThrow(() ->
                DomainException.of(AuthError.USER_NOT_FOUND.key,
                        AuthError.USER_NOT_FOUND.status,
                        "Usuario no registrado.")
        );

        // 4) Email debe coincidir (case-insensitive; dto ya viene trim)
        String emailGuardado = avistador.getEmail() == null ? "" : avistador.getEmail().trim();
        String emailRequest  = dto.getEmail() == null ? "" : dto.getEmail().trim();
        if (!emailGuardado.equalsIgnoreCase(emailRequest)) {
            throw DomainException.of(AuthError.EMAIL_MISMATCH.key,
                    AuthError.EMAIL_MISMATCH.status,
                    "El email no coincide con el registrado.");
        }
    }
}