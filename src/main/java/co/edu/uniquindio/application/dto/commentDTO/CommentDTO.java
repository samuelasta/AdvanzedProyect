package co.edu.uniquindio.application.dto.commentDTO;

import co.edu.uniquindio.application.dto.usersDTOs.UserCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.User;

import java.time.LocalDateTime;

public record CommentDTO(String comment,
                         LocalDateTime commentDate,
                         int rating,
                         UserCommentDTO user
) {
}
