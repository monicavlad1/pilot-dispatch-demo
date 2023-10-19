package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

    boolean existsByNameIgnoreCase(String name);

}
