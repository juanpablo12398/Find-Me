package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextNormalizerTest {

    private final TextNormalizer norm = new TextNormalizer();

    @Test
    @DisplayName("upperNoAccents: quita acentos y deja SoloPrimeraMayúscula resto minúsculas")
    void upperNoAccents_basic() {
        assertEquals("Jose", norm.upperNoAccents("jóse"));
        assertEquals("Perez", norm.upperNoAccents("péRez"));
        assertEquals("Nandu", norm.upperNoAccents("ñandÚ"));
        assertEquals("Creme brulee", norm.upperNoAccents("  crème BRÛLÉE  "));
    }

    @Test
    @DisplayName("upperNoAccents: null o blank → null")
    void upperNoAccents_nullOrBlank() {
        assertNull(norm.upperNoAccents(null));
        assertNull(norm.upperNoAccents("   "));
    }

    @Test
    @DisplayName("sentenceCase: Primera en mayúscula, resto minúsculas")
    void sentenceCase_basic() {
        assertEquals("Hola", norm.sentenceCase("hOLA"));
        assertEquals("Hola mundo", norm.sentenceCase("  HOLA MUNDO "));
    }

    @Test
    @DisplayName("sentenceCase: null o blank → null")
    void sentenceCase_nullOrBlank() {
        assertNull(norm.sentenceCase(null));
        assertNull(norm.sentenceCase("  "));
    }

    @Test
    @DisplayName("normalize: trim simple, preserva case")
    void normalize_trim() {
        assertEquals("a@MAIL.com", norm.normalize("  a@MAIL.com  "));
        assertEquals("", norm.normalize(""));
    }

    @Test
    @DisplayName("normalize: null → null")
    void normalize_null() {
        assertNull(norm.normalize(null));
    }
}
