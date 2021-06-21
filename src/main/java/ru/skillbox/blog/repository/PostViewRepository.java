package ru.skillbox.blog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.PostView;

@Repository
public interface PostViewRepository extends PagingAndSortingRepository<PostView, Integer> {
}
