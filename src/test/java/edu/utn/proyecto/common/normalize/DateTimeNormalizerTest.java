package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeNormalizerTest {

    private final DateTimeNormalizer norm = new DateTimeNormalizer();

    @Test
    @DisplayName("formatForDisplay: usa patrón por defecto dd/MM/yyyy HH:mm")
    void formatForDisplay_defaultPattern() {
        LocalDateTime ldt = LocalDateTime.of(2025, 1, 2, 3, 4, 59);
        assertEquals("02/01/2025 03:04", norm.formatForDisplay(ldt));
    }

    @Test
    @DisplayName("formatForDisplay: null → null")
    void formatForDisplay_null() {
        assertNull(norm.formatForDisplay(null));
    }

    @Test
    @DisplayName("formatWithPattern: aplica patrón custom")
    void formatWithPattern_custom() {
        LocalDateTime ldt = LocalDateTime.of(2024, 12, 31, 23, 59);
        assertEquals("2024-12-31", norm.formatWithPattern(ldt, "yyyy-MM-dd"));
        assertEquals("23:59",       norm.formatWithPattern(ldt, "HH:mm"));
    }

    @Test
    @DisplayName("formatWithPattern: null → null")
    void formatWithPattern_null() {
        assertNull(norm.formatWithPattern(null, "yyyy"));
    }
}
