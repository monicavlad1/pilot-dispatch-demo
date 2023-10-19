package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;


public class RegionDTO {

    private Long id;

    @NotNull
    private WARZONES name;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public WARZONES getName() {
        return name;
    }

    public void setName(final WARZONES name) {
        this.name = name;
    }

}
