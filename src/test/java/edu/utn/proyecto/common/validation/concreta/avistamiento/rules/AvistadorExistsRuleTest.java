package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorExistsRuleTest {

    @Mock IRepoDeAvistadores repo;

    @Test
    void idNull_lanzaYNoConsultaRepo() {
        AvistadorExistsRule rule = new AvistadorExistsRule(repo);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(null);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.AVISTADOR_NOT_FOUND.key, ex.getKey());
        verifyNoInteractions(repo);
    }

    @Test
    void idInvalido_lanzaIllegalArgument() {
        AvistadorExistsRule rule = new AvistadorExistsRule(repo);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId("no-uuid");

        assertThrows(IllegalArgumentException.class, () -> rule.check(dto));
        verifyNoInteractions(repo);
    }

    @Test
    void noExisteEnRepo_lanza() {
        AvistadorExistsRule rule = new AvistadorExistsRule(repo);
        String id = UUID.randomUUID().toString();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(id);

        when(repo.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.AVISTADOR_NOT_FOUND.key, ex.getKey());
        verify(repo).findById(UUID.fromString(id));
    }

    @Test
    void existeEnRepo_pasa() {
        AvistadorExistsRule rule = new AvistadorExistsRule(repo);

        UUID uid = UUID.randomUUID();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(uid.toString());

        when(repo.findById(uid)).thenReturn(Optional.of(mock(AvistadorEntity.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(repo).findById(uid);
    }
}

