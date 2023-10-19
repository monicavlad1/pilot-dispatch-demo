package com.yonder.pilot_dispatch.service;

import com.yonder.pilot_dispatch.domain.Region;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.RegionDTO;
import com.yonder.pilot_dispatch.repos.RegionRepository;
import com.yonder.pilot_dispatch.repos.UnitRepository;
import com.yonder.pilot_dispatch.util.NotFoundException;
import com.yonder.pilot_dispatch.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final UnitRepository unitRepository;

    public RegionService(final RegionRepository regionRepository,
            final UnitRepository unitRepository) {
        this.regionRepository = regionRepository;
        this.unitRepository = unitRepository;
    }

    public List<RegionDTO> findAll() {
        final List<Region> regions = regionRepository.findAll(Sort.by("id"));
        return regions.stream()
                .map(region -> mapToDTO(region, new RegionDTO()))
                .toList();
    }

    public RegionDTO get(final Long id) {
        return regionRepository.findById(id)
                .map(region -> mapToDTO(region, new RegionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RegionDTO regionDTO) {
        final Region region = new Region();
        mapToEntity(regionDTO, region);
        return regionRepository.save(region).getId();
    }

    public void update(final Long id, final RegionDTO regionDTO) {
        final Region region = regionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(regionDTO, region);
        regionRepository.save(region);
    }

    public void delete(final Long id) {
        regionRepository.deleteById(id);
    }

    private RegionDTO mapToDTO(final Region region, final RegionDTO regionDTO) {
        regionDTO.setId(region.getId());
        regionDTO.setName(region.getName());
        return regionDTO;
    }

    private Region mapToEntity(final RegionDTO regionDTO, final Region region) {
        region.setName(regionDTO.getName());
        return region;
    }

    public String getReferencedWarning(final Long id) {
        final Region region = regionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Unit regionUnit = unitRepository.findFirstByRegion(region);
        if (regionUnit != null) {
            return WebUtils.getMessage("region.unit.region.referenced", regionUnit.getId());
        }
        return null;
    }

}
