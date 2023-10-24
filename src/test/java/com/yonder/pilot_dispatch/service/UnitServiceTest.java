package com.yonder.pilot_dispatch.service;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.UNITSTATUS;
import com.yonder.pilot_dispatch.model.UnitDTO;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.repos.RegionRepository;
import com.yonder.pilot_dispatch.repos.UnitRepository;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;

import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private OperationRepository operationRepository;
    @Spy
    @InjectMocks
    private UnitService unitService;



}