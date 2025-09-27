package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {

    Optional<Reply> findByCommentId(String commentId);
}
