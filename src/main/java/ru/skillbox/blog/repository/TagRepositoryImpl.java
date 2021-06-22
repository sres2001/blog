package ru.skillbox.blog.repository;

import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.Post;
import ru.skillbox.blog.model.PostTag;
import ru.skillbox.blog.model.Tag;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public class TagRepositoryImpl implements TagRepositoryCustom {

    private final EntityManager entityManager;

    public TagRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TagAndCount> getTagsAndWeights(String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagAndCount> q = cb.createQuery(TagAndCount.class);
        Root<PostTag> pt = q.from(PostTag.class);
        Join<PostTag, Tag> t = pt.join("tag", JoinType.INNER);
        Join<PostTag, Post> p = pt.join("post", JoinType.INNER);

        q.select(cb.construct(TagAndCount.class, t.get("name"), cb.count(p.get("id"))));
        q.groupBy(t.get("id"));

        List<Predicate> predicates = new ArrayList<>();
        ParameterExpression<String> tagMask = null;
        if (query != null && !query.isEmpty()) {
            tagMask = cb.parameter(String.class);
            predicates.add(cb.like(cb.lower(t.get("name")), tagMask));
        }
        predicates.add(cb.equal(p.get("isActive"), 1));
        predicates.add(cb.equal(p.get("moderationStatus"), ModerationStatus.ACCEPTED));
        ParameterExpression<Date> timeThreshold = cb.parameter(Date.class);
        predicates.add(cb.lessThanOrEqualTo(p.get("time"), timeThreshold));
        q.where(predicates.toArray(new Predicate[0]));

        TypedQuery<TagAndCount> typedQuery = entityManager.createQuery(q);
        if (tagMask != null) {
            typedQuery.setParameter(tagMask, query.toLowerCase(Locale.ROOT) + "%");
        }
        typedQuery.setParameter(timeThreshold, new Date(), TemporalType.TIMESTAMP);
        return typedQuery.getResultList();
    }
}
