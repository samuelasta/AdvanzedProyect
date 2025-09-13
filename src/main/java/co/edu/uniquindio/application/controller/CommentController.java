package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.CreateCommentDTO;
import co.edu.uniquindio.application.dto.ReplyDTO;
import co.edu.uniquindio.application.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    //  Responder a un comentario (host)
    @PostMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<String>> reply(@PathVariable String commentId, @Valid @RequestBody ReplyDTO replyDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "respuesta a comentario exitosa"));
    }
}
