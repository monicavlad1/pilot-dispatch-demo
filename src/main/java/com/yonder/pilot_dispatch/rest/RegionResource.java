package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.RegionDTO;
import com.yonder.pilot_dispatch.service.RegionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/regions", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegionResource {

    private final RegionService regionService;

    public RegionResource(final RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(regionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRegion(@RequestBody @Valid final RegionDTO regionDTO) {
        final Long createdId = regionService.create(regionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRegion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RegionDTO regionDTO) {
        regionService.update(id, regionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRegion(@PathVariable(name = "id") final Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
