package com.yonder.pilot_dispatch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;


public class PilotDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private RANKTYPE rank;

    @NotNull
    private Integer age;

    @NotNull
    private Integer flightsCount;

    private String missionName;

    private List<Long> mission;

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

    public RANKTYPE getRank() {
        return rank;
    }

    public void setRank(final RANKTYPE rank) {
        this.rank = rank;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Integer getFlightsCount() {
        return flightsCount;
    }

    public void setFlightsCount(final Integer flightsCount) {
        this.flightsCount = flightsCount;
    }

    public List<Long> getMission() {
        return mission;
    }

    public void setMission(final List<Long> mission) {
        this.mission = mission;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }
}
