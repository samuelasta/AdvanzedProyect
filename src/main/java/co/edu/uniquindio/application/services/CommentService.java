package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> listComments(String id, int page) throws Exception;
    void createComment(String accommodationId, String userId, CreateCommentDTO createCommentDTO) throws Exception;

}
