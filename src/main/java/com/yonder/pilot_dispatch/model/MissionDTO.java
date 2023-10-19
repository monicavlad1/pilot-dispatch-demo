package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class MissionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Long operations;

    private String operationName;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getOperations() {
        return operations;
    }

    public void setOperations(final Long operations) {
        this.operations = operations;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
