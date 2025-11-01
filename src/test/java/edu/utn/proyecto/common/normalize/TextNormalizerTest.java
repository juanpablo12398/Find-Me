package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

class TextNormalizerTest {

    private final TextNormalizer normalizer = new TextNormalizer();

    @Test
    void devuelveNull_siEntradaNull() {
        assertThat(normalizer.upperNoAccents(null)).isNull();
    }

    @Test
    void devuelveNull_siEntradaBlank() {
        assertThat(normalizer.upperNoAccents("   ")).isNull();
    }

    @Test
    void trimea_y_titleCaseGlobal() {
        assertThat(normalizer.upperNoAccents("  ana  ")).isEqualTo("Ana");
    }

    @ParameterizedTest
    @CsvSource({
            "áéíóú, Aeiou",
            "ÁÉÍÓÚ, Aeiou",
            "'ñ Ñ', 'N n'",
            "María-José, Maria-jose",
            "'  pérez gómez ', 'Perez gomez'",
            "lücía, Lucia"
    })
    void eliminaTildesYDiacriticos_yAplicaTitleCaseGlobal(String in, String expected) {
        assertThat(normalizer.upperNoAccents(in)).isEqualTo(expected);
    }

    @Test
    void sentenceCase_respetaTituloSimpleSinQuitarAcentos() {
        assertThat(normalizer.sentenceCase("  hOlA MUnDo  ")).isEqualTo("Hola mundo");
    }

    @Test
    void normalize_trim_simple() {
        assertThat(normalizer.normalize("  abc  ")).isEqualTo("abc");
        assertThat(normalizer.normalize(null)).isNull();
    }
}
