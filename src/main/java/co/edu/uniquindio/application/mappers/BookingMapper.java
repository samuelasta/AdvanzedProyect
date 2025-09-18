package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "bookingState", constant = "CONFIRMED")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")

    Booking toEntity(CreateBookingDTO createBookingDTO);

    BookingDTO toBookingDTO(Booking booking);

}
