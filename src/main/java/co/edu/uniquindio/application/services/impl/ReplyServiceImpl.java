package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.commentDTO.ReplyDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.ReplyMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.HostProfile;
import co.edu.uniquindio.application.model.Reply;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.HostRepository;
import co.edu.uniquindio.application.repositories.ReplyRepository;
import co.edu.uniquindio.application.services.CommentService;
import co.edu.uniquindio.application.services.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;
    private final CommentRepository commentRepository;
    private final HostRepository hostRepository;


    @Override
    public void create(String idUser, String commentId, ReplyDTO replyDTO) {

        //verificar que si exista el comentario
        Optional<Comment> auxComment = commentRepository.findById(commentId);
        if (auxComment.isEmpty()) {
            throw new ResourceNotFoundException("No se pudo encontrar el comentario");
        }

        //verificar que ya no haya una respuesta a este comentario
        Optional<Reply> auxReply = replyRepository.findByCommentId(commentId);
        if (auxReply.isPresent()) {
            throw new ValueConflictException("no puedes responder dos veces a el mismo comentario");
        }

        //verificar que el host si exista y que si sea el dueño del alojamiento

        if(auxComment.get().getAccommodation().getUser().getId().equals(idUser)) {
            Reply reply = replyMapper.toEntity(replyDTO);
            reply.setComment(auxComment.get());
            replyRepository.save(reply);
        }
        else{
            throw new ForbiddenException("no puedes responder a un alojamiento que no es tuyo");
        }
    }
}
