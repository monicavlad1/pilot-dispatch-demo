package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.domain.Region;
import com.yonder.pilot_dispatch.model.UNITSTATUS;
import com.yonder.pilot_dispatch.model.UnitDTO;
import com.yonder.pilot_dispatch.repos.RegionRepository;
import com.yonder.pilot_dispatch.service.UnitService;
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
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final RegionRepository regionRepository;

    public UnitController(final UnitService unitService, final RegionRepository regionRepository) {
        this.unitService = unitService;
        this.regionRepository = regionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", UNITSTATUS.values());
        model.addAttribute("regionValues", regionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Region::getId, Region::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("units", unitService.findAll());
        return "unit/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("unit") final UnitDTO unitDTO) {
        return "unit/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("unit") @Valid final UnitDTO unitDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "unit/add";
        }
        unitService.create(unitDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("unit.create.success"));
        return "redirect:/units";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("unit", unitService.get(id));
        return "unit/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("unit") @Valid final UnitDTO unitDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "unit/edit";
        }
        unitService.update(id, unitDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("unit.update.success"));
        return "redirect:/units";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = unitService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            unitService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("unit.delete.success"));
        }
        return "redirect:/units";
    }

}
