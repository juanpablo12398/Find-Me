package edu.utn.proyecto.common.validation.concreta.login.rules;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.springframework.stereotype.Component;

@Component
public class LoginAvistadorExistsRule implements Rule<LoginRequestDTO> {

    private final IRepoDeAvistadores repo;

    public LoginAvistadorExistsRule(IRepoDeAvistadores repo) {
        this.repo = repo;
    }

    @Override
    public void check(LoginRequestDTO dto) {

        repo.findByDni(dto.getDni())
                .orElseThrow(() -> DomainException.of(
                        AuthError.USER_NOT_FOUND.key,
                        AuthError.USER_NOT_FOUND.status,
                        "Usuario no registrado"
                ));
    }
}