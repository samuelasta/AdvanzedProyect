package co.edu.uniquindio.application.dto.accommodationDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AccommodationStatsDTO(
                                    double averageRating,
                                    long totalComments,
                                    long totalReservations,
                                    double occupancyRate,
                                    int cancellations,
                                    LocalDateTime lastReservation,
                                    LocalDateTime nextAvailableDate,
                                    double totalRevenue) {
}
