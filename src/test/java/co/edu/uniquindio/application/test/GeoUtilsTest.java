package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.services.impl.GeoUtilsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GeoUtilsTest {

    private GeoUtilsImpl geoUtils;

    @BeforeEach
    void setUp() {
        geoUtils = new GeoUtilsImpl();
    }

    @Test
    void calcularDistancia_WhenSameCoordinates_ShouldReturnZero() {
        // Arrange
        double lat = 4.5339;
        double lon = -75.6811;

        // Act
        double distance = geoUtils.calcularDistancia(lat, lon, lat, lon);

        // Assert
        assertEquals(0, distance, 0.0001);
    }

    @Test
    void calcularDistancia_WhenDifferentPoints_ShouldReturnPositiveDistance() {
        // Arrange: Armenia → Bogotá
        double lat1 = 4.5339, lon1 = -75.6811; // Armenia
        double lat2 = 4.7110, lon2 = -74.0721; // Bogotá

        // Act
        double distance = geoUtils.calcularDistancia(lat1, lon1, lat2, lon2);

        // Assert
        assertTrue(distance > 0);
        // valor aproximado: ~177 km → 177000 m
        assertTrue(distance > 150000 && distance < 200000);
    }

    @Test
    void calcularDistancia_WhenReversedPoints_ShouldBeSymmetric() {
        // Arrange
        double lat1 = 4.5339, lon1 = -75.6811;
        double lat2 = 4.7110, lon2 = -74.0721;

        // Act
        double d1 = geoUtils.calcularDistancia(lat1, lon1, lat2, lon2);
        double d2 = geoUtils.calcularDistancia(lat2, lon2, lat1, lon1);

        // Assert
        assertEquals(d1, d2, 0.001);
    }
}
