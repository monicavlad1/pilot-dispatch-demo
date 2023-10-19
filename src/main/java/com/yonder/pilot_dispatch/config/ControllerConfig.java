package com.yonder.pilot_dispatch.config;

import com.yonder.pilot_dispatch.model.*;
import com.yonder.pilot_dispatch.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@ControllerAdvice
public class ControllerConfig {

    @InitBinder
    void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private RegionService regionService;
    private UnitService unitService;
    private AirplaneService airplaneService;
    private PilotService pilotService;
    private OperationService operationService;
    private MissionService missionService;

    public ControllerConfig(final RegionService regionService,
                            final UnitService unitService,
                            final AirplaneService airplaneService,
                            final PilotService pilotService,
                            final OperationService operationService,
                            final MissionService missionService) {
        this.regionService = regionService;
        this.unitService = unitService;
        this.airplaneService = airplaneService;
        this.pilotService = pilotService;
        this.operationService = operationService;
        this.missionService = missionService;
    }

    //Fill default values in database
    @PostConstruct
    public void init() {
        createDefaultRegions();
        createDefaultUnits();
        createDefaultAirplanes();
        createDefaultOperations();
        createDefaultMissions();
        createDefaultPilots();
    }

    private void createDefaultRegions() {
        //iterate through WARZONES enum and create Regions in database
        for (WARZONES warzone : WARZONES.values()) {
            //Create RegionDTO  object
            RegionDTO regionDTO = new RegionDTO();
            //Set region name
            regionDTO.setName(warzone);
            //Insert default region values
            regionService.create(regionDTO);
        }
    }

    //create default units
    private void createDefaultUnits() {
        //iterate through regions and create units in database
        for (RegionDTO region : regionService.findAll()) {
            //Create UnitDTO object
            UnitDTO unitDTO = new UnitDTO();
            //Set unit name
            unitDTO.setName(region.getName().toString());
            //Set region
            unitDTO.setRegion(region.getId());
            unitDTO.setStatus(UNITSTATUS.ON_HOLD);
            unitDTO.setTeamCapacity(4);
            //Insert default unit values
            unitService.create(unitDTO);
        }
    }

    //create default airplanes
    private void createDefaultAirplanes() {

        //iterate through AIRPLANESTYPE enum and create airplanes in database
        for (AIRPLANESTYPE airplane : AIRPLANESTYPE.values()) {
            //Create AirplaneDTO object
            AirplaneDTO airplaneDTO = new AirplaneDTO();
            //Set airplane name
            airplaneDTO.setName(airplane.name() + " FIGHTER ");
            airplaneDTO.setType(airplane);
            airplaneDTO.setSeats(1);
            //Insert default airplane values
            airplaneService.create(airplaneDTO);
        }
    }

    //create default operations
    private void createDefaultOperations() {
        //create list with 2 random names
        List<String> randomNames = Arrays.asList("MIDDLE EAST STABILITY", "EASTERN EUROPE STABILITY");
        //iterate through names list and create operations in database
        for (String name : randomNames) {
            //Create OperationDTO object
            OperationDTO operationDTO = new OperationDTO();
            //Set operation name
            operationDTO.setName(name);
            //Create OperationDTO object
            operationDTO.setDeparture("CLUJ");
            operationDTO.setDestination("TOP SECRET");

            //if there are units in the database set random unit to operation
            List<UnitDTO> allUnits = unitService.findAll();
            UnitDTO unitDTO = allUnits.get(new Random().nextInt(0, allUnits.size() - 1));
            operationDTO.setUnit(unitDTO.getId());
            operationDTO.setUnitName(unitDTO.getName());

            //if there are airplanes in the database set random airplane to operation
            List<AirplaneDTO> allAirplanes = airplaneService.findAll();
            operationDTO.setAirplane(allAirplanes.get(new Random().nextInt(0, allAirplanes.size() - 1)).getId());

            //Insert default operation values
            operationService.create(operationDTO);
        }
    }

    //create default missions
    private void createDefaultMissions() {
        //iterate through operations and create missions in database
        for (OperationDTO operation : operationService.findAll()) {
            //Create MissionDTO object
            MissionDTO missionDTO = new MissionDTO();
            //Set mission name
            missionDTO.setName(operation.getName() + " MISSION");
            //Set operation
            missionDTO.setOperations(operation.getId());
            //set operation name
            missionDTO.setOperationName(operation.getName());
            //Insert default mission values
            missionService.create(missionDTO);
        }
    }

    //create default pilots
    private void createDefaultPilots() {
        //create list with 20 random names
        List<String> randomNames = Arrays.asList("Maverick", "Rooster");
        //iterate through  names list and create pilots in database
        for (String name : randomNames) {
            //Create PilotDTO object
            PilotDTO pilotDTO = new PilotDTO();
            //Set pilot name
            pilotDTO.setName(name);
            // set random RANKTYPE to pilot
            pilotDTO.setRank(RANKTYPE.values()[new Random().nextInt(0,RANKTYPE.values().length)]);
            pilotDTO.setAge(new Random().nextInt(20, 30));
            //set random flights count between 20 and 100
            pilotDTO.setFlightsCount(new Random().nextInt(20, 100));
            //find all missions in database
            List<MissionDTO> missions = missionService.findAll();
            //set random mission to pilot
            pilotDTO.setMission(Arrays.asList(missions.get(new Random().nextInt(missions.size())).getId()));
            //Insert default pilot values
            pilotService.create(pilotDTO);
        }

    }

}
