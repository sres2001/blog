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
    public List<TagAndCount> getTagsAndPostsCounts(String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagAndCount> q = cb.createQuery(TagAndCount.class);
        Root<PostTag> pt = q.from(PostTag.class);
        Join<PostTag, Tag> t = pt.join("tag", JoinType.INNER);
        Join<PostTag, Post> p = pt.join("post", JoinType.INNER);

        q.select(cb.construct(TagAndCount.class, t.get("name"), cb.count(p.get("id"))));
        q.groupBy(t.get("id"));

        List<Predicate> predicates = new ArrayList<>();
        ParameterExpression<String> tagParameter = null;
        if (query != null && !query.isEmpty()) {
            tagParameter = cb.parameter(String.class);
            predicates.add(cb.like(cb.lower(t.get("name")), tagParameter));
        }
        ParameterExpression<Byte> activeParameter = cb.parameter(Byte.class);
        predicates.add(cb.equal(p.get("active"), activeParameter));
        ParameterExpression<ModerationStatus> moderationStatusParameter = cb.parameter(ModerationStatus.class);
        predicates.add(cb.equal(p.get("moderationStatus"), moderationStatusParameter));
        ParameterExpression<Date> timeThresholdParameter = cb.parameter(Date.class);
        predicates.add(cb.lessThanOrEqualTo(p.get("time"), timeThresholdParameter));
        q.where(predicates.toArray(new Predicate[0]));

        TypedQuery<TagAndCount> typedQuery = entityManager.createQuery(q);
        if (tagParameter != null) {
            typedQuery.setParameter(tagParameter, query.toLowerCase(Locale.ROOT) + "%");
        }
        typedQuery.setParameter(activeParameter, (byte)1);
        typedQuery.setParameter(moderationStatusParameter, ModerationStatus.ACCEPTED);
        typedQuery.setParameter(timeThresholdParameter, new Date(), TemporalType.TIMESTAMP);
        return typedQuery.getResultList();
    }
}
