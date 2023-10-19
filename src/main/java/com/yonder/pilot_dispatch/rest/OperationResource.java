package com.yonder.pilot_dispatch.rest;

import com.yonder.pilot_dispatch.model.OperationDTO;
import com.yonder.pilot_dispatch.service.OperationService;
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
@RequestMapping(value = "/api/operations", produces = MediaType.APPLICATION_JSON_VALUE)
public class OperationResource {

    private final OperationService operationService;

    public OperationResource(final OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public ResponseEntity<List<OperationDTO>> getAllOperations() {
        return ResponseEntity.ok(operationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> getOperation(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(operationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOperation(
            @RequestBody @Valid final OperationDTO operationDTO) {
        final Long createdId = operationService.create(operationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOperation(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OperationDTO operationDTO) {
        operationService.update(id, operationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOperation(@PathVariable(name = "id") final Long id) {
        operationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
