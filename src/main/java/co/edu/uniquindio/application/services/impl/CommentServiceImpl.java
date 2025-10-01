package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.mappers.CommentMapper;
import co.edu.uniquindio.application.mappers.ListCommentsMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AccommodationRepository accommodationRepository;
    private final ListCommentsMapper listCommentsMapper;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    //devuelve todos los comentarios de los alojamientos
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
    public void createComment(String id, CreateCommentDTO createCommentDTO) throws Exception {
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if(accommodation.isEmpty()){
            throw new ResourceNotFoundException("no se encontró el alojamiento");
        }
        Optional<User> user = userRepository.findById(createCommentDTO.userId());
        if(user.isEmpty()){
            throw new ResourceNotFoundException("no se encontró al usuario");
        }
        Comment comment = commentMapper.toEntity(createCommentDTO);
        comment.setAccommodation(accommodation.get());
        comment.setUser(user.get());
        commentRepository.save(comment);
    }

}
