package co.edu.uniquindio.application.dto.accommodationDTO;

import java.time.LocalDate;

public record AccommodationStatsDTO(
                                    double averageRating,
                                    int totalComments,
                                    int totalReservations,
                                    double occupancyRate,
                                    int cancellations,
                                    LocalDate lastReservation,
                                    LocalDate nextAvailableDate,
                                    double totalRevenue) {
}
