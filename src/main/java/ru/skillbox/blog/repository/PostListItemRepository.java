package ru.skillbox.blog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.PostListItem;

@Repository
public interface PostListItemRepository extends PagingAndSortingRepository<PostListItem, Integer> {
}
