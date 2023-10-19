package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Airplane;
import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.OperationDTO;
import com.yonder.pilot_dispatch.repos.AirplaneRepository;
import com.yonder.pilot_dispatch.repos.MissionRepository;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.repos.UnitRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import com.yonder.pilot_dispatch.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final AirplaneRepository airplaneRepository;
    private final UnitRepository unitRepository;
    private final MissionRepository missionRepository;

    public OperationService(final OperationRepository operationRepository,
            final AirplaneRepository airplaneRepository, final UnitRepository unitRepository,
            final MissionRepository missionRepository) {
        this.operationRepository = operationRepository;
        this.airplaneRepository = airplaneRepository;
        this.unitRepository = unitRepository;
        this.missionRepository = missionRepository;
    }

    public List<OperationDTO> findAll() {
        final List<Operation> operations = operationRepository.findAll(Sort.by("id"));
        return operations.stream()
                .map(operation -> mapToDTO(operation, new OperationDTO()))
                .toList();
    }

    public OperationDTO get(final Long id) {
        return operationRepository.findById(id)
                .map(operation -> mapToDTO(operation, new OperationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OperationDTO operationDTO) {
        final Operation operation = new Operation();
        mapToEntity(operationDTO, operation);
        return operationRepository.save(operation).getId();
    }

    public void update(final Long id, final OperationDTO operationDTO) {
        final Operation operation = operationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(operationDTO, operation);
        operationRepository.save(operation);
    }

    public void delete(final Long id) {
        operationRepository.deleteById(id);
    }

    private OperationDTO mapToDTO(final Operation operation, final OperationDTO operationDTO) {
        operationDTO.setId(operation.getId());
        operationDTO.setName(operation.getName());
        operationDTO.setDeparture(operation.getDeparture());
        operationDTO.setDestination(operation.getDestination());
        operationDTO.setAirplane(operation.getAirplane() == null ? null : operation.getAirplane().getId());
        operationDTO.setUnit(operation.getUnit() == null ? null : operation.getUnit().getId());
        return operationDTO;
    }

    private Operation mapToEntity(final OperationDTO operationDTO, final Operation operation) {
        operation.setName(operationDTO.getName());
        operation.setDeparture(operationDTO.getDeparture());
        operation.setDestination(operationDTO.getDestination());
        final Airplane airplane = operationDTO.getAirplane() == null ? null : airplaneRepository.findById(operationDTO.getAirplane())
                .orElseThrow(() -> new NotFoundException("airplane not found"));
        operation.setAirplane(airplane);
        final Unit unit = operationDTO.getUnit() == null ? null : unitRepository.findById(operationDTO.getUnit())
                .orElseThrow(() -> new NotFoundException("unit not found"));
        operation.setUnit(unit);
        return operation;
    }

    public boolean nameExists(final String name) {
        return operationRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final Operation operation = operationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Mission operationsMission = missionRepository.findFirstByOperations(operation);
        if (operationsMission != null) {
            return WebUtils.getMessage("operation.mission.operations.referenced", operationsMission.getId());
        }
        return null;
    }

}
