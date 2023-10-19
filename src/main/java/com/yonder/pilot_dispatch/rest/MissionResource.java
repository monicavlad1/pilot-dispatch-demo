package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.MissionDTO;
import com.yonder.pilot_dispatch.service.MissionService;
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
@RequestMapping(value = "/api/missions", produces = MediaType.APPLICATION_JSON_VALUE)
public class MissionResource {

    private final MissionService missionService;

    public MissionResource(final MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public ResponseEntity<List<MissionDTO>> getAllMissions() {
        return ResponseEntity.ok(missionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getMission(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(missionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMission(@RequestBody @Valid final MissionDTO missionDTO) {
        final Long createdId = missionService.create(missionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMission(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MissionDTO missionDTO) {
        missionService.update(id, missionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMission(@PathVariable(name = "id") final Long id) {
        missionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
