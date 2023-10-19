package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Airplane;
import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.model.AirplaneDTO;
import com.yonder.pilot_dispatch.repos.AirplaneRepository;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import com.yonder.pilot_dispatch.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;
    private final OperationRepository operationRepository;

    public AirplaneService(final AirplaneRepository airplaneRepository,
            final OperationRepository operationRepository) {
        this.airplaneRepository = airplaneRepository;
        this.operationRepository = operationRepository;
    }

    public List<AirplaneDTO> findAll() {
        final List<Airplane> airplanes = airplaneRepository.findAll(Sort.by("id"));
        return airplanes.stream()
                .map(airplane -> mapToDTO(airplane, new AirplaneDTO()))
                .toList();
    }

    public AirplaneDTO get(final Long id) {
        return airplaneRepository.findById(id)
                .map(airplane -> mapToDTO(airplane, new AirplaneDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AirplaneDTO airplaneDTO) {
        final Airplane airplane = new Airplane();
        mapToEntity(airplaneDTO, airplane);
        return airplaneRepository.save(airplane).getId();
    }

    public void update(final Long id, final AirplaneDTO airplaneDTO) {
        final Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(airplaneDTO, airplane);
        airplaneRepository.save(airplane);
    }

    public void delete(final Long id) {
        airplaneRepository.deleteById(id);
    }

    private AirplaneDTO mapToDTO(final Airplane airplane, final AirplaneDTO airplaneDTO) {
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setName(airplane.getName());
        airplaneDTO.setSeats(airplane.getSeats());
        airplaneDTO.setType(airplane.getType());
        return airplaneDTO;
    }

    private Airplane mapToEntity(final AirplaneDTO airplaneDTO, final Airplane airplane) {
        airplane.setName(airplaneDTO.getName());
        airplane.setSeats(airplaneDTO.getSeats());
        airplane.setType(airplaneDTO.getType());
        return airplane;
    }

    public boolean nameExists(final String name) {
        return airplaneRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Operation airplaneOperation = operationRepository.findFirstByAirplane(airplane);
        if (airplaneOperation != null) {
            return WebUtils.getMessage("airplane.operation.airplane.referenced", airplaneOperation.getId());
        }
        return null;
    }

}
