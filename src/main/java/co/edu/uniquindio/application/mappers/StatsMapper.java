package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationStatsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatsMapper {

    AccommodationStatsDTO toAccommodationStatsDTO(double averageRating,
                                                  long totalComments,
                                                  long totalReservations,
                                                  double occupancyRate,
                                                  int cancellations,
                                                  double totalRevenue);
}
