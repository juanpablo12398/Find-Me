package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenExistsRuleTest {

    @Test
    void tokenVacioONull_lanza() {
        var rule = new TokenExistsRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);

        when(dto.getToken()).thenReturn(null);
        DomainException ex1 = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.NOT_FOUND.key, ex1.getKey());

        when(dto.getToken()).thenReturn("");
        DomainException ex2 = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.NOT_FOUND.key, ex2.getKey());
    }

    @Test
    void tokenOk_pasa() {
        var rule = new TokenExistsRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getToken()).thenReturn("abc");

        assertDoesNotThrow(() -> rule.check(dto));
    }
}
