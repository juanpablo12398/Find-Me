package edu.utn.proyecto.common.normalize;
import org.springframework.stereotype.Component;

@Component
public class TextNormalizer {
    public String upperNoAccents(String s) {
        if (s == null) return null;
        return java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toUpperCase();
    }
}
