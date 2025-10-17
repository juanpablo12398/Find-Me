package edu.utn.proyecto.common.validation.concreta.login.rules;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.springframework.stereotype.Component;

@Component
public class LoginEmailMatchRule implements Rule<LoginRequestDTO> {

    private final IRepoDeAvistadores repo;

    public LoginEmailMatchRule(IRepoDeAvistadores repo) {
        this.repo = repo;
    }

    @Override
    public void check(LoginRequestDTO dto) {
        Avistador avistador = repo.findByDni(dto.getDni()).orElse(null);

        if (!dto.getEmail().equals(avistador.getEmail())) {
            throw DomainException.of(
                    AuthError.EMAIL_MISMATCH.key,
                    AuthError.EMAIL_MISMATCH.status,
                    "El email no coincide con el registrado"
            );
        }
    }
}
