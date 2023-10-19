package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Airplane;
import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OperationRepository extends JpaRepository<Operation, Long> {

    Operation findFirstByAirplane(Airplane airplane);

    Operation findFirstByUnit(Unit unit);

    boolean existsByNameIgnoreCase(String name);

}
