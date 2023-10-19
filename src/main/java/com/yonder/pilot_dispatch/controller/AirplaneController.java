package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.model.AIRPLANESTYPE;
import com.yonder.pilot_dispatch.model.AirplaneDTO;
import com.yonder.pilot_dispatch.service.AirplaneService;
import com.yonder.pilot_dispatch.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(final AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", AIRPLANESTYPE.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("airplanes", airplaneService.findAll());
        return "airplane/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("airplane") final AirplaneDTO airplaneDTO) {
        return "airplane/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("airplane") @Valid final AirplaneDTO airplaneDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && airplaneService.nameExists(airplaneDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.airplane.name");
        }
        if (bindingResult.hasErrors()) {
            return "airplane/add";
        }
        airplaneService.create(airplaneDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("airplane.create.success"));
        return "redirect:/airplanes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("airplane", airplaneService.get(id));
        return "airplane/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("airplane") @Valid final AirplaneDTO airplaneDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final AirplaneDTO currentAirplaneDTO = airplaneService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !airplaneDTO.getName().equalsIgnoreCase(currentAirplaneDTO.getName()) &&
                airplaneService.nameExists(airplaneDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.airplane.name");
        }
        if (bindingResult.hasErrors()) {
            return "airplane/edit";
        }
        airplaneService.update(id, airplaneDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("airplane.update.success"));
        return "redirect:/airplanes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = airplaneService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            airplaneService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("airplane.delete.success"));
        }
        return "redirect:/airplanes";
    }

}
