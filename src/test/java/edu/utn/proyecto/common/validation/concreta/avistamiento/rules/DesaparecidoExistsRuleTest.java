package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoExistsRuleTest {

    @Mock IRepoDeDesaparecidos repo;

    @Test
    void idNull_lanzaYNoConsultaRepo() {
        DesaparecidoExistsRule rule = new DesaparecidoExistsRule(repo);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDesaparecidoId(null);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.DESAPARECIDO_NOT_FOUND.key, ex.getKey());
        verifyNoInteractions(repo);
    }

    @Test
    void idInvalido_lanzaIllegalArgument() {
        DesaparecidoExistsRule rule = new DesaparecidoExistsRule(repo);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDesaparecidoId("not-a-uuid");

        assertThrows(IllegalArgumentException.class, () -> rule.check(dto));
        verifyNoInteractions(repo);
    }

    @Test
    void noExisteEnRepo_lanza() {
        DesaparecidoExistsRule rule = new DesaparecidoExistsRule(repo);
        String id = UUID.randomUUID().toString();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDesaparecidoId(id);

        when(repo.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.DESAPARECIDO_NOT_FOUND.key, ex.getKey());
        verify(repo).findById(UUID.fromString(id));
    }

    @Test
    void existeEnRepo_pasa() {
        DesaparecidoExistsRule rule = new DesaparecidoExistsRule(repo);
        String id = UUID.randomUUID().toString();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDesaparecidoId(id);

        when(repo.findById(UUID.fromString(id)))
                .thenReturn(Optional.of(mock(Desaparecido.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(repo).findById(UUID.fromString(id));
    }
}
