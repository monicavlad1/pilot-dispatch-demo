package com.yonder.pilot_dispatch.repos;

import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.domain.Pilot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PilotRepository extends JpaRepository<Pilot, Long> {

    Pilot findFirstByMission(Mission mission);

    List<Pilot> findAllByMission(Mission mission);

}
