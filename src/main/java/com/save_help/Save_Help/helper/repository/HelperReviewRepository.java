package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HelperReviewRepository extends JpaRepository<HelperReview, Long> {

    List<HelperReview> findByHelperId(Long helperId);

    @Query("SELECT AVG(r.rating) FROM HelperReview r WHERE r.helper.id = :helperId")
    Double findAverageRatingByHelperId(Long helperId);
}