package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.springframework.stereotype.Component;

@Component
public class LoginAvistadorEmailRule implements Rule<LoginRequestDTO> {

    private final IRepoDeAvistadores repo;

    public LoginAvistadorEmailRule(IRepoDeAvistadores repo) {
        this.repo = repo;
    }

    @Override
    public void check(LoginRequestDTO dto) {
        var opt = repo.findByDni(dto.getDni());
        var av = opt.orElseThrow(() -> DomainException.of(
                AuthError.AVISTADOR_NOT_FOUND.key,
                AuthError.AVISTADOR_NOT_FOUND.status));

        String in = (dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase());
        String saved = (av.getEmail() == null ? "" : av.getEmail().trim().toLowerCase());

        if (!in.equals(saved)) {
            throw DomainException.of(
                    AuthError.EMAIL_MISMATCH.key,
                    AuthError.EMAIL_MISMATCH.status);
        }
    }
}
