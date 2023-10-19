package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.AirplaneDTO;
import com.yonder.pilot_dispatch.service.AirplaneService;
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
@RequestMapping(value = "/api/airplanes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AirplaneResource {

    private final AirplaneService airplaneService;

    public AirplaneResource(final AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping
    public ResponseEntity<List<AirplaneDTO>> getAllAirplanes() {
        return ResponseEntity.ok(airplaneService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirplaneDTO> getAirplane(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(airplaneService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAirplane(@RequestBody @Valid final AirplaneDTO airplaneDTO) {
        final Long createdId = airplaneService.create(airplaneDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAirplane(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AirplaneDTO airplaneDTO) {
        airplaneService.update(id, airplaneDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAirplane(@PathVariable(name = "id") final Long id) {
        airplaneService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
