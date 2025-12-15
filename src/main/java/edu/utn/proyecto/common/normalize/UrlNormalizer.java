package edu.utn.proyecto.common.normalize;

import org.springframework.stereotype.Component;

@Component
public class UrlNormalizer {

    public String normalizeOptional(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        return url.trim();
    }

    public String normalizeRequired(String url) {
        String normalized = normalizeOptional(url);
        if (normalized == null) {
            throw new IllegalArgumentException("URL no puede estar vacía");
        }
        return normalized;
    }

    public boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) return false;
        String trimmed = url.trim().toLowerCase();
        return trimmed.startsWith("http://") || trimmed.startsWith("https://");
    }
}