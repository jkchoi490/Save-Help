package com.save_help.Save_Help.dailyNecessities.spec;


import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesSearchConditionDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public final class DailyNecessitiesSpecs {
    private DailyNecessitiesSpecs() {}

    public static Specification<DailyNecessities> byCondition(DailyNecessitiesSearchConditionDto c) {
        return (root, query, cb) -> {
            if (c == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            // centerId
            predicates.add(eqIfNotNull(cb, root.get("providedBy").get("id"), c.getCenterId()));
            // category
            predicates.add(eqIfNotNull(cb, root.get("category"), c.getCategory()));
            // approvalStatus
            predicates.add(eqIfNotNull(cb, root.get("approvalStatus"), c.getApprovalStatus()));
            // active
            predicates.add(eqIfNotNull(cb, root.get("active"), c.getActive()));

            // stock range (선택)
            predicates.add(gteIfNotNull(cb, root.get("stock"), c.getStockGte()));
            predicates.add(lteIfNotNull(cb, root.get("stock"), c.getStockLte()));

            Predicate nameLike = likeIgnoreCaseIfNotBlank(cb, root.get("name"), c.getKeyword());
            predicates.add(nameLike);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Predicate eqIfNotNull(CriteriaBuilder cb, Path<?> path, Object value) {
        return value == null ? cb.conjunction() : cb.equal(path, value);
    }

    public static Predicate likeIgnoreCaseIfNotBlank(CriteriaBuilder cb, Path<String> path, String keyword) {
        if (keyword == null) return cb.conjunction();
        String k = keyword.trim();
        if (k.isEmpty()) return cb.conjunction();
        return cb.like(cb.lower(path), "%" + k.toLowerCase() + "%");
    }

    public static Predicate gteIfNotNull(CriteriaBuilder cb, Path<Integer> path, Integer value) {
        return value == null ? cb.conjunction() : cb.greaterThanOrEqualTo(path, value);
    }

    public static Predicate lteIfNotNull(CriteriaBuilder cb, Path<Integer> path, Integer value) {
        return value == null ? cb.conjunction() : cb.lessThanOrEqualTo(path, value);
    }
}