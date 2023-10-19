package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.domain.Operation;
import com.yonder.pilot_dispatch.model.MissionDTO;
import com.yonder.pilot_dispatch.repos.OperationRepository;
import com.yonder.pilot_dispatch.service.MissionService;
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
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private final OperationRepository operationRepository;

    public MissionController(final MissionService missionService,
            final OperationRepository operationRepository) {
        this.missionService = missionService;
        this.operationRepository = operationRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("operationsValues", operationRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Operation::getId, Operation::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("missions", missionService.findAll());
        return "mission/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("mission") final MissionDTO missionDTO) {
        return "mission/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("mission") @Valid final MissionDTO missionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "mission/add";
        }
        missionService.create(missionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("mission.create.success"));
        return "redirect:/missions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("mission", missionService.get(id));
        return "mission/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("mission") @Valid final MissionDTO missionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "mission/edit";
        }
        missionService.update(id, missionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("mission.update.success"));
        return "redirect:/missions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = missionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            missionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("mission.delete.success"));
        }
        return "redirect:/missions";
    }

}
