package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.PostTag;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {
}
