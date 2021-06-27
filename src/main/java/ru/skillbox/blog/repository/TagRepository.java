package ru.skillbox.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>, TagRepositoryCustom {

    Optional<Tag> findOneByNameIgnoreCase(String name);
}
