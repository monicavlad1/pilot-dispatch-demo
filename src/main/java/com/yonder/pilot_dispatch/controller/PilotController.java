package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.domain.Mission;
import com.yonder.pilot_dispatch.model.PilotDTO;
import com.yonder.pilot_dispatch.model.RANKTYPE;
import com.yonder.pilot_dispatch.repos.MissionRepository;
import com.yonder.pilot_dispatch.service.PilotService;
import com.yonder.pilot_dispatch.util.CustomCollectors;
import com.yonder.pilot_dispatch.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/pilots")
public class PilotController {

    private final PilotService pilotService;
    private final MissionRepository missionRepository;

    public PilotController(final PilotService pilotService,
            final MissionRepository missionRepository) {
        this.pilotService = pilotService;
        this.missionRepository = missionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("rankValues", RANKTYPE.values());
        model.addAttribute("missionValues", missionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Mission::getId, Mission::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("pilots", pilotService.findAll());
        return "pilot/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("pilot") final PilotDTO pilotDTO) {
        return "pilot/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("pilot") @Valid final PilotDTO pilotDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pilot/add";
        }
        pilotService.create(pilotDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pilot.create.success"));
        return "redirect:/pilots";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("pilot", pilotService.get(id));
        return "pilot/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("pilot") @Valid final PilotDTO pilotDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pilot/edit";
        }
        pilotService.update(id, pilotDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pilot.update.success"));
        return "redirect:/pilots";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        pilotService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("pilot.delete.success"));
        return "redirect:/pilots";
    }

}
