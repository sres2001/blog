package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.Post;
import ru.skillbox.blog.model.PostVote;
import ru.skillbox.blog.model.User;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    Optional<PostVote> findByUserAndPost(User user, Post post);
}
