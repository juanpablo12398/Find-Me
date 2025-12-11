package edu.utn.proyecto.common.exception;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    @DisplayName("of(key, status, message): setea key, status y message explícito")
    void of_withMessage() {
        DomainException ex = DomainException.of("MY_KEY", HttpStatus.BAD_REQUEST, "mensaje claro");

        assertEquals("MY_KEY", ex.getKey());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("mensaje claro", ex.getMessage());
    }

    @Test
    @DisplayName("of(key, status): usa key como message por fallback")
    void of_withoutMessage_usesKeyAsMessage() {
        DomainException ex = DomainException.of("OTHER_KEY", HttpStatus.NOT_FOUND);

        assertEquals("OTHER_KEY", ex.getKey());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("OTHER_KEY", ex.getMessage());
    }
}
