package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.commentDTO.ReplyDTO;
import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.services.CommentService;
import co.edu.uniquindio.application.services.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ReplyService replyService;

    //  Responder a un comentario (host). (hecho)
    @PostMapping("/{commentId}/reply/{idUser}")//idUser temporal porque se sacará del token
    public ResponseEntity<ResponseDTO<String>> reply(@PathVariable String idUser, @PathVariable String commentId, @Valid @RequestBody ReplyDTO replyDTO) throws Exception{
        replyService.create(idUser, commentId, replyDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "respuesta a comentario exitosa"));
    }
}
