package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.domain.Pilot;
import com.yonder.pilot_dispatch.model.PilotDTO;
import com.yonder.pilot_dispatch.repos.MissionRepository;
import com.yonder.pilot_dispatch.repos.PilotRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class PilotService {

    private final PilotRepository pilotRepository;
    private final MissionRepository missionRepository;

    public PilotService(final PilotRepository pilotRepository,
            final MissionRepository missionRepository) {
        this.pilotRepository = pilotRepository;
        this.missionRepository = missionRepository;
    }

    public List<PilotDTO> findAll() {
        final List<Pilot> pilots = pilotRepository.findAll(Sort.by("id"));
        return pilots.stream()
                .map(pilot -> mapToDTO(pilot, new PilotDTO()))
                .toList();
    }

    public PilotDTO get(final Long id) {
        return pilotRepository.findById(id)
                .map(pilot -> mapToDTO(pilot, new PilotDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PilotDTO pilotDTO) {
        final Pilot pilot = new Pilot();
        mapToEntity(pilotDTO, pilot);
        return pilotRepository.save(pilot).getId();
    }

    public void update(final Long id, final PilotDTO pilotDTO) {
        final Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pilotDTO, pilot);
        pilotRepository.save(pilot);
    }

    public void delete(final Long id) {
        pilotRepository.deleteById(id);
    }

    private PilotDTO mapToDTO(final Pilot pilot, final PilotDTO pilotDTO) {
        pilotDTO.setId(pilot.getId());
        pilotDTO.setName(pilot.getName());
        pilotDTO.setRank(pilot.getRank());
        pilotDTO.setAge(pilot.getAge());
        pilotDTO.setFlightsCount(pilot.getFlightsCount());
        pilotDTO.setMission(pilot.getMission().stream()
                .map(mission -> mission.getId())
                .toList());
        //create String from all mission names
        pilotDTO.setMissionName(pilot.getMission().stream()
                .map(mission -> mission.getName())
                .collect(Collectors.joining(", ")));
        return pilotDTO;
    }

    private Pilot mapToEntity(final PilotDTO pilotDTO, final Pilot pilot) {
        pilot.setName(pilotDTO.getName());
        pilot.setRank(pilotDTO.getRank());
        pilot.setAge(pilotDTO.getAge());
        pilot.setFlightsCount(pilotDTO.getFlightsCount());
        final List<Mission> mission = missionRepository.findAllById(
                pilotDTO.getMission() == null ? Collections.emptyList() : pilotDTO.getMission());
        if (mission.size() != (pilotDTO.getMission() == null ? 0 : pilotDTO.getMission().size())) {
            throw new NotFoundException("one of mission not found");
        }
        pilot.setMission(mission.stream().collect(Collectors.toSet()));
        return pilot;
    }

}
