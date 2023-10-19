package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class OperationDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String departure;

    @NotNull
    @Size(max = 255)
    private String destination;

    private Long airplane;

    private String unitName;

    @NotNull
    private Long unit;

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

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(final String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public Long getAirplane() {
        return airplane;
    }

    public void setAirplane(final Long airplane) {
        this.airplane = airplane;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(final Long unit) {
        this.unit = unit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
