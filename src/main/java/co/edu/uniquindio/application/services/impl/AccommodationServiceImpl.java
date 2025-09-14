package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.services.AccommodationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationServiceImpl implements AccommodationService {

    @Override
    public void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception {

    }

    @Override
    public void edit(String id, UpdateDTO UpdaterDTO) throws Exception {

    }

    @Override
    public void delete(String id) throws Exception {

    }

    @Override
    public List<BookingDTO> listAll(ListBookingsDTO listBookingsDTO) throws Exception {
        return List.of();
    }


    @Override
    public List<Accommodation> search(ListAccommodationDTO listAccommodationDTO) throws Exception {
        return List.of();
    }

    @Override
    public List<Amenities> listAllAmenities(String id) throws Exception {
        return List.of();
    }

    @Override
    public List<CommentDTO> listComments(String id) throws Exception {
        return List.of();
    }

    @Override
    public void createComment(String id, CreateCommentDTO createCommentDTO) throws Exception {

    }

    @Override
    public AccommodationStatsDTO stats(String id) throws Exception {
        return null;
    }
}
