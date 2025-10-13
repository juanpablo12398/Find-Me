package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
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
    private final DniNormalizer dniNorm;
    private final TextNormalizer txtNorm;
    private final List<Rule<LoginRequestDTO>> rules;

    public LoginPolicy(IRepoDeRenaper renaper,
                       IRepoDeAvistadores repo,
                       DniNormalizer dniNorm,
                       TextNormalizer txtNorm,
                       List<Rule<LoginRequestDTO>> rules) {
        this.renaper = renaper;
        this.repo = repo;
        this.dniNorm = dniNorm;
        this.txtNorm = txtNorm;
        this.rules = rules;
    }

    @Override
    public void validate(LoginRequestDTO dto) {
        // 1) Reglas de negocio (formato DNI/Email, no nulos, etc.)
        for (var r : rules) r.check(dto);

        // 2) Normalizaciones
        final var dni = dniNorm.normalize(dto.getDni());
        final var email = (dto.getEmail() == null) ? "" : dto.getEmail().trim();

        // 3) Debe existir en RENAPER
        var persona = renaper.findByDni(dni).orElseThrow(() ->
                DomainException.of(AuthError.PADRON_NOT_FOUND.key,
                        AuthError.PADRON_NOT_FOUND.status,
                        "No existe en padrón (RENAPER).")
        );

        // 4) Debe existir avistador
        var avistador = repo.findByDni(dni).orElseThrow(() ->
                DomainException.of(AuthError.USER_NOT_FOUND.key,
                        AuthError.USER_NOT_FOUND.status,
                        "Usuario no registrado.")
        );

        // 5) Email debe coincidir (case-insensitive)
        String emailGuardado = (avistador.getEmail() == null) ? "" : avistador.getEmail().trim();
        if (!emailGuardado.equalsIgnoreCase(email)) {
            throw DomainException.of(AuthError.EMAIL_MISMATCH.key,
                    AuthError.EMAIL_MISMATCH.status,
                    "El email no coincide con el registrado.");
        }

        // 6) Dejar DTO normalizado y con nombre resuelto (opcional)
        dto.setDni(dni);
        String nombreResuelto = (avistador.getNombre() != null && !avistador.getNombre().isBlank())
                ? avistador.getNombre()
                : persona.getNombre();
        dto.setResolvedNombre(nombreResuelto);
        dto.setEmail(emailGuardado);

    }
}