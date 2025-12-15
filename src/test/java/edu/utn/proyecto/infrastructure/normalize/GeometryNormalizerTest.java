package edu.utn.proyecto.infrastructure.normalize;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import static org.junit.jupiter.api.Assertions.*;

class GeometryNormalizerTest {

    private final GeometryNormalizer norm = new GeometryNormalizer();

    @Test
    void toPoint_sets_lonlat_and_srid() {
        Double lat = -34.6, lng = -58.38;

        Point p = norm.toPoint(lat, lng);

        assertNotNull(p);
        assertEquals(-58.38, p.getX());
        assertEquals(-34.6,  p.getY());
        assertEquals(4326,   p.getSRID());
    }

    @Test
    void toPoint_nulls_throw() {
        assertThrows(IllegalArgumentException.class, () -> norm.toPoint(null, -58.38));
        assertThrows(IllegalArgumentException.class, () -> norm.toPoint(-34.6, null));
    }

    @Test
    void fromPoint_returns_y_and_x_and_null() {
        Point p = norm.toPoint(-34.61, -58.39);

        assertEquals(-34.61, norm.fromPointToLatitud(p));
        assertEquals(-58.39, norm.fromPointToLongitud(p));

        assertNull(norm.fromPointToLatitud(null));
        assertNull(norm.fromPointToLongitud(null));
    }
}