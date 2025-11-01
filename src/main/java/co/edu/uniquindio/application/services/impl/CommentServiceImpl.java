package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.mappers.CommentMapper;
import co.edu.uniquindio.application.mappers.ListCommentsMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.CommentService;
import co.edu.uniquindio.application.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AccommodationRepository accommodationRepository;
    private final ListCommentsMapper listCommentsMapper;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    //devuelve todos los comentarios de los alojamientos //
    @Override
    public List<CommentDTO> listComments(String id, int page) throws Exception {

        Pageable pageable = PageRequest.of(page, 10);

        Page<Comment> list = commentRepository.findAllByAccommodationId(id, pageable);
        if(list.isEmpty()){
            throw new ResourceNotFoundException("no se encontraron comentarios");
        }
        return list.stream()
                .map(listCommentsMapper::ToCommentDTO)
                .toList();

    }

    // metodo para crear el comentario (posible cambio). Validar que el comentario solo se haga si la reserva pasó y que corresponda al alojamiento deonde se quedó el usuario
    @Override
    @Transactional
    public void createComment(String bookingId, String userId, CreateCommentDTO createCommentDTO) throws Exception {

        // validar existencia de la reserva
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva"));

        // validar que la reserva ya haya terminado
        if (booking.getBookingState() != BookingState.COMPLETED) {
            throw new ForbiddenException("No puedes comentar si tu reserva aún no ha finalizado");
        }

        // validar existencia del usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario"));

        //  que la reserva pertenezca al usuario
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("No puedes comentar una reserva que no te pertenece");
        }

        // validamos que no haya comentado antes en esta reserva
        if (commentRepository.existsByBookingId(bookingId)) {
            throw new ForbiddenException("Ya realizaste un comentario para esta reserva");
        }

        // crear comentario y asignar relaciones
        Comment comment = commentMapper.toEntity(createCommentDTO);
        comment.setBooking(booking);
        comment.setAccommodation(booking.getAccommodation());
        comment.setUser(user);

        commentRepository.save(comment);

        Double averageRating = commentRepository.findAverageRatingByAccommodationId(booking.getAccommodation().getId(), null, null);
        booking.getAccommodation().setAverageRatings(averageRating != null ? averageRating : 0.0);

        accommodationRepository.save(booking.getAccommodation());

        // notificamos al host y a quien comenta
        emailService.sendMail(new SendEmailDTO("Nueva reseña en tu alojamiento", booking.getAccommodation().getUser().getName()+
                " acabas de recibir una nueva reseña en tu alojamiento: "+ booking.getAccommodation().getTitle()+ " ubicado en: "+
                booking.getAccommodation().getLocation().getCity()+ " de parte de: "+ comment.getUser().getName(),
                booking.getAccommodation().getUser().getEmail()));

        emailService.sendMail(new SendEmailDTO(" Reseña creada exitosamente",
                "Acabas de realizar una reseña en el alojamiento: "+ comment.getAccommodation().getTitle()+ " de: "+
                 comment.getAccommodation().getUser().getName(), comment.getUser().getEmail()));
        }


}
