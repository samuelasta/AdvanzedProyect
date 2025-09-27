package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.dto.commentDTO.ReplyDTO;
import co.edu.uniquindio.application.model.Reply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReplyMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())" )
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())" )

    Reply toEntity(ReplyDTO replyDTO);
}
