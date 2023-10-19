package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class AirplaneDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer seats;

    @NotNull
    private AIRPLANESTYPE type;

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

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(final Integer seats) {
        this.seats = seats;
    }

    public AIRPLANESTYPE getType() {
        return type;
    }

    public void setType(final AIRPLANESTYPE type) {
        this.type = type;
    }

}
