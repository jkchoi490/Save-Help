package com.save_help.Save_Help.communityCenter.repository;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import org.springframework.data.jpa.repository.JpaRepository;


import com.save_help.Save_Help.communityCenter.entity.CenterType;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCenterRepository extends JpaRepository<CommunityCenter, Long> {

    List<CommunityCenter> findByType(CenterType type);

    List<CommunityCenter> findByActiveTrue();

    List<CommunityCenter> findByTypeAndActiveTrue(CenterType type);
}
