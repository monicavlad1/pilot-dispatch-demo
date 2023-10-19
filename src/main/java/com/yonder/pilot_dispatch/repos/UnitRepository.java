package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Region;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.UNITSTATUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UnitRepository extends JpaRepository<Unit, Long> {

    Unit findFirstByRegion(Region region);

}
