package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.domain.Region;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.UNITSTATUS;
import com.yonder.pilot_dispatch.model.UnitDTO;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.repos.RegionRepository;
import com.yonder.pilot_dispatch.repos.UnitRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import com.yonder.pilot_dispatch.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final RegionRepository regionRepository;
    private final OperationRepository operationRepository;

    public UnitService(final UnitRepository unitRepository, final RegionRepository regionRepository,
            final OperationRepository operationRepository) {
        this.unitRepository = unitRepository;
        this.regionRepository = regionRepository;
        this.operationRepository = operationRepository;
    }

    public List<UnitDTO> findAll() {
        final List<Unit> units = unitRepository.findAll(Sort.by("id"));
        return units.stream()
                .map(unit -> mapToDTO(unit, new UnitDTO()))
                .toList();
    }

    public UnitDTO get(final Long id) {
        return unitRepository.findById(id)
                .map(unit -> mapToDTO(unit, new UnitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UnitDTO unitDTO) {
        final Unit unit = new Unit();
        mapToEntity(unitDTO, unit);
        return unitRepository.save(unit).getId();
    }

    public void update(final Long id, final UnitDTO unitDTO) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(unitDTO, unit);
        unitRepository.save(unit);
    }

    public void delete(final Long id) {
        unitRepository.deleteById(id);
    }

    private UnitDTO mapToDTO(final Unit unit, final UnitDTO unitDTO) {
        unitDTO.setId(unit.getId());
        unitDTO.setName(unit.getName());
        unitDTO.setTeamCapacity(unit.getTeamCapacity());
        unitDTO.setStatus(unit.getStatus());
        unitDTO.setRegion(unit.getRegion() == null ? null : unit.getRegion().getId());
        return unitDTO;
    }

    private Unit mapToEntity(final UnitDTO unitDTO, final Unit unit) {
        unit.setName(unitDTO.getName());
        unit.setTeamCapacity(unitDTO.getTeamCapacity());
        unit.setStatus(unitDTO.getStatus());
        final Region region = unitDTO.getRegion() == null ? null : regionRepository.findById(unitDTO.getRegion())
                .orElseThrow(() -> new NotFoundException("region not found"));
        unit.setRegion(region);
        return unit;
    }

    public String getReferencedWarning(final Long id) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Operation unitOperation = operationRepository.findFirstByUnit(unit);
        if (unitOperation != null) {
            return WebUtils.getMessage("unit.operation.unit.referenced", unitOperation.getId());
        }
        return null;
    }

}
