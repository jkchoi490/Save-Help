package com.save_help.Save_Help.emergency.filter;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EmergencySpecs {

    public static Specification<Emergency> status(EmergencyStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Emergency> excludeCancelled(Boolean exclude) {
        return (root, query, cb) -> (exclude != null && exclude)
                ? cb.notEqual(root.get("status"), EmergencyStatus.CANCELLED)
                : null;
    }

    public static Specification<Emergency> unresolvedOnly(Boolean unresolvedOnly) {
        // "미해결만" = RESOLVED 제외 + CANCELLED 제외
        return (root, query, cb) -> (unresolvedOnly != null && unresolvedOnly)
                ? cb.and(
                cb.notEqual(root.get("status"), EmergencyStatus.RESOLVED),
                cb.notEqual(root.get("status"), EmergencyStatus.CANCELLED)
        )
                : null;
    }

    public static Specification<Emergency> requestedWithinHours(Integer hours) {
        return (root, query, cb) -> {
            if (hours == null) return null;
            LocalDateTime from = LocalDateTime.now().minusHours(hours);
            return cb.greaterThanOrEqualTo(root.get("requestedAt"), from);
        };
    }
}
