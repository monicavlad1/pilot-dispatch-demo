package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegionRepository extends JpaRepository<Region, Long> {
}
