package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.enums.Amenities;

import java.util.List;

public interface AccommodationService {

    void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception;
    void edit(String id, UpdateDTO updateDto) throws Exception;
    void delete(String id) throws Exception;
    List<BookingDTO> listAll(ListBookingsDTO listBookingsDTO) throws Exception;
    List<Accommodation> search(ListAccommodationDTO listAccommodationDTO) throws Exception;
    List<Amenities> listAllAmenities(String id) throws Exception;
    AccommodationStatsDTO stats(String id) throws Exception;

}
