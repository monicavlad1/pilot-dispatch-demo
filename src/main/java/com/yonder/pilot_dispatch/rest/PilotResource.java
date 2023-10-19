package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.PilotDTO;
import com.yonder.pilot_dispatch.service.PilotService;
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
@RequestMapping(value = "/api/pilots", produces = MediaType.APPLICATION_JSON_VALUE)
public class PilotResource {

    private final PilotService pilotService;

    public PilotResource(final PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping
    public ResponseEntity<List<PilotDTO>> getAllPilots() {
        return ResponseEntity.ok(pilotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PilotDTO> getPilot(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(pilotService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPilot(@RequestBody @Valid final PilotDTO pilotDTO) {
        final Long createdId = pilotService.create(pilotDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePilot(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PilotDTO pilotDTO) {
        pilotService.update(id, pilotDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePilot(@PathVariable(name = "id") final Long id) {
        pilotService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
