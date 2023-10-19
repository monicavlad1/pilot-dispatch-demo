package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UnitDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer teamCapacity;

    @NotNull
    private UNITSTATUS status;

    private Long region;

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

    public Integer getTeamCapacity() {
        return teamCapacity;
    }

    public void setTeamCapacity(final Integer teamCapacity) {
        this.teamCapacity = teamCapacity;
    }

    public UNITSTATUS getStatus() {
        return status;
    }

    public void setStatus(final UNITSTATUS status) {
        this.status = status;
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(final Long region) {
        this.region = region;
    }

}
