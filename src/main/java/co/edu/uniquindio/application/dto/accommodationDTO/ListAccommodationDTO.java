package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ListAccommodationDTO(
                                    @Length(max = 30) String city,
                                    @FutureOrPresent LocalDateTime checkIn,
                                   @Future LocalDateTime checkOut,
                                   @Positive Integer guest_number,
                                    @PositiveOrZero Double minimum,
                                    @PositiveOrZero Double maximum,
                                    List<Amenities> list

) {
}
