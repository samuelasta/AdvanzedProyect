package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.commentDTO.ReplyDTO;

public interface ReplyService  {

    //idUser es temporal porque se sacará del token
    void create(String idUser, String commentId, ReplyDTO replyDTO);
}
