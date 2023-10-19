package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MissionRepository extends JpaRepository<Mission, Long> {

    Mission findFirstByOperations(Operation operation);

}
