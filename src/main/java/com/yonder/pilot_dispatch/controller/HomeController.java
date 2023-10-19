package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.model.*;
import com.yonder.pilot_dispatch.repos.RegionRepository;
import com.yonder.pilot_dispatch.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Controller
public class HomeController {


    @GetMapping("/")
    public String index() {
        return "home/index";
    }

}
