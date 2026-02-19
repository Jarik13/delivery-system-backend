package org.deliverysystem.com.utils;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationUtils {
    private static <S> Specification<S> empty() {
        return (root, query, cb) -> null;
    }

    public static <S> Specification<S> iLike(String field, Object value) {
        if (value == null) {
            return empty();
        }

        String stringValue = String.valueOf(value).trim();

        if (stringValue.isEmpty()) {
            return empty();
        }

        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, field).as(String.class)), "%" + stringValue.toLowerCase() + "%");
    }

    public static <S, T extends Comparable<? super T>> Specification<S> gte(String field, T value) {
        if (value == null) {
            return empty();
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(getPath(root, field), value);
    }

    public static <S, T extends Comparable<? super T>> Specification<S> lte(String field, T value) {
        if (value == null) {
            return empty();
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(getPath(root, field), value);
    }

    public static <S> Specification<S> equal(String field, Object value) {
        if (value == null) {
            return empty();
        }
        return (root, query, cb) -> cb.equal(getPath(root, field), value);
    }

    private static <Y, S> Path<Y> getPath(Root<S> root, String attributeName) {
        Path<Y> path = (Path<Y>) root;
        if (attributeName.contains(".")) {
            for (String part : attributeName.split("\\.")) {
                path = path.get(part);
            }
        } else {
            path = path.get(attributeName);
        }
        return path;
    }
}