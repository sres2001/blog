package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
