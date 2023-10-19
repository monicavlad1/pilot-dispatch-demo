package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.domain.Pilot;
import com.yonder.pilot_dispatch.model.MissionDTO;
import com.yonder.pilot_dispatch.repos.MissionRepository;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.repos.PilotRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import com.yonder.pilot_dispatch.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;
    private final OperationRepository operationRepository;
    private final PilotRepository pilotRepository;

    public MissionService(final MissionRepository missionRepository,
            final OperationRepository operationRepository, final PilotRepository pilotRepository) {
        this.missionRepository = missionRepository;
        this.operationRepository = operationRepository;
        this.pilotRepository = pilotRepository;
    }

    public List<MissionDTO> findAll() {
        final List<Mission> missions = missionRepository.findAll(Sort.by("id"));
        return missions.stream()
                .map(mission -> mapToDTO(mission, new MissionDTO()))
                .toList();
    }

    public MissionDTO get(final Long id) {
        return missionRepository.findById(id)
                .map(mission -> mapToDTO(mission, new MissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MissionDTO missionDTO) {
        final Mission mission = new Mission();
        mapToEntity(missionDTO, mission);
        return missionRepository.save(mission).getId();
    }

    public void update(final Long id, final MissionDTO missionDTO) {
        final Mission mission = missionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(missionDTO, mission);
        missionRepository.save(mission);
    }

    public void delete(final Long id) {
        final Mission mission = missionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        pilotRepository.findAllByMission(mission)
                .forEach(pilot -> pilot.getMission().remove(mission));
        missionRepository.delete(mission);
    }

    private MissionDTO mapToDTO(final Mission mission, final MissionDTO missionDTO) {
        missionDTO.setId(mission.getId());
        missionDTO.setName(mission.getName());
        missionDTO.setOperations(mission.getOperations() == null ? null : mission.getOperations().getId());
        //set operation name
        missionDTO.setOperationName(mission.getOperations() == null ? null : mission.getOperations().getName());
        return missionDTO;
    }

    private Mission mapToEntity(final MissionDTO missionDTO, final Mission mission) {
        mission.setName(missionDTO.getName());
        final Operation operations = missionDTO.getOperations() == null ? null : operationRepository.findById(missionDTO.getOperations())
                .orElseThrow(() -> new NotFoundException("operations not found"));
        mission.setOperations(operations);
        return mission;
    }

    public String getReferencedWarning(final Long id) {
        final Mission mission = missionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pilot missionPilot = pilotRepository.findFirstByMission(mission);
        if (missionPilot != null) {
            return WebUtils.getMessage("mission.pilot.mission.referenced", missionPilot.getId());
        }
        return null;
    }

}
