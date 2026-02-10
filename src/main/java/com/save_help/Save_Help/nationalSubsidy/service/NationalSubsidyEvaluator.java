package com.save_help.Save_Help.nationalSubsidy.service;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NationalSubsidyEvaluator {

    public boolean isApplyNationalSubsidy(User user, NationalSubsidy s, LocalDate today) {

        if (!s.isActive()) return false;
        if (s.getStartDate() != null && today.isBefore(s.getStartDate())) return false;
        if (s.getEndDate() != null && today.isAfter(s.getEndDate())) return false;

        Integer minAge = s.getMinAge();
        Integer maxAge = s.getMaxAge();
        int age = user.getAge();
        if (minAge != null && age < minAge) return false;
        if (maxAge != null && age > maxAge) return false;

        if (s.getIncomeLevel() != null && !s.getIncomeLevel().isBlank()) {
            if (user.getIncomeLevel() == null) return false;
            if (!s.getIncomeLevel().equalsIgnoreCase(user.getIncomeLevel())) return false;
        }

        if (Boolean.TRUE.equals(s.getDisabilityRequired()) && !user.isDisabled()) return false;
        if (Boolean.TRUE.equals(s.getEmergencyOnly()) && !user.isInEmergency()) return false;


        return true;
    }
}
