package ru.skillbox.blog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>, TagRepositoryCustom {

    Optional<Tag> findOneByNameIgnoreCase(String name);

    @Query("select t from Tag t join PostTag pt on t = pt.tag where pt.post.id = :postId")
    List<Tag> findPostTags(Integer postId);
}
