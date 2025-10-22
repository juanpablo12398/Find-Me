package edu.utn.proyecto.common.normalize;
import org.springframework.stereotype.Component;

@Component
public class TextNormalizer {

    public String upperNoAccents(String s) {
        if (s == null || s.isBlank()) return null;

        // 1. Remover acentos
        String sinAcentos = java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");

        // 2. Primera letra mayúscula, resto minúsculas
        return sinAcentos.substring(0, 1).toUpperCase() +
                sinAcentos.substring(1).toLowerCase();
    }

    public String sentenceCase(String s) {
        if (s == null || s.isBlank()) return null;

        String trimmed = s.trim();

        // Primera letra mayúscula, resto minúsculas
        return trimmed.substring(0, 1).toUpperCase() +
                trimmed.substring(1).toLowerCase();
    }

    public String normalize(String s) {
        if (s == null) return null;
        return s.trim();
    }
}
