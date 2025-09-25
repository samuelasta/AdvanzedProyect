package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByAccommodationId(String accommodationId);
}
