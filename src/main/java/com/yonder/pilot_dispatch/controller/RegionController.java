package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.model.RegionDTO;
import com.yonder.pilot_dispatch.model.WARZONES;
import com.yonder.pilot_dispatch.service.RegionService;
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
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(final RegionService regionService) {
        this.regionService = regionService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("nameValues", WARZONES.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("regions", regionService.findAll());
        return "region/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("region") final RegionDTO regionDTO) {
        return "region/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("region") @Valid final RegionDTO regionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "region/add";
        }
        regionService.create(regionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("region.create.success"));
        return "redirect:/regions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("region", regionService.get(id));
        return "region/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("region") @Valid final RegionDTO regionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "region/edit";
        }
        regionService.update(id, regionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("region.update.success"));
        return "redirect:/regions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = regionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            regionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("region.delete.success"));
        }
        return "redirect:/regions";
    }

}
