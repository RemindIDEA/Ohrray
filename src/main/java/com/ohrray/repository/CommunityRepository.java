package com.ohrray.repository;

import com.ohrray.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("select c,m from Community c join Mountain m on c.mountain.id = m.id where c.mountain.id=:id")
    Community findByMountainId(@Param("id") Long id);
}
