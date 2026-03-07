package com.save_help.Save_Help.dailyNecessities.spec;


import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesSearchConditionDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
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



        public static Specification<DailyNecessities> hasCenterId(Long centerId) {
            return (root, query, cb) ->
                    centerId == null ? null : cb.equal(root.get("providedBy").get("id"), centerId);
        }

        public static Specification<DailyNecessities> hasCategory(DailyNecessitiesCategory category) {
            return (root, query, cb) ->
                    category == null ? null : cb.equal(root.get("category"), category);
        }

        public static Specification<DailyNecessities> hasApprovalStatus(DailyNecessities.ApprovalStatus approvalStatus) {
            return (root, query, cb) ->
                    approvalStatus == null ? null : cb.equal(root.get("approvalStatus"), approvalStatus);
        }

        public static Specification<DailyNecessities> isActive(Boolean active) {
            return (root, query, cb) ->
                    active == null ? null : cb.equal(root.get("active"), active);
        }

        public static Specification<DailyNecessities> containsKeyword(String keyword) {
            return (root, query, cb) -> {
                if (keyword == null || keyword.isBlank()) return null;
                String pattern = "%" + keyword.toLowerCase() + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("unit")), pattern)
                );
            };
        }

}