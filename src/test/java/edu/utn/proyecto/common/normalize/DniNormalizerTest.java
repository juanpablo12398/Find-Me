package edu.utn.proyecto.common.normalize;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DniNormalizerTest {

    private final DniNormalizer normalizer = new DniNormalizer();

    @Test
    void devuelveNull_siEntradaNull() {
        assertThat(normalizer.normalize(null)).isNull();
    }

    @Test
    void quitaPuntosEspaciosYNoDigitos() {
        assertThat(normalizer.normalize(" 12.345.678 ")).isEqualTo("12345678");
        assertThat(normalizer.normalize("12-345-678")).isEqualTo("12345678");
        assertThat(normalizer.normalize("DNI: 12.345.678A")).isEqualTo("12345678");
    }

    @Test
    void dejaSoloDigitos_siYaEsNumerico() {
        assertThat(normalizer.normalize("0012345678")).isEqualTo("0012345678");
    }
}
