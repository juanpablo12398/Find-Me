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
    void trimea_y_uppercase() {
        assertThat(normalizer.upperNoAccents("  ana  ")).isEqualTo("ANA");
    }

    @ParameterizedTest
    @CsvSource({
            "áéíóú, AEIOU",
            "ÁÉÍÓÚ, AEIOU",
            "ñ Ñ, N N",
            "María-José, MARIA-JOSE",
            "  pérez gómez , PEREZ GOMEZ",
            "lücía, LUCIA"
    })
    void eliminaTildesYDiacriticos_yPoneMayusculas(String in, String expected) {
        assertThat(normalizer.upperNoAccents(in)).isEqualTo(expected);
    }
}
