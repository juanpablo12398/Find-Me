package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlNormalizerTest {

    private final UrlNormalizer norm = new UrlNormalizer();

    @Test
    @DisplayName("normalizeOptional: trim; null/blank → null")
    void normalizeOptional_behavior() {
        assertEquals("https://site.com", norm.normalizeOptional("  https://site.com  "));
        assertNull(norm.normalizeOptional(null));
        assertNull(norm.normalizeOptional("   "));
    }

    @Test
    @DisplayName("normalizeRequired: retorna trim o lanza si vacío")
    void normalizeRequired_behavior() {
        assertEquals("http://x.y", norm.normalizeRequired("  http://x.y "));
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> norm.normalizeRequired(null));
        assertEquals("URL no puede estar vacía", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> norm.normalizeRequired("   "));
        assertEquals("URL no puede estar vacía", ex2.getMessage());
    }

    @Test
    @DisplayName("isValidUrl: acepta http/https (case-insensitive) y rechaza otros")
    void isValidUrl_validation() {
        assertTrue (norm.isValidUrl("http://example.com"));
        assertTrue (norm.isValidUrl("  HTTPS://EXAMPLE.com/path  "));
        assertFalse(norm.isValidUrl("ftp://server"));
        assertFalse(norm.isValidUrl("example.com"));
        assertFalse(norm.isValidUrl("   "));
        assertFalse(norm.isValidUrl(null));
    }
}
