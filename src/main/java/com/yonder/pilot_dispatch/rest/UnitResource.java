package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.UnitDTO;
import com.yonder.pilot_dispatch.service.UnitService;
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
@RequestMapping(value = "/api/units", produces = MediaType.APPLICATION_JSON_VALUE)
public class UnitResource {

    private final UnitService unitService;

    public UnitResource(final UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitDTO> getUnit(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(unitService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUnit(@RequestBody @Valid final UnitDTO unitDTO) {
        final Long createdId = unitService.create(unitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUnit(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UnitDTO unitDTO) {
        unitService.update(id, unitDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUnit(@PathVariable(name = "id") final Long id) {
        unitService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
