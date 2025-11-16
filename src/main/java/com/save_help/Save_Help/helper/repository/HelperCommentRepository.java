package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperCommentRepository extends JpaRepository<HelperComment, Long> {
    List<HelperComment> findByPost_IdOrderByCreatedAtAsc(Long postId);
}