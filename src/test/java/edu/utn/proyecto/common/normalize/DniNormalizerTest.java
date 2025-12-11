package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DniNormalizerTest {

    private final DniNormalizer norm = new DniNormalizer();

    @Test
    @DisplayName("normalize: elimina todo lo no numérico")
    void normalize_removesNonDigits() {
        assertEquals("12345678", norm.normalize("  12.345.678  "));
        assertEquals("20300123", norm.normalize("20.300.123 "));
        assertEquals("0", norm.normalize("a-0-b"));
    }

    @Test
    @DisplayName("normalize: null → null")
    void normalize_null() {
        assertNull(norm.normalize(null));
    }

    @Test
    @DisplayName("normalize: vacío → vacío")
    void normalize_empty() {
        assertEquals("", norm.normalize(""));
    }
}

